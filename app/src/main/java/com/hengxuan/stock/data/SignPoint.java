package com.hengxuan.stock.data;

/**
 * Created by Administrator on 2015/10/26.
 */
public class SignPoint {
    public static final int BUY = 1;
    public static final int SELL = 0;

    public int hour;
    public int minut;
    private int whichSign = -1;

    public SignPoint(int h,int m){
        hour = h;
        minut = m;
    }

    public void setSignId(int i){
        whichSign = i;
    }
    public int getSignId(){
        return whichSign;
    }
}
