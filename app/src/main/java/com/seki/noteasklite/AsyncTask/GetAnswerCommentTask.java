package com.seki.noteasklite.AsyncTask;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.seki.noteasklite.Adapter.InnerQuestionAnswerCommentListAdapter;
import com.seki.noteasklite.DataUtil.InnerQuestionAnswerCommentListViewHolderData;
import com.seki.noteasklite.Util.NetWorkUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yuan on 2015/8/23.
 */
public class GetAnswerCommentTask extends AsyncTask<String,Void,String> {
    private List<InnerQuestionAnswerCommentListViewHolderData> innerQuestionAnswerCommentList;
    private InnerQuestionAnswerCommentListAdapter innerquestionanswercommentlistadapter;
    private SwipeRefreshLayout activityInnerQuestionAnswerCommentRecycleViewRefresher;
    public GetAnswerCommentTask(SwipeRefreshLayout activityInnerQuestionAnswerCommentRecycleViewRefresher, List<InnerQuestionAnswerCommentListViewHolderData> innerQuestionAnswerCommentList, InnerQuestionAnswerCommentListAdapter innerquestionanswercommentlistadapter) {
        this.innerQuestionAnswerCommentList = innerQuestionAnswerCommentList;
        this.innerquestionanswercommentlistadapter = innerquestionanswercommentlistadapter;
        this.activityInnerQuestionAnswerCommentRecycleViewRefresher = activityInnerQuestionAnswerCommentRecycleViewRefresher;
    }

    @Override
    protected String doInBackground(String... params) {
        String innerQuestionAnswerCommentListData  =
                NetWorkUtil.httpHelper("quickask_get_answercomment.php"
                        , new BasicNameValuePair("key_id", params[0]));
        return innerQuestionAnswerCommentListData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String innerQuestionAnswerCommentListData) {
        super.onPostExecute(innerQuestionAnswerCommentListData);
        try
        {
            JSONArray answerCommentArray = new JSONArray(innerQuestionAnswerCommentListData);
            int answerCommentLength = answerCommentArray.length();
            innerQuestionAnswerCommentList.clear();
            for(int answerCommentIndex = 0;answerCommentIndex<answerCommentLength;answerCommentIndex++)
            {
                InnerQuestionAnswerCommentListViewHolderData innerquestionanswercommentlistviewholderdata
                        = new InnerQuestionAnswerCommentListViewHolderData();
                JSONObject answerCommentObjeict= answerCommentArray.getJSONObject(answerCommentIndex);
                innerquestionanswercommentlistviewholderdata.innerQuestionAnswerCommentListItemContentData
                        =answerCommentObjeict.getString("comment_content");
                innerquestionanswercommentlistviewholderdata.innerQuestionAnswerCommentListItemContentUserId
                        =answerCommentObjeict.getString("comment_raiser_id");
                try{
                    innerquestionanswercommentlistviewholderdata.innerQuestionAnswerCommentListItemHeadimgData
                            =answerCommentObjeict.getString("user_headpic");
                    innerquestionanswercommentlistviewholderdata.innerQuestionAnswerCommentListItemNameData
                            =answerCommentObjeict.getString("userRealname");
                }catch (Exception e){}
                innerquestionanswercommentlistviewholderdata.innerQuestionAnswerCommentListItemTimeData
                        =answerCommentObjeict.getString("comment_time");
                innerQuestionAnswerCommentList.add(innerquestionanswercommentlistviewholderdata);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        innerquestionanswercommentlistadapter.notifyDataSetChanged();
        if(activityInnerQuestionAnswerCommentRecycleViewRefresher.isRefreshing() ==true)
        {
            activityInnerQuestionAnswerCommentRecycleViewRefresher.setRefreshing(false);
        }
    }
}
