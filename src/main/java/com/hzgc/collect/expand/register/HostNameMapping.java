package com.hzgc.collect.expand.register;

import com.hzgc.collect.expand.util.CollectProperties;

import java.io.Serializable;

public class HostNameMapping implements Serializable {
    private String ftpHostName;
    private String ftpIpAddress;

    public HostNameMapping() {
        this.ftpHostName = CollectProperties.getHostname();
        this.ftpIpAddress = CollectProperties.getFtpIp();
    }

    public String getFtpHostName() {
        return ftpHostName;
    }

    public String getFtpIpAddress() {
        return ftpIpAddress;
    }
}
