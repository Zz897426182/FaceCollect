package com.hzgc.collect.expand.util;

import java.io.Serializable;
import java.util.List;

/**
 * 发送MQ信息字段，修改字段需通知平台组
 */
public class SendMqMessage implements Serializable{

    private List<String> sessionIds;
    private String ftpUrl;

    public List<String> getSessionIds() {
        return sessionIds;
    }

    public void setSessionIds(List<String> sessionIds) {
        this.sessionIds = sessionIds;
    }

    public String getFtpUrl() {
        return ftpUrl;
    }

    public void setFtpUrl(String ftpUrl) {
        this.ftpUrl = ftpUrl;
    }

    @Override
    public String toString() {
        return "SendMqMessage{" +
                "sessionIds=" + sessionIds +
                ", ftpUrl='" + ftpUrl + '\'' +
                '}';
    }
}
