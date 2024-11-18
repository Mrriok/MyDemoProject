package com.zony.common.filenet.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;


public class ZonyStringUtil {
	
	/**
	 * @title: isEmpty
	 * @date 2014-5-9
	 * @author Jeffrey
	 * @description: 判断字符串是否为空
	 * @param str
	 *            需要判断的字符串对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}

	/**
	 * @title: isNotEmpty
	 * @date 2014-5-9
	 * @author Jeffrey
	 * @description: 判断字符串是否不为空
	 * @param str
	 *            需要判断的字符串对象
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(String str) {
		return StringUtils.isNotEmpty(str);
	}

	/**
	 * @title: equals
	 * @date 2014-8-2
	 * @author Jeffrey
	 * @description: 比较两个字符串时候相等
	 * @param str1
	 *            字符串1
	 * @param str2
	 *            字符串2
	 * @return 是否相等 true 相等 false 不相等
	 */
	public static boolean equals(String str1, String str2) {
		return StringUtils.equals(str1, str2);
	}

	/**
	 * @title: equalsIgnoreCase
	 * @date 2014-8-2
	 * @author Jeffrey
	 * @description: 比较两个字符串时候相等，忽略大小写
	 * @param str1
	 *            字符串1
	 * @param str2
	 *            字符串2
	 * @return 是否相等 true 相等 false 不相等
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return StringUtils.equalsIgnoreCase(str1, str2);
	}

	/**
	 * @title: getRandomUUID
	 * @date 2014-5-9
	 * @author Jeffrey
	 * @description: 生成系统唯一标识，默认格式为：{ UUID }
	 * @return 生成的唯一标识
	 */
	public static String getUUIDRandom() {
		return "{" + getUUID() + "}";
	}

	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		String beforeUUID = uuid.substring(0, uuid.indexOf("-") + 1);
		String afterUUID = uuid.substring(uuid.lastIndexOf("-"));
		String time = String.valueOf(System.currentTimeMillis());
		String in = time.substring(time.length() - 4);
		return beforeUUID + in + afterUUID;
	}

	/**
	 * @title: getRandomUUID
	 * @date 2014-5-9
	 * @author Jeffrey
	 * @description: 根据前缀后缀生成系统唯一标识，默认格式为：prefix + { UUID } + suffix
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @return 添加前缀后缀后的系统唯一标识，如前缀为空则不添加前缀，如后缀为空则不添加后缀
	 */
	public static String getUUIDRandom(String prefix, String suffix) {
		if (isEmpty(prefix) && isNotEmpty(suffix)) {
			return "{" + getUUID() + "}" + suffix;
		} else if (isNotEmpty(prefix) && isEmpty(suffix)) {
			return prefix + "{" + getUUID() + "}";
		} else if (isNotEmpty(prefix) && isNotEmpty(suffix)) {
			return prefix + "{" + getUUID() + "}" + suffix;
		} else {
			return "{" + getUUID() + "}";
		}
	}

	/**
	 * @Title: getQueryStrWithQuote
	 * @Description: 将字符串或数字集合转成逗号分隔的查询字符串，并且带引号（针对字符型字段）
	 * @param idList
	 * @return
	 */
	public static String getQueryStrWithQuote(List<?> idList) {
		return getQueryStr(idList, true);
	}

	/**
	 * @Title: getQueryStrNoQuote
	 * @Description: 将字符串或数字集合转成逗号分隔的查询字符串，并且带引号（针对数字型字段）
	 * @param idList
	 * @return
	 */
	public static String getQueryStrNoQuote(List<?> idList) {
		return getQueryStr(idList, false);
	}

	/**
	 * @title: getQueryStr
	 * @date 2014-5-8
	 * @author Jeffrey
	 * @description: 将简单对象数组集合转换为String字符串，对象间以‘，’分隔
	 * @param idList
	 *            需要转换的集合对象
	 * @param includeQuote
	 *            是否需要当成字符串处理，是则添加上单引号
	 * @return 转换完成的字符串
	 */
	private static String getQueryStr(List<?> idList, boolean includeQuote) {
		String queryStr = "";
		if (idList != null && idList.size() > 0) {
			if (includeQuote) {
				for (Object obj : idList) {
					queryStr += "'" + obj + "',";
				}
			} else {
				for (Object obj : idList) {
					queryStr += obj + ",";
				}
			}
			queryStr = queryStr.substring(0, queryStr.length() - 1);
		}
		return queryStr;
	}

	

	
	/**
	 * @title: getString
	 * @date 2014-5-8
	 * @author Jeffrey
	 * @description: 将简单类型数组集合转换为String字符串，对象间以' | '分隔
	 * @param list
	 *            需要转换的数据集合
	 * @return 转换完成的字符串
	 */
	public static String getString(List<?> list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(String.valueOf(list.get(i)));
			if (i != list.size() - 1) {
				sb.append("|");
			}
		}
		return sb.toString();

	}

	/**
	 * @title: getFormatStr
	 * @date 2014年8月26日
	 * @author Jeffrey
	 * @description: 格式化字符串
	 * @param bytes
	 *            需要转码的byte数组
	 * @param charsetName
	 *            转码格式化编码名称
	 * @return 格式化后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码转换异常
	 */
	public static String getFormatStr(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
		return new String(bytes, charsetName);
	}

	/**
	 * @title: getServletResStr
	 * @date 2014年8月26日
	 * @author Jeffrey
	 * @description: 格式化Servlet输出的中文格式
	 * @param bytes
	 *            需要输出的格式化bytes数组
	 * @return 格式化后的字符串 格式编码为ISO-8859-1
	 * @throws UnsupportedEncodingException
	 *             编码转换异常
	 */
	public static String getServletResStr(byte[] bytes) throws UnsupportedEncodingException {
		return getFormatStr(bytes, "ISO-8859-1");
	}

	/**
	 * @Title: hasLength
	 * @Description: 判断字符串不为空且长度不为0
	 * @param str
	 * @return
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	public static String getStringValue(Object source) {
		return source == null ? "" : source.toString();
	}

	public static ToStringBuilder createBuilder(Object o) {
		return new ToStringBuilder(o, ToStringStyle.MULTI_LINE_STYLE);
	}

	public static void appendSectionEndLine(ToStringBuilder b) {
		b.append("=== ------------------------------------------ ==");
	}
	
	
	
}
