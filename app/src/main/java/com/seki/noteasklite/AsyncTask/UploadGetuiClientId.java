package com.seki.noteasklite.AsyncTask;

import android.os.AsyncTask;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan on 2015/8/5.
 */
public class UploadGetuiClientId extends AsyncTask<String,Void,String> {
    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... data) {
        StringBuilder builder = new StringBuilder();
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://diandianapp.sinaapp.com/quickask_upload_getuiid.php");
            List<NameValuePair> paramsList = new ArrayList<>();
            paramsList.add(new BasicNameValuePair("user_name", MyApp.getInstance().userInfo.username));
            paramsList.add(new BasicNameValuePair("getui_client_id",data[0]));
            post.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            for(String s = reader.readLine();s!=null;s=reader.readLine())
            {
                builder.append(s);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
