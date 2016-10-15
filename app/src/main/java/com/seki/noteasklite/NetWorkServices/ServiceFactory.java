package com.seki.noteasklite.NetWorkServices;

import com.seki.noteasklite.RetrofitHelper.RequestBody.RetrofitWrapper;

/**
 * Created by yuan-tian01 on 2016/4/1.
 */
public class ServiceFactory {
    public static NoteService getNoteService(){
          return RetrofitWrapper.getRetrofit().create(NoteService.class);
    }
    public static PostService getPostService(){
        return RetrofitWrapper.getRetrofit().create(PostService.class);
    }
    public static AccountService getAccountService(){
        return RetrofitWrapper.getRetrofit().create(AccountService.class);
    }

    public static ShareService getShareService(){
        return RetrofitWrapper.getRetrofit().create(ShareService.class);
    }

}
