package com.hzgc.collect.zk.subscribe;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class SubscribeWatcher implements Serializable, Watcher {
    private static Logger LOG = Logger.getLogger(SubscribeWatcher.class);
    private String rootPath;
    private ZooKeeper zkClient;

    public SubscribeWatcher(int sessionTimeOut, String zkAddress, String rootPath) {
        this.rootPath = rootPath;
        try {
            this.zkClient = new ZooKeeper(zkAddress,
                    sessionTimeOut,
                    this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 当节点变动更新设备列表
    private void getData() {
        List<String> childList;
        if (pathExists()) {
            childList = getChild();
        } else {
            createRootPath();
            childList = getChild();
        }
        if (childList != null) {
            Map<String, List<String>> sessionMap = new HashMap<>();
            for (String child : childList) {
                String path = rootPath + "/" + child;
                byte[] data = null;
                try {
                    data = zkClient.getData(path, true, null);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> ipcIdList = new ArrayList<>();
                if (data != null) {
                    String ipcIds = new String(data);
                    if (!ipcIds.equals("") && ipcIds.contains(",") && ipcIds.split(",").length >= 3) {
                        ipcIds = ipcIds.substring(0, ipcIds.length() - 1);
                        List<String> list = Arrays.asList(ipcIds.split(","));
                        for (int i = 2; i < list.size(); i++) {
                            ipcIdList.add(list.get(i));
                        }
                    }
                }
                sessionMap.put(child, ipcIdList);
            }
            SubscribeInfo.flushSessionMap(sessionMap);
        }
    }

    /*private boolean isInDate(String time) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, -6);
        long endTime =  now.getTimeInMillis();
        long startTime = Long.parseLong(time);
        return startTime <= endTime;
    }*/

    private List<String> getChild() {
        List<String> childList;
        try {
            childList = zkClient.getChildren(rootPath, true);
            return childList;
        } catch (KeeperException | InterruptedException e) {
            if (e.getMessage().contains("NoNode for " + rootPath)) {
                LOG.info("Zookeeper znode " + rootPath + " not contains child");
            } else {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void createRootPath() {
        boolean pathExists = pathExists();
        if (!pathExists) {
            try {
                LOG.info("Zookeeper subcribe znode is not exists, create it, path is [" + rootPath + "]");
                zkClient.create(rootPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                LOG.info("Subcribe znod create successfull");
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean pathExists() {
        try {
            Stat stat = zkClient.exists(rootPath, false);
            return null != stat;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void process(WatchedEvent event) {
        getData();
        System.out.println(event);
    }
}
