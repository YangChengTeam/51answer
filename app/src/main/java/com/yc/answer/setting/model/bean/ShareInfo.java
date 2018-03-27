package com.yc.answer.setting.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanglin  on 2018/3/15 14:56.
 */

public class ShareInfo implements Parcelable {
    private String url;
    private String title;
    private String content;
    private String img;// 分享图片;
    private String book_id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.img);
        dest.writeString(this.book_id);
    }

    public ShareInfo() {
    }

    protected ShareInfo(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.img = in.readString();
        this.book_id = in.readString();
    }

    public static final Parcelable.Creator<ShareInfo> CREATOR = new Parcelable.Creator<ShareInfo>() {
        @Override
        public ShareInfo createFromParcel(Parcel source) {
            return new ShareInfo(source);
        }

        @Override
        public ShareInfo[] newArray(int size) {
            return new ShareInfo[size];
        }
    };
}
