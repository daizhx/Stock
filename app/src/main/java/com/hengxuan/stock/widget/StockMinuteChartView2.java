package com.hengxuan.stock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.hengxuan.stock.R;
import com.hengxuan.stock.application.Constants;
import com.hengxuan.stock.data.SignPoint;
import com.hengxuan.stock.utils.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.CheckedOutputStream;

/**
 * TODO: document your custom view class.
 */
public class StockMinuteChartView2 extends SurfaceView implements SurfaceHolder.Callback {
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...

    private SurfaceHolder mHolder;
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private float mTextBottom;
    private String mText;

    private ArrayList rawDataList;
    private float[][] data;
    private float widthUnit;
    private float heightUnit;
    private float yBaseValue;
    private float yHighValue;
    private float yLowValue;
    private float ratio;
    private static final int TIME = 240 + 1;
    private Path mPath;
    private Paint mPaint;

    private boolean isShowInterSection = false;
    private float xPoint;
    private float yPoint;
    private GestureDetector gestureDetector;
    private Context context;
    private int textSize = 24;
    private int width;
    private int height;

    private int contentWidth;
    private int contentHeight;
    private int contentLeft;
    private int contentTop;
    private int contentRight;
    private int contentBottom;

    private SignPoint[] signBSPoints;

    public void setSignBSPoints(SignPoint[] c){
        signBSPoints = c;
    }

    private void setUpData(double base,double[] prices){
        yBaseValue = (float) base;
        float range = calcuRange(prices);
        yHighValue = range + yBaseValue;
        yLowValue = yBaseValue - range;
        ratio = ((yHighValue - yBaseValue)/yBaseValue)*100;
        widthUnit = (float)contentWidth/TIME;
        heightUnit = (float)contentHeight/(yHighValue - yLowValue);
        data = new float[prices.length][2];
        for(int i=0;i<prices.length;i++){
            data[i][0] = (float) (i*widthUnit);
            data[i][1] = (float) ((yHighValue - prices[i])*heightUnit) + contentTop;
        }
//        new DrawThread().start();
    }

    public void setData(float base,ArrayList arrayList){
        yBaseValue = base;
        rawDataList = arrayList;
    }
    public void setDataAndRefresh(float base,ArrayList arrayList){
        yBaseValue = base;
        rawDataList = arrayList;
        new DrawThread().start();
    }
    private void setUpData(float base,ArrayList prices){
        yBaseValue = base;
        float range = calcuRange(prices);
        yHighValue = range + yBaseValue;
        yLowValue = yBaseValue - range;
        ratio = ((yHighValue - yBaseValue)/yBaseValue)*100;
        widthUnit = (float)contentWidth/TIME;
        heightUnit = (float)contentHeight/(yHighValue - yLowValue);
        data = new float[prices.size()][2];
        for(int i=0;i<prices.size();i++){
            data[i][0] = (float) (i*widthUnit);
            data[i][1] = (float) ((yHighValue - (float)prices.get(i))*heightUnit) + contentTop;
        }
//        new DrawThread().start();
    }

    private float calcuRange(ArrayList prices){
        float ret = 0;
        for(int i=0;i<prices.size();i++){
            float f = Math.abs((float)prices.get(i) - yBaseValue);
            if(f > ret){
                ret = f;
            }
        }
        return ret;
    }

    private float calcuRange(double[] prices){
        float ret = 0;
        for(int i=0;i<prices.length;i++){
            float f = (float) Math.abs(prices[i] - yBaseValue);
            if(f > ret){
                ret = f;
            }
        }
        return ret;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        if(isShowInterSection){
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(1);
            canvas.drawLine(xPoint, contentTop, xPoint, contentBottom, mPaint);
            canvas.drawLine(0, yPoint, contentWidth, yPoint, mPaint);
            TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.minute_chart_label_text,null);
            textview.setDrawingCacheEnabled(true);
            textview.setText(String.valueOf(yHighValue - (yPoint-contentTop) / heightUnit));
            textview.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int w = textview.getMeasuredWidth();
            int h = textview.getMeasuredHeight();
            textview.layout(0, 0, w, h);
            Bitmap bitmap = textview.getDrawingCache();
            if(xPoint > contentWidth/2){
                canvas.drawBitmap(bitmap,0,yPoint - h/2,mPaint);
            }else {
                canvas.drawBitmap(bitmap, contentWidth - w, yPoint - h / 2, mPaint);
            }

        }

        if(signBSPoints != null && signBSPoints.length > 0){
            for(int i=0;i<signBSPoints.length;i++){
                SignPoint signBSPoint = signBSPoints[i];
                int hour = signBSPoint.hour;
                int minute = signBSPoint.minut;
                int xStep =0;
                if((9 == hour && minute >= 30) || 10 == hour || (11 == hour && minute <= 30)) {
                    xStep = (hour - 9) * 60 + minute - 30;
                }
                if(13 <= hour && hour <= 15){
                    xStep = 120 + (hour - 13)*60 + minute + 1;//+1是让13点从11:30的位置偏移一个步长开始
                }
                int id = signBSPoint.getSignId();
                Bitmap bitmap = null;
                if(id == SignPoint.BUY) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.buy);
                }else if(id == SignPoint.SELL){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sell);
                }
                float x = xStep*widthUnit;
                float y = data[xStep][1];
                if(bitmap != null) {
                    canvas.drawBitmap(bitmap,x,y,mPaint);
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int h = getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);
        TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.minute_chart_label_text,null);
        Log.d(String.valueOf(yHighValue - yPoint / heightUnit));
        textview.setText(String.valueOf(yHighValue - yPoint / heightUnit));
        textview.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h2 = textview.getMeasuredHeight();
        width = w;
        height = h + h2;
        setMeasuredDimension(width, height);
        contentLeft = 0;
        contentTop = h2/2;
        contentRight = width;
        contentBottom = height - h2/2;
        contentWidth = width;
        contentHeight = height - h2;
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    //surface draw
    public void doDraw(Canvas canvas){
        if(canvas == null)return;
        Log.d("surface drawing...");
        //draw bg or else is black
        canvas.drawColor(Color.WHITE);
        //draw grid
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(contentLeft, contentTop, contentRight - 1, contentBottom, mPaint);
        for(int i=1;i<4;i++){
            float y = (contentHeight/4f)*i;
            float x = (contentWidth/4f)*i;
            canvas.drawLine(0,y+contentTop,contentWidth,y+contentTop,mPaint);
            canvas.drawLine(x,contentTop,x,contentBottom,mPaint);
        }
        if(rawDataList != null && !rawDataList.isEmpty()){
            setUpData(yBaseValue,rawDataList);
        }
//        setUpData(20.45, testData);//test code

        //draw number text
        mText = String.format("%.2f",yHighValue);
        invalidateTextPaintAndMeasurements(Color.RED);
        canvas.drawText(mText, 0, mTextHeight+contentTop, mTextPaint);

        mText = String.format("%.2f",ratio)+"%";
        invalidateTextPaintAndMeasurements();
        canvas.drawText(mText, contentWidth - mTextWidth, mTextHeight+contentTop, mTextPaint);

        mText = String.format("%.2f",yBaseValue);
        invalidateTextPaintAndMeasurements(Color.GRAY);
        canvas.drawText(mText, 0, (height + mTextHeight) / 2 - mTextBottom, mTextPaint);

        mText = String.format("%.2f",yLowValue);
        invalidateTextPaintAndMeasurements(Color.GREEN);
        canvas.drawText(mText, 0, contentBottom - mTextBottom, mTextPaint);

        mText = String.format("%.2f",ratio)+"%";
        invalidateTextPaintAndMeasurements();
        canvas.drawText(mText, contentWidth - mTextWidth, contentBottom - mTextBottom, mTextPaint);

        //draw the minute line
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        if(data != null && data.length >= 1) {
            mPath.moveTo(data[0][0], data[0][1]);
            for (int i = 1; i < data.length; i++) {
//                mPath.lineTo(data[i][0], data[i][1]);
                mPath.quadTo(data[i-1][0],data[i-1][1],data[i][0], data[i][1]);
            }
            canvas.drawPath(mPath, mPaint);
        }
    }

    public void refreshDraw(){
        new DrawThread().start();
    }

    public StockMinuteChartView2(Context context) {
        super(context);
        init(null, 0);
    }

    public StockMinuteChartView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public StockMinuteChartView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        mHolder = getHolder();
        mHolder.addCallback(this);

        gestureDetector = new GestureDetector(context,new MyGestureListener());
    }

    private void invalidateTextPaintAndMeasurements() {
//        mTextPaint.setTextSize(mExampleDimension);
//        mTextPaint.setColor(mExampleColor);
        if(mText != null) {
            mTextWidth = mTextPaint.measureText(mText);

            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            mTextHeight = fontMetrics.bottom - fontMetrics.top;
            mTextBottom = fontMetrics.bottom;
        }
    }

    private void invalidateTextPaintAndMeasurements(int color,float size) {
        mTextPaint.setTextSize(size);
        mTextPaint.setColor(color);
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements(int color){
        invalidateTextPaintAndMeasurements(color,textSize);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        Log.d("surface create");
    }

    public void drawBSSign(int hour,int minute){

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("surface surfaceChanged");
        new DrawThread().start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("surfaceDestroyed");
    }

    private class DrawThread extends Thread{
        @Override
        public void run() {
            synchronized (StockMinuteChartView2.this){
                Canvas canvas = mHolder.lockCanvas();
                doDraw(canvas);
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * show every minut point value
     * x is the touching x point
     */
    public void showInterSection(boolean show,float x) {
        Log.d("show intersection:"+show+",x="+x);
        if(show && data != null) {
            int num = (int) (x / widthUnit);
            if(num < data.length) {
                xPoint = data[num][0];
                yPoint = data[num][1];
            }
        }
        isShowInterSection = show;
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int id = event.getPointerId(0);
        float x = 0,y;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = event.getX(id);
                showInterSection(true,x);
            case MotionEvent.ACTION_MOVE:
                x = event.getX(id);
                Log.d("x="+x);
                if(x > width || x <0){
                    return false;
                }
                if(Math.abs(x - xPoint) >= widthUnit){
                    showInterSection(true,x);
                }
                break;
            case MotionEvent.ACTION_UP:
                showInterSection(false,0);
                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.d("out side...");
                showInterSection(false,0);
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("action cancel");
                showInterSection(false,0);
                break;
            default:
                break;
        }
        return true;
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(distanceX >= widthUnit){
                int id = e2.getPointerId(0);
                float x = e2.getX();
                Log.d("onScroll...");
                showInterSection(true,x);
            }
            return true;
        }

    }
}
