package com.seki.noteasklite.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by 七升 on 2015/7/17.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;

	public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int pos) {
		return fragments.get(pos);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position){
			case 0:return "笔记";
			case 1:return "内容";
			case 2:return "标签";
			case 3:return "用户";
		}
		return "";
	}
}
