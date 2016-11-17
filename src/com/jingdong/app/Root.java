package com.jingdong.app;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Root {
	private static int DEBUG = 0;
	private static int ALLTYPE = 0;
	private static int SETALL = 0;
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Configure.poolMinSize, Configure.poolMaxSize,
			3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3), new ThreadPoolExecutor.DiscardOldestPolicy());

	public static void main(String args[]) {
		for (String s : args) {
			if (s.equalsIgnoreCase("--debug")) {
				DEBUG = 1;
				continue;
			}
			if (s.equalsIgnoreCase("--alltype")) {
				ALLTYPE = 1;
				continue;
			}
			if (s.equalsIgnoreCase("--setall")) {
				SETALL = 1;
				continue;
			}
		}
		Configure.set_debug(DEBUG);
		Configure.set_alltype(ALLTYPE);
		Configure.set_putall(SETALL);
		System.out.println("set debug: " + DEBUG);
		System.out.println("set alltype: " + ALLTYPE);
		System.out.println("set setall: " + SETALL);
		System.out.println("server url: " + Configure.serverGetTask);

		try {
			System.loadLibrary("libjdmobilesecurity.so");
		} catch (java.lang.UnsatisfiedLinkError e) {
		}

		try {
			System.load("/data/local/tmp/libjdmobilesecurity.so");
		} catch (java.lang.UnsatisfiedLinkError e) {
		}

		try {
			System.load("/Users/zhipeng/Desktop/TasteChina/app_crawler/android/libs/libjdmobilesecurity.so");
		} catch (java.lang.UnsatisfiedLinkError e) {
		}

		test();
		start();
	}

	private static void test() {
		try {
			if (DEBUG == 1) {
				Download.test();
				TaskThread.test();
				System.out.println("test finish ..");
			}
			System.out.println("wait, will start ...");
			Thread.sleep(2000);
		} catch (Exception e) {
			System.out.println("test failed");
			e.printStackTrace();
		}
	}

	private static void start() {
		for (int i = 0; i < Configure.poolMaxSize; i++) {
			try {
				threadPool.execute(new TaskThread());
				Thread.sleep(Configure.threadStartSleepTime);
			} catch (Exception e) {
				Logger.getLogger(Configure.loggerName)
						.info(Configure.logTag + "start thread failed: " + e.getMessage());
//				e.printStackTrace();
			}
		}
	}
}
