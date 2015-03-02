package com.supwisdom.swpos;

import android.content.Context;

import com.supwisdom.db.KeyValueMapDao;
import com.supwisdom.utilities.KeyValueBean;

/**
 * Created by Penn on 2015-03-02.
 */
public class TransNo {
    public static final String PARA_TRANS_NO = "TransNo";
    private static KeyValueMapDao keyValueMapDao;

    public int getTransNo() {
        return transNo;
    }

    public void setTransNo(int transNo) {
        this.transNo = transNo;
    }

    private int transNo;


    public static int getTransNo(Context context){
        keyValueMapDao = KeyValueMapDao.getInstance(context);
        KeyValueBean bean = keyValueMapDao.getKeyValue(PARA_TRANS_NO);
        if(null == bean){
            keyValueMapDao.saveOrUpdateKeyValue(PARA_TRANS_NO, "0");
            return 0;
        }
        return Integer.getInteger(bean.gettValue());
    }

    public static int generateTransNo(Context context){
        int newTransNo = getTransNo(context) + 1;
        keyValueMapDao = KeyValueMapDao.getInstance(context);
        keyValueMapDao.saveOrUpdateKeyValue(PARA_TRANS_NO, String.valueOf(newTransNo));
        return newTransNo;
    }

}
