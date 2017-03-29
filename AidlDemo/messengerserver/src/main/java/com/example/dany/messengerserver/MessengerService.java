package com.example.dany.messengerserver;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by dan.y on 2017/3/27.
 */

public class MessengerService extends Service {
    private static final int MSG_SUM = 0x110;
    private Messenger mMessenger = new Messenger(new Handler(){ //最好换成HandlerThread的形式
        @Override
        public void handleMessage(Message msgFromClient) {
            Message msgToClient = Message.obtain(msgFromClient);
            switch (msgFromClient.what){
                case MSG_SUM:
                    msgToClient.what = MSG_SUM;
                    try {
                        //模拟耗时
                        Thread.sleep(2000);
                        msgToClient.arg2 = msgFromClient.arg1 + msgFromClient.arg2;
                        msgFromClient.replyTo.send(msgToClient);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            super.handleMessage(msgFromClient);
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
