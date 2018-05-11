package com.hzgc.collect.expand.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Properties;

public class CollectProperties implements Serializable {

    private static Logger LOG = Logger.getLogger(CollectProperties.class);

    private static Properties props = new Properties();
    private static int receiveQueueCapacity;
    private static int receiveNumber;
    private static int faceDetectorNumber;
    private static String ftpIp;
    private static int ftpPort;
    private static String dataPorts;
    private static String ftpImplicitSsl;
    private static int zookeeperSessionTimeout;
    private static String zookeeperAddress;
    private static String zookeeperSubscribePath;
    private static String zookeeperWatcher;
    private static boolean ftpSubscribeSwitch;
    private static String kafkaFaceObjectTopic;
    private static String rocketmqAddress;
    private static String rocketmqCaptureTopic;
    private static String rokcetmqCaptureGroup;
    private static String hostname;
    private static String ftpVersion;


    static {
        try {
            props.load(ClassLoader.getSystemResourceAsStream("collect.properties"));
            setReceiveQueueCapacity(Integer.parseInt(props.getProperty("receive.queue.capacity")));
            setReceiveNumber(Integer.parseInt(props.getProperty(props.getProperty("receive.number"))));
            setFaceDetectorNumber(Integer.parseInt(props.getProperty("face.detector.number")));
            setFtpIp(props.getProperty("${ftp.ip}"));
            setFtpPort(Integer.parseInt(props.getProperty("ftp.port")));
            setDataPorts(props.getProperty("data.ports"));
            setFtpImplicitSsl(props.getProperty("ftp.implicitSsl"));
            setZookeeperSessionTimeout(Integer.parseInt(props.getProperty("zookeeper.session.timeout")));
            setZookeeperAddress(props.getProperty("zookeeper.address"));
            setZookeeperSubscribePath(props.getProperty("zookeeper.subscribe.path"));
            setZookeeperWatcher(props.getProperty("zookeeper.watcher"));
            setFtpSubscribeSwitch(Boolean.parseBoolean(props.getProperty("ftp.subscribe.switch")));
            setKafkaFaceObjectTopic(props.getProperty("kafka.faceobject.topic"));
            setRocketmqAddress(props.getProperty("rocketmq.address"));
            setRocketmqCaptureTopic(props.getProperty("rocketmq.capture.topic"));
            setRokcetmqCaptureGroup(props.getProperty("rocketmq.capture.group"));
            setHostname(InetAddress.getLocalHost().getHostName());
            setFtpVersion(props.getProperty("ftp.version"));

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Catch an unknown error, can't load the configuration file collect.properties");
        }
    }

    public static Properties getProps() {
        return props;
    }

    public static boolean isFtpSubscribeSwitch() {
        return ftpSubscribeSwitch;
    }

    public static void setFtpSubscribeSwitch(boolean ftpSubscribeSwitch) {
        CollectProperties.ftpSubscribeSwitch = ftpSubscribeSwitch;
    }

    public static int getZookeeperSessionTimeout() {
        return zookeeperSessionTimeout;
    }

    public static void setZookeeperSessionTimeout(int zookeeperSessionTimeout) {
        CollectProperties.zookeeperSessionTimeout = zookeeperSessionTimeout;
    }

    public static void setProps(Properties props) {
        CollectProperties.props = props;
    }

    static String getFtpIp() {
        return ftpIp;
    }

    private static void setFtpIp(String ftpIp) {
        CollectProperties.ftpIp = ftpIp;
    }

    static String getFtpVersion() {
        return ftpVersion;
    }

    private static void setFtpVersion(String ftpVersion) {
        CollectProperties.ftpVersion = ftpVersion;
    }

    public static String getHostname() {
        return hostname;
    }

    private static void setHostname(String hostname) {
        CollectProperties.hostname = hostname;
    }

    public static int getReceiveQueueCapacity() {
        return receiveQueueCapacity;
    }

    private static void setReceiveQueueCapacity(int receiveQueueCapacity) {
        CollectProperties.receiveQueueCapacity = receiveQueueCapacity;
    }

    public static int getReceiveNumber() {
        return receiveNumber;
    }

    private static void setReceiveNumber(int receiveNumber) {
        CollectProperties.receiveNumber = receiveNumber;
    }

    public static int getFaceDetectorNumber() {
        return faceDetectorNumber;
    }

    private static void setFaceDetectorNumber(int faceDetectorNumber) {
        CollectProperties.faceDetectorNumber = faceDetectorNumber;
    }

    public static int getFtpPort() {
        return ftpPort;
    }

    private static void setFtpPort(int ftpPort) {
        CollectProperties.ftpPort = ftpPort;
    }

    public static String getDataPorts() {
        return dataPorts;
    }

    private static void setDataPorts(String dataPorts) {
        CollectProperties.dataPorts = dataPorts;
    }

    public static String getFtpImplicitSsl() {
        return ftpImplicitSsl;
    }

    private static void setFtpImplicitSsl(String ftpImplicitSsl) {
        CollectProperties.ftpImplicitSsl = ftpImplicitSsl;
    }

    public static String getZookeeperAddress() {
        return zookeeperAddress;
    }

    private static void setZookeeperAddress(String zookeeperAddress) {
        CollectProperties.zookeeperAddress = zookeeperAddress;
    }

    public static String getZookeeperSubscribePath() {
        return zookeeperSubscribePath;
    }

    private static void setZookeeperSubscribePath(String zookeeperSubscribePath) {
        CollectProperties.zookeeperSubscribePath = zookeeperSubscribePath;
    }

    public static String getZookeeperWatcher() {
        return zookeeperWatcher;
    }

    private static void setZookeeperWatcher(String zookeeperWatcher) {
        CollectProperties.zookeeperWatcher = zookeeperWatcher;
    }

    public static String getKafkaFaceObjectTopic() {
        return kafkaFaceObjectTopic;
    }

    private static void setKafkaFaceObjectTopic(String kafkaFaceObjectTopic) {
        CollectProperties.kafkaFaceObjectTopic = kafkaFaceObjectTopic;
    }

    public static String getRocketmqAddress() {
        return rocketmqAddress;
    }

    private static void setRocketmqAddress(String rocketmqAddress) {
        CollectProperties.rocketmqAddress = rocketmqAddress;
    }

    public static String getRocketmqCaptureTopic() {
        return rocketmqCaptureTopic;
    }

    private static void setRocketmqCaptureTopic(String rocketmqCaptureTopic) {
        CollectProperties.rocketmqCaptureTopic = rocketmqCaptureTopic;
    }

    public static String getRokcetmqCaptureGroup() {
        return rokcetmqCaptureGroup;
    }

    private static void setRokcetmqCaptureGroup(String rokcetmqCaptureGroup) {
        CollectProperties.rokcetmqCaptureGroup = rokcetmqCaptureGroup;
    }
}
