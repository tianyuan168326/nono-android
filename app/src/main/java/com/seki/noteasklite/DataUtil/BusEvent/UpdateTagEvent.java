package com.seki.noteasklite.DataUtil.BusEvent;

import java.util.List;

/**
 * Created by yuan on 2016/5/2.
 */
public class UpdateTagEvent {
    String question_id;
    List<String> tagList;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public UpdateTagEvent(String question_id, List<String> tagList) {

        this.question_id = question_id;
        this.tagList = tagList;
    }
}
