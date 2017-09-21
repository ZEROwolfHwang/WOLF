package com.yisipu.chartmap;

import android.app.Activity;
import android.os.Bundle;
import android.os.UEventObserver;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class LsActivity extends Activity {
    private static final String TAG = "LsActivity";
    public static final int STATE_WATER = 0;
    public static final int STATE_NORMAL = 1;
    private int mCurrentState = STATE_NORMAL;
    Button button;
    String fg=null;
    private static final String STATE_PATCH = "/sys/class/switch/water_det_switch/state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ls);
        mCurrentState = checkHallState();
        button = (Button) findViewById(R.id.ls_bt);

        if (new File(STATE_PATCH).exists()) {

            uEventObserver.startObserving("DEVPATH=/devices/virtual/switch/water_det_switch");
            Logger.i(TAG, "uEventObserver start Oberving  water_det_switch");

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LsActivity.this,""+mCurrentState,Toast.LENGTH_SHORT).show();
            }
        });
    }




    public static int checkHallState() {
        if (new File(STATE_PATCH).exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(
                        STATE_PATCH));
                String readLine = reader.readLine();
                Logger.i(TAG, "readLine =" + readLine);
                if (readLine != null && readLine.trim().equals("0")) {//close
                    return STATE_WATER;
                } else {
                    return STATE_NORMAL;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        } else {
            Logger.i(TAG, "not exit");
        }
        return STATE_NORMAL;
    }


    private UEventObserver uEventObserver = new UEventObserver() {

        @Override
        public void onUEvent(UEvent event) {
            Logger.i("UEventObserver", "uEventObserver onUEvent()   " + event);
            int state = "0".equals(event.get("SWITCH_STATE")) ? STATE_WATER
                    : STATE_NORMAL;
            Logger.i(TAG, "uEventObserver onUEvent() state  " + state);
            if (mCurrentState != state) {
                mCurrentState = state;

            }
        }

    };


}