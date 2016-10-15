package com.seki.noteasklite.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.seki.noteasklite.Activity.UserInfoActivity;
import com.seki.noteasklite.DataUtil.UserHomeData;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.Util.NetWorkUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/**
 * Created by yuan on 2015/8/9.
 */
public class GetUserHomeInfoTask extends AsyncTask<String,Void,String> {
    Context context;
    UserHomeData userHomeData;
    public GetUserHomeInfoTask(Context context,
                               UserHomeData userHomeData)
    {
        this.context =context;
        this.userHomeData =userHomeData;
    }


    @Override
    protected String doInBackground(String... params) {
        String userInfoHomeString =  NetWorkUtil.httpHelper("quickask_get_userinfo_home.php", new BasicNameValuePair("me_id", params[0]),
                new BasicNameValuePair("user_id", params[1]));
        return userInfoHomeString;
    }
    /**
     * user_info_voted_up_num;
     * user_info_noticed_num;
     * user_info_experience_share_num;
     * user_info_experience_answer_num;
     * user_info_expert_point_num;
     *  user_info_noticing_num;
     *
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            userHomeData.activityUserInfoExperienceAnswerNum = jsonObject.getString("user_info_experience_answer_num");
            userHomeData.activityUserInfoExperienceShareNum = jsonObject.getString("user_info_experience_share_num");
            userHomeData.activityUserInfoExpertPointNum = jsonObject.getString("user_info_expert_point_num");
            userHomeData.activityUserInfoNoticedNum = jsonObject.getString("user_info_noticed_num");
            userHomeData.activityUserInfoNoticingNum = jsonObject.getString("user_info_noticing_num");
            userHomeData.activityUserInfoVotedUpNum = jsonObject.getString("user_info_voted_up_num");
            if(userHomeData.friendShipType==null||userHomeData.friendShipType.length()<=0||userHomeData.friendShipType.compareTo("-1")!=0) {

                ((UserInfoActivity)context).getSupportActionBar().setTitle(MyApp.getInstance().userInfo.userRealName);
                userHomeData.friendShipType = jsonObject.getString("user_info_friend_relation");
                userHomeData.activityUserInfoHeadimg = jsonObject.getString("user_info_headimg");
                userHomeData.activityUserInfoRealName = jsonObject.getString("user_info_real_name");
                userHomeData.activityUserInfoSchool = jsonObject.getString("user_info_school");
                userHomeData.activityUserInfoUniversity=jsonObject.getString("user_info_university");
            }
            userHomeData.activityUserInfoAbstract = jsonObject.getString("user_info_abstract");
            userHomeData.activityUserInfoVotedUpNum = jsonObject.getString("user_info_voted_up_num");
            userHomeData.activityUserInfoExpertPointNum = jsonObject.getString("user_info_expert_point_num");
            userHomeData.activityUserInfoSex = jsonObject.getString("user_info_sex");
        }catch (Exception e){
            Toast.makeText(context, "服务器生病了，请报告管理员", Toast.LENGTH_SHORT).show();
            e.printStackTrace();}
        //bindUserInfoDataToControl(dynamicUserHomeViewGroup,userHomeData);
        upDateUI();
    }

    private void upDateUI() {
        userHomeData.upDateUI();
    }

}
