package com.cd.csdnblog.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

public class MyDownloadProgressView extends ProgressBar{
	
	private int max = 100;
	private int progress = 30;
	private Paint mPaint;
	private int mWidth;
	private int mHeight;
	private Rect mRect;
	protected Context context;
	
	public MyDownloadProgressView(Context context) {
		super(context);
	}

	public MyDownloadProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	private void init(){
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		
		mRect = new Rect(0, 0, mWidth, mHeight);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(mPaint == null){
			init();
		}
		// 绘制透明白色框
		mRect.set(0, 0, mWidth, mHeight);
		mPaint.setARGB(255, 207, 184, 143);
		canvas.drawRect(mRect, mPaint);
		// 绘制进度框
		mRect.set(0, 0, progress*mWidth/max, mHeight);
		mPaint.setARGB(255, 255, 136, 0);
		canvas.drawRect(mRect, mPaint);
	}
	
	public void setMax(int max){
		this.max = max;
	}
	
	/**
	 * must be called in UI thread
	 * @param progress
	 */
	public void setProgress(int progress){
		this.progress = progress;
		this.invalidate();
	}
}
