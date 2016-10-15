package com.seki.noteasklite.DataUtil.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yuan on 2016/6/9.
 */
public class NoteLabelBean implements Parcelable {
    long id;
    String label;
    int noteNum;
    String createTime;

    public NoteLabelBean(long id,String label, int noteNum, String createTime) {
        this.id = id;
        this.label = label;
        this.noteNum = noteNum;
        this.createTime = createTime;
    }

    public NoteLabelBean() {
        this.id = -1;
        this.label = "";
        this.noteNum = -1;
        this.createTime = "";
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setNoteNum(int noteNum) {
        this.noteNum = noteNum;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLabel() {

        return label;
    }

    public int getNoteNum() {
        return noteNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(this.label);
        dest.writeInt(this.noteNum);
        dest.writeString(this.createTime);
    }

    protected NoteLabelBean(Parcel in) {
        this.id = in.readLong();
        this.label = in.readString();
        this.noteNum = in.readInt();
        this.createTime = in.readString();
    }

    public static final Parcelable.Creator<NoteLabelBean> CREATOR = new Parcelable.Creator<NoteLabelBean>() {
        @Override
        public NoteLabelBean createFromParcel(Parcel source) {
            return new NoteLabelBean(source);
        }

        @Override
        public NoteLabelBean[] newArray(int size) {
            return new NoteLabelBean[size];
        }
    };
}
