package com.cd.csdnblog.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.R;
import com.cd.csdnblog.activity.MainActivity;
import com.cd.csdnblog.adapter.HomePageAdapter;
import com.cd.csdnblog.adapter.MyViewPagerAdapter;
import com.cd.csdnblog.base.BaseFragmentAttach;
import com.cd.csdnblog.bean.HomeItemBean;
import com.cd.csdnblog.constant.Url;
import com.cd.csdnblog.utils.DialogTool;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends BaseFragmentAttach implements OnClickListener {

    private static final String TAG = "DanceCompanyFragment";
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

	public HomeFragment() {
		super();
//		initData();
	}

	private void initData() {
		urlList.add(Url.URL_BLOB);
		urlList.add(Url.URL_BLOB1);
		urlList.add(Url.URL_BLOB2);
		urlList.add(Url.URL_BLOB3);
		urlList.add(Url.URL_BLOB4);
		urlList.add(Url.URL_BLOB5);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
			view = inflater.inflate(R.layout.act_dance_company, container, false);
			init();
		}
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
        titleView.setText(getResources().getString(R.string.txt_home));
        
        fragments = new ArrayList<Fragment>();
        for(int i=0;i<MyAPP.mRecommendData.size();i++){
        	noteListFragment=new NoteListFragment(MyAPP.mRecommendData.get(i).getBlogUserName());
        	fragments.add(noteListFragment);
//        	authorList.add(urlList.get(i));
        }
//        for(int i=0;i<urlList.size();i++){
//        	noteListFragment=new NoteListFragment(urlList.get(i));
//        	fragments.add(noteListFragment);
//        	authorList.add(urlList.get(i));
//        }
//        Log.i("authorList", authorList.toString());
        HomePageAdapter adapter = new HomePageAdapter(getChildFragmentManager(),fragments,MyAPP.mRecommendData);
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
    	MobclickAgent.onPageStart("HomeFragment"); //统计页面，"HomeFragment"为页面名称，可自定义
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	 MobclickAgent.onPageEnd("HomeFragment"); 

    }
}
