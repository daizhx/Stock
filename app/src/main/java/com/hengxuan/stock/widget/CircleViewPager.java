package com.hengxuan.stock.widget;

import java.lang.ref.WeakReference;

import com.hengxuan.stock.utils.Log;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CircleViewPager extends FrameLayout implements OnPageChangeListener {
	private ViewPager mViewPager;
	private RelativeLayout mIndicatorLayout;
	private int pageCount = 3;
	private String[] imageUris;
	private int currentPage = 0;
	private float indicatorDotSize = 4;
	private Context mContext;
	private Indicator indicator;
	private static final int WHEEL = 100;
	private static final int WHEEL_WAIT = 101;
	private static final int WHEEL_STOP = 101;
	private Handler mHandler;

    int[] index = new int[]{0,1,2,1,0};
    int p = 0;
	public void next(){
        p++;
        p = p % index.length;
        mViewPager.setCurrentItem(index[p]);
	}
	
	public void startCircle(){
		mHandler.sendEmptyMessageDelayed(WHEEL, 5000);
	}
	
	private void stopCircle(){
		mHandler.removeMessages(WHEEL);
	}

	public CircleViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		imageUris = new String[]{"assets://1.png","assets://2.png","assets://3.png"};
		mViewPager = new ViewPager(context);
		mViewPager.setAdapter(new ViewPagerAdapter());
		mViewPager.setOnPageChangeListener(this);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		addView(mViewPager, lp);
		lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,30);
		mIndicatorLayout = new RelativeLayout(context);
		mIndicatorLayout.setBackgroundColor(0x33888888);
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		indicator = new Indicator(getContext(), null);
		lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
		mIndicatorLayout.addView(indicator,lp2);
		lp.gravity = Gravity.BOTTOM;
		addView(mIndicatorLayout, lp);
		
		mHandler = new MyHandler(this);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		stopCircle();
	}
	

	private class Indicator extends View{
		private Paint mPaint;
		private int width = 180;
		private int height = 40;
		private float strokeSize = 2;
		public Indicator(Context context, AttributeSet attrs) {
			super(context, attrs);
			
			mPaint = new Paint();
			mPaint.setColor(Color.WHITE);
			mPaint.setStrokeWidth(strokeSize);
			mPaint.setAntiAlias(true);
		}
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			for(int i=0;i<pageCount;i++){
				float x = (float) ((width/pageCount)*(i+1.0/2));
				if(i == currentPage){
					mPaint.setStyle(Paint.Style.FILL);
				}else{
					mPaint.setStyle(Paint.Style.STROKE);
				}
				canvas.drawCircle(x, height/2, indicatorDotSize, mPaint);
			}
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			setMeasuredDimension(width, height);
		}
		
	}
	
	private class ViewPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return pageCount;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			final ImageView v = new ImageView(getContext());
			v.setScaleType(ImageView.ScaleType.FIT_XY);
			ImageLoader.getInstance().loadImage(imageUris[position]
					, null, null, new ImageLoadingListener() {
						
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
							v.setImageBitmap(loadedImage);
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
					});
			container.addView(v);
			return v;
		}
		
		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		currentPage = arg0;
		indicator.invalidate();
	}
	
	private static class MyHandler extends Handler{
		private final WeakReference<CircleViewPager> circleViewPagerRef;
		public MyHandler(CircleViewPager f){
			circleViewPagerRef = new WeakReference<CircleViewPager>(f);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case WHEEL:
				if(circleViewPagerRef != null){
					CircleViewPager circleViewPager = circleViewPagerRef.get();
					circleViewPager.next();
					circleViewPager.startCircle();
				}
				break;

			default:
				break;
			}
			
		}
	}
}
