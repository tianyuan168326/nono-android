package com.seki.noteasklite.AsyncTask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.seki.noteasklite.Activity.EditInfoActivity;
import com.seki.noteasklite.Activity.LogOnActivity;
import com.seki.noteasklite.Adapter.EditInfoRecycleListAdapter;
import com.seki.noteasklite.DataUtil.InfoArray;
import com.seki.noteasklite.DataUtil.SortCategory;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;

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
import java.util.Collections;
import java.util.List;

/**
 * Created by yuan on 2015/7/31.
 */
public class UpdateUserInfoTask extends AsyncTask<String,Void,String>{
    Context mContext;
    PopupWindow waitPopWindow;
    String selectedHeadImgURL;
    String userAbstract;
    String userRealname;
    public UpdateUserInfoTask(Context mContext)
    {
        super();
        this.mContext=mContext;
    }
    @Override
    protected void onPostExecute(String resultString) {
        // TODO Auto-generated method stub
        super.onPostExecute(resultString);
        try {
            JSONObject jsonObject = new JSONObject(resultString);
            String stateCodeStr = jsonObject.getString("state_code");
            int resultCode = Integer.parseInt(stateCodeStr);
            waitPopWindow.dismiss();
            switch (resultCode)
            {
                case 0:
                    Toast.makeText(mContext,mContext.getString(R.string.updata_info_success),Toast.LENGTH_SHORT).show();
                    MyApp.getInstance().userInfo.userHeadPicURL = selectedHeadImgURL;
                    MyApp.getInstance().userInfo.userAbstract = userAbstract;
                    MyApp.getInstance().userInfo.userRealName = userRealname;
                    ((Activity)mContext).finish();
                    break;
                case -2:
                    Toast.makeText(mContext,mContext.getString(R.string.user_info_erro),Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent().setClass(mContext, LogOnActivity.class));
                    break;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        EditInfoActivity mainActivity;
        try
        {
            mainActivity = (EditInfoActivity)mContext;
            mainActivity.setResult(Activity.RESULT_OK, new Intent()
                    .putExtra("headimgURL",selectedHeadImgURL ).putExtra("abstract",userAbstract));
            mainActivity.finish();
        }
        catch (ClassCastException ce)
        {
            Toast.makeText(mContext, R.string.cd_erro, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        LayoutInflater popWindowInflater=LayoutInflater.from(mContext);
        View View=popWindowInflater.inflate(R.layout.wait_pop_window, null);
        waitPopWindow=new PopupWindow(View, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        waitPopWindow.showAsDropDown(View);
    }
    @Override
    protected String doInBackground(String... data) {
        /**
         * user_info_head_img_data
         * user_info_abstract
         * user_info_favours
         */
        StringBuilder builder = new StringBuilder();
        userAbstract = data[0];
        userRealname = data[1];
        selectedHeadImgURL = data[2];
        try
        {
            HttpClient client= new DefaultHttpClient();
            HttpPost post = new HttpPost("http://diandianapp.sinaapp.com/quickask_upload_user_info.php");
            List<NameValuePair> paramsList=new ArrayList<NameValuePair>();
            paramsList.add(new BasicNameValuePair("access_token", MyApp.getInstance().userInfo.quickAskToken));
            paramsList.add(new BasicNameValuePair("user_name", MyApp.getInstance().userInfo.username));
            paramsList.add(new BasicNameValuePair("user_info_abstract", data[0]));
            paramsList.add(new BasicNameValuePair("user_info_name", data[1]));
            paramsList.add(new BasicNameValuePair("user_info_headimg_url", data[2]));
            post.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                builder.append(s);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
