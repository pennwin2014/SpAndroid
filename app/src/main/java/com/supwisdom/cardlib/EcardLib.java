package com.supwisdom.cardlib;

import android.nfc.Tag;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. File name:
 * com.supwisdom.cardlib.EcardLib.java Description: TODO Modify History（或Change
 * Log）: 操作类型（创建、修改等） 操作日期 操作者 操作内容简述 创建 2013-4-172006-4-29 Tang Cheng
 * <p/>
 * 
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
public abstract class EcardLib {
	/**
	 * APDU指令成功的状态码
	 */
	public static final byte[] SW_SUCCESS = new byte[] { (byte) 0x90, 0x00 };
	/**
	 * 卡客户名
	 */
	public static final String FIELD_CUSTNAME = "F_CUSTNAME";
	/**
	 * 学工号
	 */
	public static final String FIELD_STUEMPNO = "F_STUEMPNO";
	/**
	 * 性别
	 */
	public static final String FIELD_SEX = "F_SEX";
	/**
	 * 卡版本号（黑名单版本号）
	 */
	public static final String FIELD_CARDVERNO = "F_CARDVERNO";
	/**
	 * 卡余额
	 */
	public static final String FIELD_BALANCE = "F_BALANCE";
	/**
	 * 卡消费次数
	 */
	public static final String FIELD_PAYCNT = "F_PAYCNT";
	/**
	 * 卡充值次数
	 */
	public static final String FIELD_DPSCNT = "F_DPSCNT";
	/**
	 * 卡有效期
	 */
	public static final String FIELD_EXPIREDATE = "F_EXPIREDATE";
	/**
	 * 卡应用序列号
	 */
	public static final String FIELD_APPSRLNO = "F_APPSERIALNO";
	/**
	 * 卡收费类别（卡权限类别）
	 */
	public static final String FIELD_FEETYPE = "F_FEETYPE";
	/**
	 * 卡号（交易卡号）
	 */
	public static final String FIELD_CARDNO = "F_CARDNO";
	/**
	 * 显示卡号
	 */
	public static final String FIELD_SHOWCARDNO = "F_SHOWCARDNO";
	/**
	 * 卡状态 0-正常 1—锁卡 2-冻结 3-挂失 4-注销 5-过期 6-后付费锁卡
	 */
	public static final String FIELD_CARDSTATUS = "F_CARDSTATUS";
	/**
	 * 证件号码
	 */
	public static final String FIELD_IDNO = "F_IDNO";
	/**
	 * 证件类型
	 */
	public static final String FIELD_IDTYPE = "F_IDTYPE";
	/**
	 * 部门代码
	 */
	public static final String FIELD_DEPTCODE = "F_DEPTCODE";
	/**
	 * 客户类别
	 */
	public static final String FIELD_CUSTTYPE = "F_CUSTTYPE";
	/**
	 * 客户号
	 */
	public static final String FIELD_CUSTID = "F_CUSTID";
	/**
	 * 卡单次消费限额
	 */
	public static final String FIELD_LIMITPERONCE = "F_LIMITPERONCE";
	/**
	 * 卡日累计消费限额
	 */
	public static final String FIELD_LIMITPERDAY = "F_LIMITPERDAY";
	/**
	 * 卡密码
	 */
	public static final String FIELD_PASSWORD = "F_PASSWORD";

	/**
	 * 读卡属性
	 */
	protected Map<String, String> mReadFieldMap;

	/**
	 * 写卡属性
	 */
	protected Map<String, String> mWriteFieldMap;
	/**
	 * 卡物理ID号
	 */
	protected byte[] cardUid = null;

	/**
	 * NFC Tag
	 */
	protected Tag nfcTag;

	/**
	 * 支持卡类型字典
	 */
	private static Hashtable<String, Class> sCardlibList = new Hashtable<String, Class>();
	private static boolean sInitialized = false;

	private static void initCardLib() {
		if (sInitialized)
			return;
		sCardlibList.put(CpuCardLib.name, CpuCardLib.class);
		sInitialized = true;
	}

	/**
	 * @description: 读取支持的卡类型列表
	 * @return String[] - 卡类型名称列表
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public static String[] getCardTypeList() {
		initCardLib();
		ArrayList<String> names = new ArrayList<String>();
		for (String cardName : sCardlibList.keySet()) {
			names.add(cardName);
		}
		return (String[]) names.toArray();
	}

	/**
	 * @description: 创建卡类别实例
	 * @param name
	 *            - 卡类别名称
	 * @param tag
	 *            - NFC tag @see android.nfc.Tag
	 * @return EcardLib - 返回卡类别实例
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public static EcardLib getCardlib(String name, Tag tag) {
		initCardLib();
		if (sCardlibList.containsKey(name)) {
			Class<EcardLib> cls = sCardlibList.get(name);
			try {
				Class[] ptr = new Class[] { Tag.class };
				Constructor ctor = cls.getConstructor(ptr);
				Object[] objs = new Object[] { tag };
				return (EcardLib) ctor.newInstance(objs);
			} catch (InstantiationException e) {
				return null;
			} catch (IllegalAccessException e) {
				return null;
			} catch (NoSuchMethodException e) {
				return null;
			} catch (IllegalArgumentException e) {
				return null;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	protected EcardLib(Tag tag) {
		mReadFieldMap = new Hashtable<String, String>();
		mWriteFieldMap = new Hashtable<String, String>();
		this.nfcTag = tag;
        byte [] uid = tag.getId();
        this.cardUid = new byte[uid.length];
        for(int i = 0;i < uid.length; ++i)
            this.cardUid[i] = uid[uid.length - i - 1];
	}

	/**
	 * @description: 重置缓存
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public void reset() {
		mReadFieldMap.clear();
		mWriteFieldMap.clear();
	}

	/**
	 * @description: 关闭读卡连接
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public void close() {
		reset();
	}

	/**
	 * @description: 设置读卡属性
	 * @param fieldName
	 *            - 属性名称
	 * @return EcardLib - 返回本实例
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public EcardLib setFieldRead(String fieldName) {
		mReadFieldMap.put(fieldName, "");
		return this;
	}

	/**
	 * @description: 获取卡属性值
	 * @param fieldName
	 *            - 卡属性名
	 * @return String - null 表示未读到该属性，
	 * @throws CardException
	 *             - 表示未设置该属性进行读取
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public String getFieldValue(String fieldName) throws CardException {
		if (!mReadFieldMap.containsKey(fieldName)) {
			throw new CardException("未设置读<" + fieldName + ">");
		}
		String res = mReadFieldMap.get(fieldName);
		if (null == res) {
			return null;
		}
		return res;
	}

	/**
	 * @description: 获取整形卡属性值
	 * @param fieldName
	 *            - 卡属性名
	 * @return int - 返回卡属性值，当未读到卡属性时返回 0
	 * @throws CardException
	 *             - 表示未设置该属性进行读取
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public int getFieldValueInt(String fieldName) throws CardException {
		String res = getFieldValue(fieldName);
		if (res != null && !res.isEmpty()) {
			return Integer.parseInt(res);
		}
		return 0;
	}

	/**
	 * @description: 设置写卡属性与新的数据
	 * @param fieldName
	 *            - 属性名称
	 * @param fieldValue
	 *            - 属性值
	 * @return EcardLib - 返回本实例
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public EcardLib setFieldValue(String fieldName, String fieldValue) {
		mWriteFieldMap.put(fieldName, fieldValue);
		return this;
	}

	/**
	 * @description: 执行卡指令(HEX格式)
	 * @param cmd
	 *            - 指令
	 * @return String - 返回数据内容
	 * @throws java.io.IOException
	 *             - NFC底层操作失败
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public abstract String cardCommandHex(String cmd) throws IOException;

	/**
	 * @description: 执行卡指令
	 * @param cmd
	 *            - 指令
	 * @return byte[] - 返回的数据
	 * @throws java.io.IOException
	 *             - NFC底层操作失败
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public abstract byte[] cardCommand(byte[] cmd) throws IOException;

	/**
	 * @description:
	 * @throws CardException
	 *             - 可能卡片状态异常
	 * @throws java.io.IOException
	 *             - NFC通讯出现错误
	 * @throws CardNotSupportException
	 *             - 不支持的操作
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public final void readCard() throws CardException, IOException,
			CardNotSupportException, CardValueException {
		doReadCard();
	}

	/**
	 * @description:
	 * @throws CardException
	 *             - 可能卡片状态异常
	 * @throws java.io.IOException
	 *             - NFC通讯出现错误
	 * @throws CardNotSupportException
	 *             - 不支持的操作
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	protected void doReadCard() throws CardException, IOException,
			CardNotSupportException, CardValueException {
		CardCommand request = new CardCommand();
		CardCommand response = new CardCommand();

		if (!executeCardCommand(CardCommand.CMD_SELECT_EP_AID, request,
				response)) {
			throw new CardException("Can't select AID");
		}
		if (mReadFieldMap.containsKey(FIELD_DPSCNT)) {
			request.reset();
			response.reset();
			if (!executeCardCommand(CardCommand.CMD_GET_EP_DPSCNT, request,
					response)) {
				throw new CardException("Can't get card dpscnt");
			}
			mReadFieldMap.put(FIELD_DPSCNT,
					response.getParameterString(CardCommand.CARD_DPSCNT));
			mReadFieldMap.put(FIELD_BALANCE,
					response.getParameterString(CardCommand.CARD_BALANCE));
		}
		if (mReadFieldMap.containsKey(FIELD_PAYCNT)) {
			request.reset();
			response.reset();
			if (!executeCardCommand(CardCommand.CMD_GET_EP_PAYCNT, request,
					response)) {
				throw new CardException("Can't get card paycnt");
			}
			mReadFieldMap.put(FIELD_PAYCNT,
					response.getParameterString(CardCommand.CARD_PAYCNT));
			mReadFieldMap.put(FIELD_BALANCE,
					response.getParameterString(CardCommand.CARD_BALANCE));
		}

		if (mReadFieldMap.containsKey(FIELD_BALANCE)) {
			if (getFieldValue(FIELD_BALANCE) == "") {
				request.reset();
				response.reset();
				if (!executeCardCommand(CardCommand.CMD_GET_EP_BALANCE,
						request, response)) {
					throw new CardException("Can't get card balance");
				}
				mReadFieldMap.put(FIELD_BALANCE,
						response.getParameterString(CardCommand.CARD_BALANCE));
			}
		}

		Integer[] fileList = getReadFileList();
		for (Integer fileId : fileList) {
			String command = null;
			CardFileDefine fileDefine = this.getFileDefine(fileId);
			if (null == fileDefine) {
				throw new CardException(String.format("File %d not exists",
                        fileId));
			}
			if ((fileDefine.getAuthType() & CardFileDefine.AUTH_EXTAUTH_READ) != 0) {
				// need external auth !
				// TODO: 增加功能
				throw new CardNotSupportException();
			}
			byte[] fileContent = null;
			if ((fileDefine.getAuthType() & CardFileDefine.AUTH_MAC_READ) != 0) {
				fileContent = readBinaryFileWithMac(fileDefine, 0,
						fileDefine.getLength());
			} else {
				fileContent = readBinaryFile(fileDefine, 0,
						fileDefine.getLength());
			}
			if (null == fileContent) {
				throw new CardException(String.format("读取卡文件%d失败", fileId));
			}
			CardFieldDefine[] fields = getFieldDefinesByFile(fileId);
			if (null == fields) {
				continue;
			}
			for (CardFieldDefine field : fields) {
				int endpos = field.getOffset() + field.getRawLength();
				if (endpos > fileContent.length) {
					throw new CardException("Field <" + field.getName()
							+ ">define error");
				}
				byte[] fieldData = Arrays.copyOfRange(fileContent,
                        field.getOffset(), endpos);
				mReadFieldMap.put(field.getName(), field.getValue(fieldData));
			}
		}
	}

	/**
	 * @description: 读取二进制文件
	 * @param fileDefine
	 *            - CardFileDefine class
	 * @param offset
	 *            - 文件偏移
	 * @param length
	 *            - 读取的数据长度
	 * @return byte[] - 返回文件数据， 返回 null 读取失败
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public byte[] readBinaryFile(CardFileDefine fileDefine, int offset,
			int length) throws IOException {
		String cmd = String.format("00B0%02X%02X%02X",
                (fileDefine.getFileId() + 0x80) & 0xFF, offset & 0xFF,
                length & 0xFF);
		try {
			return cardCommand(EcardUtils.decodeHex(cmd));
		} catch (CardValueException e) {
			throw new RuntimeException("Read Binary Command Error");
		}
	}

	/**
	 * @description: Read Binary file with MAC
	 * 
	 * @param fileDefine
	 *            - File define class
	 * @param offset
	 *            - read file offset
	 * @param length
	 *            - read file length
	 * @return - file content
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public byte[] readBinaryFileWithMac(CardFileDefine fileDefine, int offset,
			int length) {
		return null;
	}

	/**
	 * @description: Get Card UID
	 * @return byte[] - card uid , or null
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public byte[] getCardUid() {
		return cardUid;
	}

	/**
	 * @description: get card UID in hex format
	 * @return String - card uid (hex format), or null
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public String getCardUidHex() {
		if (null == cardUid) {
			return null;
		}
		return EcardUtils.encodeHex(cardUid);
	}

	/**
	 * @description:
	 * @return Integer[]
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	protected abstract Integer[] getReadFileList();

	/**
	 * @description: void
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public final void writeCard() {

	}

	/**
	 * @description: 获取文件定义
	 * @param fileId
	 *            - 文件ID
	 * @return CardFileDefine - 文件定义
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public abstract CardFileDefine getFileDefine(int fileId);

	/**
	 * @description: 获取文件的属性定义, 按照offset排序
	 * @param fileId
	 * @return CardFieldDefine[]
	 * @author Tang Cheng
	 * @create 2013-4-17
	 */
	public abstract CardFieldDefine[] getFieldDefinesByFile(int fileId);

	public abstract boolean executeCardCommand(String cmd, CardCommand request,
			CardCommand response) throws IOException;

    public abstract boolean testConnectionOk();

    public abstract int purchase(int amount);
}
