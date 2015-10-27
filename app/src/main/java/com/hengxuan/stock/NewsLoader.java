package com.hengxuan.stock;

import java.util.List;

import com.hengxuan.stock.model.News;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * 加载News，每次加载10条
 * @author Administrator
 *
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {
	public NewsLoader(Context c){
		super(c);
	}
	@Override
	public List<News> loadInBackground() {
		// TODO Auto-generated method stub
		return null;
	}

}
