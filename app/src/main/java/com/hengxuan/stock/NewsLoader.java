package com.hengxuan.stock;

import java.util.List;

import com.hengxuan.stock.model.News;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * ����News��ÿ�μ���10��
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
