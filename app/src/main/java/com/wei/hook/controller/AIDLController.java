package com.wei.hook.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.wei.wanandroid.ICalcAIDL;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: WEI
 * @date: 2018/3/1
 */

public class AIDLController
{
    private final static String TAG = "AIDLController";
    static int x = 0, y = 0;

    public static void invokeAIDL(Context context) {
        Intent intent = new Intent("com.wei.wanandroid.CALC_AIDL");
        intent.setPackage("com.wei.wanandroid");
        context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    static ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected : " + name.getClassName());
            final ICalcAIDL iCalcAIDL = ICalcAIDL.Stub.asInterface(service);
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(0);
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        int addResult = iCalcAIDL.add(x, y);
                        int minResult = iCalcAIDL.min(x, y);
                        Log.e(TAG, x + " + " + y + " = " + addResult);
                        Log.e(TAG, x + " - " + y + " = " + minResult);
                        x += Math.random() * 10;
                        y += Math.random() * 10;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }, 1, 3, TimeUnit.SECONDS);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected : " + name.getClassName());
        }
    };
}
