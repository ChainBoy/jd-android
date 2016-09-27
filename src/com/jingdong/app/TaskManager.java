package com.jingdong.app;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TaskManager {
	private final static Base64.Decoder decoder = Base64.getDecoder();
	private final static Base64.Encoder encoder = Base64.getEncoder();

	public static String base64_encode(String text) {
		String encodedText = "";
		try {
			byte[] textByte = text.getBytes("UTF8");
			encodedText = encoder.encodeToString(textByte);
		} catch (UnsupportedEncodingException e) {
		}
		return encodedText;
	}

	public static String base64_decode(String encodedText) {
		String result = "";
		try {
			result = new String(decoder.decode(encodedText), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<Map> getTaskMapList(String serverURL) {
		String content = Download.get(serverURL);
		return extractTaskMapList(content);
	}

	private static ArrayList<Map> extractTaskMapList(String content) {
		ArrayList<Map> taskMapList = new ArrayList<Map>();
		if (content == "") {
			return taskMapList;
		}
		String[] taskStringList = content.split("\n");

		for (String line : taskStringList) {
			Map taskMap = new HashMap();
			ArrayList arrayList = new ArrayList(4);
			taskMap.put("task", line);
			try {
				String[] resultList = line.split(",");
				taskMap.put("uuid", resultList[0]);
				taskMap.put("sku", resultList[1]);
				taskMap.put("page", resultList[2]);
				taskMap.put("size", resultList[3]);
			} catch (Exception e) {
				Logger.getLogger(Configure.loggerName).info(Configure.logTag + "get error task: " + line);
				continue;
			}
			taskMapList.add(taskMap);
		}
		return taskMapList;
	}

	public static void sendTaskSign(String serverURL, String task, String sign, String params) {
		Map httpParams = new HashMap();
		String base64_sign = base64_encode(sign);
		String base64_params = base64_encode(params);
		httpParams.put("sign", base64_sign);
		httpParams.put("params", base64_params);
		Download.get("http://www.baidu.com", httpParams);
	}

	public static void sendTaskFailed(String serverURL, String task) {

	}

}
