// ICalcAIDL.aidl
package com.wei.wanandroid;

// Declare any non-default types here with import statements

interface ICalcAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    //在aidl文件写要被调用的方法,此方法不能有权限修饰符
    int add(int x, int y);
    int min(int x, int y);
}
