package com.hengxuan.stock.utils;

public class Log {
	public static final String TAG = "stock";
	public static final boolean DEBUG = true;
	public static void d(String msg){
		if(DEBUG){
			android.util.Log.d(TAG, msg);
		}
	}

    public static void d(String tag,String msg){
        if(DEBUG){
            android.util.Log.d(tag, msg);
        }
    }

    public static void e(String msg){
        if(DEBUG){
            android.util.Log.d(TAG, msg);
        }
    }

    public static void e(String tag,String msg){
        if(DEBUG){
            android.util.Log.d(tag, msg);
        }
    }

    public static void i(String msg){
        if(DEBUG){
            android.util.Log.d(TAG, msg);
        }
    }

    public static void i(String tag,String msg){
        if(DEBUG){
            android.util.Log.d(tag, msg);
        }
    }
    public static void w(String msg){
        if(DEBUG){
            android.util.Log.d(TAG, msg);
        }
    }

    public static void w(String tag,String msg){
        if(DEBUG){
            android.util.Log.d(tag, msg);
        }
    }
}
