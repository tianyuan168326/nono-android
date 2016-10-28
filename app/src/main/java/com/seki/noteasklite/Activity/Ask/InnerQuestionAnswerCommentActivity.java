package com.seki.noteasklite.Activity.Ask;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.seki.noteasklite.Activity.UserInfoActivity;
import com.seki.noteasklite.Adapter.InnerQuestionAnswerCommentListAdapter;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Base.RecycleViewLayoutManager.ScrollableLinearLayoutManager;
import com.seki.noteasklite.Controller.CommunityController;
import com.seki.noteasklite.CustomControl.TintImageView;
import com.seki.noteasklite.DataUtil.Bean.AllCommentListBean;
import com.seki.noteasklite.DataUtil.BusEvent.AllCommentEvent;
import com.seki.noteasklite.DataUtil.BusEvent.RefreshCommentEvent;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.InputTools;
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
    private List<AllCommentListBean.CommentEntity> innerQuestionAnswerCommentList;
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
        CommunityController.getReplyCommentAll(keyId);
        setUpToolBar();
    }

    private void setUpToolBar() {
        setSupportActionBar(activityInnerQuestionAnswerCommentToolbar);
        if(getSupportActionBar() != null)
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
        CommunityController.getReplyCommentAll(keyId);
    }

    private void setUpAnswerCommentRecycleView() {
        activityInnerQuestionAnswerCommentRecycleView.setLayoutManager(new ScrollableLinearLayoutManager(this));
//        activityInnerQuestionAnswerCommentRecycleView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));
        activityInnerQuestionAnswerCommentRecycleView.setAdapter(innerquestionanswercommentlistadapter);
    }


    @Override
    public void onClick(View v) {
        if(UserInfoActivity.verifyState(this))
            return ;
        switch (v.getId()) {
            case R.id.activity_inner_question_answer_comment_content_sender:
                CommunityController.postReplyNewComment(keyId,activityInnerQuestionAnswerCommentContent.getText().toString());
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

    @SuppressWarnings("unused")
    public void onEventMainThread(RefreshCommentEvent event){
        Log.d("nono","on comment refresh event");
        activityInnerQuestionAnswerCommentContent.setText("");
        InputTools.HideKeyboard(activityInnerQuestionAnswerCommentContent);
        innerQuestionAnswerCommentList.add(new AllCommentListBean.CommentEntity(
                event.getComment(),
                MyApp.userInfo.userId,
                MyApp.userInfo.userHeadPicURL,
                MyApp.userInfo.userRealName,
                TimeLogic.getNowTimeFormatly("yyyy-MM-dd HH:mm:ss")
        ));
        innerquestionanswercommentlistadapter.notifyDataSetChanged();
        activityInnerQuestionAnswerCommentRecycleView.smoothScrollToPosition(
                innerquestionanswercommentlistadapter.getItemCount() - 1
        );
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(AllCommentEvent event){
        Log.d("nono","on comment event");
        innerQuestionAnswerCommentList.clear();
        innerQuestionAnswerCommentList.addAll(event.c.comment_list);
        innerquestionanswercommentlistadapter.notifyDataSetChanged();
        if(activityInnerQuestionAnswerCommentRecycleViewRefresher.isRefreshing())
            activityInnerQuestionAnswerCommentRecycleViewRefresher.setRefreshing(false);
    }
}
