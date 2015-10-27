package com.hengxuan.stock.widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import com.hengxuan.stock.utils.DecimalUtil;
import com.hengxuan.stock.utils.Log;

import android.R;
import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;


public class ScrollTextView extends TextView {
	long animationTime = 100L;
	ValueAnimator animator;
	int fadeLength = 60;
	float fontHeight;
	private int height;
	float initTextSize;
	boolean isAnimateEnd = true;
	Matrix matrix = new Matrix();
	private float midX;
	private float offsetY;
	private OnScrollEndListener onScrollEndListener;
	Paint paint;
	Shader shader_bottom;
	Paint shader_paint_top;
	Shader shader_top;
	ArrayList<String> string_ArrayList = new ArrayList();
	float textBaseY;
	int textColor = Color.BLACK;
	private int width;
	CharSequence defaultString;
	float padding;
	Paint shader_paint;

	public ScrollTextView(Context paramContext) {
		super(paramContext);
		init();
	}

	public ScrollTextView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		int count = attributeSet.getAttributeCount();
		int resourceId = attributeSet.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textColor", 0);
		if(resourceId > 0){
			textColor = context.getResources().getColor(resourceId);
		}
		init();
	}

	public ScrollTextView(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		textColor = paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textColor", Color.WHITE);
		init();
	}

	private void init() {
		this.initTextSize = getTextSize();
		this.paint = getPaint();
		this.paint.setTextAlign(Paint.Align.LEFT);
//		paint.setColor(Color.WHITE);
		this.paint.setColor(this.textColor);
		this.shader_paint_top = new Paint();

		this.fadeLength = getVerticalFadingEdgeLength();
		int color = Color.rgb(0xe9, 0x30, 0x30);
		this.shader_top = new LinearGradient(0.0F, 0.0F, 0.0F, this.fadeLength,
				color, 0, Shader.TileMode.CLAMP);
//		this.shader_paint_top.setShader(this.shader_top);
		shader_paint_top.setStyle(Style.FILL);
		shader_paint_top.setColor(color);
//		this.shader_paint_top.setXfermode(new PorterDuffXfermode(
//				PorterDuff.Mode.DST_OUT));
		this.fontHeight = getFontHeight();
		defaultString = getText();
		if(defaultString == "" || defaultString == null){
			defaultString = "0.00";
		}
		textBaseY = getFontHeight();
		setText(defaultString);
		
		shader_paint = new Paint();
		shader_paint.setStyle(Style.FILL);
		shader_paint.setColor(0xe93030);
		shader_paint.setXfermode(new PorterDuffXfermode(
				PorterDuff.Mode.DST_OUT));
		
	}

	public float getFontHeight() {
		Rect localRect = new Rect();
		this.paint.getTextBounds("0", 0, 1, localRect);
		this.fontHeight = localRect.bottom - localRect.top;
		return this.fontHeight;
	}

	protected void onDraw(Canvas canvas) {
		textBaseY = ((this.height + this.fontHeight) / 2.0F);
		
		this.matrix.reset();
        this.shader_top.setLocalMatrix(this.matrix);
		
        int gap = 0;
        if(offsetY == fontHeight){
        	gap = -2;
        }
        
		canvas.drawText(defaultString.toString(), 0, textBaseY-offsetY + gap, paint);
		canvas.drawText(defaultString.toString(), 0, textBaseY+(fontHeight - offsetY), paint);
		
		//top
		canvas.drawRect(0, 0, width, padding, shader_paint_top);
		//bottom
		canvas.drawRect(0, padding+fontHeight+2, width, height, shader_paint_top);
	}

	protected void onMeasure(int paramInt1, int paramInt2) {
		super.onMeasure(paramInt1, paramInt2);
		this.width = getMeasuredWidth();
		this.midX = (0.5F * getMeasuredWidth());
		this.height = getMeasuredHeight();
		padding = (height - fontHeight)/2; 
	}

	public void reSizeText(String paramString) {
		setTextSize(0, this.initTextSize);
		if ((paramString == null) || (paramString.length() == 0)) {
			return;
		}
		float f1;
		int i = 0;
		do {
			if (this.width != 0) {
				break;
			}
			f1 = this.paint.measureText(paramString);
			this.width = ((int) f1);
			i = this.width - getPaddingLeft() - getPaddingRight();
		} while (i == 0);
		float f2 = getTextSize();
		for (;;) {
			if (this.paint.measureText(paramString) <= i) {
				setTextSize(0, f2);
				f1 = this.width;
			}
			f2 -= 2.0F;
			this.paint.setTextSize(f2);
		}
	}

	public void refreshNumbers(BigDecimal paramBigDecimal, int paramInt,
			long paramLong) {
		reSizeText(DecimalUtil.amountFromat(paramBigDecimal));
		setTextNew(DecimalUtil.format(0.0D));
		this.animationTime = paramLong;
		double d = paramBigDecimal.doubleValue();
		float[] arrayOfFloat = new float[paramInt];
		arrayOfFloat[0] = 0.0F;
		int j;
		for (int i = 1;; i++) {
			if (i >= arrayOfFloat.length) {
				Arrays.sort(arrayOfFloat);
				j = 0;
				if (j < arrayOfFloat.length)
					break;
				postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						startScrollAnimation();
					}
				}, 250L);
				return;
			}
			arrayOfFloat[i] = (float) (d * Math.random());
		}

		while (true) {
			j++;
			if (j == -1 + arrayOfFloat.length) {
				this.string_ArrayList.add(DecimalUtil
						.amountFromat(paramBigDecimal));
				break;
			}
			this.string_ArrayList.add(DecimalUtil.format(arrayOfFloat[j]));
		}
	}

	public void setOnScrollEndListener(
			OnScrollEndListener paramOnScrollEndListener) {
		this.onScrollEndListener = paramOnScrollEndListener;
	}

	public void setTextColor(int paramInt) {
		this.textColor = paramInt;
		this.paint.setColor(paramInt);
	}

	public void setTextNew(String paramString) {
		this.offsetY = 0.0F;
		this.string_ArrayList.clear();
		this.string_ArrayList.add(paramString);
		this.fontHeight = getFontHeight();
		if (this.width == 0)
			;
		for (float f = this.paint.measureText(paramString);; f = this.width) {
			this.width = (int) f;
			this.midX = (0.5F * this.width);
			invalidate();
			return;
		}
	}

	public void setTextNewReSize(String paramString) {
		reSizeText(paramString);
		setTextNew(paramString);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void startScrollAnimation() {
		float[] arrayOfFloat = new float[2];
		arrayOfFloat[0] = 0.0F;
		arrayOfFloat[1] = this.fontHeight;
		this.animator = ValueAnimator.ofFloat(arrayOfFloat);
		
		this.animator.setInterpolator(new LinearInterpolator());
		this.animator.setDuration(this.animationTime);
		this.animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float f = ((Float) animation.getAnimatedValue()).floatValue();
				offsetY = f;
				invalidate();
			}
		});
//		this.animator.addListener(new AnimatorListener() {
//
//			@Override
//			public void onAnimationStart(Animator animation) {
//				isAnimateEnd = false;
//				Log.d("daizhx","onAnimationStart");
//				postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						animator.start();
//						Log.d("daizhx","animator.start");
//					}
//				}, animationTime);
//			}
//
//			@Override
//			public void onAnimationRepeat(Animator animation) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void onAnimationEnd(Animator animation) {
//				Log.d("daizhx","onAnimationEnd");
//			}
//
//			@Override
//			public void onAnimationCancel(Animator animation) {
//				isAnimateEnd = true;
//				Log.d("daizhx","onAnimationCancel");
//			}
//		});
		animator.setRepeatCount(10);
		this.animator.start();
	}

	public static abstract interface OnScrollEndListener {
		public abstract void onScrollEnd();

		public abstract void onSoundEnd();
	}

}
