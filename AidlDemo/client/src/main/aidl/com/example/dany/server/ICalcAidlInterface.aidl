// ICalcAidlInterface.aidl
package com.example.dany.server;

// Declare any non-default types here with import statements

interface ICalcAidlInterface {

    void calc(int a, int b);

    int getA();

    int getC();

    boolean isCalcOver();

}
