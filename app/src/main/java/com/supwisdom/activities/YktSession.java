package com.supwisdom.activities;

import android.nfc.Tag;
import android.util.Log;

import com.supwisdom.R;
import com.supwisdom.cardlib.EcardLib;
import com.supwisdom.cardlib.EcardUtils;
import com.supwisdom.protocol.EpaySession;
import com.supwisdom.protocol.MessageReader;
import com.supwisdom.protocol.MessageWriter;
import com.supwisdom.protocol.WebAPISession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd.
 * File name: com.supwisdom.ecardterm
 * Description: TODO Modify History（或Change Log）:
 * 操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 * 创建 13-4-22 tangcheng
 * <p/>
 *
 * @author tangcheng
 * @version 1.0
 * @since 1.0
 */
public class YktSession {
    private static final long LOGIN_EXPIRED_INTERVAL = 60 * 10;
    /**
     * YktSession 实例
     */
    private static YktSession yktSession = null;
    private static final String tag = "supwisdom.webservice";
    private String operCode;
    private int checkSum;
    private String sessionId;
    private String samNo = "000000000000";
    private Tag nfcTag = null;
    private EcardLib cardLib = null;

    public String getSessionId() {
        return sessionId;
    }

    /**
     * Create YktSession instance
     *
     * @return YktSession object
     */
    public static synchronized YktSession getInstance() {
        if (null == yktSession) {
            yktSession = new YktSession();
        }
        return yktSession;
    }


    private WebAPISession mSession = new WebAPISession();
    private EpaySession epaySession = new EpaySession();

    private int cardNo = 0;
    private String stuEmpNo = "";
    private boolean isLogin = false;
    private String cardPhyId = null;
    private int custId = 0;
    private Date lastLoginTime = null;

    public String getStuEmpNo() {
        return stuEmpNo;
    }

    public void setCardNo(int cardNo) {
        this.cardNo = cardNo;
    }

    public void setCardPhyId(String cardPhyId) {
        this.cardPhyId = cardPhyId;
    }

    public void setStuEmpNo(String stuEmpNo) {
        this.stuEmpNo = stuEmpNo;
    }

    public String getCardPhyId() {
        return cardPhyId;
    }

    public int getCardNo() {
        return cardNo;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public String getOperCode() {
        return operCode;
    }

    private YktSession() {
        // pass
    }

    public WebAPISession getWebAPISession() {
        return mSession;
    }

    public boolean isWebSessionAuthorized() {
        return mSession.isAuthorized();
    }

    public boolean userLogin(int cardNo, String phyId, int custId) {
        isLogin = true;
        this.cardNo = cardNo;
        this.cardPhyId = phyId;
        this.custId = custId;
        this.lastLoginTime = new Date();
        return true;
    }

    public void setAuth(String sessionId, String operCode, int checkSum){
        this.operCode = operCode;
        this.checkSum = checkSum;
        this.sessionId = sessionId;
    }

    public void resetAuth() {
        this.operCode = "";
        this.checkSum = 0;
    }

    public boolean isLogin() {
        return this.isLogin;
    }

    public void logout() {
        cardNo = 0;
        cardPhyId = null;
        isLogin = false;
        this.lastLoginTime = null;
    }

    public boolean checkExpireLogin(){
        if(!isLogin)
            return false;
        Date now = new Date();
        if(now.getTime()  < lastLoginTime.getTime()){
            this.logout();
            return false;
        } else {
            long span = (now.getTime() - lastLoginTime.getTime()) / 1000;
            if(span >= LOGIN_EXPIRED_INTERVAL){
                this.logout();
                return false;
            } else {
                return true;
            }
        }
    }

    public void updateLogin(){
        if(!isLogin)
            return;
        this.lastLoginTime = new Date();
    }

    public boolean sendYktRequest(MessageWriter request, MessageReader response, int timeout){
        try {
            String requestBuffer = request.serialize();
            String url = SPApplication.getInstance().getString(R.string.app_ykt_url) +
                    "/ecardservice/ecardapi";
            if(timeout < 10){
                timeout = 10;
            }
            StringBuffer responseBuffer = new StringBuffer();
            if(mSession.callYKTApi(url, requestBuffer, responseBuffer, timeout * 1000)){
                response.loadFromString(responseBuffer.toString());
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(tag, "Cannot request YktAPI , " + e);
            return false;
        }
    }

    public String md5(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        byte[] byteArray = messageDigest.digest();

        return EcardUtils.encodeHex(byteArray).toUpperCase();
    }

    public String getSamNo() {
        return samNo;
    }

    public void setSamNo(String samNo) {
        this.samNo = samNo;
    }

    public Tag getNfcTag(){
        return this.nfcTag;
    }

    public void setNfcTag(Tag tag){
        this.nfcTag = tag;
        this.cardLib = null;
    }

    public void resetNfcTag() {
        this.nfcTag = null;
        if(this.cardLib != null){
            this.cardLib.close();
        }
        this.cardLib = null;
    }

    public EcardLib createFromTag(Tag tag){
        if(this.nfcTag != null){
            this.resetNfcTag();
        }
        this.nfcTag = tag;
        this.cardLib = EcardLib.getCardlib(SPApplication.getInstance().getString(R.string
                .cardlib_name), tag);
        return this.cardLib;
    }

    public EcardLib getDefaultCardLib(){
        if(null == this.cardLib){
            if(null == this.nfcTag){
                return null;
            }
            this.cardLib = EcardLib.getCardlib(SPApplication.getInstance().getString(R.string
                    .cardlib_name), this.nfcTag);
        }
        return this.cardLib;
    }

    public EcardLib testAndGetDefaultCardLib() {
        EcardLib card = getDefaultCardLib();
        if(card != null){
            if(card.testConnectionOk()){
                return card;
            }
        }
        return null;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public EpaySession getEpaySession(){
        return this.epaySession;
    }
}

