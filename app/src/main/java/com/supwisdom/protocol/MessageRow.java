package com.supwisdom.protocol;

import java.util.HashMap;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd.
 * File name: com.supwisdom.protocol
 * Description: TODO
 * Modify History（或Change Log）:
 * 操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 * 创建 13-4-18 tangcheng
 * <p/>
 *
 * @author tangcheng
 * @version 1.0
 * @since 1.0
 */
public class MessageRow {
	/**
	 * 数据列
	 */
	private HashMap<String, MessageElement> rowData =
			new HashMap<String, MessageElement>();

	public MessageRow(){

	}
	public MessageRow(String[] colNames, MessageElement[] colValues){
		if(colNames.length != colValues.length){
			throw new RuntimeException("Column define's length is error!");
		}
		for(int i = 0; i < colNames.length; ++i){
			rowData.put(colNames[i], colValues[i]);
		}
	}
	public MessageRow(String[] colNames, String[] colValues){
		if(colNames.length != colValues.length){
			throw new RuntimeException("Column define's length is error!");
		}
		for(int i = 0; i < colNames.length; ++i){
			rowData.put(colNames[i], new MessageElement(colValues[i]));
		}
	}
	public MessageElement getElement(String name){
		return rowData.get(name);
	}

	public boolean hasElement(String name){
		return rowData.containsKey(name);
	}

    public String getElementAsString(String name) {
        MessageElement e = rowData.get(name);
        if(null == e)
            return null;
        return e.getDataAsString();
    }

    public int getElementAsInt(String name) {
        MessageElement e = rowData.get(name);
        if(null == e)
            return 0;
        return e.getDataAsInt();
    }

    public double getElementAsDouble(String name) {
        MessageElement e = rowData.get(name);
        if(null == e)
            return 0.0;
        return e.getDataAsDouble();

    }
}
