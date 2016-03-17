package com.cd.csdnblog.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexMatches {

	//26条数据 共2页1 2 下一页 尾页
	public static String getStr(String str){
		Pattern p=Pattern.compile("[0-9]{1,10}");  
		Matcher m=p.matcher(str);      
		if(m.find()){
			  str=m.group().toString();
		} 
		return str;
	}
}
