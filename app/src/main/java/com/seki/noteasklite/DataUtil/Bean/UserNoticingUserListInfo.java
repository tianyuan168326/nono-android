package com.seki.noteasklite.DataUtil.Bean;

import java.util.List;

/**
 * Created by yuan on 2016/2/8.
 */
public class UserNoticingUserListInfo {
    public int state_code;
    public List<FullUserinfo> data;
    public static class FullUserinfo{
        public int userId;
        public String userName;
        public String userEmail;
        public String userUniversity;
        public String userSubject;
        public String userRealname;
        public String user_headpic;
        public String user_info;
        public String user_favours;
        public int user_voted_up_num;
        public int 	user_expert_point_num;
        public String user_sex;
        public int user_experience_answer_num;
        public int noticed_num;
    }
}
