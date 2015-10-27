package com.hengxuan.stock.model;

import java.sql.Date;

import android.text.format.DateFormat;

public class News {
	public String title;
	//ժҪ
	public String abs;
	public String date;
	public String imageUri;
	//�����ļ�html��uri
	public String uri;
	
	//ͼƬ�����ַ
	public String imageCache;
	//��Դ
	public String author;
	//������ӣ����������ݿ���ȡ����������
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
