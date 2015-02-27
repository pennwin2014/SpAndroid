package com.supwisdom.cardlib;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. 
 * File name: com.supwisdom.cardlib.CardFileDefine.java
 * Description: TODO Modify History（或Change Log）: 
 *	操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 *  创建 2013-4-17 Tang Cheng
 * <p/>
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
public final class CardFileDefine {
	/**
	 * 不需要权限
	 */
	public static final int AUTH_NONE = 1;
	/**
	 * MAC 读权限
	 */
	public static final int AUTH_MAC_READ = 2;
	/**
	 * MAC 写权限
	 */
	public static final int AUTH_MAC_WRITE = 4;
	/**
	 * 外部认证读权限
	 */
	public static final int AUTH_EXTAUTH_READ = 8;
	/**
	 * 外部认证写权限
	 */
	public static final int AUTH_EXTAUTH_WRITE = 16;
	
	
	/**
	 * 文件ID
	 */
	private final int fileId;
	/**
	 * 文件长度
	 */
	private final int length;
	/**
	 * 文件权限
	 */
	private final int authType;
	
	public int getFileId() {
		return fileId;
	}

	public int getLength() {
		return length;
	}

	public int getAuthType() {
		return authType;
	}

	public CardFileDefine(int fileId, int fileLength, int authType){
		this.fileId = fileId;
		this.length = fileLength;
		this.authType = authType;
	}
}
