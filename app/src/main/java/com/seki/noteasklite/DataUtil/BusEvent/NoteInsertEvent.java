package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.NoteDatabaseArray;

/**
 * Created by yuan-tian01 on 2016/4/2.
 */
public class NoteInsertEvent {
    long noteId;
    String noteUuid;
    NoteDatabaseArray noteDatabaseArray;

    public NoteInsertEvent(NoteDatabaseArray noteDatabaseArray, long noteId, String noteUuid) {
        this.noteDatabaseArray = noteDatabaseArray;
        this.noteId = noteId;
        this.noteUuid = noteUuid;
    }

    public NoteDatabaseArray getNoteDatabaseArray() {
        return noteDatabaseArray;
    }

    public void setNoteDatabaseArray(NoteDatabaseArray noteDatabaseArray) {
        this.noteDatabaseArray = noteDatabaseArray;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getNoteUuid() {
        return noteUuid;
    }

    public void setNoteUuid(String noteUuid) {
        this.noteUuid = noteUuid;
    }
}

