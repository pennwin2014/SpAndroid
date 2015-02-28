package com.supwisdom.swpos;

/**
 * Created by Penn on 2015-02-28.
 */
public class ErrorDef {
    //成功
    public static final int SPSUCCESS = 0x0000;
    //寻卡失败
    public static final int REQUESTCARDERROR = 0x0001;
    //读卡失败
    public static final int READCARDERROR = 0x0002;
    //卡类型不支持
    public static final int CARDNOTSUPPORT = 0x0003;
    //卡数据不正确
    public static final int CARDDATAERROR = 0x0004;
}
