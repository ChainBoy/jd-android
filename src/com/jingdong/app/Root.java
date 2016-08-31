package com.jingdong.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Root {
	public static void main(String args[]) {
		System.load("/data/local/tmp/libjdmobilesecurity.so");
//		System.out.println("Hello World!");
		String taskString = args[0];
		String taskArgs[] = taskString.split(",");
		String paramsString = "";
		String uuid = taskArgs[0];
		String sku = taskArgs[1];
		String page = taskArgs[2];
		String size = taskArgs[3];

		paramsString = buildBody(sku, page, size);
		String signString = sign("getMobileCommentList", paramsString, uuid);
//		Log.d("sign" , taskString + ":" + signString);
		System.out.println(taskString + " params:"+paramsString);
		System.out.println(taskString + " sign:"+signString);
	}

	private static String sign(String funcioinId, String paramsString, String deviceUUID) {
		// System.out.println(System.getProperty("java.library.path"));
		// System.out.println("================");
		Map pMap = new HashMap();
		pMap.put("functionId", funcioinId);
		pMap.put("body", paramsString);
		pMap.put("uuid", deviceUUID);
		ArrayList pList = new ArrayList(5);
		pList.add("functionId");
		pList.add("body");
		pList.add("uuid");
		Map sign_map = Sign.getSignMap(pMap, pList);
//		Log.w("xxx", "[Text 1]: " + sign_map.toString());
		// Log.d("xxx", "[Text 2]: " + sign_map.toString().replaceAll(", ",
		// "&"));
		String sign_key = sign_map.toString().replaceAll(", ", "&");
		sign_key = sign_key.substring(0, sign_key.length() - 1);
		sign_key = "&" + sign_key.substring(1, sign_key.length());
		// Log.d("xxx", "[Text 3]: " + sign_key);

		String base_url = "http://ngw.m.jd.com/client.action?functionId=" + funcioinId + "&uuid=" + deviceUUID
				+ "&clientVersion=3.9.9&build=13317&client=android&d_brand=LGE&d_model=Nexus5&osVersion=6.0.1&screen=1776*1080&partner=tencent&area=1_0_0_0&networkType=wifi";
		String result_url = base_url + sign_key;
		return sign_key;
	}

	private static String buildBody(String sku, String offset, String num) {
		try {
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("offset", offset);
			jsonParams.put("num", num);

			jsonParams.put("sku", sku);
			jsonParams.put("type", "0");
			String paramsString = "";

			return jsonParams.toString();
			// paramsString = "{'sku':'" + sku + "','type':'0','offset':'" +
			// offset + "','num':'" + num + "'}&";
			// return paramsString.replaceAll("", "");
		} catch (JSONException e) {
			return "";
		}
	}

}
