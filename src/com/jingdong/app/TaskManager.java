package com.jingdong.app;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//import org.ow2.util.base64.*;
import it.sauronsoftware.base64.Base64;

public class TaskManager {
	// private final static Base64.Decoder decoder = Base64.getDecoder();
	// private final static Base64.Encoder encoder = Base64.getEncoder();

	public static String base64_encode(String text) {

		String encodedText = "";
		// byte[] textByte = text.getBytes("UTF8");
		// Base64.encode(text.getBytes("UTF8")).toString();
		encodedText = Base64.encode(text, "UTF-8");
		// encodedText = encoder.encodeToString(textByte);
		return encodedText;
	}

	public static String base64_decode(String encodedText) {
		String result = "";
		// result = new String(decoder.decode(encodedText), "UTF-8");
		result = Base64.decode(encodedText, "UTF-8");
		return result;
	}

	public static ArrayList<Map> getTaskMapList(String serverURL) {
		// System.out.println("get task url: " + serverURL);
		String content = Download.get(serverURL);
		// System.out.println("get task html: \n----\n" + content + "\n====");
		return extractTaskMapList(content);
	}

	private static ArrayList<Map> extractTaskMapList(String content) {
		ArrayList<Map> taskMapList = new ArrayList<Map>();
		if (content == "") {
			return taskMapList;
		}
		String[] taskStringList = content.split("\n");

		for (String line : taskStringList) {
			// 358239051596619-020000000000,2782638,2782638,1,10
			// phone_id,child_id,product_id,page_index,page_size
			Map taskMap = new HashMap();
			ArrayList arrayList = new ArrayList(4);
			taskMap.put("task", line);
			try {
				String[] resultList = line.split(",");
				taskMap.put("uuid", resultList[0]);
				taskMap.put("sku", resultList[1]);
				// taskMap.put("sku", resultList[2]);
				taskMap.put("page", resultList[3]);
				taskMap.put("size", resultList[4]);
			} catch (Exception e) {
				Logger.getLogger(Configure.loggerName).info(Configure.logTag + "get error task: " + line);
				System.out.println("get error task: " + line);
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
		httpParams.put("sign_key", task);
		httpParams.put("sign_value", base64_sign);
		httpParams.put("params_value", base64_params);
		Download.get(serverURL, httpParams);
	}

	public static void sendTaskSignList(String serverURL, List<HashMap<String, String>> SignList) {
		String content = "";
		for (HashMap signMap : SignList) {
			String signKey = (String) signMap.get("sign_key");
			String signValue = (String) signMap.get("sign_value");
			String paramsValue = (String) signMap.get("params_value");
			content += signKey + "://:" + signValue + "://:" + paramsValue + ":||:";
		}
		Map httpParams = new HashMap();
		String base64_data = base64_encode(content);

		httpParams.put("data", base64_data);
		Download.get(serverURL, httpParams);
	}

	public static void sendTaskFailed(String serverURL, String task) {

	}

}
