package com.zony.common.filenet.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexUtil {

	private static Logger logger = LoggerFactory.getLogger(RegexUtil.class);

	private static final Pattern PATTERN_DATA_YM = Pattern.compile("^([12][0-9]{3})(-)(1[0-2]|0?[1-9])$");
	private static final Pattern PATTERN_DATA_YMD = Pattern.compile("^([12][0-9]{3})(-)(1[0-2]|0?[1-9])(-)([12][0-9]|3[01]|0?[1-9])$");
	private static final Pattern PATTERN_VERSION = Pattern.compile("^[1-9][0-9]?$");
	private static final Pattern PATTERN_METADATA = Pattern.compile("^[A-Za-z][A-Za-z_0-9]*$");

	private RegexUtil() {
	}

	public static boolean isValidDateYM(String date) {
		return regexFind(PATTERN_DATA_YM, date);
	}

	public static boolean isValidVersion(String version) {
		return regexFind(PATTERN_VERSION, version);
	}

	public static boolean isValidMetadata(String metadata) {
		return regexFind(PATTERN_METADATA, metadata);
	}

	// public static boolean regexEqual(String regex, String str) {
	// return regexFind(Pattern.compile(regex), str);
	// }

	private static boolean regexFind(Pattern pattern, String str) {
		if (!ZonyStringUtil.isEmpty(str)) {
			Matcher m = pattern.matcher(str);
			if (m.find()) {
				// logger.debug("RegexEqual : " + pattern.pattern() + " Equal "
				// + str);
				return true;
			}
		}
		// logger.debug("RegexEqual : " + pattern.pattern() + " Not Equal " +
		// str);
		return false;
	}

	public static String getFileNetStartDateForSearch(String date) {
		return getFileNetDateForSearch(date, 0, 0, 0);
	}

	public static String getFileNetEndDateForSearch(String date) {
		return getFileNetDateForSearch(date, 23, 59, 59);
	}

	/**
	 * 生成FileNet的时间格式,并消除因时区而引起的时间偏移
	 * 
	 * @param date
	 * @param hour
	 * @param mintue
	 * @param second
	 * @return
	 */
	private static String getFileNetDateForSearch(String date, int hour, int mintue, int second) {
		if (!ZonyStringUtil.isEmpty(date)) {
			Matcher m = PATTERN_DATA_YMD.matcher(date);
			if (m.find() && m.group().length() == date.length()) {
				Date d = ZonyDateUtil.parseUTCDate(date + " " + hour + ":" + mintue + ":" + second);
				d.setTime(d.getTime() - TimeZone.getDefault().getRawOffset());
				String str = ZonyDateUtil.formatDate(d, "yyyyMMdd HHmmss");
				String[] strs = str.split(" ");

				String res = strs[0] + "T" + strs[1] + "Z";
				logger.debug("valid date : " + res);
				return res;
			}
		}
		logger.debug("illegal date : " + date);
		return null;
	}

}
