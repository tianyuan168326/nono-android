package com.seki.noteasklite.Activity.Note;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seki.noteasklite.CustomControl.MarkDownWebView;
import com.seki.noteasklite.CustomControl.MarkdownEditText;
import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.BusEvent.MarkDownUndoEditEvent;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MarkdownNoteEditActivity extends NoteEditBaseActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void registerAdapters() {

    }
    NoteAllArray noteData ;
    String keyWord;
    int indexInList;
    public static final int OpenNote = 1;
    public static final int EditNote = 2;
    int mode;

    public static Intent openNote(Context context, NoteAllArray currentNoteInfo, int indexInList){
        Intent intent = new Intent()
                .setClass(context,MarkdownNoteEditActivity.class)
                .putExtra("data",currentNoteInfo)
                .putExtra("index",indexInList);
        intent.putExtra("mode",OpenNote);
        context.startActivity(intent);
        return  intent;
    }
    public static Intent openNote(Context context, NoteAllArray currentNoteInfo, String k){
        Intent intent = new Intent()
                .setClass(context,MarkdownNoteEditActivity.class)
                .putExtra("data",currentNoteInfo)
                .putExtra("keyWord",k);
        intent.putExtra("mode",OpenNote);
        context.startActivity(intent);
        return  intent;
    }
    public static Intent openNote(Context context,NoteAllArray currentNoteInfo){
        Intent intent = new Intent()
                .setClass(context,MarkdownNoteEditActivity.class)
                .putExtra("data",currentNoteInfo);
        intent.putExtra("mode",OpenNote);
        context.startActivity(intent);
        return  intent;
    }
    public static Intent openNote(Context context){
        Intent intent = new Intent(context, MarkdownNoteEditActivity.class);
        context.startActivity(intent);
        return intent;
    }
    public static Intent editNote(Context context){
        Intent intent = new Intent(context, MarkdownNoteEditActivity.class);
        context.startActivity(intent);
        return intent;
    }
    public static Intent editNote(Context context, NoteAllArray currentNoteInfo, int indexInList){
        Intent intent = new Intent()
                .setClass(context,MarkdownNoteEditActivity.class)
                .putExtra("data",currentNoteInfo)
                .putExtra("index",indexInList);
        intent.putExtra("mode",EditNote);
        context.startActivity(intent);
        return  intent;
    }
    public static Intent editNote(Context context, NoteAllArray currentNoteInfo, String k){
        Intent intent = new Intent()
                .setClass(context,MarkdownNoteEditActivity.class)
                .putExtra("data",currentNoteInfo)
                .putExtra("keyWord",k);
        intent.putExtra("mode",EditNote);
        context.startActivity(intent);
        return  intent;
    }
    public static Intent editNote(Context context,NoteAllArray currentNoteInfo){
        Intent intent = new Intent()
                .setClass(context,MarkdownNoteEditActivity.class)
                .putExtra("data",currentNoteInfo);
        intent.putExtra("mode",EditNote);
        context.startActivity(intent);
        return  intent;


//        Intent intent = new Intent()
//                .setClass(context,MarkdownNoteEditActivity.class)
//                .putExtra("GroupName", currentNoteInfo.group)
//                .putExtra("noteTitle", currentNoteInfo.title)
//                .putExtra("uuid", currentNoteInfo.uuid)
//                .putExtra("sdfId",currentNoteInfo.sdfId);
//        intent.putExtra("mode",EditNote);
//        context.startActivity(intent);
//        return  intent;

    }

    private void processIntent() {
        noteData = getIntent().getParcelableExtra("data");
        keyWord = getIntent().getStringExtra("keyWord");
        if(TextUtils.isEmpty(keyWord)){
            keyWord = "";
        }
        indexInList = getIntent().getIntExtra("index",-1);
        mode = getIntent().getIntExtra("mode",EditNote);
        switch (mode){
            case OpenNote:

                break;
        }
    }

    @Override
    protected IntentDataTuple getIntentData() {
        processIntent();
        if(noteData == null){
         return null;
        }
        String groupName = noteData.group;
        long sdfId = noteData.sdfId;
        String Title = noteData.title;
        String uuid = noteData.uuid;
        groupName = TextUtils.isEmpty(groupName)? AppPreferenceUtil.getDefaultGroup():groupName;
        boolean isNew = true;
        String content = null;
        if (sdfId >= 0) {
            isNew = false;
            content = NoteDBHelper.getInstance().getContentById(sdfId);
        }
        return new IntentDataTuple(sdfId,Title, content,  groupName,  uuid,  isNew);

    }

    @Override
    protected void loadEditBgColor() {
        super.loadEditBgColor();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if(fragments == null){
            return;
        }
        PlaceholderFragment f0 = (PlaceholderFragment) fragments.get(0);
        PlaceholderFragment f1 = (PlaceholderFragment) fragments.get(1);
        if (f1.webView != null) {
            f1.webView.setBackgroundColor(AppPreferenceUtil.getEditBgColor());
        } else if (f0.webView != null) {
            f0.webView.setBackgroundColor(AppPreferenceUtil.getEditBgColor());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown,groupName);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.note_edit_editText);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position==1) {
                    try {
                        List<Fragment> fragments = getSupportFragmentManager().getFragments();
                        PlaceholderFragment f0 = (PlaceholderFragment) fragments.get(0);
                        PlaceholderFragment f1 = (PlaceholderFragment) fragments.get(1);
                        if (f1.webView != null) {
                            f1.parseMarkdown(f0.getMarkdown());
                        } else if (f0.webView != null) {
                            f0.parseMarkdown(f1.getMarkdown());
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        switch (mode){
            case OpenNote:
                mViewPager.setCurrentItem(1);
                break;
            case EditNote:
                mViewPager.setCurrentItem(0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            PlaceholderFragment f0 = (PlaceholderFragment) fragments.get(0);
            PlaceholderFragment f1 = (PlaceholderFragment) fragments.get(1);
            if (f1.webView != null) {
                ((MarkdownEditText) f0.getView().findViewById(R.id.markdown_editor)).onActivityResult(requestCode, resultCode, data);
            } else if (f0.webView != null) {
                ((MarkdownEditText) f1.getView().findViewById(R.id.markdown_editor)).onActivityResult(requestCode, resultCode, data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {

    }
    @Override
    protected void registerWidget() {
        titleEditText  =$(R.id.note_edit_title);
        if(noteData !=null){
            titleEditText.setText(noteData.title);
        }

    }

    public static class PlaceholderFragment extends Fragment {

        class History{
            int curPos;
            String history;

            public History(int curPos, String history) {
                this.curPos = curPos;
                this.history = history;
            }

            public History() {
            }
        }
        int MAX_HISTORY = 10;
        List<History> historyList = new ArrayList<>();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(historyList.size() == MAX_HISTORY+1){
                    historyList.remove(0);
                }
                historyList.add(new History(start,s.toString()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_CONTENT = "content";
        private static final int TYPE_EDIT=0x01;
        private static final int TYPE_VIEW=0x02;
        MarkDownWebView webView=null;
        AppCompatEditText edit = null;
        public PlaceholderFragment() {
        }
        public void onEvent(MarkDownUndoEditEvent e){
            if(historyList.size() <1){
               // Toast.makeText(getActivity(),"没有可撤销的记录了",Toast.LENGTH_SHORT).show();
            }else{
                History h = historyList.get(historyList.size()-1);
                edit.removeTextChangedListener(textWatcher);
                edit.setText(h.history);
                edit.setSelection(h.curPos);
                historyList.remove(h);
                edit.addTextChangedListener(textWatcher);
            }
        }
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber,String content) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_CONTENT,content);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onResume() {
            super.onResume();
            EventBus.getDefault().register(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            EventBus.getDefault().unregister(this);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView=null;
            final String content = getArguments().getString(ARG_CONTENT);
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case TYPE_EDIT:
                    showHelp(container);
                    rootView = inflater.inflate(R.layout.fragment_markdown, container, false);
                    edit =(AppCompatEditText) rootView.findViewById(R.id.markdown_edit);
                    edit.requestFocus();
                    if(content !=null){
                        edit.setText(content);
                        historyList.add(new History(0,content));
                    }
                    edit.addTextChangedListener(textWatcher);
                    break;
                case TYPE_VIEW:
                    showHelp(container);
                    rootView=inflater.inflate(R.layout.fragment_webview,container,false);
                    webView=(MarkDownWebView) rootView.findViewById(R.id.web_view);
                    webView.ini();
                    webView.setBackgroundColor(AppPreferenceUtil.getEditBgColor());

                    webView.loadUrl("file:///android_asset/markdown.html");
                    webView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            parseMarkdown(content);
                            getView().findViewById(R.id.loading_bg).setVisibility(View.GONE);
                            getView().findViewById(R.id.actual_view).setVisibility(View.VISIBLE);
                        }
                    },300);
            }
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            if(getArguments().getInt(ARG_SECTION_NUMBER) == TYPE_VIEW){
                while(!webView.requestFocus()){
                    parseMarkdown(getArguments().getString(ARG_CONTENT));
                }
            }
        }

        private void showHelp(ViewGroup container) {
            if(AppPreferenceUtil.isShowMarkDownEditHelp()){
                Snackbar.make(container,"左右滑动进行预览/编辑切换",Snackbar.LENGTH_LONG)
                        .setAction("不再提醒", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AppPreferenceUtil.dontShowMarkDownEditHelp();
                            }
                        })
                        .show();
            }
        }

        public String getMarkdown(){
            return ((MarkdownEditText) getView().findViewById(R.id.markdown_editor)).getText();
        }

        public void parseMarkdown(String markdown){
            if(webView==null ){
                return;
            }
            final String hm=markdown.replace("\\","\\\\").replace("\n","\\n").replace("\t","\\t");
            webView.loadUrl("javascript:parseMarkdown('"+hm+"')");
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:return PlaceholderFragment.newInstance(PlaceholderFragment.TYPE_EDIT,noteData==null?"":noteData.content);
                case 1:return PlaceholderFragment.newInstance(PlaceholderFragment.TYPE_VIEW,noteData==null?"":noteData.content);
                default:return null;
            }
        }
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
    @Override
    protected void save() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        PlaceholderFragment f0 = (PlaceholderFragment) fragments.get(0);
        PlaceholderFragment f1 = (PlaceholderFragment) fragments.get(1);
        if (f1.webView != null) {
            saveText(f0.getMarkdown(),titleEditText.getText().toString(),".md");
           // f1.parseMarkdown(f0.getMarkdown());
        } else if (f0.webView != null) {
            saveText(f1.getMarkdown(),titleEditText.getText().toString(),".md");
//            f0.parseMarkdown(f1.getMarkdown());
        }


        finish();
    }

    @Override
    public String getAutoSaveText() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        PlaceholderFragment f0 = (PlaceholderFragment) fragments.get(0);
        PlaceholderFragment f1 = (PlaceholderFragment) fragments.get(1);
        if (f1.webView != null) {
            return f0.getMarkdown();
        } else if (f0.webView != null) {
            return f1.getMarkdown();
        }else{
            return "";
        }
    }

    @Override
    public String getHtmlTextForTextNum() {
        return "0";
    }

    @Override
    public String getAffix() {
        return ".md";
    }

    public void undoEdit(){
        EventBus.getDefault().post(new MarkDownUndoEditEvent());
    }
    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        setMenuResId(R.menu.menu_note_edit);
        HashMap<Integer, String> idMethosNamePaire = new HashMap<Integer, String>();
        idMethosNamePaire.put(android.R.id.home, "save");
        idMethosNamePaire.put(R.id.action_save, "save");
        idMethosNamePaire.put(R.id.action_lable,"addLable");
        idMethosNamePaire.put(R.id.action_undo,"undoEdit");
        idMethosNamePaire.put(R.id.action_change_bg_color,"changeBgColor");
        idMethosNamePaire.put(R.id.action_help,"showHelpWindow");
        return  idMethosNamePaire;
    }
    protected  void showHelpWindow(){
        Uri  uri = Uri.parse("http://www.appinn.com/markdown/basic.html");
        Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public String getFromAssets(String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

