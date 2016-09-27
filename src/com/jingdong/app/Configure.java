package com.jingdong.app;

public class Configure {
	public static final String logTag = "[JDSign]";
	public static final String loggerName = "JDSign";
	public static final int poolMinSize = 2;
	public static final int poolMaxSize = 10;
	
	public static final int threadStartSleepTime = 2;

	public static final int startThreadWaitTime = 2000;
	
	public static final String serverGetTask = "http://192.168.3.4:8008/get_task";
	public static final String serverDoneTask = "http://192.168.3.4:8008/done_task";
	public static final String serverFailedTask = "http://192.168.3.4:8008/failed_task";
	

	public static final int CONNECTION_TIMEOUT = 5000; /* 5 seconds */
	public static final int SOCKET_TIMEOUT = 5000; /* 5 seconds */
	public static final long MCC_TIMEOUT = 5000; /* 5 seconds */
	
	
}
