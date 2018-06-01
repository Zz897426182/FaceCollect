package com.hzgc.collect;

import com.hzgc.collect.expand.util.FtpLogo;
import com.hzgc.collect.ftp.ClusterOverFtp;
import com.hzgc.collect.ftp.ConnectionConfigFactory;
import com.hzgc.collect.ftp.FtpServer;
import com.hzgc.collect.ftp.FtpServerFactory;
import com.hzgc.collect.ftp.command.CommandFactoryFactory;
import com.hzgc.collect.ftp.nativefs.filesystem.NativeFileSystemFactory;
import com.hzgc.collect.ftp.ftplet.FtpException;
import com.hzgc.collect.ftp.listener.ListenerFactory;
import com.hzgc.collect.ftp.usermanager.PropertiesUserManagerFactory;
import com.hzgc.collect.expand.util.CollectProperties;
import com.hzgc.collect.zk.register.FtpRegister;
import com.hzgc.collect.zk.subscribe.SubscribeWatcher;
import com.hzgc.jni.NativeFunction;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.Serializable;

public class FTP extends ClusterOverFtp implements Serializable {

    private static Logger LOG = Logger.getLogger(FTP.class);

    @Override
    public void startFtpServer() {

        //使用带CommonConf对象的有参构造器可以构造带有expand模块的FtpServerContext
        FtpServerFactory serverFactory = new FtpServerFactory();
        LOG.info("Create " + FtpServerFactory.class + " successful");
        ListenerFactory listenerFactory = new ListenerFactory();
        LOG.info("Create " + ListenerFactory.class + " successful");
        //set the port of the listener
        listenerFactory.setPort(listenerPort);
        LOG.info("The port for listener is " + listenerPort);
        // replace the default listener
        serverFactory.addListener("default", listenerFactory.createListener());
        LOG.info("Add listner, name:default, class:" + serverFactory.getListener("default").getClass());
        // set customer user manager
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        try {
            userManagerFactory.setFile(new File(ClassLoader.getSystemResource("users.properties").getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        serverFactory.setUserManager(userManagerFactory.createUserManager());
        LOG.info("Set customer user manager factory is successful, " + userManagerFactory.getClass());
        //set customer cmd factory
        CommandFactoryFactory commandFactoryFactory = new CommandFactoryFactory();
        serverFactory.setCommandFactory(commandFactoryFactory.createCommandFactory());
        LOG.info("Set customer command factory is successful, " + commandFactoryFactory.getClass());
        //set local file system
        NativeFileSystemFactory nativeFileSystemFactory = new NativeFileSystemFactory();
        serverFactory.setFileSystem(nativeFileSystemFactory);
        LOG.info("Set customer file system factory is successful, " + nativeFileSystemFactory.getClass());
        // TODO: 2017-10-9
        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
        LOG.info("FTP Server Maximum logon number:" + connectionConfigFactory.createUDConnectionConfig().getMaxLogins());
        serverFactory.setConnectionConfig(connectionConfigFactory.createUDConnectionConfig());
        LOG.info("Set user defined connection config file is successful, " + connectionConfigFactory.getClass());
        //Set the dynamic log configuration file refresh time
        PropertyConfigurator.configureAndWatch(
                ClassLoader.getSystemResource("log4j.properties").getPath(), 5000);
        LOG.info("Dynamic log configuration is successful! Log configuration file refresh time 5000ms");

        SubscribeWatcher subscribeWatcher = new SubscribeWatcher(CollectProperties.getZookeeperSessionTimeout(),
                CollectProperties.getZookeeperAddress(),
                CollectProperties.getZookeeperSubscribePath()
        );
        subscribeWatcher.startSubscribe();

        FtpRegister ftpRegister = new FtpRegister(CollectProperties.getZookeeperAddress(),
                CollectProperties.getZookeeperSessionTimeout(),
                CollectProperties.getProxyIpAddress(),
                CollectProperties.getProxyPort(),
                CollectProperties.getFtpPathRule(),
                CollectProperties.getFtpAccount(),
                CollectProperties.getFtpPassword(),
                CollectProperties.getHostname(),
                CollectProperties.getFtpIp());
        ftpRegister.registFtp();

        FtpServer server = serverFactory.createServer();
        try {
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        int detectorNum = CollectProperties.getFaceDetectorNumber();
        LOG.info("Init face detector, number is " + detectorNum);
        for (int i = 0; i < detectorNum; i++) {
            NativeFunction.init();
        }
        FTP ftp = new FTP();
        ftp.loadConfig();
        ftp.startFtpServer();
        LOG.info("\n" + FtpLogo.getLogo());
    }
}
