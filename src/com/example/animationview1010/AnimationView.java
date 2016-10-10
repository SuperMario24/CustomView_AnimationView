package com.example.animationview1010;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class AnimationView extends View{

	private Bitmap[] bitmapArray;
	private int currentIndex = 0;
	private int viewWidth;
	private int viewHeight;
	// 一个控件，在多个布局中用
	private int sleepTime =100;
	private Thread thread;
	private boolean isRunning = true;


	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//读取自定义属性(R.styleable.AnimationView 是一个数组，元素是自定义的sleep_time和images)
		TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.AnimationView); 
		
		//--获取元素sleepTime， 加载错误默认设置为10，真实值在xml布局文件中设置
		sleepTime = (int) typedArray.getFloat(R.styleable.AnimationView_sleep_time, 10);
		
		//--获取元素images数组(是资源的用getResources获取资源)
		Resources resources = getResources();
		TypedArray imageTypedArray = resources.obtainTypedArray(R.array.animationImages);
		int length = imageTypedArray.length();
		bitmapArray = new Bitmap[length];
		for (int i = 0; i < length; i++) {
			//获取资源的ID，
			int imageId = imageTypedArray.getResourceId(i, 0);
			Bitmap bm = BitmapFactory.decodeResource(getResources(), imageId);
			//存入bitmap数组中
			bitmapArray[i]=bm;
		}
		
//		//没有自定义属性的方法
//		bitmapArray = new Bitmap[4];
//		bitmapArray[0] = BitmapFactory.decodeResource(getResources(), R.drawable.logo1);
//		bitmapArray[1] = BitmapFactory.decodeResource(getResources(), R.drawable.logo2);
//		bitmapArray[2] = BitmapFactory.decodeResource(getResources(), R.drawable.logo3);
//		bitmapArray[3] = BitmapFactory.decodeResource(getResources(), R.drawable.logo4);

		//启动线程,实现图片切换
		MyRunnable runnable = new MyRunnable();
		thread = new Thread(runnable);
		thread.start();

	}
	
	/**
	 * 测量大小
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 测量自己大小
		int firstImageWidth = bitmapArray[0].getWidth();
		int firstImageHeight = bitmapArray[1].getHeight();
		setMeasuredDimension(firstImageWidth, firstImageHeight);
	}

	

	/**
	 * 必须重写的方法，onDraw--画出元素
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		Bitmap bm = bitmapArray[currentIndex++];
		//创建Paint对象，相当于画笔
		Paint paint = new Paint();
		//画矩形
		Rect rect = new Rect(0, 0, viewWidth, viewHeight);
		paint.setColor(Color.RED);
		canvas.drawRect(rect, paint);
		//元素的坐标
		int bitmapX = (viewWidth - bm.getWidth()) / 2;
		int bitmapY = (viewHeight - bm.getHeight()) / 2;
		//画出图片
		canvas.drawBitmap(bm, bitmapX, bitmapY, paint);


	}

	/**
	 * 必须重写的方法，onSizeChanged--初始化自定义控件的高度和宽度
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		viewHeight = h;
		viewWidth = w;

	}

	/**
	 * 工作线程
	 */
	class MyRunnable implements Runnable{

		@Override
		public void run() {
			while(isRunning){

				if(currentIndex>bitmapArray.length -1){
					currentIndex=0;
				}
				/**
				 * 刷新界面
				 * 主线程调用invalidate()
				 * 工作线程调用postInvalidate()
				 */
				postInvalidate();
				try {
					Thread.currentThread().sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
