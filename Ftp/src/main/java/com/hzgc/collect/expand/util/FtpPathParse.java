package com.hzgc.collect.expand.util;

import org.apache.commons.lang3.StringUtils;

public class FtpPathParse {
    private static BoxParser boxParser = new BoxParser();
    private static DaHuaParser daHuaParser = new DaHuaParser();

    public static FtpPathMetaData parse(String fileName) {
        return boxParser.parse(fileName);
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
