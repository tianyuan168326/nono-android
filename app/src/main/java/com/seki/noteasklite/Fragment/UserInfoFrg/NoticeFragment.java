package com.seki.noteasklite.Fragment.UserInfoFrg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seki.noteasklite.Base.BaseRecycleView;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.DataUtil.Bean.UserNoticingUserListInfo;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.ThirdWrapper.PowerListener;
import com.seki.noteasklite.ThirdWrapper.PowerStringRequest;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.NetWorkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends Fragment {

    List<NoticeArray> list = new ArrayList<>();
    MyAdapter adapter;
    BaseRecycleView recyclerView;
    private long id;


    public static NoticeFragment newInstance(long id) {
        NoticeFragment fragment = new NoticeFragment();
        Bundle args = new Bundle();
        args.putLong("Id",id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            id=getArguments().getLong("Id",-1);
        }
    }
    public NoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.recycle_view, container, false);
        recyclerView=(BaseRecycleView)view.findViewById(R.id.recycle_view);
        makeData();
        adapter=new MyAdapter();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        TextView empty=(TextView)view.findViewById(R.id.empty_view);
        empty.setText("还没有谁能引起TA的兴趣");
        recyclerView.setEmptyView(empty);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void makeData() {
        StringRequest getNoticingList = new PowerStringRequest(Request.Method.POST,
                NONoConfig.makeNONoSonURL("/NONo/NONoGetNoticingPeople.php")
                , new PowerListener() {
            @Override
            public void onCorrectResponse(String s) {
                super.onCorrectResponse(s);
                UserNoticingUserListInfo listInfo = new Gson().fromJson(s,new TypeToken<UserNoticingUserListInfo>(){}.getType());
                updateUI(listInfo);
            }

            @Override
            public void onJSONStringParseError() {
                super.onJSONStringParseError();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("me_id", String.valueOf(id));
                return params;
            }
        };
        MyApp.getInstance().volleyRequestQueue.add(getNoticingList);
    }

    private void updateUI(UserNoticingUserListInfo listInfo) {
        if(listInfo.state_code != 0){
            return ;
        }
        if(listInfo.data.size() ==0){
            return ;
        }
        for (UserNoticingUserListInfo.FullUserinfo fullUserInfo:
             listInfo.data) {
            list.add(new NoticeArray(fullUserInfo.userId,fullUserInfo.userRealname,fullUserInfo.noticed_num
            ,fullUserInfo.user_voted_up_num,fullUserInfo.userUniversity,fullUserInfo.userSubject
            ,fullUserInfo.user_headpic));
        }
        adapter.notifyDataSetChanged();
    }

    public void update(Bundle bundle){

    }

    public static class NoticeArray{
        long id;
        String name;
        long noticeNum;
        long agreeNum;
        String university;
        String school;
        String headImage;
        public NoticeArray(
                long id,
                String name,
                long noticeNum,
                long agreeNum,
                String university,
                String school,String headImage){

           this.id=id;
           this.name=name;
           this.noticeNum=noticeNum;
           this.agreeNum=agreeNum;
           this.university=university;
           this.school=school;
            this.headImage = headImage;
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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            setAnimation(holder.itemView, position);
            MyViewHolder holderWrapper = (MyViewHolder)holder;
            final NoticeArray noticeArray = (NoticeArray)list.get(position);
            try{
                FrescoImageloadHelper.simpleLoadImageFromURL(holderWrapper.userHeadView,noticeArray.headImage);
            }catch(Exception e){
                e.printStackTrace();
            }
            holderWrapper.userName.setText(noticeArray.name);
            holderWrapper.userNoticedNum.setText(String.valueOf(noticeArray.noticeNum));
            holderWrapper.userVotedUpNum.setText(String.valueOf(noticeArray.agreeNum));
            holderWrapper.userSchool.setText(noticeArray.school);
            holderWrapper.userUniversity.setText(noticeArray.university);
            holderWrapper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetWorkUtil.verifyWhichActivityToSwitch(getActivity(),MyApp.userInfo.userId,String.valueOf(noticeArray.id));
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notice
                    , parent, false);
            //if(viewType==0){
            //    float density=getResources().getDisplayMetrics().density;
            //    StaggeredGridLayoutManager.LayoutParams lp= (( StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams());
            //    lp.setMargins((int) (8 * density), (int) (8 * density), (int) (8 * density), (int) (8 * density));
            //    view.setLayoutParams(lp);
            //}
            return new MyViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            public SimpleDraweeView userHeadView ;
            public TextView userName;
            public TextView userNoticedNum;
            public TextView userVotedUpNum;
            public TextView userUniversity;
            public TextView userSchool;
            MyViewHolder(View rootView){
                super(rootView);
                userHeadView = (SimpleDraweeView)rootView.findViewById(R.id.user_head);
                userName = (TextView)rootView.findViewById(R.id.user_name);
                userNoticedNum = (TextView)rootView.findViewById(R.id.user_noticed_num);
                userVotedUpNum = (TextView)rootView.findViewById(R.id.user_voted_up_num);
                userUniversity = (TextView)rootView.findViewById(R.id.user_university);
                userSchool = (TextView)rootView.findViewById(R.id.user_school);
            }
        }
    }

}
