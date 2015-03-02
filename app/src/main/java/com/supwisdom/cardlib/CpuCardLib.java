package com.supwisdom.cardlib;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import com.supwisdom.utilities.ErrorDef;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;

import com.supwisdom.utilities.PsamCard;
import com.supwisdom.utilities.ErrorInfo;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd. File name:
 * com.supwisdom.cardlib.CpuCardLib.java Description: TODO Modify
 * History（或Change Log）: 操作类型（创建、修改等） 操作日期 操作者 操作内容简述 创建 2013-4-17 Tang Cheng
 *
 * @author Tang Cheng
 * @version 1.0
 * @since 1.0
 */
public class CpuCardLib extends EcardLib {
    private static final String RECORD_CARDCNT = "record_cardcnt";
    private static final String RECORD_TRANSDATE = "record_transdate";
    private static final String RECORD_TRANSTIME = "record_transtime";
    private static final String RECORD_AMOUNT = "record_amount";
    private static final String RECORD_SAMNO = "record_samno";
    private static final String RECORD_TRANSTYPE = "record_transtype";

    /**
     * tag , for Android log use only
     */
    private static final String tag = "com.supwisdom.cardlib.cpucardlib";

    /**
     * Card Library name
     */
    public static final String name = "supwisdom.kscpucard";

    /**
     * Card file definition dict
     */
    private static final Hashtable<Integer, CardFileDefine> sCardFileMap =
            new Hashtable<Integer, CardFileDefine>();

    /**
     * Card field definition dict
     */
    private static final Hashtable<String, CardFieldDefine> sCardFieldMap =
            new Hashtable<String, CardFieldDefine>();

    private static final ArrayList<CardFieldDefine> sCardRecordFields =
            new ArrayList<CardFieldDefine>();

    /**
     * 初始话状态
     */
    private static boolean sInitialized = false;

    /**
     * @return boolean - true 表示成功， false 表示失败
     * @description: 初始化卡结构定义
     * @author Tang Cheng
     * @create 2013-4-17
     */
    public synchronized static boolean initialize() {
        if (sInitialized)
            return true;
        int fileId = 0x15;
        int offset = 0;
        sCardFileMap.put(fileId, new CardFileDefine(fileId, 56,
                CardFileDefine.AUTH_MAC_WRITE));
        sCardFieldMap.put(EcardLib.FIELD_CARDNO, new BCDIntegerFieldDefine(
                EcardLib.FIELD_CARDNO, fileId, offset, 10));
        offset += 10;

        sCardFieldMap.put(EcardLib.FIELD_SHOWCARDNO, new StringFieldDefine(
                EcardLib.FIELD_SHOWCARDNO, fileId, offset, 10));
        offset += 10;

        sCardFieldMap.put(EcardLib.FIELD_CARDSTATUS, new IntegerFieldDefine(
                EcardLib.FIELD_CARDSTATUS, fileId, offset, 1));
        ++offset;

        sCardFieldMap.put(EcardLib.FIELD_CARDVERNO, new BCDFieldDefine(
                EcardLib.FIELD_CARDVERNO, fileId, offset, 7));
        offset += 7;
        offset += 6; // $$

        sCardFieldMap.put(EcardLib.FIELD_FEETYPE, new IntegerFieldDefine(
                EcardLib.FIELD_FEETYPE, fileId, offset, 1));
        ++offset;
        offset += 1 + 4; // $$

        sCardFieldMap.put(EcardLib.FIELD_EXPIREDATE, new BCDFieldDefine(
                EcardLib.FIELD_EXPIREDATE, fileId, offset, 4));
        offset += 4;
        // //////////////////////////////////////////////////////////////////////
        fileId = 0x16;
        offset = 0;
        sCardFieldMap.put(EcardLib.FIELD_CUSTNAME, new StringFieldDefine(
                EcardLib.FIELD_CUSTNAME, fileId, offset, 32));
        offset += 32;

        sCardFieldMap.put(EcardLib.FIELD_IDNO, new StringFieldDefine(
                EcardLib.FIELD_IDNO, fileId, offset, 32));
        offset += 32;

        sCardFieldMap.put(EcardLib.FIELD_IDTYPE, new IntegerFieldDefine(
                EcardLib.FIELD_IDTYPE, fileId, offset, 1));
        ++offset;

        sCardFieldMap.put(EcardLib.FIELD_SEX, new IntegerFieldDefine(
                EcardLib.FIELD_SEX, fileId, offset, 1));
        ++offset;

        sCardFieldMap.put(EcardLib.FIELD_STUEMPNO, new StringFieldDefine(
                EcardLib.FIELD_STUEMPNO, fileId, offset, 20));
        offset += 20;

        sCardFieldMap.put(EcardLib.FIELD_DEPTCODE, new StringFieldDefine(
                EcardLib.FIELD_DEPTCODE, fileId, offset, 20));
        offset += 20;

        sCardFieldMap.put(EcardLib.FIELD_CUSTTYPE, new IntegerFieldDefine(
                EcardLib.FIELD_CUSTTYPE, fileId, offset, 1));
        ++offset;

        sCardFieldMap.put(EcardLib.FIELD_CUSTID, new IntegerFieldDefine(
                EcardLib.FIELD_CUSTID, fileId, offset, 4));

        sCardFileMap.put(fileId, new CardFileDefine(fileId, 112,
                CardFileDefine.AUTH_MAC_WRITE));
        // //////////////////////////////////////////////////////////////////////
        fileId = 0x12;
        offset = 0;
        sCardFieldMap.put(EcardLib.FIELD_LIMITPERONCE, new IntegerFieldDefine(
                EcardLib.FIELD_LIMITPERONCE, fileId, offset, 3));
        offset += 3;

        sCardFieldMap.put(EcardLib.FIELD_LIMITPERDAY, new IntegerFieldDefine(
                EcardLib.FIELD_LIMITPERDAY, fileId, offset, 3));
        offset += 3;

        sCardFileMap.put(fileId, new CardFileDefine(fileId, 16,
                CardFileDefine.AUTH_MAC_WRITE));
        // //////////////////////////////////////////////////////////////////////

        fileId = 0x19;
        offset = 0;
        CardFieldDefine field = new StringFieldDefine(EcardLib.FIELD_PASSWORD,
                fileId, offset, 6);
        field.setReadable(false);
        sCardFieldMap.put(EcardLib.FIELD_PASSWORD, field);
        offset += 6;

        sCardFileMap.put(fileId, new CardFileDefine(fileId, 16,
                CardFileDefine.AUTH_MAC_WRITE
                        | CardFileDefine.AUTH_EXTAUTH_READ));

        /////////////////////////////////////////////////////////////////////////
        // record define
        fileId = 0x18;
        offset = 0;
        sCardRecordFields.add(new IntegerFieldDefine(RECORD_CARDCNT, fileId, offset, 2));
        offset += 2;
        offset += 3;
        sCardRecordFields.add(new IntegerFieldDefine(RECORD_AMOUNT, fileId, offset, 4));
        offset += 4;
        sCardRecordFields.add(new IntegerFieldDefine(RECORD_TRANSTYPE, fileId, offset, 1));
        ++offset;
        sCardRecordFields.add(new BCDFieldDefine(RECORD_SAMNO, fileId, offset, 6));
        offset += 6;
        sCardRecordFields.add(new BCDFieldDefine(RECORD_TRANSDATE, fileId, offset, 4));
        offset += 4;
        sCardRecordFields.add(new BCDFieldDefine(RECORD_TRANSTIME, fileId, offset, 3));

        sInitialized = true;
        return true;
    }

    /**
     * NFC IsoDep class
     *
     * @see android.nfc.tech.IsoDep
     */
    private IsoDep nfcTech;

    /**
     * @param tag - @see android.nfc.Tag
     */
    public CpuCardLib(Tag tag) {
        super(tag);
        initialize();
        nfcTech = IsoDep.get(tag);
    }

    /**
     * @throws java.io.IOException - @see {@link android.nfc.tech.IsoDep#connect()}
     * @description: Check if card is connected,
     * @author Tang Cheng
     * @create 2013-4-17
     */
    private void checkConnection() throws IOException {
        if (nfcTech.isConnected())
            return;
        nfcTech.connect();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.supwisdom.cardlib.EcardLib#getReadFileList()
     */
    @Override
    protected Integer[] getReadFileList() {
        HashSet<Integer> fileset = new HashSet<Integer>();
        for (String key : mReadFieldMap.keySet()) {
            CardFieldDefine field = sCardFieldMap.get(key);
            if (null == field) {
                continue;
            }
            if (!field.isReadable()) {
                continue;
            }
            if (!fileset.contains(field.getFileId())) {
                fileset.add(Integer.valueOf(field.getFileId()));
            }
        }
        Integer[] res = new Integer[fileset.size()];
        fileset.toArray(res);
        return res;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.supwisdom.cardlib.EcardLib#getFileDefine(int)
     */
    @Override
    public CardFileDefine getFileDefine(int fileId) {
        return sCardFileMap.get(fileId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.supwisdom.cardlib.EcardLib#cardCommandHex(java.lang.String)
     */
    @Override
    public String cardCommandHex(String cmd) throws IOException {
        byte[] req;
        try {
            req = EcardUtils.decodeHex(cmd);
        } catch (CardValueException e) {
            Log.e(tag, "Apdu command is not HEX format");
            throw new RuntimeException("Apdu command is not HEX format");
        }
        byte[] resp = cardCommand(req);
        if (resp != null) {
            return EcardUtils.encodeHex(resp);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.supwisdom.cardlib.EcardLib#cardCommand(byte[])
     */
    @Override
    public byte[] cardCommand(byte[] cmd) throws IOException {
        checkConnection();
        byte[] resp = nfcTech.transceive(cmd);
        byte sw1 = resp[resp.length - 2];
        byte sw2 = resp[resp.length - 1];
        if (sw1 != SW_SUCCESS[0] && sw2 != SW_SUCCESS[1]) {
            if (sw1 == 0x61 && sw2 > 0) {
                String inner_cmd = String.format("00C00000%02X",
                        (int) sw2 & 0xFF);
                try {
                    return cardCommand(EcardUtils.decodeHex(inner_cmd));
                } catch (CardValueException e) {
                    Log.e(tag, "Get Response Data Error");
                    throw new RuntimeException("Apdu command error");
                }
            }
            Log.e(tag, String.format("Apdu command error, sw=%02X%02X",
                    (int) sw1 & 0xFF, (int) sw2 & 0xFF));
            return null;
        }
        return resp;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.supwisdom.cardlib.EcardLib#close()
     */
    @Override
    public void close() {
        super.close();
        try {
            this.nfcTech.close();
        } catch (IOException e) {
            Log.e(tag, "关闭卡连接失败");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.supwisdom.cardlib.EcardLib#getFieldDefinesByFile(int)
     */
    @Override
    public CardFieldDefine[] getFieldDefinesByFile(int fileId) {
        ArrayList<CardFieldDefine> res = new ArrayList<CardFieldDefine>();
        for (CardFieldDefine field : sCardFieldMap.values()) {
            if (field.getFileId() == fileId) {
                res.add(field);
            }
        }
        CardFieldDefine[] sortres = new CardFieldDefine[res.size()];
        res.toArray(sortres);
        Arrays.sort(sortres);
        return sortres;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.supwisdom.cardlib.EcardLib#executeCardCommand(java.lang.String,
     * com.supwisdom.cardlib.CardCommand, com.supwisdom.cardlib.CardCommand)
     */
    @Override
    public boolean executeCardCommand(String cmd, CardCommand request,
                                      CardCommand response) throws IOException {
        if (CardCommand.CMD_SELECT_EP_AID.equals(cmd)) {
            return selectAID(response);
        } else if (CardCommand.CMD_GET_EP_BALANCE.equals(cmd)) {
            return getBalance(response);
        } else if (CardCommand.CMD_GET_EP_DPSCNT.equals(cmd)) {
            return getDpscnt(response);
        } else if (CardCommand.CMD_GET_EP_PAYCNT.equals(cmd)) {
            return getPaycnt(response);
        } else if (CardCommand.CMD_GET_EP_RECORD.equals(cmd)) {
            return getEPRecord(request, response);
        } else if (CardCommand.CMD_INIT_FOR_LOAD.equals(cmd)) {
            return init4Load(request, response);
        } else if (CardCommand.CMD_CREDIT_FOR_LOAD.equals(cmd)) {
            return credit4Load(request, response);
        } else if (CardCommand.CMD_INIT_FOR_PURCHASE.equals(cmd)) {
            return init4Purchase(request, response);
        } else if (CardCommand.CMD_DEBIT_FOR_PURCHASE.equals(cmd)) {
            return debit4Purchase(request, response);
        }
        return false;
    }

    private boolean debit4Purchase(CardCommand request, CardCommand response) throws IOException {
        String mac1 = request.getParameterString("mac1");
        String termDate = request.getParameterString("termdate");
        String termTime = request.getParameterString("termtime");
        int seqNo = request.getParameterInt("samseqno");
        String apduCmd = String.format("805401000F%08X%s%s%s08", seqNo, termDate, termTime, mac1);
        try {
            byte [] result = cardCommand(EcardUtils.decodeHex(apduCmd));
            if(null == result){
                return false;
            }
            response.setParameter("tac", Arrays.copyOfRange(result, 0, 4));
            response.setParameter("mac2", Arrays.copyOfRange(result, 4, 8));
            return true;
        } catch (CardValueException e) {
            Log.e(tag, "init4load command error");
            return false;
        }
    }

    private boolean init4Purchase(CardCommand request, CardCommand response) throws IOException {
        int amount = request.getParameterInt("amount");
        String samno = request.getParameterString("samno");
        String apduCmd = String.format("805001020B01%08X%s", amount, samno);
        try {
            byte [] result = cardCommand(EcardUtils.decodeHex(apduCmd));
            if(null == result){
                return false;
            }
            int offset = 0;
            int balance = EcardUtils.byte2int(Arrays.copyOfRange(result, offset, offset + 4),
                    EcardUtils.BIT_BIG_ENDDIN);
            offset += 4;
            int paycnt = EcardUtils.byte2int(Arrays.copyOfRange(result, offset, offset + 2),
                    EcardUtils.BIT_BIG_ENDDIN);
            offset += 2;
            offset += 5;
            response.setParameter("cardbefbal", balance);
            response.setParameter("paycnt", paycnt);
            response.setParameter("random_num", EcardUtils.encodeHex(Arrays.copyOfRange(result,
                    offset, offset + 4)));
            return true;
        } catch (CardValueException e) {
            Log.e(tag, "init4purchase command error");
            return false;
        }
    }

    private boolean credit4Load(CardCommand request, CardCommand response) throws IOException {
        String mac2 = request.getParameterString("mac2");
        String hostDate = request.getParameterString("hostdate");
        String hostTime = request.getParameterString("hosttime");
        String apduCmd = String.format("805200000B%s%s%s", hostDate, hostTime, mac2);
        try {
            byte [] result = cardCommand(EcardUtils.decodeHex(apduCmd));
            if(null == result){
                return false;
            }
            response.setParameter("tac", Arrays.copyOfRange(result, 0, 4));
            return true;
        } catch (CardValueException e) {
            Log.e(tag, "init4load command error");
            return false;
        }
    }

    private boolean init4Load(CardCommand request, CardCommand response) throws IOException {
        String verifyPinCmd = "0020000003000000";
        String verifyResp = cardCommandHex(verifyPinCmd);
        if (null == verifyResp) {
            return false;
        }
        int amount = request.getParameterInt("amount");
        String samno = request.getParameterString("samno");
        String apduCmd = String.format("805000020B01%08X%s", amount, samno);
        try {
            byte [] result = cardCommand(EcardUtils.decodeHex(apduCmd));
            if(null == result){
                return false;
            }
            int offset = 0;
            int balance = EcardUtils.byte2int(Arrays.copyOfRange(result, offset, offset + 4),
                    EcardUtils.BIT_BIG_ENDDIN);
            offset += 4;
            int dpscnt = EcardUtils.byte2int(Arrays.copyOfRange(result, offset, offset + 2),
                    EcardUtils.BIT_BIG_ENDDIN);
            offset += 2;
            offset += 2;
            response.setParameter("cardbefbal", balance);
            response.setParameter("dpscnt", dpscnt);
            response.setParameter("random_num", EcardUtils.encodeHex(Arrays.copyOfRange(result,
                    offset, offset + 4)));
            offset += 4;
            response.setParameter("mac1", EcardUtils.encodeHex(Arrays.copyOfRange(result,
                    offset, offset + 4)));
            return true;
        } catch (CardValueException e) {
            Log.e(tag, "init4load command error");
            return false;
        }
    }

    private boolean getEPRecord(CardCommand request, CardCommand response) throws IOException {
        int maxCount = 10;
        if (request.hasParameter("max_count")) {
            maxCount = request.getParameterInt("max_count");
        }
        ArrayList<CardRecord> recordArray = new ArrayList<CardRecord>();
        for (int recIndex = 0; recIndex < maxCount; ++recIndex) {
            String cmd = String.format("00B2%02XC400", (recIndex + 1) & 0xFF);
            try {
                byte[] recordContent = cardCommand(EcardUtils.decodeHex(cmd));
                if(null == recordContent){
                    break;
                }
                CardRecord record = new CardRecord();
                for(int i = 0;i < sCardRecordFields.size(); ++i){
                    CardFieldDefine define = sCardRecordFields.get(i);
                    int endPos = define.getOffset() + define.getRawLength();
                    String value = define.getValue(Arrays.copyOfRange(recordContent, define.getOffset(), endPos));
                    if(RECORD_SAMNO.equals(define.getName())){
                        record.setSamNo(value);
                    } else if(RECORD_AMOUNT.equals(define.getName())){
                        record.setAmount(Integer.parseInt(value));
                    } else if(RECORD_CARDCNT.equals(define.getName())){
                        record.setCardCnt(Integer.parseInt(value));
                    } else if(RECORD_TRANSTYPE.equals(define.getName())){
                        record.setTransType(Integer.parseInt(value));
                    } else if(RECORD_TRANSDATE.equals(define.getName())) {
                        record.setTransDate(value);
                    } else if(RECORD_TRANSTIME.equals(define.getName())) {
                        record.setTransTime(value);
                    }
                }
                recordArray.add(record);
            } catch (CardValueException e) {
                return false;
            }
        }
        response.setParameter("records", recordArray);
        return true;
    }

    /**
     * @param response
     * @return
     * @throws java.io.IOException boolean
     * @description: 选择消费应用
     * @author Tang Cheng
     * @create 2013-4-17
     */
    private boolean selectAID(CardCommand response) throws IOException {
        String apduCmd = "00A4000002DF03";
        String resp = cardCommandHex(apduCmd);
        if (null == resp)
            return false;
        try {
            response.setParameter(CardCommand.APDU_RESPONSE,
                    EcardUtils.decodeHex(resp));
        } catch (CardValueException e) {
            return false;
        }
        return true;
    }

    /**
     * @param response - CARD_BALANCE 保持卡余额
     * @return boolean - true 成功， false 失败
     * @throws java.io.IOException
     * @description: 读取卡余额
     * @author Tang Cheng
     * @create 2013-4-17
     */
    private boolean getBalance(CardCommand response) throws IOException {
        String apduCmd = "805C000204";
        byte[] req = null;
        try {
            req = EcardUtils.decodeHex(apduCmd);
        } catch (CardValueException e) {
            Log.e(tag, "Convert error");
            return false;
        }
        byte[] resp = cardCommand(req);
        if (null == resp)
            return false;
        int balance = EcardUtils.byte2int(Arrays.copyOfRange(resp, 0, 4),
                EcardUtils.BIT_BIG_ENDDIN);
        response.setParameter(CardCommand.CARD_BALANCE,
                Integer.valueOf(balance));
        return true;
    }

    /**
     * @param response
     * @return
     * @throws java.io.IOException
     * @description: 获取卡充值次数与余额
     * @author Tang Cheng
     * @create 2013-4-17
     */
    private boolean getDpscnt(CardCommand response) throws IOException {
        String verifyPinCmd = "0020000003000000";
        String verifyResp = cardCommandHex(verifyPinCmd);
        if (null == verifyResp) {
            return false;
        }
        String apduCmd = "805000020B0100000000000000000000";
        byte[] req = null;
        try {
            req = EcardUtils.decodeHex(apduCmd);
        } catch (CardValueException e) {
            Log.e(tag, "Convert error");
            return false;
        }
        byte[] resp = cardCommand(req);
        if (null == resp)
            return false;
        int balance = EcardUtils.byte2int(Arrays.copyOfRange(resp, 0, 4),
                EcardUtils.BIT_BIG_ENDDIN);
        int dpscnt = EcardUtils.byte2int(Arrays.copyOfRange(resp, 4, 6),
                EcardUtils.BIT_BIG_ENDDIN);
        response.setParameter(CardCommand.CARD_BALANCE,
                Integer.valueOf(balance));
        response.setParameter(CardCommand.CARD_DPSCNT, Integer.valueOf(dpscnt));
        return true;
    }

    /**
     * @param response
     * @return
     * @throws java.io.IOException
     * @description: 获取卡消费次数与余额
     * @author Tang Cheng
     * @create 2013-4-17
     */
    private boolean getPaycnt(CardCommand response) throws IOException {
        String apduCmd = "805001020B0100000000000000000000";
        byte[] req = null;
        try {
            req = EcardUtils.decodeHex(apduCmd);
        } catch (CardValueException e) {
            Log.e(tag, "Convert error");
            return false;
        }
        byte[] resp = cardCommand(req);
        if (null == resp)
            return false;
        int balance = EcardUtils.byte2int(Arrays.copyOfRange(resp, 0, 4),
                EcardUtils.BIT_BIG_ENDDIN);
        int paycnt = EcardUtils.byte2int(Arrays.copyOfRange(resp, 4, 6),
                EcardUtils.BIT_BIG_ENDDIN);
        response.setParameter(CardCommand.CARD_BALANCE,
                Integer.valueOf(balance));
        response.setParameter(CardCommand.CARD_PAYCNT, Integer.valueOf(paycnt));
        return true;
    }

    @Override
    public boolean testConnectionOk() {
        String cmd = "0084000004";
        String resp = null;
        try {
            resp = cardCommandHex(cmd);
            if(null == resp){
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    @Override
    public ErrorInfo purchase(int amount){
        CardCommand request = new CardCommand();
        CardCommand response = new CardCommand();
        PsamCard.initCard(1);
        request.setParameter("amount", amount);
        request.setParameter("samno", PsamCard.getSamno());
        boolean cmdResult = true;
        //step1：消费初始化
        try {
            cmdResult = executeCardCommand(CardCommand.CMD_INIT_FOR_PURCHASE, request, response);
            if(false == cmdResult){
                return ErrorDef.SP_INIT_FOR_PURCHASE_FAIL;
            }
        }
        catch(IOException e){
            Log.e(tag, "消费初始化失败"+e.getMessage());
            return ErrorDef.SP_INIT_FOR_PURCHASE_FAIL;
        }
        int paycnt = response.getParameterInt("paycnt");
        String random_num = response.getParameterString("random_num");
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String devtime = dateFormat.format( now );
        //step2：获取mac1
        try {
            request.setParameter("cardphyid", this.getCardUidHex());
            request.setParameter("paycnt", paycnt);
            request.setParameter("pay_random", random_num);
            request.setParameter("devtime", devtime);
            request.setParameter("key_index", 6);
            cmdResult = PsamCard.executePsamCommand(CardCommand.CMD_GET_MAC1, request, response);
            if(false == cmdResult){
                Log.e(tag, "获取mac1失败");
                return ErrorDef.SP_GET_MAC1_FAIL;
            }
        }
        catch(IOException e){
            Log.e(tag, "获取mac1失败"+e.getMessage());
            return ErrorDef.SP_GET_MAC1_FAIL;
        }
        int samSeqno = response.getParameterInt("samSeqno");
        String mac1 = response.getParameterString("mac1");
        //step3：消费确认
        request.setParameter("samseqno", samSeqno);
        request.setParameter("mac1", mac1);
        request.setParameter("termdate", devtime.substring(0, 8));
        request.setParameter("termtime", devtime.substring(8, 14));
        try {
            cmdResult = executeCardCommand(CardCommand.CMD_DEBIT_FOR_PURCHASE, request, response);
            if(false == cmdResult){
                Log.e(tag, "消费确认失败");
                return ErrorDef.SP_DEBI_FOR_PURCHASE_FAIL;
            }
        }
        catch(IOException e){
            Log.i(tag, "消费确认失败"+e.getMessage());
            return ErrorDef.SP_DEBI_FOR_PURCHASE_FAIL;
        }
        return ErrorDef.SP_SUCCESS;
    }
}
