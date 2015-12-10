package com.hengxuan.stock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.hengxuan.stock.R;
import com.hengxuan.stock.utils.Log;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class MinuteAmountView extends DataChartView{

    private TextPaint mTextPaint;
    private float[] priceArray;
    private int[] volumnArray;
    private Paint drawPaint;
    private Context context;
    private SurfaceHolder surfaceHolder;
    private static final int TIME = 240 + 1;
    private float closePrice;

    private float xPoint;
    private float yPoint;
    private float wunit;
    private float hunit;
    private boolean isShowLabelLine = false;

    public void setDataAndInvalidate(float closePrice,float[] prices,int[] volumn){
        if(prices.length != volumn.length || closePrice == 0){
            Log.i("data is wrong");
            return;
        }
        this.closePrice = closePrice;
        priceArray = prices;
        volumnArray = volumn;
        invalidate();
    }

    public void setDataAndInvalidate(float closePrice,ArrayList prices,ArrayList volumn){
        if(prices.size() != volumn.size() || closePrice == 0){
            Log.i("data is wrong");
            return;
        }
        this.closePrice = closePrice;
        int length = prices.size();
        priceArray = new float[length];
        volumnArray = new int[length];
        for(int i=0;i<length;i++){
            priceArray[i] = (float)prices.get(i);
            if(i == 0){
                volumnArray[i] = (int)volumn.get(i);
            }else {
                volumnArray[i] = (int) volumn.get(i) - (int)volumn.get(i-1);
            }
        }
        invalidate();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int id = event.getPointerId(0);
        float x = 0,y;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = event.getX(id);
                drawLabelLine(true, x);
            case MotionEvent.ACTION_MOVE:
                x = event.getX(id);
                Log.d("x="+x);
                if(x > getWidth() || x <0){
                    return false;
                }
                if(Math.abs(x - xPoint) >= wunit){
                    drawLabelLine(true, x);
                }
                break;
            case MotionEvent.ACTION_UP:
                drawLabelLine(false, 0);
                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.d("out side...");
                drawLabelLine(false,0);
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("action cancel");
                break;
            default:
                break;
        }
        return true;
    }

    private void drawLabelLine(boolean b, float x) {
        if(b){
            xPoint = x;
            invalidate();
        }
        redraw_type = TOUCH_REDRAW;
    }

    private int getMaxValue(){
        int max =0;
        if(volumnArray != null) {
            for (int volum : volumnArray) {
                if (volum > max) {
                    max = volum;
                }
            }
        }
        return max;
    }


    public MinuteAmountView(Context context) {
        super(context);
        init(null, 0);
        this.context = context;
    }

    public MinuteAmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
        this.context = context;
    }

    public MinuteAmountView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        this.context = context;
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        drawPaint = new Paint();
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

    }

    private void invalidateTextPaintAndMeasurements() {

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int h = getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);
        TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.minute_chart_label_text,null);
        textview.setText("0");
        textview.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h2 = textview.getMeasuredHeight();
        setMeasuredDimension(w, h+h2);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

        if(redraw_type == TOUCH_REDRAW){
            canvas.drawBitmap(dataChart, paddingLeft, paddingTop, drawPaint);
            if(isShowLabelLine){
                drawPaint.setStyle(Paint.Style.STROKE);
                drawPaint.setStrokeWidth(2);
                drawPaint.setColor(Color.BLACK);
                canvas.drawLine(xPoint, 0, xPoint, contentHeight, drawPaint);
                redraw_type = -1;
            }
        }else {
            dataChart = createDataChart(contentWidth,contentHeight);
            canvas.drawBitmap(dataChart, paddingLeft, paddingTop, drawPaint);
        }
    }

    private Bitmap createDataChart(int w,int h){
        Bitmap bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        drawPaint.setColor(Color.BLACK);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(1);
        drawPaint.setAntiAlias(true);
        canvas.drawRect(0, 0, w, h, drawPaint);
        drawPaint.setColor(Color.GRAY);
        canvas.drawLine(0, (float) h / 2, w, (float) h / 2, drawPaint);
        canvas.drawLine((float) w / 4, 0, (float) w / 4, h, drawPaint);
        canvas.drawLine((float) w * 2 / 4, 0, (float) w * 2 / 4, h, drawPaint);
        canvas.drawLine((float) w * 3 / 4, 0, (float) w * 3 / 4, h, drawPaint);

        int max = getMaxValue();

        wunit = (float) w / TIME;
        if (max != 0) {
            hunit = (float) h / max;
        }
        drawPaint.setStrokeWidth(wunit);
        if (priceArray != null) {
            if (priceArray[0] > closePrice) {
                drawPaint.setColor(Color.RED);
            } else if (priceArray[0] < closePrice) {
                drawPaint.setColor(Color.GREEN);
            } else {
                drawPaint.setColor(Color.GRAY);
            }
//                    canvas.drawLine(0 * wunit, -contentHeight, 0 * wunit, volumnArray[0] * hunit, drawPaint);
            for (int i = 0; i < volumnArray.length; i++) {
                if (i == 0) {
                    if (priceArray[i] > closePrice) {
                        drawPaint.setColor(Color.RED);
                    } else if (priceArray[i] < closePrice) {
                        drawPaint.setColor(Color.GREEN);
                    } else {
                        drawPaint.setColor(Color.GRAY);
                    }
                } else {
                    if (priceArray[i] > priceArray[i - 1]) {
                        drawPaint.setColor(Color.RED);
                    } else if (priceArray[i] < priceArray[i - 1]) {
                        drawPaint.setColor(Color.GREEN);
                    } else {
                        drawPaint.setColor(Color.GRAY);
                    }
                }

                canvas.drawLine(i * wunit+1, h-1, i * wunit, h - volumnArray[i] * hunit, drawPaint);
            }
        }
        return bitmap;
    }
}
