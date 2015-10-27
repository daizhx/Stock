package com.hengxuan.stock.application;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
				.Builder(this)
		.memoryCacheExtraOptions(480, 800)//保存的每个人缓存文件的最大长宽
		.threadPoolSize(4)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.memoryCache(new UsingFreqLimitedMemoryCache(2*1024*1024))
		.memoryCacheSize(2*1024*1024)
		.discCacheSize(50*1024*1024)
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.discCacheFileCount(100)//缓存的文件数量
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
		.imageDownloader(new BaseImageDownloader(this,5*1000,30*1000))
		.build();
		
		ImageLoader.getInstance().init(configuration);
	}
}
