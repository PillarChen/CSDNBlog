package com.cd.csdnblog.bean;

import cn.bmob.v3.BmobObject;

/**
 * 我的博客
 * @author topsage
 *
 */
public class Blog extends BmobObject{
	private MyUser author;
	private String blogUserName;
	public Blog() {
		super();
	}
	public Blog(MyUser author, String blogUserName) {
		super();
		this.author = author;
		this.blogUserName = blogUserName;
	}
	public MyUser getAuthor() {
		return author;
	}
	public void setAuthor(MyUser author) {
		this.author = author;
	}
	public String getBlogUserName() {
		return blogUserName;
	}
	public void setBlogUserName(String blogUserName) {
		this.blogUserName = blogUserName;
	}
	@Override
	public String toString() {
		return "Blog [author=" + author + ", blogUserName=" + blogUserName
				+ "]";
	}
	
}
