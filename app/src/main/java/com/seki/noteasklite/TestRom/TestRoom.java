package com.seki.noteasklite.TestRom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seki.noteasklite.R;

/**
 * Created by yuan-tian01 on 2016/2/27.
 */
public class TestRoom extends AppCompatActivity{
    ViewPager viewPager;
    BallTabLayout ballTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testroom);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        ballTabLayout =(BallTabLayout)findViewById(R.id.ball_tab_layout);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                return FragmentWithNum.newInstance(position);
            }
        });
        ballTabLayout.setupWithViewPager(viewPager);
    }
    public static  class FragmentWithNum extends  Fragment{
        private int num;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_num,container,false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ((TextView)(getView().findViewById(R.id.tv_num))).setText(String.valueOf(num));
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            num = getArguments().getInt("num");
        }
        public static FragmentWithNum newInstance(int num){
            FragmentWithNum fragment = new FragmentWithNum();
            Bundle args = new Bundle();
            args.putInt("num", num);
            fragment.setArguments(args);
            return fragment;
        }
    }
}
