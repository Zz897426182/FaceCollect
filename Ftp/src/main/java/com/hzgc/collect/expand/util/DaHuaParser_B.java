package com.hzgc.collect.expand.util;

/**
 * 大华工程机路径解析
 * 相机配置根路径: dahua
 */
public class DaHuaParser_B implements Parser {
    @Override
    public boolean canParse(String path) {
        if (path.contains("unknown") || !path.contains(".jpg")) {
            return false;
        }
        String tmpStr = path.substring(path.lastIndexOf("[") + 1, path.lastIndexOf("]"));
        return Integer.parseInt(tmpStr) > 0;
    }

    // /dahua/ipcid/2018-07-16/pic_001/14.50.02[M][0@0][0].jpg
    @Override
    public FtpPathMetaData parse(String path) {
        FtpPathMetaData message = new FtpPathMetaData();
        if (canParse(path)) {
            String ipcID =  path.substring(path.indexOf("/", 1))
                    .substring(1,path.substring(path.indexOf("/", 1)).indexOf("/", 1));
            String dateStr = path.split("/")[2].replace("-","");
            String year = dateStr.substring(0, 4);
            String month = dateStr.substring(4, 6);
            String day = dateStr.substring(6, 8);
            String timeStr = path.substring(path.lastIndexOf("/") + 1, path.indexOf("["))
                    .replace(".", "");;
            String hour = timeStr.substring(0, 2);
            String minute = timeStr.substring(2, 4);
            String second = timeStr.substring(4, 6);

            StringBuilder time = new StringBuilder();
            time = time.append(year).append("-").append(month).append("-").append(day).
                    append(" ").append(hour).append(":").append(minute).append(":").append(second);

            StringBuilder date = new StringBuilder();
            date = date.append(year).append("-").append(month).append("-").append(day);

            StringBuilder sj = new StringBuilder();
            sj = sj.append(hour).append(minute);

            message.setIpcid(ipcID);
            message.setTimeStamp(time.toString());
            message.setDate(date.toString());
            message.setTimeslot(Integer.parseInt(sj.toString()));
        }
        return message;
    }
}
