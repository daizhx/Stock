package com.hengxuan.stock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
public class MinuteAmountView extends SurfaceView implements SurfaceHolder.Callback {

    private TextPaint mTextPaint;
    private float[] priceArray;
    private int[] volumnArray;
    private Paint drawPaint;
    private int width;
    private int height;
    private int contentWidth;
    private int contentHeight;
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

        new DrawThread().start();
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

        new DrawThread().start();
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
                if(x > width || x <0){
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
        drawPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        drawPaint.setColor(Color.BLACK);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(2);
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    private void invalidateTextPaintAndMeasurements() {

    }

    private void invalidateDrawPaint(){

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int h = getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);
        TextView textview = (TextView) LayoutInflater.from(context).inflate(R.layout.minute_chart_label_text,null);
        textview.setText("0");
        textview.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h2 = textview.getMeasuredHeight();
        width = w;
        height = h + h2;
        setMeasuredDimension(width, height);
        contentWidth = width;
        contentHeight = height - h2;
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        if(isShowLabelLine){
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeWidth(2);
            drawPaint.setColor(Color.BLACK);
            canvas.drawLine(xPoint,0,xPoint,contentHeight,drawPaint);
        }

    }

    private void doDraw(){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        new DrawThread().start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private class DrawThread extends Thread{
        @Override
        public void run() {
            synchronized (MinuteAmountView.this){
                Canvas canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                drawPaint.setColor(Color.BLACK);
                canvas.drawRect(0, 0, contentWidth - 1, contentHeight, drawPaint);
                drawPaint.setColor(Color.GRAY);
                canvas.drawLine(0, (float) contentHeight / 2, contentWidth, (float) contentHeight / 2, drawPaint);
                canvas.drawLine((float) contentWidth / 4, 0, (float) contentWidth / 4, contentHeight, drawPaint);
                canvas.drawLine((float) contentWidth * 2 / 4, 0, (float) contentWidth * 2 / 4, contentHeight, drawPaint);
                canvas.drawLine((float) contentWidth * 3 / 4, 0, (float) contentWidth * 3 / 4, contentHeight, drawPaint);

                int max = getMaxValue();

                wunit = (float) contentWidth / TIME;
                if (max != 0) {
                    hunit = (float) contentHeight / max;
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

                        canvas.drawLine(i * wunit, contentHeight, i * wunit, contentHeight - volumnArray[i] * hunit, drawPaint);
                    }
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
