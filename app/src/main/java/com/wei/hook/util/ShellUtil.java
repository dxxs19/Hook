package com.wei.hook.util;

import java.io.DataOutputStream;

/**
 * Created by Administrator on 2018-01-28.
 */

public class ShellUtil {
    /**
     * 执行shell指令
     * @param strings 指令集
     * @return 指令集是否执行成功
     */
    public static boolean exeCmdByRoot(String... strings) {
        try {
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                outputStream.writeBytes(s + "\n");
                outputStream.flush();
            }
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            su.waitFor();
            outputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
