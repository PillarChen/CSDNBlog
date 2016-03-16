package com.cd.csdnblog.utils;

import android.app.Activity;
import android.util.Log;

import com.cd.csdnblog.R;
import com.umeng.socialize.bean.LIKESTATUS;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ShareUtil {

    public static UMSocialService mController;

    private static void initShare(Activity activity) {
        com.umeng.socialize.utils.Log.LOG = true;
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, "1105220954", "ZFS2ciKDXi610vI9");
        qqSsoHandler.addToSocialSDK();
        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, "1105220954","ZFS2ciKDXi610vI9");
        qZoneSsoHandler.addToSocialSDK();
        
        //添加微信分享
  		UMWXHandler wxHandler=new UMWXHandler(activity, "wx84b668082dedf19d", "2c91a562eaa17fae5c332eca828d1834");
  		wxHandler.addToSocialSDK();
  		//添加微信朋友圈分享
  		UMWXHandler wxCircleHandler=new UMWXHandler(activity, "wx84b668082dedf19d", "2c91a562eaa17fae5c332eca828d1834");
  		wxCircleHandler.setToCircle(true);
  		wxCircleHandler.addToSocialSDK();
  		
  		//设置新浪SSO handler
  		mController.getConfig().setSsoHandler(new SinaSsoHandler());    
  		
  		//设置腾讯微博SSO handler
  		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        
        // 添加短信  
        SmsHandler smsHandler = new SmsHandler();  
        smsHandler.addToSocialSDK();  
        
        // 添加email
        EmailHandler emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();

    }

    /**
     * @param activity
     * @param description 描述信息
     * @param musicname 音频名称
     * @param url 音频URL
     */
    public static void shareMusic(Activity activity, String description, String musicname,
                                  String url) {
        initShare(activity);

        /**
         * 腾讯微博、新浪微博链接分享处理
         */
        mController.setShareContent(url);
        /**
         * 设置QQ分享内容使用下面的代码
         */
        QQShareContent qqShareContent = new QQShareContent();
        //设置分享文字
        qqShareContent.setShareContent(description);
        //设置分享title
        qqShareContent.setTitle(musicname);
        qqShareContent.setTargetUrl(url);
        //        //设置分享图片
        //        qqShareContent.setShareImage(new UMImage(activity, R.drawable.logo));
        //设置音频
        UMusic music = new UMusic(url);
        music.setTitle(musicname);
        music.setThumb(new UMImage(activity, R.drawable.logo));
        qqShareContent.setShareMedia(music);
        //设置点击分享内容的跳转链接
        mController.setShareMedia(qqShareContent);

        /**
         * 设置Qzone分享内容使用下面的代码
         */

        QZoneShareContent qzone = new QZoneShareContent();
        //设置分享文字
        qzone.setShareContent(description);
        //设置点击消息的跳转URL
        qzone.setTargetUrl(url);
        //设置分享内容的标题
        qzone.setTitle(musicname);
        //设置分享图片
        qzone.setShareImage(new UMImage(activity, R.drawable.logo));
        //设置音频
        //        UMusic music = new UMusic(url);
        //        music.setTitle("title");
        //        music.setThumb(new UMImage(activity, R.drawable.logo));
        qzone.setShareMedia(music);
        mController.setShareMedia(qzone);
        /**
         * 已默认样式打开分享面板
         */
        mController.openShare(activity, false);
    }
    /**
     * @param activity
     * @param description 描述信息
     * @param musicname 音频名称
     * @param url 音频URL
     */
    public static void shareMusic(Activity activity, String description, String musicname,
    		String url,String target) {
    	initShare(activity);
    	
    	/**
    	 * 腾讯微博、新浪微博链接分享处理
    	 */
    	mController.setShareContent(url);
    	/**
    	 * 设置QQ分享内容使用下面的代码
    	 */
    	QQShareContent qqShareContent = new QQShareContent();
    	//设置分享文字
    	qqShareContent.setShareContent(description);
    	//设置分享title
    	qqShareContent.setTitle(musicname);
    	qqShareContent.setTargetUrl(target);
    	//        //设置分享图片
    	//        qqShareContent.setShareImage(new UMImage(activity, R.drawable.logo));
    	//设置音频
    	UMusic music = new UMusic(url);
    	music.setTitle(musicname);
    	music.setThumb(new UMImage(activity, R.drawable.logo));
    	qqShareContent.setShareMedia(music);
    	//设置点击分享内容的跳转链接
    	mController.setShareMedia(qqShareContent);
    	
    	/**
    	 * 设置Qzone分享内容使用下面的代码
    	 */
    	
    	QZoneShareContent qzone = new QZoneShareContent();
    	//设置分享文字
    	qzone.setShareContent(description);
    	//设置点击消息的跳转URL
    	qzone.setTargetUrl(target);
    	//设置分享内容的标题
    	qzone.setTitle(musicname);
    	//设置分享图片
    	qzone.setShareImage(new UMImage(activity, R.drawable.logo));
    	//设置音频
    	//        UMusic music = new UMusic(url);
    	//        music.setTitle("title");
    	//        music.setThumb(new UMImage(activity, R.drawable.logo));
    	qzone.setShareMedia(music);
    	mController.setShareMedia(qzone);
    	/**
    	 * 已默认样式打开分享面板
    	 */
    	mController.openShare(activity, false);
    }

    /**
     * @param activity
     * @param description 描述信息
     * @param title 名称
     * @param url 分享URL
     */
    public static void shareOther(Activity activity, String description, String title, String url) {
        initShare(activity);

        /**
         * 腾讯微博、新浪微博链接分享处理
         */

        mController.setShareContent(description + url);

        /**
         * 设置QQ分享内容使用下面的代码
         */
        QQShareContent qqShareContent = new QQShareContent();
        //设置分享文字
        qqShareContent.setShareContent(description);
        //设置分享title
        qqShareContent.setTitle(title);
        //设置分享图片
        qqShareContent.setShareImage(new UMImage(activity, R.drawable.logo));
        //设置点击分享内容的跳转链接
        qqShareContent.setTargetUrl(url);
        mController.setShareMedia(qqShareContent);

        /**
         * 设置Qzone分享内容使用下面的代码
         */

        QZoneShareContent qzone = new QZoneShareContent();
        //设置分享文字
        qzone.setShareContent(description);
        //设置点击消息的跳转URL
        qzone.setTargetUrl(url);
        //设置分享内容的标题
        qzone.setTitle(title);
        //设置分享图片
        qzone.setShareImage(new UMImage(activity, R.drawable.logo));
        mController.setShareMedia(qzone);
        
        /**
         * 设置微信分享内容使用下面的代码
         */

        WeiXinShareContent wxShareContent=new WeiXinShareContent();
        wxShareContent.setShareContent(description);
        wxShareContent.setTitle(title);
        wxShareContent.setShareImage(new UMImage(activity, R.drawable.logo));
        wxShareContent.setTargetUrl(url);
        mController.setShareMedia(wxShareContent);
        
        /**
         * 设置微信朋友圈分享内容
         */
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(description);
        //设置朋友圈title
        circleMedia.setTitle(title);
        circleMedia.setShareImage(new UMImage(activity, R.drawable.logo));
        circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia);
        
        /**
         * 已默认样式打开分享面板
         */
        mController.openShare(activity, false);
    }
}
