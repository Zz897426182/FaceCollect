package com.hzgc.collect.expand.processer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

public class ProcessCallBack implements Callback {

    private static Logger LOG = Logger.getLogger(ProcessCallBack.class);

    private String  elapsedTime;
    private String key;

    ProcessCallBack(String key, String time) {
        this.key = key;
        this.elapsedTime = time;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception e) {
        if (metadata != null) {
            LOG.info("Send Kafka successfully! message:[topic:" + metadata.topic() + ", key:" + key +
                    "], send to partition(" + metadata.partition() + "), offset(" + metadata.offset() + ") in " + elapsedTime + "ms");
        } else {
            LOG.error(e.getMessage());
        }
    }
}
