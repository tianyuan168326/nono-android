package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.NoteReelArray;

/**
 * Created by yuan on 2016/6/6.
 */
public class AddReelEvent {
    NoteReelArray array;

    public AddReelEvent(NoteReelArray array) {
        this.array = array;
    }

    public NoteReelArray getArray() {
        return array;
    }

    public void setArray(NoteReelArray array) {
        this.array = array;
    }
}
