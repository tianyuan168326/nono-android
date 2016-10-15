package com.seki.noteasklite.DataUtil.Bean;

/**
 * Created by yuan-tian01 on 2016/4/1.
 */
public class DeleteNoteBean  {
    public long id;
    public String group;
    public  String uuid;

    public DeleteNoteBean(long id, String group, String uuid) {
        this.id = id;
        this.group = group;
        this.uuid = uuid;
    }
}
