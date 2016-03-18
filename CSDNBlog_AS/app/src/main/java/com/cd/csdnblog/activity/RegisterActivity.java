package com.cd.csdnblog.activity;

import com.cd.csdnblog.R;
import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.base.BaseActivity;
import com.cd.csdnblog.bean.MyUser;
import com.cd.csdnblog.utils.ValidateUtil;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.listener.SaveListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity implements OnClickListener{
	private EditText usernameView;
	private EditText emailView;
	private EditText passwordView1;
	private EditText passwordView2;
	private EditText ageView;
	private TextView banner_title;
	private TextView hintView;
	private Context mContext;
	private final String mPageName = "RegisterActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
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
	/**
	 * 
	 */
	public void initView() {
		
		usernameView = (EditText) findViewById(R.id.usernameView);
		emailView = (EditText) findViewById(R.id.emailView);
		hintView = (TextView) findViewById(R.id.hintView);
		banner_title = (TextView) findViewById(R.id.banner_title);
		banner_title.setText("注册");
		passwordView1 = (EditText) findViewById(R.id.passwordView1);
		passwordView2 = (EditText) findViewById(R.id.passwordView2);
		ageView = (EditText) findViewById(R.id.ageView);
		
		findViewById(R.id.registerBtn).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
//		if(MyAPP.isFastClick()){
//			return;
//		}
		switch (v.getId()) {
		case R.id.registerBtn:

			String message = "";
			String username = usernameView.getText().toString().trim();
			String email = emailView.getText().toString().trim();
			String password1 = passwordView1.getText().toString().trim();
			String password2 = passwordView2.getText().toString().trim();
			if ("".equals(usernameView.getText().toString().trim())) {
				message += "用户名不能为空！";
				usernameView.requestFocus();
				hintView.setVisibility(View.VISIBLE);
				hintView.setText(message);
				return;
			}	
			if ("".equals(email)) {
				message += "邮箱不能为空！\n\n";
				emailView.requestFocus();
				hintView.setVisibility(View.VISIBLE);
				hintView.setText(message);
				return;
			} else if (!ValidateUtil.isEmail(email)) {
				message += "邮箱格式不正确！\n\n";
				emailView.requestFocus();
				hintView.setVisibility(View.VISIBLE);
				hintView.setText(message);
				return;
			}
			if ("".equals(password1) || "".equals(password2)) {
				message += "密码不能为空！\n\n";
				passwordView1.requestFocus();
				hintView.setVisibility(View.VISIBLE);
				hintView.setText(message);
				return;
			} else if (!password1.equals(password2)) {
				message += "密码输入不一致！\n\n";
				passwordView1.setText("");
				passwordView2.setText("");
				passwordView1.requestFocus();
				hintView.setVisibility(View.VISIBLE);
				hintView.setText(message);
				return;
			}
			if("".equals(ageView.getText().toString())||!TextUtils.isDigitsOnly(ageView.getText().toString())){
				message += "年龄只能为数字！\n\n";
				hintView.setVisibility(View.VISIBLE);
				hintView.setText(message);
				return;
			}
			
			
			hintView.setVisibility(View.GONE);
			
			final MyUser myUser = new MyUser();
			myUser.setUsername(username);
			myUser.setPassword(password1);
			myUser.setAge(Integer.parseInt(ageView.getText().toString()));
			myUser.setEmail(email);
			myUser.signUp(this, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					MyAPP.toast(getApplicationContext(),"注册成功!");
					finish();
				}

				@Override
				public void onFailure(int code, String msg) {
					// TODO Auto-generated method stub
					MyAPP.toast(getApplicationContext(),"注册失败:" + msg);
				}
			});
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
