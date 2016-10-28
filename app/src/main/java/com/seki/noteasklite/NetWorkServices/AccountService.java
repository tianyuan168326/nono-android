package com.seki.noteasklite.NetWorkServices;

import com.seki.noteasklite.DataUtil.Bean.AuthorBean;
import com.seki.noteasklite.DataUtil.Bean.LogOnResponse;
import com.seki.noteasklite.DataUtil.Bean.QQLogOnResponse;
import com.seki.noteasklite.DataUtil.Bean.WonderFull;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by yuan on 2016/5/2.
 */
public interface AccountService {
    @FormUrlEncoded
    @POST("/CommunityAuthorAdmin/GetAuthor.php")
    rx.Observable<AuthorBean> getAllNote(@FieldMap Map<String, String> params
    );
    @FormUrlEncoded
    @POST("/CommunityAuthorAdmin/qqLogOn.php")
    rx.Observable<QQLogOnResponse> qqLogOn(@FieldMap Map<String, String> params
    );

    @FormUrlEncoded
    @POST("logon.php")
    rx.Observable<LogOnResponse> LogOn(@FieldMap Map<String, String> params
    );

    @FormUrlEncoded
    @POST("/quickask_upload_user_info.php")
    rx.Observable<WonderFull> updateUserInfo(@FieldMap Map<String, String> params
    );
}
