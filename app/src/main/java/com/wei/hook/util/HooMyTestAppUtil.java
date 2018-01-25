package com.wei.hook.util;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author: WEI
 * @date: 2018/1/25
 */
public class HooMyTestAppUtil extends BaseUtil {
    private static HooMyTestAppUtil sHooMyTestAppUtil = null;

    public static HooMyTestAppUtil getInstance() {
        if (sHooMyTestAppUtil == null) {
            sHooMyTestAppUtil = new HooMyTestAppUtil();
        }
        return sHooMyTestAppUtil;
    }


    public void changeMethods(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        hookMethod("com.wei.permissionsetting.permission.activity.MainActivity",
                loadPackageParam.classLoader, "isShouldShowToast", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Log.e(TAG, "--- beforeHookedMethod ---" + param.getResultOrThrowable());
                        param.setResult(true);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Log.e(TAG, "--- afterHookedMethod ---" + param.getResultOrThrowable());
                        param.setResult(true);
                    }
                });
    }
}
