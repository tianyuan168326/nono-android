package com.seki.noteasklite.DataUtil.Bean;


import com.seki.noteasklite.DataUtil.AppUserInfo;
import com.seki.noteasklite.DataUtil.LogStateEnum;
import com.seki.noteasklite.MyApp;

/**
 * Created by yuan on 2016/5/16.
 */
public class QQLogOnResponse extends WonderFull {

    /**
     * user_head_img : http://7xrcdn.com1.z0.glb.clouddn.com/Fj-45YHOxE0rR70P9pCj_B0galHA-head
     * user_sex : male
     * user_real_name : 风弦
     * user_university : 武汉大学
     * user_school : 电子信息工程
     * user_token : null
     * user_id : 72
     * user_abstract : NONo笔记构建者,欢迎关注我并反馈
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String user_head_img;
        private String user_sex;
        private String user_real_name;
        private String user_university;
        private String user_school;
        private String user_token;
        private String user_id;
        private String user_abstract;

        public String getUser_head_img() {
            return user_head_img;
        }

        public void setUser_head_img(String user_head_img) {
            this.user_head_img = user_head_img;
        }

        public String getUser_sex() {
            return user_sex;
        }

        public void setUser_sex(String user_sex) {
            this.user_sex = user_sex;
        }

        public String getUser_real_name() {
            return user_real_name;
        }

        public void setUser_real_name(String user_real_name) {
            this.user_real_name = user_real_name;
        }

        public String getUser_university() {
            return user_university;
        }

        public void setUser_university(String user_university) {
            this.user_university = user_university;
        }

        public String getUser_school() {
            return user_school;
        }

        public void setUser_school(String user_school) {
            this.user_school = user_school;
        }

        public String getUser_token() {
            return user_token;
        }

        public void setUser_token(String user_token) {
            this.user_token = user_token;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_abstract() {
            return user_abstract;
        }

        public void setUser_abstract(String user_abstract) {
            this.user_abstract = user_abstract;
        }
    }
    public AppUserInfo toUserInfo(){
        AppUserInfo userInfo =  new AppUserInfo();
        userInfo.userHeadPicURL =  getData().getUser_head_img();
        userInfo.fuckHeadImgae();
         userInfo.userRealName =  getData().getUser_real_name();
         userInfo.userSchool = getData().getUser_school();
         userInfo.userUniversity =  getData().getUser_university();
         userInfo.userToken = getData().getUser_token();
         userInfo.userId = getData().getUser_id();
         userInfo.userAbstract = getData().getUser_abstract();
         userInfo.logStateEnum = LogStateEnum.ONLINE;

         userInfo.userSex = getData().getUser_sex();
         userInfo.wonderFull = wonderFull;

        return userInfo;
    }
}
