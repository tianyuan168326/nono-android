package com.seki.noteasklite.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seki.noteasklite.Adapter.NotifyNotificationRecycleViewAdapter;
import com.seki.noteasklite.Base.BaseFragment;
import com.seki.noteasklite.Base.BaseRecycleView;
import com.seki.noteasklite.DataUtil.Bean.NotificationDataModel;
import com.seki.noteasklite.DataUtil.BusEvent.ReadAllNotify;
import com.seki.noteasklite.DividerItemDecoration;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;

import java.util.List;


/**
 * Created by yuan on 2015/8/5.
 */
public class NotificationFragment extends BaseFragment {
    View rootView;
    BaseRecycleView notifyNotificationListview;
    NotifyNotificationRecycleViewAdapter notifyNotificationRecycleViewAdapter;
    SwipeRefreshLayout notifyNotificationListRefresher;
    List<NotificationDataModel> notificationDataModelList;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_notification,null);
        return rootView;
    }
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        getDefinition();
        registerEvents();
        setUpQuestionRecycleView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(notifyNotificationRecycleViewAdapter!=null){
            notifyNotificationRecycleViewAdapter.notifyDataSetChanged();
        }
    }


    private void registerEvents() {
        notifyNotificationListRefresher.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        notifyNotificationListRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshNotifyNotificationListview();
            }
        });
    }

    private void RefreshNotifyNotificationListview() {
        notifyNotificationListRefresher.setRefreshing(true);
        notifyNotificationRecycleViewAdapter.notifyDataSetChanged();
        if(notifyNotificationListRefresher.isRefreshing()){
            notifyNotificationListRefresher.setRefreshing(false);
        }
    }

    private void setUpQuestionRecycleView() {
        notifyNotificationListview.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        notifyNotificationListview.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
    }
    private void getDefinition() {
        notifyNotificationListRefresher = (SwipeRefreshLayout)rootView.findViewById(R.id.notify_notification_list_refresher);
        notificationDataModelList =  MyApp.getInstance().getNotificationDataModelList();
        notifyNotificationListview = (BaseRecycleView) rootView.findViewById(R.id.notify_notification_listview);
        notifyNotificationListview.setEmptyView($(R.id.empty_notification));
        notifyNotificationRecycleViewAdapter = new NotifyNotificationRecycleViewAdapter(this.getActivity(),notificationDataModelList);
        notifyNotificationListview.setAdapter(notifyNotificationRecycleViewAdapter);
        notifyNotificationRecycleViewAdapter.notifyDataSetChanged();
    }
    @SuppressWarnings("unused")
    public void onEvent(ReadAllNotify readAllNotify){
        for (NotificationDataModel model :
                notificationDataModelList) {
            model.hasRead = true;
        }
        notifyNotificationRecycleViewAdapter.notifyDataSetChanged();
        NotificationDataModel.broadClearNotification();
        Snackbar.make(getView().findViewById(R.id.root),"已经标记全部消息为已读",Snackbar.LENGTH_SHORT).show();
    }

}
