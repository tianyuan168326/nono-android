package com.seki.noteasklite.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.seki.noteasklite.DataUtil.QuestionDetailData;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.NetWorkUtil;
import com.seki.noteasklite.Util.NotifyHelper;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.List;

//import io.rong.imlib.model.UserInfo;

/**
 * Created by yuan on 2015/7/28.
 * the asyncTask for fetching question detail and associated answer abstract (text and imgs)
 */
public class GetQuestionDetailTask extends AsyncTask<String ,Void,Void> {
    Context context;
    List<Object> innerQuestionDataList;
    RecyclerView.Adapter innerQuestionListAdapter;
    SwipeRefreshLayout innerQuestionListRefresher;
    public GetQuestionDetailTask(Context context, List<Object> innerQuestionDataList
            , RecyclerView.Adapter innerQuestionListAdapter, SwipeRefreshLayout innerQuestionListRefresher) {
        this.context = context;
        this.innerQuestionDataList = innerQuestionDataList;
        this.innerQuestionListAdapter = innerQuestionListAdapter;
        this.innerQuestionListRefresher = innerQuestionListRefresher;
    }
    @Override
    protected void onPreExecute() {
        innerQuestionListRefresher.setRefreshing(true);
        try {
           // ((InnerQuestionActivity) context).socialBar.setVisibility(View.GONE);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(String... data) {
        try
        {
            String s =  NetWorkUtil.httpHelper("get_question_detail.php", new BasicNameValuePair("question_id", data[0]),
                    new BasicNameValuePair("user_id", MyApp.getInstance().userInfo.userId));
            JSONObject jsonObject = new JSONObject(
                    s
            );
            String stateCodeStr = jsonObject.getString("state_code");
            int stateCode= Integer.parseInt(stateCodeStr);
            switch(stateCode)
            {
                default:
                    NotifyHelper.popNotifyInfo(context, context.getString(R.string.get_question_detail_erro), "");
                    System.exit(-1);
                    break;
                case 0:
                    String question_raiser_id = jsonObject.getString("question_raiser_id");
                    String question_raiser_name = jsonObject.getString("question_raiser_realname");
                    String question_raiser_headimg = jsonObject.getString("question_raiser_headimg");
                    //synchronized (MyApp.getInstance().userInfoCache) {
                    //    if (!MyApp.getInstance().userInfoCache.keySet().contains(question_raiser_id)) {
                    //        MyApp.getInstance().userInfoCache.put(question_raiser_id, new UserInfo(question_raiser_id,
                    //                question_raiser_name, Uri.parse(question_raiser_headimg)));
                    //    }
                    //}
                    QuestionDetailData questionDetailData=new QuestionDetailData();
                    questionDetailData.questionRaiserName =question_raiser_name;
                    questionDetailData.questionRaiserSchool = jsonObject.getString("question_raiser_school");
                    questionDetailData.questionRaiserCollege = jsonObject.getString("question_raiser_college");
                    questionDetailData.questionDetail = jsonObject.getString("question_detail");
                    questionDetailData.questionHotDegree = jsonObject.getString("question_hot_degree");
                    questionDetailData.questionAnswerNum = jsonObject.getString("question_answer_num");
                    questionDetailData.questionRaiserId = question_raiser_id;
                    questionDetailData.questionRaiserTime = jsonObject.getString("question_raise_time");
                    questionDetailData.questionInnerCategory = jsonObject.getString("question_inner_category");
                    questionDetailData.questionOuterCategory = jsonObject.getString("question_outer_category");
                    questionDetailData.questionTitle = jsonObject.getString("question_title");
                    questionDetailData.questionVoteType = jsonObject.getString("question_vote_type");
                    questionDetailData.questionNoticeType = jsonObject.getString("question_noticed_type");
                    questionDetailData.questionNoticedNum = jsonObject.getString("question_notice_num");
                    questionDetailData.questionId =  data[0];
                    synchronized (innerQuestionDataList){
                        innerQuestionDataList.clear();
                        innerQuestionDataList.add(questionDetailData);
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
