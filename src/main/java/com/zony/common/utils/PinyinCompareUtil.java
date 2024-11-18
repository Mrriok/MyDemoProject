package com.zony.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 字符串按拼音排序工具类
 *
 * @Author gubin
 * @Date 2020-04-22
 */
public class PinyinCompareUtil {

    public static int compare(String o1, String o2) {
        for (int i = 0; i < o1.length() && i < o2.length(); i++) {
            int codePoint1 = o1.charAt(i);
            int codePoint2 = o2.charAt(i);
            boolean flag = Character.isSupplementaryCodePoint(codePoint1)
                    || Character.isSupplementaryCodePoint(codePoint2);
            if (flag) {
                i++;
            }
            if (codePoint1 != codePoint2) {
                if (flag) {
                    return codePoint1 - codePoint2;
                }
                String pinyin1 = PinyinHelper.toHanyuPinyinStringArray((char) codePoint1) == null
                        ? null : PinyinHelper.toHanyuPinyinStringArray((char) codePoint1)[0];
                String pinyin2 = PinyinHelper.toHanyuPinyinStringArray((char) codePoint2) == null
                        ? null : PinyinHelper.toHanyuPinyinStringArray((char) codePoint2)[0];

                if (pinyin1 != null && pinyin2 != null) { // 两个字符都是汉字
                    if (!pinyin1.equals(pinyin2)) {
                        return pinyin1.compareTo(pinyin2);
                    }
                } else {
                    return codePoint1 - codePoint2;
                }
            }
        }
        return o1.length() - o2.length();
    }
}
