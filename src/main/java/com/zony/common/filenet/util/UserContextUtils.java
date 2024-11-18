package com.zony.common.filenet.util;

import com.filenet.api.core.Connection;
import com.filenet.api.util.UserContext;

import javax.security.auth.Subject;

/**
 * @fileName UserContextUtils.java
 * @package com.zony.filenet.util
 * @function 用户认证信息处理类
 * @version 1.0.0
 * @date 2014-7-31
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */

public class UserContextUtils {

	/**
	 * @title: popSubject
	 * @date 2014-7-31
	 * @author Jeffrey
	 * @description: 移除当前用户认证信息
	 * @return
	 */
	public static Subject popSubject() {
		UserContext uc = UserContext.get();
		return uc.popSubject();
	}

	/**
	 * @title: pushObject
	 * @date 2014-7-31
	 * @author Jeffrey
	 * @description: 根据Connection连接对象，用户名、密码信息，构建当前用户的认证信息对象
	 * @param conn
	 *            服务器连接对象
	 * @param username
	 *            用户名
	 * @param password
	 *            用户密码
	 * @return 用户认证信息
	 */
	public static Subject pushSubject(Connection conn, String username, String password) {
		Subject subject = UserContext.createSubject(conn, username, password, "FileNetP8WSI");
		
		pushSubject(subject);
		return subject;
	}

	/**
	 * @title: pushObject
	 * @date 2014-7-31
	 * @author Jeffrey
	 * @description: 根据服务器连接对象，认证信息 生成当前用户认证
	 * @param conn
	 *            服务器连接对象 Connection
	 * @param subject
	 *            认证对象
	 */
	public static void pushSubject(Subject subject) {
		UserContext uc = UserContext.get();
		uc.pushSubject(subject);
	}

}