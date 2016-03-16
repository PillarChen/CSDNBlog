package com.cd.csdnblog.activity;

import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.R;
import com.cd.csdnblog.utils.ShareUtil;
import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class DetailActivity extends Activity implements OnClickListener{
	 private final String mPageName = "DetailActivity:WebViewPage";
	private WebView webView;
	private String href="";
	private String title="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail);
		initView();
	}
	private void initView() {
		
		((TextView)findViewById(R.id.shareView)).setOnClickListener(this);
		
		webView=(WebView)findViewById(R.id.webView);
		WebSettings settings =webView.getSettings();
		settings.setTextZoom(120);
		
//		//支持javascript
//		settings.setJavaScriptEnabled(true); 
//		// 设置可以支持缩放 
//		settings.setSupportZoom(true); 
//		// 设置出现缩放工具 
//		settings.setBuiltInZoomControls(true);
//		//扩大比例的缩放
//		settings.setUseWideViewPort(true);
//		//自适应屏幕
//		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		settings.setLoadWithOverviewMode(true);

		
		href=getIntent().getStringExtra("href");
		title=getIntent().getStringExtra("title");
		webView.loadUrl(href);
		webView.setWebViewClient(new WebViewClient(){
	           @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            // TODO Auto-generated method stub
	               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
	             view.loadUrl(url);
	            return true;
	        }
	       });
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shareView:
			ShareUtil.shareOther(this, title, "用户"+MyAPP.username+"分享了博客", href);
		default:
			break;
		}
		
	}
}
