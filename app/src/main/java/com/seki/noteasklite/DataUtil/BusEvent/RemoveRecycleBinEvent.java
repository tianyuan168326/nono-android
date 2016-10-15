package com.seki.noteasklite.DataUtil.BusEvent;

/**
 * Created by yuan on 2016/4/27.
 */
public class RemoveRecycleBinEvent {
    long id;

    public RemoveRecycleBinEvent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
