package com.seki.noteasklite.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.SearchArray;
import com.seki.noteasklite.DataUtil.SubjectArray;
import com.seki.noteasklite.Fragment.Ask.ContentFragment;
import com.seki.noteasklite.MyApp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by yuan on 2015/8/7.
 */

public class SearchTask extends AsyncTask<String,Void,Void> {
    public static final int TYPE_NOTE = 0;
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_TOPIC = 2;
    public static final int TYPE_USER = 3;

    @Override
    protected Void doInBackground(String... params) {
        switch (Integer.valueOf(params[1])){
            case TYPE_NOTE:
                searchNote(params[0]);
                break;
            case TYPE_CONTENT:
                searchContent(params[0]);
                break;
            case TYPE_TOPIC:
                searchTopic(params[0]);
                break;
            case TYPE_USER:
                searchUser(params[0]);
                break;
        }
        return null;
    }

    private void searchUser(String param) {
        searchContent(param);
    }

    private void searchTopic(String param) {
        searchContent(param);
    }

    private void searchContent(String param) {
        try{
            HttpClient client = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            HttpPost post = new HttpPost("http://diandianapp.sinaapp.com/quick_ask_search.php");
            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
            paramsList.add(new BasicNameValuePair("key_word", param));
            post.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                builder.append(s);
            }
            JSONObject jsonObject = new JSONObject(builder.toString());
            contentList.clear();
            userList.clear();
            try
            {
                JSONArray keyJSONObjectArray = jsonObject.getJSONArray("key_search_result");
                if(keyJSONObjectArray!=null){
                    int keyNb = keyJSONObjectArray.length();
                    for(int keyIndex =0;keyIndex <keyNb ;keyIndex++)
                    {
                        JSONObject keyJSONObject = keyJSONObjectArray.getJSONObject(keyIndex);
                        SearchArray keySearchResult = new SearchArray(
                                keyJSONObject.getString("key_detail"),"", SearchArray.ContentCategory.CONTENT_ANSWER,keyJSONObject.getString("key_id"));
                        contentList.add(keySearchResult);                }
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            try {
                JSONArray questionJSONObjectArray = jsonObject.getJSONArray("question_search_result");
                if(questionJSONObjectArray !=null){
                    int questionNb = questionJSONObjectArray.length();
                    for (int keyIndex = 0; keyIndex < questionNb; keyIndex++) {
                        JSONObject questionJSONObject = questionJSONObjectArray.getJSONObject(keyIndex);
                        SearchArray keySearchResult = new SearchArray(
                                questionJSONObject.getString("question_title"),
                                questionJSONObject.getString("question_detail"),
                                SearchArray.ContentCategory.CONTENT_QUESTION,
                                questionJSONObject.getString("question_id"));
                        keySearchResult.setQuestionOtherValue(questionJSONObject.getString("question_raise_time"),
                                questionJSONObject.getString("question_inner_category"),questionJSONObject.getString("question_outer_category"));
                        contentList.add(keySearchResult);
                    }
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                JSONArray userInfoJSONObjectArray = jsonObject.getJSONArray("user_info_search_result");
                if(userInfoJSONObjectArray != null){
                    int questionNb = userInfoJSONObjectArray.length();
                    for (int keyIndex = 0; keyIndex < questionNb; keyIndex++) {
                        JSONObject keyJSONObject = userInfoJSONObjectArray.getJSONObject(keyIndex);
                        SearchArray keySearchResult = new SearchArray(
                                keyJSONObject.getString("userRealname"),
                                keyJSONObject.getString("user_info"),
                                keyJSONObject.getString("user_headpic"),
                                keyJSONObject.getString("userId"));
                        userList.add(keySearchResult);
                    }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        tagList.clear();
        List<SubjectArray> outerSubjectList=new ArrayList<>();
        Set<String> outerCategoryKeys = MyApp.getInstance().subjectCategory.keySet();
        for (String key : outerCategoryKeys) {
            outerSubjectList.add(new SubjectArray(key));
        }
        for(int i=0;i<outerSubjectList.size();i++) {
            boolean isOuter=false;
            String tempouterSubject=outerSubjectList.get(i).getSubjectName();
            if(param.contains(tempouterSubject)){
                tagList.add(new SearchArray(tempouterSubject));
                isOuter=true;
            }else if(tempouterSubject.contains(param)){
                isOuter=true;
                tagList.add(new SearchArray(tempouterSubject));
            }
            List<String> tempInnerSubjectList =
                    MyApp.getInstance().subjectCategory.get(tempouterSubject);
            //List<SubjectArray> innerSubjectList = new ArrayList<SubjectArray>();
            for (String s : tempInnerSubjectList) {
                if(isOuter){
                    tagList.add(new SearchArray(tempouterSubject+" - "+s));
                    continue;
                }
                if(s.contains(param)){
                    tagList.add(new SearchArray(tempouterSubject+" - "+s));
                }else if(param.contains(s)){
                    tagList.add(new SearchArray(tempouterSubject+" - "+s));
                }
            }
        }
    }

    private void searchNote(String param) {
        NoteDBHelper.getInstance().searchNote(param);

    }

    private Context searchActivity;
    List<NoteAllArray> noteList;
    private List<SearchArray> contentList;
    private List<SearchArray> tagList;
    private List<SearchArray> userList;
    private ArrayList<Fragment> fragments;
    public SearchTask(Context searchActivity, List<NoteAllArray> noteList, List<SearchArray> contentList,
                      List<SearchArray> tagList, List<SearchArray> userList, ArrayList<Fragment> fragments) {
        this.searchActivity = searchActivity;
        this.contentList = contentList;
        this.tagList = tagList;
        this.userList = userList;
        this.fragments = fragments;
        this.noteList = noteList;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        ((ContentFragment)(fragments.get(0))).changeNoteSearchList(noteList);
//        ((ContentFragment)(fragments.get(1))).changeNormalSearchList(contentList);
//        ((ContentFragment)(fragments.get(2))).changeNormalSearchList(tagList);
//        ((ContentFragment)(fragments.get(3))).changeNormalSearchList(userList);
    }
}
