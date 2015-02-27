package com.supwisdom.protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

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
public class WebParams {
	private HashMap<String, Object> mParameters = new HashMap<String, Object>();

	public WebParams() {
	}

	public void setParameter(String name, String value) {
		mParameters.put(name, value);
	}

	public void setParameter(String name, int value) {
		mParameters.put(name, Integer.valueOf(value));
	}

	public void setParameter(String name, float value) {
		mParameters.put(name, Float.valueOf(value));
	}

	public String getParameterString(String name) {
		Object ret = mParameters.get(name);
		if (String.class.isInstance(ret)) {
			return (String) ret;
		} else if(Integer.class.isInstance(ret)){
            return String.valueOf(ret);
        } else if(Double.class.isInstance(ret)) {
            return String.valueOf(ret);
        }
		return "";
	}

	public int getParameterInt(String name) {
		Object ret = mParameters.get(name);
		if (Integer.class.isInstance(ret)) {
			return (Integer) ret;
		}
		return 0;
	}

	public float getParameterFloat(String name) {
		Object ret = mParameters.get(name);
		if (Float.class.isInstance(ret)) {
			return (Float) ret;
		}
		return 0;
	}

	public void removeParameter(String name){
		if(mParameters.containsKey(name)){
			mParameters.remove(name);
		}
	}

	public String encodeURL() throws UnsupportedEncodingException {
		StringBuffer result = new StringBuffer();
		for (String name : mParameters.keySet()) {
			Object value = mParameters.get(name);
			result.append(URLEncoder.encode(name, "utf-8"))
					.append("=")
					.append(URLEncoder.encode(value.toString(), "utf-8"))
					.append("&");
		}
		return result.toString();
	}

	public int encodeURL(OutputStream output) throws IOException {
		DataOutputStream byteStream = new DataOutputStream(output);
		Writer writer = new OutputStreamWriter(byteStream, "utf-8");
		for (String name : mParameters.keySet()) {
			Object value = mParameters.get(name);
			writer.write(URLEncoder.encode(name, "utf-8"));
			writer.write("=");
			writer.write(URLEncoder.encode(value.toString(), "utf-8"));
			writer.write("&");
		}
		return byteStream.size();
	}

    public Set<String> allParameterNames(){
        return mParameters.keySet();
    }

}
