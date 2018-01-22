package com.wei.hook;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

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
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Log.e(TAG, "pkg : " + loadPackageParam.packageName);
        hookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getDeviceId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                Log.e(TAG, "hook getDeviceId");
                Object obj = param.getResult();
                Log.e(TAG, "imei : " + obj);
                param.setResult("123456789");
            }
        });

        hookMethods("android.location.LocationManager", "getLastKnownLocation", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                Log.e(TAG, "hook getLastKnownLocation");
                Location location = new Location(LocationManager.PASSIVE_PROVIDER);
                double lon = 10d;
                double lat = 20d;
                location.setLongitude(lon);
                location.setLatitude(lat);
                param.setResult(location);
            }
        });

        hookMethods("android.location.LocationManager", "requestLocationUpdates", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                Log.e(TAG, "hook requestLocationUpdates" + ", param.args[0] " + param.args[0]);
                if (param.args.length == 4 && ( param.args[0] instanceof  String))
                {
                    LocationListener locationListener = (LocationListener) param.args[3];
                    Class<?> clazz = LocationListener.class;
                    Method method = null;
                    for (Method method1 : clazz.getDeclaredMethods())
                    {
                        if (method1.getName().equals("onLocationChanged"))
                        {
                            method = method1;
                            break;
                        }
                    }
                    try {
                        if (method != null)
                        {
                            Object[] args = new Object[1];
                            Location location = new Location(LocationManager.PASSIVE_PROVIDER);
                            double lon = System.currentTimeMillis();
                            double lat = System.currentTimeMillis()/1000;
                            location.setLongitude(lon);
                            location.setLatitude(lat);
                            args[0] = location;
                            method.invoke(locationListener, args);
                        }
                    }catch (Exception e)
                    {
                        XposedBridge.log(e);
                    }
                }
            }
        });

    }

    private void hookMethod (String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback)
    {
        try {
            XposedHelpers.findAndHookMethod(className, classLoader, methodName, parameterTypesAndCallback);
        }catch (Exception e)
        {
            XposedBridge.log(e);
        }
    }

    private void hookMethods(String className, String methodName, XC_MethodHook callback)
    {
        try {
            Class<?> clazz = Class.forName(className);
            for (Method method : clazz.getDeclaredMethods())
            {
                if (method.getName().equals(methodName) && !Modifier.isAbstract(method.getModifiers()) && Modifier.isPublic(method.getModifiers()))
                {
                    XposedBridge.hookMethod(method, callback);
                }
            }
        } catch (Exception e) {
            XposedBridge.log(e);
        }

    }
}
