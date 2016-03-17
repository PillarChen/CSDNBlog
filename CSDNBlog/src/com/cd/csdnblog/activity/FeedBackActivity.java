package com.cd.csdnblog.activity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.R;
import com.cd.csdnblog.adapter.BlobUserNameAdapter;
import com.cd.csdnblog.base.BaseActivity;
import com.cd.csdnblog.bean.Blog;
import com.cd.csdnblog.bean.Feedback;
import com.cd.csdnblog.bean.MyUser;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class FeedBackActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = "FeedBackActivity";
	private EditText feedbackContentView;
	private EditText contactView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);

		initView();
	}

	public void initView() {
		feedbackContentView = (EditText) findViewById(R.id.feedbackContentView);
		contactView = (EditText) findViewById(R.id.contactView);
		((RelativeLayout) findViewById(R.id.sendBtn)).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.sendBtn:// 提交
			sendFeedBack();
			break;

		default:
			break;
		}

	}
	
	/**
	 * 意见反馈
	 */
	private void sendFeedBack() {
		if(TextUtils.isEmpty(feedbackContentView.getText().toString())){
			MyAPP.toast(getApplicationContext(),"反馈意见不能为空!");
			return;
		}
		final Feedback feedbackBean=new Feedback();
		feedbackBean.setContact(contactView.getText().toString());
		feedbackBean.setContent(feedbackContentView.getText().toString());
		feedbackBean.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				MyAPP.toast(getApplicationContext(),"反馈成功!");
				contactView.setText("");
				feedbackContentView.setText("");
			}
			
			@Override
			public void onFailure(int code, String msg) {
				MyAPP.toast(getApplicationContext(),"反馈失败：" + msg);
				
			}
		});
	}
}
