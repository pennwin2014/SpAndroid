package com.supwisdom.protocol;

import android.util.JsonWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. File name:
 * com.supwisdom.protocol.MessageWriter.java Description: TODO Modify
 * History（或Change Log）:
 * 操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 * 创建 2013-4-17 Tang Cheng
 *
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
public final class MessageWriter {
	/**
	 * 属性
	 */
	HashMap<String, MessageElement> attributes = new HashMap<String, MessageElement>();
	/**
	 * 列属性名
	 */
	HashSet<String> colNames = new HashSet<String>();
	/**
	 * 列属性描述
	 */
	HashSet<String> colDesc = new HashSet<String>();
	/**
	 * 当前行
	 */
	HashMap<String, MessageElement> currentRow = new HashMap<String, MessageElement>();
	/**
	 * 列数据
	 */
	ArrayList<HashMap<String, MessageElement>> rowData = new ArrayList<HashMap<String, MessageElement>>();

	/**
	 * 功能号
	 * @return
	 */
	public int getFuncNo() {
		return funcNo;
	}

	/**
	 * 设置请求功能号
	 * @param funcNo
	 */
	public void setFuncNo(int funcNo) {
		this.funcNo = funcNo;
	}

	/**
	 * 功能号
	 */
	private int funcNo = 0;

	/**
	 *
	 */
	public MessageWriter() {

	}

	/**
	 * 设置属性值
	 * @param name
	 * @param value
	 * @return
	 */
	public MessageWriter addAttr(String name, String value) {
		attributes.put(name, new MessageElement(value));
		return this;
	}

	/**
	 * 设置属性值
	 * @param name
	 * @param value
	 * @return
	 */
	public MessageWriter addAttr(String name, int value) {
		attributes.put(name, new MessageElement(value));
		return this;
	}

	/**
	 * 设置属性值
	 * @param name
	 * @param value
	 * @return
	 */
	public MessageWriter addAttr(String name, float value) {
		attributes.put(name, new MessageElement(value));
		return this;
	}

	/**
	 * 获取列属性
	 * @param name
	 * @return
	 */
	public MessageElement getAttr(String name){
		return attributes.get(name);
	}

	/**
	 * 增加列属性值
	 * @param name
	 * @param value
	 * @return
	 */
	public MessageWriter addCol(String name, String value) {
		addCol(name, new MessageElement(value));
		return this;
	}

	/**
	 * 增加列属性值
	 * @param name
	 * @param value
	 * @return
	 */
	public MessageWriter addCol(String name, int value) {
		addCol(name, new MessageElement(value));
		return this;
	}

	/**
	 * 增加列属性值
	 * @param name
	 * @param value
	 * @return
	 */
	public MessageWriter addCol(String name, float value) {
		addCol(name, new MessageElement(value));
		return this;
	}

	/**
	 * 设置列属性名与属性描述
	 * @param colNames
	 * @param colDesc
	 * @throws MessageException
	 */
	public void setColumnDefine(String[] colNames, String[] colDesc) throws MessageException {
		if (colNames.length != colDesc.length) {
			throw new MessageException("Column define error!");
		}
		for(String col: colNames){
			this.colNames.add(col);
		}
		for(String desc : colDesc){
			this.colDesc.add(desc);
		}
		if(this.colNames.size() != this.colDesc.size()){
			throw new MessageException("Column define error!");
		}
	}

	/**
	 * 增加列属性值
	 * @param name
	 * @param element
	 */
	public void addCol(String name, MessageElement element) {
		if (!colNames.contains(name)) {
			colNames.add(name);
			colDesc.add(name);
		}
		currentRow.put(name, element);
	}

	/**
	 * 将当前列增加到请求中
	 */
	public void addRow() {
		rowData.add(currentRow);
		currentRow = new HashMap<String, MessageElement>();
	}

	/**
	 * 将请求转换为 String , 使用 utf-8 编码
	 * @return
	 * @throws java.io.IOException
	 */
	public String serialize() throws IOException {
		return serialize("utf-8");
	}
	public String serialize(String encoding) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		toOutputStream(os);
		try {
			return os.toString("utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 将请求数据转换写入 OutputStream 中
	 * @param os
	 * @throws java.io.IOException
	 */
	public void toOutputStream(OutputStream os) throws IOException {
		OutputStreamWriter sw = new OutputStreamWriter(os);
		JsonWriter writer = new JsonWriter(sw);
		writer.setIndent("");
		saveMessage(writer);
		writer.close();
	}

	/**
	 * 将请求数组转换为json数据
	 * @param writer
	 * @throws java.io.IOException
	 */
	private void saveMessage(JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name("funcno").value(funcNo);
		String[] colNameArray = new String[colNames.size()];
		colNames.toArray(colNameArray);
		saveMessageArray(writer, "colname", colNameArray);

		String[] colDescArray = new String[colDesc.size()];
		colDesc.toArray(colDescArray);
		saveMessageArray(writer, "coldesc", colDescArray);

        MessageElement colCnt = new MessageElement(colNames.size());
        writer.name("colcnt");
        saveMessageElement(writer, colCnt);

		for (String k : attributes.keySet()) {
			if ("colname".equals(k) || "funcno".equals(k) || "coldesc".equals(k)) {
				continue;
			}
			MessageElement element = attributes.get(k);
			writer.name(k);
			saveMessageElement(writer, element);
		}

		saveRowData(writer);

		writer.endObject();
	}

	/**
	 * 转换列数据
	 * @param writer
	 * @throws java.io.IOException
	 */
	private void saveRowData(JsonWriter writer) throws IOException {
		writer.name("rowdata");
		writer.beginArray();
		String[] colNameArray = new String[colNames.size()];
		colNames.toArray(colNameArray);
        int count = 0;
		for (HashMap<String, MessageElement> row : rowData) {
			writer.beginArray();
			for (int i = 0; i < colNameArray.length; ++i) {
				MessageElement element = row.get(colNameArray[i]);
				saveMessageElement(writer, element);
			}
			writer.endArray();
            ++count;
		}
		writer.endArray();
        writer.name("rowcnt");
        MessageElement rowCnt = new MessageElement(count);
        saveMessageElement(writer, rowCnt);
	}

	/**
	 * 将 MessageElement 保持到json中
	 * @param writer
	 * @param element
	 * @throws java.io.IOException
	 */
	private void saveMessageElement(JsonWriter writer, MessageElement element) throws IOException {
		if (null == element)
			writer.nullValue();
		else if (element.isString())
			writer.value(element.getDataAsString());
		else if (element.isInteger())
			writer.value(element.getDataAsInt());
		else
			writer.value(element.getDataAsDouble());
	}

	/**
	 * 将 String[] 保存到 json 中
	 * @param writer
	 * @param name
	 * @param data
	 * @throws java.io.IOException
	 */
	private void saveMessageArray(JsonWriter writer, String name, String[] data) throws IOException {
		writer.name(name);
		writer.beginArray();
		for (String col : data) {
			writer.value(col);
		}
		writer.endArray();
	}

}
