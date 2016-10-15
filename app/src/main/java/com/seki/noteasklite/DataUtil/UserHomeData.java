package com.seki.noteasklite.DataUtil;

import java.util.Observable;

/**
 * Created by yuan on 2015/8/9.
 */
public class UserHomeData extends Observable {
    public String activityUserInfoVotedUpNum;
    public String activityUserInfoNoticedNum;
    public String activityUserInfoExperienceShareNum;
    public String activityUserInfoExperienceAnswerNum;
    public String activityUserInfoExpertPointNum;
    public String activityUserInfoNoticingNum;
    public String friendShipType;
    public String activityUserInfoId;
    public String activityUserInfoHeadimg;
    public String activityUserInfoRealName;
    public String activityUserInfoSex;
    public String activityUserInfoAbstract;
    public String activityUserInfoUniversity;
    public String activityUserInfoSchool;

//    public UserHomeData( String activityUserInfoVotedUpNum,
//                         String activityUserInfoNoticedNum
//            , String activityUserInfoExperienceShareNum
//            ,String activityUserInfoExperienceAnswerNum
//            , String activityUserInfoNoticingNum,
//            , String activityUserInfoExpertPointNum
//                        String friendShipType)
//    {
//        this.activityUserInfoVotedUpNum = activityUserInfoVotedUpNum;
//        this.activityUserInfoNoticedNum = activityUserInfoNoticedNum;
//        this.activityUserInfoExperienceShareNum = activityUserInfoExperienceShareNum;
//        this.activityUserInfoExperienceAnswerNum = activityUserInfoExperienceAnswerNum;
//        this.activityUserInfoExpertPointNum = activityUserInfoExpertPointNum;
//        this.activityUserInfoNoticingNum = activityUserInfoNoticingNum;
//        this.friendShipType = friendShipType;
//    }
    public UserHomeData()
    {

    }
    public void upDateUI()
    {
        setChanged();
        notifyObservers();
    }
}
