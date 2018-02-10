package com.wei.hook.util;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: WEI
 * @date: 2018/2/10
 */

public class FileUtil
{
    public static void saveFile(String content)
    {
        String filePath;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator + "wechat_log.txt";
        }
        else
        {
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "wechat_log.txt";
        }

        File file = new File(filePath);
        if (!file.exists())
        {
            File dir = new File(file.getParent());
            dir.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.write("\n".getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
