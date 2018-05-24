package com.hzgc.collect.zk.subscribe;

import java.util.ArrayList;
import java.util.List;

public class SubscribeInfo {
    private static SubscribeInfo instance;
    private static List<String> ipcList;
    private static final Object[] lock = new Object[0];

    public SubscribeInfo() {
        ipcList = new ArrayList<>();
    }

    public static SubscribeInfo getInstance() {
        if (instance == null) {
            synchronized (SubscribeInfo.class) {
                instance = new SubscribeInfo();
            }
        }
        return instance;
    }

    public static void flushIpcList(List<String> newIpcList) {
        synchronized (lock) {
            ipcList = newIpcList;
        }
    }

    public static List<String> getIpcList() {
        return ipcList;
    }
}
