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
	// һ���ؼ����ڶ����������
	private int sleepTime =100;
	private Thread thread;
	private boolean isRunning = true;


	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//��ȡ�Զ�������(R.styleable.AnimationView ��һ�����飬Ԫ�����Զ����sleep_time��images)
		TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.AnimationView); 
		
		//--��ȡԪ��sleepTime�� ���ش���Ĭ������Ϊ10����ʵֵ��xml�����ļ�������
		sleepTime = (int) typedArray.getFloat(R.styleable.AnimationView_sleep_time, 10);
		
		//--��ȡԪ��images����(����Դ����getResources��ȡ��Դ)
		Resources resources = getResources();
		TypedArray imageTypedArray = resources.obtainTypedArray(R.array.animationImages);
		int length = imageTypedArray.length();
		bitmapArray = new Bitmap[length];
		for (int i = 0; i < length; i++) {
			//��ȡ��Դ��ID��
			int imageId = imageTypedArray.getResourceId(i, 0);
			Bitmap bm = BitmapFactory.decodeResource(getResources(), imageId);
			//����bitmap������
			bitmapArray[i]=bm;
		}
		
//		//û���Զ������Եķ���
//		bitmapArray = new Bitmap[4];
//		bitmapArray[0] = BitmapFactory.decodeResource(getResources(), R.drawable.logo1);
//		bitmapArray[1] = BitmapFactory.decodeResource(getResources(), R.drawable.logo2);
//		bitmapArray[2] = BitmapFactory.decodeResource(getResources(), R.drawable.logo3);
//		bitmapArray[3] = BitmapFactory.decodeResource(getResources(), R.drawable.logo4);

		//�����߳�,ʵ��ͼƬ�л�
		MyRunnable runnable = new MyRunnable();
		thread = new Thread(runnable);
		thread.start();

	}
	
	/**
	 * ������С
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// �����Լ���С
		int firstImageWidth = bitmapArray[0].getWidth();
		int firstImageHeight = bitmapArray[1].getHeight();
		setMeasuredDimension(firstImageWidth, firstImageHeight);
	}

	

	/**
	 * ������д�ķ�����onDraw--����Ԫ��
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		Bitmap bm = bitmapArray[currentIndex++];
		//����Paint�����൱�ڻ���
		Paint paint = new Paint();
		//������
		Rect rect = new Rect(0, 0, viewWidth, viewHeight);
		paint.setColor(Color.RED);
		canvas.drawRect(rect, paint);
		//Ԫ�ص�����
		int bitmapX = (viewWidth - bm.getWidth()) / 2;
		int bitmapY = (viewHeight - bm.getHeight()) / 2;
		//����ͼƬ
		canvas.drawBitmap(bm, bitmapX, bitmapY, paint);


	}

	/**
	 * ������д�ķ�����onSizeChanged--��ʼ���Զ���ؼ��ĸ߶ȺͿ��
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		viewHeight = h;
		viewWidth = w;

	}

	/**
	 * �����߳�
	 */
	class MyRunnable implements Runnable{

		@Override
		public void run() {
			while(isRunning){

				if(currentIndex>bitmapArray.length -1){
					currentIndex=0;
				}
				/**
				 * ˢ�½���
				 * ���̵߳���invalidate()
				 * �����̵߳���postInvalidate()
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
