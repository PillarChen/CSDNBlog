package com.cd.csdnblog.activity;

import cn.bmob.v3.Bmob;
import com.cd.csdnblog.R;
import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.utils.NetManager;
import com.cd.csdnblog.utils.SharedConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class SplashActivity extends Activity{
	private Context mContext;
	private final String mPageName = "SplashActivity";
	private Animation animation;
	private NetManager netManager;
	private SharedPreferences shared;
	private static int TIME = 1000; 
	private View view;
	private boolean first;	//判断是否第一次打开软件
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);
		//友盟统计
		mContext = this;
		shared = new SharedConfig(mContext).GetConfig(); 	// 得到配置文件
		netManager = new NetManager(mContext); 				// 得到网络管理器
        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
        // MobclickAgent.setSessionContinueMillis(1000);
		
		initData();
	}

	private void initData() {
		//友盟自动更新
		UmengUpdateAgent.update(this);
//		UpdateConfig.setDebug(true);
		
		// 初始化 Bmob SDK
		Bmob.initialize(this, MyAPP.APPID);
//		setContentView(R.layout.act_splash);
//		try {
//			Thread.sleep(50000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		startActivity(new Intent(this, MainActivity.class));
//		this.finish();
		initVersion();
	}
	
	/**
	 * 初始化版本信息
	 */
	private void initVersion() {
		TextView tvVersion = (TextView) findViewById(R.id.tv_version);
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String versionName = packageInfo.versionName;
			tvVersion.setText(versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
	    MobclickAgent.onResume(mContext);
	    into();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
	}
	
	// 进入主程序的方法
		private void into() {
			if (netManager.isOpenNetwork()) {
				// 如果网络可用则判断是否第一次进入，如果是第一次则进入欢迎界面
				first = shared.getBoolean("First", true);

				// 设置动画效果是alpha，在anim目录下的alpha.xml文件中定义动画效果
				animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
				// 给view设置动画效果
				view.startAnimation(animation);
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation arg0) {
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
					}

					// 这里监听动画结束的动作，在动画结束的时候开启一个线程，这个线程中绑定一个Handler,并
					// 在这个Handler中调用goHome方法，而通过postDelayed方法使这个方法延迟500毫秒执行，达到
					// 达到持续显示第一屏500毫秒的效果
					@Override
					public void onAnimationEnd(Animation arg0) {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								Intent intent;
								//如果第一次，则进入引导页WelcomeActivity
								if (first) {
									intent = new Intent(SplashActivity.this,
											WelcomeActivity.class);
								} else {
									intent = new Intent(SplashActivity.this,
											MainActivity.class);
								}
								startActivity(intent);
								// 设置Activity的切换效果
								overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
								SplashActivity.this.finish();
							}
						}, TIME);
					}
				});
			} else {
				// 如果网络不可用，则弹出对话框，对网络进行设置
				Builder builder = new Builder(mContext);
				builder.setTitle("没有可用的网络");
				builder.setMessage("是否对网络进行设置?");
				builder.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = null;
								try {
									String sdkVersion = android.os.Build.VERSION.SDK;
									if (Integer.valueOf(sdkVersion) > 10) {
										intent = new Intent(
												android.provider.Settings.ACTION_WIRELESS_SETTINGS);
									} else {
										intent = new Intent();
										ComponentName comp = new ComponentName(
												"com.android.settings",
												"com.android.settings.WirelessSettings");
										intent.setComponent(comp);
										intent.setAction("android.intent.action.VIEW");
									}
									SplashActivity.this.startActivity(intent);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								SplashActivity.this.finish();
							}
						});
				builder.show();
			}
		}
}
