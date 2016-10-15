package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.NoteDatabaseArray;

/**
 * Created by yuan-tian01 on 2016/3/14.
 */
public class NoteDeleteEvent {

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

    public NoteDeleteEvent( NoteDatabaseArray noteDatabaseArray, long noteId) {

        this.noteDatabaseArray = noteDatabaseArray;
        this.noteId = noteId;
    }

    private NoteDatabaseArray noteDatabaseArray;
    private long noteId;
}
