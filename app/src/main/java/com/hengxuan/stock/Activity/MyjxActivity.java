package com.hengxuan.stock.Activity;

import com.hengxuan.stock.R;
import com.hengxuan.stock.SubActivity;
import com.hengxuan.stock.application.Constants;
import com.hengxuan.stock.fragment.StockFragment;
import com.hengxuan.stock.fragment.StocksListFragment;
import com.hengxuan.stock.utils.Log;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class MyjxActivity extends FragmentActivity implements TabHost.OnTabChangeListener,TabHost.TabContentFactory{
	private FragmentManager mFM;
	private StockFragment dayStocks;
    private StockFragment monthStocks;
	private static final String DAY_Tag = "day";
    private static final String MONTH_Tag = "month";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        mFM = getSupportFragmentManager();
        setContentView(R.layout.activity_myjx);
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);
        tabHost.addTab(tabHost.newTabSpec(DAY_Tag).setIndicator(getString(R.string.stocks_day)).setContent(this));
        tabHost.addTab(tabHost.newTabSpec(MONTH_Tag).setIndicator(getString(R.string.stocks_month)).setContent(this));
//		setTitle(getString(R.string.stocks_month));
	}

    @Override
    public void onTabChanged(String tabId) {
        Log.i("onTabChanged:" + tabId);
        if(tabId.equals(DAY_Tag)) {
            mFM.beginTransaction().replace(android.R.id.tabcontent, dayStocks, tabId).commit();
        }else if(tabId.equals(MONTH_Tag)){
            mFM.beginTransaction().replace(android.R.id.tabcontent, monthStocks, tabId).commit();
        }

    }

    @Override
    public View createTabContent(String tag) {
        Log.i("createTabContent:"+tag);
        if(tag.equals(DAY_Tag) && dayStocks == null && (dayStocks = (StockFragment) mFM.findFragmentByTag(tag)) == null){
            dayStocks = new StockFragment();
            Bundle args = new Bundle();
            args.putInt("flag", Constants.STOCKS_1);
            args.putString("title",getString(R.string.stocks_day));
            dayStocks.setArguments(args);
        }else if(tag.equals(MONTH_Tag) && monthStocks == null && (monthStocks = (StockFragment) mFM.findFragmentByTag(tag)) == null){
            monthStocks = new StockFragment();
            Bundle args = new Bundle();
            args.putInt("flag", Constants.STOCKS_2);
            args.putString("title",getString(R.string.stocks_month));
            monthStocks.setArguments(args);
        }
        return new View(this);
    }
}
