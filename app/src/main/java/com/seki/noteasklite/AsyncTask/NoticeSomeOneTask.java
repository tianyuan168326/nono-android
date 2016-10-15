package com.seki.noteasklite.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;


import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.NetWorkUtil;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by yuan on 2015/8/9.
 */
public class NoticeSomeOneTask extends AsyncTask<String,Void,String> {
    Context context;
    TextView activityUserInfoNotice;
    String isToNotice;
    String activityUserInfoNoticeText;
    public NoticeSomeOneTask(Context context, TextView activityUserInfoNotice) {
        this.context = context;
        this.activityUserInfoNotice = activityUserInfoNotice;
    }

    @Override
    protected void onPreExecute() {
        activityUserInfoNoticeText = activityUserInfoNotice.getText().toString();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        isToNotice = activityUserInfoNoticeText.equals(context.getApplicationContext().getString(R.string.notice))?"true":"false";
        NetWorkUtil.httpHelper("Notice/quickask_notice.php", new BasicNameValuePair("me_id", params[0]),
                new BasicNameValuePair("user_id", params[1]), new BasicNameValuePair("is_tonotice", isToNotice));


        isToNotice = isToNotice.equals("true")?"false":"true";
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        switch (isToNotice)
        {
            case "true":
                activityUserInfoNotice.setText(R.string.notice);
                break;
            case "false":
                activityUserInfoNotice.setText(R.string.unnotice);
                break;
        }
    }
}
