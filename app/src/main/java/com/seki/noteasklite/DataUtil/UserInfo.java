package com.seki.noteasklite.DataUtil;

/**
 * Created by yuan on 2015/7/31.
 */
public class UserInfo {
    public UserInfo()
    {
        this.userRealName = new String();
        this.userHeadPicURL =  new String();
        this.userSchool =  new String();
        this.userUniversity =  new String();
        this. userToken = new String();
        this.userId = new String("0");
        this. username = new String();
        this.userpassword = new String();
        this.quickAskToken = new String();
        this.logStateEnum = LogStateEnum.OFFLINE;
        this.userAbstract = new String();
        this.userSex = new String();
    }
    public void setSex(String sex){
        this.userSex = sex;
    }
    public UserInfo(String userRealName,
                    String userHeadPicURL,
                    String userSchool,
                    String userUniversity,
                    String userToken,
                    String userId,
                    String username,
                    String userpassword,
                    LogStateEnum logStateEnum,
                    String quickAskToken,
                    String userAbstract)
    {
        this.userRealName = userRealName;
        this.userHeadPicURL = userHeadPicURL;
        this.userSchool = userSchool;
        this.userUniversity = userUniversity;
        this.userToken = userToken;
        this.userId = userId;
        this.username = username;
        this.userpassword = userpassword;
        this.logStateEnum = logStateEnum;
        this.quickAskToken = quickAskToken;
        this.userAbstract = userAbstract;
    }
    public String userRealName;
    public String userHeadPicURL;
    public String userSchool;
    public String userUniversity;
    public String userToken;
    public String userId;
    public  String userSex;
    public String username;
    public String userpassword;
    public  LogStateEnum logStateEnum ;
    public String quickAskToken;
    public String userAbstract;
    public String wonderFull;
    public void reset(){
        this.userRealName = new String();
        this.userHeadPicURL =  new String();
        this.userSchool =  new String();
        this.userUniversity =  new String();
        this. userToken = new String();
        this.userId = new String("0");
        this. username = new String();
        this.userpassword = new String();
        this.quickAskToken = new String();
        this.logStateEnum = LogStateEnum.OFFLINE;
        this.userAbstract = new String();
        this.userSex = new String();
    }
}
