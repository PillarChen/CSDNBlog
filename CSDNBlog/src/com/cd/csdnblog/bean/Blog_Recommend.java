package com.cd.csdnblog.bean;

import cn.bmob.v3.BmobObject;

/**
 * 推荐博客
 * @author topsage
 *
 */
public class Blog_Recommend extends BmobObject{
	private String blogUserName;

	public String getBlogUserName() {
		return blogUserName;
	}

	public void setBlogUserName(String blogUserName) {
		this.blogUserName = blogUserName;
	}

	public Blog_Recommend(String blogUserName) {
		super();
		this.blogUserName = blogUserName;
	}

	@Override
	public String toString() {
		return "Blog_Recommend [blogUserName=" + blogUserName + "]";
	}
	
}
