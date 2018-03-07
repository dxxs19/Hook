package com.wei.wanandroid.activity.ndk;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wei.hook.R;
import com.wei.wanandroid.activity.BaseActivity;

public class JNIActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);
    }

    @Override
    public void initView()
    {
        String code = getAppID();
        Log.e(TAG, "code : " + code);
        setAppID("999999999");
        invokeSuperClassMethodShowMsg();
    }

    static {
        System.loadLibrary("jniTest");
    }

    private native String getAppID();
    private void invokedByNative(String msg)
    {
        Log.e(TAG, "底层回调信息 ： " + msg);
        showMsg(msg, Toast.LENGTH_LONG);
    }

    private static native String setAppID(String id);
    private static void changeId(String id)
    {
        Log.e(JNIActivity.class.getSimpleName(), "id 变成了 : " + id);
    }

    private native String invokeSuperClassMethodShowMsg();
}
