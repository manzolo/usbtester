package it.manzolo.bluewatcher.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbVoltwatcherAdapter {
    public static final String KEY_ID = "_id";
    public static final String KEY_DEVICE = "device";
    public static final String KEY_VOLT = "volts";
    public static final String KEY_TEMP = "temps";
    public static final String KEY_DATA = "data";
    public static final String KEY_LON = "longitude";
    public static final String KEY_LAT = "latitude";
    private static final String LOG_TAG = DbVoltwatcherAdapter.class.getSimpleName();
    // Database fields
    private static final String DATABASE_TABLE = "voltwatcher";
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public DbVoltwatcherAdapter(Context context) {
        this.context = context;
    }

    public DbVoltwatcherAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues createContentValues(String device, String volt, String temp, String data, String longitude, String latitude) {
        ContentValues values = new ContentValues();
        values.put(KEY_DEVICE, device);
        values.put(KEY_VOLT, volt);
        values.put(KEY_TEMP, temp);
        values.put(KEY_DATA, data);
        values.put(KEY_LON, longitude);
        values.put(KEY_LAT, latitude);

        return values;
    }

    // create a contact
    public long createRow(String device, String volt, String temp, String data, String longitude, String latitude) {
        Integer sent = 0;
        ContentValues initialValues = createContentValues(device, volt, temp, data, longitude, latitude);
        return database.insertOrThrow(DATABASE_TABLE, null, initialValues);
    }

    // update a contact
    public boolean updateRow(long id, String device, String volt, String temp, String data, String longitude, String latitude) {
        ContentValues updateValues = createContentValues(device, volt, temp, data, longitude, latitude);
        ContentValues values = new ContentValues();
        return database.update(DATABASE_TABLE, updateValues, KEY_ID
                + "=" + id, null) > 0;
    }

    // delete a contact
    public boolean deleteRow(long id) {
        return database.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    // fetch all rows
    public Cursor fetchAllRows() {
        return database.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_DEVICE,
                KEY_VOLT, KEY_TEMP, KEY_DATA, KEY_LON, KEY_LAT}, null, null, null, null, null);
    }

    //
    public Cursor RowExists(String device, String id) {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_DEVICE,
                KEY_VOLT, KEY_TEMP, KEY_DATA, KEY_LON, KEY_LAT}, KEY_ID + " = " +id, null, null, null, null, null);
        return mCursor;

    }

}