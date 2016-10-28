package com.seki.noteasklite.DataUtil.Bean;

import java.util.List;

/**
 * Created by tianyuan on 16/10/26.
 */
public class AllCommentListBean extends WonderFull {
    public List<CommentEntity> comment_list;
    public static class CommentEntity{
        public String comment_id;
        public String comment_content;
        public String comment_vote_up;
        public String comment_vote_down;
        public String comment_raiser_id;
        public String comment_time;
        public String userId;
        public String userRealname;
        public String user_headpic;




        public CommentEntity(String comment_content, String comment_raiser_id, String user_headpic, String userRealname, String comment_time) {
            this.comment_content = comment_content;
            this.comment_raiser_id = comment_raiser_id;
            this.user_headpic = user_headpic;
            this.userRealname = userRealname;
            this.comment_time = comment_time;
        }
    }
}

