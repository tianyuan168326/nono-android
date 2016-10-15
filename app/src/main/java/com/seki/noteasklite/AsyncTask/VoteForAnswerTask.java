package com.seki.noteasklite.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.ThirdWrapper.PowerListener;
import com.seki.noteasklite.ThirdWrapper.PowerStringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan on 2015/8/25.
 */
public class VoteForAnswerTask {
    String key_id;
    String vote_type;
    String preVoteType;
    PowerStringRequest  voteRequest;
    public VoteForAnswerTask(final String key_id, final String vote_type, final String preVoteType) {
        this.key_id = key_id;
        this.vote_type = vote_type;
        this.preVoteType = preVoteType;
        voteRequest = new PowerStringRequest(
                Request.Method.POST,
                NONoConfig.makeNONoSonURL("/quickask_vote_for_answer.php"),
                new PowerListener() {
                    @Override
                    public void onCorrectResponse(String s) {
                        super.onCorrectResponse(s);

                    }

                    @Override
                    public void onJSONStringParseError() {
                        super.onJSONStringParseError();
                    }

                    @Override
                    public void onResponse(String s) {
                        super.onResponse(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("answer_id",key_id);
                params.put("vote_type",vote_type);
                params.put("pre_vote_type",preVoteType);
                params.put("user_name", MyApp.getInstance().userInfo.username);
                params.putAll(super.getParams());
                return params;
            }
        };
    }
    public PowerStringRequest getRequest(){
        return this.voteRequest;
    }
}
