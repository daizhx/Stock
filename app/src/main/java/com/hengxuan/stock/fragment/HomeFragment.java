package com.hengxuan.stock.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hb.views.PinnedHeaderListView;
import com.hb.views.SectionedBaseAdapter;
import com.hengxuan.pulltorefresh.PullToRefreshBase;
import com.hengxuan.stock.Activity.AnalystsActivity;
import com.hengxuan.stock.Activity.DpgdActivity;
import com.hengxuan.stock.Activity.NewsActivity;
import com.hengxuan.stock.NewsListAdapter;
import com.hengxuan.stock.R;
import com.hengxuan.stock.Activity.JgmrActivity;
import com.hengxuan.stock.Activity.MyjxActivity;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.R.string;
import com.hengxuan.stock.http.HttpAPI;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.http.MyJsonObjectRequest;
import com.hengxuan.stock.model.News;
import com.hengxuan.stock.utils.Log;
import com.hengxuan.stock.widget.CircleViewPager;
import com.hengxuan.stock.widget.PullToRefreshPinnedHeaderListView;
import com.hengxuan.stock.widget.PullToRefreshPinnedSectionListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
	private CircleViewPager mCircleViewPager;
	private TextView menu1;
	private TextView menu2;
	private TextView menu3;
    private TextView menu4;
	private ListView mListView;
//	private BaseAdapter mMyListAdapter;
//    private NewsListAdapter newsListAdapter;

    private TextView tvDpgd;
    PullToRefreshPinnedHeaderListView pullToRefreshPinneHeaderListView;

    private DataListAdapter adapter;
    private LinkedList dataList = new LinkedList();

    private class DataListAdapter extends SectionedBaseAdapter{

        private Context mContext;
        public DataListAdapter(Context context){
            mContext = context;
        }
        @Override
        public Object getItem(int section, int position) {
            return null;
        }

        @Override
        public long getItemId(int section, int position) {
            return 0;
        }

        @Override
        public int getSectionCount() {
            return 1;
        }

        @Override
        public int getCountForSection(int section) {
            return dataList.size();
        }

        @Override
        public View getItemView(int section, int position, View convertView, ViewGroup parent) {
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
            News news = (News) dataList.get(position);
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

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            LinearLayout layout = null;
            if (convertView == null) {
                LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
            } else {
                layout = (LinearLayout) convertView;
            }
            ((TextView) layout.findViewById(R.id.textItem)).setText("重要消息");
            return layout;
        }



        public News getData(int position){
            return (News) dataList.get(position);
        }
    }

    public void getDataViaHttp(int page,final boolean append){
        Log.d("get news data");
        final HttpUtils httpUtils = HttpUtils.getInstance(getActivity());
        String d = (String) DateFormat.format("yyyy-MM-dd", System.currentTimeMillis());
        String url = HttpAPI.GET_ARTICLE + "/"+page+"/0/"+d;
        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject data = (JSONObject) httpUtils.parseData(response);
                if(data !=null){
                    JSONArray news = httpUtils.getNewsList(data);
                    Log.d("get news:"+news);
                    //刷新数据
                    if(!append){
                        dataList.clear();
                    }
                    for(int i=0;i<news.length();i++){
                        JSONObject msg = null;
                        try {
                            msg = (JSONObject) news.get(i);
                            News n = new News();
                            n.title = msg.getString("title");
                            n.date = msg.getString("createOn");
                            n.articleId = msg.getString("stockArticleId");
                            dataList.add(n);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                pullToRefreshPinneHeaderListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("get news list fail..."+error.getLocalizedMessage());
                pullToRefreshPinneHeaderListView.onRefreshComplete();
            }
        });
        httpUtils.addToRequestQueue(jsonObjectRequest);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_home, null, false);
        pullToRefreshPinneHeaderListView = (PullToRefreshPinnedHeaderListView) root.findViewById(android.R.id.list);
        pullToRefreshPinneHeaderListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getDataViaHttp(0, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        mListView = pullToRefreshPinneHeaderListView.getRefreshableView();
        mListView.setHeaderDividersEnabled(false);
        View headerView = View.inflate(getActivity(), R.layout.home_list_head, null);
        mListView.addHeaderView(headerView);

        TextView tvTitle = (TextView) root.findViewById(id.title_text);
        tvTitle.setText(string.home);
		mCircleViewPager = (CircleViewPager) root.findViewById(R.id.circleviewpager);
		menu1 = (TextView) root.findViewById(R.id.menu1);
		menu1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),MyjxActivity.class));
			}
		});
		menu2 = (TextView) root.findViewById(R.id.menu2);
		menu2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),JgmrActivity.class));
			}
		});
        menu3 = (TextView) root.findViewById(R.id.menu3);
        menu3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DpgdActivity.class));
            }
        });
		menu4 = (TextView) root.findViewById(R.id.menu4);
		menu4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                startActivity(new Intent(getActivity(), AnalystsActivity.class));
			}
		});

//        tvDpgd = (TextView) root.findViewById(id.tv_dpgd);
//        tvDpgd.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), DpgdActivity.class));
//            }
//        });
//        mListView = (ListView) root.findViewById(android.R.id.list);
        ((PinnedHeaderListView)mListView).setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
                if(position == 1)return;

                News news = adapter.getData(position - 2);
                Intent intent = new Intent(getActivity(),NewsActivity.class);
                intent.putExtra("articleId", news.articleId);
                intent.putExtra("date", news.date);
                startActivity(intent);
            }

            @Override
            public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {
                Log.d("section="+section);
            }
        });
        adapter = new DataListAdapter(getActivity());
        getDataViaHttp(0,false);
        mListView.setAdapter(adapter);
		return root;
	}

    @Override
    public void onResume() {
        super.onResume();
        if(mCircleViewPager != null) {
            mCircleViewPager.startCircle();
        }
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
	}

}
