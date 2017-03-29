package com.example.dany.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dany.server.ICalcAidlInterface;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mLyContainer;
    private int mA;
    private boolean isconn;
    private TextView mTipTv;
    private ICalcAidlInterface iService;
    private final static int MSG = 0x0010;
    private final static int  MSG_SEND = 0x0011;
    private HandlerThread mHandlerThread;
    private Handler mThreadHandler;//后台获取处理数据handler
    private Handler mHandler = new Handler(){//UI线程交互的handler.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG:
                    try {
                        if(iService.isCalcOver()){
                            TextView tv = (TextView) mLyContainer.findViewById(iService.getA());
                            tv.setText(tv.getText() + "=>" + iService.getC());
                        }else{
                            mHandler.sendEmptyMessage(MSG);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iService = ICalcAidlInterface.Stub.asInterface(service);
            mTipTv.setText("连接成功！");
            isconn = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iService = null;
            mTipTv.setText("断开连接！");
            isconn = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTipTv = (TextView) findViewById(R.id.tv_tip);
        mLyContainer = (LinearLayout) findViewById(R.id.id_ll_container);
        Intent intent = new Intent();
        intent.setAction("com.danny.aidl.calc");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Intent mIntent = IntentUtils.getExplicitIntent(MainActivity.this,intent);
        bindService(mIntent,connection,BIND_AUTO_CREATE);

        mHandlerThread = new HandlerThread("check-message-coming");
        mHandlerThread.start();
        mThreadHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_SEND:
                        try {
                            iService.calc(msg.arg1, msg.arg2);
                            mHandler.sendEmptyMessage(MSG);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final int a = mA++;
                    final int b = (int) (Math.random() * 100);

                    //创建一个tv,添加到LinearLayout中
                    TextView tv = new TextView(MainActivity.this);
                    tv.setText(a + " + " + b + " = caculating ...");
                    tv.setId(a);
                    mLyContainer.addView(tv);
                    Message msg = new Message();
                    msg.what = MSG_SEND;
                    msg.arg1 = a;
                    msg.arg2 = b;
                    mThreadHandler.sendMessage(msg);
                }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();
    }
}
