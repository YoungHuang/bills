package com.hy.bills.utils;

public class RegexUtils {
	public final static String CHINESE_LETTER_NUM = "[a-zA-Z0-9\u4e00-\u9fa5]+";

	public static boolean isChineseLetterNum(String str) {
		return str.matches(CHINESE_LETTER_NUM);
	}
}
