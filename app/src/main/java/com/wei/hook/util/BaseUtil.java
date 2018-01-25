package com.wei.hook.util;

import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * @author: WEI
 * @date: 2018/1/22
 */

public class BaseUtil
{
    protected String TAG = getClass().getSimpleName();

    protected void hookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback)
    {
        try {
            XposedHelpers.findAndHookMethod(className, classLoader, methodName, parameterTypesAndCallback);
        }catch (Exception e)
        {
//            XposedBridge.log(e);
            Log.e(TAG, e.getMessage());
        }
    }

    protected void hookMethods(String className, String methodName, XC_MethodHook callback)
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
//            XposedBridge.log(e);
            Log.e(TAG, e.getMessage());
        }

    }
}
