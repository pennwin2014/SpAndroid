package com.supwisdom.protocol;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Copyright (C), 2000-2013, Supwisdom Co., Ltd.
 * File name: com.supwisdom.protocol
 * Description: TODO Modify History（或Change Log）:
 * 操作类型（创建、修改等） 操作日期 操作者 操作内容简述
 * 创建 13-4-19 tangcheng
 * <p/>
 *
 * @author tangcheng
 * @version 1.0
 * @since 1.0
 */
public class WebAPISession implements Cloneable {
    private static final String tag = "supwisdom.webapisession";
    private String appId;
    private String appSecret;
    private String termId;
    private String sessionKey = null;
    private boolean isAuthorized;

    @Override
    public Object clone() throws CloneNotSupportedException {
        WebAPISession another = new WebAPISession();
        another.appId = this.appId;
        another.appSecret = this.appSecret;
        another.termId = this.termId;
        another.sessionKey = this.sessionKey;
        another.isAuthorized = isAuthorized;
        return another;
    }

    public WebAPISession() {
        isAuthorized = false;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public boolean auth(String url, String appId, String appSecret, String termId) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.termId = termId;
        isAuthorized = false;
        try {
            String token = getAccessToken(url, 5000);
            if (null == token)
                return false;
            if (getSessionKey(url, token, 5000)) {
                isAuthorized = true;
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean getSessionKey(String url, String token, int timeout)
            throws IOException {
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append(url).append("/authservice/getauth/");
        urlBuffer.append(this.appId).append("?");
        WebParams params = new WebParams();
        params.setParameter("access_token", token);
        params.setParameter("v", "1");
        calcAuthMac(params, token);
        urlBuffer.append(params.encodeURL());
        HttpURLConnection connection = (HttpURLConnection) (
                new URL(urlBuffer.toString())).openConnection();
        try {

            connection.setRequestMethod("GET");
            connection.setReadTimeout(timeout);
            connection.setConnectTimeout(timeout);
            connection.connect();
            int code = connection.getResponseCode();
            if (code != 200) {
                return false;
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            parseSessionKey(response);
            if (null == sessionKey)
                return false;
            return true;
        } finally {
            connection.disconnect();
        }
    }

    private void parseSessionKey(StringBuffer response) {
        JsonReader reader = new JsonReader(new StringReader(response.toString()));

        try {
            this.sessionKey = null;
            reader.beginObject();
            while (reader.hasNext()) {
                if ("session_key".equals(reader.nextName())) {
                    sessionKey = reader.nextString();
                    break;
                }
                reader.skipValue();
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAccessToken(String url, int timeout) throws IOException {
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append(url).append("/authservice/getauth/").append(this.appId)
                .append("/getaccesstoken?term_id=").append(this.termId);
        HttpURLConnection connection = (HttpURLConnection)
                (new URL(urlBuffer.toString())).openConnection();
        try {

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.connect();
            int code = connection.getResponseCode();
            if (200 == code) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                return parseToken(response);
            }
            return null;
        } catch (IOException e) {
            Log.e(tag, "Error : " + e);
            throw e;
        } catch (Exception e) {
            Log.e(tag, "Error : " + e);
        } finally {
            connection.disconnect();
        }
        return null;
    }

    private String parseToken(StringBuffer response) {
        JsonReader reader = new JsonReader(new StringReader(response.toString()));

        try {
            String token = null;
            reader.beginObject();
            while (reader.hasNext()) {
                if ("access_token".equals(reader.nextName())) {
                    token = reader.nextString();
                    break;
                }
                reader.skipValue();
            }
            reader.endObject();
            return token;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean sendRequest(String url, WebParams params, StringBuffer response,
                               int timeout) throws IOException {
        return sendRequest("POST", url, params, response, timeout, null);
    }

    public boolean sendRequest(String url, StringBuffer response, int timeout)
            throws IOException {
        WebParams params = new WebParams();
        return sendRequest("GET", url, params, response, timeout, null);
    }

    public boolean sendRequestExtra(String url, StringBuffer response, String extra,
                                    int timeout) throws IOException {
        WebParams params = new WebParams();
        return sendRequest("GET", url, params, response, timeout, extra);
    }

    public boolean callYKTApi(String url, String funcData, StringBuffer response, int timeout) {
        WebParams param = new WebParams();
        StringBuffer requestData = new StringBuffer();
        prepareAuthParameter(param, null);
        URL urlObject = null;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        if (!prepareAPIBuffer(requestData, funcData, param)) {
            return false;
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json");
            byte [] data = requestData.toString().getBytes();
            int contentLength = data.length;
//            if (contentLength <= 512) {
                connection.setRequestProperty("Content-length",
                        String.valueOf(contentLength));
//            } else {
//                connection.setChunkedStreamingMode(512);
//            }
            OutputStream stream = new DataOutputStream(connection.getOutputStream());
            stream.write(data);
            stream.flush();
            boolean exeOk = false;
            int code = connection.getResponseCode();
            if (code / 100 == 2) {
                exeOk = true;
            }
            String line;
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return exeOk;
        } catch (IOException e) {
            Log.e(tag, "Call YKTAPI: " + e);
            return false;
        }
    }

    private boolean prepareAPIBuffer(StringBuffer requestData, String funcData, WebParams param) {
        StringWriter strWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(strWriter);
        try {
            writer.beginObject();
            writer.name("app_id").value(this.appId);
            writer.name("term_id").value(this.termId);
            writer.name("timestamp").value(param.getParameterString("timestamp"));
            writer.name("sign_method").value(param.getParameterString("sign_method"));
            writer.name("sign").value(param.getParameterString("sign"));
            writer.name("funcdata").value(funcData);
            writer.endObject();
            writer.setIndent("");
            requestData.append(strWriter.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean sendRequest(String method, String url, WebParams params,
                               StringBuffer response, int timeout, String extra)
            throws IOException {
        if (!isAuthorized) {
            return false;
        }
        try {
            prepareAuthParameter(params, extra);
            URL urlObject = null;
            if ("GET".equals(method) || "DELETE".equals(method) ||
                    "PUT".equals(method) || "OPTIONS".equals(method) ||
                    "TRACE".equals(method)) {
                char paramConnect = '?';
                for (int i = 0; i < url.length(); ++i) {
                    if (url.charAt(i) == '?') {
                        paramConnect = '&';
                    }
                }
                String extraParams = params.encodeURL();
                urlObject = new URL(url + paramConnect + extraParams);
            } else if ("POST".equals(method)) {
                urlObject = new URL(url);
            } else {
                return false;
            }
            HttpURLConnection connection = (HttpURLConnection) urlObject
                    .openConnection();
            if ("POST".equals(method)) {
                connection.setDoOutput(true);
            }
            connection.setRequestMethod(method);
            return processRequest(connection, params, timeout, response);
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private boolean processRequest(HttpURLConnection connection, WebParams params,
                                   int timeout, StringBuffer responseContent)
            throws IOException {
        boolean execOk = false;
        try {
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            if (connection.getRequestMethod().equals("POST")) {
                String postData = params.encodeURL();
                int contentLength = postData.length();
                connection.setRequestProperty("Content-length",
                        Integer.toString(contentLength));
                OutputStream out = new BufferedOutputStream(
                        connection.getOutputStream());
                out.write(postData.getBytes());
            } else {
                connection.setRequestProperty("Content-length", "0");
                connection.connect();
            }

            int code = connection.getResponseCode();
            if (code / 100 == 2) {
                execOk = true;
            }
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            Log.e("webservice", "Error : " + e);
        } finally {
            connection.disconnect();
            return execOk;
        }

    }

    private void prepareAuthParameter(WebParams params, String extra) {
        params.setParameter("app_id", this.appId);
        params.setParameter("term_id", this.termId);
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = format.format(now);
        params.setParameter("timestamp", timestamp);
        String mac = calcHMAC(params, timestamp, extra);
        params.setParameter("sign_method", "HMAC");
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

    private String calcHMAC(WebParams params, String nonce, String extra) {
        StringBuffer data = new StringBuffer();
        data.append(this.appId).append(termId).append(sessionKey).append(nonce);
        if (extra != null) {
            data.append(extra);
        }
        return calcHMAC(data.toString());
    }

    private void calcAuthMac(WebParams params, String token) {
        params.setParameter("appid", this.appId);
        params.setParameter("access_token", token);
        params.setParameter("term_id", this.termId);
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = format.format(now);
        params.setParameter("timestamp", timestamp);
        StringBuffer data = new StringBuffer();
        data.append(token).append(timestamp);
        String mac = calcHMAC(data.toString());
        params.setParameter("sign_method", "HMAC");
        params.setParameter("sign", mac);

    }
}
