package com.seki.noteasklite.DataUtil.Bean;

import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;

/**
 * Created by yuan on 2016/5/24.
 */
public class NoteDateItemArray {
    public String title;
    public String detail;
    public String time;
    public String group;
    public long sdfId;
    public String isOnCloud;
    public String uuid;

    public NoteDateItemArray(String title, String time, String detail, String group, long sdfId, String isOnCloud, String uuid) {
        this.title = title;
        this.isOnCloud = isOnCloud;
        this.detail = detail;
        this.time = time;
        this.group = group;
        this.sdfId = sdfId;
        this.uuid = uuid;
    }
    public NoteDateItemArray(NoteDatabaseArray noteDatabaseArray, long noteId){
        this.title=noteDatabaseArray.Title;
        this.group=noteDatabaseArray.group;
        this.isOnCloud=noteDatabaseArray.is_on_cloud;
        this.detail=noteDatabaseArray.content;
        this.time=noteDatabaseArray.time;
        this.sdfId=noteId;
        this.uuid = noteDatabaseArray.uuid;
    }
    public NoteAllArray toNoteAllArray(){
        return new NoteAllArray(title,detail,group,"",time,sdfId,isOnCloud,uuid);
    }

    public DeleteNoteBean createDeleteNoteBean(){
        return new DeleteNoteBean(sdfId,group,uuid);
    }
}
