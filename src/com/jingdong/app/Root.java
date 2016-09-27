package com.jingdong.app;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Root {

	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Configure.poolMinSize, Configure.poolMaxSize,
			3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3), new ThreadPoolExecutor.DiscardOldestPolicy());

	public static void main(String args[]) {
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
			Download.test();
			TaskThread.test();
			System.out.println("test finish, wait ...");
			Thread.sleep(1000);
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
				e.printStackTrace();
			}
		}
	}
}
