package com.seki.noteasklite.Activity.Note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.Controller.ShareController;
import com.seki.noteasklite.CustomControl.MarkDownWebView;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.Delegate.EditNoteDelegate;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;

import java.util.HashMap;

/**
 * Created by yuan on 2016/5/26.
 */
public class MarkDownNoteDetailActivity  extends NoteDetailBaseActivity{
    private MarkDownWebView contentWV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_note_detail, currentNoteInfo.group);
        openEventBus();
        howToEditmMode();
    }
    GestureDetector mGestureDetector;
    private void howToEditmMode() {
        mGestureDetector = new GestureDetector(this, onGestureListener);
    }
    @Override
    protected void showHelp(View container) {
        if(AppPreferenceUtil.isShowMarkDownDetailHelp()){
            Snackbar.make(container,"右滑或双击都可以进入MarkDown编辑模式哦!",Snackbar.LENGTH_LONG)
                    .setAction("不再提醒", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppPreferenceUtil.dontShowMarkDownDetailHelp();
                        }
                    })
                    .show();
        }
    }
    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {
        long ts  = 0 ;
        int thred = 50;
        float oldX = 0;
        float oldY  = 0;
        @Override
        public boolean onDown(MotionEvent e) {
            long currentSt = System.currentTimeMillis();
            long deltaSt = currentSt - ts;
            ts  = currentSt;
            if ((deltaSt) <300 ){

                if(Math.abs(e.getX() - oldX) <thred &&
                        Math.abs(e.getY()-oldY)<thred
                        ){
                    EditNoteDelegate.start(MarkDownNoteDetailActivity.this,currentNoteInfo);
                    oldX = e.getX();
                    oldY = e.getY();
                    return true;
                }
            }
            oldX = e.getX();
            oldY = e.getY();
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() <- 120) {
                EditNoteDelegate.start(MarkDownNoteDetailActivity.this,currentNoteInfo);
                return true;
            }
            return false;
        }

        //添加未实现的方法

    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
       // return mGestureDetector.onTouchEvent(event);
    }//

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
         mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void loadBgColor() {
        super.loadBgColor();
        contentWV.setBackgroundColor(AppPreferenceUtil.getDetailBgColor());
        $(R.id.scrollView).setBackgroundColor(AppPreferenceUtil.getDetailBgColor());
    }

    @Override
    protected NoteHeadContentViewPair getSnapShotViews() {
        return new NoteHeadContentViewPair($(R.id.real_toolbar),$(R.id.note_detail_content));
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
        ShareController.shareNoteText(this,htmlString);
    }

    @Override
    protected NestedScrollView getNestedScrollView() {
        return (NestedScrollView)$(R.id.scrollView);
    }

    @NonNull
    @Override
    public String getLocalClassName() {
        return super.getLocalClassName();
    }

    @Override
    protected void renderNormalNote() {
        Log.d(NONoConfig.TAG_NONo,"begin load markdown!");
//        contentWV.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                parseMarkdown(currentNoteInfo.content,contentWV);
////                getView().findViewById(R.id.loading_bg).setVisibility(View.GONE);
////                getView().findViewById(R.id.actual_view).setVisibility(View.VISIBLE);
//            }
//        });
    }
    public void parseMarkdown(String markdown,WebView webView){
        if(webView==null ){
            return;
        }
        final String hm=markdown.replace("\\","\\\\").replace("\n","\\n").replace("\t","\\t");
        webView.loadUrl("javascript:parseMarkdown('"+hm+"')");
        webView.loadUrl("javascript:window.HtmlViewer.getHtml" +
                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        showTextNum(htmlString);
    }
    @Override
    protected void renderHighLightNote() {

    }

    @Override
    protected void setNewContent(String content) {
        parseMarkdown(currentNoteInfo.content,contentWV);
    }


    @Override
    protected void registerAdapters() {

    }

    @Override
    protected void registerWidget() {
        super.registerWidget();
        contentWV = $(R.id.note_detail_content);
        contentWV.addJavascriptInterface(new MarkDownJSBridge(), "HtmlViewer");
        contentWV.ini();
        contentWV.setWebViewClient(new MyWebView(this));

        contentWV.loadUrl("file:///android_asset/markdown.html");
    }
    String htmlString;

    public static Intent start(Context context, NoteAllArray currentNoteInfo, OpenNoteDelegate.NoteUIControl ui) {
        Intent intent = new Intent()
                .setClass(context,MarkDownNoteDetailActivity.class)
                .putExtra("openNote",currentNoteInfo)
                .putExtra("ui",ui)
                ;
        context.startActivity(intent);
        return intent;
    }
    public static Intent start(Context context,NoteAllArray currentNoteInfo,String k){
        Intent intent = new Intent()
                .setClass(context,MarkDownNoteDetailActivity.class)
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

    public  class MyWebView extends WebViewClient {
        Context context=null;
        public MyWebView(Context context){
            this.context=context;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            final String hm=currentNoteInfo.content .replace("\\","\\\\").replace("\n","\\n").replace("\t","\\t");
            view.loadUrl("javascript:parseMarkdown('"+hm+"')");
            view.loadUrl("javascript:window.HtmlViewer.getHtml" +
                    "(''+document.getElementById('content').innerHTML+'');");
            showTextNum(htmlString);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url); //url为你要链接的地址
            Intent intent =new Intent(Intent.ACTION_VIEW, uri);
            ((Activity)context).startActivity(intent);
            return true;
        }
    }
    class MarkDownJSBridge {
        @JavascriptInterface
        public void getHtml(String html) {
            htmlString = html;
        }

    }
    public static Intent start(Context context, NoteAllArray currentNoteInfo){
        Intent intent = new Intent()
                .setClass(context,MarkDownNoteDetailActivity.class)
                .putExtra("openNote",currentNoteInfo);
        context.startActivity(intent);
        return intent;
    }
    public static Intent start(Context context, NoteAllArray currentNoteInfo, int indexInList){
        Intent intent = new Intent()
                .setClass(context,MarkDownNoteDetailActivity.class)
                .putExtra("openNote",currentNoteInfo)
                .putExtra("index",indexInList);
        context.startActivity(intent);
        return intent;
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void edit() {
        EditNoteDelegate.start(MarkDownNoteDetailActivity.this,currentNoteInfo);
    }


}
