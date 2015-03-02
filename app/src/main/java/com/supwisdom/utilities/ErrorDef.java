package com.supwisdom.utilities;

import com.supwisdom.utilities.ErrorInfo;

import java.util.Map;

/**
 * Created by Penn on 2015-02-28.
 */
public class ErrorDef {
    public static final ErrorInfo SP_SUCCESS = new ErrorInfo(0x0000, "成功");
    public static final ErrorInfo SP_INIT_FOR_PURCHASE_FAIL = new ErrorInfo(0x0001, "消费初始化失败");
    public static final ErrorInfo SP_DEBI_FOR_PURCHASE_FAIL = new ErrorInfo(0x0002, "消费确认失败");
    public static final ErrorInfo SP_GET_MAC1_FAIL = new ErrorInfo(0x0003, "获取mac1失败");
    public static final ErrorInfo SP_CONNECT_FAIL = new ErrorInfo(0x0004, "连接卡片失败");
    public static final ErrorInfo SP_READ_CARD_ERROR = new ErrorInfo(0x0005, "读卡失败");
    public static final ErrorInfo SP_CARD_NOT_SUPPORT = new ErrorInfo(0x0006, "卡片不支持");
    public static final ErrorInfo SP_CARD_DATA_ERROR = new ErrorInfo(0x0007, "卡数据错误");
    public static final ErrorInfo SP_READ_DATABASE_FAIL = new ErrorInfo(0x0008, "读取sqlite数据库出错");
    public static final ErrorInfo SP_TRANS_RECORD_NULL = new ErrorInfo(0x0009, "流水结构为空");
    public static final ErrorInfo SP_TRANSNO_ERROR = new ErrorInfo(0x000A, "流水号有误");
    public static final ErrorInfo SP_INSERT_TRANSDTL_ERROR = new ErrorInfo(0x000B, "插入数据库有误");
//    public static final int SP_INIT_FOR_PURCHASE_FAIL = 0x0001;
//    public static final int SP_DEBI_FOR_PURCHASE_FAIL = 0x0002;
//    public static final int SP_GET_MAC1_FAIL = 0x0003;
//    public static final int SP_CONNECT_FAIL = 0x0004;
//    public static final int SP_READ_CARD_ERROR = 0x0005;
//    public static final int SP_CARD_NOT_SUPPORT = 0x0006;
//    public static final int SP_CARD_DATA_ERROR = 0x0007;
}
