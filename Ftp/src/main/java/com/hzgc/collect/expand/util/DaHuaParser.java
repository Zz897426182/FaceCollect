package com.hzgc.collect.expand.util;

public class DaHuaParser implements Parser {
    @Override
    public boolean canParse(String path) {
        if (path.contains("unknown") || !path.contains(".jpg")) {
            return false;
        }
        String tmpStr = path.substring(path.lastIndexOf("[") + 1, path.lastIndexOf("]"));
        return Integer.parseInt(tmpStr) > 0;
    }

    // /dahua/ipcid/2018-01-01/001/jpg/12/12/01[M][0@0][0].jpg
    @Override
    public FtpPathMetaData parse(String path) {
        FtpPathMetaData message = new FtpPathMetaData();
        if (canParse(path)) {
            String ipcID =  path.substring(path.indexOf("/", 1))
                    .substring(1,path.substring(path.indexOf("/", 1)).indexOf("/", 1));
            String dateStr = path.split("/")[3].replace("-","");
            String year = dateStr.substring(0, 4);
            String month = dateStr.substring(4, 6);
            String day = dateStr.substring(6, 8);
            String hour = path.split("/")[6];
            String minute = path.split("/")[7];
            String second = path.split("/")[8].substring(0,2);

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
