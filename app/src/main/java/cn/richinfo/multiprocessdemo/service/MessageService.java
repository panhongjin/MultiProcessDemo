package cn.richinfo.multiprocessdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import cn.richinfo.multiprocessdemo.MessageReceiver;
import cn.richinfo.multiprocessdemo.MessageSender;
import cn.richinfo.multiprocessdemo.data.MessageModel;

public class MessageService extends Service {
    private static final String TAG = "MessageService";

    private AtomicBoolean serviceStop = new AtomicBoolean(false);
    private RemoteCallbackList<MessageReceiver> listenerList = new RemoteCallbackList<>();

    public MessageService() {
    }

    IBinder messageSender = new MessageSender.Stub(){
        @Override
        public void sendMessage(MessageModel model) throws RemoteException {
            Log.e(TAG, model.toString());
        }

        @Override
        public void registerReceiveListener(MessageReceiver receiver) throws RemoteException {
            Log.e(TAG, "registerReceiveListener");
            listenerList.register(receiver);
        }

        @Override
        public void unregisterReceiveListener(MessageReceiver receiver) throws RemoteException {
            Log.e(TAG, "unregisterReceiveListener");
            listenerList.unregister(receiver);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

            return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return messageSender;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new FakeTCPTask()).start();
    }

    @Override
    public void onDestroy() {
        serviceStop.set(true);
        super.onDestroy();
    }

    private class FakeTCPTask implements Runnable {
        @Override
        public void run() {
            while (!serviceStop.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                MessageModel messageModel = new MessageModel();
                messageModel.setFrom("Server");
                messageModel.setTo("client");
                messageModel.setContent(String.valueOf(System.currentTimeMillis()));

                final int listenerCount = listenerList.beginBroadcast();
                Log.e(TAG, "listenerCount is " + listenerCount);
                for(int i = 0; i < listenerCount; i++) {
                    MessageReceiver messageReceiver = listenerList.getBroadcastItem(i);
                    if (messageReceiver != null) {
                        try {
                            messageReceiver.onMessageReceived(messageModel);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    }
                }
                listenerList.finishBroadcast();
            }
        }
    }
}
