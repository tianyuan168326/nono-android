package com.seki.noteasklite.DataUtil.Bean;

import java.util.List;

/**
 * Created by yuan-tian01 on 2016/3/4.
 */
public class AllNoteListBean extends WonderFull{

    /**
     * state_code : 0
     * data : [{"note_id":"1","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"2","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"3","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"4","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"5","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"6","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"7","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"8","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"9","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"10","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"11","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"12","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"13","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"14","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"15","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"16","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"17","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""},{"note_id":"18","note_title":"test","note_content":"testContent","note_group":"test","note_date":"2011","note_time":"0.0","note_user_id":"69","note_uuid":""}]
     */

    /**
     * note_id : 1
     * note_title : test
     * note_content : testContent
     * note_group : test
     * note_date : 2011
     * note_time : 0.0
     * note_user_id : 69
     * note_uuid :
     */

    private List<DataEntity> data;

    public void setState_code(int state_code) {
        this.state_code = state_code;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getState_code() {
        return state_code;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String note_id;
        private String note_title;
        private String note_content;
        private String note_group;
        private String note_date;
        private String note_time;
        private String note_user_id;
        private String note_uuid;

        public void setNote_id(String note_id) {
            this.note_id = note_id;
        }

        public void setNote_title(String note_title) {
            this.note_title = note_title;
        }

        public void setNote_content(String note_content) {
            this.note_content = note_content;
        }

        public void setNote_group(String note_group) {
            this.note_group = note_group;
        }

        public void setNote_date(String note_date) {
            this.note_date = note_date;
        }

        public void setNote_time(String note_time) {
            this.note_time = note_time;
        }

        public void setNote_user_id(String note_user_id) {
            this.note_user_id = note_user_id;
        }

        public void setNote_uuid(String note_uuid) {
            this.note_uuid = note_uuid;
        }

        public String getNote_id() {
            return note_id;
        }

        public String getNote_title() {
            return note_title;
        }

        public String getNote_content() {
            return note_content;
        }

        public String getNote_group() {
            return note_group;
        }

        public String getNote_date() {
            return note_date;
        }

        public String getNote_time() {
            return note_time;
        }

        public String getNote_user_id() {
            return note_user_id;
        }

        public String getNote_uuid() {
            return note_uuid;
        }
    }
}
