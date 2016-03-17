package com.cd.csdnblog.adapter;

import java.util.List;
import java.util.Map;
import com.cd.csdnblog.R;
import com.cd.csdnblog.activity.ManagerActivity;
import com.cd.csdnblog.bean.Blog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class BlobUserNameAdapter extends BaseAdapter {
	private List<Blog> mData;
	private Context mContext;
	
	public BlobUserNameAdapter(List<Blog> mData, Context mContext) {
		super();
		this.mData = mData;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final CheckBox selectCbView;
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_blog_username, null);
			holder=new ViewHolder();
			holder.blogUserNameView=(TextView) convertView.findViewById(R.id.blogUserNameView);
			convertView.setTag(holder);
		}
		selectCbView=(CheckBox) convertView.findViewById(R.id.selectCbView);
		holder=(ViewHolder) convertView.getTag();
		holder.blogUserNameView.setText(mData.get(position).getBlogUserName());
		
		selectCbView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (selectCbView.isChecked()) {
					ManagerActivity.delList.add(mData.get(position).getObjectId());
				} else {
					ManagerActivity.delList.remove(mData.get(position).getBlogUserName());
				}
			}
		});
		return convertView;
	}
	
	static class ViewHolder{
		TextView blogUserNameView;
	}

}
