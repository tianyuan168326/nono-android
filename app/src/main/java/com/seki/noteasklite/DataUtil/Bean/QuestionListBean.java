package com.seki.noteasklite.DataUtil.Bean;

import java.util.List;

/**
 * Created by yuan on 2016/2/17.
 */
public class QuestionListBean {
    public int state_code;
    /**
     * item_num : 10
     * state_code : 0
     * min_question_id : 319
     * question_list : [{"question_id":"328","question_title":"么","question_detail":"&#20040;&#20040;","answer_item_xmltree_path":"","question_raiser_id":"2","question_hot_degree":"0","question_raise_time":"2016-02-17 21:42:45","question_outer_category":"","question_inner_category":"","question_tag_list":"[\"么么\",\"喵\",\"未分类话题\",\"性生活\"]","question_vote_down":"0","question_abstract":"&#20040;&#20040;","question_noticed_num":"0","raiser_university":"武汉大学","raiser_subject":"电子信息工程","raiser_realname":"田元","raiser_headimg":"http://diandianapp-impic.stor.sinaapp.com/2headimg","question_answer_num":0},{"question_id":"327","question_title":"测","question_detail":"&#27979;&#35797;","answer_item_xmltree_path":"","question_raiser_id":"2","question_hot_degree":"0","question_raise_time":"2016-02-17 21:40:02","question_outer_category":"","question_inner_category":"","question_tag_list":"[\"未分类话题\",\"么么\"]","question_vote_down":"0","question_abstract":"&#27979;&#35797;","question_noticed_num":"0","raiser_university":"武汉大学","raiser_subject":"电子信息工程","raiser_realname":"田元","raiser_headimg":"http://diandianapp-impic.stor.sinaapp.com/2headimg","question_answer_num":0},{"question_id":"326","question_title":"么","question_detail":"&#20040;&#20040;","answer_item_xmltree_path":"","question_raiser_id":"2","question_hot_degree":"0","question_raise_time":"2016-02-17 21:38:52","question_outer_category":"","question_inner_category":"","question_tag_list":"[\"喵\",\"田元\",\"元元\"]","question_vote_down":"0","question_abstract":"&#20040;&#20040;","question_noticed_num":"0","raiser_university":"武汉大学","raiser_subject":"电子信息工程","raiser_realname":"田元","raiser_headimg":"http://diandianapp-impic.stor.sinaapp.com/2headimg","question_answer_num":0},{"question_id":"325","question_title":"么","question_detail":"&#20040;&#20040;","answer_item_xmltree_path":"","question_raiser_id":"2","question_hot_degree":"0","question_raise_time":"2016-02-17 21:38:47","question_outer_category":"","question_inner_category":"","question_tag_list":"[\"喵\",\"未分类话题\"]","question_vote_down":"0","question_abstract":"&#20040;&#20040;","question_noticed_num":"0","raiser_university":"武汉大学","raiser_subject":"电子信息工程","raiser_realname":"田元","raiser_headimg":"http://diandianapp-impic.stor.sinaapp.com/2headimg","question_answer_num":0},{"question_id":"324","question_title":"么","question_detail":"&#20040;&#20040;","answer_item_xmltree_path":"","question_raiser_id":"2","question_hot_degree":"0","question_raise_time":"2016-02-17 21:26:46","question_outer_category":"","question_inner_category":"","question_tag_list":"[\"田元\",\"喵\"]","question_vote_down":"0","question_abstract":"&#20040;&#20040;","question_noticed_num":"0","raiser_university":"武汉大学","raiser_subject":"电子信息工程","raiser_realname":"田元","raiser_headimg":"http://diandianapp-impic.stor.sinaapp.com/2headimg","question_answer_num":0},{"question_id":"323","question_title":"测试用的","question_detail":"测试用的","answer_item_xmltree_path":"","question_raiser_id":"2","question_hot_degree":"0","question_raise_time":"2016-02-17 21:24:00","question_outer_category":"","question_inner_category":"","question_tag_list":"[\"1\",\"2\",\"3\"]","question_vote_down":"0","question_abstract":"测试用的","question_noticed_num":"0","raiser_university":"武汉大学","raiser_subject":"电子信息工程","raiser_realname":"田元","raiser_headimg":"http://diandianapp-impic.stor.sinaapp.com/2headimg","question_answer_num":0},{"question_id":"322","question_title":"么么","question_detail":"&#20040;&#20040;&#21714;","answer_item_xmltree_path":"","question_raiser_id":"2","question_hot_degree":"0","question_raise_time":"2016-02-17 21:23:11","question_outer_category":"","question_inner_category":"","question_tag_list":"","question_vote_down":"0","question_abstract":"&#20040;&#20040;&#21714;","question_noticed_num":"0","raiser_university":"武汉大学","raiser_subject":"电子信息工程","raiser_realname":"田元","raiser_headimg":"http://diandianapp-impic.stor.sinaapp.com/2headimg","question_answer_num":0},{"question_id":"321","question_title":"测试用的","question_detail":"测试用的","answer_item_xmltree_path":"","question_raiser_id":"2","question_hot_degree":"0","question_raise_time":"2016-02-17 21:20:04","question_outer_category":"","question_inner_category":"","question_tag_list":"[\"1\",\"2\",\"3\"]","question_vote_down":"0","question_abstract":"测试用的","question_noticed_num":"0","raiser_university":"武汉大学","raiser_subject":"电子信息工程","raiser_realname":"田元","raiser_headimg":"http://diandianapp-impic.stor.sinaapp.com/2headimg","question_answer_num":0},{"question_id":"320","question_title":"测试用的","question_detail":"测试用的","answer_item_xmltree_path":"","question_raiser_id":"2","question_hot_degree":"0","question_raise_time":"2016-02-17 21:18:06","question_outer_category":"","question_inner_category":"","question_tag_list":"[\"1\",\"2\",\"3\"]","question_vote_down":"0","question_abstract":"测试用的","question_noticed_num":"0","raiser_university":"武汉大学","raiser_subject":"电子信息工程","raiser_realname":"田元","raiser_headimg":"http://diandianapp-impic.stor.sinaapp.com/2headimg","question_answer_num":0},{"question_id":"319","question_title":"测试用的","question_detail":"测试用的","answer_item_xmltree_path":"","question_raiser_id":"2","question_hot_degree":"0","question_raise_time":"2016-02-17 21:15:48","question_outer_category":"","question_inner_category":"","question_tag_list":"[\"1\",\"2\",\"3\"]","question_vote_down":"0","question_abstract":"测试用的","question_noticed_num":"0","raiser_university":"武汉大学","raiser_subject":"电子信息工程","raiser_realname":"田元","raiser_headimg":"http://diandianapp-impic.stor.sinaapp.com/2headimg","question_answer_num":0}]
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class Data{

   }
    public static class Question{
        public int question_id;
        public String question_title;
        public String question_detail;

    }

    public static class DataEntity {
        private String item_num;
        private String state_code;
        private String min_question_id;
        /**
         * question_id : 328
         * question_title : 么
         * question_detail : &#20040;&#20040;
         * answer_item_xmltree_path :
         * question_raiser_id : 2
         * question_hot_degree : 0
         * question_raise_time : 2016-02-17 21:42:45
         * question_outer_category :
         * question_inner_category :
         * question_tag_list : ["么么","喵","未分类话题","性生活"]
         * question_vote_down : 0
         * question_abstract : &#20040;&#20040;
         * question_noticed_num : 0
         * raiser_university : 武汉大学
         * raiser_subject : 电子信息工程
         * raiser_realname : 田元
         * raiser_headimg : http://diandianapp-impic.stor.sinaapp.com/2headimg
         * question_answer_num : 0
         */

        private List<QuestionListEntity> question_list;

        public void setItem_num(String item_num) {
            this.item_num = item_num;
        }

        public void setState_code(String state_code) {
            this.state_code = state_code;
        }

        public void setMin_question_id(String min_question_id) {
            this.min_question_id = min_question_id;
        }

        public void setQuestion_list(List<QuestionListEntity> question_list) {
            this.question_list = question_list;
        }

        public String getItem_num() {
            return item_num;
        }

        public String getState_code() {
            return state_code;
        }

        public String getMin_question_id() {
            return min_question_id;
        }

        public List<QuestionListEntity> getQuestion_list() {
            return question_list;
        }

        public static class QuestionListEntity {
            private String question_id;
            private String question_title;
            private String question_detail;
            private String answer_item_xmltree_path;
            private String question_raiser_id;
            private String question_hot_degree;
            private String question_raise_time;
            private String question_outer_category;
            private String question_inner_category;
            private String question_tag_list;
            private String question_vote_down;
            private String question_abstract;
            private String question_noticed_num;
            private String raiser_university;
            private String raiser_subject;
            private String raiser_realname;
            private String raiser_headimg;
            private int question_answer_num;

            public void setQuestion_id(String question_id) {
                this.question_id = question_id;
            }

            public void setQuestion_title(String question_title) {
                this.question_title = question_title;
            }

            public void setQuestion_detail(String question_detail) {
                this.question_detail = question_detail;
            }

            public void setAnswer_item_xmltree_path(String answer_item_xmltree_path) {
                this.answer_item_xmltree_path = answer_item_xmltree_path;
            }

            public void setQuestion_raiser_id(String question_raiser_id) {
                this.question_raiser_id = question_raiser_id;
            }

            public void setQuestion_hot_degree(String question_hot_degree) {
                this.question_hot_degree = question_hot_degree;
            }

            public void setQuestion_raise_time(String question_raise_time) {
                this.question_raise_time = question_raise_time;
            }

            public void setQuestion_outer_category(String question_outer_category) {
                this.question_outer_category = question_outer_category;
            }

            public void setQuestion_inner_category(String question_inner_category) {
                this.question_inner_category = question_inner_category;
            }

            public void setQuestion_tag_list(String question_tag_list) {
                this.question_tag_list = question_tag_list;
            }

            public void setQuestion_vote_down(String question_vote_down) {
                this.question_vote_down = question_vote_down;
            }

            public void setQuestion_abstract(String question_abstract) {
                this.question_abstract = question_abstract;
            }

            public void setQuestion_noticed_num(String question_noticed_num) {
                this.question_noticed_num = question_noticed_num;
            }

            public void setRaiser_university(String raiser_university) {
                this.raiser_university = raiser_university;
            }

            public void setRaiser_subject(String raiser_subject) {
                this.raiser_subject = raiser_subject;
            }

            public void setRaiser_realname(String raiser_realname) {
                this.raiser_realname = raiser_realname;
            }

            public void setRaiser_headimg(String raiser_headimg) {
                this.raiser_headimg = raiser_headimg;
            }

            public void setQuestion_answer_num(int question_answer_num) {
                this.question_answer_num = question_answer_num;
            }

            public String getQuestion_id() {
                return question_id;
            }

            public String getQuestion_title() {
                return question_title;
            }

            public String getQuestion_detail() {
                return question_detail;
            }

            public String getAnswer_item_xmltree_path() {
                return answer_item_xmltree_path;
            }

            public String getQuestion_raiser_id() {
                return question_raiser_id;
            }

            public String getQuestion_hot_degree() {
                return question_hot_degree;
            }

            public String getQuestion_raise_time() {
                return question_raise_time;
            }

            public String getQuestion_outer_category() {
                return question_outer_category;
            }

            public String getQuestion_inner_category() {
                return question_inner_category;
            }

            public String getQuestion_tag_list() {
                return question_tag_list;
            }

            public String getQuestion_vote_down() {
                return question_vote_down;
            }

            public String getQuestion_abstract() {
                return question_abstract;
            }

            public String getQuestion_noticed_num() {
                return question_noticed_num;
            }

            public String getRaiser_university() {
                return raiser_university;
            }

            public String getRaiser_subject() {
                return raiser_subject;
            }

            public String getRaiser_realname() {
                return raiser_realname;
            }

            public String getRaiser_headimg() {
                return raiser_headimg;
            }

            public int getQuestion_answer_num() {
                return question_answer_num;
            }
        }
    }
}
