package com.hzgc.collect.expand.subscribe;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 订阅与演示对象
 */
public class ReceiveIpcIds implements Serializable {

    /**
     * zookeeper中保存的抓拍订阅设备信息
     */
    Map<String, Map<String, List<String>>> map_ZKData;

    /**
     * 订阅功能设备列表
     */
    private volatile List<String> ipcIdList;

    private static ReceiveIpcIds instance = null;

    ReceiveIpcIds() {
    }

    public static ReceiveIpcIds getInstance() {
        if (instance == null) {
            synchronized (ReceiveIpcIds.class) {
                if (instance == null) {
                    instance = new ReceiveIpcIds();
                }
            }
        }
        return instance;
    }

    public List<String> getIpcIdList() {
        return ipcIdList;
    }

    public void setIpcIdList(List<String> ipcIdList) {
        this.ipcIdList = ipcIdList;
    }

    void setIpcIdList(Map<String, Map<String, List<String>>> map) {
        List<String> ipcIdList = new ArrayList<>();
        if (!map.isEmpty()) {
            for (String userId : map.keySet()) {
                if (!StringUtils.isBlank(userId)) {
                    Map<String, List<String>> map1 = map.get(userId);
                    if (!map1.isEmpty()) {
                        for (String time : map1.keySet()) {
                            if (!StringUtils.isBlank(time)) {
                                ipcIdList.addAll(map1.get(time));
                            }
                        }
                    }
                }
            }
        }
        this.ipcIdList = ipcIdList;
    }
}
