package com.supwisdom.utilities;

import android.util.Log;

import com.supwisdom.cardlib.CardCommand;
import com.supwisdom.cardlib.CardValueException;
import com.supwisdom.cardlib.EcardUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Penn on 2015-02-28.
 */
public class PsamCard {

    private static final String tag = "com.supwisdom.cardlib.psamcard";

    private static String samno = "0000000000";

    //获取psam卡号
    public static String getSamno(){
        return samno;
    }

    //二进制发送指令
    public static byte[] psamCommand(byte[] cmd){
        //
        byte[] resp = cmd;
        return resp;
    }

    private static boolean getMac1(CardCommand request, CardCommand response) {
        int paycnt = request.getParameterInt("paycnt");
        int amount = request.getParameterInt("amount");
        String pay_random = request.getParameterString("pay_random");
        String devtime = request.getParameterString("devtime");
        int key_index = request.getParameterInt("key_index");
        String cardphyid = request.getParameterString("cardphyid");
        String apduCmd = String.format("807000001C%s%04x%08x06%s%02x00%s80000000", pay_random, paycnt, amount, devtime, key_index,
                cardphyid);
        try {
            byte [] result = psamCommand(EcardUtils.decodeHex(apduCmd));
            if(null == result){
                return false;
            }
            int offset = 0;
            int samSeqno = EcardUtils.byte2int(Arrays.copyOfRange(result, offset, offset + 4),
                    EcardUtils.BIT_BIG_ENDDIN);
            offset += 4;
            response.setParameter("samSeqno", samSeqno);
            response.setParameter("mac1", EcardUtils.encodeHex(Arrays.copyOfRange(result,
                    offset, offset + 4)));
            return true;
        } catch (CardValueException e) {
            Log.e(tag, "getmac1 command error");
            return false;
        }
    }

    private static boolean selectAID(CardCommand response) throws IOException {
        String apduCmd = "00A4000002DF03";
        String resp = psamCommandHex(apduCmd);
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

    public static boolean executePsamCommand(String cmd, CardCommand request,
                                      CardCommand response) throws IOException {
        if (CardCommand.CMD_SELECT_EP_AID.equals(cmd)) {
            return selectAID(response);
        } else if (CardCommand.CMD_GET_MAC1.equals(cmd)) {
            return getMac1(request, response);
        }
        return false;
    }


    //字符串发送指令
    public static String psamCommandHex(String cmd) throws IOException {
        byte[] req;
        try {
            req = EcardUtils.decodeHex(cmd);
        } catch (CardValueException e) {
            Log.e(tag, "Apdu command is not HEX format");
            throw new RuntimeException("Apdu command is not HEX format");
        }
        byte[] resp = psamCommand(req);
        if (resp != null) {
            return EcardUtils.encodeHex(resp);
        }
        return null;
    }

    public static void initCard(int slot) {
        //读取相应的卡槽的psam卡号
        samno = "00000000000000";
    }
}
