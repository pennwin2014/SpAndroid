package com.supwisdom.cardlib;

import java.nio.charset.Charset;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. 
 * File name: com.supwisdom.cardlib.CardFieldDefine.java
 * Description: TODO Modify History（或Change Log）: 
 *	操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 *  创建 2013-4-17 Tang Cheng
 * <p/>
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
public abstract class CardFieldDefine implements Comparable<CardFieldDefine> {
	/**
	 * 属性名
	 */
	private final String name;
	/**
	 * 所在文件ID
	 */
	private final int fileId;
	/**
	 * 所在文件的偏移
	 */
	private final int offset;
	/**
	 * 数据长度
	 */
	private final int rawLength;
	/**
	 * 是否可读
	 */
	private boolean readable;
	/**
	 * 是否可写
	 */
	private boolean writable;
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(CardFieldDefine other) {
		if(null == other)
			return -1;
		if(this.offset < other.offset)
			return -1;
		else if(this.offset > other.offset)
			return 1;
		else
			return 0;
	}

	public boolean isReadable() {
		return readable;
	}

	public boolean isWritable() {
		return writable;
	}
	
	public void setReadable(boolean r){
		this.readable = r;
	}
	
	public void setWritable(boolean r){
		this.writable = r;
	}

	public CardFieldDefine(String name, int fileId, int offset, int rawLength) {
		this.name = name;
		this.fileId = fileId;
		this.offset = offset;
		this.rawLength = rawLength;
		this.readable = true;
		this.writable = true;
	}

	public java.lang.String getName() {
		return this.name;
	}

	public int getFileId() {
		return this.fileId;
	}

	public int getOffset() {
		return this.offset;
	}

	public int getRawLength() {
		return this.rawLength;
	}

	public abstract String getValue(byte[] raw) throws CardValueException;
}

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. 
 * File name: com.supwisdom.cardlib.CardFieldDefine.java
 * Description: TODO Modify History（或Change Log）: 
 *	操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 *  创建 2013-4-17 Tang Cheng
 * <p/>
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
class StringFieldDefine extends CardFieldDefine {
	public static final String CODE_ASCII = "ascii";
	public static final String CODE_USC = "usc";

	private final String code;

	public StringFieldDefine(String name, int fileId, int offset,
			int rawLength, String code) {
		super(name, fileId, offset, rawLength);
		this.code = code;
	}
	
	public StringFieldDefine(String name, int fileId, int offset,
			int rawLength) {
		super(name, fileId, offset, rawLength);
		this.code = CODE_ASCII;
	}

	@Override
	public String getValue(byte[] raw) {
		if (CODE_ASCII.equals(code)) {
            int endPos;
            for(endPos = 0;endPos < getRawLength(); ++endPos){
                if(raw[endPos] == 0)
                    break;
            }
			return new String(raw, 0, endPos, Charset.forName("gbk"));
		} else {
			throw new RuntimeException("Not Support Coding");
		}
	}
}

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. 
 * File name: com.supwisdom.cardlib.CardFieldDefine.java
 * Description: TODO Modify History（或Change Log）: 
 *	操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 *  创建 2013-4-17 Tang Cheng
 * <p/>
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
class IntegerFieldDefine extends CardFieldDefine {
	public final static int BIT_BIG_ENDDIN = 1;
	public final static int BIT_LITTLE_ENDDIN = 2;
	private final int bit_enddin;

	public IntegerFieldDefine(String name, int fileId, int offset,
			int rawLength, int enddin) {
		super(name, fileId, offset, rawLength);
		this.bit_enddin = enddin;
	}
	
	public IntegerFieldDefine(String name, int fileId, int offset,
			int rawLength){
		super(name, fileId, offset, rawLength);
		this.bit_enddin = BIT_BIG_ENDDIN;
	}

	@Override
	public String getValue(byte[] raw) {
		if (raw.length < 1 && raw.length > 4) {
			throw new RuntimeException("Length Error!");
		}
		int res = 0;
		for (int i = 0; i < raw.length; ++i) {
			if (this.bit_enddin == BIT_BIG_ENDDIN) {
				res <<= 8;
				res |= ((int) raw[i]) & 0xFF;
			} else {
				res |= (((int) raw[i]) << (i * 8)) & 0xFF;
			}
		}
		return String.valueOf(res);
	}
}

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. 
 * File name: com.supwisdom.cardlib.CardFieldDefine.java
 * Description: TODO Modify History（或Change Log）: 
 *	操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 *  创建 2013-4-17 Tang Cheng
 * <p/>
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
class BCDFieldDefine extends CardFieldDefine {
	public BCDFieldDefine(String name, int fileId, int offset,
			int rawLength){
		super(name, fileId, offset, rawLength);
	}

	@Override
	public String getValue(byte[] raw) throws CardValueException {
		return EcardUtils.decodeBCD(raw);
	}
}

class BCDIntegerFieldDefine extends CardFieldDefine {
    public BCDIntegerFieldDefine(String name, int fileId, int offset, int rawLength){
        super(name, fileId, offset, rawLength);
    }

    @Override
    public String getValue(byte[] raw) throws CardValueException {
        String value = EcardUtils.decodeBCD(raw);
        if(null == value)
            return null;
        int startPos = 0;
        while(startPos < value.length()){
            if(value.charAt(startPos) != '0'){
                break;
            }
            ++startPos;
        }
        String res = value.substring(startPos);
        if(null == res || "".equals(res)){
            return "0";
        }
        return res;
    }
}