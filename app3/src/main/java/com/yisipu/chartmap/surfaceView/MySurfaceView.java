package com.yisipu.chartmap.surfaceView;

/**
 * Created by Administrator on 2016/8/5.
 */


        import android.content.Context;
        import android.content.res.Resources;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.graphics.Rect;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;

        import com.yisipu.chartmap.bean.ShipBean;

public class MySurfaceView extends SurfaceView implements
        SurfaceHolder.Callback
{

    private Paint paint;
    private DrawThread mThread = null;
    private Path path;//主要用于绘制复杂的图形轮廓，比如折线，圆弧以及各种复杂图案
    public MySurfaceView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {


        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        mThread = new DrawThread(holder);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {

        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {

        mThread.setRun(true);
        mThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mThread.setRun(false);



    }

    /**
     * 绘制线程类
     *
     */
    public class DrawThread extends Thread
    {
        private SurfaceHolder mHolder = null;
        private boolean isRun = false;

        public DrawThread(SurfaceHolder holder)
        {

            mHolder = holder;

        }

        public void setRun(boolean isRun)
        {

            this.isRun = isRun;
        }
        private Path p=new Path();
        @Override
        public void run()
        {

            int count = 0;

            while (isRun)
            {
                Canvas canvas = null;
                synchronized (mHolder)
                {
                    try
                    {

                        canvas = mHolder.lockCanvas();
                        canvas.drawColor(Color.WHITE);
                        Paint p = new Paint();
                        p.setColor(Color.BLACK);

                        Rect r = new Rect(100, 50, 300, 250);
                        canvas.drawRect(r, p);
                        canvas.drawText("这是第" + (count++) + "秒", 100, 310, p);
                        //起始顶点，绘制的方向为左下右上。

                        //画路径1，不规则封闭图形
                        p.setStrokeWidth(0.5f);
                        path = new Path();
                        path.moveTo(10,70);//指定初始轮廓点，若没指定默认从(0,0)点开始
                        path.lineTo(10,100);//从当前轮廓点绘制一条线段到指定轮廓点
                        path.lineTo(50,100);
                        path.lineTo(50, 80);
                        path.close(); // 回到初始点形成封闭的曲线
                        canvas.drawPath(path, p);
                        Thread.sleep(20000);// 睡眠时间为1秒

                    }
                    catch (Exception e)
                    {

                        e.printStackTrace();

                    }
                    finally
                    {
                        if (null != canvas)
                        {
                            mHolder.unlockCanvasAndPost(canvas);
                        }
                    }

                }

            }
        }

    }

}
