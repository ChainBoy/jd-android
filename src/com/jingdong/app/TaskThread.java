package com.jingdong.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class TaskThread implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;

	public static void test() {
		signTask("358239051596619-020000000000,2782638,1,10");
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
			if (resultMap.get("result") == "finish") {
				break;
			}
			try {
				Thread.sleep(Configure.nextGetTaskSleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
		resultMap.put("result", "");
		String taskString = "";
		ArrayList<Map> taskMapList = TaskManager.getTaskMapList(Configure.serverGetTask);
		System.out.println("get task size: " + taskMapList.size());
		for (Map taskMap : taskMapList) {
			taskString = (String) taskMap.get("task");
			if (taskString == "finish")
				continue;
			signTask(taskString);
		}
		if (taskString == "finish")
			resultMap.put("result", "finish");
		System.out.println("==================== sign end, wait next ... ===============");
	}

	private static void signTask(String taskString) {
		String taskArgs[] = taskString.split(",");
		String paramsString = "";
		String uuid = taskArgs[0];
		String sku = taskArgs[1];
		String page = taskArgs[2];
		String size = taskArgs[3];

		paramsString = buildBody(sku, page, size);
		String signString = sign("getMobileCommentList", paramsString, uuid);
		// Log.d("sign" , taskString + ":" + signString);
		System.out.println(taskString + " result params:" + paramsString);
		System.out.println(taskString + " result sign:" + signString);

		TaskManager.sendTaskSign(Configure.serverDoneTask, taskString, signString, paramsString);
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
