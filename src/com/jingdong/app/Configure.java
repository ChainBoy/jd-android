package com.jingdong.app;

public class Configure {
	private static int DEBUG = 1;

	// if(DEBUBG==1)
	// Debug host
	public static final String serverHostDEBUG = "192.168.3.2:8008";
	// online host
	public static final String serverHostRELEASE = "192.168.3.4:8008";
	public static final String logTag = "[JDSign]";
	public static final String loggerName = "JDSign";
	// thread pool size
	public static final int poolMinSize = 5;
	public static final int poolMaxSize = 20;
	public static final String splitChar = "\t";

	public static final int threadStartSleepTime = 2;
	
	public static final int startThreadWaitTime = 2000;

	public static String serverGetTask = "http://192.168.3.4:8008/get";
	public static String serverDoneTask = "http://192.168.3.4:8008/set";
	public static String serverFailedTask = "http://192.168.3.4:8008/reset";
	
	public static int ALLTYPE = 0;

	// get next task wait time
	public static final int nextGetTaskSleepTime = 2000;
	
	// request timeout setting
	public static final int CONNECTION_TIMEOUT = 300 * 1000; /* 5 seconds */
	public static final int SOCKET_TIMEOUT = 300 * 1000; /* 5 seconds */
	public static final long MCC_TIMEOUT = 300 * 1000; /* 5 seconds */

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
	
	public static void set_alltype(int alltype){
		ALLTYPE = alltype;
	}
}
