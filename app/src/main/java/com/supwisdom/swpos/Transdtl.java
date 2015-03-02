package com.supwisdom.swpos;

import android.util.Log;

import com.supwisdom.utilities.ErrorDef;
import com.supwisdom.utilities.ErrorInfo;
import com.supwisdom.utilities.TransRecord;

import java.util.Map;

/**
 * Created by Penn on 2015-02-28.
 */
public class Transdtl {
    public static Map<String, String> transnoMap;
    private static final String tag = "com.supwisdom.cardlib.transdtl";



    public static ErrorInfo insertTransdtl(TransRecord record){
        //
        Log.i(tag, "新增一笔流水,流水号="+Integer.toString(record.transno));

        return ErrorDef.SP_SUCCESS;
    }


}
