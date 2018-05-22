package com.hzgc.collect.ftp.expand.processer;

import com.hzgc.collect.ftp.expand.receiver.Event;
import com.hzgc.collect.ftp.expand.util.CollectProperties;
import com.hzgc.collect.ftp.expand.util.JsonUtil;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.FaceFunction;

import java.util.concurrent.BlockingQueue;

public class ProcessThread implements Runnable {

    private BlockingQueue<Event> queue;

    public ProcessThread(BlockingQueue<Event> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Event event;
        try {
            while ((event = queue.take()) != null) {
                FaceAttribute attribute =
                        FaceFunction.featureExtract(event.getAbsolutePath());
                if (attribute.getFeature() != null) {
                    FaceObject faceObject = new FaceObject(event.getIpcId(),
                            event.getTimeStamp(),
                            SearchType.PERSON,
                            event.getDate(),
                            event.getTimeSlot(),
                            attribute,
                            event.getFtpHostNameUrlPath(),
                            event.getBigPicurl(),
                            CollectProperties.getHostname());
                    ProcessCallBack callBack = new ProcessCallBack(event.getFtpIpUrlPaht(),
                            System.currentTimeMillis());
                    ProducerKafka.getInstance().sendKafkaMessage(
                            CollectProperties.getRocketmqCaptureTopic(),
                            event.getFtpHostNameUrlPath(),
                            JsonUtil.toJson(faceObject),
                            callBack);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
