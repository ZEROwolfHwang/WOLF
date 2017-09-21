package com.yisipu.chartmap.surfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class MyCompass extends SurfaceView implements SurfaceHolder.Callback {

     public  static   boolean drawOk = true;
    /*
    判断是否可以点击
     */
	public  static boolean canClick=true;
    Handler handler=new Handler();
	private static final String TAG = MyCompass.class.getSimpleName();

	private static final long DOUBLE_CLICK_TIME_SPACE = 300;
   /*
   旋转角度  我手机顺时针 图往逆时针转
    */
   private float degree=0;
	private double chuanshou=0;
	private float windowWidth, windowHeight;

	private Bitmap mBitmap;
	private Paint mPaint;
    /*
    范围海里 0.5-20NM
     */
	private double fanweiNm=20;
    /*
    报警海里 0.5-10NM
     */
	private double baojingNm=8;
	private PointF mStartPoint, mapCenter;// mapCenter表示地图中心在屏幕上的坐标

	public MyshipClickListener getMy() {
		return my;
	}

	public void setMy(MyshipClickListener my) {
		this.my = my;
	}

	public double getChuanshou() {
		return chuanshou;
	}

	public void setChuanshou(double chuanshou) {
		this.chuanshou = chuanshou;
	}


	public interface MyshipClickListener {
		public void onMyshipClick();
	}




	private List<CompassMarkObject> markList = new ArrayList<CompassMarkObject>();

	public MyCompass(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public MyCompass(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public MyCompass(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		// 获取屏幕的宽和高
		windowWidth = getResources().getDisplayMetrics().widthPixels;
		windowHeight = getResources().getDisplayMetrics().heightPixels;
		mPaint = new Paint();

		mStartPoint = new PointF();
		mapCenter = new PointF();
	}

	public void setBitmap(Bitmap bitmap) {

		this.mBitmap = bitmap;


		float bitmapRatio = mBitmap.getHeight() / mBitmap.getWidth();
		float winRatio = windowHeight / windowWidth;

//		draw();
	}

	/**
	 * 为当前地图添加标记
	 * 
	 * @param object
	 */
	public void addMark(CompassMarkObject object) {
		markList.add(object);
	}
	public void addMarkList(List<CompassMarkObject> list) {
		markList.clear();
		markList.addAll(list);
	}
	/**
	 * 清理标志
	 *
	 * @param
	 */
	public void clearMark() {
		markList.clear();
	}



   /*
   根据旋转角度计算新的值
    */
	private static Point calcNewPoint(Point p, Point pCenter, float angle) {
		// calc arc
		float l = (float) ((angle * Math.PI) / 180);

		//sin/cos value
		float cosv = (float) Math.cos(l);
		float sinv = (float) Math.sin(l);

		// calc new point
		float newX = (float) ((p.x - pCenter.x) * cosv - (p.y - pCenter.y) * sinv + pCenter.x);
		float newY = (float) ((p.x - pCenter.x) * sinv + (p.y - pCenter.y) * cosv + pCenter.y);
		return new Point((int) newX, (int) newY);
	}
	MyshipClickListener my;
	// 处理点击标记的事件
	private void clickAction(MotionEvent event) {

		Point p;
		int clickX = (int) event.getX();
		int clickY = (int) event.getY();
		Point pCenter=new Point((int)CirX,
				(int)CirY);
		if (pCenter.x - mBitmap.getWidth() < clickX
				&& pCenter.x +mBitmap.getWidth() > clickX
				&& pCenter.y + mBitmap.getHeight() > clickY
				&& pCenter.y - mBitmap.getHeight() < clickY) {
			if(MyCompass.canClick) {
				my.onMyshipClick();
			}
//			return;
		}

		Logger.i( pCenter.x+"ha哈哈z"+ pCenter.y);
		Point p2;
		for (CompassMarkObject object : markList) {
			Bitmap location = object.getmBitmap();

			p = new Point((int) (float) (CirX - (object.getMapX() / (fanweiNm + 0.0)) * 20 * 15 - object.getmBitmap().getWidth() / 2.0), (int) (float) (CirY - (object.getMapY() / (fanweiNm + 0.0)) * 20 * 15 - object.getmBitmap().getHeight() / 2.0));
			Logger.i(p.x + "ha哈哈" + p.y);
//			p2=new Point((int)((p.x-CirX)*Math.cos(90-degree)-(p.y-CirY)*Math.sin(90-degree)+CirX),(int)((p.x-CirX)*Math.sin(-degree)+(p.y-CirY)*Math.cos(-degree)+CirY));
			p2 = calcNewPoint(p, pCenter,-degree);
			Logger.i(p2.x+"ha哈哈h"+p2.y+"DSG"+p.x+"DSHS"+p.y+"DFG"+clickX+"DSG"+clickY);
			if (p2.x - location.getWidth() < clickX
					&& p2.x + location.getWidth() > clickX
					&& p2.y + location.getHeight() > clickY
					&& p2.y - location.getHeight() < clickY) {
				if (object.getMarkListener() != null&&MyCompass.canClick) {
					object.getMarkListener().onMarkClick(clickX, clickY);
				}


			}

		}

//			Bitmap location = object.getmBitmap();
//			int objX = (int) (mapCenter.x - location.getWidth() / 2
//					- mBitmap.getWidth() * mCurrentScale / 2 + mBitmap
//					.getWidth() * object.getMapX() * mCurrentScale);
//			int objY = (int) (mapCenter.y - location.getHeight()
//					- mBitmap.getHeight() * mCurrentScale / 2 + mBitmap
//					.getHeight() * object.getMapY() * mCurrentScale);
//			// 判断当前object是否包含触摸点，在这里为了得到更好的点击效果，我将标记的区域放大了
//			if (objX - location.getWidth() < clickX
//					&& objX + location.getWidth() > clickX
//					&& objY + location.getHeight() > clickY
//					&& objY - location.getHeight() < clickY) {
//				if (object.getMarkListener() != null) {
//					object.getMarkListener().onMarkClick(clickX, clickY);
//				}
//				break;
//			}
//
//		}

	}

	// 计算两个触摸点的距离
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}
	/*
	圆心的坐标 x,y
	 */
    private  double CirX=0;
	private  double CirY=0;
	Point pCenter2;

	Runnable runnable=new Runnable() {
		@Override
		public void run() {
			while (true) {
				if (drawOk) {
					// TODO Auto-generated method stub
					Canvas canvas = getHolder().lockCanvas();
					if (canvas != null && mBitmap != null) {
						CirX = windowWidth / 2;
						CirY = windowWidth / 2;
						canvas.drawColor(Color.GRAY);
						canvas.clipRect(0, 0, 720, 720);                  //设置裁剪区
						canvas.save();
						canvas.rotate(-degree, 360, 360);  //锁定画布
						//旋转45

						//画布旋转
//					canvas.rotate(-degree,(float) CirX,(float) CirY);

//					mPaint.setStyle(Paint.Style.STROKE);
						mPaint.setAntiAlias(true);
						mPaint.setColor(Color.RED);
						mPaint.setStyle(Paint.Style.STROKE);
						mPaint.setStrokeWidth(2);
						Matrix matrix = new Matrix();

//					matrix.postRotate(90);
//					matrix.setTranslate(windowWidth/2-mBitmap.getWidth()/2,windowWidth/2-mBitmap.getWidth()/2);
//					matrix.setRotate((float) getChuanshou());
						matrix.preTranslate(windowWidth / 2 - mBitmap.getWidth() / 2, windowWidth / 2 - mBitmap.getWidth() / 2); //设置初始位置
//					 matrix.preRotate((float) getChuanshou(),windowWidth/2,windowWidth/2); //设置旋转角度，及旋转中心（相对座标）
						// canvas.drawBitmap(bitmap, matrix, paint);
//					matrix.setRotate(();
						matrix.setTranslate(windowWidth / 2 - mBitmap.getWidth() / 2, windowWidth / 2 - mBitmap.getWidth() / 2);
						matrix.preRotate((float) getChuanshou(), (float) mBitmap.getWidth() / 2, (float) mBitmap.getHeight() / 2);  //要旋转的角度

						canvas.drawBitmap(mBitmap, matrix, mPaint);
						canvas.drawCircle((float) CirX, (float) CirY, (float) (20 * 15 * (baojingNm / (fanweiNm + 0.0))), mPaint);

						mPaint.setColor(Color.YELLOW);
						mPaint.setStyle(Paint.Style.STROKE);
						mPaint.setStrokeWidth(2);
//					canvas.drawCircle((float) CirX,(float) CirY,(float) fanweiNm*15,mPaint);
						canvas.drawCircle((float) CirX, (float) CirY, (float) 20 * 15, mPaint);
						mPaint.setColor(Color.BLACK);
						mPaint.setStrokeWidth(2);
						mPaint.setStyle(Paint.Style.FILL);
						mPaint.setTextSize(60);
					/*
					固定20
					 */
						canvas.drawText("N", (float) CirX - 20, (float) (CirY - 20 * 15), mPaint);
						canvas.drawText("S", (float) (CirX), (float) (CirY + 20 * 15 + 20), mPaint);
						canvas.drawText("W", (float) (CirX - 20 * 15 - 20), (float) (CirY), mPaint);
						canvas.drawText("E", (float) (CirX + 20 * 15 - 20), (float) (CirY), mPaint);
//					canvas.drawText("N",(float) CirX,(float)(CirY- fanweiNm*15),mPaint);
//					canvas.drawText("S",(float) (CirX),(float)(CirY+ fanweiNm*15+20),mPaint);
//					canvas.drawText("W",(float) (CirX- fanweiNm*15-20),(float)(CirY),mPaint);
//					canvas.drawText("E",(float) (CirX+fanweiNm*15),(float)(CirY),mPaint);


//					matrix.setScale(1,1);

						pCenter2 = new Point((int) CirX,
								(int) CirY);
						Point p = new Point((int) CirX,
								(int) (CirY - 20 * 15));
						p = calcNewPoint(p, pCenter2, (float) getChuanshou());

						;

						canvas.drawLine((float) CirX, (float) CirY, p.x, p.y, mPaint);
//					canvas.drawBitmap(mBitmap,windowWidth/2-mBitmap.getWidth()/2,windowWidth/2-mBitmap.getWidth()/2,mPaint);

						for (CompassMarkObject object : markList) {
//						canvas.drawBitmap(object.getmBitmap(),(float)(CirX-object.getMapX()*15-(object.getmBitmap().getWidth()/2.0)),(float) (CirY-object.getMapY()*15-(object.getmBitmap().getHeight()/2.0)),mPaint);
//						canvas.drawBitmap(object.getmBitmap(),(float)(CirX-object.getMapX()*15-(object.getmBitmap().getWidth()/2.0)),(float) (CirY-object.getMapY()*15-(object.getmBitmap().getHeight()/2.0)),mPaint);
							Matrix matrix2 = new Matrix();

//					matrix.postRotate(90);
//					matrix.setTranslate(windowWidth/2-mBitmap.getWidth()/2,windowWidth/2-mBitmap.getWidth()/2);
//					matrix.setRotate((float) getChuanshou());
							matrix2.preTranslate((float) (CirX - (object.getMapX() / (fanweiNm + 0.0)) * 20 * 15 - (object.getmBitmap().getWidth() / 2.0)), (float) (CirY - (object.getMapY() / (fanweiNm + 0.0)) * 20 * 15 - (object.getmBitmap().getHeight() / 2.0))); //设置初始位置
//	                    matrix2.preTranslate((float)(CirX-object.getMapX()*15-(object.getmBitmap().getWidth()/2.0)),(float) (CirY-object.getMapY()*15-(object.getmBitmap().getHeight()/2.0))); //设置初始位置
// matrix.preRotate((float) getChuanshou(),windowWidth/2,windowWidth/2); //设置旋转角度，及旋转中心（相对座标）
							// canvas.drawBitmap(bitmap, matrix, paint);
//					matrix.setRotate(();
//						matrix2.setTranslate((float)(CirX-object.getMapX()*15-(object.getmBitmap().getWidth()/2.0)),(float) (CirY-object.getMapY()*15-(object.getmBitmap().getHeight()/2.0)));
							matrix2.setTranslate((float) (CirX - (object.getMapX() / (fanweiNm + 0.0)) * 20 * 15 - (object.getmBitmap().getWidth() / 2.0)), (float) (CirY - (object.getMapY() / (fanweiNm + 0.0)) * 20 * 15 - (object.getmBitmap().getHeight() / 2.0)));
							if (object.getChuanshou() != 511 && object.getChuanshou() != 0) {
								matrix2.preRotate((float) object.getChuanshou(), (float) object.getmBitmap().getWidth() / 2, (float) object.getmBitmap().getHeight() / 2);  //要旋转的角度
							}
							canvas.drawBitmap(object.getmBitmap(), matrix2, mPaint);
						}
//
//					canClick=true;
						canvas.restore();
//						Bitmap location = object.getmBitmap();
//
//						canvas.drawBitmap(location, matrix, mPaint);
//					}

					}
					if (canvas != null) {
						getHolder().unlockCanvasAndPost(canvas);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	private void draw() {

             new Thread(runnable).start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:

			clickAction(event);
			break;

		case MotionEvent.ACTION_POINTER_DOWN:

			break;

		case MotionEvent.ACTION_MOVE:


			break;
		case MotionEvent.ACTION_UP:




			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		drawOk = true;
	draw();


	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

		drawOk = false;
		if (mBitmap != null) {
			mBitmap.recycle();
		}
		for (CompassMarkObject object : markList) {
			if (object.getmBitmap() != null) {
				object.getmBitmap().recycle();
			}
		}
//		}
	}


	public double getBaojingNm() {
		return baojingNm;
	}

	public void setBaojingNm(double baojingNm) {
		this.baojingNm = baojingNm;
	}

	public double getFanweiNm() {
		return fanweiNm;
	}

	public void setFanweiNm(double fanweiNm) {
		this.fanweiNm = fanweiNm;
	}

	public Bitmap getmBitmap() {
		return mBitmap;
	}

	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}

	public float getDegree() {
		return degree;
	}

	public void setDegree(float degree) {
		this.degree = degree;

	}
}
