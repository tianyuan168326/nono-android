package com.seki.noteasklite.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.seki.noteasklite.Adapter.FragmentAdapter;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.Fragment.Note.SearchFragment;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * edittext textwatcher  配合 getResult
 */
public class SearchActivity extends BaseActivity {

	private EditText editText;
	private ViewPager viewPager;
	private TabLayout tabLayout;
	private ArrayList<Fragment> fragments;
	private FragmentAdapter fragmentAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search, "搜索");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void registerWidget() {
		editText=(EditText)findViewById(R.id.search_msg);
		tabLayout=(TabLayout)findViewById(R.id.search_tab);
		viewPager=(ViewPager)findViewById(R.id.search_viewpager);
		fragments = new ArrayList<Fragment>();
		fragments.add(SearchFragment.newInstance());
		//search note
		//fragments.add(ContentFragment.newInstance(ContentFragment.TYPE_NOTE));
		//search content
//		fragments.add(ContentFragment.newInstance(ContentFragment.TYPE_CONTENT));
//		//search topic
//		fragments.add(ContentFragment.newInstance(ContentFragment.TYPE_TOPIC));
//		//search user
//		fragments.add(ContentFragment.newInstance(ContentFragment.TYPE_USER));

		fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),
				fragments);
		viewPager.setOffscreenPageLimit(fragments.size());
		viewPager.setAdapter(fragmentAdapter);
		tabLayout.setupWithViewPager(viewPager);

		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String searchKeyWord = s.toString().trim();
				if(searchKeyWord.isEmpty()) {
					searchKeyWord = null;
				}
				NoteController.searchNote(searchKeyWord);
//				SearchTask searchTask = new SearchTask(SearchActivity.this,noteList,contentList,tagList,userList,fragments);
//				searchTask.execute(searchKeyWord,String.valueOf(viewPager.getCurrentItem()));
//				searchTaskList.add(searchTask);
			}
		});
	}

	@Override
	protected void registerAdapters() {

	}

	@Override
	protected HashMap<Integer, String> setUpOptionMenu() {
		return null;
	}

	@Override
	protected void onPause() {
		super.onPause();
//		for(SearchTask task:searchTaskList)
//		{
//			task.cancel(true);
//		}
	}

	@Override
	public void onClick(View v) {

	}
}
