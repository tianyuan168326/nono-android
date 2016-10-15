package com.seki.noteasklite.DataUtil.BusEvent;

/**
 * Created by yuan-tian01 on 2016/4/23.
 */
public class ChangeNoteGroupEvent {
    long sdfId;
    String oldGroup;
    String currentGroup;

    public String getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(String currentGroup) {
        this.currentGroup = currentGroup;
    }

    public String getOldGroup() {
        return oldGroup;
    }

    public void setOldGroup(String oldGroup) {
        this.oldGroup = oldGroup;
    }

    public long getSdfId() {
        return sdfId;
    }

    public void setSdfId(long sdfId) {
        this.sdfId = sdfId;
    }

    public ChangeNoteGroupEvent(String currentGroup, String oldGroup, long sdfId) {

        this.currentGroup = currentGroup;
        this.oldGroup = oldGroup;
        this.sdfId = sdfId;
    }
}
