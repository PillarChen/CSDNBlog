package com.cd.csdnblog.bean;

import cn.bmob.v3.BmobObject;

/**
 * 我的博客
 * @author topsage
 *
 */
public class Feedback extends BmobObject{
	private String contact;
	private String content;
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Feedback(String contact, String content) {
		super();
		this.contact = contact;
		this.content = content;
	}
	public Feedback() {
		super();
	}
	@Override
	public String toString() {
		return "Feedback [contact=" + contact + ", content=" + content + "]";
	}
	
}
