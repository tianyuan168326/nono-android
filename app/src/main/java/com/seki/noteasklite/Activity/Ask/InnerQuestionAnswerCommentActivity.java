package com.seki.noteasklite.Activity.Ask;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.seki.noteasklite.Activity.UserInfoActivity;
import com.seki.noteasklite.Adapter.InnerQuestionAnswerCommentListAdapter;
import com.seki.noteasklite.AsyncTask.AddAnswerCommentTask;
import com.seki.noteasklite.AsyncTask.GetAnswerCommentTask;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.CustomControl.TintImageView;
import com.seki.noteasklite.DataUtil.InnerQuestionAnswerCommentListViewHolderData;
import com.seki.noteasklite.DividerItemDecoration;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.InputTools;
import com.seki.noteasklite.Util.NotifyHelper;
import com.seki.noteasklite.Util.TimeLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InnerQuestionAnswerCommentActivity extends BaseActivity  {

    RecyclerView activityInnerQuestionAnswerCommentRecycleView;
    SwipeRefreshLayout activityInnerQuestionAnswerCommentRecycleViewRefresher;
    EditText activityInnerQuestionAnswerCommentContent;
    Toolbar activityInnerQuestionAnswerCommentToolbar;
    TintImageView activityInnerQuestionAnswerCommentContentSender;


    private String keyId;
    private List<InnerQuestionAnswerCommentListViewHolderData> innerQuestionAnswerCommentList;
    private InnerQuestionAnswerCommentListAdapter innerquestionanswercommentlistadapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openEventBus();
        setContentView(R.layout.activity_inner_question_answer_comment, "评论区");
        keyId = getIntent().getStringExtra("key_id");
        getDefinition();
        setUpAnswerCommentRecycleView();
        setUpCommentRecycleViewRefresher();
        registerEvents();
        new GetAnswerCommentTask(activityInnerQuestionAnswerCommentRecycleViewRefresher,
                innerQuestionAnswerCommentList, innerquestionanswercommentlistadapter)
                .execute(keyId);
        setUpToolBar();
    }
    private void setUpSpinner(){

    }
    private void setUpToolBar() {
        setSupportActionBar(activityInnerQuestionAnswerCommentToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getDefinition() {
        innerQuestionAnswerCommentList = new ArrayList<>();
        innerquestionanswercommentlistadapter = new InnerQuestionAnswerCommentListAdapter(this,innerQuestionAnswerCommentList);
    }

    private void registerEvents() {
        activityInnerQuestionAnswerCommentContentSender
                .setOnClickListener(this);
    }

    private void setUpCommentRecycleViewRefresher() {
        activityInnerQuestionAnswerCommentRecycleViewRefresher.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        activityInnerQuestionAnswerCommentRecycleViewRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshAnswerCommentList();
            }
        });
    }

    private void RefreshAnswerCommentList() {
        new GetAnswerCommentTask(activityInnerQuestionAnswerCommentRecycleViewRefresher,
                innerQuestionAnswerCommentList, innerquestionanswercommentlistadapter)
                .execute(keyId);
    }

    private void setUpAnswerCommentRecycleView() {
        activityInnerQuestionAnswerCommentRecycleView.setLayoutManager(new LinearLayoutManager(this));
//        activityInnerQuestionAnswerCommentRecycleView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));
        activityInnerQuestionAnswerCommentRecycleView.setAdapter(innerquestionanswercommentlistadapter);
    }
    public static class RefreshCommentEvent{
        String comment;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public RefreshCommentEvent(String comment) {
            this.comment = comment;
        }
    }
    @SuppressWarnings("unused")
    public void onEventMainThread(RefreshCommentEvent event){
        InnerQuestionAnswerCommentListViewHolderData data = new InnerQuestionAnswerCommentListViewHolderData();
        data.innerQuestionAnswerCommentListItemContentData = event.getComment();
        data.innerQuestionAnswerCommentListItemHeadimgData = MyApp.getInstance().userInfo.userHeadPicURL;
        data.innerQuestionAnswerCommentListItemNameData = MyApp.getInstance().userInfo.userRealName;
        data.innerQuestionAnswerCommentListItemTimeData = TimeLogic.getNowTimeFormatly("yyyy-MM-dd HH:mm:ss");
        innerQuestionAnswerCommentList.add(data);
        innerquestionanswercommentlistadapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        if(UserInfoActivity.verifyState(this))
            return ;
        switch (v.getId()) {
            case R.id.activity_inner_question_answer_comment_content_sender:
                if(TextUtils.isEmpty(activityInnerQuestionAnswerCommentContent.getText()))
                {
                    NotifyHelper.makeToastwithTextAndPic(InnerQuestionAnswerCommentActivity.this,
                            "内容不能为空", R.drawable.ic_error_outline_black_48dp);
                    break;
                }
                new AddAnswerCommentTask((Context) this, keyId).execute(activityInnerQuestionAnswerCommentContent.getText().toString());
                activityInnerQuestionAnswerCommentContent.setText("");
                InputTools.HideKeyboard(activityInnerQuestionAnswerCommentContent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:onBackPressed();break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void registerWidget() {
        activityInnerQuestionAnswerCommentRecycleView = findView(R.id.activity_inner_question_answer_comment_recycle_view);
        activityInnerQuestionAnswerCommentRecycleViewRefresher = findView(R.id.activity_inner_question_answer_comment_recycle_view_refresher);
        activityInnerQuestionAnswerCommentContent = findView(R.id.activity_inner_question_answer_comment_content);
        activityInnerQuestionAnswerCommentToolbar = findView(R.id.toolbar);
        activityInnerQuestionAnswerCommentContentSender = findView(R.id.activity_inner_question_answer_comment_content_sender);
    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }
}
