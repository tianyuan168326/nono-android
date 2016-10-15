package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.Search.LocalNoteArray;

import java.util.List;

/**
 * Created by yuan-tian01 on 2016/4/6.
 */
public class SearchNoteEvent {
    List<LocalNoteArray> noteList;
    String keyWord;

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public List<LocalNoteArray> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<LocalNoteArray> noteList) {
        this.noteList = noteList;
    }

    public SearchNoteEvent(String keyWord, List<LocalNoteArray> noteList) {

        this.keyWord = keyWord;
        this.noteList = noteList;
    }
}
