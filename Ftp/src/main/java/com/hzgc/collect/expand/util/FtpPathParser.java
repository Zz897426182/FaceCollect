package com.hzgc.collect.expand.util;

import org.apache.commons.lang3.StringUtils;

public class FtpPathParser {
    public static int pcikSmallPicture(String pictureName) {
        int picType = 0;
        if (null != pictureName) {
            String tmpStr = pictureName.substring(pictureName.lastIndexOf("_") + 1, pictureName.lastIndexOf("."));
            try {
                picType = Integer.parseInt(tmpStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return picType;
    }

    /**
     * 根据文件上传至ftp的绝对路径获取到ipcID、TimeStamp、Date、TimeSlot
     *
     * @param fileName 文件上传至ftp的绝对路径，例如：/3B0383FPAG51511/2017/05/23/16/00/2017_05_23_16_00_15_5704_0.jpg
     * @return 设备、时间等信息 例如：{date=2017-05-23, sj=1600, ipcID=3B0383FPAG51511, time=2017-05-23 16:00:15}
     */
    public static FtpPathMetaData getFtpPathMetaData(String fileName) {
        FtpPathMetaData message = new FtpPathMetaData();
        String ipcID = fileName.substring(1, fileName.indexOf("/", 1));
        String timeStr = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("_")).replace("_", "");

        String year = timeStr.substring(0, 4);
        String month = timeStr.substring(4, 6);
        String day = timeStr.substring(6, 8);
        String hour = timeStr.substring(8, 10);
        String minute = timeStr.substring(10, 12);
        String second = timeStr.substring(12, 14);

        StringBuilder time = new StringBuilder();
        time = time.append(year).append("-").append(month).append("-").append(day).
                append(" ").append(hour).append(":").append(minute).append(":").append(second);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder date = new StringBuilder();
        date = date.append(year).append("-").append(month).append("-").append(day);

        StringBuilder sj = new StringBuilder();
        sj = sj.append(hour).append(minute);

        try {
            /*Date date = sdf.parse(time.toString());
            long timeStamp = date.getTime();*/
            message.setIpcid(ipcID);
            message.setTimeStamp(time.toString());
            message.setDate(date.toString());
            message.setTimeslot(sj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 根据ftpUrl获取到IP、HostName、ipcID、TimeStamp、Date、TimeSlot
     *
     * @param ftpUrl ftp地址 例如：ftp://172.18.18.109:2121/ABCVS20160823CCCH/2017_11_09_10_53_35_2_0.jpg
     * @return 设备、时间等信息 例如：{date=2017-11-09, filepath=/ABCVS20160823CCCH/2017_11_09_10_53_35_2_0.jpg, port=2121, ip=172.18.18.109
     * , timeslot=1053, ipcid=ABCVS20160823CCCH, timestamp=2017-11-09 10:53:35}
     */
    public static FtpUrlMetaData getFtpUrlMessage(String ftpUrl) {
        FtpUrlMetaData message = new FtpUrlMetaData();
        String ip = ftpUrl.substring(ftpUrl.indexOf(":") + 3, ftpUrl.lastIndexOf(":"));
        String portStr = ftpUrl.substring(ftpUrl.lastIndexOf(":") + 1);
        String port = portStr.substring(0, portStr.indexOf("/"));
        String filePath = portStr.substring(portStr.indexOf("/"));
        FtpPathMetaData pathMessage = getFtpPathMetaData(filePath);
        if (pathMessage != null) {
            message.setIpcid(pathMessage.getIpcid());
            message.setTimeStamp(pathMessage.getTimeStamp());
            message.setDate(pathMessage.getDate());
            message.setTimeslot(pathMessage.getTimeslot());
            message.setHostname(ip);
            message.setPort(port);
            message.setFilePath(filePath);
        }
        return message;
    }

    /**
     * ftpUrl中的HostName转为IP
     *
     * @param ftpUrl 带HostName的ftpUrl
     * @return 带IP的ftpUrl
     */
    public static String hostNameUrl2IpUrl(String ftpUrl) {
        String hostName = ftpUrl.substring(ftpUrl.indexOf("/") + 2, ftpUrl.lastIndexOf(":"));
        String ftpServerIP = CollectProperties.getFtpIp();
        if (!StringUtils.isBlank(ftpUrl)) {
            return ftpUrl.replace(hostName, ftpServerIP);
        }
        return ftpUrl;
    }

    /**
     * 通过上传文件路径解析到文件的ftp地址（ftp发送至kafka的key）
     * /xx/xx/xx => ftp://hostname/xx/xx/xx
     *
     * @param filePath ftp接收数据路径
     * @return 文件的ftp地址
     */
    public static String ftpPath2HostNamepath(String filePath) {
        StringBuilder url = new StringBuilder();
        String hostName = CollectProperties.getHostname();
        url = url.append("ftp://").append(hostName).append(":").append(CollectProperties.getFtpPort()).append(filePath);
        return url.toString();
    }

    /**
     * 小图ftpUrl转大图ftpUrl
     *
     * @param surl 小图ftpUrl
     * @return 大图ftpUrl
     */
    public static String surlToBurl(String surl) {
        StringBuilder burl = new StringBuilder();
        String s1 = surl.substring(0, surl.lastIndexOf("_") + 1);
        String s2 = surl.substring(surl.lastIndexOf("."));
        burl.append(s1).append(0).append(s2);
        return burl.toString();
    }
}
