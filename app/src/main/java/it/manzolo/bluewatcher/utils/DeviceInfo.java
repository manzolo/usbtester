package it.manzolo.bluewatcher.utils;

import java.util.Arrays;

public class DeviceInfo {
    private String address;
    private byte[] data;

    private Double volt;
    private Double amp;
    private Double mW;
    private Integer tempF;
    private Integer tempC;

    public DeviceInfo(String address, byte[] data) throws Exception {
        this.address = address;
        this.data = data;
        this.load();

    }

    private void load() throws Exception {
        Struct struct = new Struct();
        long[] volts = struct.unpack("!H", Arrays.copyOfRange(this.data, 2, 4));
        long[] amps = struct.unpack("!H", Arrays.copyOfRange(this.data, 4, 6));
        long[] mWs = struct.unpack("!I", Arrays.copyOfRange(this.data, 6, 10));

        long[] tempCs = struct.unpack("!H", Arrays.copyOfRange(this.data, 10, 12));
        long[] tempFs = struct.unpack("!H", Arrays.copyOfRange(this.data, 12, 14));

        volt = Double.parseDouble(new StringBuilder().append(volts[0] / 100.0).toString());
        amp = Double.parseDouble(new StringBuilder().append(amps[0] / 1000.0).toString());
        tempC = Integer.parseInt(new StringBuilder().append(tempCs[0]).toString());
        tempF = Integer.parseInt(new StringBuilder().append(tempFs[0]).toString());
        mW = Double.parseDouble(new StringBuilder().append(mWs[0] / 1000.0).toString());

    }

    public Double getVolt() {
        return this.volt;
    }

    public String getAddress() {
        return address;
    }

    public Double getAmp() {
        return amp;
    }

    public Double getmW() {
        return mW;
    }

    public Integer getTempF() {
        return tempF;
    }

    public Integer getTempC() {
        return tempC;
    }
}
