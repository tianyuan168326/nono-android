package com.seki.noteasklite.DataUtil.BusEvent;

/**
 * Created by yuan on 2016/5/8.
 */
public class EditCommunityItemEvent {
    public String question_id;
    public String title;
    public String content;
    public String tagList;

    public EditCommunityItemEvent(String question_id, String title, String content, String tagList) {
        this.question_id = question_id;
        this.title = title;
        this.content = content;
        this.tagList = tagList;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }
}
