package com.seki.noteasklite.DataUtil.Bean;

import android.content.Context;
import android.content.Intent;

import com.seki.noteasklite.DataUtil.NotificationHistoryType;

import java.lang.ref.WeakReference;

/**
 * Created by yuan on 2015/8/5.
 */
public class NotificationDataModel  {
    public static final String  intentFormMainUpDateNotiyNumTag =  "com.seki.noteasklite.intentFormMainUpDateNotiyNum";
    public static int unRead = 0;
    public int notificationHistoryType;
    public String otherSideUserId;
    public String questionId;
    public String answerId;
    public String otherSideUserRealName;
    public String answerAbstract;
    public String questionAbstract;
    public String dataTime;
    public String commentId;
    public String commentAbstract;
    public boolean hasRead;
    public static WeakReference<Context> applicationContextRef ;
    public static void broadAddNotification(){
        NotificationDataModel.unRead++;
        Intent intentFormMainUpDateNotiyNum = new Intent(intentFormMainUpDateNotiyNumTag);
        applicationContextRef.get().sendBroadcast(intentFormMainUpDateNotiyNum);
    }
    public static void broadMinusNotification(){
        NotificationDataModel.unRead--;
        Intent intentFormMainUpDateNotiyNum = new Intent(intentFormMainUpDateNotiyNumTag);
        applicationContextRef.get().sendBroadcast(intentFormMainUpDateNotiyNum);
    }
    public static void broadClearNotification(){
        NotificationDataModel.unRead = 0;
        Intent intentFormMainUpDateNotiyNum = new Intent(intentFormMainUpDateNotiyNumTag);
        applicationContextRef.get().sendBroadcast(intentFormMainUpDateNotiyNum);
    }
    public NotificationDataModel()
    {
        notificationHistoryType = NotificationHistoryType.NONE;
        otherSideUserId = new String();
        questionId = new String();
        answerId = new String();
        otherSideUserRealName = new String();
        answerAbstract = new String();
        questionAbstract = new String();
        dataTime = new String();
        hasRead = false;
    }
}
