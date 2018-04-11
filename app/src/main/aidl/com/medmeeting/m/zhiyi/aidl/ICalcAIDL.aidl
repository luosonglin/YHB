// ICalcAIDL.aidl
package com.medmeeting.m.zhiyi.aidl;

// Declare any non-default types here with import statements

interface ICalcAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

            void add(int x, int y);
            void min(int x,int y);
}
