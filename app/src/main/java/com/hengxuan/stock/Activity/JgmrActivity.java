package com.hengxuan.stock.Activity;

import com.hengxuan.stock.R;
import com.hengxuan.stock.SubActivity;
import com.hengxuan.stock.application.Constants;
import com.hengxuan.stock.fragment.StockFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class JgmrActivity extends FragmentActivity {
	private FragmentManager mFM;
	private StockFragment stocks;
	private static final String FRAGMENT_Tag = "jgmr";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        setTitle(getString(R.string.jg_stocks));
		mFM = getSupportFragmentManager();
		if(stocks == null && (stocks = (StockFragment) mFM.findFragmentByTag(FRAGMENT_Tag)) == null){
            stocks = new StockFragment();
            Bundle args = new Bundle();
            args.putInt("flag",Constants.STOCKS_3);
            args.putString("title",getString(R.string.jg_stocks));
            stocks.setArguments(args);
		}
		mFM.beginTransaction().replace(android.R.id.content, stocks, FRAGMENT_Tag).commit();
	}
}
