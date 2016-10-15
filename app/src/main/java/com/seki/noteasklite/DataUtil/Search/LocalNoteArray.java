package com.seki.noteasklite.DataUtil.Search;

import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;

/**
 * Created by yuan-tian01 on 2016/4/7.
 */
public class LocalNoteArray   {
    public int titleStart,titleEnd;
    public int contentStart,contentEnd;
    public int groupStart,groupEnd;
    public NoteAllArray noteAllArray;

    public LocalNoteArray(int contentEnd, int contentStart, NoteAllArray noteAllArray, int titleEnd, int titleStart
            ,int groupStart,int groupEnd) {
        this.contentEnd = contentEnd;
        this.contentStart = contentStart;
        this.noteAllArray = noteAllArray;
        this.titleEnd = titleEnd;
        this.titleStart = titleStart;
        this.groupStart = groupStart;
        this.groupEnd = groupEnd;

    }

    public LocalNoteArray() {
        noteAllArray= null;
        titleStart = titleEnd = contentStart = contentEnd = groupStart = groupEnd = -1;
    }

    public int getContentEnd() {
        return contentEnd;
    }

    public void setContentEnd(int contentEnd) {
        this.contentEnd = contentEnd;
    }

    public int getContentStart() {
        return contentStart;
    }

    public void setContentStart(int contentStart) {
        this.contentStart = contentStart;
    }

    public int getGroupEnd() {
        return groupEnd;
    }

    public void setGroupEnd(int groupEnd) {
        this.groupEnd = groupEnd;
    }

    public int getGroupStart() {
        return groupStart;
    }

    public void setGroupStart(int groupStart) {
        this.groupStart = groupStart;
    }

    public NoteAllArray getNoteAllArray() {
        return noteAllArray;
    }

    public void setNoteAllArray(NoteAllArray noteAllArray) {
        this.noteAllArray = noteAllArray;
    }

    public int getTitleEnd() {
        return titleEnd;
    }

    public void setTitleEnd(int titleEnd) {
        this.titleEnd = titleEnd;
    }

    public int getTitleStart() {
        return titleStart;
    }

    public void setTitleStart(int titleStart) {
        this.titleStart = titleStart;
    }
}
