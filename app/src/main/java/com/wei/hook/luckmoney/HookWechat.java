package com.wei.hook.luckmoney;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.wei.hook.util.BaseUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author: WEI
 * @date: 2018/2/8
 */

public class HookWechat extends BaseUtil {
    private static HookWechat sWechat;
    public static HookWechat getInstance()
    {
        if (sWechat == null)
        {
            sWechat = new HookWechat();
        }
        return sWechat;
    }

    public void getOpenBtnId(XC_LoadPackage.LoadPackageParam loadPackageParam)
    {
        hookMethod("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI",
                loadPackageParam.classLoader, "initView", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        super.beforeHookedMethod(param);
                        Log.e(TAG, "--- beforeHookedMethod ---");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        super.afterHookedMethod(param);
                        Log.e(TAG, "--- afterHookedMethod ---");
                        Object obj = param.thisObject;
                        Activity activity = (Activity) obj;

                        Method[] methods = activity.getClass().getDeclaredMethods();
                        for (Method method:methods) {
                            Log.e(TAG, method.getName() + ", " + method.getDeclaredAnnotations());
                        }

                        Field[] fields = activity.getClass().getDeclaredFields();
                        for (Field field:fields) {
                            Log.e(TAG, field.getName() + ", " + field.getType());
                        }

                        Field field = activity.getClass().getDeclaredField("ogX");
                        if (field != null)
                        {
                            field.setAccessible(true);
                            if ( field.getType() == Button.class )
                            {
                                // 2131758897
                                Button button1 = (Button) field.get(obj);
                                Log.e(TAG, "openid1 : " + button1.getId());
                                button1.performClick();
                            }
                        }
                    }
                });

        /**
         * 劫持滚动对话框，获取文本信息
          */
        hookMethod("com.tencent.mm.ui.base.r", loadPackageParam.classLoader,
                "setMessage", CharSequence.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e(TAG, "setMessage : " + param.getResult());
                Object o = param.thisObject;
                ProgressDialog progressDialog = (ProgressDialog) o;
                Field field = progressDialog.getClass().getDeclaredField("xHZ");
                if (field != null)
                {
                    field.setAccessible(true);
                    if ( field.getType() == TextView.class )
                    {
                        // 正在加载...
                        TextView txt = (TextView) field.get(o);
                        Log.e(TAG, "content : " + txt.getText() + "");
                    }
                }

            }
        });
    }

}
