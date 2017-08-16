package cn.richinfo.multiprocessdemo.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : Pan
 * time   : 2017/8/15
 * desc   : xxxx描述
 * version: 1.0
 * <p>
 * Copyright: Copyright (c) 2017
 * Company:深圳彩讯科技有限公司
 */

public class MessageModel implements Parcelable {
    private String from;
    private String  to;
    private String content;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(from);
        parcel.writeString(to);
        parcel.writeString(content);
    }

    public static final Parcelable.Creator<MessageModel> CREATOR
            = new Parcelable.Creator<MessageModel>(){
        @Override
        public MessageModel createFromParcel(Parcel parcel) {
            return new MessageModel(parcel);
        }

        @Override
        public MessageModel[] newArray(int i) {
            return new MessageModel[0];
        }
    };

    private MessageModel(Parcel in) {
        from = in.readString();
        to = in.readString();
        content = in.readString();
    }

    public MessageModel() {
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
