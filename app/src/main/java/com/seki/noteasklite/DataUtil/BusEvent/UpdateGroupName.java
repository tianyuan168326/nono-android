package com.seki.noteasklite.DataUtil.BusEvent;

/**
 * Created by yuan-tian01 on 2016/4/11.
 */
public class UpdateGroupName {
    private String oldG;
    private String newG;

    public String getNewG() {
        return newG;
    }

    public void setNewG(String newG) {
        this.newG = newG;
    }

    public String getOldG() {
        return oldG;
    }

    public void setOldG(String oldG) {
        this.oldG = oldG;
    }

    public UpdateGroupName(String newG, String oldG) {

        this.newG = newG;
        this.oldG = oldG;
    }
}
