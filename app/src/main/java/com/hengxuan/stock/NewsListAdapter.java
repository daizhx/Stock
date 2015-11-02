package com.hengxuan.stock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.Inflater;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hengxuan.stock.http.HttpAPI;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.http.MyJsonObjectRequest;
import com.hengxuan.stock.model.News;
import com.hengxuan.stock.utils.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsListAdapter extends BaseAdapter{
	private Context mContext;
	private LinkedList<News> mListNews = new LinkedList<>();//data from web
	
	public NewsListAdapter(Context c){
		mContext = c;
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

    public void getDataViaHttp(int page,final boolean append){
        Log.d("get news data");
        final HttpUtils httpUtils = HttpUtils.getInstance(mContext);
        String d = (String) DateFormat.format("yyyy-MM-dd", System.currentTimeMillis());
        String url = HttpAPI.GET_ARTICLE + "/"+page+"/0/"+d;
        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject data = (JSONObject) httpUtils.parseData(response);
                if(data !=null){
                    JSONArray news = httpUtils.getNewsList(data);
                    Log.d("get news:"+news);
                    //Ë¢ÐÂÊý¾Ý
                    if(!append){
                        mListNews.clear();
                    }
                    for(int i=0;i<news.length();i++){
                        JSONObject msg = null;
                        try {
                            msg = (JSONObject) news.get(i);
                            News n = new News();
                            n.title = msg.getString("title");
                            n.date = msg.getString("createOn");
                            n.articleId = msg.getString("stockArticleId");
                            mListNews.add(n);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                notifyDataSetChanged();
//                mPullToRefreshListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("get news list fail..."+error.getLocalizedMessage());
//                mPullToRefreshListView.onRefreshComplete();
            }
        });
        httpUtils.addToRequestQueue(jsonObjectRequest);
    }

    public News getData(int position){
        return mListNews.get(position);
    }

}
