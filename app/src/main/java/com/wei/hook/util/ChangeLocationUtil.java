package com.wei.hook.util;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 随意修改位置
 * @author: WEI
 * @date: 2018/1/22
 */

public class ChangeLocationUtil extends BaseUtil
{
    private static ChangeLocationUtil mChangeLocationUtil = null;

    public static ChangeLocationUtil getInstance()
    {
        if (mChangeLocationUtil == null)
        {
            mChangeLocationUtil = new ChangeLocationUtil();
        }
        return mChangeLocationUtil;
    }

    public void changeLocation(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        hookMethod("android.net.wifi.WifiManager", loadPackageParam.classLoader, "getScanResults", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(null);
            }
        });

        hookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getCellLocation", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(null);
            }
        });

        hookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getNeighboringCellInfo", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(null);
            }
        });

        hookMethods("android.location.LocationManager", "getLastKnownLocation", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e(TAG, "hook getLastKnownLocation");
                Location location = new Location(LocationManager.PASSIVE_PROVIDER);
                double lon = 120d;
                double lat = 30d;
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
                            Location location = new Location(LocationManager.GPS_PROVIDER);
                            double lon = 120.53d;
                            double lat = 25.04d;
                            location.setLongitude(lon);
                            location.setLatitude(lat);
                            args[0] = location;
                            method.invoke(locationListener, args);
                            XposedBridge.log("current location : " + lat + ", " + lon);
                        }
                    }catch (Exception e)
                    {
                        XposedBridge.log(e);
                    }
                }
            }
        });
    }

}
