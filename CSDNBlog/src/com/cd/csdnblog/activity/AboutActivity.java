package com.cd.csdnblog.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.R;
import com.cd.csdnblog.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

public class AboutActivity extends BaseActivity implements OnClickListener{

	private LinearLayout qqView;
	private TextView qqNumView;
	private TextView versionNameView;
	private Context mContext;
	private final String mPageName = "AboutActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_about);
		
		//友盟统计
		mContext = this;
        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
        // MobclickAgent.setSessionContinueMillis(1000);
		
		initView();
	}
	private void initView() {
		qqView = (LinearLayout) findViewById(R.id.qqView);
		qqNumView = (TextView) findViewById(R.id.qqNumView);
		versionNameView = (TextView) findViewById(R.id.versionNameView);
		
		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String versionName = packageInfo.versionName;
			versionNameView.setText("Android版本："+versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		qqView.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(MyAPP.isFastClick()){
			return;
		}
		switch (v.getId()) {
		case R.id.qqView:
		    String url="mqqwpa://im/chat?chat_type=wpa&uin="+qqNumView.getText();  
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));  
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 MobclickAgent.onPageStart(mPageName);
	     MobclickAgent.onResume(mContext);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
	}

}
