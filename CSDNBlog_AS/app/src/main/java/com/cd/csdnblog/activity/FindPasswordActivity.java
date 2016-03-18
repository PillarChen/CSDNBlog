package com.cd.csdnblog.activity;

import com.cd.csdnblog.R;
import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.base.BaseActivity;
import com.cd.csdnblog.utils.ValidateUtil;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**找回密码
 * @author TopSage
 *
 */
public class FindPasswordActivity extends BaseActivity implements OnClickListener{
	
	private View backBtn;
	private TextView reSetPwdBtn;
	private EditText emailView;
	private TextView banner_title;
	private Context mContext;
	private final String mPageName = "FindPasswordActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_password);
		
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
		emailView=(EditText)findViewById(R.id.emailView);
		backBtn=(View)findViewById(R.id.banner_left_btn);
		reSetPwdBtn=(TextView)findViewById(R.id.reSetPwdBtn);
		banner_title = (TextView) findViewById(R.id.banner_title);
		banner_title.setText("找回密码");
		
		backBtn.setOnClickListener(this);
		reSetPwdBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(MyAPP.isFastClick()){
			return;
		}
		switch (v.getId()) {
		case R.id.banner_left_btn:
			this.onBackPressed();
			break;
		case R.id.reSetPwdBtn:
			if(ValidateUtil.isEmail(emailView.getText().toString().trim())){
				final String email = emailView.getText().toString();
				BmobUser.resetPasswordByEmail(this, email, new ResetPasswordByEmailListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						MyAPP.toast(getApplicationContext(),"重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
					}

					@Override
					public void onFailure(int code, String e) {
						// TODO Auto-generated method stub
						MyAPP.toast(getApplicationContext(),"重置密码失败:" + e);
					}
				});
				
			}else{//客户端验证邮箱合法性
				clientValidate();
			}
			
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 客户端验证
	 */
	private void clientValidate() {
		String message="";
		String email=emailView.getText().toString().trim();
		if("".equals(email)){
			message+="邮箱不能为空！\n\n";
			emailView.requestFocus();
		}else if(!ValidateUtil.isEmail(email)){
			message+="邮箱格式不正确！\n\n";
			emailView.requestFocus();
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
