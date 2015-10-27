package com.hengxuan.stock.fragment;

import java.util.ArrayList;
import java.util.List;

import com.hengxuan.stock.Activity.AnalystsActivity;
import com.hengxuan.stock.Activity.DpgdActivity;
import com.hengxuan.stock.R;
import com.hengxuan.stock.Activity.JgmrActivity;
import com.hengxuan.stock.Activity.MyjxActivity;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.R.string;
import com.hengxuan.stock.utils.Log;
import com.hengxuan.stock.widget.CircleViewPager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {
	private CircleViewPager mCircleViewPager;
	private TextView menu1;
	private TextView menu2;
	private TextView menu3;
//	private ListView mListView;
//	private BaseAdapter mMyListAdapter;
	private List arrayList = new ArrayList<CharSequence>();

    private TextView tvDpgd;
	
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
                Toast.makeText(getActivity(),"ÔÝÎ´¿ª·Å",Toast.LENGTH_SHORT).show();
				startActivity(new Intent(getActivity(),JgmrActivity.class));
			}
		});
		menu3 = (TextView) root.findViewById(R.id.menu3);
		menu3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                startActivity(new Intent(getActivity(), AnalystsActivity.class));
			}
		});

        tvDpgd = (TextView) root.findViewById(id.tv_dpgd);
        tvDpgd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DpgdActivity.class));
            }
        });
		return root;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mCircleViewPager.startCircle();
	}
}
