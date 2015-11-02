package com.hengxuan.stock.fragment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.anim;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hengxuan.pulltorefresh.listfragment.PullToRefreshListFragment;
import com.hengxuan.pulltorefresh.PullToRefreshBase;
import com.hengxuan.pulltorefresh.PullToRefreshBase.Mode;
import com.hengxuan.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.hengxuan.pulltorefresh.PullToRefreshBase.OnPullEventListener;
import com.hengxuan.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.hengxuan.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.hengxuan.pulltorefresh.PullToRefreshBase.State;
import com.hengxuan.pulltorefresh.PullToRefreshListView;
import com.hengxuan.stock.NewsListAdapter;
import com.hengxuan.stock.R;
import com.hengxuan.stock.Activity.MainActivity;
import com.hengxuan.stock.Activity.NewsActivity;
import com.hengxuan.stock.R.color;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.R.string;
import com.hengxuan.stock.http.HttpAPI;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.http.MyJsonObjectRequest;
import com.hengxuan.stock.model.News;
import com.hengxuan.stock.utils.Log;

public class NewsListFragment extends Fragment{
	private NewsListAdapter mAdapter;
	private int listMargin = 8;
	private int listItemMargin = 16;//*density for pixel
	private ImageView headImage;
	private int headViewHeight = 160;
	private PullToRefreshListView mPullToRefreshListView;
	private int page = 0;
    private JsonObjectRequest jsonObjectRequest = null;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

    @Override
    public void onDetach() {
        if(jsonObjectRequest != null){
            jsonObjectRequest.cancel();
            jsonObjectRequest = null;
        }
        super.onDetach();
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_news, null, false);
		mPullToRefreshListView = (PullToRefreshListView) root.findViewById(R.id.pull_refresh_list);
		mAdapter = new NewsListAdapter(getActivity());
		mPullToRefreshListView.setAdapter(mAdapter);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ColorDrawable c = new ColorDrawable(getResources().getColor(R.color.divider_color));
		mPullToRefreshListView.setDividerDrawable(c);
		mPullToRefreshListView.getRefreshableView().setDividerHeight(2);
//		headImage = new ImageView(getActivity());
//		headImage.setLayoutParams(new android.widget.AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, (int) (headViewHeight*MainActivity.density)));
//		headImage.setScaleType(ScaleType.FIT_XY);
//		mPullToRefreshListView.getRefreshableView().addHeaderView(headImage);
//		mPullToRefreshListView.setLayoutParams(lp);
		mPullToRefreshListView.getRefreshableView().setVerticalScrollBarEnabled(false);
//		loadHeadImage();

        mAdapter.getDataViaHttp(0, false);
        mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                News news = mAdapter.getData(arg2 - 1);
                Intent intent = new Intent(getActivity(),NewsActivity.class);
                intent.putExtra("articleId", news.articleId);
                intent.putExtra("date", news.date);
                startActivity(intent);
            }

        });

        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                mAdapter.getDataViaHttp(0, false);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                page++;
                mAdapter.getDataViaHttp(page, true);
            }


        });
		return root;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
//	private void loadHeadImage() {
////		ImageLoader.getInstance().displayImage("assets://place_holder.png", headImage);
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//		.bitmapConfig(Bitmap.Config.RGB_565).build();
//		ImageLoader.getInstance().displayImage("assets://place_holder.png", headImage, options);
//		
//	}

}
