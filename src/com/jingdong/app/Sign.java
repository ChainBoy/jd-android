package com.jingdong.app;

import java.util.List;
import java.util.Map;

public class Sign {
	public static native Map getSignMap(Map map, List list);

	static {
		// System.load("/data/local/tmp/libjdmobilesecurity.so");
		// System.load("/sdcard/test/libjdmobilesecurity.so");
		// System.loadLibrary("jdmobilesecurity");
	}
}
