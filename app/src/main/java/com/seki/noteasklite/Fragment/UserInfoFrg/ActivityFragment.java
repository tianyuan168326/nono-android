package com.seki.noteasklite.Fragment.UserInfoFrg;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seki.noteasklite.Activity.Ask.AnswerDetailHTMLActivity;
import com.seki.noteasklite.Activity.Ask.InnerQuestionActivity;
import com.seki.noteasklite.Base.BaseRecycleView;
import com.seki.noteasklite.DataUtil.Bean.UserActivity;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.ThirdWrapper.PowerListener;
import com.seki.noteasklite.Util.NetWorkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    List<Object> list=new ArrayList<>();
    MyAdapter adapter;
    BaseRecycleView recyclerView;
    SwipeRefreshLayout recycle_view_refresher;
    private long id;

    List<UserActivity.ActivityData> activityDataList;

    public static ActivityFragment newInstance(long id) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putLong("Id",id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activityDataList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            id=getArguments().getLong("Id",-1);
        }
    }

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recycle_view, container, false);
        recyclerView=(BaseRecycleView)view.findViewById(R.id.recycle_view);
        recycle_view_refresher = (SwipeRefreshLayout)view.findViewById(R.id.recycle_view_refresher);
        makeData();
        registerRefresher();
        adapter=new MyAdapter();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        TextView empty=(TextView)view.findViewById(R.id.empty_view);
        empty.setText("还没来得及做些什么");
        recyclerView.setEmptyView(empty);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void registerRefresher() {
        recycle_view_refresher.setColorSchemeColors(R.color.colorPrimary,R.color.colorAccent);
        recycle_view_refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recycle_view_refresher.setRefreshing(true);
                makeData();
            }
        });
    }

    private void makeData() {
        StringRequest getUserActivity = new StringRequest(Request.Method.POST, "http://2.diandianapp.sinaapp.com/NONo/NONoGetUserActivity.php"
                , new PowerListener() {
            @Override
            public void onCorrectResponse(String s) {
                super.onCorrectResponse(s);
                UserActivity userActivity = new Gson().fromJson(s,new TypeToken<UserActivity>(){}.getType());
                activityDataList = userActivity.data;
                updateUI(userActivity);
                if(recycle_view_refresher.isRefreshing()){
                    recycle_view_refresher.setRefreshing(false);
                }
            }

            @Override
            public void onJSONStringParseError() {
                super.onJSONStringParseError();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(recycle_view_refresher.isRefreshing()){
                    recycle_view_refresher.setRefreshing(false);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("activity_begin_index","-1");
                params.put("activity_num", "100");
                params.put("me_id",String.valueOf(id));
                return params;
            }
        };
        MyApp.getInstance().volleyRequestQueue.add(getUserActivity);
    }

    private void updateUI(UserActivity userActivity) {
        if(userActivity.state_code != 0 ){
            return ;
        }
        list.clear();
        for (UserActivity.ActivityData activityData:
             activityDataList) {
            switch (activityData.activity_type){
                case UserActivity.Type.ADD_ANSWER:
                    list.add(new AnswerArray(0,activityData.object_question_id,activityData.object_answer_id,activityData.qTitle,activityData.aDetail));
                    break;
                case UserActivity.Type.ADD_ANSWER_COMMENT:
                    list.add(new AnswerArray(2,activityData.object_question_id,activityData.object_answer_id,activityData.qTitle,activityData.aDetail));
                    break;
                case UserActivity.Type.ADD_NOTICE_PEOPLE:
                    list.add(new NoticeArray(activityData.object_user_id,activityData.peopleRealName,activityData.peopleNoticedNum));
                    break;
                case UserActivity.Type.ADD_NOTICE_QUESTION:
                    list.add(new NoticeQuestionArray(activityData.object_question_id,activityData.object_user_id,activityData.qTitle));
                    break;
                case UserActivity.Type.ADD_NOTICE_TOPIC:
                    list.add(new HobbyArray(activityData.activity_user_id,"",activityData.topicNoticedNum));
                    break;
                case UserActivity.Type.ADD_QUESTION:
                    list.add(new AskArray(0,activityData.object_question_id,activityData.qTitle,activityData.qDetail));
                    break;
                case UserActivity.Type.CANCEL_NOTICE_PEOPLE:
                    break;
                case UserActivity.Type.CANCEL_NOTICE_QUESTION:
                    break;
                case UserActivity.Type.CANCEL_NOTICE_TOPIC:
                    break;
                case UserActivity.Type.VOTE_UP_ANSWER:
                    list.add(new AnswerArray(1,activityData.object_question_id,activityData.object_answer_id,activityData.qTitle,activityData.aDetail));
                    //to do
                    break;
                case UserActivity.Type.VOTE_UP_QUESTION:
                    list.add(new AskArray(1,activityData.object_question_id,activityData.qTitle,activityData.qDetail));
                    break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void update(Bundle bundle){

    }

    public static class AnswerArray{
        /**
        * flag
        * 0 回答
        * 1 赞同了回答
        * 2 评论了回答
        * */
        int flag;
        long qid;
        long aid;
        String qTitle;
        String aDetail;

        public AnswerArray(int flag,
                           long qid,
                long aid,
                String qTitle,
                String aDetail){
           this.flag=flag;
            this.qid=qid;
           this.aid=aid;
           this.qTitle=qTitle;
           this.aDetail=aDetail;
        }
    }
    
    public static class AskArray{
        /*
        * flag
        * 0 提出问题
        * 1 赞同问题*/
        int flag;
        long qid;
        String qTitle;
        String qDetail;

        public AskArray(int flag,long qid,
                           String qTitle,
                           String qDetail){
            this.flag=flag;
            this.qid=qid;
            this.qTitle=qTitle;
            this.qDetail=qDetail;
        }
    }

    public static class HobbyArray{
        long id;
        String Title;
        long topicNum;

        public HobbyArray(long id,
                String Title,
                long topicNum){
            this.id=id;
            this.Title=Title;
            this.topicNum=topicNum;
        }
    }

    public static class NoticeArray{
        long id;
        String peopleRealName;
        String peopleHeadImageUrl;
        long noticeNum;
        long votedForNum;
        public NoticeArray(long id,
                          String peopleRealName,
                          long noticeNum){
            this.id=id;
            this.peopleRealName=peopleRealName;
            this.noticeNum=noticeNum;
        }
    }

    public static class NoticeQuestionArray{
        long question_id;
        long questionRaiserId;
        String questionTitle;

        public NoticeQuestionArray(long question_id, long questionRaiserId, String questionTitle) {
            this.question_id = question_id;
            this.questionRaiserId = questionRaiserId;
            this.questionTitle = questionTitle;
        }
    }


    public class MyAdapter extends RecyclerView.Adapter{

        private int lastPosition = -1;

        private void setAnimation(View viewToAnimate, int position)
        {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition)
            {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_activity
                    , parent, false);
            //if(viewType==0){
            //    float density=getResources().getDisplayMetrics().density;
            //    StaggeredGridLayoutManager.LayoutParams lp= (( StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams());
            //    lp.setMargins((int) (8 * density), (int) (8 * density), (int) (8 * density), (int) (8 * density));
            //    view.setLayoutParams(lp);
            //}
            String className=list.get(viewType).getClass().getName();
            if(AnswerArray.class.getName().compareTo(className)==0){
                return new MyAnswerViewHolder(view);
            }else if(AskArray.class.getName().compareTo(className)==0){
                return new MyAskViewHolder(view);
            }else if(HobbyArray.class.getName().compareTo(className)==0){
                return new MyHobbyViewHolder(view);
            }else if(NoticeArray.class.getName().compareTo(className)==0){
                return new MyNoticeViewHolder(view);
            }else if(NoticeQuestionArray.class.getName().compareTo(className)==0){
                return new MyNoticeQuestionViewHolder(view);
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MyAnswerViewHolder){
                onBindMyAnswerViewHolder((MyAnswerViewHolder)holder,position);
            }else if(holder instanceof MyAskViewHolder){
                onBindMyAskViewHolder((MyAskViewHolder)holder,position);
            }else if(holder instanceof MyHobbyViewHolder){
                onBindMyHobbyViewHolder((MyHobbyViewHolder)holder,position);
            }else if(holder instanceof MyNoticeViewHolder){
                onBindMyNoticeViewHolder((MyNoticeViewHolder)holder,position);
            }else if(holder instanceof MyNoticeQuestionViewHolder){
                onBindMyNoticeQuestionViewHolder((MyNoticeQuestionViewHolder) holder, position);
            }
            setAnimation(holder.itemView,position);
        }

        private void onBindMyAnswerViewHolder(MyAnswerViewHolder holder,int position){
            final AnswerArray answerArray=(AnswerArray)list.get(position);
            switch (answerArray.flag){
                case 0:
                    holder.title.setText("回答了问题");break;
                case 1:
                    holder.title.setText("赞同了回答");break;
                case 2:
                    holder.title.setText("评论了回答");break;
            }
            holder.subTitle.setText(answerArray.qTitle);
            holder.text.setText(Html.fromHtml(answerArray.aDetail).toString());
            holder.subTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), InnerQuestionActivity.class)
                                    .putExtra("questionId", String.valueOf(answerArray.qid))
                                    .putExtra("question_title", answerArray.qTitle)
                    );
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnswerDetailHTMLActivity.start(getActivity(), String.valueOf(answerArray.aid), answerArray.qTitle,
                            "", "", "",
                            answerArray.aDetail , "0", "0",String.valueOf(answerArray.qid));
                }
            });

        }

        private void onBindMyAskViewHolder(MyAskViewHolder holder,int position) {
            final AskArray askArray = (AskArray) list.get(position);
            switch (askArray.flag) {
                case 0:
                    holder.title.setText("提出了问题");
                    break;
                case 1:
                    holder.title.setText("赞同了问题");
                    break;
            }
            holder.subTitle.setText(askArray.qTitle);
            holder.text.setText(Html.fromHtml(askArray.qDetail).toString());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), InnerQuestionActivity.class)
                                    .putExtra("questionId", String.valueOf(askArray.qid))
                                    .putExtra("question_title", askArray.qTitle)
                    );
                }
            });
        }

        private void onBindMyHobbyViewHolder(MyHobbyViewHolder holder,int position){
            HobbyArray array=(HobbyArray)list.get(position);
            holder.title.setText("关注了话题");
            holder.subTitle.setText(array.Title);
            holder.text.setText(array.topicNum+"人已关注");
        }

        private void onBindMyNoticeViewHolder(MyNoticeViewHolder holder,int position){
            final NoticeArray array=(NoticeArray)list.get(position);
            holder.title.setText("关注了");
            holder.subTitle.setText(array.peopleRealName);
            holder.text.setText(array.noticeNum+"人已关注");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetWorkUtil.verifyWhichActivityToSwitch(getActivity(), MyApp.userInfo.userId, String.valueOf(array.id));
                }
            });
        }
        private void onBindMyNoticeQuestionViewHolder(MyNoticeQuestionViewHolder holder,int position){
            final NoticeQuestionArray array=(NoticeQuestionArray)list.get(position);
            holder.title.setText("关注了问题");
            holder.subTitle.setText(array.questionTitle);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InnerQuestionActivity.start(getActivity(), String.valueOf(array.question_id), array.questionTitle);
                }
            });
        }
        public class MyAnswerViewHolder extends RecyclerView.ViewHolder{

            TextView title;
            TextView subTitle;
            TextView text;

            MyAnswerViewHolder(View rootView){
                super(rootView);
                title=(TextView)rootView.findViewById(R.id.title);
                subTitle=(TextView)rootView.findViewById(R.id.subtitle);
                text=(TextView)rootView.findViewById(R.id.text);
            }
        }

        public class MyAskViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            TextView subTitle;
            TextView text;
            MyAskViewHolder(View rootView){
                super(rootView);
                title=(TextView)rootView.findViewById(R.id.title);
                subTitle=(TextView)rootView.findViewById(R.id.subtitle);
                text=(TextView)rootView.findViewById(R.id.text);
            }
        }

        public class MyNoticeViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            TextView subTitle;
            TextView text;
            MyNoticeViewHolder(View rootView){
                super(rootView);
                title=(TextView)rootView.findViewById(R.id.title);
                subTitle=(TextView)rootView.findViewById(R.id.subtitle);
                text=(TextView)rootView.findViewById(R.id.text);
            }
        }

        public class MyHobbyViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            TextView subTitle;
            TextView text;
            MyHobbyViewHolder(View rootView){
                super(rootView);
                title=(TextView)rootView.findViewById(R.id.title);
                subTitle=(TextView)rootView.findViewById(R.id.subtitle);
                text=(TextView)rootView.findViewById(R.id.text);
            }
        }


        public class MyNoticeQuestionViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            TextView subTitle;
            TextView text;
            MyNoticeQuestionViewHolder(View rootView){
                super(rootView);
                title=(TextView)rootView.findViewById(R.id.title);
                subTitle=(TextView)rootView.findViewById(R.id.subtitle);
                text=(TextView)rootView.findViewById(R.id.text);
            }
        }

    }
}
