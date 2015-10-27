package com.hengxuan.stock.model;

import java.sql.Date;

import android.text.format.DateFormat;

public class News {
	public String title;
	//摘要
	public String abs;
	public String date;
	public String imageUri;
	//新闻文件html的uri
	public String uri;
	
	//图片缓存地址
	public String imageCache;
	//来源
	public String author;
	//后来添加，用来从数据库里取出正文内容
	public String articleId;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "News:title="+title+",abs="+abs+",date="+date+",imageUri="+imageUri+",uri="+uri+",imageCache="+imageCache;
	}
	public String formatTime(){
		if(date == null || date.equals("null")){
			return null;
		}
		DateFormat df = new DateFormat();
		String time = (String) df.format("yyyy-MM-dd", Long.parseLong(date));
		return time;
		
	}
}
