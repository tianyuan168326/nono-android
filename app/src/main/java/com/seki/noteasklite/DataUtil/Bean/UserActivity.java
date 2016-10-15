package com.seki.noteasklite.DataUtil.Bean;

import java.util.List;

/**
 * Created by yuan on 2016/2/8.
 */

public class UserActivity {
    public static interface Type{
        public static final int ADD_ANSWER = 1;
        public static final int ADD_QUESTION = 2;
        public static final int ADD_ANSWER_COMMENT = 3;
        public static final int ADD_NOTICE_PEOPLE = 4;
        public static final int CANCEL_NOTICE_PEOPLE = 5;
        public static final int ADD_NOTICE_TOPIC = 6;
        public static final int CANCEL_NOTICE_TOPIC = 7;
        public static final int VOTE_UP_ANSWER = 8;
        public static final int VOTE_UP_QUESTION = 9;
        public static final int ADD_NOTICE_QUESTION = 10;
        public static final int CANCEL_NOTICE_QUESTION = 11;
    }
    public int state_code;
    public List<ActivityData> data;
    public static class ActivityData{
        public int activity_user_id;
        public int activity_type;
        public int object_user_id;
        public int object_question_id;
        public int object_answer_id;
        public int object_comment_id;
        public String activity_date;
        public String qTitle;
        public String qDetail;
        public String aDetail;
        public int topicNoticedNum;
        public String peopleHeadImageUrl;
        public String peopleRealName;
        public int peopleNoticedNum;
        public int peopleVotedForNum;
    }
}
