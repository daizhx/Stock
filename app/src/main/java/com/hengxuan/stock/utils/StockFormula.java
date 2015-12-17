package com.hengxuan.stock.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/9/28.
 */
public class StockFormula {

    /**
     * get exchange code on stock code
     * @param code
     * @returny
     */
    public static String hostExchange(String code){
        if(code.startsWith("60")){
            return "sh";
        }else{
            return "sz";
        }
    }

    /**
     * ��������5������ί��
     * @param buy
     * @param sell
     * @return
     */
    public static String calcWB(float[] buy,float[] sell){
        float totalbuy = 0;
        float totalsell = 0;
        for(int i=0;i<5;i++){
            totalbuy += buy[i];
            totalsell += sell[i];
        }
        float wb = (totalbuy - totalsell)/(totalbuy + totalsell);
        return String.valueOf(wb);
    }

    /**
     * ���ݳɽ��������ֵ���㻻����
     * @param tradeVolumn �ɽ��� ����
     * @param totalVolumn ����ֵ���ڣ�
     * @return
     */
    public static float calcExhangePercentage(long tradeVolumn,long totalVolumn){
        return tradeVolumn/(totalVolumn*10000);
    }

    public static boolean isOpenTime(){
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        if(Calendar.SUNDAY < calendar.get(Calendar.DAY_OF_WEEK) && calendar.get(Calendar.DAY_OF_WEEK) < Calendar.SATURDAY){
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            if(9 <= hour && hour < 15){
                if(hour==9 && minute <30){
                    return false;
                }else if(hour==11 && minute > 30){
                    return false;
                }else {
                    return true;
                }

            }
        }

        return false;
    }
}
