package com.yisipu.chartmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerLayout;
import com.yisipu.chartmap.provider.BitmapProviderAssets;
import com.yisipu.chartmap.utils.GetLongLati;

import java.util.ArrayList;



public class RealMapTileViewActivity extends TileViewActivity {

//    public static final double NORTH_WEST_LATITUDE = 39.9639998777094;//西北纬度
//    public static final double NORTH_WEST_LONGITUDE = -75.17261900652977;//西北经度
//    public static final double SOUTH_EAST_LATITUDE = 39.93699709962642;//东南纬度
//    public static final double SOUTH_EAST_LONGITUDE = -75.12462846235614;//东南经度

//    public static final double NORTH_WEST_LATITUDE = 116.11638888888889;//西北纬度
//    public static final double NORTH_WEST_LONGITUDE =  21.389722222222222;//西北经度
//    public static final double SOUTH_EAST_LATITUDE = 121.31666666666666;//东南纬度
//    public static final double SOUTH_EAST_LONGITUDE =  25.6775;//东南经度

    public static final double NORTH_WEST_LATITUDE = GetLongLati.getlat(11,0,5);//西北经度
    public static final double NORTH_WEST_LONGITUDE = GetLongLati.getLong(25,0,5);//西北纬度
    public static final double SOUTH_EAST_LATITUDE = GetLongLati.getlat(16,0,5);//东南经度
    public static final double SOUTH_EAST_LONGITUDE =GetLongLati.getLong(28,0,5);//东南纬度

    private float windowWidth, windowHeight;
    private double CirX = 0;
    private double CirY = 0;
    float xDown, yDown, xUp;
    boolean isLongClickModule = false;
    boolean isLongClicking = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Logger.i("ddkjdkgkg"+"n"+NORTH_WEST_LATITUDE+ "b:"+NORTH_WEST_LONGITUDE+"c"+SOUTH_EAST_LATITUDE+"d"+SOUTH_EAST_LONGITUDE);
        Logger.i("sdgg"+GetLongLati.getY(24,5));
        // we'll reference the TileView multiple times
        //我们会参考tileview多东南纬度 次
        final TileView tileView = getTileView();
        // simple http provider

        // size and geolocation
        //规模和地理位置 8967/256-1  6726/256 得到最大的行 和最大的列
//        tileView.setSize(8967, 6726);
//        tileView.setSize(15360,3027);

//        tileView.setSize(41*256,61*256);
//        tileView.setSize(1000,1000);
        // we won't use a downsample here, so color it similarly to tiles
        //我们不会使用一个采样，所以它的颜色同样的瓷砖
        tileView.setBackgroundColor(0xFFe7e7e7);

//        tileView.addDetailLevel(0.1250f, "tiles/map/phi-62500-%d_%d.jpg");
//        tileView.addDetailLevel(1.0000f, "tiles/map/phi-125000-%d_%d.jpg");
//        tileView.addDetailLevel(2.0000f, "tiles/map/phi-250000-%d_%d.jpg");
//        tileView.addDetailLevel(4.0000f, "tiles/map/phi-500000-%d_%d.jpg");
//        tileView.addDetailLevel(1f, "tiles/map2/5/google1_x%dy%dz5.jpg");
        tileView.addDetailLevel(1f, "tiles/map2/5/%d_%d_5.png");
        tileView.addDetailLevel(2f, "tiles/map2/6/%d_%d_6.png");


        tileView.addDetailLevel(4f, "tiles/map2/7/%d_%d_7.png");
        tileView.addDetailLevel(8f, "tiles/map2/8/%d_%d_8.png");

        tileView.addDetailLevel(16.0000f, "tiles/map2/9/%d_%d_9.png");
        tileView.setBitmapProvider( new BitmapProviderAssets() );
        tileView.setSize(256*3,256*5);

        // markers should align to the coordinate along the horizontal center and vertical bottom
        //标记应沿水平中心和垂直底部的坐标对齐
        tileView.setMarkerAnchorPoints(-0.5f, -1.0f);
        // provide the corner coordinates for relative positioning
        //提供相对定位的角点坐标
//        tileView.defineBounds(
//
//
//                NORTH_WEST_LONGITUDE,
//                NORTH_WEST_LATITUDE,
//
//                SOUTH_EAST_LONGITUDE,
//                SOUTH_EAST_LATITUDE
//        );
        tileView.setShouldScaleToFit(true);
//        tileView.defineBounds(
//                116.11638888888889,
//                21.389722222222222,
//                121.31666666666666,
//                25.6775
//
//        );
//        tileView.defineBounds(
//                116.11638888888889,
//                21.389722222222222,
//                121.31666666666666,
//                25.6775
//
//        );
        //获取编程DP指标
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Paint paint = tileView.getDefaultPathPaint();
//        tileView.defineBounds(
//                180,
//                NORTH_WEST_LATITUDE,
//               -180,
//                SOUTH_EAST_LATITUDE
//        );
        // add markers for all the points
        //添加所有点的标记

        for (double[] point : points) {
            // any view will do...
            //任何视图将做…
            ImageView marker = new ImageView(this);
            // save the coordinate for centering and callout positioning
            //拯救协调中心和标注定位
            marker.setTag(point);
//            marker.setImageBitmap();
            // give it a standard marker icon - this indicator points down and is centered, so we'll use appropriate anchors
            //给它一个标准的标记图标-这个指标下来，并为中心，所以我们会使用适当的锚
            marker.setImageResource(Math.random() < 0.75 ? R.drawable.map_marker_normal : R.drawable.map_marker_featured);
            // on tap show further information about the area indicated
            // this could be done using a OnClickListener, which is a little more "snappy", since
            // MarkerTapListener uses GestureDetector.onSingleTapConfirmed, which has a delay of 300ms to
            // confirm it's not the start of a double-tap. But this would consume the touch event and
            // interrupt dragging
//            在点击显示区域进一步的信息表明这可能是通过使用一个onclicklistener，这是一个更“快”，
//            因为markertaplistener使用gesturedetector.onsingletapconfirmed，具有延迟300ms确认这是不是一个双击启动。
//            但这将消耗触摸事件和中断拖动
            tileView.getMarkerLayout().setMarkerTapListener(markerTapListener);

            // add it to the view tree
            //将它添加到视图树
            tileView.addMarker(marker, point[0], point[1], null, null);
        }

        // let's start off framed to the center of all points
        //让我们开始框架的所有点的中心
        double x = 0;
        double y = 0;
        for (double[] point : points) {
            x = x + point[0];
            y = y + point[1];
        }
        int size = points.size();
        x = x / size;
        y = y / size;
        frameTo(x, y);
        paint.setShadowLayer(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics),
                0x66000000
        );
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, metrics));
        paint.setPathEffect(
                new CornerPathEffect(
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, metrics)
                )
        );
        windowWidth = getResources().getDisplayMetrics().widthPixels;
        windowHeight = getResources().getDisplayMetrics().heightPixels;
        CirX = windowWidth / 2;
        CirY = windowWidth / 2;
        tileView.drawPath(points.subList(1, 5), null);
        Canvas canvas = new Canvas();
//        Paint paint1 = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawCircle((float) CirX, (float) CirY, (float) 20 * 15, paint);

//        canvas.drawCircle(200, 200, 100f, paint1);
//        canvas.drawText("啊科技示范户", 200, 200, paint1);
        tileView.draw(canvas);

        // test higher than 1
        //试验高于1
        tileView.setScaleLimits(0,16);

        // start small and allow zoom
        //启动小，允许变焦
        tileView.setScale(0.5f);


        // with padding, we might be fast enough to create the illusion of a seamless image
        //用填充，我们可能是足够快，以创造一个无缝的图像的错觉
        tileView.setViewportPadding(256);

        // we're running from assets, should be fairly fast decodes, go ahead and render asap
        //我们从资产，应该是相当快速的解码，去渲染ASAP
        tileView.setShouldRenderWhilePanning(true);
        tileView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TileView tileView = getTileView();

                float fraction=tileView.getDetailLevelManager().getScale();
                Logger.d("getScale",""+fraction);
                Logger.d("getPivotX","x"+tileView.getPivotX()+"   y"+tileView.getPivotY());
                Logger.d("getX","x"+tileView.getX()+"   y"+tileView.getY());
                Logger.d("getScaleX","x"+tileView.getOffsetX()+"   y"+tileView.getOffsetY());
                Logger.d("getLeft","x"+tileView.getRight()+"   y"+tileView.getBottom());
                Logger.d("getScaledHeight","x"+tileView.getScaledHeight()+"   y"+tileView.getScaledWidth());
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xDown = event.getX();
                    yDown = event.getY();
                    Log.v("OnTouchListener", "Down");

                } else if (event.getAction() == MotionEvent.ACTION_UP) {// 松开处理
                    //获取松开时的x坐标
                    if (isLongClickModule) {
                        isLongClickModule = false;
                        isLongClicking = false;
                    }
                    xUp = event.getX();

                    Log.v("OnTouchListener", "Up");
                    //按下和松开绝对值差当大于20时滑动，否则不显示
                    if ((xUp - xDown) > 20) {
                        //添加要处理的内容
                    } else if ((xUp - xDown) < -20) {
                        //添加要处理的内容
                    } else if (0 == (xDown - xUp)) {
                        int viewWidth = v.getWidth();
                        if (xDown < viewWidth / 3) {
                            //靠左点击
//                            Toast.makeText(RealMapTileViewActivity.this, "靠左点击", Toast.LENGTH_LONG).show();
                        } else if (xDown > viewWidth / 3 && xDown < viewWidth * 2 / 3) {
                            //中间点击
//                            Toast.makeText(RealMapTileViewActivity.this, "中间点击", Toast.LENGTH_LONG).show();
                        } else {
//                            Toast.makeText(RealMapTileViewActivity.this, "靠右点击", Toast.LENGTH_LONG).show();
                            //靠右点击
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //当滑动时背景为选中状态 //检测是否长按,在非长按时检测
                    if (!isLongClickModule) {
                        isLongClickModule = isLongPressed(xDown, yDown, event.getX(),
                                event.getY(), event.getDownTime(), event.getEventTime(), 300);
                    }
                    if (isLongClickModule && !isLongClicking) {
                        //处理长按事件
                        Logger.d("weidu",""+(NORTH_WEST_LATITUDE+(SOUTH_EAST_LATITUDE-NORTH_WEST_LATITUDE)*(((v.getScrollY()+event.getY())/(fraction+0.0))/(tileView.getBaseHeight()+0.0))));
                        Logger.d("jingdu",""+(NORTH_WEST_LONGITUDE+(SOUTH_EAST_LONGITUDE-NORTH_WEST_LONGITUDE)*(((v.getScrollX()+event.getX())/(fraction+0.0))/(tileView.getBaseWidth()+0.0))));
                        Toast.makeText(RealMapTileViewActivity.this, "xDown:"+xDown+"yDown:"+yDown+"长按"+"getY:"+event.getY()+"getX:"+event.getX()+"getXReal"+(v.getScrollX()+event.getX())+"getYReal"+(v.getScrollY()+event.getY()), Toast.LENGTH_LONG).show();
                        isLongClicking = true;

//                        HotSpot hotSpot = new HotSpot();
//                        hotSpot.setTag( this );
//                        hotSpot.set( new Rect( 0, 0, 100, 100 ) );  // or any other API to define the region
//                        tileView.addHotSpot( hotSpot, new HotSpot.HotSpotTapListener(){
//                            @Override
//                            public void onHotSpotTap(HotSpot hotSpot, int x, int y) {
//                                Activity activity = (Activity) hotSpot.getTag();
//                                Logger.d( "HotSpotTapped", "With access through the tag API to the Activity " + activity );
//                            }
//
//
//                        });
                    }
                } else {
                    //其他模式
                }
                return false;

            }


        });
    }

    /* 判断是否有长按动作发生
* @param lastX 按下时X坐标
* @param lastY 按下时Y坐标
* @param thisX 移动时X坐标
* @param thisY 移动时Y坐标
* @param lastDownTime 按下时间
* @param thisEventTime 移动时间
* @param longPressTime 判断长按时间的阀值
*/
    private boolean isLongPressed(float lastX, float lastY,
                                  float thisX, float thisY,
                                  long lastDownTime, long thisEventTime,
                                  long longPressTime) {
        float offsetX = Math.abs(thisX - lastX);
        float offsetY = Math.abs(thisY - lastY);
        long intervalTime = thisEventTime - lastDownTime;
        if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime) {
            return true;
        }
        return false;
    }

    private MarkerLayout.MarkerTapListener markerTapListener = new MarkerLayout.MarkerTapListener() {

        @Override
        public void onMarkerTap(View view, int x, int y) {
            Logger.d("xxxxxxx","x"+x+"y"+y);
            // get reference to the TileView
            //得到的tileview参考

            TileView tileView = getTileView();
           /*
           当前缩放比 有可能为2.3 不一定我 1 2 4 8 16
            */
           float fraction=tileView.getDetailLevelManager().getScale();
            Logger.d("getScale222",""+fraction);
            Logger.d("getHeight",""+ tileView.getHeight());
            Logger.d("getWidth",""+ tileView.getWidth());
            Logger.d("getBaseHeight",""+  tileView.getBaseHeight());
            Logger.d("getBaseWidth",""+  tileView.getBaseWidth());
//            public static final double NORTH_WEST_LATITUDE = 116.11638888888889;//西北纬度
//            public static final double NORTH_WEST_LONGITUDE =  21.389722222222222;//西北经度
//            public static final double SOUTH_EAST_LATITUDE = 121.31666666666666;//东南纬度
//            public static final double SOUTH_EAST_LONGITUDE =  25.6775;//东南经度
   /*
           当前缩放 1 2 4 8 16
            */
            float scale2=tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();

            Logger.d("getScale222",""+fraction+"ddf"+tileView.getDetailLevelManager().getCurrentDetailLevel().getScale()+"sg"+tileView.getDetailLevelManager().getDetailLevelForScale().getScale());
            Logger.d("getHeight",""+ tileView.getHeight());
            Logger.d("getWidth",""+ tileView.getWidth());
            Logger.d("getBaseHeight",""+  tileView.getBaseHeight());
            Logger.d("getBaseWidth",""+  tileView.getBaseWidth());
//            public static final double NORTH_WEST_LATITUDE = 116.11638888888889;//西北纬度
//            public static final double NORTH_WEST_LONGITUDE =  21.389722222222222;//西北经度
//            public static final double SOUTH_EAST_LATITUDE = 121.31666666666666;//东南纬度
//            public static final double SOUTH_EAST_LONGITUDE =  25.6775;//东南经度
            int zoom=5;
            if(tileView.getDetailLevelManager().getCurrentDetailLevel().getScale()==1.0){
                zoom=5;
            }else    if(tileView.getDetailLevelManager().getCurrentDetailLevel().getScale()==2.0) {
                zoom = 6;
            }else    if(tileView.getDetailLevelManager().getCurrentDetailLevel().getScale()==4.0) {
                zoom = 7;
            }else    if(tileView.getDetailLevelManager().getCurrentDetailLevel().getScale()==8.0) {
                zoom = 8;
            }else    if(tileView.getDetailLevelManager().getCurrentDetailLevel().getScale()==16.0) {
                zoom = 9;
            }
            Logger.d("weidu",""+(NORTH_WEST_LATITUDE+(SOUTH_EAST_LATITUDE-NORTH_WEST_LATITUDE)*((y/(fraction+0.0))/(tileView.getBaseHeight()+0.0))));
            Logger.d("jingdu",""+(NORTH_WEST_LONGITUDE+(SOUTH_EAST_LONGITUDE-NORTH_WEST_LONGITUDE)*((x/(fraction+0.0))/(tileView.getBaseWidth()+0.0))));

            /*
            从这通过x y 算经纬度
             */
            Logger.d("getJingduhah","xxxxxxxxxxx"+(GetLongLati.pixelToLng((x/( fraction/scale2)+25*(256)*scale2),zoom))+"y"+(GetLongLati.pixelToLat((y/( fraction/scale2)+(11*(256)*scale2)),zoom)));
            Logger.d("weidu",""+(NORTH_WEST_LATITUDE+(SOUTH_EAST_LATITUDE-NORTH_WEST_LATITUDE)*((y/(fraction+0.0))/(tileView.getBaseHeight()+0.0))));
            Logger.d("jingdu",""+(NORTH_WEST_LONGITUDE+(SOUTH_EAST_LONGITUDE-NORTH_WEST_LONGITUDE)*((x/(fraction+0.0))/(tileView.getBaseWidth()+0.0))));
            Toast.makeText(RealMapTileViewActivity.this,(GetLongLati.pixelToLng((x/( fraction/scale2)+25*(256)*scale2),zoom))+"y"+(GetLongLati.pixelToLat((y/( fraction/scale2)+(11*(256)*scale2)),zoom)),Toast.LENGTH_SHORT).show();
//            public static final double NORTH_WEST_LONGITUDE = -75.17261900652977;//西北经度
//
//            float a=0;
//            if(fraction==16f) {
//               a= (float) GetLongLati.pixelToLat(y, 9);
//            } else if(fraction==8f) {
//               a= (float) GetLongLati.pixelToLat(y, 8);
//            }else   if(fraction==4f) {
//               a= (float) GetLongLati.pixelToLat(y,7);
//            }else   if(fraction==2f) {
//              a=  (float) GetLongLati.pixelToLat(y, 6);
//            } else if(fraction==1f) {
//               a= (float) GetLongLati.pixelToLat(y,5);
//            }
//            Logger.i("frkdkdkfg"+a);








//            public static final double SOUTH_EAST_LONGITUDE = -75.12462846235614;//东南经度
            // we saved the coordinate in the marker's tag
            //我们保存了标记的标签中的坐标
            double[] position = (double[]) view.getTag();
            // lets center the screen to that coordinate
            //让屏幕到该坐标
            tileView.slideToAndCenter(position[0], position[1]);
            // create a simple callout
            //创建一个简单的标注
            SampleCallout callout = new SampleCallout(view.getContext());
            // add it to the view tree at the same position and offset as the marker that invoked it
            //将它添加到与调用它的标记相同的位置和偏移量的视图树中
            tileView.addCallout(callout, position[0], position[1], -0.5f, -1.0f);
            // a little sugar
            callout.transitionIn();
            // stub out some text
            //短一些文本
            callout.setTitle("MAP CALLOUT");
            callout.setSubtitle("Info window at coordinate:\n" + position[1] + ", " + position[0]);
        }
    };

    // a list of points to demonstrate markers and paths
    //显示标记和路径的点列表
    private ArrayList<double[]> points = new ArrayList<>();

    {



        points.add(new double[]{(120.0-GetLongLati.getLong(25,0,5))/((GetLongLati.getLong(28,0,5)-GetLongLati.getLong(25,0,5)))*256*3, GetLongLati.getY(24,5)-11*256});
        points.add(new double[]{(118.0-GetLongLati.getLong(25,0,5))/((GetLongLati.getLong(28,0,5)-GetLongLati.getLong(25,0,5)))*256*3, GetLongLati.getY(23,5)-11*256});
        points.add(new double[]{(116.0-GetLongLati.getLong(25,0,5))/((GetLongLati.getLong(28,0,5)-GetLongLati.getLong(25,0,5)))*256*3, GetLongLati.getY(22,5)-11*256});
        points.add(new double[]{(120.0-GetLongLati.getLong(25,0,5))/((GetLongLati.getLong(28,0,5)-GetLongLati.getLong(25,0,5)))*256*3, GetLongLati.getY(23.5,5)-11*256});
        points.add(new double[]{(118.0-GetLongLati.getLong(25,0,5))/((GetLongLati.getLong(28,0,5)-GetLongLati.getLong(25,0,5)))*256*3, GetLongLati.getY(23.2,5)-11*256});
        points.add(new double[]{(116.0-GetLongLati.getLong(25,0,5))/((GetLongLati.getLong(28,0,5)-GetLongLati.getLong(25,0,5)))*256*3, GetLongLati.getY(22.3,5)-11*256});

    }



}