package com.seki.noteasklite.DataUtil;

import android.os.Parcel;
import android.os.Parcelable;

import com.seki.noteasklite.DataUtil.Bean.DeleteNoteBean;

/**
 * Created by Seki on 2015/10/11.
 */
public class NoteAllArray implements Parcelable {
    public String title;
    public String content;
    public String group;
    public String date;
    public String time;
    public long sdfId;
    public String isOnCloud;
    public String uuid;

    public NoteAllArray() {
        this.title = "";
        this.content = "";
        this.group = "";
        this.date = "";
        this.time = "";
        this.sdfId = -1;
        this.isOnCloud = "false";
        this.uuid = "";
    }

    public NoteAllArray(String title, String content, String group, String date, String time, long sdfId, String isOnCloud, String uuid) {
        this.title = title;
        this.content = content;
        this.group = group;
        this.date = date;
        this.time = time;
        this.sdfId = sdfId;
        this.isOnCloud = isOnCloud;
        this.uuid = uuid;
    }
    public NoteAllArray(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NoteAllArray(NoteDatabaseArray noteDatabaseArray, long id) {
        this.title = noteDatabaseArray.Title;
        this.content = noteDatabaseArray.content;
        this.group = noteDatabaseArray.group;
        this.date = noteDatabaseArray.date;
        this.time = noteDatabaseArray.time;
        this.sdfId = id;
        this.isOnCloud = noteDatabaseArray.is_on_cloud;
        this.uuid = noteDatabaseArray.uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.group);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeLong(this.sdfId);
        dest.writeString(this.isOnCloud);
        dest.writeString(this.uuid);
    }

    protected NoteAllArray(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.group = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.sdfId = in.readLong();
        this.isOnCloud = in.readString();
        this.uuid = in.readString();
    }

    public static final Creator<NoteAllArray> CREATOR = new Creator<NoteAllArray>() {
        public NoteAllArray createFromParcel(Parcel source) {
            return new NoteAllArray(source);
        }

        public NoteAllArray[] newArray(int size) {
            return new NoteAllArray[size];
        }
    };

    public DeleteNoteBean createDeleteNoteBean() {
        return new DeleteNoteBean(sdfId, group, uuid);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof NoteAllArray){
            return
                    ((NoteAllArray) o).content.equals(content)&&
                            ((NoteAllArray) o).date.equals(date)&&
                            ((NoteAllArray) o).group.equals(group)&&
                            ((NoteAllArray) o).isOnCloud.equals(isOnCloud)&&
                            ((NoteAllArray) o).sdfId==sdfId&&
                            ((NoteAllArray) o).time.equals(time)&&
                            ((NoteAllArray) o).title.equals(title)&&
                            ((NoteAllArray) o).uuid.equals(uuid);
        }
        return super.equals(o);
    }
}
