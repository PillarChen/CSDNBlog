package com.cd.csdnblog.activity;

import java.util.List;
import java.util.Map;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;
import cn.domob.android.ads.InterstitialAd;
import cn.domob.android.ads.InterstitialAdListener;
import cn.domob.android.ads.AdManager.ErrorCode;
import com.cd.csdnblog.R;
import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.bean.Blog;
import com.cd.csdnblog.bean.Blog_Recommend;
import com.cd.csdnblog.bean.MyUser;
import com.cd.csdnblog.fragment.HomeFragment;
import com.cd.csdnblog.fragment.MyBlogFragment;
import com.cd.csdnblog.utils.DialogTool;
import com.cd.csdnblog.utils.DrawerTag;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

public class MainActivity extends FragmentActivity {

	public static String TAG = "MainActivity";
	// 当前显示的Fragment
	private Fragment currentFragment;

	private DrawerLayout mDrawerLayout;
	private FragmentManager fm;

	private Fragment fragment;
	private HomeFragment homeFragment;
	private MyBlogFragment myBlogFragment;
	
	private Map<String, Object> map;
	private List<Map<String, Object>> list;
	public static boolean isRefreshMyBlogFrg=false;
	private FragmentTransaction transaction;
	private Context mContext;
	
	public static void refreshMyBlogFragment(){
		isRefreshMyBlogFrg=true;
	}
	
	private Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {
		switch (msg.what) {
		case 0:
			DialogTool.dismissDialog();
			fragment = new HomeFragment();
			currentFragment = fragment;
			fm.beginTransaction().replace(R.id.act_main_container, fragment).commit();
			break;
		default:
			break;
		}
	};
};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
		queryRecommend();
//		queryMyBlog(this,true);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("myBlog", "onRestart()");
//		queryMyBlog(this,true);
//		if(isRefreshMyBlogFrg&&currentFragment == myBlogFragment){
//			Log.i("myBlog", "isRefreshMyBlogFrg--"+isRefreshMyBlogFrg);
//			changeFragment(DrawerTag.MyBlog);
//		}
		if(isRefreshMyBlogFrg){
            isRefreshMyBlogFrg=false;
            queryMyBlog();
        }
	}
	private void init() {
		mContext = this;
		mDrawerLayout = (DrawerLayout) findViewById(R.id.act_main_drawerlayout);
		mDrawerLayout.setDrawerLockMode(1, 5);
		mDrawerLayout.setScrimColor(Color.parseColor("#60000000"));
		mDrawerLayout.setDrawerListener(mDrawerListener);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		fm = getSupportFragmentManager();
	}
	
	private void login() {
//		final BmobUser bUser = new BmobUser();
//		bUser.setUsername(MyAPP.username);
//		bUser.setPassword(MyAPP.password);
//		bUser.setObjectId(MyAPP.userid);
		queryMyBlog();
//		bUser.login(this, new SaveListener() {
//			
//			@Override
//			public void onSuccess() {
////				MyAPP.isLogin=true;
////				MainActivity.refreshMyBlogFragment();
////				SharePreferencesUtil.putStringValue(getApplicationContext(), "username", MyAPP.username);
////				SharePreferencesUtil.putStringValue(getApplicationContext(), "password", MyAPP.password);
////				SharePreferencesUtil.putBooleanValue(getApplicationContext(), "isLogin", MyAPP.isLogin);
//				queryMyBlog();
//			}
//			
//			@Override
//			public void onFailure(int code, String msg) {
//				MyAPP.toast(getApplicationContext(),"登陆失败:" + msg);
//				handler.sendEmptyMessage(0);
//			}
//		});
	}
	
	
	
	private void queryRecommend(){
		DialogTool.showDialog(this, "", "加载中......");
		final BmobQuery<Blog_Recommend> bmobQuery= new BmobQuery<Blog_Recommend>();
		bmobQuery.order("createdAt");
		//先判断是否有缓存
		boolean isCache = bmobQuery.hasCachedResult(this,Blog_Recommend.class);
		if(isCache){
			bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
		}else{
			bmobQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	// 如果没有缓存的话，则先从网络中取
		}
		bmobQuery.findObjects(this, new FindListener<Blog_Recommend>() {

			@Override
			public void onSuccess(List<Blog_Recommend> object) {
				// TODO Auto-generated method stub
//				MyAPP.toast(getApplicationContext(),"查询成功：共"+object.size()+"条数据。");
				MyAPP.mRecommendData=object;
//				for (Blog_Recommend blogRecommend : object) {
//					Log.d(TAG, "~~~~~~~~~~~~~queryRecommend~~~~~~~~~~~~~~~ ");
//					Log.d(TAG, "ObjectId = "+blogRecommend.getObjectId());
//					Log.d(TAG, "Name = "+blogRecommend.getBlogUserName());
//					Log.d(TAG, "CreatedAt = "+blogRecommend.getCreatedAt());
//					Log.d(TAG, "UpdatedAt = "+blogRecommend.getUpdatedAt());
//				}
				if(MyAPP.isLogin){
					queryMyBlog();
//					login();
				}else{
					handler.sendEmptyMessage(0);
				}
			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				MyAPP.toast(getApplicationContext(),"查询失败："+msg);
				handler.sendEmptyMessage(0);
			}
		});
	}
//	/**
//	 *  查询我的博客
//	 */
//	public static void queryMyBlog(final Context mContext,boolean isRePost){
//		MyUser user=BmobUser.getCurrentUser(mContext, MyUser.class);
//		if(user==null){
//			return;
//		}
//		final BmobQuery<Blog> bmobQuery= new BmobQuery<Blog>();
//		bmobQuery.order("createdAt");
//		
//		bmobQuery.addWhereEqualTo("author", user);
////		if(!isRePost){
////			//先判断是否有缓存
////			boolean isCacheMyBlog = bmobQuery.hasCachedResult(mContext,Blog.class);
////			if(isCacheMyBlog){
////				bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
////			}else{
////				bmobQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	// 如果没有缓存的话，则先从网络中取
////			}
////		}
//		bmobQuery.findObjects(mContext, new FindListener<Blog>() {
//			
//			@Override
//			public void onSuccess(List<Blog> object) {
//				// TODO Auto-generated method stub
////				MyAPP.toast(mContext.getApplicationContext(),"查询成功：共"+object.size()+"条数据。");
//				MyAPP.mMyBlogData=object;
////				for (Blog blog : object) {
////					Log.d(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
////					Log.d(TAG, "ObjectId = "+blog.getObjectId());
////					Log.d(TAG, "Name = "+blog.getBlogUserName());
////					Log.d(TAG, "Author = "+blog.getAuthor());
////					Log.d(TAG, "CreatedAt = "+blog.getCreatedAt());
////					Log.d(TAG, "UpdatedAt = "+blog.getUpdatedAt());
////				}
//			}
//			
//			@Override
//			public void onError(int code, String msg) {
//				// TODO Auto-generated method stub
//				MyAPP.toast(mContext.getApplicationContext(),"查询失败："+msg);
//			}
//		});
//	}
	
	/**
	 *  查询我的博客
	 */
	private void queryMyBlog(){
		MyUser user=BmobUser.getCurrentUser(mContext, MyUser.class);
		if(user==null){
			handler.sendEmptyMessage(0);
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
				handler.sendEmptyMessage(0);
			}
		});
	}
	
//	/**
//	 * 查询我的博客
//	 */
//	private void searchBlog(Context mContext) {
//		// 查找Person表里面id为6b6c11c537的数据
//		BmobQuery<Blog> bmobQuery = new BmobQuery<Blog>();
//		bmobQuery.order("createdAt");
//		MyUser user=BmobUser.getCurrentUser(mContext, MyUser.class);
//		if(user==null){
//			DialogTool.dismissDialog();
//			return;
//		}
//		bmobQuery.addWhereEqualTo("author", user);
//		
//		bmobQuery.findObjects(mContext, new FindListener<Blog>() {
//
//			@Override
//			public void onSuccess(List<Blog> blogList) {
//				Log.i("blogBean", blogList.toString());
//				MyAPP.mMyBlogData=blogList;
//			}
//
//			@Override
//			public void onError(int code, String msg) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}
	
	private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {

		@Override
		public void onDrawerStateChanged(int newState) {
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			View localView = mDrawerLayout.getChildAt(0);
			float f1 = 1.0F - slideOffset;
			ViewHelper.setAlpha(drawerView, 0.6F + 0.4F * (1.0F - f1));
			ViewHelper.setTranslationX(localView,-drawerView.getMeasuredWidth() * (1.0F - f1));
			ViewHelper.setPivotX(localView, 0.0F);
			ViewHelper.setPivotY(localView, localView.getMeasuredHeight() / 2);
		}

		@Override
		public void onDrawerOpened(View drawerView) {
		}

		@Override
		public void onDrawerClosed(View drawerView) {
		}
	};
	
	public void toggleDrawer() {
		if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
			mDrawerLayout.closeDrawer(Gravity.RIGHT);
		} else {
			mDrawerLayout.openDrawer(Gravity.RIGHT);
		}
	}
	
	public void switchFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction().replace(R.id.act_main_container, fragment).commit();
		currentFragment = fragment;
	}

	
	public void changeFragment(DrawerTag tag) {
		transaction = fm.beginTransaction();
		switch (tag) {
		case Account:
//			startActivity(new Intent(this, AccountActivity.class));
			break;

		case Home:
			toggleDrawer();
			if (homeFragment == null) {
				homeFragment = new HomeFragment();
				transaction.add(R.id.act_main_container,homeFragment);
			}
			transaction.hide(currentFragment).show(homeFragment);
			currentFragment = homeFragment;
			break;
		case MyBlog:
			toggleDrawer();
			if (myBlogFragment == null||isRefreshMyBlogFrg) {
				isRefreshMyBlogFrg=false;
				myBlogFragment= new MyBlogFragment();
				transaction.add(R.id.act_main_container, myBlogFragment);
			}
			transaction.hide(currentFragment).show(myBlogFragment);
			currentFragment = myBlogFragment;
			break;

		case Update:

//			startActivity(new Intent(this, CollectionActivity.class));
			break;
		case BlogManager:
			startActivity(new Intent(this, ManagerActivity.class));

			break;
		case ClearCache:

//			// 弹出更新选择
//			MyDialog.Builder builder = new MyDialog.Builder(this);
//			builder.setTitle("清除缓存");
//			builder.setMessage("确定清除缓存吗?");
//			builder.setPositiveButton("确定",
//					new android.content.DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							handler.sendEmptyMessage(ClearCache_OK);
//						}
//					});
//			builder.setNegativeButton("取消",
//					new android.content.DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//						}
//					});
//			builder.create().show();

			break;
		case About:

			startActivity(new Intent(this, AboutActivity.class));
			break;
		case Feedback:
			startActivity(new Intent(this, FeedBackActivity.class));
			break;
		case Login:
//			startActivity(new Intent(this, LoginActivity.class));
			break;
		default:
			break;

		}

		transaction.commit();
		if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
			mDrawerLayout.closeDrawer(Gravity.RIGHT);
		}
	}
	
	private static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mExitFlag = false;
		};
	};
	
	// 退出程序辅助标记
	private static boolean mExitFlag;

	@Override
	public void onBackPressed() {
		if (mExitFlag) {
			this.finish();
		} else {
			mExitFlag = true;
			MyAPP.toast(getApplicationContext(),"再按一次退出程序~");
			mHandler.sendEmptyMessageDelayed(0, 2000);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 MobclickAgent.onResume(this);       //统计时长
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 MobclickAgent.onPause(this);
	}
}
