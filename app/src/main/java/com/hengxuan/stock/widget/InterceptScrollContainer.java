package com.hengxuan.stock.widget;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hengxuan.stock.utils.Log;

/**
 * Created by Administrator on 2015/9/30.
 */
public class InterceptScrollContainer extends LinearLayout implements GestureDetector.OnGestureListener{

    GestureDetectorCompat detector;
    private HorizontalScrollView view1;
    private ListView view2;
    private boolean flag = false;
    private int orientation = -1;
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;

    public InterceptScrollContainer(Context context) {
        super(context);
        detector = new GestureDetectorCompat(context,this);
    }

    public InterceptScrollContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        detector = new GestureDetectorCompat(context,this);
    }

    public InterceptScrollContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        detector = new GestureDetectorCompat(context,this);
    }

    public void setHockView(HorizontalScrollView v1,ListView v2){
        view1 = v1;
        view2 = v2;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
//        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
//        return super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        flag = true;
        view2.onTouchEvent(e);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("onSingleTapUp");
        view2.onTouchEvent(e);
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(flag){
            if(Math.abs(distanceX) > Math.abs(distanceY)){
                orientation = HORIZONTAL;
            }else {
                orientation = VERTICAL;
            }
            flag = false;
        }
        if(orientation == HORIZONTAL){
            view1.smoothScrollBy((int) distanceX, 0);
        }else if(orientation == VERTICAL){
            view2.smoothScrollBy((int)distanceY,0);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}
