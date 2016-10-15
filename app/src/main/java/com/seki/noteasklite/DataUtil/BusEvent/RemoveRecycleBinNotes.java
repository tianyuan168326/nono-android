package com.seki.noteasklite.DataUtil.BusEvent;

import java.util.List;

/**
 * Created by yuan on 2016/6/13.
 */
public class RemoveRecycleBinNotes {
    public List<Long> recycleNoteIDs;
    public RemoveRecycleBinNotes(List<Long> recycleNoteIDs) {
        this.recycleNoteIDs = recycleNoteIDs;
    }
}
