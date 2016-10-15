package com.seki.noteasklite.Activity.Ask;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Controller.CommunityController;
import com.seki.noteasklite.DataUtil.BusEvent.PostAnswerFailEvent;
import com.seki.noteasklite.DataUtil.BusEvent.PostAnswerSuccessEvent;
import com.seki.noteasklite.R;
import com.seki.therichedittext.EditView;

import java.util.HashMap;

import de.greenrobot.event.EventBus;


public class AnswerInputActivity extends BaseActivity {
    private String questionId;
    private EditView mEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_input,"发表自己的观点");
        mEditor =$(R.id.answer_input_richeditor);
        questionId = getIntent().getStringExtra("question_id");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_answer_input_activty,menu);
        return true;
    }
    public static void start(Context context){
        context.startActivity(new Intent()
        .setClass(context,AnswerInputActivity.class));

    }
    Dialog waitingDialog;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.answer_input_commit_button:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(mEditor.findFocus()!=null) {
                    imm.hideSoftInputFromWindow(mEditor.findFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                CommunityController.postNew(mEditor.getHtmlText(),questionId);
                waitingDialog = new AlertDialog.Builder(this)
                        .setView(R.layout.layout_more_progress)
                        .show();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void registerWidget() {

    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // mEditor.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {

    }

    public void onEventMainThread(PostAnswerSuccessEvent e) {
        if(waitingDialog !=null){
            waitingDialog.dismiss();
            finish();
        }
        EventBus.getDefault().post(new InnerQuestionActivity.AnswerListUpdateEvent());
    }
    public void onEventMainThread(PostAnswerFailEvent e) {
        if(waitingDialog !=null){
            waitingDialog.setTitle("添加失败( ▼-▼ )");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    waitingDialog.dismiss();
                    finish();
                }
            },200);
        }
    }
}
