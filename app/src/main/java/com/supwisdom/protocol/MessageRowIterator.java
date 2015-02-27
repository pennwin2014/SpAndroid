package com.supwisdom.protocol;

import java.util.Iterator;


public class MessageRowIterator implements Iterator<MessageRow> {
	/**
	 * 遍历的列数据
	 */
	private MessageRow[] mRowData = null;
	/**
	 * 数据集地址
	 */
	private int mReadPtr = 0;

	/**
	 * constructor
	 * @param rowData
	 */
	protected MessageRowIterator(MessageRow[] rowData){
		this.mRowData = rowData;
		this.mReadPtr = 0;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 * @return
	 */
	@Override
	public boolean hasNext() {
		if(mReadPtr < mRowData.length){
			return true;
		}
		return false;
	}

	/**
	 * @see java.util.Iterator#next()
	 * @return
	 */
	@Override
	public MessageRow next() {
		if(!hasNext()){
			return null;
		}
		return mRowData[mReadPtr++];
	}

	/**
	 * not implement
	 */
	@Override
	public void remove(){
	}
}
