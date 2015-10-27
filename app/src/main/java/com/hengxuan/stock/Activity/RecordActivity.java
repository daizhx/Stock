package com.hengxuan.stock.Activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hengxuan.pulltorefresh.PullToRefreshListView;
import com.hengxuan.stock.R;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.SubActivity;
import com.hengxuan.stock.http.HttpUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
/**
 * show the exchange records
 * @author Administrator
 *
 */
public class RecordActivity extends SubActivity {
	private PullToRefreshListView mPullToRefreshListView;
	private MyListAdapter listAdapter;
	private LinkedList<Map<String,String>> listItems = new LinkedList<Map<String,String>>();
	private static final String KEY_OPR = "opr";
	private static final String KEY_NAME = "name";
	private static final String KEY_CODE = "code";
	private static final String KEY_NUM = "number";
	private static final String KEY_PRICE = "price";
	private static final String KEY_DATE = "date";
	private static final String KEY_TIME = "time";
	
	private TextView emptyText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.record));
		setContentView(R.layout.activity_record);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		emptyText = (TextView) findViewById(R.id.empty_text);
		listAdapter = new MyListAdapter(this);
		mPullToRefreshListView.setAdapter(listAdapter);
		getDataTask();
	}
	
	/**
	 * async get data for listItems
	 */
	private void getDataTask(){
		//TODO
//		String url = HttpUtils.API_URL_BASE + HttpUtils.GET_EXCHANGE_DETAIL +  "/" + 0;
//		HttpUtils.executeHttpAPI(this, url, new JsonHttpResponseHandler(){
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					JSONObject response) {
//				JSONArray data = (JSONArray) HttpUtils.parseData(response);
//				if(data != null){
//
//				}
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					Throwable throwable, JSONObject errorResponse) {
//				// TODO Auto-generated method stub
//				super.onFailure(statusCode, headers, throwable, errorResponse);
//			}
//		});
	}
	
	private void testAddData(){
		Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_OPR, "Âò");
		map.put(KEY_NAME, "±©·ç¿Æ¼¼");
		map.put(KEY_CODE, "808808");
		map.put(KEY_NUM,"1000");
		map.put(KEY_PRICE, "18.90");
		map.put(KEY_DATE, "2015.05.15");
		map.put(KEY_TIME, "9.00");
		listItems.add(map);
		listAdapter.notifyDataSetChanged();
		emptyText.setVisibility(View.GONE);
	}
	
	private class MyListAdapter extends BaseAdapter{
		Context mContext;
		
		public MyListAdapter(Context c){
			mContext = c;
		}
		@Override
		public int getCount() {
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			return listItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			Map map = listItems.get(position);
			if(convertView == null){
				holder = new Holder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.record_list_item, null, false);
				holder.operation = (TextView) convertView.findViewById(R.id.opr);
				holder.stockName = (TextView) convertView.findViewById(R.id.name);
				holder.stockCode = (TextView) convertView.findViewById(R.id.code);
				holder.num = (TextView) convertView.findViewById(R.id.num);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				convertView.setTag(holder);
			}
			holder = (Holder) convertView.getTag();
			holder.operation.setText((CharSequence) map.get(KEY_OPR));
			holder.stockName.setText((CharSequence) map.get(KEY_NAME));
			holder.stockCode.setText((CharSequence) map.get(KEY_CODE));
			holder.num.setText((CharSequence) map.get(KEY_NUM));
			holder.price.setText((CharSequence) map.get(KEY_PRICE));
			holder.date.setText((CharSequence) map.get(KEY_DATE));
			holder.time.setText((CharSequence) map.get(KEY_TIME));
			return convertView;
		}
		class Holder{
			TextView operation;
			TextView stockName;
			TextView stockCode;
			TextView num;
			TextView price;
			TextView date;
			TextView time;
		}
		
	}
}
