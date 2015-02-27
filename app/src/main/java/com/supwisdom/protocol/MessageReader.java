package com.supwisdom.protocol;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. File name:
 * com.supwisdom.protocol.MessageReader.java
 * Description: TODO Modify
 * History（或Change Log）:
 * 操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 * 创建 2013-4-17 Tang Cheng
 *
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
public final class MessageReader implements Iterable<MessageRow> {
    /**
     * 请求功能号
     */
    private int funcNo = 0;
    /**
     * 返回码
     */
    private int retCode = 9999;
    /**
     * 返回信息
     */
    private String retMsg = new String("Call Error");
    /**
     * 列属性名称
     */
    private String[] columnDesc = null;
    /**
     * 列属性中文名
     */
    private String[] columnNames = null;
    /**
     * 应答记录
     */
    private ArrayList<MessageRow> rowData = new ArrayList<MessageRow>();
    /**
     * 属性列表
     */
    private HashMap<String, MessageElement> attributes =
            new HashMap<String, MessageElement>();

    /**
     * 返回信息
     *
     * @return
     */
    public String getRetMsg() {
        return retMsg;
    }

    /**
     * 返回码
     *
     * @return
     */
    public int getRetCode() {
        return retCode;
    }

    /**
     * 功能号
     *
     * @return
     */
    public int getFuncNo() {
        return funcNo;
    }

    /**
     * 列属性描述
     *
     * @return
     */
    public String[] getColumnDesc() {
        return columnDesc;
    }

    /**
     * 列属性名称
     *
     * @return
     */
    public String[] getColumnNames() {
        return columnNames;
    }

    /**
     * constructor
     */
    public MessageReader() {
    }

    /**
     * 从字符串加载
     *
     * @param content - 返回数据
     * @return - true 成功， false 失败
     */
    public boolean loadFromString(String content) throws IOException {
        StringReader reader = new StringReader(content);
        return loadFromReader(reader);
    }

    /**
     * 从 InputStream 加载
     *
     * @param is - InputStream
     * @return - true 成功， false 失败
     * @throws java.io.IOException
     */
    public boolean loadFromInputStream(InputStream is) throws IOException {
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(is, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        return loadFromReader(reader);
    }

    /**
     * load from java.io.Reader
     *
     * @param inputReader - Reader
     * @return - true 成功， false 失败
     * @throws java.io.IOException
     */
    private boolean loadFromReader(Reader inputReader) throws IOException {
        JsonReader reader = new JsonReader(inputReader);
        try {
            readMessage(reader);
            return true;
        } catch (Exception e) {
            Log.e("supwisdom.messageread", "Load response error, " + e);
            return false;
        } finally {
            reader.close();
        }
    }

    /**
     * 分析读取Json数据
     *
     * @param reader -
     * @throws java.io.IOException
     */
    private void readMessage(JsonReader reader) throws IOException {
        ArrayList<String[]> dataBuffer = new ArrayList<String[]>();
        reader.beginObject();
        while (reader.hasNext()) {
            String nextName = reader.nextName();
            if ("funcno".equals(nextName)) {
                funcNo = reader.nextInt();
                attributes.put("funcno", new MessageElement(funcNo));
            } else if ("retcode".equals(nextName)) {
                retCode = reader.nextInt();
                attributes.put("retcode", new MessageElement(retCode));
            } else if ("retmsg".equals(nextName)) {
                retMsg = reader.nextString();
                attributes.put("retmsg", new MessageElement(retMsg));
            } else if ("coldesc".equals(nextName)) {
                columnDesc = readStringArray(reader);
            } else if ("colname".equals(nextName)) {
                columnNames = readStringArray(reader);
            } else if ("rowdata".equals(nextName)) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                } else {
                    readDataRows(reader, dataBuffer);
                }
            } else {
                // attribute
                attributes.put(nextName, new MessageElement(reader.nextString()));
            }
        }
        reader.endObject();
        prepareRowData(dataBuffer);
    }

    /**
     * 分析列数据
     *
     * @param dataBuffer
     */
    private void prepareRowData(ArrayList<String[]> dataBuffer) {
        for (String[] row : dataBuffer) {
            MessageRow msgRow = new MessageRow(columnNames, row);
            rowData.add(msgRow);
        }
    }

    /**
     * 读取列数据
     *
     * @param reader
     * @param dataBuffer
     * @throws java.io.IOException
     */
    private void readDataRows(JsonReader reader, ArrayList<String[]> dataBuffer)
            throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            String[] row = readStringArray(reader);
            if (null == row || row.length == 0) {
                continue;
            }
            dataBuffer.add(row);
        }
        reader.endArray();
    }

    /**
     * 读取数组
     *
     * @param reader
     * @return
     * @throws java.io.IOException
     */
    private String[] readStringArray(JsonReader reader) throws IOException {
        ArrayList<String> res = new ArrayList<String>();
        reader.beginArray();
        while (reader.hasNext()) {
            res.add(reader.nextString());
        }
        reader.endArray();
        String[] ret = new String[res.size()];
        res.toArray(ret);
        return ret;
    }

    /**
     * 获取属性值
     *
     * @param attrName
     * @return
     */
    public String getAttr(String attrName) {
        MessageElement ele = attributes.get(attrName);
        if (null == ele) {
            return null;
        }
        return ele.getDataAsString();
    }

    /**
     * 获取属性整形值
     *
     * @param attrName
     * @return
     */
    public int getAttrInt(String attrName) {
        MessageElement ele = attributes.get(attrName);
        if (null == ele) {
            return 0;
        }
        return ele.getDataAsInt();
    }

    /**
     * 返回列 Iterator
     *
     * @return
     * @see MessageRowIterator
     */
    @Override
    public Iterator<MessageRow> iterator() {
        MessageRow[] data = new MessageRow[rowData.size()];
        rowData.toArray(data);
        return new MessageRowIterator(data);
    }

    /**
     * 列记录数
     *
     * @return
     */
    public int rowCount() {
        return rowData.size();
    }

}
