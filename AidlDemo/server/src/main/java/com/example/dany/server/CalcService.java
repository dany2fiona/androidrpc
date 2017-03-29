package com.example.dany.server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/28.
 */

public class CalcService extends Service {
    private final static String TAG = "CalcService";
    private int _a;
    private int c;
    private boolean isCalcOver = false;
    private ICalcAidlInterface.Stub bind = new ICalcAidlInterface.Stub() {
        @Override
        public int calc(int a, int b) throws RemoteException {
            isCalcOver = false;
            _a = a;
            c = a + b;
            try {
                //模拟耗时
                Thread.sleep(2000);
                c = a + b;
                isCalcOver = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return c;
        }

        @Override
        public int getA() throws RemoteException {
            return _a;
        }

        @Override
        public int getC() throws RemoteException {
            return c;
        }

        @Override
        public boolean isCalcOver() throws RemoteException {
            return isCalcOver;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"ServiceServer onBind");
        return bind;
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"ServiceServer onCreate");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG,"ServiceServer onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"ServiceServer onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"ServiceServer onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"ServiceServer onDestroy");
        super.onDestroy();
    }
}
