package com.yisipu.chartmap.surfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.location.GpsSatellite;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class SatellitesView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String LOG_TAG = "SatellitesView";
    private SurfaceHolder holder;
    private Paint mPaint;
    private float windowWidth, windowHeight;
    private PointF mStartPoint, mapCenter;
    private List<GpsSatellite> satellites = new ArrayList<>();
    public List<GpsSatellite> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<GpsSatellite> satellites) {
        this.satellites.clear();
        this.satellites.addAll(satellites);
        draw();
    }

    // 卫星图
    private Bitmap satelliteBitmap;
    private Bitmap compassBitmap;
    private int cx = 0;
    private int cy = 0;
    private int compassRadius=434/2;

    /* Callback invoked when the surface dimensions change. */
    public void setSurfaceSize(int width, int height) {
        synchronized (holder) {
            cx = width / 2;
            cy = height  / 2;
        }
    }

    public SatellitesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setKeepScreenOn(true);//设置屏幕常量
        init();//初始化
    }

    public SatellitesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SatellitesView(Context context) {
        super(context);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder=holder;

        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setSurfaceSize(width, height);
    }
    public static void MyRecycle(Bitmap bmp){
        if(!bmp.isRecycled() && null!=bmp){
            bmp=null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {



        if (satelliteBitmap != null) {
          MyRecycle(satelliteBitmap);

        }
        if (compassBitmap != null) {
            MyRecycle(compassBitmap);
        }

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
        mPaint.setSubpixelText(true);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setTextSize(24);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }
    public void setBitmap(Bitmap compassBitmap,Bitmap satelliteBitmap) {
        this.satelliteBitmap = satelliteBitmap;
        this.compassBitmap = compassBitmap;
        compassRadius = compassBitmap.getWidth() / 2;

        draw();
    }

//    public void setcompassBitmap(Bitmap bitmap) {
//        this.compassBitmap = bitmap;
//        draw();
//    }
    private void draw() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Canvas canvas =getHolder().lockCanvas();
//
                if (canvas != null && compassBitmap != null) {
                    canvas.drawColor(Color.WHITE);
//                    cx = (int)windowWidth / 2;
//                    cy = (int)windowWidth  / 2;
                    cx = canvas.getWidth() / 2;
                    cy = canvas.getWidth()  / 2;

                    doDraw(canvas, satellites);

                }
                if (canvas != null) {
                    getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }).start();
    }

    /**
     * 绘制背景罗盘
     *
     * @param canvas
     * @param cx     罗盘中心点位于画布上的X坐标
     * @param cy     罗盘中心点位于画布上的Y坐标
     * @param r      罗盘的半径
     */
    private void drawBackground(Canvas canvas, int cx, int cy, int r) {
//        int x = cx - compassBitmap.getWidth()/2;
//        int y = cy - compassBitmap.getHeight()/2;
        int x = cx - r;
        int y = cy - r;
        canvas.drawBitmap(compassBitmap, x, y, mPaint);
    }

    /**
     * 将角度转换为弧度，以用于三角函数的运算
     *
     * @param degree
     * @return
     */
    private double degreeToRadian(double degree) {
        return (degree * Math.PI) / 180.0d;
    }

    /*
     * 将SNR的值，转化为通用的信号强度级别，主要用于在绘制卫星时，通过颜色来表明它的信号强度，暂时没用到
     * SNR is mapped to signal strength [0,1,4-9] COMMENT SNR: >500 >100 >50 >10
     * >5 >0 bad n/a COMMENT sig: 9 8 7 6 5 4 1 0 COMMENT
     */
    private int snrToSignalLevel(float snr) {
        int level = 0;
        if (snr >= 0 && snr < 5) {
            level = 4;
        } else if (snr >= 5 && snr < 10) {
            level = 5;
        } else if (snr >= 10 && snr < 50) {
            level = 6;
        } else if (snr >= 50 && snr < 100) {
            level = 7;
        } else if (snr >= 100 && snr < 500) {
            level = 8;
        } else if (snr >= 500) {
            level = 9;
        }
        return level;
    }

    /**
     * 在背景罗盘上绘制卫星
     *
     * @param canvas
     * @param satellite
     * @param cx        中心圆点的X座标
     * @param cy        中心圆点的Y座标
     * @param r         罗盘背景的半径
     */
    private void drawSatellite(Canvas canvas, GpsSatellite satellite, int cx, int cy, int r) {

        /**
         * GPS卫星导航仪通常选用仰角大于5º，小于85º。 因为当卫星仰角大于85º时，L1波段的电离层折射误差较大，故规定仰角大于85º时，
         * 定位无效，不进行数据更新。而卫星仰角越小，则对流层折射误差越大，故一般选用仰角大于5º的卫星来定位。
         */
        //得到仰角
        float elevation = satellite.getElevation();
        //通过仰角，计算出这个卫星应该绘制到离圆心多远的位置，这里用的是角度的比值
        double r2 = r * ((90.0f - elevation) / 90.0f);

		/*得到方位角（与正北向也就是Y轴顺时针方向的夹角，注意我们通常几何上的角度
         * 是与X轴正向的逆时针方向的夹角）,在计算X，Y座标的三角函数时，要做转换
         */
        double azimuth = satellite.getAzimuth();

		/*
         * 转换成XY座标系中的夹角,方位角是与正北向也就是Y轴顺时针方向的夹角，
         * 注意我们通常几何上的角度是与X轴正向的逆时针方向的夹角）,
         * 在计算X，Y座标的三角函数时，要做转换
         */
        double radian = degreeToRadian(360 - azimuth + 90);

        double x = cx + Math.cos(radian) * r2;
        double y = cy + Math.sin(radian) * r2;

        //得到卫星图标的半径
        int sr = satelliteBitmap.getWidth() / 2;
        //以x,y为中心绘制卫星图标
        canvas.drawBitmap(satelliteBitmap, (float) (x - sr), (float) (y - sr), mPaint);
        //在卫星图标的位置上绘出文字（卫星编号及信号强度）
        String info = String.format("#%s", satellite.getPrn());
        canvas.drawText(info, (float) (x - sr), (float) (y - sr), mPaint);

    }

    private void doDraw(Canvas canvas, List<GpsSatellite> satellites) {

        // 绘制背景罗盘
        drawBackground(canvas, cx, cy, compassRadius);
        //绘制卫星分布
        if (satellites != null) {
            for (GpsSatellite satellite : satellites) {
                drawSatellite(canvas, satellite, cx, cy, compassRadius);
            }
        }


    }

    public Bitmap getSatelliteBitmap() {
        return satelliteBitmap;
    }

    public void setSatelliteBitmap(Bitmap satelliteBitmap) {
        this.satelliteBitmap = satelliteBitmap;
    }

    public Bitmap getCompassBitmap() {
        return compassBitmap;
    }

    public void setCompassBitmap(Bitmap compassBitmap) {
        this.compassBitmap = compassBitmap;
    }
}
