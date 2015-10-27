package com.hengxuan.stock.Activity;

import com.hengxuan.stock.R;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.SubActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class StockDetailActivity extends SubActivity {
	ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_detail);
		mImageView = (ImageView) findViewById(R.id.img);
		ImageLoader.getInstance().displayImage("http://image.sinajs.cn/newchart/min/n/sh000001.gif", mImageView);
	}
}
