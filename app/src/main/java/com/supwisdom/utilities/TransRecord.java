package com.supwisdom.utilities;

/**
 * Created by Penn on 2015-02-28.
 */
public class TransRecord {
    private int transno;
    private int cardno;
    private int lastTransCount;
    private int lastLimitAmount;
    private int lastAmount;
    private int lastTransFlag;
    private String lastTermno;
    private String lastDatetime;
    private int cardBeforeBalance;
    private int cardBeforeCount;
    private int amount;
    private int extraAmount;
    private String transDatatime;
    private String samNo;
    private String tac;
    private int transFlag;
    private String reserve;
    private String crc;

    public int getLastLimitAmount() {
        return lastLimitAmount;
    }

    public void setLastLimitAmount(int lastLimitAmount) {
        this.lastLimitAmount = lastLimitAmount;
    }

    public int getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(int lastAmount) {
        this.lastAmount = lastAmount;
    }

    public int getLastTransFlag() {
        return lastTransFlag;
    }

    public void setLastTransFlag(int lastTransFlag) {
        this.lastTransFlag = lastTransFlag;
    }

    public String getLastTermno() {
        return lastTermno;
    }

    public void setLastTermno(String lastTermno) {
        this.lastTermno = lastTermno;
    }

    public String getLastDatetime() {
        return lastDatetime;
    }

    public void setLastDatetime(String lastDatetime) {
        this.lastDatetime = lastDatetime;
    }

    public int getCardBeforeBalance() {
        return cardBeforeBalance;
    }

    public void setCardBeforeBalance(int cardBeforeBalance) {
        this.cardBeforeBalance = cardBeforeBalance;
    }

    public int getCardBeforeCount() {
        return cardBeforeCount;
    }

    public void setCardBeforeCount(int cardBeforeCount) {
        this.cardBeforeCount = cardBeforeCount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(int extraAmount) {
        this.extraAmount = extraAmount;
    }

    public String getTransDatatime() {
        return transDatatime;
    }

    public void setTransDatatime(String transDatatime) {
        this.transDatatime = transDatatime;
    }

    public String getSamNo() {
        return samNo;
    }

    public void setSamNo(String samNo) {
        this.samNo = samNo;
    }

    public String getTac() {
        return tac;
    }

    public void setTac(String tac) {
        this.tac = tac;
    }

    public int getTransFlag() {
        return transFlag;
    }

    public void setTransFlag(int transFlag) {
        this.transFlag = transFlag;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public int getTransno() {
        return transno;
    }

    public void setTransNo(int transno) {
        this.transno = transno;
    }

    public int getCardno(){
        return cardno;
    }

    public void setCardno(int cardno){
        this.cardno = cardno;
    }

    public int getLastTransCount(){
        return lastTransCount;
    }

    public void setLastTransCount(int lastTransCount){
        this.lastTransCount = lastTransCount;
    }



}
