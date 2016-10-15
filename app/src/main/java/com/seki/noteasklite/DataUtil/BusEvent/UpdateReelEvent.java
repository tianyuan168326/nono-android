package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.NoteReelArray;

/**
 * Created by yuan on 2016/6/5.
 */
public class UpdateReelEvent {
    public int id;
    public NoteReelArray noteReelArray;
    public UpdateReelEvent(int id, NoteReelArray noteReelArray) {
        this.id =id;
        this.noteReelArray  =noteReelArray;
    }
}
