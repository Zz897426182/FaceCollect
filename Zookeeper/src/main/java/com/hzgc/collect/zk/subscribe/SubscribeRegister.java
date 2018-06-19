package com.hzgc.collect.zk.subscribe;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class SubscribeRegister {
    private Logger LOG = Logger.getLogger(SubscribeRegister.class);
    private ZooKeeper zooKeeper;
    private String rootPath = "/ftp_subscribe";

    public SubscribeRegister(String zkAddress) {
        try {
            this.zooKeeper = new ZooKeeper(zkAddress, 6000, new SubscribeWatcher(6000, zkAddress, rootPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    创建抓拍订阅根路径
     */
    public void createRootPath() {
        if (!pathExists(rootPath)) {
            try {
                LOG.info("FTP Subscribe Root Path [" + rootPath + "] is not exists, creating it");
                zooKeeper.create(rootPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            LOG.info("FTP Subscribe Root Path [" + rootPath + "] is exists");
        }
    }

    /*
    创建/更新抓拍订阅用户路径
     */
    public void updateSessionPath(String sessionId, List<String> ipcIdList) {
        String sessionPath = rootPath + "/" + sessionId;
        long time = System.currentTimeMillis();
        StringBuilder data = new StringBuilder();
        data.append(sessionId).append(",").append(time).append(",");
        for (String ipcId : ipcIdList) {
            data.append(ipcId).append(",");
        }
        if (!pathExists(sessionPath)) {

            try {
                zooKeeper.create(sessionPath, data.toString().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                LOG.info("Creating znode [" + sessionPath + "] successful, data : " + data);
            } catch (KeeperException | InterruptedException e) {
                LOG.error("Creating znode [" + sessionPath + "] failed");
                e.printStackTrace();
            }
        } else {
            try {
                zooKeeper.setData(sessionPath, data.toString().getBytes(), -1);
                LOG.info("Updating znode [" + sessionPath + "] successful, update data : " + data);
            } catch (KeeperException | InterruptedException e) {
                LOG.error("Updating znode [" + sessionPath + "] failed");
                e.printStackTrace();
            }
        }
    }

    /*
    删除抓拍订阅用户路径
     */
    public void deleteSessionPath(String sessionId) {
        String sessionPath = rootPath + "/" + sessionId;
        if (pathExists(sessionPath)) {
            try {
                zooKeeper.delete(sessionPath, -1);
                LOG.info("Deleting znode [" + sessionPath + "] successful");
            } catch (InterruptedException | KeeperException e) {
                LOG.error("Deleting znode [" + sessionPath + "] failed");
                e.printStackTrace();
            }
        }else {
            LOG.info("Deleting znode [" + sessionPath + "] is not exists");
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
