package com.jingdong.app;

public class Configure {
	private static int DEBUG = 1;

	// if(DEBUBG==1)
	public static final String serverHostDEBUG = "10.16.52.51:8888";
	public static final String serverHostRELEASE = "192.168.3.4:8008";
	public static final String logTag = "[JDSign]";
	public static final String loggerName = "JDSign";
	public static final int poolMinSize = 2;
	public static final int poolMaxSize = 10;

	public static final int threadStartSleepTime = 2;

	public static final int startThreadWaitTime = 2000;

	public static String serverGetTask = "http://192.168.3.4:8008/get";
	public static String serverDoneTask = "http://192.168.3.4:8008/set";
	public static String serverFailedTask = "http://192.168.3.4:8008/reset";

	public static final int nextGetTaskSleepTime = 2000;

	public static final int CONNECTION_TIMEOUT = 5000; /* 5 seconds */
	public static final int SOCKET_TIMEOUT = 5000; /* 5 seconds */
	public static final long MCC_TIMEOUT = 5000; /* 5 seconds */

	public static void set_debug(int debug) {
		DEBUG = debug;
		String serverHost = "";
		if (debug == 1) {
			serverHost = serverHostDEBUG;
		} else
			serverHost = serverHostRELEASE;
		serverGetTask = "http://" + serverHost + "/get";
		serverDoneTask = "http://" + serverHost + "/set";
		serverFailedTask = "http://" + serverHost + "/reset";
	}
}
