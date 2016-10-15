package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.NoteDatabaseArray;

/**
 * Created by yuan on 2016/4/27.
 */
public class InsertRecycleBinEvent {
    NoteDatabaseArray array;
    long id;

    public InsertRecycleBinEvent(NoteDatabaseArray array, long id) {
        this.array = array;
        this.id = id;
    }

    public NoteDatabaseArray getArray() {
        return array;
    }

    public void setArray(NoteDatabaseArray array) {
        this.array = array;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
