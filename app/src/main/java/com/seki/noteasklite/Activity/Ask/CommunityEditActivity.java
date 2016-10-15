package com.seki.noteasklite.Activity.Ask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seki.noteasklite.Activity.UserInfoActivity;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Controller.CommunityController;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.CustomControl.TagSelector.AutoTagCompleteAdapter;
import com.seki.noteasklite.CustomControl.TagSelector.TagAutoCompleteTextView;
import com.seki.noteasklite.DataUtil.Bean.TagSearchResult;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.StaticFinalValue;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.ThirdWrapper.PowerListener;
import com.seki.therichedittext.EditView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityEditActivity extends BaseActivity {
    private String content = "";
    private String itemId;
    EditView editTextWrapper;
    AppCompatEditText noteEditTitle;
    AppCompatEditText titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(StaticFinalValue.STATIC_STRING_NoteTitle);
        content = getIntent().getStringExtra(StaticFinalValue.STATIC_STRING_NoteContent);
        itemId = getIntent().getStringExtra(StaticFinalValue.STATIC_STRING_itemId);
        setContentView(R.layout.activity_note_edit,TextUtils.isEmpty(content)?"分享一些有意义的文字": "编辑条目...");
        noteEditTitle.setText(TextUtils.isEmpty(title) ? "": title);
        editTextWrapper.forceRefocus();
        editTextWrapper.setHtml(TextUtils.isEmpty(content) ? "": content);
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    public static void start(Context context,  String Title, String content,String itemId){
        context.startActivity(new Intent(context, CommunityEditActivity.class)
                .putExtra(StaticFinalValue.STATIC_STRING_NoteTitle, Title)
                .putExtra(StaticFinalValue.STATIC_STRING_NoteContent, content)
                .putExtra(StaticFinalValue.STATIC_STRING_itemId, itemId)
        );
    }
    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        setMenuResId(R.menu.menu_note_edit_ask);
        HashMap<Integer, String> idMethosNamePaire = new HashMap<Integer, String>();
        idMethosNamePaire.put(R.id.action_issue, "issueNote");
        return idMethosNamePaire;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void issueNote() {
        buildCategoryDialog();
    }

    private void buildCategoryDialog() {
        if(TextUtils.isEmpty(titleEditText.getText().toString()) ){
            Snackbar.make($(R.id.note_edit_editText),"添加标题吧",Snackbar.LENGTH_SHORT).show();
        }
        View categoryDialog = getLayoutInflater().from(this).inflate(R.layout.layout_category_list, null);
        final LinearLayout tagSetLinearLayout = (LinearLayout) categoryDialog.findViewById(R.id.tag_set);
        final TagAutoCompleteTextView tagAutoCompleteTextView = (TagAutoCompleteTextView) categoryDialog.findViewById(R.id.tag_search_view);
        final List<String> tagSetFromServer = new ArrayList<>();
        final List<String> tags = new ArrayList<>();
        final AutoTagCompleteAdapter<String> tagAdapter = new AutoTagCompleteAdapter<String>(CommunityEditActivity.this, tagSetFromServer);
        //final ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(categoryDialog.getContext(),android.R.layout.simple_dropdown_item_1line,new String[]{"ddsdsd"});
        tagAutoCompleteTextView.setAdapter(tagAdapter);
        tagAutoCompleteTextView.setThreshold(1);
        tagAdapter.notifyDataSetChanged();
        tagAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //wait to limit the lenth to trigger the communication
                tagSetFromServer.add("loading...");
                tagAdapter.notifyDataSetChanged();
                final String tagKeyWordWrapper = s.toString();
                MyApp.getInstance().volleyRequestQueue.add(new StringRequest(Request.Method.POST, "http://2.diandianapp.sinaapp.com/NONo/search_tag.php"
                        , new PowerListener(){
                    @Override
                    public void onCorrectResponse(String s) {
                        super.onCorrectResponse(s);
                        final TagSearchResult tagSearchResult = new Gson().fromJson(s, new TypeToken<TagSearchResult>() {
                        }.getType());
                        if (tagSearchResult.state_code < 0) {
                            tagSetFromServer.clear();
                            tagSetFromServer.add("暂时没有相关Tag");
                            tagSetFromServer.add("+添加新的Tag");
                            tagAdapter.notifyDataSetChanged();
                            tagAutoCompleteTextView.setOnItemSelectedListener(new TagAutoCompleteTextView.OnItemSelectedListener() {
                                @Override
                                public void onSelect(CharSequence s) {
                                    switch (s.toString()) {
                                        case "+添加新的Tag":
                                            //post the tag to server
                                            MyApp.getInstance().volleyRequestQueue.add(new StringRequest(Request.Method.POST,
                                                    "http://2.diandianapp.sinaapp.com/NONo/add_tag.php"
                                                    , new Response.Listener<String>() {

                                                @Override
                                                public void onResponse(String s) {

                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {

                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    params.put("tag", tagKeyWordWrapper);
                                                    return params;
                                                }
                                            });
                                            //add the tag to view
                                            LinearLayout tagText = (LinearLayout) tagSetLinearLayout.inflate(CommunityEditActivity.this, R.layout.layout_tag_view, tagSetLinearLayout);
                                            ((TextView) tagText.findViewById(R.id.text)).setText(tagKeyWordWrapper);
                                            tags.add(tagKeyWordWrapper);
                                            for (int i = 0; i < tagSetLinearLayout.getChildCount(); i++) {
                                                ((TextView) tagSetLinearLayout.getChildAt(i).findViewById(R.id.text))
                                                        .setText(tags.get(i));
                                            }
                                            break;
                                    }
                                }
                            });
                        } else if (tagSearchResult.state_code == 0) {
                            tagSetFromServer.clear();
                            for (String tag :
                                    tagSearchResult.tag_list) {
                                tagSetFromServer.add(tag);
                            }
                            tagSetFromServer.add("+添加新的Tag");
                            tagAdapter.notifyDataSetChanged();
                            tagAutoCompleteTextView.setOnItemSelectedListener(new TagAutoCompleteTextView.OnItemSelectedListener() {
                                @Override
                                public void onSelect(CharSequence s) {
                                    switch (s.toString()) {
                                        case "+添加新的Tag":
                                            //post the tag to server
                                            MyApp.getInstance().volleyRequestQueue.add(new StringRequest(Request.Method.POST,
                                                    "http://2.diandianapp.sinaapp.com/NONo/add_tag.php"
                                                    , new Response.Listener<String>() {

                                                @Override
                                                public void onResponse(String s) {

                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {

                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    params.put("tag", tagKeyWordWrapper);
                                                    return params;
                                                }
                                            });
                                            //add the tag to view
                                            LinearLayout tagText = (LinearLayout) (tagSetLinearLayout.inflate(CommunityEditActivity.this, R.layout.layout_tag_view, tagSetLinearLayout));
                                            tags.add(tagKeyWordWrapper);
                                            ((TextView) (tagText.findViewById(R.id.text))).setText(tagKeyWordWrapper);
                                            for (int i = 0; i < tagSetLinearLayout.getChildCount(); i++) {
                                                ((TextView) tagSetLinearLayout.getChildAt(i).findViewById(R.id.text))
                                                        .setText(tags.get(i));
                                            }
                                            break;
                                        default:
                                            LinearLayout tagText1 = (LinearLayout) (tagSetLinearLayout.inflate(CommunityEditActivity.this, R.layout.layout_tag_view, tagSetLinearLayout));
                                            tags.add(s.toString());
                                            ((TextView) (tagText1.findViewById(R.id.text))).setText(s.toString());
                                            for (int i = 0; i < tagSetLinearLayout.getChildCount(); i++) {
                                                ((TextView) tagSetLinearLayout.getChildAt(i).findViewById(R.id.text))
                                                        .setText(tags.get(i));
                                            }
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onJSONStringParseError() {
                        super.onJSONStringParseError();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("key", tagKeyWordWrapper);
                        return params;
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择分类");
        builder.setView(categoryDialog);
        builder.setPositiveButton("发布", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(UserInfoActivity.verifyState(CommunityEditActivity.this))
                    return ;
                content = editTextWrapper.getHtmlText();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        content  =  NoteController.preProcessNoteContent(content);
                        postNote(itemId, titleEditText.getText().toString(),content,new Gson().toJson(tags));
                    }
                }).start();
            }
        });
        builder.show();
    }

    private void postNote(final String itemId,final String postTitle,final String postContent ,final String tagsString) {

        if(TextUtils.isEmpty(itemId)){
            NoteController.shareNote(new NoteAllArray(postTitle,postContent),tagsString);
        }else{
            CommunityController.editPost(itemId, postTitle, postContent, tagsString);
        }
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
    @Override
    protected void registerWidget() {
        noteEditTitle = $(R.id.note_edit_title);
        editTextWrapper =$(R.id.note_edit_editText);
        titleEditText = (AppCompatEditText) findViewById(R.id.note_edit_title);
        editTextWrapper.setFocusable(true);
        editTextWrapper.setFocusableInTouchMode(true);
        editTextWrapper.requestFocus();
    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    public void onClick(View view) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        editTextWrapper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
