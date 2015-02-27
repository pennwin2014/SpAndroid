package com.supwisdom.protocol;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd.
 * File name: com.supwisdom.protocol.MessageElement.java
 * Description: TODO Modify History（或Change Log）:
 * 操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 * 创建 13-4-18 tangcheng
 * <p/>
 *
 * @author tangcheng
 * @version 1.0
 * @since 1.0
 */
public class MessageElement implements Cloneable {
	private static final int STRING_TYPE = 0;
	private static final int INT_TYPE = 0;
	private static final int DOUBLE_TYPE = 0;
	private String mData = null;
	private int dataType;

	@Override
	protected Object clone() throws CloneNotSupportedException {
		MessageElement other = new MessageElement(this);
		return other;
	}

	public MessageElement(String data) {
		if (null != data) {
			mData = data;
		} else {
			mData = new String("");
		}
		dataType = STRING_TYPE;
	}

	public MessageElement(int data) {
		mData = String.valueOf(data);
		dataType = INT_TYPE;
	}

	public MessageElement(double data) {
		mData = String.valueOf(data);
		dataType = DOUBLE_TYPE;
	}

	public MessageElement(MessageElement another) {
		this.mData = another.mData;
		dataType = another.dataType;
	}

	public String getDataAsString() {
		return mData;
	}

	public int getDataAsInt() {
		return Integer.parseInt(mData);
	}

	public double getDataAsDouble() {
		return Double.parseDouble(mData);
	}

	public boolean isString() {
		return dataType == STRING_TYPE;
	}
	public boolean isInteger() {
		return dataType == INT_TYPE;
	}
	public boolean isFloat() {
		return dataType == DOUBLE_TYPE;
	}
}
