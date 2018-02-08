package com.wei.hook;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import com.wei.hook.luckmoney.HookWechat;
import com.wei.hook.util.ChangeLocationUtil;
import com.wei.hook.util.HooMyTestAppUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 只要新建一个实现IXposedHookLoadPackage接口的类，然后在handleLoadPackage回调方法中进行拦截操作即可，
 * 而具体的拦截操作是借助XposedHelpers.findAndHookMethod方法和XposedBridge.hookMethod方法实现的，
 * 这两个方法也是比较简单的，从参数含义可以看到，主要是Hook的类名和方法名，然后还有一个就是拦截的回调方法，
 * 一般是拦截之前做什么的一个beforeHookedMethod方法和拦截之后做什么的一个afterHookedMethod方法。
 * @author: WEI
 * @date: 2018/1/22
 */
public class Hook implements IXposedHookLoadPackage
{
    private final String TAG = getClass().getSimpleName();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable
    {
        String pkgName = loadPackageParam.packageName;
        Log.e(TAG, "pkg : " + pkgName);
//        if (!"com.wei.permissionsetting".equals(pkgName))
//        {
//            return;
//        }
//        // 拦截位置并修改，以欺骗其它定位工具
//        ChangeLocationUtil.getInstance().changeLocation(loadPackageParam);
//        HooMyTestAppUtil.getInstance().changeMethods(loadPackageParam);

        if ("com.tencent.mm".equals(pkgName))
        {
            // 拦截微信
            HookWechat.getInstance().getOpenBtnId(loadPackageParam);
        }

    }


}