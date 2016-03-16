package com.cd.csdnblog.activity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import com.cd.csdnblog.R;
import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.adapter.BlobUserNameAdapter;
import com.cd.csdnblog.base.BaseActivity;
import com.cd.csdnblog.bean.Blog;
import com.cd.csdnblog.bean.MyUser;
import com.cd.csdnblog.utils.DialogTool;
import com.cd.csdnblog.utils.SharePreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView registerBtn;
	private TextView findPasswordBtn;
	private TextView loginBtn;
	private TextView banner_title;
	private TextView versionCode;
	private EditText userNameView;
	private EditText passwordView;
	public static String username = "";
	public static String password = "";
	private PackageInfo packageInfo;
	private Context mContext;
	private final String mPageName = "LoginActivity";
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				DialogTool.dismissDialog();
//				MyAPP.toast(getApplicationContext(),"登陆成功");
				finish();	
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
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
		registerBtn = (TextView) findViewById(R.id.registerTv);
		findPasswordBtn = (TextView) findViewById(R.id.findPwdTv);
		versionCode = (TextView) findViewById(R.id.versionCode);
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionCode.setText(packageInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		banner_title = (TextView) findViewById(R.id.banner_title);
		banner_title.setText("登录");
		userNameView = (EditText) findViewById(R.id.userName);
		passwordView = (EditText) findViewById(R.id.password);
		loginBtn = (TextView) findViewById(R.id.loginBtn);

		registerBtn.setOnClickListener(this);
		findPasswordBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(MyAPP.isFastClick()){
			return;
		}
		switch (v.getId()) {
		case R.id.registerTv:
			startActivity(new Intent(this,RegisterActivity.class));
			break;
		case R.id.findPwdTv:
			startActivity(new Intent(this, FindPasswordActivity.class));
			break;
	
		case R.id.loginBtn:
			login();
			break;
		default:
			break;
		}

	}

	private void login() {
		MyAPP.username=userNameView.getText().toString();
		MyAPP.password=passwordView.getText().toString();
		if(TextUtils.isEmpty(MyAPP.username)||TextUtils.isEmpty(MyAPP.password)){
			MyAPP.toast(getApplicationContext(),"用户名或密码不能为空!");
			return;
		}
		DialogTool.showDialog(mContext, "", "加载中......");
		final BmobUser bUser = new BmobUser();
		bUser.setUsername(MyAPP.username);
		bUser.setPassword(MyAPP.password);
		bUser.login(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				MyAPP.toast(getApplicationContext(),bUser.getUsername() + "登陆成功");
				MyAPP.isLogin=true;
				MainActivity.refreshMyBlogFragment();
				SharePreferencesUtil.putStringValue(getApplicationContext(), "username", MyAPP.username);
				MyAPP.userid=bUser.getObjectId();
				SharePreferencesUtil.putStringValue(getApplicationContext(), "userid", MyAPP.userid);
				SharePreferencesUtil.putStringValue(getApplicationContext(), "password", MyAPP.password);
				SharePreferencesUtil.putBooleanValue(getApplicationContext(), "isLogin", MyAPP.isLogin);
				queryMyBlog();
			}
			
			@Override
			public void onFailure(int code, String msg) {
				MyAPP.toast(getApplicationContext(),"登陆失败:" + msg);
				DialogTool.dismissDialog();
			}
		});
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.finish();
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
	
	/**
	 *  查询我的博客
	 */
	private void queryMyBlog(){
		MyUser user=BmobUser.getCurrentUser(mContext, MyUser.class);
		if(user==null){
			return;
		}
		final BmobQuery<Blog> bmobQuery= new BmobQuery<Blog>();
		bmobQuery.order("createdAt");
		
		bmobQuery.addWhereEqualTo("author", user);
//		if(!isRePost){
//			//先判断是否有缓存
//			boolean isCacheMyBlog = bmobQuery.hasCachedResult(mContext,Blog.class);
//			if(isCacheMyBlog){
//				bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
//			}else{
//				bmobQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	// 如果没有缓存的话，则先从网络中取
//			}
//		}
		bmobQuery.findObjects(mContext, new FindListener<Blog>() {
			
			@Override
			public void onSuccess(List<Blog> object) {
				// TODO Auto-generated method stub
//				MyAPP.toast(mContext.getApplicationContext(),"查询成功：共"+object.size()+"条数据。");
				MyAPP.mMyBlogData=object;
				handler.sendEmptyMessage(0);
			}
			
			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				MyAPP.toast(mContext.getApplicationContext(),"查询失败："+msg);
			}
		});
	}

}
