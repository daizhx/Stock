package com.hengxuan.stock.model;

public class Stock {
	public String name;
	public String number;
	public String latestPrice;
	public String openingPrice;
	public String peak;//最高
	private String increase;//涨幅
	public String lastPrice;//昨日收盘价
	
	public String getIncrease(){
		float f1 = Float.valueOf(latestPrice);
		float f2 = Float.valueOf(openingPrice);
		float ret = ((f1-f2)/f2)*100;
		String str = String.format("%.2f", ret)+"%";
		return str;
	}
	
	public float getIncreaseNumber(){
		float f1 = Float.valueOf(latestPrice);
		float f2 = Float.valueOf(openingPrice);
		float ret = ((f1-f2)/f2)*100;
		return ret;
	}
	
}
