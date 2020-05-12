package com.krishworks.adminlogtest;

public class Note {

    private  String mac, imei, timestamp;
    private String space;
    private int id;

    public Note() {
    }

    public Note(String mac, String imei, String timestamp, String space, int id) {
        this.mac = mac;
        this.imei = imei;
        this.timestamp = timestamp;
        this.space = space;
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public String getImei() {
        return imei;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSpace() {
        return space;
    }

    public int getId() {
        return id;
    }
}
