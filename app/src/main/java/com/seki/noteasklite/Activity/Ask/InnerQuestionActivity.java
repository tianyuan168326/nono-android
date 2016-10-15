package com.seki.noteasklite.Activity.Ask;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.seki.noteasklite.Activity.UserInfoActivity;
import com.seki.noteasklite.Adapter.InnerQuestionAdapter;
import com.seki.noteasklite.Base.BaseAcitivityWithRecycleView;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.CustomControl.Dialog.VoteDialog;
import com.seki.noteasklite.CustomControl.NoticeButton;
import com.seki.noteasklite.CustomControl.VoteButton;
import com.seki.noteasklite.DataUtil.QuestionDetailData;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.ThirdWrapper.PowerListener;
import com.seki.noteasklite.ThirdWrapper.PowerStringRequest;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.NotifyHelper;
import com.seki.noteasklite.Util.TimeLogic;
import com.seki.therichedittext.RichEdit;

import org.json.JSONObject;
import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ActivityOptionsICS.transition.TransitionCompat;

public class InnerQuestionActivity extends BaseAcitivityWithRecycleView {
    private String questionId;
    private String questionTitle;
    private List<Object> innerQuestionDataList;
    private NoticeButton notice_btn;
    private VoteButton voteBtn;
    private FloatingActionButton answerBtn;
    public CoordinatorLayout socialBar;
    private RichEdit innerQuestionDetailRender;
    private SimpleDraweeView inner_question_raiser_head_image;
    private TextView question_inner_user_name;
    private TextView inner_question_list_item_is_sameuniversity;
    private TextView inner_question_list_item_is_sameschool;
    private  TextView question_inner_raise_time;
    //private CircleButton pull_card_fab;
    private LinearLayout question_detail_card_view;
    private CollapsingToolbarLayout collapseToolBar;
    TextView question_answer_num;
    QuestionDetailData questionDetailData=new QuestionDetailData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openEventBus();
        questionId=getIntent().getStringExtra("questionId");
        questionTitle=getIntent().getStringExtra("question_title");
        setContentView(R.layout.activity_inner_question, questionTitle);
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP) {
            TransitionCompat.startTransition(this, R.layout.activity_inner_question);
        }
    }
    public static void start(Context context,String questionId,String questionTitle){
            Intent intent = new Intent();
            intent.setClass(context,InnerQuestionActivity.class);
            intent.putExtra("questionId", questionId);
            intent.putExtra("question_title",questionTitle);
            context.startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            TransitionCompat.finishAfterTransition(this);
        }
    }

    private void changeQuestionNoticeType(final String oldQuestionNoticeType, final QuestionDetailData questionDetailData) {
        StringRequest changeQuestionNoticeTypeRequest = new PowerStringRequest(Request.Method.POST,
                NONoConfig.makeNONoSonURL("/Notice/quickask_change_question_noticetype.php")
                , new PowerListener() {
            @Override
            public void onCorrectResponse(String s) {
                super.onCorrectResponse(s);
                try
                {
                    JSONObject jsonObject = new JSONObject(s);
                    String questionNoticedNum = jsonObject.getString("question_noticed_num");
                    String questionNoticedType = jsonObject.getString("question_noticed_type");
                    questionDetailData.questionNoticedNum = questionNoticedNum;
                    questionDetailData.questionNoticeType = questionNoticedType;
                    notice_btn.setNoticeType(questionDetailData.questionNoticeType);
                    notice_btn.setNotice_num(Integer.valueOf(questionDetailData.questionNoticedNum));
                    Snackbar.make(socialBar,"有"+questionDetailData.questionNoticedNum+"人关注",Snackbar.LENGTH_LONG).show();
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void onJSONStringParseError() {
                super.onJSONStringParseError();
            }

            @Override
            public void onResponse(String s) {
                super.onResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("question_id",questionDetailData.questionId);
                params.put("user_name",MyApp.getInstance().userInfo.username);
                params.put("user_token",MyApp.getInstance().userInfo.userToken);
                params.put("old_question_notice_type",oldQuestionNoticeType);
                params.putAll(super.getParams());
                return params;
            }
        };
        MyApp.getInstance().volleyRequestQueue.add(changeQuestionNoticeTypeRequest);
    }

    private void voteForQuestion(final String question_id, final String vote_type, final String pre_vote_type) {

        StringRequest voteForQuestionRequest = new PowerStringRequest(Request.Method.POST,
                NONoConfig.makeNONoSonURL("/quickask_vote_for_question.php")
                , new PowerListener(){
            @Override
            public void onCorrectResponse(String s) {
                super.onCorrectResponse(s);
            }

            @Override
            public void onJSONStringParseError() {
                super.onJSONStringParseError();
            }

            @Override
            public void onResponse(String s) {
                super.onResponse(s);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("access_token", "");
                params.put("user_name", MyApp.getInstance().userInfo.username);
                params.put("question_id", question_id);
                params.put("vote_type",vote_type);
                params.put("pre_vote_type",pre_vote_type);
                params.putAll(super.getParams());
                return params;
            }
        };
        MyApp.getInstance().volleyRequestQueue.add(voteForQuestionRequest);
    }

    @Override
    protected void registerWidget() {
        question_answer_num = findView(R.id.question_answer_num);
        socialBar = findView(R.id.socialBar);
        innerQuestionDetailRender =$(R.id.inner_question_detail);
        inner_question_raiser_head_image = findView(R.id.inner_question_raiser_head_image);
        inner_question_raiser_head_image.setOnClickListener(this);
        question_inner_user_name = findView(R.id.question_inner_user_name);
        inner_question_list_item_is_sameuniversity = findView(R.id.inner_question_list_item_is_sameuniversity);
        inner_question_list_item_is_sameschool = findView(R.id.inner_question_list_item_is_sameschool);
        notice_btn = findView(R.id.notice_btn);
        notice_btn.setOnClickListener(this);
        question_inner_raise_time = findView(R.id.question_inner_raise_time);
        collapseToolBar =(CollapsingToolbarLayout) findViewById(R.id.collapseToolBar);
        collapseToolBar.setCollapsedTitleTextColor(getResources().getColor(R.color.md_text));
        collapseToolBar.setExpandedTitleTextAppearance(R.style.questionDetailCollapseStyle);

       // pull_card_fab = findView(R.id.pull_card_fab);
        question_detail_card_view = findView(R.id.question_detail_card_view);
        voteBtn=(VoteButton)findViewById(R.id.inner_question_detail_vote_tooltip_trigger);
        voteBtn.setOnClickListener(this);
        answerBtn = findView(R.id.add_answer);
        answerBtn.setOnClickListener(this);
    }
    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer,String> setUpOptionMenu() {
        HashMap<Integer,String> idMethodNamePair = new HashMap<Integer,String>();
        idMethodNamePair.put(android.R.id.home,"onBackPressed");
        return idMethodNamePair;
    }

    @Override
    protected RecyclerView.Adapter setRecyclerViewAdapter() {
        innerQuestionDataList=new ArrayList<>();

        return new InnerQuestionAdapter(this,innerQuestionDataList);
    }
    @Override
    protected void afterSetUpRecycleView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
       getQuestionDetailFromNet();
    }
    @Override
    public void onClick(View v) {
        if(UserInfoActivity.verifyState(this))
            return ;
        switch(v.getId()){
            case R.id.notice_btn:
                changeQuestionNoticeType(questionDetailData.questionNoticeType, questionDetailData);
                break;
            case R.id.inner_question_detail_vote_tooltip_trigger:
                final VoteDialog voteDialog = new VoteDialog(this, voteBtn);
                final AlertDialog dialog = voteDialog.show();
                voteDialog.setOnVoteListener(new VoteDialog.OnVoteListener() {
                    @Override
                    public void onVoteUp() {
                        // 0=>NORMAL
                        //1=>UP
                        //2=>DOWN
                        int preVoteType = 0;
                        switch (voteBtn.getVoteState()) {
                            case VOTE_NORMAL:preVoteType = 0;break;
                            case VOTE_DOWN:preVoteType = 2;break;
                            case VOTE_UP:preVoteType = 1;voteForQuestion(questionId, "0", "1");
                                dialog.dismiss();
                                return;
                        }
                        voteForQuestion(questionId, "1", String.valueOf(preVoteType));
                        dialog.dismiss();
                    }

                    @Override
                    public void onVoteDown() {
                        // 0=>NORMAL
                        //1=>UP
                        //2=>DOWN
                        int preVoteType = 0;
                        switch (voteBtn.getVoteState()) {case VOTE_NORMAL:preVoteType = 0;break;
                            case VOTE_DOWN:preVoteType = 2;voteForQuestion(questionId, "0", String.valueOf(preVoteType));
                                dialog.dismiss();
                                return;
                            case VOTE_UP:
                                preVoteType = 1;
                                break;
                        }
                        voteForQuestion(questionId, "2", String.valueOf(preVoteType));
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.add_answer:
                startActivity(
                        new Intent()
                                .setClass(InnerQuestionActivity.this, AnswerInputActivity.class)
                                .putExtra("question_id", questionId)
                );
                break;
            case R.id.inner_question_raiser_head_image:
                UserInfoActivity.start(this,questionDetailData.questionRaiserId);
                break;
        }
    }

    private void getQuestionDetailFromNet() {
        StringRequest  getQuestionDetailRequest = new StringRequest(Request.Method.POST,
                "http://diandianapp.sinaapp.com/get_question_detail.php", new PowerListener() {
            @Override
            public void onCorrectResponse(String s) {
                super.onCorrectResponse(s);
                try{
                    JSONObject jsonObject = new JSONObject(s );
                    String stateCodeStr = jsonObject.getString("state_code");
                    int stateCode= Integer.parseInt(stateCodeStr);
                    switch(stateCode)
                    {
                        default:
                            NotifyHelper.popNotifyInfo(InnerQuestionActivity.this, InnerQuestionActivity.this.getString(R.string.get_question_detail_erro), "");
                            break;
                        case 0:
                            questionDetailData.questionRaiserHeadImage = jsonObject.getString("question_raiser_headimg");
                            questionDetailData.questionRaiserName =jsonObject.getString("question_raiser_realname");
                            questionDetailData.questionRaiserSchool = jsonObject.getString("question_raiser_school");
                            questionDetailData.questionRaiserCollege = jsonObject.getString("question_raiser_college");
                            questionDetailData.questionDetail = jsonObject.getString("question_detail");
                            questionDetailData.questionHotDegree = jsonObject.getString("question_hot_degree");
                            questionDetailData.questionAnswerNum = jsonObject.getString("question_answer_num");
                            questionDetailData.questionRaiserId = jsonObject.getString("question_raiser_id");
                            questionDetailData.questionRaiserTime = jsonObject.getString("question_raise_time");
                            questionDetailData.questionInnerCategory = jsonObject.getString("question_inner_category");
                            questionDetailData.questionOuterCategory = jsonObject.getString("question_outer_category");
                            questionDetailData.questionTitle = jsonObject.getString("question_title");
                            questionDetailData.questionVoteType = jsonObject.getString("question_vote_type");
                            questionDetailData.questionNoticeType = jsonObject.getString("question_noticed_type");
                            questionDetailData.questionNoticedNum = jsonObject.getString("question_notice_num");
                            questionDetailData.questionId =  questionId;
                            question_answer_num.setText(questionDetailData.questionAnswerNum);
                            FrescoImageloadHelper.simpleLoadImageFromURL(inner_question_raiser_head_image, questionDetailData.questionRaiserHeadImage);
                            question_inner_user_name.setText(questionDetailData.questionRaiserName);
                            if(questionDetailData.questionRaiserSchool.trim().equals(MyApp.getInstance().userInfo.userUniversity.trim())){
                                inner_question_list_item_is_sameuniversity.setVisibility(View.VISIBLE);
                                if(questionDetailData.questionRaiserCollege.trim().equals(MyApp.getInstance().userInfo.userSchool.trim())){
                                    inner_question_list_item_is_sameschool.setVisibility(View.INVISIBLE);
                                }
                            }
                            toolBar.setTitle(questionDetailData.questionTitle);
                            innerQuestionDetailRender.setHtmlText(questionDetailData.questionDetail,false);
                            innerQuestionDetailRender.post(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });

                            $(R.id.scrollView).post(new Runnable() {
                                @Override
                                public void run() {
                                    NestedScrollView view = (NestedScrollView)findViewById(R.id.scrollView);
                                    view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                                    view.setFocusable(true);
                                    view.setFocusableInTouchMode(true);
                                    view.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            v.requestFocusFromTouch();
                                            return false;
                                        }
                                    });
                                    innerQuestionDetailRender.setHtmlText(questionDetailData.questionDetail,false);
                                    ((NestedScrollView)$(R.id.scrollView)).fullScroll(NestedScrollView.FOCUS_UP);
                                    collapseToolBar.setScrimsShown(true,true);

                                }
                            });

                            //innerQuestionDetailRender.setHtmlText("都不会说的是肯定不",false);

                            notice_btn.setNoticeType(questionDetailData.questionNoticeType);
                            notice_btn.setNotice_num(Integer.valueOf(questionDetailData.questionNoticedNum));
                            switch(questionDetailData.questionVoteType){
                                case "0":
                                    voteBtn.setVoteState(VoteButton.VoteState.VOTE_NORMAL);
                                    break;
                                case "1":
                                    voteBtn.setVoteState(VoteButton.VoteState.VOTE_UP);
                                    break;
                                case "2":
                                    voteBtn.setVoteState(VoteButton.VoteState.VOTE_DOWN);
                                    break;
                            }
                            voteBtn.setVoteNum(Integer.valueOf(questionDetailData.questionHotDegree));
                            question_inner_raise_time.setText(TimeLogic.timeLogic(questionDetailData.questionRaiserTime));
                            break;
                    }
                }
                catch(Exception e){
                    e.printStackTrace();}
                getAnswerListFromNet();
            }

            @Override
            public void onJSONStringParseError() {
                super.onJSONStringParseError();
            }

            @Override
            public void onResponse(String s) {
                super.onResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {}
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("question_id",questionId);
                params.put("user_id",MyApp.getInstance().userInfo.userId);
                return params;
            }
        };
        MyApp.getInstance().volleyRequestQueue.add(getQuestionDetailRequest);
    }
    public static class AnswerListUpdateEvent{

    }
    public void onEventMainThread(AnswerListUpdateEvent event){
        refrshAnswerListFromNet();
    }

    private void refrshAnswerListFromNet() {
        StringRequest  GetQuestionAnswerAbstractRequest = new StringRequest(Request.Method.POST,
                NONoConfig.makeNONoSonURL("/get_key_abstract_list.php"), new PowerListener() {
            @Override
            public void onCorrectResponse(String s) {
                super.onCorrectResponse(s);
                try{
                    JSONObject jsonObject = new JSONObject(s);

                    String stateCodeStr = jsonObject.getString("state_code");
                    int stateCode= Integer.parseInt(stateCodeStr);

                    String answerNbStr= jsonObject.getString("answer_nb");
                    final int answer_nb = Integer.parseInt(answerNbStr);
                    switch(stateCode)
                    {
                        default:
                            System.exit(-1);
                            break;
                        case 0:
                            innerQuestionDataList.clear();
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
                                ItemInfo.put("question_abstract", questionDetailData.questionTitle);

                                synchronized (innerQuestionDataList){
                                    innerQuestionDataList.add(ItemInfo);
                                }
                            }
                    }
                }catch(Exception e){
                    e.printStackTrace();}
                recycleViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onJSONStringParseError() {
                super.onJSONStringParseError();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {}
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("question_id", questionId);
                return params;
            }
        };
        MyApp.getInstance().volleyRequestQueue.add(GetQuestionAnswerAbstractRequest);
    }

    public void getAnswerListFromNet() {
        StringRequest  GetQuestionAnswerAbstractRequest = new StringRequest(Request.Method.POST,
                NONoConfig.makeNONoSonURL("/get_key_abstract_list.php"), new PowerListener() {
            @Override
            public void onCorrectResponse(String s) {
                super.onCorrectResponse(s);
                try{
                    JSONObject jsonObject = new JSONObject(s);

                    String stateCodeStr = jsonObject.getString("state_code");
                    int stateCode= Integer.parseInt(stateCodeStr);

                    String answerNbStr= jsonObject.getString("answer_nb");
                    final int answer_nb = Integer.parseInt(answerNbStr);
                    switch(stateCode)
                    {
                        default:
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
                                ItemInfo.put("question_abstract", questionDetailData.questionTitle);
                                synchronized (innerQuestionDataList){
                                    innerQuestionDataList.add(ItemInfo);
                                }
                            }
                    }
                }catch(Exception e){
                    e.printStackTrace();}
                recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        recyclerView.removeOnLayoutChangeListener(this);
                        if (recyclerView.getLayoutManager().getChildCount() > 1) {
                            recycleViewAdapter.notifyDataSetChanged();
                            if (!(innerQuestionDataList.size() > 2)) {

                            }
                        }
                    }
                });
                Log.d("ui", "card height after list on" + String.valueOf(question_detail_card_view.getHeight()));

                recycleViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onJSONStringParseError() {
                super.onJSONStringParseError();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {}
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("question_id", questionId);
                return params;
            }
        };
        MyApp.getInstance().volleyRequestQueue.add(GetQuestionAnswerAbstractRequest);
    }

    @Override
    protected void themePatch() {
        super.themePatch();
        $(R.id.just_bg_primary).setBackgroundColor(ThemeController.getCurrentColor().mainColor);
        $(R.id.collapseToolBar).setBackgroundColor(ThemeController.getCurrentColor().mainColor);
        ((CollapsingToolbarLayout)$(R.id.collapseToolBar)).setContentScrimColor((ThemeController.getCurrentColor().mainColor));
    }
}
