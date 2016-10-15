package com.seki.noteasklite.DataUtil.BusEvent;

import android.util.Log;

import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;

/**
 * Created by yuan-tian01 on 2016/4/2.
 */
public class NoteUpdateEvent {
    long oldNoteId;
    long newNoteId;
    String noteContent;
    NoteDatabaseArray noteDatabaseArray;
    String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public NoteUpdateEvent(long newNoteId, String noteContent, NoteDatabaseArray noteDatabaseArray, long oldNoteId, String uuid) {
        Log.d(NONoConfig.TAG_NONo,"begin update note");
        this.newNoteId = newNoteId;
        this.noteContent = noteContent;
        this.noteDatabaseArray = noteDatabaseArray;
        this.oldNoteId = oldNoteId;
        this.uuid = uuid;
    }

    public long getNewNoteId() {
        return newNoteId;
    }

    public void setNewNoteId(long newNoteId) {
        this.newNoteId = newNoteId;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public NoteDatabaseArray getNoteDatabaseArray() {
        return noteDatabaseArray;
    }

    public void setNoteDatabaseArray(NoteDatabaseArray noteDatabaseArray) {
        this.noteDatabaseArray = noteDatabaseArray;
    }

    public long getOldNoteId() {
        return oldNoteId;
    }

    public void setOldNoteId(long oldNoteId) {
        this.oldNoteId = oldNoteId;
    }

}
