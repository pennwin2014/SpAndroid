package com.supwisdom.cardlib;

import java.util.HashMap;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. File name:
 * com.supwisdom.cardlib.CardCommand.java Description: TODO Modify
 * History（或Change Log）: 操作类型（创建、修改等） 操作日期 操作者 操作内容简述 
 * 创建 2013-4-17 Tang Cheng
 * 
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
public final class CardCommand {
	/**
	 * 选择应用指令
	 */
	public static final String CMD_SELECT_EP_AID = "select_ep_aid";
	/**
	 * 读取电子钱包余额
	 */
	public static final String CMD_GET_EP_BALANCE = "get_ep_balance";
	/**
	 * 读取电子钱包流水
	 */
	public static final String CMD_GET_EP_RECORD = "get_ep_record";
	/**
	 * 读取电子钱包充值次数
	 */
	public static final String CMD_GET_EP_DPSCNT = "get_ep_dpscnt";
	/**
	 * 读取电子钱包消费次数
	 */
	public static final String CMD_GET_EP_PAYCNT = "get_ep_paycnt";

    public static final String CMD_INIT_FOR_LOAD = "ep_init4load";

    public static final String CMD_CREDIT_FOR_LOAD = "ep_credit4load";

    public static final String CMD_INIT_FOR_PURCHASE = "ep_init4purchase";

    public static final String CMD_DEBIT_FOR_PURCHASE = "ep_debit4purchase";

	/**
	 * APDU指令返回状态码
	 */
	public static final String APDU_RESPONSE = "apdu_response";
	/**
	 * 卡余额
	 */
	public static final String CARD_BALANCE = "card_balance";
	/**
	 * 卡充值次数
	 */
	public static final String CARD_DPSCNT = "card_dpscnt";
	/**
	 * 卡消费次数
	 */
	public static final String CARD_PAYCNT = "card_paycnt";

	/**
	 * 参数字典
	 */
	private HashMap<String, Object> mParameters = null;

	/**
	 * 
	 */
	public CardCommand() {
		mParameters = new HashMap<String, Object>();
	}

	/**
	* @description: 
	* @param name
	* @param value void
	* @author Tang Cheng
	* @create 2013-4-17
	*/
	public void setParameter(String name, Object value) {
		mParameters.put(name, value);
	}

	/**
	* @description: 
	* @param name
	* @param value void
	* @author Tang Cheng
	* @create 2013-4-17
	*/
	public void setParameter(String name, int value) {
		mParameters.put(name, String.valueOf(value));
	}

	/**
	* @description: 
	* @param name
	* @return String
	* @author Tang Cheng
	* @create 2013-4-17
	*/
	public String getParameterString(String name) {
		Object res = mParameters.get(name);
		if (res != null) {
			return res.toString();
		}
		return null;
	}

	/**
	* @description: 
	* @param name
	* @return int
	* @author Tang Cheng
	* @create 2013-4-17
	*/
	public int getParameterInt(String name) {
		String n = getParameterString(name);
		return Integer.parseInt(n);
	}

	/**
	* @description: 
	* @param name
	* @return Object
	* @author Tang Cheng
	* @create 2013-4-17
	*/
	public Object getParameter(String name) {
		return mParameters.get(name);
	}

	/**
	* @description:  void
	* @author Tang Cheng
	* @create 2013-4-17
	*/
	public void reset() {
		mParameters.clear();
	}

    public boolean hasParameter(String name){
        return mParameters.containsKey(name);
    }
}
