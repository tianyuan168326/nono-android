package com.seki.noteasklite.Activity.Ask;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DataUtil.BusEvent.ReadAllNotify;
import com.seki.noteasklite.Fragment.Ask.ChatListFragment;
import com.seki.noteasklite.Fragment.NotificationFragment;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created by yuan on 2015/8/4.
 */
public class NotifyActivity extends BaseActivity  {
    TabLayout notifyTabLayout;
    ViewPager notifyViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify, "最近的通知");
        setupViewPager();
        setupTabLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(int i=0;i< notifyViewPagerAdapter.getCount();i++){
            notifyViewPagerAdapter.getItem(i).onResume();
        }

    }

    private void setupTabLayout() {

        notifyTabLayout.addTab( notifyTabLayout.newTab());
        notifyTabLayout.setupWithViewPager(notifyViewPager);
    }
    NotifyViewPagerAdapter notifyViewPagerAdapter = new NotifyViewPagerAdapter(getSupportFragmentManager());
    private void setupViewPager() {
        notifyViewPagerAdapter.addFragment(new NotificationFragment(),getString(R.string.me));
        notifyViewPagerAdapter.addFragment(new ChatListFragment(),"信件");
        notifyViewPager.setAdapter(notifyViewPagerAdapter);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void registerWidget() {
        notifyTabLayout = findView(R.id.notify_tab_layout);
        notifyViewPager = findView(R.id.notify_view_pager);
    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer,String> setUpOptionMenu() {
        setMenuResId(R.menu.menu_notify);
        HashMap<Integer,String> map = new HashMap<>();
        map.put(R.id.action_read_all,"handleReadAll");
        return map;
    }
    @SuppressWarnings("unused")
    private void handleReadAll(){
        EventBus.getDefault().post(new ReadAllNotify());
    }
    class NotifyViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private  final List<String> titleList = new ArrayList<>();
        public NotifyViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        public void addFragment(Fragment fragment,String title)
        {
            fragmentList.add(fragment);
            titleList.add(title);
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    @Override
    protected void themePatch() {
        super.themePatch();
        $(R.id.notify_tab_layout).setBackgroundColor(ThemeController.getCurrentColor().mainColor);
    }

}
