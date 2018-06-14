package com.hzgc.collect.expand.util;

public interface Parser {
    public boolean canParse(String path);

    public FtpPathMetaData parse(String path);
}
