package com.cd.csdnblog.adapter;

import java.util.List;

import com.cd.csdnblog.bean.Blog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	private List<String> authorList;
	List<Blog> mData;

	public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments,
			List<Blog> mData) {
		super(fm);
		this.fragments = fragments;
		this.mData = mData;
	}




	public static String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public Fragment getItem(int position) {

		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
//		Log.i("authorList", "--adapter--" + authorList.get(position).toString());
		return mData.get(position).getBlogUserName();
//		if (authorList.get(position).toString().equals("/Pillar1066527881")||authorList.get(position).toString().equals("Pillar1066527881")) {
//			return "Pillar";
//		} else
//			return authorList.get(position);
	}

}
