package com.hzgc.collect.expand.util;

import java.io.Serializable;

public class FtpPathMetaData implements Serializable {

    private String ipcid;
    private String timeStamp;
    private String date;
    private String timeslot;

    public String getIpcid() {
        return ipcid;
    }

    void setIpcid(String ipcid) {
        this.ipcid = ipcid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeslot() {
        return timeslot;
    }

    void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }
}
