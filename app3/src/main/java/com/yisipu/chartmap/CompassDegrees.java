package com.yisipu.chartmap;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * 电子罗盘 方向传感器
 */
public class CompassDegrees extends Activity implements SensorEventListener {
    private ImageView imageView;
    private float currentDegree = 0f;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass);
        imageView = (ImageView) findViewById(R.id.compass_imageView);

        // 传感器管理器
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 注册传感器(Sensor.TYPE_ORIENTATION(方向传感器);SENSOR_DELAY_FASTEST(0毫秒延迟);
        // SENSOR_DELAY_GAME(20,000毫秒延迟)、SENSOR_DELAY_UI(60,000毫秒延迟))
        sm.registerListener( CompassDegrees.this,
                sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);

    }
    //传感器报告新的值(方向改变)
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = event.values[0];

			/*
			RotateAnimation类：旋转变化动画类

			参数说明:

			fromDegrees：旋转的开始角度。
			toDegrees：旋转的结束角度。
			pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
			pivotXValue：X坐标的伸缩值。
			pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
			pivotYValue：Y坐标的伸缩值
			*/
            RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            //旋转过程持续时间
            ra.setDuration(200);
            //罗盘图片使用旋转动画
            imageView.startAnimation(ra);

            currentDegree = -degree;
        }
    }
    //传感器精度的改变
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}