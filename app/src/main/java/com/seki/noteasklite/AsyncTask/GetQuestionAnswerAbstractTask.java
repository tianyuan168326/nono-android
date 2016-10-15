package com.seki.noteasklite.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.NotifyHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yuan on 2015/7/28.
 */
public class GetQuestionAnswerAbstractTask extends AsyncTask<String ,Void,Void> {
    Context context;
    List<Object> innerQuestionDataList;
    RecyclerView.Adapter innerQuestionListAdapter;
//    SwipeRefreshLayout innerQuestionListRefresher;
    public GetQuestionAnswerAbstractTask(Context context,
                                         List<Object> innerQuestionDataList,
                                         RecyclerView.Adapter innerQuestionListAdapter, SwipeRefreshLayout innerQuestionListRefresher) {
        this.context = context;
        this.innerQuestionDataList = innerQuestionDataList;
        this.innerQuestionListAdapter = innerQuestionListAdapter;
//        this. innerQuestionListRefresher= innerQuestionListRefresher;
    }
    @Override
    protected void onPreExecute() {
//        innerQuestionListRefresher.setRefreshing(true);
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        innerQuestionListAdapter.notifyDataSetChanged();
//        if(innerQuestionListRefresher.isRefreshing())
//        {
//            innerQuestionListRefresher.setRefreshing(false);
//        }
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(String... data) {
        try
        {
            HttpClient client = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
                HttpPost post = new HttpPost("http://diandianapp.sinaapp.com/get_key_abstract_list.php");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("question_id", data[0]));
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                builder.append(s);
            }
            String temp = builder.toString();
            JSONObject jsonObject = new JSONObject(temp);

            String stateCodeStr = jsonObject.getString("state_code");
            int stateCode= Integer.parseInt(stateCodeStr);

            String answerNbStr= jsonObject.getString("answer_nb");
            final int answer_nb = Integer.parseInt(answerNbStr);
            switch(stateCode)
            {
                default:
                    NotifyHelper.popNotifyInfo(context, context.getString(R.string.get_question_detail_erro), "");
                    System.exit(-1);
                    break;
                case 0:
                    for(int answerIndex = 0;answerIndex <answer_nb ; answerIndex++)
                    {
                        HashMap<String, String> ItemInfo=new HashMap<String, String>();
                        JSONObject answerJsonObject  = jsonObject.getJSONObject("answer" + String.valueOf(answerIndex));
                        String answerImgCountStr = answerJsonObject.getString("key_image_num");
                        int answerImgCount = Integer.parseInt(answerImgCountStr);
                        switch (answerImgCount)
                        {
                            case 0:
                                ItemInfo.put("answer_img_count", "0");
                                break;
                            case 1:
                                ItemInfo.put("answer_img_count", "1");
                                ItemInfo.put("answer_abstract_img0", answerJsonObject.getString("answer_abstract_img0"));
                                break;
                            default :
                                ItemInfo.put("answer_img_count", "2");
                                ItemInfo.put("answer_abstract_img0", answerJsonObject.getString("answer_abstract_img0"));
                                ItemInfo.put("answer_abstract_img1", answerJsonObject.getString("answer_abstract_img1"));
                                break;
                        }
                        ItemInfo.put("answer_raiser_id", answerJsonObject.getString("answer_raiser_id"));
                        ItemInfo.put("answer_raiser_name", answerJsonObject.getString("answer_raiser_name"));
                        ItemInfo.put("answer_raiser_head_img", answerJsonObject.getString("answer_raiser_head_img"));
                        ItemInfo.put("answer_raiser_school", " "+answerJsonObject.getString("answer_raiser_school"));
                        ItemInfo.put("answer_raiser_college", " "+answerJsonObject.getString("answer_raiser_college"));
                        ItemInfo.put("answer_id", answerJsonObject.getString("answer_id"));

                        ItemInfo.put("answer_abstract_text", answerJsonObject.getString("answer_abstract_text"));
                        ItemInfo.put("answer_hot_degree", answerJsonObject.getString("answer_hot_degree"));
                        ItemInfo.put("answer_comment_num", answerJsonObject.getString("answer_comment_num"));
                        synchronized (innerQuestionDataList){
                            innerQuestionDataList.add(ItemInfo);
                        }
                    }
                    break;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
