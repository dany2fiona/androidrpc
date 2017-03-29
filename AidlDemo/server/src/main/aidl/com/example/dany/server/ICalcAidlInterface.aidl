// ICalcAidlInterface.aidl
package com.example.dany.server;

// Declare any non-default types here with import statements

interface ICalcAidlInterface {

    int calc(int a, int b);

    int getA();

    int getC();

    boolean isCalcOver();

}
