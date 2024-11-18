package com.zony.common.filenet.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @fileName ZonyDateUtil.java
 * @package com.zony.common.util
 * @function 系统日期工具类
 * @version 1.0.0
 * @date 2014-5-8
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class ZonyDateUtil {
	/**
	 * 系统统一的日期格式
	 */
	public static final String DATEFORMATSTR = "yyyy-MM-dd HH:mm:ss";
	public static final String DATEFORMATFULLSTR = "yyyy-MM-dd HH:mm:ss.S";
	public static final String DATESIMGLEFORMATSTR = "yyyy-MM-dd";
	public static final String SYSTEMDEFULTFORMATSTR = DATEFORMATSTR;

	/**
	 * @title: getNow
	 * @date 2014-5-8
	 * @author Jeffrey
	 * @description: 根据系统预设的日期格式返回当前时间的字符串
	 * @return 日期时间字符串
	 */
	public static String getNow() {
		return formatNow(SYSTEMDEFULTFORMATSTR);
	}

	/**
	 * @title: getNow
	 * @date 2014-5-8
	 * @author Jeffrey
	 * @description: 请谨慎使用此方法，一般系统都使用统一的日期格式；
	 *               根据指定的日期格式获取当前系统时间的字符串，
	 * @param formatstr
	 *            日期格式
	 * @return 根据日期格式返回日期字符串
	 */
	public static String formatNow(String formatstr) {
		String formatedatestr = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(formatstr);
			Date currentTime = new Date();
			formatedatestr = formatter.format(currentTime);
		} catch (Exception e) {
			System.out.println("FormateDate error:" + e);
		}
		return formatedatestr;
	}

	public static String formatDate(Date d_date, String pattern) {
		if (null == d_date) {
			return "";
		}
		String formatedatestr = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			formatedatestr = formatter.format(d_date);
		} catch (Exception e) {
			System.out.println("FormateDate error:" + e);
		}
		return formatedatestr;
	}

	/**
	 * @title: getNowTimeStamp
	 * @date 2014-5-8
	 * @author Jeffrey
	 * @description: 返回当前日期的数据库日期对象 Timestamp
	 * @return 数据库日期对象Timestamp
	 */
	public static Timestamp getNowTimeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static String getTimeStampStr(Timestamp timeStamp) {
		return dateToString(new Date(timeStamp.getTime()));
	}

	/**
	 * @title: dateToString
	 * @date 2014-5-8
	 * @author Jeffrey
	 * @description: 根据系统默认的日期格式返回当成传入日期对象的日期字符串 ,日期格式为：yyyy-MM-dd HH:mm:ss
	 * @param date
	 *            需要转换的日期对象
	 * @return 包含年月日时分秒的字符串
	 */
	public static String dateToString(Date date) {
		return formatDate(date, DATEFORMATSTR);
	}

	/**
	 * @title: dateToString
	 * @date 2014-8-2
	 * @author Jeffrey
	 * @description: 获取当前时间的字符串，日期格式为：yyyy-MM-dd HH:mm:ss
	 * @return 包含年月日时分秒的字符串
	 */
	public static String dateToString() {
		return formatDate(new Date(), DATEFORMATSTR);
	}

	/**
	 * @title: dateToFullString
	 * @date 2014-5-8
	 * @author Jeffrey
	 * @description: 获取当前时间的字符串，日期格式 yyyy-MM-dd HH:mm:ss.S
	 * @param date
	 *            需要转换的日期对象
	 * @return 包含年月日时分秒毫秒的字符串
	 */
	public static String dateToFullString(Date date) {
		return formatDate(date, DATEFORMATFULLSTR);
	}

	/**
	 * @title: dateToFullString
	 * @date 2014-5-8
	 * @author Jeffrey
	 * @description: 获取当前时间的字符串，日期格式 yyyy-MM-dd HH:mm:ss.S
	 * @return 包含年月日时分秒毫秒的字符串
	 */
	public static String dateToFullString() {
		return formatDate(new Date(), DATEFORMATFULLSTR);
	}

	public static boolean before(String sdate1, String sdate2) {
		Date date1 = new Date(Integer.parseInt(sdate1.substring(0, 4)), Integer.parseInt(sdate1.substring(5, 7)), Integer.parseInt(sdate1.substring(8, 10)));
		Date date2 = new Date(Integer.parseInt(sdate2.substring(0, 4)), Integer.parseInt(sdate2.substring(5, 7)), Integer.parseInt(sdate2.substring(8, 10)));
		return date1.before(date2);
	}

	/**
	 * @title: minusDayByNow
	 * @date 2014-5-8
	 * @author Jeffrey
	 * @description: 根据当前系统时间减去传入参数的天数。
	 * @param day
	 *            需要减去的天数
	 * @param formatStr
	 *            处理完成后返回的日期格式
	 * @return 默认返回年-月-日格式
	 */
	public static String minusDayByNow(int day, String formatStr) {
		long date_3_hm = System.currentTimeMillis() - 3600000 * 24 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		if (StringUtils.isEmpty(formatStr)) {
			formatStr = "yyyy-MM-dd";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		String formatedatestr = formatter.format(date_3_hm_date);
		formatedatestr = formatter.format(date_3_hm_date);
		return formatedatestr;
	}

	/**
	 * @title: parseDate
	 * @date 2014-5-8
	 * @author Jeffrey
	 * @description: 根据传入的日期字符串，及日期格式转换字符串为日期对象
	 * @param datestr
	 *            日期字符串
	 * @param pattern
	 *            日期字符串的格式
	 * @return 日期对象
	 */
	public static Date parseDate(String datestr, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date parseUTCDate(String date) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("UTC"));
			return format.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException("Parse String to Date failed.", e);
		}
	}

	public static String parseDateToUTCString(Date date) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
		return format.format(getUTCTime(date));
	}

	public static String parseDateToLocalString(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy'年'MM'月'dd'日'HH'时'mm'分'ss'秒'");
		return format.format(getLocalTime(date));
	}

	private static Date getUTCTime(Date date) {
		Calendar cal = Calendar.getInstance();
		int offset = cal.get(15);
		return new Date(date.getTime() - (long) offset);
	}

	private static Date getLocalTime(Date date) {
		Calendar cal = Calendar.getInstance();
		int offset = cal.get(15);
		return new Date(date.getTime() + (long) offset);
	}
}
