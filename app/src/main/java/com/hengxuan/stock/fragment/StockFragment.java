package com.hengxuan.stock.fragment;

import java.lang.reflect.Field;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hengxuan.pulltorefresh.PullToRefreshBase;
import com.hengxuan.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.hengxuan.stock.application.Constants;
import com.hengxuan.stock.http.HttpAPI;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.http.MyJsonObjectRequest;
import com.hengxuan.stock.user.User;
import com.hengxuan.stock.utils.Log;
import com.hengxuan.stock.widget.PullToRefreshPinnedSectionListView;
import com.hb.views.PinnedSectionListView;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.hengxuan.stock.R;
import com.hengxuan.stock.Activity.SubscriptionActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StockFragment extends Fragment {
//	private ListView mListView;
	private BaseAdapter mListAdapter;
	private PullToRefreshPinnedSectionListView mPullToRefreshPinnedSectionListView;
	private RelativeLayout mRelativeLayout;
    private int mId;
    private String title;
    private TextView mTVHint;
    private Button mBtnRetry;
    private JsonObjectRequest jsonObjectRequest = null;

    public static final String INTENT_ACTION_STOCK_ANALYST = "com.hengxuan.stock.STOCK_ANALYST";
    public static final String EXTRA_STOCK_NAME = "name";
    public static final String EXTRA_STOCK_CODE = "code";

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            mId = args.getInt("flag");
            title = args.getString("title");
        }
		mListAdapter = new MyPinnedSectionListAdapter(getActivity());
		getData(mId, 0);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_stocks, container, false);
//        TextView tvTitle = (TextView) root.findViewById(R.id.title_text);
//        if(title == null) {
//            tvTitle.setText(R.string.jx);
//        }else{
//            tvTitle.setText(title);
//        }
//		mListView = (ListView) root.findViewById(android.R.id.list);
//		mListView.setAdapter(mListAdapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("listview item cliked:"+position);
//            }
//        });
		mPullToRefreshPinnedSectionListView = (PullToRefreshPinnedSectionListView) root.findViewById(R.id.pull_to_refresh_list);
        mPullToRefreshPinnedSectionListView.setAdapter(mListAdapter);
        mPullToRefreshPinnedSectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int type = mListAdapter.getItemViewType(position);
                if(type == MyPinnedSectionListAdapter.ITEM){
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                    CharSequence name = text1.getText();
                    CharSequence code = text2.getText();
                    if(code.toString().endsWith("***")) {
                       ChargeDialogFragment chargeDialogFragment = new ChargeDialogFragment();
                        chargeDialogFragment.show(getFragmentManager(),ChargeDialogFragment.class.getSimpleName());
                    }else{
                        Intent intent = new Intent(INTENT_ACTION_STOCK_ANALYST);
                        intent.putExtra(EXTRA_STOCK_CODE, code);
                        intent.putExtra(EXTRA_STOCK_NAME, name);
                        startActivity(intent);
                    }
                }else if(type == MyPinnedSectionListAdapter.SECTION){

                }else {

                    Log.w("some wrong");
                }

            }
        });
		mPullToRefreshPinnedSectionListView.setOnRefreshListener(new OnRefreshListener<PinnedSectionListView>() {

            @Override
            public void onRefresh(
                    PullToRefreshBase<PinnedSectionListView> refreshView) {
                getData(mId, 0);
            }
        });

		mRelativeLayout = (RelativeLayout) root.findViewById(R.id.subscription_hint);
        mTVHint = (TextView) root.findViewById(android.R.id.hint);
        mBtnRetry = (Button) root.findViewById(android.R.id.button1);
		return root;
		
	};
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	@Override
	public void onDetach() {
        if(jsonObjectRequest != null){
            jsonObjectRequest.cancel();
            jsonObjectRequest = null;
        }
		super.onDetach();
		//如果不detach内部fragment的话，重新attach的时候会不显示
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	
	private LinkedList parseData(JSONArray data){
		LinkedList list = new LinkedList();
		String addedDate = null;
		for(int i=0;i<data.length();i++){
			JSONObject item;
			try {
				item = (JSONObject) data.get(i);
				String ds = item.getString("date");
                if(ds != null) {
                    String date = (String) DateFormat.format("yyyy-MM-dd", Long.parseLong(ds));
                    if(!date.equals(addedDate)){
                        ListSection section = new ListSection();
                        addedDate = section.title = date;
                        list.add(section);
                    }
                }else{
                    //no time recode,pass
                    continue;
                }

				String code = item.getString("code");

				String name = item.getString("name");
				ListItem listitem = new ListItem();
				listitem.stockCode = code;
				listitem.stockName = name;
				if(name == "null"){
					listitem.stockName = "";
				}

                if(mId == Constants.STOCKS_1) {
                    String comment = item.getString("comment");
                    listitem.comment = comment;
                    if(comment == "null"){
                        listitem.comment = "";
                    }
                }

				list.add(listitem);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
        return list;

	}
	private void getData(final int flag,int page){
        final HttpUtils httpUtils = HttpUtils.getInstance(getActivity());
		String date = (String) DateFormat.format("yyyy-MM-dd", System.currentTimeMillis());
        String url = HttpAPI.GET_TODAY_STOCK + "/"+page+"/"+date;
        User user = User.getUser(getActivity());
        if(flag == Constants.STOCKS_2) {
            url = HttpAPI.GET_MONTH_STOCK + "/" + user.getId() + "/"+page + "/" + date;
        }else if(flag == Constants.STOCKS_3){
            url = HttpAPI.GET_JG_STOCK + "/" + user.getId() + "/" + page + "/" + date;
        }
        Log.d("url="+url);
        jsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jasonArray = null;
                        Log.d("result="+response.toString());
                        try {
                            JSONObject data = (JSONObject)httpUtils.parseData(response);
                            if(data == null)return;
                            jasonArray = data.getJSONArray("data");
                            LinkedList list = parseData(jasonArray);
                            if(list.isEmpty()){
                                mTVHint.setText(getString(R.string.no_data));
                                mTVHint.setVisibility(View.VISIBLE);
                                mPullToRefreshPinnedSectionListView.onRefreshComplete();
                                return;
                            }

                            ((MyPinnedSectionListAdapter)mListAdapter).setData(list);
                            if(flag == Constants.STOCKS_2 || flag == Constants.STOCKS_3){

                            }else {
                                mRelativeLayout.setVisibility(View.VISIBLE);
                                mRelativeLayout.findViewById(R.id.btn_subscription).setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getActivity(), SubscriptionActivity.class));
                                    }
                                });
                            }
                            mListAdapter.notifyDataSetChanged();
                            mPullToRefreshPinnedSectionListView.onRefreshComplete();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        mTVHint.setText(getString(R.string.not_access_server));
                        mTVHint.setVisibility(View.VISIBLE);
                        mBtnRetry.setVisibility(View.VISIBLE);
                        mBtnRetry.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData(mId, 0);
                            }
                        });
						mPullToRefreshPinnedSectionListView.onRefreshComplete();
                    }
                });

        if(mTVHint != null && mBtnRetry != null) {
            mTVHint.setVisibility(View.GONE);
            mBtnRetry.setVisibility(View.GONE);
        }
        httpUtils.addToRequestQueue(jsonObjectRequest);
	}
//	public ListView getList(){
//		return mListView;
//	}
	
	static class ListSection{
		public String title;
	}
	static class ListItem{
		public String stockName;
		public String stockCode;
		public String comment;
	}
	private class MyPinnedSectionListAdapter extends BaseAdapter implements PinnedSectionListAdapter{
		private LinkedList data = new LinkedList();
		public static final int ITEM = 0;
		public static final int SECTION = 1;
		private Context mContext;
		
		public void addFist(Object obj){
			data.addFirst(obj);
		}
		
		public void add(Object obj){
			data.add(obj);
		}
		public void setData(LinkedList d){
			data = d;
		}
		
		public MyPinnedSectionListAdapter(Context context){
			mContext = context;
		}
		@Override
		public int getCount() {
			return data.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Object obj = getItem(position);
			if(convertView == null){
				if(obj instanceof ListSection){
					convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, null, false);
					convertView.setBackgroundColor(getResources().getColor(R.color.bg));
					TextView text = (TextView)convertView.findViewById(android.R.id.text1);
					text.setText(((ListSection)obj).title);
					
				}else{
					convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_expandable_list_item_2, null, false);
					TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
					TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
					text1.setText(((ListItem)obj).stockName);
					text2.setText(((ListItem)obj).stockCode);
				}
			}else{
				if(obj instanceof ListSection){
					TextView text = (TextView)convertView.findViewById(android.R.id.text1);
					text.setText(((ListSection)obj).title);
				}else{
					TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
					TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
					text1.setText(((ListItem)obj).stockName);
					text2.setText(((ListItem)obj).stockCode);
				}
				
			}
			
			return convertView;
		}

		@Override
		public int getItemViewType(int position) {
			if(data.get(position) instanceof ListSection){
				return SECTION;
			}
			return ITEM;
		}
		@Override
		public boolean isItemViewTypePinned(int viewType) {
			// TODO Auto-generated method stub
			return SECTION == viewType;
		}
		
		@Override
		public int getViewTypeCount() {
			return 2;
		}
	}
}
