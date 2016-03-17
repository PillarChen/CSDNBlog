package com.cd.csdnblog.activity;

import java.util.ArrayList;
import java.util.List;
import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.R;
import com.cd.csdnblog.adapter.BlobUserNameAdapter;
import com.cd.csdnblog.base.BaseActivity;
import com.cd.csdnblog.bean.Blog;
import com.cd.csdnblog.bean.MyUser;
import com.cd.csdnblog.fragment.HomeFragment;
import com.cd.csdnblog.utils.DialogTool;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.domob.android.ads.AdEventListener;
import cn.domob.android.ads.AdView;
import cn.domob.android.ads.AdManager.ErrorCode;

public class ManagerActivity extends BaseActivity implements OnClickListener{
	private AlertDialog dialog;
	private EditText blogUserView;
	private LinearLayout addBlogLL;
	private LinearLayout cancleView;
	private LinearLayout okView;
	private TextView selectAllView;
	private TextView addUserView;
	private TextView delUserView;
	private ListView blobUserName_lv;
	private BlobUserNameAdapter adapter;
	public static List<String> delList=new ArrayList<String>();
	private List<BmobObject> delBlogList=new ArrayList<BmobObject>();
	private String objectId;
	
	private Context mContext;
	private final String mPageName = "ManagerActivity";

	RelativeLayout mAdContainer;
	AdView mAdview;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				DialogTool.dismissDialog();
				adapter=new BlobUserNameAdapter(MyAPP.mMyBlogData, mContext);
				blobUserName_lv.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager);
//		MainActivity.searchBlog(this);
		
		//友盟统计
		mContext = this;
        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
        // MobclickAgent.setSessionContinueMillis(1000);
		
		initView();
		queryMyBlog();
		
	}
	
	/**
	 *  查询我的博客
	 */
	private void queryMyBlog(){
		DialogTool.showDialog(mContext, "", "加载中......");
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

	private void initView() {
		selectAllView=(TextView)findViewById(R.id.selectAllView);
		addUserView=(TextView)findViewById(R.id.addUserView);
		delUserView=(TextView)findViewById(R.id.delUserView);
		addBlogLL=(LinearLayout)findViewById(R.id.addBlogLL);
		cancleView = (LinearLayout)findViewById(R.id.cancleView);
		okView = (LinearLayout)findViewById(R.id.okView);
		blogUserView = (EditText)findViewById(R.id.blogUserView);
		blobUserName_lv=(ListView)findViewById(R.id.blobUserName_lv);
//		adapter=new BlobUserNameAdapter(MyAPP.mMyBlogData, this);
//		blobUserName_lv.setAdapter(adapter);
		
		selectAllView.setOnClickListener(this);
		addUserView.setOnClickListener(this);
		delUserView.setOnClickListener(this);
		cancleView.setOnClickListener(this);
		okView.setOnClickListener(this);
		
		try {
			doMobAdver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doMobAdver() {
		mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
		mAdview = new AdView(this, MyAPP.PUBLISHER_ID, MyAPP.InlinePPID);
		mAdview.setKeyword("game");
		mAdview.setUserGender("male");
		mAdview.setUserBirthdayStr("2016-03-14");
		mAdview.setUserPostcode("123456");
		mAdview.setAdEventListener(new AdEventListener() {
			@Override
			public void onAdOverlayPresented(AdView adView) {
				Log.i("DomobSDKDemo", "overlayPresented");
			}
			@Override
			public void onAdOverlayDismissed(AdView adView) {
				Log.i("DomobSDKDemo", "Overrided be dismissed");
			}
			@Override
			public void onAdClicked(AdView arg0) {
				Log.i("DomobSDKDemo", "onDomobAdClicked");
			}
			@Override
			public void onLeaveApplication(AdView arg0) {
				Log.i("DomobSDKDemo", "onDomobLeaveApplication");
			}
			@Override
			public Context onAdRequiresCurrentContext() {
				return mContext;
			}
			@Override
			public void onAdFailed(AdView arg0, ErrorCode arg1) {
				Log.i("DomobSDKDemo", "onDomobAdFailed");
			}
			@Override
			public void onEventAdReturned(AdView arg0) {
				Log.i("DomobSDKDemo", "onDomobAdReturned");
			}
		});
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mAdview.setLayoutParams(layout);
		mAdContainer.addView(mAdview);
	}
	
	/**
	 * 添加博客用户
	 */
	private void addblog() {

		AlertDialog.Builder builder = new Builder(this);
		dialog = builder.create();
//		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.dialog_add_bloguser,null);
		blogUserView = (EditText) view.findViewById(R.id.blogUserView);
		blogUserView.setText("");
		
		blogUserView.setClickable(true);
		blogUserView.setFocusable(true);
		blogUserView.setFocusableInTouchMode(true);
		blogUserView.requestFocus();
		
		cancleView = (LinearLayout) view.findViewById(R.id.cancleView);
		okView = (LinearLayout) view.findViewById(R.id.okView);
		
		dialog.show();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = getWindowManager().getDefaultDisplay().getWidth();
		
		dialogWindow.setAttributes(lp);
		dialog.setContentView(view);
		
		okView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//添加博客用户方法
				
				
				dialog.cancel();
			}
		});
		cancleView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		blogUserView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				showSoftInput(blogUserView);
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectAllView:
			
			break;
		case R.id.addUserView:
//			addblog();
			addBlogLL.setVisibility(View.VISIBLE);
			blogUserView.setText("");
			blogUserView.setClickable(true);
			blogUserView.setFocusable(true);
			blogUserView.setFocusableInTouchMode(true);
			blogUserView.requestFocus();
			showSoftInput(blogUserView);
			break;
		case R.id.delUserView:
			
			delBlogUser();
			break;
		case R.id.cancleView:
			addBlogLL.setVisibility(View.GONE);
			hideSoftInput(blogUserView);
			delList.clear();
			break;
		case R.id.okView:
			addBlogLL.setVisibility(View.GONE);
			hideSoftInput(blogUserView);
			//添加博客
			addBlogUser();
			break;

		default:
			break;
		}
		
	}

	/**
	 * 删除博客
	 */
	private void delBlogUser() {
		final List<Blog> myBlogTemp=MyAPP.mMyBlogData;
		delBlogList.clear();
		for(int i=0;i<MyAPP.mMyBlogData.size();i++){
			
			if(delList.contains(MyAPP.mMyBlogData.get(i).getObjectId())){
				final Blog blog=new Blog();
				Log.i("del",MyAPP.mMyBlogData.get(i).getBlogUserName());
				blog.setObjectId(MyAPP.mMyBlogData.get(i).getObjectId());
				delBlogList.add(blog);
				myBlogTemp.remove(i);
//				MyAPP.mMyBlogData.remove(i);
			}
		}
		new BmobObject().deleteBatch(this, delBlogList, new DeleteListener() {
			
			@Override
			public void onSuccess() {
				MyAPP.toast(getApplicationContext(),"删除成功！");
				MainActivity.refreshMyBlogFragment();
				Log.i("onSuccess", "--delBlogUser()1--"+MyAPP.mMyBlogData.toString());
				MyAPP.mMyBlogData=myBlogTemp;
				Log.i("onSuccess", "--delBlogUser()2--"+MyAPP.mMyBlogData.toString());
//				adapter=new BlobUserNameAdapter(myBlogTemp, getApplicationContext());
				adapter=new BlobUserNameAdapter(MyAPP.mMyBlogData, getApplicationContext());
				blobUserName_lv.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				MainActivity.isRefreshMyBlogFrg=true;
				delList.clear();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				MyAPP.toast(getApplicationContext(),"删除失败！");
				
			}
		});
	}

	/**
	 * 添加博客
	 */
	private void addBlogUser() {
		if(TextUtils.isEmpty(blogUserView.getText().toString())){
			MyAPP.toast(getApplicationContext(),"添加内容为空!");
			return;
		}
		MyUser user=BmobUser.getCurrentUser(this, MyUser.class);
		final Blog blogBean=new Blog();
		blogBean.setBlogUserName(blogUserView.getText().toString());
		blogBean.setAuthor(user);
		blogBean.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				MyAPP.toast(getApplicationContext(),"添加博客成功!");
//				MainActivity.refreshMyBlogFragment();
				Log.i("onSuccess", "--addBlogUser()1--"+MyAPP.mMyBlogData.toString());
				if(!MyAPP.mMyBlogData.contains(blogBean)){
					MyAPP.mMyBlogData.add(blogBean);
				}else{
					MyAPP.toast(getApplicationContext(),"已存在!");
				}
				Log.i("onSuccess", "--addBlogUser()2--"+MyAPP.mMyBlogData.toString());
				adapter=new BlobUserNameAdapter(MyAPP.mMyBlogData, getApplicationContext());
				blobUserName_lv.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				MainActivity.isRefreshMyBlogFrg=true;
//				MainActivity.queryMyBlog(getApplicationContext(),true);
			}
			
			@Override
			public void onFailure(int code, String msg) {
				MyAPP.toast(getApplicationContext(),"创建数据失败：" + msg);
				
			}
		});
	}
	
	/**
	 * 隐藏软键盘输入法
	 * 
	 * @param v
	 */
	private void hideSoftInput(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/**
	 * 显示软键盘输入法
	 * 
	 * @param v
	 */
	private void showSoftInput(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
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
