package cn.richinfo.multiprocessdemo.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.richinfo.multiprocessdemo.MessageReceiver;
import cn.richinfo.multiprocessdemo.MessageSender;
import cn.richinfo.multiprocessdemo.R;
import cn.richinfo.multiprocessdemo.data.MessageModel;
import cn.richinfo.multiprocessdemo.service.MessageService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MessageSender messageSender;

    IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e(TAG, "binderDied");
            if (messageSender != null) {
                messageSender.asBinder().unlinkToDeath(this, 0);
                messageSender = null;
            }
            setup();
        }
    };

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e(TAG, "onServiceConnected");

            messageSender = MessageSender.Stub.asInterface(iBinder);

            MessageModel messageModel = new MessageModel();
            messageModel.setFrom("client user id");
            messageModel.setTo("receiver user id");
            messageModel.setContent("This is message content");
            try {
                messageSender.asBinder().linkToDeath(deathRecipient, 0);
                messageSender.registerReceiveListener(messageReceiver);
                messageSender.sendMessage(messageModel);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(TAG, "onServiceDisconnected");
        }
    };

    private MessageReceiver messageReceiver = new MessageReceiver.Stub(){
        @Override
        public void onMessageReceived(MessageModel model) throws RemoteException {
            Log.e(TAG, "onMessageReceived : " + model.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    @Override
    protected void onDestroy() {
        if (messageSender != null && messageSender.asBinder().isBinderAlive()) {
            try {
                messageSender.unregisterReceiveListener(messageReceiver);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(connection);
        super.onDestroy();
    }

    private void setup() {
        Intent intent = new Intent(this, MessageService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }


}
