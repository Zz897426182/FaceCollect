package com.hzgc.collect.expand.processer;

import com.hzgc.collect.expand.receiver.Event;
import com.hzgc.collect.expand.util.CollectProperties;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.jni.FaceAttribute;
import com.hzgc.common.jni.FaceFunction;
import com.hzgc.common.util.json.JSONUtil;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;

public class ProcessThread implements Runnable {
    private Logger LOG = Logger.getLogger(ProcessThread.class);
    private BlockingQueue<Event> queue;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                    FaceObject faceObject = FaceObject.builder()
                            .setIpcId(event.getIpcId())
                            .setTimeStamp(event.getTimeStamp())
                            .setDate(event.getDate())
                            .setTimeSlot(event.getTimeSlot())
                            .setAttribute(attribute)
                            .setStartTime(dateFormat.format(System.currentTimeMillis()))
                            .setSurl(event.getFtpHostNameUrlPath())
                            .setBurl(event.getBigPicurl())
                            .setHostname(CollectProperties.getHostname())
                            .setRelativePath(event.getRelativePath());
                    ProcessCallBack callBack = new ProcessCallBack(event.getFtpIpUrlPath(),
                            dateFormat.format(System.currentTimeMillis()));
                    String jsonObject = JSONUtil.toJson(faceObject);
                    ProducerKafka.getInstance().sendKafkaMessage(
                            CollectProperties.getKafkaFaceObjectTopic(),
                            event.getFtpHostNameUrlPath(),
                            jsonObject,
                            callBack);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
