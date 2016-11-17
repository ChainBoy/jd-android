package com.jingdong.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class TaskThread implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;

	public static void test() {
		signTask("358239051596619-020000000000,2782638,2782638,1,10");
	}

	public void run() {
		Logger.getLogger(Configure.loggerName)
				.info(Configure.logTag + "start task thread. name: " + Thread.currentThread().getName());
		try {
			Thread.sleep(Configure.startThreadWaitTime);
			start_thread();
		} catch (Exception e) {
			Logger.getLogger(Configure.loggerName)
					.severe(Configure.logTag + "TaskThread run failed, error:" + e.getMessage());
		}
	}

	private static void start_thread() {
		Map<String, String> resultMap = new HashMap<String, String>();
		while (true) {
			run_task(resultMap);
			System.out.println("==================== sign end, wait next ... ===============");
			if (resultMap.get("result") == "finish") {
				break;
			}
			try {
				Thread.sleep(Configure.nextGetTaskSleepTime);
			} catch (InterruptedException e) {
			}
		}
		if (resultMap.get("result") == "finish")
			Logger.getLogger(Configure.loggerName).info(Configure.logTag + "Finish all. get finish from server.");
		else
			Logger.getLogger(Configure.loggerName)
					.warning(Configure.logTag + "Finish all, not get finish from server!");
	}

	private static void run_task() {
		run_task(new HashMap<String, String>());
	}

	private static void run_task(Map resultMap) {
		ArrayList<HashMap<String, String>> resultSignList= new ArrayList();
		resultMap.put("result", "");
		String taskString = "";
		ArrayList<Map> taskMapList = TaskManager.getTaskMapList(Configure.serverGetTask);
		// System.out.println("get task size: " + taskMapList.size());
		for (Map taskMap : taskMapList) {
			taskString = (String) taskMap.get("task");
			if (taskString == "finish")
				continue;
			resultSignList.add(signTask(taskString));
		}
		if (taskString == "finish")
			resultMap.put("result", "finish");
		if (Configure.SETALL==1){
			TaskManager.sendTaskSignList(Configure.serverDoneFullTask, resultSignList);
		}
	}

	private static HashMap<String, String> signTask(String taskString) {
		String taskArgs[] = taskString.split(",");
		String uuid = taskArgs[0];
		String sku = taskArgs[1];
		String base_sku = taskArgs[2];
		String page = taskArgs[3];
		String size = taskArgs[4];
		String signString = "";
		String paramsString = "";
		HashMap<String, String> resultMap = new HashMap<String, String>();
		String paramsString_0 = buildBody(sku, page, size, "0");
		String signString_0 = sign("getMobileCommentList", paramsString_0, uuid);
		paramsString += "0: " + paramsString_0;
		signString += "0: " + signString_0;
		System.out.println(taskString + "  params: " + paramsString_0 + " type:0 result sign: " + signString_0);

		if (Configure.ALLTYPE == 1) {
			String paramsString_1 = buildBody(sku, page, size, "1");
			String signString_1 = sign("getMobileCommentList", paramsString_1, uuid);
			signString += Configure.splitChar + "1: " + signString_1 + Configure.splitChar;
			paramsString += Configure.splitChar + "1: " + paramsString_1 + Configure.splitChar;

			String paramsString_2 = buildBody(sku, page, size, "2");
			String signString_2 = sign("getMobileCommentList", paramsString_2, uuid);
			signString += "2: " + signString_2 + Configure.splitChar;
			paramsString += "2: " + paramsString_2 + Configure.splitChar;

			String paramsString_3 = buildBody(sku, page, size, "3");
			String signString_3 = sign("getMobileCommentList", paramsString_3, uuid);
			signString += "3: " + signString_3 + Configure.splitChar;
			paramsString += "3: " + paramsString_3 + Configure.splitChar;

			System.out.println(taskString + "  params: " + paramsString_1 + " type:1 result sign: " + signString_1);
			System.out.println(taskString + "  params: " + paramsString_2 + " type:2 result sign: " + signString_2);
			System.out.println(taskString + "  params: " + paramsString_3 + " type:3 result sign: " + signString_3);
		}
		if (Configure.SETALL == 0) {
			TaskManager.sendTaskSign(Configure.serverDoneTask, taskString, signString, paramsString);
		}
		resultMap.put("sign_key", taskString);
		resultMap.put("sign_value", signString);
		resultMap.put("params_value", paramsString);
		return resultMap;
		// TODO: 上传错误
		// TaskManager.sendTaskFailed(serverFailedTask, taskString);
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
		// Log.w("xxx", "[Text 1]: " + sign_map.toString());
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

	private static String buildBody(String sku, String offset, String num, String type) {
		try {
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("offset", offset);
			jsonParams.put("num", num);

			jsonParams.put("sku", sku);
			jsonParams.put("type", type);
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
