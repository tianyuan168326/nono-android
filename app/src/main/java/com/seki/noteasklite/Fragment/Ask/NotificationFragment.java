package com.seki.noteasklite.Fragment.Ask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seki.noteasklite.Adapter.NotifyNotificationRecycleViewAdapter;
import com.seki.noteasklite.Controller.CommunityController;
import com.seki.noteasklite.DataUtil.Bean.NotificationDataModel;
import com.seki.noteasklite.DataUtil.Bean.OldNotificationsBean;
import com.seki.noteasklite.DataUtil.BusEvent.GetOldNotificationsDoneEvent;
import com.seki.noteasklite.DividerItemDecoration;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;

import java.util.List;


/**
 * Created by yuan on 2015/8/5.
 */
public class NotificationFragment extends Fragment {
    View rootView;
    RecyclerView notifyNotificationListview;
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
        CommunityController.getOldNotifications();
    }

    private void setUpQuestionRecycleView() {
        notifyNotificationListview.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        notifyNotificationListview.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
    }
    private void getDefinition() {
        notifyNotificationListRefresher = (SwipeRefreshLayout)rootView.findViewById(R.id.notify_notification_list_refresher);
            notificationDataModelList =  MyApp.getInstance().getNotificationDataModelList();
            notifyNotificationListview = (RecyclerView) rootView.findViewById(R.id.notify_notification_listview);
            notifyNotificationRecycleViewAdapter = new NotifyNotificationRecycleViewAdapter
                    (this.getActivity(),notificationDataModelList);
        notifyNotificationListview.setAdapter(notifyNotificationRecycleViewAdapter);
        notifyNotificationRecycleViewAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(GetOldNotificationsDoneEvent e){
        notificationDataModelList.clear();
        for (OldNotificationsBean.NotificationEntityBean notification:
        e.notificationsList) {
            NotificationDataModel m = new NotificationDataModel();
            m.questionId=notification.question_id;
            m.questionAbstract = notification.question_abstract;
            m.otherSideUserRealName = notification.other_side_user_real_name;
            m.otherSideUserId = notification.other_side_user_id;
            m.answerAbstract = notification.key_abstract;
            m.answerId = notification.answer_id;
            m.dataTime = notification.notification_data;
            m.notificationHistoryType = notification.notify_history_type;
            notificationDataModelList.add(m);
        }
        notifyNotificationRecycleViewAdapter.notifyDataSetChanged();
        if(notifyNotificationListRefresher.isRefreshing())
        {
            notifyNotificationListRefresher.setRefreshing(false);
        }
    }
}
