package com.cd.csdnblog.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 * 
 * @author TopSage
 * 
 */
public class ValidateUtil {
	/**
	 * 客户端验证邮箱地址的合法性
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail) {
		String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}
	public static boolean isDouble(String strNum) {
		String strPattern = "^(-?\\d+)(\\.\\d+)?$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strNum);
		return m.matches();
	}
}
