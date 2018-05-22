package com.hzgc.collect.ftp.ftp;

import com.hzgc.collect.ftp.expand.util.CollectProperties;

import java.io.Serializable;

public abstract class ClusterOverFtp implements Serializable {
    protected static int listenerPort = 0;

    public void loadConfig() throws Exception {
        DataConnectionConfigurationFactory dataConnConf = new DataConnectionConfigurationFactory();
        listenerPort = CollectProperties.getFtpPort();
        String passivePorts = CollectProperties.getDataPorts();
        if (passivePorts != null) {
            dataConnConf.setPassivePorts(passivePorts);
        }
    }

    public abstract void startFtpServer();
}
