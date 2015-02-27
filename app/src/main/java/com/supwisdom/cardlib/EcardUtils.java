package com.supwisdom.cardlib;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. File name:
 * com.supwisdom.cardlib.EcardUtils.java Description: TODO Modify
 * History（或Change Log）: 操作类型（创建、修改等） 操作日期 操作者 操作内容简述 
 * 创建 2013-4-17 Tang Cheng
 * 
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
public final class EcardUtils {
	/**
	 * 大字节序
	 */
	public static final int BIT_BIG_ENDDIN = 0;
	/**
	 * 小字节序
	 */
	public static final int BIT_LITTLE_ENDDIN = 1;

	/**
	 * @description: 转换成 HEX 编码
	 * @param data
	 *            - byte 数组
	 * @return String - HEX 编码字符串
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public static String encodeHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; ++i) {
			buf.append(String.format("%02X", (int) data[i] & 0xFF));
		}
		return buf.toString();
	}

	/**
	 * @description: 将HEX字符串解码
	 * @param value
	 *            - HEX 字符串
	 * @return - byte[] - 返回解码后的byte数组
	 * @throws CardValueException
	 *             - HEX 是否传长度不对
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public static byte[] decodeHex(String value) throws CardValueException {
		if (null == value || value.length() % 2 != 0) {
			throw new CardValueException("value length must div by 2");
		}
		byte[] result = new byte[value.length() / 2];
		for (int i = 0; i < value.length(); i += 2) {
			int c = Integer.parseInt(value.substring(i, i + 2), 16);
			result[i / 2] = (byte) c;
		}
		return result;
	}

	/**
	 * @description: 将字符串压缩成 BCD 编码
	 * @param value
	 *            - 压缩的字符串，长度为2的整数倍
	 * @return - byte[] 返回BCD编码数组
	 * @throws CardValueException
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public static byte[] encodeBCD(String value) throws CardValueException {
		if (value.length() % 2 != 0) {
			throw new CardValueException("value length must div by 2");
		}
		byte[] res = new byte[value.length() / 2];
		for (int i = 0; i < value.length(); i += 2) {
			res[i / 2] = (byte) Integer.parseInt(value.substring(i, i + 1));
			res[i / 2] <<= 4;
			res[i / 2] |= ((byte) Integer.parseInt(value
                    .substring(i + 1, i + 2))) & 0x0F;
		}
		return res;
	}

	/**
	 * @description: 解码BCD
	 * @param data
	 *            - BCD数据
	 * @return String - 解码后的字符串
	 * @throws CardValueException
	 *             - BCD数据不正确
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public static String decodeBCD(byte[] data) throws CardValueException {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; ++i) {
			int t1 = (((int) data[i] & 0xF0) >> 4) & 0x0F;
			int t2 = ((int) data[i]) & 0x0F;
			if ((t1 < 0 || t1 > 9) || (t2 < 0 || t2 > 9)) {
				throw new CardValueException("Value must between 0 and 9");
			}
			buf.append(String.format("%d%d", t1, t2));
		}
		return buf.toString();
	}

	/**
	 * @description: byte 转换为整数
	 * @param raw
	 *            - byte 数组
	 * @param enddin
	 *            - 字节序
	 * @return int - 返回整数值
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public static int byte2int(byte[] raw, int enddin) {
		if (raw.length < 1 && raw.length > 4) {
			throw new RuntimeException("Length Error!");
		}
		int res = 0;
		for (int i = 0; i < raw.length; ++i) {
			if (enddin == BIT_BIG_ENDDIN) {
				res <<= 8;
				res |= ((int) raw[i]) & 0xFF;
			} else {
				res |= (((int) raw[i]) << (i * 8)) & 0xFF;
			}
		}
		return res;
	}
}
