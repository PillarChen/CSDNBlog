package com.cd.csdnblog.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.R;
import com.cd.csdnblog.adapter.BlobUserNameAdapter;
import com.cd.csdnblog.base.BaseActivity;
import com.cd.csdnblog.bean.Blog;
import com.cd.csdnblog.bean.Feedback;
import com.cd.csdnblog.bean.MyUser;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.TextView;

public class PayActivity extends BaseActivity implements
		OnClickListener{
	private static final String TAG = "PayActivity";
	private EditText contactView;//CSDN用户名
	private EditText moneyView;//赞助金额
	private EditText contentView;//赞助描述
	private TextView banner_title;
	private TextView payBtn;
	
	private Double price;
	
	RadioGroup type;
	
	String APPID = "50c9706014c6f57506e7d80af2eccdd5";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);

		// 必须先初始化
		BP.init(this, APPID);
		
		initView();
		
		
	}

	public void initView() {
		contactView = (EditText) findViewById(R.id.contactView);
		moneyView = (EditText) findViewById(R.id.moneyView);
		contentView = (EditText) findViewById(R.id.contentView);
		payBtn= (TextView) findViewById(R.id.payBtn);
		banner_title= (TextView) findViewById(R.id.banner_title);
		banner_title.setText(getResources().getString(R.string.banner_txt_pay));
		type = (RadioGroup) findViewById(R.id.type);
		
		payBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.payBtn:// 支付
			
			try {
				price=Double.parseDouble(moneyView.getText().toString());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				price=0.01;
			}
			
			// 当选择的是支付宝支付时
			if (type.getCheckedRadioButtonId() == R.id.alipay) {
				payByAli();
			} else if (type.getCheckedRadioButtonId() == R.id.wxpay) {
				// 调用插件用微信支付
				payByWeiXin();
			} else if (type.getCheckedRadioButtonId() == R.id.query) {
				// 选择查询时
//				query();
			}
			break;

		default:
			break;
		}

	}
	
	// 调用支付宝支付
	void payByAli() {
		
		BP.pay(this, contactView.getText().toString(), contentView.getText().toString(), price, true, new PListener() {

			// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {
				MyAPP.toast(getApplicationContext(), "支付结果未知,请稍后手动查询");
			}

			// 支付成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				MyAPP.toast(getApplicationContext(),  "支付成功!");
			}

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				// 此处应该保存订单号,比如保存进数据库等,以便以后查询
				MyAPP.toast(getApplicationContext(), "获取订单成功!请等待跳转到支付页面~");
			}

			// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
			@Override
			public void fail(int code, String reason) {
				MyAPP.toast(getApplicationContext(), "支付意外中断!");
			}
		});
	}

	// 调用微信支付
	void payByWeiXin() {

		BP.pay(this, contactView.getText().toString(), contentView.getText().toString(), price,false, new PListener() {

			// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {
				MyAPP.toast(getApplicationContext(), "支付结果未知,请稍后手动查询");
			}

			// 支付成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				MyAPP.toast(getApplicationContext(), "支付成功!");
			}

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				// 此处应该保存订单号,比如保存进数据库等,以便以后查询
				MyAPP.toast(getApplicationContext(), "获取订单成功!请等待跳转到支付页面~");
			}

			// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
			@Override
			public void fail(int code, String reason) {

				// 当code为-2,意味着用户中断了操作
				// code为-3意味着没有安装BmobPlugin插件
				if (code == -3) {
					new AlertDialog.Builder(getApplicationContext())
							.setMessage(
									"监测到你尚未安装支付插件,无法进行微信支付,请选择安装插件(已打包在本地,无流量消耗)还是用支付宝支付")
							.setPositiveButton("安装",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											installBmobPayPlugin("bp_wx.db");
										}
									})
							.setNegativeButton("支付宝支付",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											payByAli();
										}
									}).create().show();
				} else {
					Toast.makeText(getApplicationContext(), "支付中断!",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	void installBmobPayPlugin(String fileName) {
		try {
			InputStream is = getAssets().open(fileName);
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + fileName + ".apk");
			if (file.exists())
				file.delete();
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + file),
					"application/vnd.android.package-archive");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	// 执行订单查询
//	void query() {
//
//		BP.query(this, "", new QListener() {
//
//			@Override
//			public void succeed(String status) {
//				
//			}
//
//			@Override
//			public void fail(int code, String reason) {
//				
//			}
//		});
//	}
}
