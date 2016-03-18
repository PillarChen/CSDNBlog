package com.cd.csdnblog.bean;

public class HomeItemBean implements Comparable<HomeItemBean> {

	public String title;
	public String titleImg;
	public int type;
	public int column_id;
	public String titleIcon;
	public String top;

	public HomeItemBean() {
	}

	public HomeItemBean(String title, String titleImg, int type, int column_id,
			String titleIcon, String top) {
		super();
		this.title = title;
		this.titleImg = titleImg;
		this.type = type;
		this.column_id = column_id;
		this.titleIcon = titleIcon;
		this.top = top;
	}

	@Override
	public String toString() {
		return "HomeItemBean [title=" + title + ", titleImg=" + titleImg
				+ ", type=" + type + ", column_id=" + column_id
				+ ", titleIcon=" + titleIcon + ", top=" + top + "]";
	}

	@Override
	public int compareTo(HomeItemBean another) {
		if (another == null)
			return 0;
		if (this.column_id == another.column_id) {
			return 0;
		} else if (this.column_id < another.column_id) {
			return -1;
		} else {
			return 1;
		}
	}

}
