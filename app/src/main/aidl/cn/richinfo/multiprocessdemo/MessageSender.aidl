// MessageSender.aidl
package cn.richinfo.multiprocessdemo;
import cn.richinfo.multiprocessdemo.data.MessageModel;
import cn.richinfo.multiprocessdemo.MessageReceiver;

// Declare any non-default types here with import statements

interface MessageSender {
     void sendMessage(in MessageModel model);
     void registerReceiveListener(MessageReceiver receiver);
     void unregisterReceiveListener(MessageReceiver receiver);
}
