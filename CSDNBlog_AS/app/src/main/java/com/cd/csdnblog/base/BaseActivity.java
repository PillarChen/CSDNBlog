package com.cd.csdnblog.base;

import com.baidu.mobstat.StatService;
import com.cd.csdnblog.R;
import com.cd.csdnblog.view.SwipeBackLayout;
import com.umeng.analytics.MobclickAgent;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

/**
 * 侧滑的父Activity
 * 
 */
public class BaseActivity extends FragmentActivity {
	public SwipeBackLayout swipeBackLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_ani_enter, 0);

		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().getDecorView().setBackgroundDrawable(null);
		swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.base, null);
		swipeBackLayout.attachToActivity(this);
	}

	public void setSwipeBackEnable(boolean enable) {
		swipeBackLayout.setEnableGesture(enable);
	}
	
	/**
	 * 返回键调成此方法
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(0, R.anim.activity_ani_exist);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.activity_ani_exist);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onPause(this);
	}
}
