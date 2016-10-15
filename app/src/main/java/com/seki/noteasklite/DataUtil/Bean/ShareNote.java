package com.seki.noteasklite.DataUtil.Bean;

import com.seki.noteasklite.DataUtil.NoteAllArray;

/**
 * Created by yuan-tian01 on 2016/4/6.
 */
public class ShareNote {
    public NoteAllArray getArray() {
        return array;
    }

    public void setArray(NoteAllArray array) {
        this.array = array;
    }

    NoteAllArray array;
    public ShareNote(NoteAllArray array) {
        this.array = array;
    }

}
