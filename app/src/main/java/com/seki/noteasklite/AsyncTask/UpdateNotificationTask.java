package com.seki.noteasklite.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.seki.noteasklite.Adapter.NotifyNotificationRecycleViewAdapter;
import com.seki.noteasklite.DataUtil.Bean.NotificationDataModel;
import com.seki.noteasklite.MyApp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan on 2015/8/7.
 */
public class UpdateNotificationTask extends AsyncTask<String,Void,String>{
    Context context;
    SwipeRefreshLayout notifyNotificationListRefresher;
    List<NotificationDataModel> notificationDataModelList;
    NotifyNotificationRecycleViewAdapter notifyNotificationRecycleViewAdapter;
    public UpdateNotificationTask(Context context, SwipeRefreshLayout notifyNotificationListRefresher, List<NotificationDataModel> notificationDataModelList,
                                  NotifyNotificationRecycleViewAdapter notifyNotificationRecycleViewAdapter)
    {
        this.context = context;
        this.notifyNotificationListRefresher = notifyNotificationListRefresher;
        this.notificationDataModelList = notificationDataModelList;
        this.notifyNotificationRecycleViewAdapter = notifyNotificationRecycleViewAdapter;
    }
    @Override
    protected String doInBackground(String... data) {
        StringBuilder builder = new StringBuilder();
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://diandianapp.sinaapp.com/quickask_old_notifications.php");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", MyApp.getInstance().userInfo.username));
            params.add(new BasicNameValuePair("access_token", ""));
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                builder.append(s);
            }
            JSONObject jsonObject =  new JSONObject(builder.toString());
            int notificationNb = Integer.valueOf(jsonObject.getString("notification_nb"));
            JSONArray notificationObjectArray = jsonObject.getJSONArray("notification_data");
            synchronized (notificationDataModelList)
            {
                notificationDataModelList.clear();
                for(  int notificationIndex =0 ;notificationIndex<notificationNb;notificationIndex++)
                {
                    NotificationDataModel notificationDataModel = new NotificationDataModel();
                    JSONObject notificationObject = new JSONObject(notificationObjectArray.getString(notificationIndex));
                    notificationDataModel.questionId=notificationObject.getString("question_id");
                    notificationDataModel.questionAbstract = notificationObject.getString("question_abstract");
                    notificationDataModel.otherSideUserRealName = notificationObject.getString("other_side_user_real_name");
                    notificationDataModel.otherSideUserId = notificationObject.getString("other_side_user_id");
                    notificationDataModel.answerAbstract = notificationObject.getString("key_abstract");
                    notificationDataModel.answerId = notificationObject.getString("answer_id");
                    notificationDataModel.dataTime = notificationObject.getString("notification_data");
                    notificationDataModel.notificationHistoryType = Integer.valueOf(notificationObject.getString("notify_history_type"));
                    notificationDataModelList.add(0,notificationDataModel);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        notifyNotificationRecycleViewAdapter.notifyDataSetChanged();
        if(notifyNotificationListRefresher.isRefreshing())
        {
            notifyNotificationListRefresher.setRefreshing(false);
        }
    }
}
