package com.hzgc.collect.zk.register;

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
    private String proxyIp;
    private String proxyPort;
    private String ftppathRule;
    private String ftpAccount;
    private String ftpPassword;
    private String ftpHostName;
    private String ftpIp;

    public FtpRegister(String zkAddress,
                       int sessionTimeOut,
                       String proxyIp,
                       String proxyPort,
                       String ftppathRule,
                       String ftpAccount,
                       String ftpPassword,
                       String ftpHostName,
                       String ftpIp) {
        try {
            this.zooKeeper = new ZooKeeper(zkAddress,
                    sessionTimeOut, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    LOG.info(event);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
        this.ftppathRule = ftppathRule;
        this.ftpAccount = ftpAccount;
        this.ftpPassword = ftpPassword;
        this.ftpHostName = ftpHostName;
        this.ftpIp = ftpIp;
    }

    private void createRootPath() {
        if (!pathExists(rootPath)) {
            try {
                LOG.info("Root Path [" + rootPath + "] is not exists, create it");
                zooKeeper.create(rootPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            LOG.info("Root Path [" + rootPath + "] is exists");
        }
    }

    private void createProxyPath() {
        try {
            if (!pathExists(rootPath + "/" + proxyPath)) {
                LOG.info("Proxy path [" + rootPath + "/" + proxyPath + "] is not exists, create it");
                zooKeeper.create(rootPath + "/" + proxyPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                LOG.info("Proxy path [" + rootPath + "/" + proxyPath + "] is exists");
                createChildProxyPath();
            }
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }


    private void createChildProxyPath() {
        if (!pathExists(rootPath + "/" + proxyPath + "/" + this.proxyIp + ":" + this.proxyPort)) {
            LOG.info("Proxy child path ["
                    + rootPath + "/" + proxyPath + "/" + this.proxyIp + ":" + this.proxyPort
                    + "] is not exists, create it");
            try {
                zooKeeper.create(rootPath
                                + "/" + proxyPath
                                + "/" + this.proxyIp + ":" + this.proxyPort,
                        null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            LOG.info("Proxy child path ["
                    + rootPath + "/" + proxyPath + "/" + this.proxyIp + ":" + this.proxyPort
                    + "] is exists");
        }
    }

    private void createProtocolPath() {
        if (!pathExists(rootPath + "/" + protocol)) {
            try {
                LOG.info("Protocol path ["
                        + rootPath + "/" + protocol + "] is not exists, create it");
                zooKeeper.create(rootPath + "/" + protocol, this.ftppathRule.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            LOG.info("Protocol path ["
                    + rootPath + "/" + protocol + "] is exists");
        }
    }

    private void createLoginPath() {
        if (!pathExists(rootPath + "/" + ftpLogin)) {
            try {
                LOG.info("Login path ["
                        + rootPath + "/" + ftpLogin + "] is not exists, create it");
                zooKeeper.create(rootPath + "/" + ftpLogin,
                        (this.ftpAccount + ":" + this.ftpPassword).getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            LOG.info("Login path ["
                    + rootPath + "/" + ftpLogin + "] is exists");
        }
    }

    private void createHostMapping() {
        try {
            LOG.info("Host Mapping path ["
                    + this.rootPath + "/" + this.ftpHostName + ":" + this.ftpIp
                    + "] is not exists, create it");
            zooKeeper.create(this.rootPath + "/" + this.ftpHostName + ":" + this.ftpIp,
                    null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void registFtp() {
        createRootPath();
        createProxyPath();
        createProtocolPath();
        createLoginPath();
        createHostMapping();
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
