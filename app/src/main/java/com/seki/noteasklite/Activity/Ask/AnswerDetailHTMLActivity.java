package com.seki.noteasklite.Activity.Ask;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.seki.noteasklite.Activity.UserInfoActivity;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.Controller.CommunityController;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.CustomControl.Dialog.VoteDialog;
import com.seki.noteasklite.CustomControl.VoteButton;
import com.seki.noteasklite.DataUtil.AnswerDataModel;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.ThirdWrapper.PowerListener;
import com.seki.noteasklite.ThirdWrapper.PowerStringRequest;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.NetWorkUtil;
import com.seki.therichedittext.RichEdit;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AnswerDetailHTMLActivity extends BaseActivity{

    SimpleDraweeView innerQuestionAnswerRaiserHeadimg;
    TextView innerQuestionAnswerRaiserInfo;
    RichEdit innerQuestionItemHtmlWebview;
    LinearLayout innerQuestionAnswerAddComment;
    TextView innerQuestionAnswerCommentNum;
    TextView innerQuestionAnswerRaiserName;
    SwipeRefreshLayout innerQuestionItemHtmlRefresher;
    Toolbar innerQuestionItemHtmlActivityToolbar;
    private  String[] answer_raiser_id = new String[]{""};
    /*the vote type of answer,which would be fetched in AsyncTask;
    * */
    AnswerDataModel answerDataModel = new AnswerDataModel();
    VoteButton vote_button;
    TextView question_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_question_item_html, getString(R.string.answer_detail_inhtml));
        processIntent();
        vote_button = (VoteButton)findViewById(R.id.vote_button);
        getDefinition();
        registerEvent();
        getAnswerDetail();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntent();
    }
    String key_id,question_abstract,user_head,user_name,user_abstract,answer_abstract,vote_num,comment_num,question_id;
    private void processIntent() {
        Intent intent  = getIntent();
        key_id = intent.getStringExtra("key_id");
        question_abstract = intent.getStringExtra("question_abstract");
        user_head = intent.getStringExtra("user_head");
        user_name = intent.getStringExtra("user_name");
        user_abstract = intent.getStringExtra("user_abstract");
        answer_abstract = intent.getStringExtra("answer_abstract");
        vote_num = intent.getStringExtra("vote_num");
        comment_num = intent.getStringExtra("comment_num");
        question_id = intent.getStringExtra("question_id");
    }

    public static void start(Context context,String key_id,String question_abstract,
                             String user_head,String user_name,String user_abstract,
                             String answer_abstract,String vote_num,String comment_num){
        context.startActivity(new Intent()
                .putExtra("key_id", key_id)
                .putExtra("question_abstract", question_abstract)
                .putExtra("user_head", user_head)
                .putExtra("user_name", user_name)
                .putExtra("user_abstract", user_abstract)
                .putExtra("answer_abstract", answer_abstract)
                .putExtra("vote_num", vote_num)
                .putExtra("comment_num", comment_num)
                .setClass(context, AnswerDetailHTMLActivity.class));
    }

    public static void start(Context context,String key_id,String question_abstract,
                             String user_head,String user_name,String user_abstract,
                             String answer_abstract,String vote_num,String comment_num,
                             String question_id){
        context.startActivity(new Intent()
                .putExtra("key_id", key_id)
                .putExtra("question_abstract", question_abstract)
                .putExtra("user_head", user_head)
                .putExtra("user_name", user_name)
                .putExtra("user_abstract", user_abstract)
                .putExtra("answer_abstract", answer_abstract)
                .putExtra("vote_num", vote_num)
                .putExtra("comment_num", comment_num)
                .putExtra("question_id", question_id)
                .setClass(context, AnswerDetailHTMLActivity.class));
    }

    private void registerEvent() {
        innerQuestionItemHtmlRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAnswerDetail();
            }
        });
        innerQuestionAnswerAddComment.setOnClickListener(this);
        vote_button.setOnClickListener(this);
    }
    TextView question_title_view;
    private void getDefinition() {
        innerQuestionItemHtmlRefresher.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        question_title = (TextView)findViewById(R.id.question_title);
        if(user_head !=null)
        FrescoImageloadHelper.simpleLoadImageFromURL(innerQuestionAnswerRaiserHeadimg,user_head);
        if(user_abstract!=null)
        innerQuestionAnswerRaiserInfo.setText(user_abstract);
        if(comment_num!=null)
        innerQuestionAnswerCommentNum.setText(comment_num);
        if(user_name!=null)
        innerQuestionAnswerRaiserName.setText(user_name);
        if(question_abstract!=null)
        question_title.setText(question_abstract);
        question_title_view = (TextView)findViewById(R.id.question_title);
        if(!TextUtils.isEmpty(question_id)){
            question_title_view.setOnClickListener(this);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inner_question_item_html, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void registerWidget() {
        innerQuestionAnswerRaiserHeadimg = findView(R.id.inner_question_answer_raiser_headimg);
        innerQuestionAnswerRaiserInfo = findView(R.id.inner_question_answer_raiser_info);
        innerQuestionItemHtmlWebview = findView(R.id.inner_question_item_html_webview);
        innerQuestionAnswerAddComment = findView(R.id.inner_question_answer_add_comment);
        innerQuestionAnswerCommentNum = findView(R.id.inner_question_answer_comment_num);
        innerQuestionAnswerRaiserName = findView(R.id.inner_question_answer_raiser_name);
        innerQuestionItemHtmlRefresher = findView(R.id.inner_question_item_html_refresher);
        innerQuestionItemHtmlActivityToolbar = findView(R.id.toolbar);
    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.inner_question_answer_add_comment:
                startActivity(new Intent()
                        .setClass(this,InnerQuestionAnswerCommentActivity.class)
                        .putExtra("key_id",key_id));
                break;
            case R.id.question_title:
                InnerQuestionActivity.start(this,question_id,question_abstract);
                finish();
                break;
            case R.id.vote_button:
                if(UserInfoActivity.verifyState(this))
                    return;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        final VoteDialog voteDialog = new VoteDialog(AnswerDetailHTMLActivity.this, vote_button);
                        final AlertDialog dialog = voteDialog.show();
                        voteDialog.setOnVoteListener(new VoteDialog.OnVoteListener() {
                            @Override
                            public void onVoteUp() {
                                // 0=>NORMAL
                                //1=>UP
                                //2=>DOWN
                                int preVoteType = 0;
                                switch (vote_button.getVoteState()) {
                                    case VOTE_NORMAL:
                                        preVoteType = 0;
                                        break;
                                    case VOTE_DOWN:
                                        preVoteType = 2;
                                        break;
                                    case VOTE_UP:
                                        preVoteType = 1;
                                        CommunityController.voteForReply(key_id, String.valueOf(0)
                                                , String.valueOf(preVoteType));
                                        dialog.dismiss();
                                        return;
                                }
                                CommunityController.voteForReply(key_id, String.valueOf(1)
                                        , String.valueOf(preVoteType));
                                dialog.dismiss();
                            }

                            @Override
                            public void onVoteDown() {
                                // 0=>NORMAL
                                //1=>UP
                                //2=>DOWN
                                int preVoteType = 0;
                                switch (vote_button.getVoteState()) {
                                    case VOTE_NORMAL:
                                        preVoteType = 0;
                                        break;
                                    case VOTE_DOWN:
                                        preVoteType = 2;
                                        CommunityController.voteForReply(key_id, String.valueOf(0)
                                                , String.valueOf(preVoteType));
                                        dialog.dismiss();
                                        return;
                                    case VOTE_UP:
                                        preVoteType = 1;
                                        break;
                                }
                                CommunityController.voteForReply(key_id, String.valueOf(2)
                                        , String.valueOf(preVoteType));
                                dialog.dismiss();
                            }
                        });

                    }
                });
                break;
        }
    }

    private void getAnswerDetail(){
        MyApp.getInstance().volleyRequestQueue.add(new PowerStringRequest(
                                                           Request.Method.POST,
                                                           NONoConfig.makeNONoSonURL("/get_answer_and_raiser_detail.php"),
                                                           new PowerListener() {
                                                               @Override
                                                               public void onCorrectResponse(String s) {
                                                                   super.onCorrectResponse(s);
                                                                   putDataToUI(s);
                                                               }

                                                               @Override
                                                               public void onJSONStringParseError() {
                                                                   super.onJSONStringParseError();
                                                               }
                                                           },
                                                           new Response.ErrorListener() {
                                                               @Override
                                                               public void onErrorResponse(VolleyError volleyError) {

                                                               }
                                                           }
                                                   ) {
                                                       @Override
                                                       protected Map<String, String> getParams() throws AuthFailureError {
                                                           HashMap<String, String> params = new HashMap<String, String>();
                                                           params.put("key_id", key_id);
                                                           params.put("user_id", MyApp.userInfo.userId);
                                                           return params;
                                                       }
                                                   }
        );
    }

    private void putDataToUI(String s) {
        try{
            JSONObject jsonObject = new JSONObject(s);
            String stateCodeStr = jsonObject.getString("state_code");
            int stateCode = Integer.valueOf(stateCodeStr);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            final String inner_question_answer_raiser_id = dataObject.getString("inner_question_answer_raiser_id");
            final String inner_question_answer_hot_degree = dataObject.getString("inner_question_answer_hot_degree");
            final String inner_question_answer_vote_type = dataObject.getString("inner_question_answer_vote_type");

            String inner_question_answer_raiser_name = dataObject.getString("inner_question_answer_raiser_name");
            String inner_question_answer_raiser_headimg = dataObject.getString("inner_question_answer_raiser_headimg");
            answerDataModel.answer_raiser_id  = inner_question_answer_raiser_id ;
            answerDataModel.answerHotDegreeInt = Integer.valueOf(inner_question_answer_hot_degree);
            answerDataModel.answerVoteTypeInt = Integer.valueOf(inner_question_answer_vote_type);
            switch (answerDataModel.answerVoteTypeInt){
                case 0:
                    vote_button.setVoteState(VoteButton.VoteState.VOTE_NORMAL);
                    break;
                case 1:
                    vote_button.setVoteState(VoteButton.VoteState.VOTE_UP);
                    break;
                case 2:
                    vote_button.setVoteState(VoteButton.VoteState.VOTE_DOWN);
                    break;
                default:
                    vote_button.setVoteState(VoteButton.VoteState.VOTE_NORMAL);
            }
            vote_button.setVoteNum(answerDataModel.answerHotDegreeInt);


            innerQuestionAnswerRaiserHeadimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetWorkUtil.verifyWhichActivityToSwitch(AnswerDetailHTMLActivity.this,
                            MyApp.getInstance().userInfo.userId, inner_question_answer_raiser_id);
                }
            });
            FrescoImageloadHelper.simpleLoadImageFromURL(innerQuestionAnswerRaiserHeadimg,
                    inner_question_answer_raiser_headimg);
            innerQuestionAnswerRaiserInfo .setText(dataObject.getString("inner_question_answer_raiser_info"));
             innerQuestionAnswerRaiserName .setText(inner_question_answer_raiser_name);
            innerQuestionAnswerCommentNum .setText(dataObject.getString("inner_question_answer_comment_num"));
            innerQuestionItemHtmlWebview.setHtmlText(dataObject.getString("inner_question_item_html_webview"),false);
            innerQuestionItemHtmlWebview.post(new Runnable() {
                @Override
                public void run() {
                    ((ScrollView)$(R.id.scrollView)).fullScroll(ScrollView.FOCUS_UP);
                }
            });
            if(innerQuestionItemHtmlRefresher.isRefreshing())
            {
                innerQuestionItemHtmlRefresher.setRefreshing(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void themePatch() {
        super.themePatch();
        $(R.id.just_bg_primary).setBackgroundColor(ThemeController.getCurrentColor().mainColor);
        $(R.id.question_title).setBackgroundColor(ThemeController.getCurrentColor().mainColor);
    }

}
