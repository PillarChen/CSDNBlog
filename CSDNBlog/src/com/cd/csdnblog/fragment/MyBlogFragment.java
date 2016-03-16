package com.cd.csdnblog.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;

import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.R;
import com.cd.csdnblog.activity.MainActivity;
import com.cd.csdnblog.adapter.MyViewPagerAdapter;
import com.cd.csdnblog.base.BaseFragmentAttach;
import com.cd.csdnblog.bean.Blog;
import com.cd.csdnblog.bean.HomeItemBean;
import com.cd.csdnblog.bean.MyUser;
import com.cd.csdnblog.constant.Url;
import com.cd.csdnblog.utils.DialogTool;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

public class MyBlogFragment extends BaseFragmentAttach implements OnClickListener {

    private static final String TAG = "MyBlogFragment";
    public static final int REQUEST_ITEM = 8;
    
    private ViewPager           mViewPager;
    private TabPageIndicator    mIndicator;
    private View                view;
    private View                titleMore;
	protected MainActivity mActivity;
	private List<Fragment> fragments;
	private NoteListFragment noteListFragment;
	private List<String> urlList=new ArrayList<String>();
	private List<String>    authorList=new ArrayList<String>();
	private TextView titleView;
	 private MyViewPagerAdapter adapter;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				DialogTool.dismissDialog();
				
				init();
				ViewGroup parent = (ViewGroup) view.getParent();
				if (parent != null) {
					parent.removeView(view);
				}
				
				break;

			default:
				break;
			}
		};
	};
	
	public MyBlogFragment() {
		super();
		Log.d(TAG, "--MyBlogFragment()--");
		initData();
	}

	private void initData() {
//		urlList.add(Url.URL_BLOB);
//		urlList.add(Url.URL_BLOB1);
//		urlList.add(Url.URL_BLOB2);
//		urlList.add(Url.URL_BLOB3);
//		urlList.add(Url.URL_BLOB4);
//		urlList.add(Url.URL_BLOB5);
		if(MyAPP.mMyBlogData==null){
//			urlList.removeAll(urlList);
//			urlList.clear();
//			for(int i=0;i<MyAPP.mMyBlogData.size();i++){
//				urlList.add("/"+MyAPP.mMyBlogData.get(i).getBlogUserName());
//			}
			queryMyBlog();
		}
	}
	
	/**
	 *  查询我的博客
	 */
	private void queryMyBlog(){
		MyUser user=BmobUser.getCurrentUser(mActivity, MyUser.class);
		if(user==null){
			return;
		}
		final BmobQuery<Blog> bmobQuery= new BmobQuery<Blog>();
		bmobQuery.order("createdAt");
		
		bmobQuery.addWhereEqualTo("author", user);
		
		//先判断是否有缓存
		boolean isCacheMyBlog = bmobQuery.hasCachedResult(mActivity,Blog.class);
		if(isCacheMyBlog){
			bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
		}else{
			bmobQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	// 如果没有缓存的话，则先从网络中取
		}
		bmobQuery.findObjects(mActivity, new FindListener<Blog>() {
			
			@Override
			public void onSuccess(List<Blog> object) {
				// TODO Auto-generated method stub
				MyAPP.toast(mActivity,"查询成功：共"+object.size()+"条数据。");
				MyAPP.mMyBlogData=object;
				for (Blog blog : object) {
					Log.d(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
					Log.d(TAG, "ObjectId = "+blog.getObjectId());
					Log.d(TAG, "Name = "+blog.getBlogUserName());
					Log.d(TAG, "Author = "+blog.getAuthor());
					Log.d(TAG, "CreatedAt = "+blog.getCreatedAt());
					Log.d(TAG, "UpdatedAt = "+blog.getUpdatedAt());
				}
				adapter.notifyDataSetChanged();
				view.invalidate();
			}
			
			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				MyAPP.toast(mActivity,"查询失败："+msg);
			}
		});
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "--onCreateView()--");
        if (view == null) {
			view = inflater.inflate(R.layout.act_dance_company, container, false);
		}
//        DialogTool.showDialog(mActivity, "", "加载中......");
//        handler.sendEmptyMessage(0);
        init();
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
        return view;
    }
    private void init() {
        titleMore = view.findViewById(R.id.title_more);
        titleMore.setOnClickListener(this);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mIndicator = (TabPageIndicator) view.findViewById(R.id.TabPageindicator);       
        titleView=(TextView)view.findViewById(R.id.titleView);
        titleView.setText(getResources().getString(R.string.txt_myBlog));
    
        fragments = new ArrayList<Fragment>();
        Log.i("myBlog", "--MyAPP.mMyBlogData--"+MyAPP.mMyBlogData);
        for(int i=0;MyAPP.mMyBlogData!=null&&i<MyAPP.mMyBlogData.size();i++){
        	noteListFragment=new NoteListFragment(MyAPP.mMyBlogData.get(i).getBlogUserName());
        	fragments.add(noteListFragment);
//        	authorList.add(urlList.get(i));
        }
//        for(int i=0;i<urlList.size();i++){
//        	noteListFragment=new NoteListFragment(urlList.get(i));
//        	fragments.add(noteListFragment);
//        	authorList.add(urlList.get(i));
//        }
//        Log.i("authorList", authorList.toString());
        adapter = new MyViewPagerAdapter(getChildFragmentManager(),fragments,MyAPP.mMyBlogData);
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setCurrentItem(0);
     // 如果我们要对ViewPager设置监听，用indicator设置就行了
        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
	
			@Override
			public void onPageSelected(int arg0) {
	//     				Toast.makeText(getApplicationContext(), TITLE[arg0],
	//     						Toast.LENGTH_SHORT).show();
			}
	
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
	
			}
	
			@Override
			public void onPageScrollStateChanged(int arg0) {
	
			}
	});
    }

    @Override
    public void onClick(View v) {
    	if(MyAPP.isFastClick()){
			return;
		}
        switch (v.getId()) {
            case R.id.title_more:
                ((MainActivity) getActivity()).toggleDrawer();
                break;

            default:
                break;
        }
    }
    @Override
    public void onResume() {
    	super.onResume();
    	MobclickAgent.onPageStart("MyBlogFragment"); //统计页面，"MyBlogFragment"为页面名称，可自定义
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	 MobclickAgent.onPageEnd("MyBlogFragment"); 
    }
}
