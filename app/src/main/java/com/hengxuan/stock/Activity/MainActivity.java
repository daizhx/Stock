package com.hengxuan.stock.Activity;

import com.hengxuan.stock.R;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.application.Constants;
import com.hengxuan.stock.fragment.HomeFragment;
import com.hengxuan.stock.fragment.MemberFragment;
import com.hengxuan.stock.fragment.NewsListFragment;
import com.hengxuan.stock.fragment.ProfitFragment;
import com.hengxuan.stock.fragment.SettingsFragment;
import com.hengxuan.stock.fragment.StockFragment;
import com.hengxuan.stock.fragment.ZXGFragment;
import com.hengxuan.stock.user.User;
import com.hengxuan.stock.utils.Log;
import com.hengxuan.stock.utils.StockFormula;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener{
    private static final int MSG_UPDATE_ZXG = 0;
    RadioGroup tabs;
	RadioButton btnSelect;
	RadioButton btnStocks;
	RadioButton btnMember;
	RadioButton btnHome;
	FragmentManager mFM;
	private int currentTab = 0;
	public static float density;
	private static final int REQUEST_LOGIN = 110;

	private String userId;

    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private Handler mHandler;
    private Fragment[] fragments = new Fragment[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getStringExtra("userId");
        setContentView(R.layout.activity_main);
        mFM = getSupportFragmentManager();
        btnSelect = (RadioButton) findViewById(R.id.select);
        btnStocks = (RadioButton) findViewById(R.id.stocks);
        btnMember = (RadioButton) findViewById(R.id.member);
        btnHome = (RadioButton) findViewById(R.id.home);
        tabs = (RadioGroup) findViewById(R.id.tabs);
        tabs.setOnCheckedChangeListener(this);

        viewPager = new MyViewPager(this);
        viewPager.setId(R.id.viewpager);
        //ensure fragments not destroyed
        viewPager.setOffscreenPageLimit(3);
        fragmentPagerAdapter = new MyAdapter(mFM);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    Fragment f = fragments[1];
                    if(f != null && ((ZXGFragment)f).getStocksNum() > 0 && StockFormula.isOpenTime()) {
                        ((ZXGFragment) f).refresh();
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_ZXG, 5 * 1000);
                    }
                } else {
                    mHandler.removeMessages(MSG_UPDATE_ZXG);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        ((FrameLayout) findViewById(R.id.container)).addView(viewPager, layoutParams);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        density = displayMetrics.density;

        XGPushConfig.enableDebug(this, true);
        if(userId != null) {
            XGPushManager.registerPush(getApplicationContext(), userId,
                    new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object data, int flag) {
                            Log.d("+++ register push sucess. token:" + data);
                        }

                        @Override
                        public void onFail(Object data, int errCode, String msg) {
                            Log.d("+++ register push fail. token:" + data
                                    + ", errCode:" + errCode + ",msg:"
                                    + msg);
                        }
                    });
        }else{
            XGPushManager.registerPush(getApplicationContext(),
                    new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object data, int flag) {
                            Log.d("+++ register push sucess. token:" + data);
                        }

                        @Override
                        public void onFail(Object data, int errCode, String msg) {
                            Log.d("+++ register push fail. token:" + data
                                    + ", errCode:" + errCode + ",msg:"
                                    + msg);
                        }
                    });
        }

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_UPDATE_ZXG:
                        ((ZXGFragment)fragments[1]).refresh();
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_ZXG,5*1000);
                        break;
                    default:
                        break;
                }

            }
        };
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	if(currentTab == R.id.select){
        	btnSelect.setChecked(true);
            Fragment f = fragments[1];
            if(f != null && ((ZXGFragment)f).getStocksNum() > 0 && StockFormula.isOpenTime()) {
                mHandler.sendEmptyMessageDelayed(MSG_UPDATE_ZXG, 5 * 1000);
            }
        }else if(currentTab == R.id.stocks){
        	btnStocks.setChecked(true);
        }else if(currentTab == R.id.member){
        	btnMember.setChecked(true);
        }else if(currentTab == R.id.home){
        	btnHome.setChecked(true);
        }else{
        	btnHome.setChecked(true);
        	currentTab = R.id.home;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mHandler != null){
            mHandler.removeMessages(MSG_UPDATE_ZXG);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.select:
//			if(fragmentSelect == null && (fragmentSelect = (ZXGFragment)mFM.findFragmentByTag(ZXGFragment.class.getSimpleName())) == null){
//				fragmentSelect = new ZXGFragment();
//			}
//			mFM.beginTransaction().replace(R.id.container, fragmentSelect, NewsListFragment.class.getSimpleName()).commit();
            viewPager.setCurrentItem(1,false);
			currentTab = R.id.select;
			break;
		case R.id.stocks:
//			if(stocks == null && (stocks = (StockFragment) mFM.findFragmentByTag(StockFragment.class.getSimpleName())) == null){
//				stocks = new StockFragment();
//			}
//			mFM.beginTransaction().replace(R.id.container, stocks, StocksListFragment.class.getSimpleName()).commit();
            viewPager.setCurrentItem(2,false);
			currentTab = R.id.stocks;
			break;
		case R.id.member:
//            if(settingsFragment == null && (settingsFragment = (SettingsFragment) mFM.findFragmentByTag(SettingsFragment.class.getSimpleName())) == null){
//                settingsFragment = new SettingsFragment();
//            }
//            mFM.beginTransaction().replace(R.id.container, settingsFragment, SettingsFragment.class.getSimpleName()).commit();
            viewPager.setCurrentItem(3,false);
            currentTab = R.id.member;
			break;
		case R.id.home:
//			if(homeFragment == null && (homeFragment = (HomeFragment) mFM.findFragmentByTag(HomeFragment.class.getSimpleName())) == null){
//				homeFragment = new HomeFragment();
//			}
//			mFM.beginTransaction().replace(R.id.container, homeFragment,HomeFragment.class.getSimpleName()).commit();
            viewPager.setCurrentItem(0,false);
			currentTab = R.id.home;
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0,arg1,arg2);
        Log.d("requestCode="+arg0+",resultCode="+arg1);
		if(arg0 == REQUEST_LOGIN){
			if(arg1 == RESULT_OK){
				currentTab = R.id.member;
			}else{
				
			}
		}
	}

    private class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            switch (position){
                case 0:
                    f = new HomeFragment();
                    break;
                case 1:
                    f = new ZXGFragment();
                    break;
                case 2:
                    f = new ProfitFragment();
                    break;
                case 3:
                    f = new SettingsFragment();
                    break;
                default:
                    break;
            }
            fragments[position] = f;
            return f;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public static class MyViewPager extends ViewPager{

        public MyViewPager(Context context) {
            super(context);
        }
        public MyViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            return false;
//            return super.onTouchEvent(ev);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return false;
//            return super.onInterceptTouchEvent(ev);
        }
    }

}
