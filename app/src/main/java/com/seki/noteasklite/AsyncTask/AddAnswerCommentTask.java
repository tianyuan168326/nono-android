package com.seki.noteasklite.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.PopupWindow;

import com.seki.noteasklite.Activity.Ask.InnerQuestionAnswerCommentActivity;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.Util.NetWorkUtil;
import com.seki.noteasklite.Util.NotifyHelper;

import org.apache.http.message.BasicNameValuePair;

import de.greenrobot.event.EventBus;

/**
 * Created by yuan on 2015/8/18.
 */
public class AddAnswerCommentTask extends AsyncTask<String,Void,Void> {
    Context context;
    String keyId;
    PopupWindow popupWindow;
    public AddAnswerCommentTask(Context context, String keyId) {
        this.context = context;
        this.keyId = keyId;
    }
    String comment;
    @Override
    protected Void doInBackground(String... params) {
        comment = params[0];
       String s  =  NetWorkUtil.httpHelper("quickask_answer_add_comment.php"
                , new BasicNameValuePair("access_token", "")
                , new BasicNameValuePair("user_name", MyApp.getInstance().userInfo.username)
                , new BasicNameValuePair("key_id", keyId)
                , new BasicNameValuePair("answer_comment", params[0]));
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        NotifyHelper.popUpWaitingAnimationFinished(popupWindow);
        EventBus.getDefault().post(new InnerQuestionAnswerCommentActivity.RefreshCommentEvent(comment));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        popupWindow = NotifyHelper.popUpWaitingAnimation(context, popupWindow);

    }
}
