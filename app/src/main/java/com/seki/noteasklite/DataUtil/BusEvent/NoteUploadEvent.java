package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.NoteAllArray;

/**
 * Created by yuan-tian01 on 2016/4/3.
 */
public class NoteUploadEvent {
    NoteAllArray noteAllArray;
    private boolean uploadState;

    public NoteAllArray getNoteAllArray() {
        return noteAllArray;
    }

    public boolean isUploadState() {
        return uploadState;
    }

    public void setUploadState(boolean uploadState) {
        this.uploadState = uploadState;
    }

    public NoteUploadEvent(NoteAllArray noteAllArray, boolean uploadState) {

        this.noteAllArray = noteAllArray;
        this.uploadState = uploadState;
    }

    public void setNoteAllArray(NoteAllArray noteAllArray) {
        this.noteAllArray = noteAllArray;
    }

}
