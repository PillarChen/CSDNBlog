package com.cd.csdnblog.fragment;

import com.cd.csdnblog.R;
import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.activity.LoginActivity;
import com.cd.csdnblog.activity.MainActivity;
import com.cd.csdnblog.base.BaseFragmentAttach;
import com.cd.csdnblog.utils.DrawerTag;
import com.cd.csdnblog.utils.SharePreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 主页右侧抽屉布局
 * 
 * @author TopSage
 * 
 */
public class RightDrawerFragment extends BaseFragmentAttach implements OnClickListener {

	private TextView firstpage;
	private TextView myBlog;
	private TextView update;
	private TextView about;
	private TextView feedback;
	private TextView loginName;
	private TextView blobManager;
	private TextView clearcache;
	private LinearLayout drawer_clearcache;
	private LinearLayout loginOut;
	private LinearLayout drawer_home;
	private LinearLayout drawer_myBlog;
	private LinearLayout drawer_update;
	private LinearLayout drawer_downloaded_manager;
	private LinearLayout drawer_about;
	private LinearLayout drawer_feedback;
	private View view;
	private ImageView headimgView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_right_drawer, container,false);
		init(view);
		
		
		
		return view;
	}

	@Override
	public void onClick(View v) {
		if(MyAPP.isFastClick()){
			return;
		}
		switch (v.getId()) {
		case R.id.drawer_home:
			mActivity.changeFragment(DrawerTag.Home);
			changeDrawerTitleColor();
			firstpage.setTextColor(getResources().getColor(R.color.black));
//			drawer_home.setBackgroundColor(Color.WHITE);
		
			break;
		case R.id.drawer_myBlog:
			if(!MyAPP.isLogin){
				MyAPP.toast(mActivity, getResources().getString(R.string.hint_unlogin));
				return;
			}
//			if(MyAPP.mMyBlogData==null){
//				return;
//			}
			mActivity.changeFragment(DrawerTag.MyBlog);
			changeDrawerTitleColor();
			myBlog.setTextColor(getResources().getColor(R.color.black));
//			drawer_myBlog.setBackgroundColor(Color.WHITE);
			
			break;
		case R.id.drawer_update:
			mActivity.changeFragment(DrawerTag.Update);
//			changeDrawerTitleColor();
//			update.setTextColor(getResources().getColor(R.color.black));
//			drawer_update.setBackgroundColor(Color.WHITE);
			break;
		case R.id.drawer_blob_manager:
			if(!MyAPP.isLogin){
				MyAPP.toast(mActivity, getResources().getString(R.string.hint_unlogin));
				return;
			}
			mActivity.changeFragment(DrawerTag.BlogManager);
//			changeDrawerTitleColor();
//			blobManager.setTextColor(getResources().getColor(R.color.black));
//			drawer_downloaded_manager.setBackgroundColor(Color.WHITE);
			
			break;
		case R.id.drawer_clearcache:
			mActivity.changeFragment(DrawerTag.ClearCache);
//			changeDrawerTitleColor();
//			clearcache.setTextColor(getResources().getColor(R.color.black));
//			drawer_clearcache.setBackgroundColor(Color.WHITE);
			break;
		case R.id.drawer_about:
			mActivity.changeFragment(DrawerTag.About);
//			changeDrawerTitleColor();
//			about.setTextColor(getResources().getColor(R.color.black));
//			drawer_about.setBackgroundColor(Color.WHITE);
			
			break;
		case R.id.drawer_feedback:
			mActivity.changeFragment(DrawerTag.Feedback);
//			changeDrawerTitleColor();
//			feedback.setTextColor(getResources().getColor(R.color.black));
//			drawer_feedback.setBackgroundColor(Color.WHITE);
			
			break;
		case R.id.drawer_loginOut:
			MyAPP.isLogin=false;
			MyAPP.username=getResources().getString(R.string.default_username);
			SharePreferencesUtil.putStringValue(mActivity, "username", MyAPP.username);
			SharePreferencesUtil.putBooleanValue(mActivity, "isLogin", MyAPP.isLogin);
			changeLoginStatues();
//			MainActivity.refreshMyBlogFragment();
			break;
		case R.id.drawer_account://登录头像，点击进入登录
		case R.id.loginName_frag_right_drawer://登录文字，点击进入登录
			if(MyAPP.isLogin){
				return;
			}
			startActivity(new Intent(mActivity, LoginActivity.class));
			break;
		default:
			break;
		}
	}

	private void changeLoginStatues() {
		if(MyAPP.isLogin){
			loginOut.setVisibility(View.VISIBLE);
			loginName.setText(MyAPP.username);
		}else{
			loginOut.setVisibility(View.GONE);
			loginName.setText(getResources().getString(R.string.txt_unlogin));
		}
		view.invalidate();
	}
	
	
	private void changeDrawerTitleColor() {
//		firstpage.setTextColor(getResources().getColor(R.color.black));
		firstpage.setTextColor(getResources().getColor(R.color.banner_color));
		myBlog.setTextColor(getResources().getColor(R.color.banner_color));
		update.setTextColor(getResources().getColor(R.color.banner_color));
		about.setTextColor(getResources().getColor(R.color.banner_color));
		feedback.setTextColor(getResources().getColor(R.color.banner_color));
		clearcache.setTextColor(getResources().getColor(R.color.banner_color));
		blobManager.setTextColor(getResources().getColor(R.color.banner_color));
	}

	private void init(View view) {
		firstpage = (TextView) view.findViewById(R.id.firstpage_frag_right_drawer);
		myBlog = (TextView) view.findViewById(R.id.myBlog_frag_right_drawer);
		update = (TextView) view.findViewById(R.id.update_frag_right_drawer);
		about = (TextView) view.findViewById(R.id.about_frag_right_drawer);
		feedback = (TextView) view.findViewById(R.id.feedback_frag_right_drawer);
		loginName= (TextView) view.findViewById(R.id.loginName_frag_right_drawer);
		blobManager= (TextView) view.findViewById(R.id.blob_managerTitle);
		loginOut=(LinearLayout)view.findViewById(R.id.drawer_loginOut);
		headimgView=(ImageView)view.findViewById(R.id.headimgView);
		
		drawer_home=(LinearLayout)view.findViewById(R.id.drawer_home);
		drawer_myBlog=(LinearLayout)view.findViewById(R.id.drawer_myBlog);
		drawer_update=(LinearLayout)view.findViewById(R.id.drawer_update);
		drawer_downloaded_manager=(LinearLayout)view.findViewById(R.id.drawer_blob_manager);
		drawer_about=(LinearLayout)view.findViewById(R.id.drawer_about);
		drawer_feedback=(LinearLayout)view.findViewById(R.id.drawer_feedback);
		
		clearcache = (TextView) view.findViewById(R.id.clearcache_frag_right_drawer);
		drawer_clearcache = (LinearLayout) view.findViewById(R.id.drawer_clearcache);

		drawer_clearcache.setOnClickListener(this);
		loginOut.setOnClickListener(this);
		drawer_home.setOnClickListener(this);
		drawer_myBlog.setOnClickListener(this);
		drawer_update.setOnClickListener(this);
		drawer_downloaded_manager.setOnClickListener(this);
		drawer_about.setOnClickListener(this);
		drawer_feedback.setOnClickListener(this);
		view.findViewById(R.id.drawer_account).setOnClickListener(this);
		
		
	}
	
	@Override
    public void onResume() {
    	super.onResume();
    	changeLoginStatues();
    	MobclickAgent.onPageStart("RightDrawerFragment"); //统计页面，"MyBlogFragment"为页面名称，可自定义
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	 MobclickAgent.onPageEnd("RightDrawerFragment"); 
    }
}
