package com.supwisdom.protocol;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd.
 * File name: com.supwisdom.protocol
 * Description: TODO Modify History（或Change Log）:
 * 操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 * 创建 13-5-2 tangcheng
 * <p/>
 *
 * @author tangcheng
 * @version 1.0
 * @since 1.0
 */
public class EpayResponse {
    private int retCode;
    private String retMsg;
    private HashMap<String, String> mValues = new HashMap<String, String>();

    public EpayResponse(){
        this.retCode = 9999;
        this.retMsg = "";
    }

    public boolean loadFromString(String response){
        JsonReader reader = new JsonReader(new StringReader(response));
        try {
            reader.beginObject();
            while(reader.hasNext()){
                String name = reader.nextName();
                if("retcode".equals(name)){
                    this.retCode = reader.nextInt();
                } else if("retmsg".equals(name)){
                    this.retMsg = reader.nextString();
                } else {
                    JsonToken node = reader.peek();
                    if(node == JsonToken.NULL){
                        mValues.put(name, "");
                        reader.skipValue();
                    }else if(node == JsonToken.BEGIN_ARRAY
                            || node == JsonToken.END_ARRAY){
                        reader.skipValue();
                    }else if(node == JsonToken.BEGIN_OBJECT
                            || node == JsonToken.END_OBJECT){
                        reader.skipValue();
                    } else {
                        mValues.put(name, reader.nextString());
                    }
                }
            }
            reader.endObject();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getRetCode() {
        return retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }
    public String getResultString(String name){
        if(mValues.containsKey(name)){
            return mValues.get(name);
        }
        return "";
    }

    public int getResultInt(String name){
        String result = getResultString(name);
        if("".equals(name)){
            return 0;
        }
        return Integer.parseInt(result);
    }

    public double getResultDouble(String name){
        String result = getResultString(name);
        if("".equals(name)){
            return 0.0;
        }
        return Double.parseDouble(result);
    }

}
