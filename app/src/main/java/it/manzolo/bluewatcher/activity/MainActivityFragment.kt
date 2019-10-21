package it.manzolo.bluewatcher.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.manzolo.bluewatcher.R
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.File
import android.content.Intent
import android.os.AsyncTask
import android.preference.PreferenceManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import it.manzolo.bluewatcher.enums.BluetoothEvents
import it.manzolo.bluewatcher.utils.BluetoothClient


class MainActivityFragment : Fragment() {

    companion object {
        val TAG: String = MainActivityFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fileupdate = File(context?.cacheDir, "app.ava")
        fileupdate.delete()

        startJobService()

    }

    private fun startJobService() {
        Log.d(TAG, "startJobService")
        btTask().execute(this.context)

        //App.scheduleWatcherService(activity as Context)
        activity.run { textView.text = "Service started" }
    }

}

private class btTask : AsyncTask<Context, Void, String>() {
    override fun doInBackground(vararg args: Context): String {
        val context = args[0]

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val address = preferences.getString("device", "")
        val bluetoothDevices = address.split(",")
        for (i in 0 until bluetoothDevices.size) {
            val bluetoothDeviceAddress = bluetoothDevices[i].replace("\\s".toRegex(), "")
            loopok@ for (i in 1..5) {
                if (btConnectionRetry(context, bluetoothDeviceAddress)) {
                    Thread.sleep(1000)
                    break@loopok
                }
            }
        }
        return "OK"
    }

    fun btConnectionRetry(context: Context, addr: String): Boolean {
        try {
            val bluetoothClient = BluetoothClient(context, addr)
            bluetoothClient.retrieveData()
            Thread.sleep(500)
            //bluetoothClient.close()
            return true
        } catch (e: Exception) {
            //e.printStackTrace()
            val intent = Intent(BluetoothEvents.ERROR)
            // You can also include some extra data.
            intent.putExtra("message", e.message)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            return false
        }
    }

    @Override
    override fun onPreExecute() {
        super.onPreExecute()

    }

    @Override
    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        //Log.d(MainService.TAG, result)
    }
}