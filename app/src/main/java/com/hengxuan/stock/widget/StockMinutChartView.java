package com.hengxuan.stock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.hengxuan.stock.R;

/**
 * TODO: document your custom view class.
 */
public class StockMinutChartView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private float mTextBottom;
    private String mText;

    private float[][] data;
    private float widthUnit;
    private float heightUnit;
    private float yBaseValue;
    private float yHighValue;
    private float yLowValue;
    private float ratio;
    private static final int TIME = 240;
    private Path mPath;
    private Paint mPaint;

    private double[] testData = new double[]{20.80,21.00,20.80,20.61,21.00,21.20,21.06,21.10,21.13,21.11,
            21.05,21.10,21.03,21.00,21.10,21.00,20.93,20.68,20.93,20.81,20.92,20.81,20.85,20.80,20.79,20.75,20.79,20.90,20.94
            ,20.94,20.93,20.95,21.01,21.13,21.28,21.40,21.26,21.32,21.40,21.41,21.32,21.40,21.37,21.44,21.48,21.58,21.58,21.40,21.39,
            21.47,21.56,21.55,21.57,21.42,21.38,21.35,21.47,21.50,21.48,21.37,21.45,21.40,21.47,21.50,21.59,21.60,21.60,21.57,21.55,21.55,
            21.54,21.55,21.67,21.70,21.71,21.71,21.70,21.59,21.40,21.50,21.60,21.65,21.50,21.39,21.40,21.45,21.51,21.48,21.40,21.40,21.37,21.40,21.38,
            21.48,21.50,21.40,21.40,21.34,21.30,21.32,21.31,21.35,21.30,21.30,21.15,21.03,21.02,21.08,21.11,21.14,21.05,21.00,21.00,20.90,20.85,20.90,20.99,
            21.00,21.15,21.18,21.18,21.35,21.35,21.21,21.34,21.20,21.19,21.10,21.03,21.08,21.20,21.20,21.10,21.10,21.05,21.00,21.05,21.10,21.15,21.12,21.11,21.10,
            21.10,21.10,21.06,21.02,21.04,21.06,21.10,21.07,21.10,21.09,21.17,21.07,21.02,20.92,20.92,20.98,21.01,21.03,21.00,21.00,20.99,20.92,20.90,20.94,20.99,21.00,
            21.04,21.06,21.10,21.20,21.33,21.36,21.30,21.26,21.25,21.22,21.25,21.50,21.50,21.61,21.67,21.53,21.47,21.50,21.60,21.70,21.76,21.81,21.90,21.85,21.65,21.79,21.87,
            21.96,22.01,22.20,22.10,22.01,22.19,22.45,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50
            ,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50,22.50};

    public void setUpData(double base,double[] prices){
        yBaseValue = (float) base;
        yHighValue = findMaxValue(prices);
        yLowValue = yBaseValue - (yHighValue - yBaseValue);
        ratio = ((yHighValue - yBaseValue)/yBaseValue)*100;
        int w = getWidth();
        int h = getHeight();
        widthUnit = (float)w/TIME;
        heightUnit = (float)h/(yHighValue - yLowValue);
        data = new float[prices.length][2];
        for(int i=0;i<prices.length;i++){
            data[i][0] = (float) (i*widthUnit);
            data[i][1] = (float) ((yHighValue - prices[i])*heightUnit);
        }
    }

    private float findMaxValue(double[] prices){
        float ret = 0;
        for(int i=0;i<prices.length;i++){
            float f = (float) Math.abs(prices[i]);
            if(f > ret){
                ret = f;
            }
        }
        return ret;
    }

    public StockMinutChartView(Context context) {
        super(context);
        init(null, 0);
    }

    public StockMinutChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public StockMinutChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.StockMinutChartView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.StockMinutChartView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.StockMinutChartView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.StockMinutChartView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.StockMinutChartView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.StockMinutChartView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
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
        invalidateTextPaintAndMeasurements(color);
    }

    private void invalidateTextPaintAndMeasurements(int color){
        mTextPaint.setColor(color);
        invalidateTextPaintAndMeasurements();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        mWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        mHeight = getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);
//        setMeasuredDimension(mWidth,mHeight);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
//        int paddingLeft = getPaddingLeft();
//        int paddingTop = getPaddingTop();
//        int paddingRight = getPaddingRight();
//        int paddingBottom = getPaddingBottom();
//
//        int contentWidth = getWidth() - paddingLeft - paddingRight;
//        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
//        canvas.drawText(mExampleString,
//                paddingLeft + (contentWidth - mTextWidth) / 2,
//                paddingTop + (contentHeight + mTextHeight) / 2,
//                mTextPaint);

        // Draw the example drawable on top of the text.
//        if (mExampleDrawable != null) {
//            mExampleDrawable.setBounds(paddingLeft, paddingTop,
//                    paddingLeft + contentWidth, paddingTop + contentHeight);
//            mExampleDrawable.draw(canvas);
//        }

        int width = getWidth();
        int height = getHeight();
        //draw grid
        for(int i=1;i<4;i++){
            float y = (height/4f)*i;
            float x = (width/4f)*i;
            canvas.drawLine(0,y,width,y,mPaint);
            canvas.drawLine(x,0,x,height,mPaint);
        }

        setUpData(20.45, testData);//test code

        //draw number text
        mText = String.format("%.2f",yHighValue);
        invalidateTextPaintAndMeasurements(Color.RED);
        canvas.drawText(mText, 0, mTextHeight, mTextPaint);

        mText = String.format("%.2f",ratio)+"%";
        invalidateTextPaintAndMeasurements();
        canvas.drawText(mText, width - mTextWidth, mTextHeight, mTextPaint);

        mText = String.format("%.2f",yBaseValue);
        invalidateTextPaintAndMeasurements(Color.GRAY);
        canvas.drawText(mText, 0, (height + mTextHeight) / 2 - mTextBottom, mTextPaint);

        mText = String.format("%.2f",yLowValue);
        invalidateTextPaintAndMeasurements(Color.GREEN);
        canvas.drawText(mText, 0, height - mTextBottom, mTextPaint);

        mText = String.format("%.2f",ratio)+"%";
        invalidateTextPaintAndMeasurements();
        canvas.drawText(mText, width - mTextWidth,height - mTextBottom,mTextPaint);

        //draw the minute line
        if(data.length >= 1) {
            mPath.moveTo(data[0][0], data[0][1]);
            for (int i = 1; i < data.length; i++) {
                mPath.lineTo(data[i][0], data[i][1]);
            }
            canvas.drawPath(mPath, mPaint);
        }

    }

    public void fun(){

    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
