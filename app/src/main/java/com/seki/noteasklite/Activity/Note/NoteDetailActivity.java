package com.seki.noteasklite.Activity.Note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatEditText;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.seki.noteasklite.Controller.ShareController;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.Delegate.EditNoteDelegate;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.therichedittext.BaseContainer;
import com.seki.therichedittext.BaseRichEditText;
import com.seki.therichedittext.DotLayout;
import com.seki.therichedittext.NumericLayout;
import com.seki.therichedittext.PhotoLayout;
import com.seki.therichedittext.RichEdit;
import com.seki.therichedittext.RichEditText;
import com.seki.therichedittext.TodoLayout;

import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NoteDetailActivity extends NoteDetailBaseActivity {

    private RichEdit contentTV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        openEventBus();
        setContentView(R.layout.activity_note_detail, currentNoteInfo.group);
	}

    @Override
    protected void loadBgColor() {
        super.loadBgColor();
        contentTV.setBackgroundColor(AppPreferenceUtil.getDetailBgColor());
    }

    @Override
    protected NoteHeadContentViewPair getSnapShotViews() {
        return new NoteHeadContentViewPair($(R.id.real_toolbar),$(R.id.note_detail_content));
    }
    @Override
    protected void onResume() {
        contentTV.post(new Runnable() {
            @Override
            public void run() {
                addContentTVEvent();
                contentTV.clearFocus();
                ((NestedScrollView)$(R.id.scrollView)).fullScroll(NestedScrollView.FOCUS_UP);
            }
        });
        super.onResume();
    }
    @Override
	protected HashMap<Integer, String> setUpOptionMenu() {
        setMenuResId(R.menu.menu_note_detail);
		HashMap<Integer, String> idMethosNamePaire = new HashMap<Integer, String>();
		idMethosNamePaire.put(android.R.id.home, "onBackPressed");
		idMethosNamePaire.put(R.id.action_delete, "deleteNote");
		idMethosNamePaire.put(R.id.action_share, "shareNote");
		idMethosNamePaire.put(R.id.action_change_group, "changeGroup");
		idMethosNamePaire.put(R.id.action_change_bg_color, "changeBgColor");
		return idMethosNamePaire;
	}
    @Override
    protected void shareText() {
        ShareController.shareNoteText(this,currentNoteInfo.content);
    }
    @Override
	protected void registerWidget() {
       super.registerWidget();
        contentTV = $(R.id.note_detail_content);
        contentTV.setFocusableInTouchMode(true);
        contentTV.post(new Runnable() {
            @Override
            public void run() {
                addContentTVEvent();

            }
        });
	}

    @Override
    protected void showHelp(View shareFab) {
        if(AppPreferenceUtil.isShowDoubleTap2EditHelp()){
            Snackbar.make(shareFab,"双击进入编辑模式，光标不变哦！",Snackbar.LENGTH_LONG)
                    .setAction("不再提示", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppPreferenceUtil.dontShowDoubleTap2EditHelp();
                        }
                    }).show();
        }
    }

    @Override
    protected NestedScrollView getNestedScrollView() {
        return (NestedScrollView)findViewById(R.id.scrollView);
    }

    float lastX=0;
    float lastY=0;
    float threshold=50;
    long lastTime=0;
    float timeThreshold=300;

    private void addContentTVEvent() {
        int contentChild = contentTV.getChildCount();

        for(int index = 0;index <contentChild;index++){
            final int indexWrapper = index;
            final ViewGroup child = (BaseContainer)contentTV.getChildAt(index);//BaseContainer
            if(child instanceof  RichEditText||
                    child instanceof  DotLayout||
                    child instanceof  NumericLayout||
                    child instanceof TodoLayout
                    ){
                ViewGroup frame =(ViewGroup) child.getChildAt(0);//每个BaseContaner的根FrameLayout
                int count = frame.getChildCount();
                for(int i=0;i<count;i++){
                    View v = frame.getChildAt(i);
                    if(v instanceof BaseRichEditText){
                        v.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent event) {
                                float x = event.getX();
                                float y = event.getY();
                                Date date = new Date();
                                long time = date.getTime();
                                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                                    if (lastX - threshold <= x && x <= lastX + threshold &&
                                            Math.abs(lastTime - time) <= timeThreshold) {
                                        onContentDoubleClick(view,indexWrapper,x,y);
                                        return true;
                                    }
                                }
                                if(event.getAction()==MotionEvent.ACTION_UP) {
                                    lastX = x;
                                    lastY = y;
                                    lastTime = time;
                                    return true;
                                }
                                return true;
                            }
                        });
                    }
                }
            }else if(child instanceof PhotoLayout){
                child.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        onContentDoubleClick(view,indexWrapper,-1,-1);
                        return true;
                    }
                });
            }
        }
    }

    private void onContentDoubleClick(
            //float x, float y
            View view,int index, float x, float y) {
        int sel  = -1;
        if(x !=-1 && y!=-1 && view instanceof AppCompatEditText){
            sel = ((AppCompatEditText)view).getOffsetForPosition(x,y);
        }
        EditNoteDelegate.start(NoteDetailActivity.this,currentNoteInfo,index,sel);
    }

    static int bg_color = MyApp.getInstance().applicationContext.getResources().getColor(R.color.md_search_bg);
    @Override
    protected void renderHighLightNote() {
        int titleIndex = currentNoteInfo.title.indexOf(keyWord);
        SpannableStringBuilder hightTitleBuilder = new SpannableStringBuilder(currentNoteInfo.title);
        if(titleIndex>-1){
            hightTitleBuilder.setSpan(new BackgroundColorSpan(bg_color),titleIndex,titleIndex+keyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        note_detail_groupname.setText(hightTitleBuilder);
        note_detail_time.setText(currentNoteInfo.date+" "+currentNoteInfo.time);

        String orginContent = currentNoteInfo.content;
        Pattern contentP = Pattern.compile(keyWord);

        Pattern contentPNCR = Pattern.compile(encode(keyWord));
        Matcher contentM = contentP.matcher(orginContent);
        Matcher contentMNCR = contentPNCR.matcher(orginContent);
        StringBuilder currentNoteDetailFake  =new StringBuilder();
        int lastIndex = 0;
        int MSearchNum = 0;
        while( contentM.find()){
                currentNoteDetailFake.append(orginContent.substring(lastIndex,contentM.start()));
                currentNoteDetailFake.append("<span style=\"background-color:#d4e157\">");
                currentNoteDetailFake.append(orginContent.substring(contentM.start(),contentM.end()));
                currentNoteDetailFake.append("</span>");
                lastIndex =contentM.end();
            MSearchNum++;
        }
        if(MSearchNum == 0){
            while( contentMNCR.find()){
                currentNoteDetailFake.append(orginContent.substring(lastIndex,contentMNCR.start()));
                currentNoteDetailFake.append("<span style=\"background-color:#d4e157\">");
                currentNoteDetailFake.append(orginContent.substring(contentMNCR.start(),contentMNCR.end()));
                currentNoteDetailFake.append("</span>");
                lastIndex =contentMNCR.end();
            }
        }
        currentNoteDetailFake.append(orginContent.substring(lastIndex,orginContent.length()));
        contentTV.setHtmlText(currentNoteDetailFake.toString(),false);

    }
    public static String encode( String str ) {
        StringBuffer sb = new StringBuffer();
        try{
            char[] ch = str.toCharArray();
            for ( int i = 0 ; i < ch.length ; i++ ) {
                if ( ch[i] < 0x20 || ch[i] > 0x7f )
                    sb.append("&#").append((int) ch[i]).append(";");
                else
                    sb.append(ch[i]);
            }
        }catch (Exception e){
            return "";
        }
        return sb.toString();
    }

    @Override
    protected void renderNormalNote() {
        note_detail_groupname.setText(currentNoteInfo.title);
        note_detail_time.setText(currentNoteInfo.date+" "+currentNoteInfo.time);
        contentTV.setHtmlText(currentNoteInfo.content==null?"":currentNoteInfo.content,false);
    }

    @Override
	protected void registerAdapters() {

	}

    @Override
    protected void setNewContent(String content) {
        contentTV.setHtmlText(currentNoteInfo.content,false);
    }
    PopupWindow popupWindow = null;


    public static Intent start(Context context, NoteAllArray currentNoteInfo){

        Intent intent = new Intent()
                .setClass(context,NoteDetailActivity.class)
                .putExtra("openNote",currentNoteInfo);
        context.startActivity(intent);
        return intent;
    }
    public static Intent start(Context context, NoteAllArray currentNoteInfo, int indexInList){
        Intent intent = new Intent()
                .setClass(context,NoteDetailActivity.class)
                .putExtra("openNote",currentNoteInfo)
                .putExtra("index",indexInList);
        context.startActivity(intent);
        return intent;
    }

    public static Intent start(Context context,NoteAllArray currentNoteInfo,String k){
        Intent intent = new Intent()
                .setClass(context,NoteDetailActivity.class)
                .putExtra("openNote",currentNoteInfo)
                .putExtra("keyWord",k);
        if(!(context instanceof Activity)){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(
                intent
        ) ;
        return intent;
    }

    @Override
	public void onClick(View v) {
        super.onClick(v);
	}

    @Override
    protected void edit() {
        EditNoteDelegate.start(NoteDetailActivity.this,currentNoteInfo,-1,-1);
    }

    public static Intent start(Context context, NoteAllArray currentNoteInfo, OpenNoteDelegate.NoteUIControl ui) {
        Intent intent = new Intent()
                .setClass(context,NoteDetailActivity.class)
                .putExtra("openNote",currentNoteInfo)
                .putExtra("ui",ui);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void themePatch() {
        super.themePatch();

    }
}

