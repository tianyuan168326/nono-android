package com.seki.noteasklite.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seki.noteasklite.Adapter.EditInfoRecycleListAdapter;
import com.seki.noteasklite.DataUtil.InfoArray;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.Util.FrescoImageloadHelper;

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
 * Created by yuan on 2015/7/31.
 */
public class EditInfoUserDetailTask extends AsyncTask<String,Void,String>{
    private Context editInfoActivity;
    private SimpleDraweeView editInfoHeadpic;
    private EditText editInfoAbstract;
    public EditInfoUserDetailTask(Context editInfoActivity, SimpleDraweeView editInfoHeadpic, EditText editInfoAbstract) {
        this.editInfoActivity = editInfoActivity;
        this.editInfoHeadpic = editInfoHeadpic;
        this.editInfoAbstract = editInfoAbstract;
    }

    @Override
    protected void onPostExecute(String builder) {
        /**
         * 获得的json格式
         *user_info_headimg
         * user_info_abstract
         * user_info_favours
         */
        try
        {
            JSONObject jsonObject = new JSONObject(builder);
            String stateCodeStr = jsonObject.getString("state_code");
            int stateCode = Integer.valueOf(stateCodeStr);
            if(stateCode != 0)
            {
                return ;
            }
            JSONObject infoObject = jsonObject.getJSONObject("data");
           // FrescoImageloadHelper.removeCacheFromDisk(Uri.parse(infoObject.getString("user_info_headimg")).toString());
            FrescoImageloadHelper.LoadFullImageFromURL(editInfoHeadpic, MyApp.getInstance().userInfo.userHeadPicURL);
            editInfoAbstract.setText(infoObject.getString("user_info_abstract"));
            editInfoAbstract.setSelection(editInfoAbstract.getText().length());
            super.onPostExecute(builder);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param data
     * @return
     */
    @Override
    protected String doInBackground(String... data) {
        String Detail = new String();
        try {
            HttpClient client = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            HttpPost post = new HttpPost("http://diandianapp.sinaapp.com/quickask_get_user_info.php");
            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
            paramsList.add(new BasicNameValuePair("user_name", MyApp.getInstance().userInfo.username));
            post.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                builder.append(s);
                Detail = builder.toString();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return Detail;
    }
}
