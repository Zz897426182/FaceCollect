package com.hzgc.collect.zk.subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribeInfo {
    private static SubscribeInfo instance;
    private static Map<String, List<String>> sessionMap;
    private static final Object[] lock = new Object[0];

    public SubscribeInfo() {
        sessionMap = new HashMap<>();
    }

    public static SubscribeInfo getInstance() {
        if (instance == null) {
            synchronized (SubscribeInfo.class) {
                instance = new SubscribeInfo();
            }
        }
        return instance;
    }

    public static void flushSessionMap(Map<String, List<String>> newSessionMap) {
        synchronized (lock) {
            sessionMap = newSessionMap;
        }
    }

    public static Map<String, List<String>> getSessionMap() {
        Map<String, List<String>> ipcMappingUser = new HashMap<>();
        sessionMap.keySet().forEach(sessionId -> {
            List<String> ipcList = sessionMap.get(sessionId);
            for (String ipc: ipcList) {
                if (!ipcMappingUser.containsKey(ipc)) {
                    List<String> userList = new ArrayList<>();
                    userList.add(sessionId);
                    ipcMappingUser.put(ipc, userList);
                } else {
                    List<String> userList = ipcMappingUser.get(ipc);
                    userList.add(sessionId);
                    ipcMappingUser.put(ipc, userList);
                }
            }
        });
        return ipcMappingUser;
    }
}
