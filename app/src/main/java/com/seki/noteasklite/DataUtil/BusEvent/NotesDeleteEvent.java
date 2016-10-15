package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.NoteDatabaseArray;

/**
 * Created by yuan on 2016/6/12.
 */
public class NotesDeleteEvent {
    public NoteDatabaseArray[] noteDatabaseArrays;
    public long[] iDs;
    public NotesDeleteEvent(NoteDatabaseArray[] noteDatabaseArrays, long[] iDs) {
        this.noteDatabaseArrays = noteDatabaseArrays;
        this.iDs = iDs;
    }
}
