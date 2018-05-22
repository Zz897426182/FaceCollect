package com.hzgc.collect.zk.register;

import java.util.HashMap;
import java.util.Map;

public class RegisterInfo {
    private Map<String, String> hostNameMapping;
    private Map<String, String> proxyInfo;
    private String pathRule;
    private String ftpAccountName;
    private String ftpPassword;

    public RegisterInfo() {
        this.hostNameMapping = new HashMap<>();
        this.proxyInfo = new HashMap<>();
    }

    public Map<String, String> getProxyInfo() {
        return proxyInfo;
    }

    public void setProxyInfo(Map<String, String> proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    public Map<String, String> getHostNameMapping() {
        return hostNameMapping;
    }

    public void setHostNameMapping(Map<String, String> hostNameMapping) {
        this.hostNameMapping = hostNameMapping;
    }

    public String getPathRule() {
        return pathRule;
    }

    public void setPathRule(String pathRule) {
        this.pathRule = pathRule;
    }

    public String getFtpAccountName() {
        return ftpAccountName;
    }

    public void setFtpAccountName(String ftpAccountName) {
        this.ftpAccountName = ftpAccountName;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }
}

