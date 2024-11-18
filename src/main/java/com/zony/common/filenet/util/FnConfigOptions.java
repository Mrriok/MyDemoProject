package com.zony.common.filenet.util;

import com.zony.app.config.ZonyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.Hashtable;
import java.util.MissingResourceException;

/**
 * 加载关于FileNet相关配置的工具类
 *
 * @Author gubin
 * @Date 2020-05-15
 **/
public class FnConfigOptions {
	private static final Logger logger = LoggerFactory.getLogger(FnConfigOptions.class);
	private static Hashtable<String, String> env = new Hashtable<String, String>();
	private static ZonyConfig zonyConfig;

	public static String getContentEngineUrl() {
		return zonyConfig.getFnCeUrl();
	}

	public static String getObjectStoreName() {
		return zonyConfig.getFnObjectStoreName();
	}

	public static String getUserName() {
		return zonyConfig.getFnAdmin();
	}

	public static String getPassword() {
		return zonyConfig.getFnPassword();
	}

	public static Hashtable<String, String> getEnv() {
		return env;
	}

	/**
	 * Initializes the FileNet Content Engine configuration options
	 */
	public static void init(ZonyConfig zonyConfig) {
		try {
			FnConfigOptions.zonyConfig = zonyConfig;
			String jaasConfigFile = new File(FnConfigOptions.class.getClassLoader().getResource("jaas.conf").getPath()).getAbsolutePath();
			env.put("java.security.auth.login.config", jaasConfigFile);
			env.put("filenet.pe.bootstrap.ceuri", getContentEngineUrl());
		} catch (MissingResourceException mre) {
			logger.error("JAAS configuration file 'jaas.conf' cannot be found.", mre);
		}
		FnConfigUtils.config();
	}
}
