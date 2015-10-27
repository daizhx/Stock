package com.hengxuan.stock;

import java.util.ArrayList;
import java.util.List;

import com.hengxuan.stock.model.Stock;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StockListAdapter extends BaseAdapter {
	private Context mContext;
	List<Stock> stocks;
	
	public StockListAdapter(Context c) {
		mContext = c;
	}
	public StockListAdapter(Context c,List<Stock> l) {
		mContext = c;
		stocks = l;
	}
	
	public void setData(List<Stock> l){
		stocks = l;
	}
	
	@Override
	public int getCount() {
		if(stocks != null){
			return stocks.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(stocks != null){
			return stocks.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.stocks_item, parent, false);
			holder = new Holder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.latest = (TextView) convertView.findViewById(R.id.latest);
			holder.opening = (TextView) convertView.findViewById(R.id.opening);
			holder.peak = (TextView) convertView.findViewById(R.id.peak);
			holder.increase = (TextView) convertView.findViewById(R.id.increase);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		if(stocks != null && stocks.size() > 0){
			Stock s = stocks.get(position);
			holder.name.setText(s.name);
			holder.number.setText(s.number);
			holder.latest.setText(s.latestPrice);
			if(Float.parseFloat(s.latestPrice) >= Float.parseFloat(s.openingPrice)){
				holder.latest.setTextColor(Color.RED);
			}else{
				holder.latest.setTextColor(Color.GREEN);
			}
			holder.opening.setText(s.openingPrice);
			if(Float.parseFloat(s.openingPrice) >= Float.parseFloat(s.lastPrice)){
				holder.opening.setTextColor(Color.RED);
			}else{
				holder.opening.setTextColor(Color.GREEN);
			}
			holder.peak.setText(stocks.get(position).peak);
			if(Float.parseFloat(s.peak) >= Float.parseFloat(s.lastPrice)){
				holder.peak.setTextColor(Color.RED);
			}else{
				holder.peak.setTextColor(Color.GREEN);
			}
			holder.increase.setText(s.getIncrease());
			if(s.getIncreaseNumber() >= 0){
				holder.increase.setTextColor(Color.RED);
			}else{
				holder.increase.setTextColor(Color.GREEN);
			}
		}
		return convertView;
	}
	class Holder{
		TextView name;
		TextView number;
		TextView latest;
		TextView opening;
		TextView peak;
		TextView increase;
	}

}
