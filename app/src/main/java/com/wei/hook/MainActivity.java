package com.wei.hook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wei.hook.controller.AIDLController;
import com.wei.hook.util.NetworkUtils;
import com.wei.hook.util.ShellUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends BaseActivity  {
    private TextView mLocationTv, mImeiTv, mTipsTv;
    private Button mOpenADBBtn;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // AIDL 调用 。 需要 WanAndroid 先启动，作为服务端
//        AIDLController.invokeAIDL(this);
    }

    @Override
    protected void initView() {
        mImeiTv = findViewById(R.id.tv_imei);
        mLocationTv = findViewById(R.id.tv_location);
        mTipsTv = findViewById(R.id.tv_tips);
        mOpenADBBtn = findViewById(R.id.btn_open);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (location != null)
        {
            showLocation(location);
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        mImeiTv.setText("IMEI : " + imei);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        simulationClick(mOpenADBBtn, mOpenADBBtn.getX(), mOpenADBBtn.getY());

    }

    /**
     * 模拟点击
     * @param view
     * @param x
     * @param y
     */
    private void simulationClick(View view, float x, float y) {
        Log.e(TAG, x + ", " + y);
        long downTime = SystemClock.uptimeMillis();

        MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;

        MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);

        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    private void showLocation(Location location)
    {
        StringBuilder builder = new StringBuilder()
                .append("经度 ： ")
                .append(location.getLongitude())
                .append("  纬度 ：")
                .append(location.getLatitude());
        mLocationTv.setText(builder);
    }

    @Override
    protected void initListener() {
        mOpenADBBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_open:
                String tips;
                if (canOpenADB())
                {
                    tips = String.format("请在PC终端输入:\nadb connect %s:5555", NetworkUtils.getLocalIpAddress(this));
                }
                else
                {
                    tips = "请检查手机是否已root并且app已经获取root权限！";
                }
                mTipsTv.setText(tips);
                break;

                default:
        }
    }

    private boolean canOpenADB() {
        String[] commandList = new String[]{
                "setprop service.adb.tcp.port 5555",
                "stop adbd",
                "start adbd"
        };
        return ShellUtil.exeCmdByRoot(commandList);
    }
}
