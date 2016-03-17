package com.cd.csdnblog.fragment;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.cd.csdnblog.MyAPP;
import com.cd.csdnblog.R;
import com.cd.csdnblog.activity.DetailActivity;
import com.cd.csdnblog.adapter.NoteListAdapter;
import com.cd.csdnblog.base.BaseFragmentAttach;
import com.cd.csdnblog.bean.BlogItem;
import com.cd.csdnblog.bean.Page;
import com.cd.csdnblog.callback.ILoading;
import com.cd.csdnblog.constant.Constants;
import com.cd.csdnblog.constant.Url;
import com.cd.csdnblog.utils.HttpUtil;
import com.cd.csdnblog.utils.JsoupUtil;
import com.cd.csdnblog.view.MyListView;
import com.umeng.analytics.MobclickAgent;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class NoteListFragment extends BaseFragmentAttach implements OnItemClickListener,ILoading{
	private MyListView titleListView;
	private List<Map<String,String>> titleList=new ArrayList<Map<String,String>>();
	private Map<String,String> listMap;
	private String url;
	private NoteListAdapter adapter;
	private int blogType = 0; // 博客类别
	private Page page; // 页面引用
	private int pages;//页数
	
	private SwipeRefreshLayout swipeRl;
	
	private List<BlogItem> mData=new ArrayList<BlogItem>();
	
	public NoteListFragment(String blogUserName) {
		super();
		this.url = Url.BASEURL+"/"+blogUserName;
	}
	

	public void setBlogType(int blogType) {
		this.blogType = blogType;
	}



//	Handler handler=new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			adapter.notifyDataSetChanged();
//		};
//	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_notelist, container, false);
		
		initView(view);
		
		initData();
		
		return view;
	}


	private void initView(View view) {
		titleListView=(MyListView)view.findViewById(R.id.titleListView);
		initSwipeRefreshLayout(view);
	}

	/**
	 * 初始化SwipeRefreshLayout
	 */
	private void initSwipeRefreshLayout(View view) {
		swipeRl = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
		swipeRl.setColorSchemeResources(android.R.color.holo_blue_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light,
				android.R.color.holo_red_light);
		swipeRl.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
//				swipeRl.setRefreshing(false);
				page.setPage(1);
				new MainTask().execute(Url.getBlogListURL(blogType, page.getCurrentPage(),url),"refresh");
//				clickCancleSearchTool();
//				if(!isSkipPage){
//					MyListView.skipPageNum=0;
//					page = 1;
//					currentPageNumView_noteall.setText(""+page);
//				}else{
//					if(page>1){
//						Log.i("boardPage", "--page--"+page);
//						postParentBoard(--page, false);
//						MyListView.isShowToast=false;//设置关闭页码Toast
//						if(page==0){
//							MyListView.isSkipPageLoad=false;
//						}else{
//							MyListView.isSkipPageLoad=true;
//						}
//					}
//				}
			}
		});
	}

	private void initData() {
		page = new Page();
//		page.setPageStart();
		adapter=new NoteListAdapter(mData,mActivity);
		titleListView.setAdapter(adapter);
		
		titleListView.setOnItemClickListener(this);
		titleListView.setOnLoadingInterFace(this);
		
//		Thread thread=new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				
//				  getHtmlInfo(url);  
//				
//			}
//		});
//		thread.start();
		Log.i("page", "refresh--page--"+page.getCurrentPage()+"--url:"+Url.getBlogListURL(blogType, page.getCurrentPage(),url));
		new MainTask().execute(Url.getBlogListURL(blogType, page.getCurrentPage(),url),"refresh");
	}
	
//	/**
//	 * 获取html中信息方法1
//	 */
//	private void getHtmlInfo(String url) {
//		String htmlString = getHtmlString(url);  
//	    Document document = Jsoup.parse(htmlString);  
//		
////		Element content = document.getElementById("body"); 
//		Elements links = document.getElementsByTag("a"); 
//		String href="";
//		for (Element link : links) { 
//			href=link.attr("href");
//			if(!href.contains("?id=")){
//				continue;
//			}else{
//				href=href.replace("?id=", "/");
//			}
//			listMap=new HashMap<String, String>();
//			listMap.put("title", link.text());
//			listMap.put("href", href);
//			titleList.add(listMap);
//			
//		  Log.i("link", "--linkText--"+link.text());
//		  Log.i("link", "--href--"+href);
//		} 
//	    
//	   
//	    handler.sendEmptyMessage(0);
//	}
//	
//	public String getHtmlString(String urlString) {  
//        try {  
//            URL url = new URL(urlString);  
//            URLConnection ucon = url.openConnection();  
//            InputStream instr = ucon.getInputStream();  
//            BufferedInputStream bis = new BufferedInputStream(instr);  
//            ByteArrayBuffer baf = new ByteArrayBuffer(500);  
//            int current = 0;  
//            while ((current = bis.read()) != -1) {  
//                baf.append((byte) current);  
//            }  
//            return EncodingUtils.getString(baf.toByteArray(), "utf-8");  
//        } catch (Exception e) {  
//            return "";  
//        }  
//    }

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(MyAPP.isFastClick()){
			return;
		}
		try {
//			String clickUrl="";
//			clickUrl=Url.BASEURL+Url.URL_BLOB+adapter.getList().get(position).getLink();
			Intent intent=new Intent(mActivity,DetailActivity.class);
			intent.putExtra("href", adapter.getList().get(position).getLink());
			intent.putExtra("title",adapter.getList().get(position).getTitle());
			startActivity(intent);
//			Log.i("url", clickUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
    public void onResume() {
    	super.onResume();
    	MobclickAgent.onPageStart("NoteListFragment"); //统计页面，"MyBlogFragment"为页面名称，可自定义
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	 MobclickAgent.onPageEnd("NoteListFragment"); 
    }

	@Override
	public void onLoad() {
		Log.i("NoteList", "--onLoad()-");
//		page.addPage();
		Log.i("page", "onLoad()--page--"+page.getCurrentPage()+"--url:"+Url.getBlogListURL(blogType, page.getCurrentPage(),url));
//		if(Integer.parseInt(page.getCurrentPage())<pages){		
//			new MainTask().execute(Url.getBlogListURL(blogType, page.getCurrentPage(),url),"load");
//		}
		new MainTask().execute(Url.getBlogListURL(blogType, page.getCurrentPage(),url),"load");
	}

	@Override
	public void onHide() {
		Log.i("NoteList", "--onHide()-");
	}

	@Override
	public void onShowToast() {
		Log.i("NoteList", "--onShowToast()-");
		
	}

	@Override
	public void onHideToast() {
		Log.i("NoteList", "--onHideToast()-");
		
	}
	
	private class MainTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			// 获取网页html数据
			String temp = HttpUtil.httpGet(params[0]);
			if (temp == null) {
				return Constants.DEF_RESULT_CODE.ERROR;
			}
			// 解析html页面获取列表
			List<BlogItem> list = JsoupUtil.getBlogItemList(blogType, temp);
			Log.i("url", "--list--"+list.size());
//			if (list.size() == 0) {
//				return Constants.DEF_RESULT_CODE.NO_DATA;
//			}
			pages=list.get(0).getPages();
			titleListView.pages=pages;
			// 刷新动作
			if (params[1].equals("refresh")) {
				adapter.setList(list);
				return Constants.DEF_RESULT_CODE.REFRESH;
			} else {// 加载更多
//				adapter.setList(list);
				adapter.addList(list);
				return Constants.DEF_RESULT_CODE.LOAD;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// 通知列表数据更新
			adapter.notifyDataSetChanged();
			switch (result) {
			case Constants.DEF_RESULT_CODE.ERROR: // 错误
				Toast.makeText(getActivity(), "网络信号不佳", Toast.LENGTH_LONG);
//				blogListView.stopRefresh(getDate());
//				blogListView.stopLoadMore();
				break;
			case Constants.DEF_RESULT_CODE.NO_DATA: // 无数据
				Toast.makeText(getActivity(), "无更多加载内容", Toast.LENGTH_LONG).show();
				titleListView.onLoadComplete();
//				blogListView.stopLoadMore();
				// noBlogView.setVisibility(View.VISIBLE); // 显示无博客
				break;
			case Constants.DEF_RESULT_CODE.REFRESH: // 刷新
				swipeRl.setRefreshing(false);
//				blogListView.stopRefresh(getDate());
//				
//				db.delete(blogType);
//				db.insert(adapter.getList());// 保存到数据库
//				if (adapter.getCount() == 0) {
//					noBlogView.setVisibility(View.VISIBLE); // 显示无博客
//				}
				break;
			case Constants.DEF_RESULT_CODE.LOAD:
				titleListView.onLoadComplete();
//				blogListView.stopLoadMore();
				if(Integer.parseInt(page.getCurrentPage())<pages){
					page.addPage();
				}else{
//					Toast.makeText(getActivity(), "无更多加载内容", Toast.LENGTH_LONG).show();
					titleListView.onLoadComplete();
				}
//				if (adapter.getCount() == 0) {
//					noBlogView.setVisibility(View.VISIBLE); // 显示无博客
//				}
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}

	}
}
