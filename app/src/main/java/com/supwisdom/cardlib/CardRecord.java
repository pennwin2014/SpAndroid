package com.supwisdom.cardlib;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd.
 * File name: com.supwisdom.cardlib
 * Description: TODO Modify History（或Change Log）:
 * 操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 * 创建 13-4-23 tangcheng
 * <p/>
 *
 * @author tangcheng
 * @version 1.0
 * @since 1.0
 */
public final class CardRecord {
    private String samNo = null;
    private String transDate = null;
    private String transTime = null;
    private int amount = 0;
    private int transType = 0;
    private int cardCnt = 0;

    public CardRecord(String samNo, String transDate, String transTime, int amount, int transType){
        this.samNo = samNo;
        this.transDate = transDate;
        this.transTime = transTime;
        this.amount = amount;
        this.transType = transType;
    }

    public CardRecord(){
        //
    }

    public int getAmount() {
        return amount;
    }

    public String getSamNo() {
        return samNo;
    }

    public String getTransDate() {
        return transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    public int getTransType() {
        return transType;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setSamNo(String samNo) {
        this.samNo = samNo;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public int getCardCnt() {
        return cardCnt;
    }

    public void setCardCnt(int cardCnt) {
        this.cardCnt = cardCnt;
    }
}
