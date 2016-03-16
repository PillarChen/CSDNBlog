package com.cd.csdnblog;

import java.util.ArrayList;
import java.util.List;

import cn.domob.android.ads.InterstitialAd;
import cn.domob.android.ads.InterstitialAdListener;
import cn.domob.android.ads.AdManager.ErrorCode;

import com.cd.csdnblog.bean.Blog;
import com.cd.csdnblog.bean.Blog_Recommend;
import com.cd.csdnblog.utils.SharePreferencesUtil;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class MyAPP extends Application {

	/**
	 * SDK初始化建议放在启动页
	 */
	public static String APPID = "50c9706014c6f57506e7d80af2eccdd5";//外
	public static boolean isLogin;
	public static String userid = "";
	public static String username = "";
	public static String password = "";
	private static long mLastClickTime;//防止连续快速点击
	public static List<Blog> mMyBlogData=new ArrayList<Blog>();
	public static List<Blog_Recommend> mRecommendData=new ArrayList<Blog_Recommend>();
	public static List<String> blogUserNameList;
	
	//多盟广告
	public static final String PUBLISHER_ID = "56OJ2RSIuNx2NUJNLt";
//	public static final String InterstitialPPID = "16TLPK0oAp9yONUUc2Kq7m0k";
	public static final String InlinePPID = "16TLPK0oAp9yONUUc9hojv5z";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		initData();
	
	}

	private void initData() {
		isLogin= SharePreferencesUtil.getBooleanValue(this, "isLogin", false);
		if(MyAPP.isLogin){
			userid = SharePreferencesUtil.getStringValue(this, "userid", "");
			username = SharePreferencesUtil.getStringValue(this, "username", "");
			password = SharePreferencesUtil.getStringValue(this, "password", "");
		}
		Log.i("login", "--isLogin:"+isLogin+"--userid:"+userid+"--username："+username+"--password:"+password);
	}
	
	public static void toast(Context context,String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
		
	}
	
	/**
	 * 判断是否是连续快速点击
	 * @return
	 */
	public static boolean isFastClick() {
        // 当前时间
        long currentTime = System.currentTimeMillis();
        // 两次点击的时间差
        long time = currentTime - mLastClickTime;
        if ( 0 < time && time < 500) {   
            return true;   
        }   


        mLastClickTime = currentTime;   
        return false;   
    }
	
}
