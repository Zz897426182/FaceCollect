package com.hzgc.collect.zk.register;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterWatcher implements Serializable, Watcher {
    private static Logger LOG = Logger.getLogger(RegisterWatcher.class);
    private ZooKeeper zkClient;
    private String rootPath = "/ftp_register";
    private RegisterInfo registerInfo = new RegisterInfo();

    public RegisterWatcher(String zkAddress) {
        try {
            this.zkClient = new ZooKeeper(zkAddress, 6000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        createPath();
        getData();
    }

    private void getData() {
        List<String> childList;
        if (pathExists()) {
            childList = getChild(rootPath);
        } else {
            createPath();
            childList = getChild(rootPath);
        }
        if (childList != null) {
            Map<String, String> hostMapping = new HashMap<>();
            for (String child : childList) {
                if (child.contains("proxy")) {
                    List<String> proxyChild = getChild(rootPath + "/" + child);
                    Map<String, String> proxyMap = new HashMap<>();
                    if (proxyChild != null && proxyChild.size() > 0) {
                        for (String proxy : proxyChild) {
                            String[] mapping = proxy.split(":");
                            proxyMap.put(mapping[0], mapping[1]);
                        }
                    }
                    registerInfo.setProxyInfo(proxyMap);
                } else if (child.contains("protocol")) {
                    try {
                        byte[] rule = zkClient.getData(rootPath + "/" + "protocol", false, null);
                        registerInfo.setPathRule(new String(rule));
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (child.contains("login")) {
                    try {
                        byte[] data = zkClient.getData(rootPath + "/" + "login", false, null);
                        String[] logionInfo = new String(data).split(":");
                        registerInfo.setFtpAccountName(logionInfo[0]);
                        registerInfo.setFtpPassword(logionInfo[1]);
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    String[] mapping = child.split(":");
                    hostMapping.put(mapping[0], mapping[1]);
                }
                registerInfo.setHostNameMapping(hostMapping);
            }
        }
    }

    private List<String> getChild(String path) {
        List<String> childList;
        try {
            childList = zkClient.getChildren(path, true);
            return childList;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createPath() {
        boolean pathExists = pathExists();
        if (!pathExists) {
            try {
                LOG.info("Root path [" + rootPath + "] is not exists, create it");
                zkClient.create(rootPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            LOG.info("Root path [" + rootPath + "] is exists");
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
        LOG.info(event);
        getData();
        LOG.info("New host mapping is " + registerInfo.getHostNameMapping());
    }

    public RegisterInfo getRegisterInfo() {
        return registerInfo;
    }

    public void setRegisterInfo(RegisterInfo registerInfo) {
        this.registerInfo = registerInfo;
    }
}

