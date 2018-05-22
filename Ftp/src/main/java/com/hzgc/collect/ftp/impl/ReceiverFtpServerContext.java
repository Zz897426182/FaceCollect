package com.hzgc.collect.ftp.ftp.impl;

import com.hzgc.collect.ftp.expand.receiver.ReceiverScheduler;

public class ReceiverFtpServerContext extends DefaultFtpServerContext {
    private ReceiverScheduler scheduler;

    public ReceiverFtpServerContext() {
        super();
        this.scheduler = new ReceiverScheduler();
    }

    @Override
    public ReceiverScheduler getScheduler() {
        return this.scheduler;
    }
}
