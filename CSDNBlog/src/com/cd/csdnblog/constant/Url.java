package com.cd.csdnblog.constant;

import com.umeng.socialize.utils.Log;

public class Url {
	public static final String BASEURL = "http://blog.csdn.net";
	public static final String URL_NOTELIST = "http://blog.csdn.net";
	public static final String URL_BLOB = "/pillar1066527881";
	public static final String URL_BLOB1= "/nugongahou110";
	public static final String URL_BLOB2 = "/wwj_748";
	public static final String URL_BLOB3 = "/kaiwii";
	public static final String URL_BLOB4 = "/zd_1471278687";
	public static final String URL_BLOB5 = "/lmj623565791";
	
	/**
	 * 获取博客列表的URL
	 * 
	 * @param blogType
	 *            博客类型
	 * @param page
	 *            页数
	 * @return
	 */
	public static String getBlogListURL(int blogType, String page,String url) {
		String mUrl="";
		switch (blogType) {
		case 0:
			mUrl=url+ "/article/list";
			break;
		default:
			break;
		}
		mUrl = mUrl+"/" + page;
		Log.i("mUrl","--mUrl--"+mUrl);
		return mUrl;
	}
}
