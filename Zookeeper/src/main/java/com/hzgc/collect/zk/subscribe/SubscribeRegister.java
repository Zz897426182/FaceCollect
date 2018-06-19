package com.hzgc.collect.zk.subscribe;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class SubscribeRegister {
    private Logger LOG = Logger.getLogger(SubscribeRegister.class);
    private ZooKeeper zooKeeper;
    private String zkAddress;
    public static String ROOT_PATH = "/ftp_subscribe";

    public SubscribeRegister(String zkAddress, String rootPath){
        try {
            ROOT_PATH = rootPath;
            this.zooKeeper = new ZooKeeper(zkAddress, 6000, new SubscribeWatcher(6000, zkAddress, rootPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    创建抓拍订阅根路径
     */
    public void createRootPath(){
        if (!pathExists(ROOT_PATH)){
            try {
                LOG.info("Root Path [" + ROOT_PATH + "] is not exists, create it");
                zooKeeper.create(ROOT_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            LOG.info("Root Path [" + ROOT_PATH + "] is exists");
        }
    }

    /*
    创建抓拍订阅用户路径
     */
    public void createSessionPath(String sessionPath, String data){
        try {
            zooKeeper.create(sessionPath , data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            LOG.info("Creating znode successful, session path :" + sessionPath );
        } catch (KeeperException | InterruptedException e) {
            LOG.error("Creating znode failed, session path :" + sessionPath);
            e.printStackTrace();
        }
    }

    /*
    删除抓拍订阅用户路径
     */
    public void deleteSessionPath(String sessionPath){
        try {
            zooKeeper.delete(sessionPath, -1);
            LOG.info("Delete znode successful, session path :" + sessionPath);
        } catch (InterruptedException | KeeperException e) {
            LOG.info("Delete znode failed, session path :" + sessionPath);
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
