package com.wei.hook.util;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.lang.reflect.Field;

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

        hookMethod("com.wei.permissionsetting.permission.activity.MainActivity",
                loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Log.e(TAG, "--- afterHookedMethod ---" + param.args.length);
                        Field field = param.thisObject.getClass().getDeclaredField("mSetPermissionBtn");
                        if ( field != null && Button.class == field.getType())
                        {
                            field.setAccessible(true);
                            Button button = (Button) field.get(param.thisObject);
                            Log.e(TAG, "目标按钮是否已注册过监听事件？ " + button.hasOnClickListeners());
                            // 0x7f080023(十六进制) == 2131230755(十进制)
                            Log.e(TAG, "目标按钮id值：" + button.getId());
                            button.performClick();
                        }
                    }
                });
    }
}
