package com.hzgc.collect.expand.register;

import com.hzgc.collect.expand.util.CollectProperties;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.Serializable;

public class FtpRegister implements Serializable {
    private Logger LOG = Logger.getLogger(FtpRegister.class);
    private ZooKeeper zooKeeper;
    private String rootPath = "/ftp_register";
    private String proxyPath = "proxy";
    private String protocol = "protocol";
    private String ftpLogin = "login";

    public FtpRegister() {
        try {
            this.zooKeeper = new ZooKeeper(CollectProperties.getZookeeperAddress(),
                    CollectProperties.getZookeeperSessionTimeout(), new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    LOG.info(event);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.rootPath = CollectProperties.getRegisterPath();
    }

    public void registFtp() {
        if (!pathExists(rootPath)) {
            try {
                zooKeeper.create(rootPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            System.out.println(pathExists(rootPath + "/" + proxyPath));
            if (!pathExists(rootPath + "/" + proxyPath)) {
                zooKeeper.create(rootPath + "/" + proxyPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                if (!pathExists(rootPath + "/" + proxyPath + "/" + CollectProperties.getProxyIpAddress() + ":" + CollectProperties.getProxyPort())) {
                    zooKeeper.create(rootPath
                            + "/" + proxyPath
                            + "/" + CollectProperties.getProxyIpAddress() + ":" + CollectProperties.getProxyPort(),
                            null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }

        if (!pathExists(rootPath + "/" + protocol)) {
            try {
                zooKeeper.create(rootPath + "/" + protocol, CollectProperties.getFtpPathRule().getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!pathExists(rootPath + "/" + ftpLogin)) {
            try {
                zooKeeper.create(rootPath + "/" + ftpLogin,
                        (CollectProperties.getFtpAccount() + ":" + CollectProperties.getFtpPassword()).getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            zooKeeper.create(CollectProperties.getRegisterPath()
                    + "/" + CollectProperties.getHostname() + ":"
                    + CollectProperties.getFtpIp(),
                    null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean pathExists(String path) {
        try {
            Stat stat = zooKeeper.exists(path, false);
            return null != stat;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
