package com.seki.noteasklite.RetrofitHelper.RequestBody;

import com.seki.noteasklite.MyApp;

/**
 * Created by yuan-tian01 on 2016/3/30.
 */
public class GetNoteBody extends AuthBody {
    public String user_id;
    public GetNoteBody(){
        super();
        user_id = MyApp.getInstance().userInfo.userId;
    }
}
