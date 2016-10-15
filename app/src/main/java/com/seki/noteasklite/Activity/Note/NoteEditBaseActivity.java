package com.seki.noteasklite.Activity.Note;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.Controller.NoteLabelController;
import com.seki.noteasklite.Controller.NoteReelsController;
import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.noteasklite.Util.FuckBreaker;
import com.seki.noteasklite.Util.PreferenceUtils;
import com.seki.therichedittext.ColorPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yuan on 2016/5/23.
 */
public abstract  class NoteEditBaseActivity extends BaseActivity {
    protected boolean isNew = true;

    protected long sdfId;
    protected String content = "";
    protected String Title;
    protected String groupName;
    protected String uuid;
    protected AppCompatEditText titleEditText;
    protected AppCompatEditText noteEditTitle;
    //@ R.id.title_zone ; used for title zone
    protected LinearLayout title_zone;
    //@ (R.id.note_edit_editText); used for edit text
    View editView;
    //@ R.id.title_zone ; used for title zone
   // View title_zone;
    private Runnable autoSaveTask;
    private Runnable showTextNumTask;
    protected Handler handler = new Handler();

    static class IntentDataTuple{
        public long sdfId;
        public String Title;
        public String content;
        public String groupName;
        public String uuid;
        public boolean isNew;

        public IntentDataTuple(long sdfId, String title, String content, String groupName, String uuid, boolean isNew) {
            this.sdfId = sdfId;
            Title = title;
            this.content = content;
            this.groupName = groupName;
            this.uuid = uuid;
            this.isNew = isNew;
        }
    }

    protected  abstract IntentDataTuple getIntentData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupName = AppPreferenceUtil.getDefaultGroup();
        IntentDataTuple  intentData= getIntentData();
        if(intentData != null){
            groupName = intentData.groupName;
            sdfId = intentData.sdfId;
            Title = intentData.Title;
            uuid = intentData.uuid;
            content = intentData.content;
            isNew = intentData.isNew;
        }
        autoSaveTask = new Runnable() {
            @Override
            public void run() {
                autoSaveNote(getAutoSaveText(),getAffix() ,titleEditText);
                handler.postDelayed(this, 5000);
            }
        };
        showTextNumTask = new Runnable() {
            @Override
            public void run() {
                if ($(R.id.note_text_num) != null) {
                    ((TextView) $(R.id.note_text_num))
                            .setText(String.valueOf(FuckBreaker.fuckBreakerAndSpace(getHtmlTextForTextNum()).length()));
                }
                handler.postDelayed(this, 500);
            }
        };
    }

    @Override
    public void setContentView(int layoutResID, String title) {
        super.setContentView(layoutResID, title);
        afterRegisterWidget();
    }

    public static Intent start(Context context, String groupName, long sdfId, String Title, String uuid){
        Intent intent = new Intent(context, NoteEditActivity.class)
                .putExtra("GroupName", groupName)
                .putExtra("noteTitle", Title)
                .putExtra("uuid", uuid)
                .putExtra("sdfId",sdfId);
        context.startActivity(intent);
        return intent;
    }
    public static Intent start(Context context, NoteAllArray date){
        Intent intent = new Intent(context, NoteEditActivity.class)
                .putExtra("GroupName", date.group)
                .putExtra("noteTitle", date.title)
                .putExtra("uuid", date.uuid)
                .putExtra("sdfId",date.sdfId);
        context.startActivity(intent);
        return intent;
    }
    public static Intent start(Context context, String groupName){
        Intent intent = new Intent(context, NoteEditActivity.class)
                .putExtra("GroupName", groupName);
        context.startActivity(intent);
        return intent;
    }
    public static Intent start(Context context){
        Intent intent = new Intent(context, NoteEditActivity.class);
        context.startActivity(intent);
        return intent;
    }
    protected void loadEditBgColor() {
        findView(R.id.note_edit_editText).setBackgroundColor(AppPreferenceUtil.getEditBgColor());

        title_zone.setBackgroundColor(
                AppPreferenceUtil.getEditHeadBgColor()
        );
        int titleChildCount = title_zone.getChildCount();
        for(int index =0 ;index<titleChildCount;index++){
            title_zone.getChildAt(index).setBackgroundColor(
                    AppPreferenceUtil.getEditHeadBgColor()
            );
        }
    }

    protected long noteTempId = -1;
    protected String noteTempUuid ;
    protected void autoSaveNote(final String text,final String affix, final View editView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String plainText= Html.fromHtml(text).toString();
                if(plainText.trim().replace("\n"," ").isEmpty()){
                    return ;
                }
                String title = titleEditText.getText().toString().trim();
                NoteDatabaseArray noteDatabaseArray = new NoteDatabaseArray("草稿夹", null, null,TextUtils.isEmpty(title)? "自动草稿":
                        "自动草稿-"+title, text, "false", null,
                        null
                );
                if(".md".equals(affix) &&! noteDatabaseArray.Title.endsWith(affix)){
                    noteDatabaseArray.Title +=affix;
                }
                if(AppPreferenceUtil.isShowSaveDraftSnack()){
                    Snackbar.make(editView,"已自动保存草稿",Snackbar.LENGTH_SHORT).setAction(
                            "不再提醒", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AppPreferenceUtil.disableShowSaveDraftSnack();
                                }
                            }
                    ).show();
                }

                if(noteTempId ==-1){
                    noteTempId = NoteController.insertNote(noteDatabaseArray,false);
                    noteTempUuid = NoteController.getNoteUuid(noteTempId);
                }else{
                    noteTempId = NoteController.updateNote(noteTempId,"草稿夹",noteDatabaseArray,noteTempUuid,false);
                    noteTempUuid = NoteController.getNoteUuid(noteTempId);
                }
            }
        }).start();
    }
    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        setMenuResId(R.menu.menu_note_edit);
        HashMap<Integer, String> idMethosNamePaire = new HashMap<Integer, String>();
        idMethosNamePaire.put(android.R.id.home, "save");
        idMethosNamePaire.put(R.id.action_save, "save");
        idMethosNamePaire.put(R.id.action_issue, "issueNote");
        idMethosNamePaire.put(R.id.action_lable,"addLable");
        idMethosNamePaire.put(R.id.action_undo,"undoEdit");
        idMethosNamePaire.put(R.id.action_change_bg_color,"changeBgColor");
        return  idMethosNamePaire;
    }
    protected  Set<String> currentNoteGroupSet;
    public List<String > oldLabels = new ArrayList<>();
    protected void addLable(){
        List<String> groupSet = new ArrayList<>();
        groupSet.addAll(NoteLabelController.getHistoryLabelNames());
        int groupNum = groupSet.size();
        final ArrayList<Boolean> choices = new ArrayList<>();
        if(isNew){
            for(int i = 0;i<groupNum;i++){
                choices.add(false);
            }
        }else{
            currentNoteGroupSet =  NoteLabelController.getLabelList(sdfId);
            oldLabels.addAll(currentNoteGroupSet);
            for (String group :
                    groupSet) {
                if (currentNoteGroupSet.contains(group)){
                    choices.add(true);
                }else{
                    choices.add(false);
                }
            }
        }
        BuildDialog((ArrayList<String>) groupSet, choices);
    }
    private void BuildDialog(final ArrayList<String> groupSet, final ArrayList<Boolean> choices) {
        AlertDialog.Builder lableDislogBuilder = new AlertDialog.Builder(this);
        if(groupSet.size()>0){
            lableDislogBuilder.setMultiChoiceItems(groupSet.toArray(new CharSequence[0]),BooleanArrayCoverter(choices), new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    choices.set(which,isChecked);
                }
            });
        }
        lableDislogBuilder.setTitle("管理标签")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("新建标签", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View edit  = LayoutInflater.from(NoteEditBaseActivity.this).inflate(R.layout.edit,null,false);
                        final EditText editText = (EditText)edit.findViewById(R.id.edit_view);
                        AlertDialog.Builder  lableInputDialog =new AlertDialog.Builder(NoteEditBaseActivity.this);
                        lableInputDialog.setView(edit)
                                .setTitle("新的标签")
                                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String newGroup = editText.getText().toString();
                                        groupSet.add(newGroup);
                                        NoteLabelController.addLabel(newGroup);
                                        choices.add(false);
                                        dialog.dismiss();
                                        BuildDialog(groupSet,choices);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        lableInputDialog.show();
                    }
                })
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setGroupSet(groupSet,choices);
                    }
                });
        lableDislogBuilder.show();
    }
    private void setGroupSet(ArrayList<String> groupSet, ArrayList<Boolean> choices) {
        currentNoteGroupSet = makeSetFromArrayAndBoolean(groupSet,choices);
    }

    private boolean[] BooleanArrayCoverter(ArrayList<Boolean> choices) {
        if(choices.size() ==0){
            return  new boolean[0];
        }
        boolean[] boolSet =new boolean[choices.size()];
        for(int index = 0 ;index<choices.size();index++){
            boolSet[index] = choices.get(index);
        }
        return  boolSet;
    }
    private static HashSet<String> makeSetFromArrayAndBoolean(ArrayList<String> groupSet, ArrayList<Boolean> choices){
        HashSet<String > set = new HashSet<>();
        for(int index = 0;index < groupSet.size();index++){
            if(choices.get(index)){
                set.add(groupSet.get(index));
            }
        }
        return set;
    }
    protected   void save(){
        handler.removeCallbacks(showTextNumTask);
        handler.removeCallbacks(autoSaveTask);
    }
    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }
    public void changeBgColor(){
        //root
        new ColorPanel(this,((ColorDrawable) findView(R.id.note_edit_editText).getBackground()).getColor()).setOnColorChoseCallback(new ColorPanel.OnColorChoseCallback() {
            @Override
            public void onColorChose(@ColorInt int color) {
                AppPreferenceUtil.setEditBgColor(color);
                loadEditBgColor();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(showTextNumTask);
        handler.removeCallbacks(autoSaveTask);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(autoSaveTask,5000);
        handler.postDelayed(showTextNumTask,500);
    }

    protected void handleIniCursor(){

    }


    protected void afterRegisterWidget() {
        title_zone =  $(R.id.title_zone);
        installGroupSwitchButton();
        loadEditBgColor();
        handleIniCursor();
        title_zone.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(AppPreferenceUtil.isShowBackToSaveHelp()){
                    Snackbar.make(title_zone,"按返回键可以自动保存哦",Snackbar.LENGTH_LONG)
                            .setAction("不再提醒", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppPreferenceUtil.dontShowBackToSaveHelp();
                                }
                            })
                            .show();
                }
            }
        },1000);
    }
    private void installGroupSwitchButton() {
        TextView titleText = null;
        int toolChildSize = toolBar.getChildCount();
        for(int index =0;index<toolChildSize;index++){
            View view = toolBar.getChildAt(index);
            if(view instanceof  TextView){
                titleText =(TextView) view;
                break;
            }
        }
        if(titleText !=null){
            titleText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_down_white_18dp,0);

            titleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final List<String> groupList = NoteReelsController.getReels();
                    if(getTitle() !=null){
                        groupList.remove(getTitle());
                    }
                    final String [] items = groupList.toArray(new String[1]);
                    final String[] newGoupName = {items[0]};
                    new AlertDialog.Builder(NoteEditBaseActivity.this)
                            .setTitle("切换分组")
                            .setSingleChoiceItems(items,0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    newGoupName[0] = items[i];

                                }
                            })
                            .setNegativeButton("新建分组", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface outterDialog, int which) {
//                                    final View root = NoteEditBaseActivity.this.getLayoutInflater().inflate(R.layout.edit,null,false);
//                                    final View edit_view = (root.findViewById(R.id.edit_view));
//                                    new AlertDialog.Builder(NoteEditBaseActivity.this)
//                                            .setTitle("新的分组")
//                                            .setView(root)
//                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    dialog.dismiss();
//                                                }
//                                            })
//                                            .setPositiveButton("新建", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    EditText edit = (EditText) edit_view;
//                                                    if(TextUtils.isEmpty(edit.getText().toString().trim())){
//                                                        Snackbar.make(title_zone,"组名不能为空",Snackbar.LENGTH_SHORT).show();
//                                                        return;
//                                                    }
//                                                    ManageNoteCategory.mayAddGroup(edit.getText().toString().trim());
//                                                    dialog.dismiss();
//                                                    groupName = edit.getText().toString().trim();
//                                                    setTitle(groupName);
//                                                    toolBar.setTitle(groupName);
//                                                    outterDialog.dismiss();
//                                                }
//                                            })
//                                            .show();
                                }
                            })
                            .setNeutralButton("设为默认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //旧的笔记发出切换分组消息
                                    if(sdfId !=-1)
                                        NoteController.changeGroup(sdfId,groupName,newGoupName[0]);
                                    groupName = newGoupName[0];
                                    AppPreferenceUtil.setDefaultGroup(groupName);
                                    setTitle(groupName);
                                    toolBar.setTitle(groupName);
                                    dialog.dismiss();
                                    if(AppPreferenceUtil.isShowSetDefaultGroupSnack()){
                                        Snackbar.make(findView(R.id.note_edit_editText),"默认分组为 " +groupName,Snackbar.LENGTH_SHORT)
                                                .setAction("不再显示", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        AppPreferenceUtil.setShowSetDefaultGroupSnack(false);
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //旧的笔记发出切换分组消息
                                    if(sdfId !=-1)
                                        NoteController.changeGroup(sdfId,groupName,newGoupName[0]);
                                    groupName = newGoupName[0];
                                    setTitle(groupName);
                                    toolBar.setTitle(groupName);
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }
    }
    long newId = -1;
    protected boolean saveText(String text,String title){
        return saveText(text,title,"");
    }
    protected boolean saveText(String text,String title,String affix) {
        newId = sdfId;
        String plainText=Html.fromHtml(text).toString();
        if(plainText.trim().replace("\n"," ").isEmpty()){
            return false;
        }
        if(TextUtils.isEmpty(title)){
            if(PreferenceUtils.getPrefBoolean(this,"is_auto_title",true)){
                title = plainText.substring(0,plainText.length()>7?7:plainText.length());
            }else{
                title = "";
            }
        }
        if(!title.endsWith(affix)){
            title+=affix;
        }

        NoteDatabaseArray noteDatabaseArray = new NoteDatabaseArray(groupName, null, null, title, text, "false", null,
                null
        );
        if (plainText.length() > 0) {
            if (isNew) {
                newId = NoteController.insertNote(noteDatabaseArray);
            } else {
                newId = NoteController.updateNote(sdfId,groupName,noteDatabaseArray,uuid);
            }
            NoteLabelController.setNoteLabelList(newId,currentNoteGroupSet,oldLabels);
        } else {
            return false;
        }
        NoteDBHelper.getInstance().deleteNoteById(noteTempId,"草稿夹");
        return true;
    }

    public abstract String getAutoSaveText() ;

    public abstract  String getHtmlTextForTextNum();

    public abstract String getAffix();
}
