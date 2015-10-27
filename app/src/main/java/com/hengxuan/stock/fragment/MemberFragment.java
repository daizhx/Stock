package com.hengxuan.stock.fragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.hengxuan.stock.R;
import com.hengxuan.stock.Activity.PurchaseActivity;
import com.hengxuan.stock.Activity.RecordActivity;
import com.hengxuan.stock.Activity.RedeemActivity;
import com.hengxuan.stock.Activity.SettingsActivity;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.user.User;
import com.hengxuan.stock.utils.Log;
import com.hengxuan.stock.widget.ScrollTextView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class MemberFragment extends Fragment {
	private TextView tvName;
	private ImageButton BtnRefresh;
	private ImageButton BtnSettings;
	//in
	private View menu1;
	//details
	private View menu2;
	//out
	private View menu3;
	
	private ScrollTextView tvTotalProperty;
	private ScrollTextView tvTodayBonus;
	private ScrollTextView tvTotalBonus;
	
	private void setUpView(){
		tvName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		BtnRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvTotalProperty.startScrollAnimation();
				tvTodayBonus.startScrollAnimation();
				tvTotalBonus.startScrollAnimation();
				updateAccouteInfo();
			}
		});
		BtnSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),SettingsActivity.class));
			}
		});
		menu1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),PurchaseActivity.class));
			}
		});
		
		menu2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),RecordActivity.class));
				
			}
		});
		menu3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),RedeemActivity.class));
				
			}
		});
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        getActivity().getActionBar().hide();
	}

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().getActionBar().show();
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_member, container, false);
		tvName = (TextView) root.findViewById(R.id.name);
        User user = User.getUser(getActivity());
		String msg = user.getName();
        if(tvName != null) {
            tvName.setText(user.getName());
        }
		BtnRefresh = (ImageButton) root.findViewById(R.id.icon_refresh);
		BtnSettings = (ImageButton) root.findViewById(R.id.icon_setting);
		menu1 = root.findViewById(R.id.menu1);
		menu2 = root.findViewById(R.id.menu2);
		menu3 = root.findViewById(R.id.menu3);
		tvTotalProperty = (ScrollTextView) root.findViewById(R.id.tv_amount);
		tvTodayBonus = (ScrollTextView) root.findViewById(R.id.tv_today_bonus);
		tvTotalBonus = (ScrollTextView) root.findViewById(R.id.tv_total_bonus);
		setUpView();
		return root;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tvTotalProperty.startScrollAnimation();
		tvTodayBonus.startScrollAnimation();
		tvTotalBonus.startScrollAnimation();
		updateAccouteInfo();
	}
	
	private void updateAccouteInfo(){
//		String url = HttpUtils.API_URL_BASE + HttpUtils.GET_ACCOUT_DETAIL + "/" + User.getName(getActivity());
//		HttpUtils.executeHttpAPI(getActivity(), url, new JsonHttpResponseHandler(){
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					JSONObject response) {
//				Log.d(response.toString());
//				JSONObject data = (JSONObject) HttpUtils.parseData(response);
//				if(data != null){
//					try {
//						tvTodayBonus.setText("" + data.get("todayRate"));
//						tvTotalBonus.setText("" + data.get("historyRate"));
//						tvTotalProperty.setText("" + data.get("mytollMoney"));
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
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

}
