package com.cd.csdnblog.adapter;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.cd.csdnblog.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteListAdapter extends BaseAdapter {

	private List<Map<String,String>> mData;
	private Context mContext;
	private String href;
	

	public String getHref() {
		return href;
	}

	public NoteListAdapter(List<Map<String, String>> mData, Context mContext) {
		super();
		this.mData = mData;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_titlelist, null);
			holder=new ViewHolder();
			holder.titleView=(TextView) convertView.findViewById(R.id.titleView);
			convertView.setTag(holder);
		}
		holder=(ViewHolder) convertView.getTag();
		holder.titleView.setText(mData.get(position).get("title"));
		href=mData.get(position).get("href");
		return convertView;
	}
	
	static class ViewHolder{
		TextView titleView;
	}

}
