package com.hengxuan.stock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.Inflater;

import com.hengxuan.stock.model.News;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsListAdapter extends BaseAdapter{
	private Context mContext;
	LinkedList<News> mListNews;//Ë«ÏòÁÐ±í
	
	public NewsListAdapter(Context c, LinkedList<News> l){
		mContext = c;
		mListNews = l;
	}
	
	public void setData(LinkedList<News> list){
		mListNews = list;
	}
	@Override
	public int getCount() {
		if(mListNews == null){
			return 0;
		}else{
			return mListNews.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.news_simple_item, parent, false);
			holder.img = (ImageView) convertView.findViewById(R.id.image);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.author = (TextView) convertView.findViewById(R.id.author);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		News news = mListNews.get(position);
		if(news.imageCache == null && news.imageUri != null){
			ImageLoader.getInstance().displayImage(news.imageUri, holder.img, null, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
		}else{
			if(news.imageCache == null){
				//show placeholder bitmap
				
			}else{
				//load image from cache
			}
			
		}
		holder.title.setText(Html.fromHtml(news.title));
		holder.date.setText(news.formatTime());
//		holder.text.setText(news.abs);
		holder.author.setText(news.author);
		return convertView;
	}
	
	class Holder{
		ImageView img;
		TextView title;
		TextView date;
		TextView text;
		TextView author;
	}

}
