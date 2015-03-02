package com.supwisdom.db;

/**
 * Created by jov on 2015/1/22.
 */
public class BeanPropEnum {
    public enum KeyValue {
        tKey,
        tValue,
        tType
    }

    public enum LocalUserProp {
        uid,
        userid,
        phone,
        username,
        idno,
        gid,
        rsapbulic,
        isLogined,
        isRemembed,
        isAutoLogin,
        school,
        money,
        passwd,
        isPwdSeted,
        accesstoken,
        refreshtoken,
        schoolcode,
        secretkey
    }

    public enum Transdtl {
        transNo,
        cardNo,
        lastTransCount,
        lastLimitAmount,
        lastAmount,
        lastTransFlag,
        lastTermno,
        lastDatetime,
        cardBeforeBalance,
        cardBeforeCount,
        amount,
        extraAmount,
        transDatatime,
        samNo,
        tac,
        transFlag,
        reserve,
        crc
    }
}
