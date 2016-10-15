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

import com.seki.noteasklite.Base.BaseRecycleView;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HobbyFragment extends Fragment {

    List<HobbyArray> list;
    MyAdapter adapter;
    BaseRecycleView recyclerView;
    private long id;


    public static HobbyFragment newInstance(long id) {
        HobbyFragment fragment = new HobbyFragment();
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
    public HobbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.recycle_view, container, false);
        recyclerView=(BaseRecycleView)view.findViewById(R.id.recycle_view);
        list=new ArrayList<>();
        list.add(new HobbyArray(1,"",1,1));
        list.add(new HobbyArray(1,"",1,1));
        list.add(new HobbyArray(1,"",1,1));
        adapter=new MyAdapter();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        TextView empty=(TextView)view.findViewById(R.id.empty_view);
        empty.setText("似乎没什么感兴趣的呢");
        recyclerView.setEmptyView(empty);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void update(Bundle bundle){

    }

    public static class HobbyArray{
        long hid;
        String title;
        long noticeNum;
        long contentNum;

        HobbyArray(long hid,
                String title,
                long noticeNum,
                long contentNum){
            this.hid=hid;
            this.title=title;
            this.noticeNum=noticeNum;
            this.contentNum=contentNum;
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
            setAnimation(holder.itemView,position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hobby
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

            MyViewHolder(View rootView){
                super(rootView);
            }
        }
    }
}
