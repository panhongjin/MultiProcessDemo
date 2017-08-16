// MessageReceived.aidl
package cn.richinfo.multiprocessdemo;
import cn.richinfo.multiprocessdemo.data.MessageModel;

// Declare any non-default types here with import statements

interface MessageReceiver {
    void onMessageReceived(in MessageModel model);
}
