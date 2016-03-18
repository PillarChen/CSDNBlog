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
import com.cd.csdnblog.constant.Url;
import com.cd.csdnblog.utils.DialogTool;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class NoteListFragment extends BaseFragmentAttach implements OnItemClickListener {
	private ListView titleListView;
	private List<Map<String,String>> titleList=new ArrayList<Map<String,String>>();
	private Map<String,String> listMap;
	private String url;
	private NoteListAdapter adapter;
	public NoteListFragment(String blogUserName) {
		super();
		this.url = Url.BASEURL+"/"+blogUserName;
	}


	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			adapter.notifyDataSetChanged();
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_notelist, container, false);
		
		titleListView=(ListView)view.findViewById(R.id.titleListView);
		
		adapter=new NoteListAdapter(titleList,mActivity);
		titleListView.setAdapter(adapter);
		
		titleListView.setOnItemClickListener(this);
		
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				  getHtmlInfo(url);  
				
			}
		});
		thread.start();
		
		return view;
	}
	
	/**
	 * 获取html中信息方法1
	 */
	private void getHtmlInfo(String url) {
		String htmlString = getHtmlString(url);  
	    Document document = Jsoup.parse(htmlString);  
		
//		Element content = document.getElementById("body"); 
		Elements links = document.getElementsByTag("a"); 
		String href="";
		for (Element link : links) { 
			href=link.attr("href");
			if(!href.contains("?id=")){
				continue;
			}else{
				href=href.replace("?id=", "/");
			}
			listMap=new HashMap<String, String>();
			listMap.put("title", link.text());
			listMap.put("href", href);
			titleList.add(listMap);
			
		  Log.i("link", "--linkText--"+link.text());
		  Log.i("link", "--href--"+href);
		} 
	    
	   
	    handler.sendEmptyMessage(0);
	}
	
	public String getHtmlString(String urlString) {  
        try {  
            URL url = new URL(urlString);  
            URLConnection ucon = url.openConnection();  
            InputStream instr = ucon.getInputStream();  
            BufferedInputStream bis = new BufferedInputStream(instr);  
            ByteArrayBuffer baf = new ByteArrayBuffer(500);  
            int current = 0;  
            while ((current = bis.read()) != -1) {  
                baf.append((byte) current);  
            }  
            return EncodingUtils.getString(baf.toByteArray(), "utf-8");  
        } catch (Exception e) {  
            return "";  
        }  
    }

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(MyAPP.isFastClick()){
			return;
		}
		String clickUrl="";
		clickUrl=Url.BASEURL+Url.URL_BLOB+titleList.get(position).get("href");
		Intent intent=new Intent(mActivity,DetailActivity.class);
		intent.putExtra("href", clickUrl);
		intent.putExtra("title", titleList.get(position).get("title"));
		startActivity(intent);
		Log.i("url", clickUrl);
		
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
}
