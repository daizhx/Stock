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
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
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
public class StockMinutChartView extends View{
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
//    private int width;
//    private int height;
//
//    private int contentWidth;
//    private int contentHeight;
//    private int contentLeft;
//    private int contentTop;
//    private int contentRight;
//    private int contentBottom;

    private SignPoint[] signBSPoints;

    public void setSignBSPoints(SignPoint[] c){
        signBSPoints = c;
    }

    private int redraw_type = -1;
    private static final int TOUCH_REDRAW = 0;
    private static final int DATA_UPDATE_REDRAW = 1;

    private Bitmap dataChart;


    public void setData(float base,ArrayList arrayList){
        yBaseValue = base;
        rawDataList = arrayList;
    }
    public void setDataAndRefresh(float base,ArrayList arrayList){
        yBaseValue = base;
        rawDataList = arrayList;
        redraw_type = DATA_UPDATE_REDRAW;
        invalidate();
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
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        if(dataChart == null || redraw_type == DATA_UPDATE_REDRAW){
            dataChart = createDataChart(contentWidth,contentHeight);
        }
        canvas.drawBitmap(dataChart,paddingLeft,paddingTop,mPaint);

        if(redraw_type == TOUCH_REDRAW && isShowInterSection){
//            canvas.drawColor(Color.TRANSPARENT);
//            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(1);
            canvas.drawLine(xPoint, paddingTop, xPoint, paddingTop+contentHeight, mPaint);
            canvas.drawLine(paddingLeft, yPoint, paddingLeft+contentWidth, yPoint, mPaint);
            TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.minute_chart_label_text,null);
            textview.setDrawingCacheEnabled(true);
            textview.setText(String.valueOf(yHighValue - (yPoint-paddingTop) / heightUnit));
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
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outHeight=10;
                options.outWidth=10;
                options.inJustDecodeBounds = false;
                options.inScaled = true;
                if(id == SignPoint.BUY) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.buy,options);
                }else if(id == SignPoint.SELL){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sell,options);
                }
                Log.d("bitmap.w="+bitmap.getWidth()+",h="+bitmap.getHeight());
                float x = xStep*widthUnit;
                float y = data[xStep][1];
                if(bitmap != null) {
                    canvas.drawBitmap(bitmap,x,y,mPaint);
                }
            }
        }
        redraw_type = -1;
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
        setMeasuredDimension(w, h+h2);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    public Bitmap createDataChart(int w,int h){
        Bitmap bitmap= Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //draw bg or else is black
        canvas.drawColor(Color.WHITE);
        //draw grid
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        canvas.drawRect(0, 0, w, h, mPaint);
        for(int i=1;i<4;i++){
            float y = (h/4f)*i;
            float x = (w/4f)*i;
            canvas.drawLine(0,y,w,y,mPaint);
            canvas.drawLine(x,0,x,h,mPaint);
        }

        //setup data
        if(rawDataList != null) {
            float range = calcuRange(rawDataList);
            yHighValue = range + yBaseValue;
            yLowValue = yBaseValue - range;
            ratio = ((yHighValue - yBaseValue) / yBaseValue) * 100;
            widthUnit = (float) w / TIME;
            heightUnit = (float) h / (yHighValue - yLowValue);
            Log.d("widthUnit="+widthUnit);
            data = new float[rawDataList.size()][2];
            for (int i = 0; i < rawDataList.size(); i++) {
                data[i][0] = (float) (i * widthUnit);
                data[i][1] = (float) ((yHighValue - (float) rawDataList.get(i)) * heightUnit);
            }
        }

        //draw number text
        mText = String.format("%.2f",yHighValue);
        invalidateTextPaintAndMeasurements(Color.RED);
        canvas.drawText(mText, 0, mTextHeight, mTextPaint);

        mText = String.format("%.2f",ratio)+"%";
        invalidateTextPaintAndMeasurements();
        canvas.drawText(mText, w - mTextWidth, mTextHeight, mTextPaint);

        mText = String.format("%.2f",yBaseValue);
        invalidateTextPaintAndMeasurements(Color.GRAY);
        canvas.drawText(mText, 0, (h + mTextHeight) / 2 - mTextBottom, mTextPaint);

        mText = String.format("%.2f",yLowValue);
        invalidateTextPaintAndMeasurements(Color.GREEN);
        canvas.drawText(mText, 0, h - mTextBottom, mTextPaint);

        mText = String.format("%.2f",ratio)+"%";
        invalidateTextPaintAndMeasurements();
        canvas.drawText(mText, w - mTextWidth, h - mTextBottom, mTextPaint);

        //draw the minute line
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);


        if (data != null && data.length >= 1) {
            mPath.moveTo(data[0][0], data[0][1]);
            for (int i = 1; i < data.length; i++) {
//                mPath.lineTo(data[i][0], data[i][1]);
                mPath.quadTo(data[i - 1][0], data[i - 1][1], data[i][0], data[i][1]);
            }
            canvas.drawPath(mPath, mPaint);
        }
        return bitmap;
    }


    public StockMinutChartView(Context context) {
        super(context);
        init(null, 0);
    }

    public StockMinutChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public StockMinutChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();


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


    public void drawBSSign(int hour,int minute){

    }


    /**
     * show every minut point value
     * x is the touching x point
     */
    public void showInterSection(boolean show,float x) {
        Log.d("show intersection:"+show+",x="+x);
        if(show && rawDataList != null) {
            int num = (int) (x / widthUnit);
            if(num < data.length) {
                xPoint = data[num][0];
                yPoint = data[num][1];
            }
        }
        isShowInterSection = show;
        redraw_type = TOUCH_REDRAW;
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
                if(x > getWidth() || x <0){
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
