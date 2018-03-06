//
// Created by Administrator on 2018/3/6.
//
#include "com_wei_hook_MainActivity.h"

JNIEXPORT jstring JNICALL Java_com_wei_hook_MainActivity_getAppID (JNIEnv *env, jobject jobj)
{
    jstring code = (*env) -> NewStringUTF(env, "1236547895");
    return code;
}

