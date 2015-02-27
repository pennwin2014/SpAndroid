package com.supwisdom.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd.
 * File name: com.supwisdom.protocol
 * Description: TODO Modify History（或Change Log）:
 * 操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 * 创建 13-5-2 tangcheng
 * <p/>
 *
 * @author tangcheng
 * @version 1.0
 * @since 1.0
 */
public class EpaySession {
    private String appId;
    private String appSecret;
    private boolean isAuth;
    private static final SimpleDateFormat sTimeFormat =
            new SimpleDateFormat("yyyyMMddHHmmss");

    public EpaySession(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.isAuth = false;
    }

    public EpaySession() {
        this.appId = null;
        this.appSecret = null;
        this.isAuth = false;
    }

    public void auth(String baseUrl, String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.isAuth = true;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public boolean sendRequest(String url, WebParams params, StringBuffer response,
                               int timeout) {
        try {
            boolean find = false;
            for (int i = 0; i < url.length(); ++i) {
                if (url.charAt(i) == '?') {
                    find = true;
                    break;
                }
            }
            doCalcSign(params);
            if (find) {
                url = url + "&" + params.encodeURL();
            } else {
                url = url + "?" + params.encodeURL();
            }
            HttpURLConnection connection = (HttpURLConnection) (
                    (new URL(url)).openConnection());
            timeout = timeout * 1000;
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("GET");
            return doRequest(connection, response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void doCalcSign(WebParams params) {
        String timestamp = sTimeFormat.format(new Date());
        params.setParameter("timestamp", timestamp);
        params.setParameter("partner_id", this.appId);
        params.setParameter("sign_method", "HMAC");
        Set<String> paramNames = params.allParameterNames();
        String[] namesArray = new String[paramNames.size()];
        paramNames.toArray(namesArray);
        Arrays.sort(namesArray);
        StringBuffer signData = new StringBuffer();
        String empty = "";
        for (int i = 0; i < namesArray.length; ++i) {
            if ("sign".equals(namesArray[i])) {
                continue;
            }
            String v = params.getParameterString(namesArray[i]);
            if (v == null) {
                v = empty;
            }
            signData.append(namesArray[i])
                    .append("=").append(v);
            if (i < namesArray.length - 1) {
                signData.append("&");
            }
        }
        String mac = calcHMAC(signData.toString());
        params.setParameter("sign", mac);

    }

    private String calcHMAC(String data) {

        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec = new SecretKeySpec(appSecret.getBytes(), "HmacSHA1");
            mac.init(spec);
            byteHMAC = mac.doFinal(data.getBytes());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Calc MAC key error");
        } catch (NoSuchAlgorithmException ignore) {
            throw new RuntimeException("HMAC Algorithm");
        }
        StringBuffer mac = new StringBuffer();
        for (int i = 0; i < byteHMAC.length; ++i) {
            mac.append(String.format("%02X", (int) byteHMAC[i] & 0xFF));
        }
        return mac.toString();
    }

    private boolean doRequest(HttpURLConnection connection, StringBuffer response)
            throws IOException {
        int code = connection.getResponseCode();
        String line;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        return ((code / 100) == 2);
    }
}
