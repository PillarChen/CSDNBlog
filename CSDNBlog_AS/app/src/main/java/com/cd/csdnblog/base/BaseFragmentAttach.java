package com.cd.csdnblog.base;

import com.baidu.mobstat.StatService;
import com.cd.csdnblog.activity.MainActivity;

import android.app.Activity;
import android.support.v4.app.Fragment;

public abstract class BaseFragmentAttach extends Fragment {

	protected MainActivity mActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/**
		 * Fragment页面起始 (注意： 如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
		 */
		StatService.onResume(this);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/**
		 *Fragment 页面结束（注意：如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
		 */
		StatService.onPause(this);
	}
}
