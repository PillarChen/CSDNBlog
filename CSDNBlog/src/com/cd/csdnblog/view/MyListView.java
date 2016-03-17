package com.cd.csdnblog.view;

import com.cd.csdnblog.R;
import com.cd.csdnblog.callback.ILoading;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class MyListView extends ListView implements OnScrollListener{


	private static final String TAG = "MyListView";
	private Context mContext;
	private int totalItemCount;
	private int slideCount;
	private ILoading mloading;
	private boolean isLoading=false;
	private LinearLayout llFooter;
	public static TextView currentPageNumView;
	public static boolean isShowToast=false;
	public static int currentPageNum;
	public static int skipPageNum=0;
	public static int pages;
	public static boolean isSkipPageLoad=false;
	//精确定位、恢复位置
	private int mPositionTop;
	
	private TextView hintFooter;
	
	public int getmPositionTop() {
		return mPositionTop;
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}

	/**
	 * 初始化footerView
	 */
	private void initView() {
		View footerView = View.inflate(mContext, R.layout.layout_footer_view,null);
		llFooter = (LinearLayout) footerView.findViewById(R.id.ll_footer);
		hintFooter=(TextView)findViewById(R.id.hintFooter);
		this.setOnScrollListener(this);
		this.addFooterView(footerView);
		llFooter.setVisibility(View.GONE);
	}

	public void setOnLoadingInterFace(ILoading mloading) {
		this.mloading = mloading;
	}
	
	public void onLoadComplete(){
		isLoading=false;
		llFooter.setVisibility(View.GONE);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {

		slideCount = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;
		if(skipPageNum!=0){
			slideCount=skipPageNum*20;
			currentPageNum=(slideCount/20)==0 ? 1:(slideCount/20);
		}else{
			currentPageNum=(slideCount/20)==0 ? 1:(slideCount/20+1);
		}
		if(currentPageNumView!=null&&!isSkipPageLoad){
			currentPageNumView.setText(currentPageNum+"");
		}
		Log.i("listScroll", "--firstVisibleItem:"+firstVisibleItem+"--visibleItemCount："+visibleItemCount+"--totalItemCount:"+totalItemCount+"--slideCount:"+slideCount);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (slideCount == totalItemCount && scrollState == SCROLL_STATE_IDLE) {
			// 滑动到了底部，并且滑动停止
			if (mloading != null)
				if(!isLoading){
					if(currentPageNum<pages){
						llFooter.setVisibility(View.VISIBLE);
						isLoading=true;
						mloading.onLoad();
					}
				}else{
					
				}
		}
		if(skipPageNum!=0&& scrollState == SCROLL_STATE_IDLE){
			if (mloading != null)
				if(!isLoading){
					if(currentPageNum<pages){
						llFooter.setVisibility(View.VISIBLE);
						isLoading=true;
						mloading.onLoad();
					}
				}else{
					
				}
		}
		if(slideCount == totalItemCount || scrollState == SCROLL_STATE_IDLE){
			if(isShowToast){
				mloading.onHideToast();
			}
		}
		if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
			if(mloading != null){
				mloading.onHide();
			}
			if(isShowToast){
				mloading.onShowToast();
			}
		}
		
		// 不滚动时保存当前滚动到的位置  
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {   
            mPositionTop= view.getFirstVisiblePosition();
            mPositionTop=mPositionTop>0 ?(++mPositionTop):0;
        }  
	}
//	@Override
//	/**
//	 * 重写该方法，达到使ListView适应ScrollView的效果
//	 */
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);
//		super.onMeasure(widthMeasureSpec, expandSpec);
//	}
}
