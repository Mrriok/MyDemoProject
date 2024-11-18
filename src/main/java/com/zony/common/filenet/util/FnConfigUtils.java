package com.zony.common.filenet.util;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * @fileName CEConfigUtils.java
 * @package com.zony.filenet.util
 * @function 加载CEPE环境变量到系统变量中
 * @version 1.0.0
 * @date 2014-7-31
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class FnConfigUtils {
	public static void config() {
		setSystemVars(FnConfigOptions.getEnv());
	}

	private static void setSystemVars(Hashtable<String, String> env) {
		Iterator<String> it = env.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = env.get(key);
			System.setProperty(key, value);
		}
	}
}
