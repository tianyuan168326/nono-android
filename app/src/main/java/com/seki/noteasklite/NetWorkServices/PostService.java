package com.seki.noteasklite.NetWorkServices;

import com.seki.noteasklite.DataUtil.Bean.AllNoteListBean;
import com.seki.noteasklite.DataUtil.Bean.AuthorBean;
import com.seki.noteasklite.DataUtil.Bean.WonderFull;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by yuan on 2016/5/2.
 */
public interface PostService {
    @FormUrlEncoded
    @POST("/CommunityItemAdmin/delPost.php")
    rx.Observable<AuthorBean> removePost(@FieldMap Map<String, String> params
    );
    @FormUrlEncoded
    @POST("/CommunityItemAdmin/editPostContent.php")
    rx.Observable<AuthorBean> editPost(@FieldMap Map<String, String> params
    );
    @FormUrlEncoded
    @POST("/CommunityItemAdmin/changePostTags.php")
    rx.Observable<WonderFull> changePostTags(@FieldMap Map<String, String> params
    );
    @FormUrlEncoded
    @POST("/add_key_html.php")
    rx.Observable<WonderFull> newPost(@FieldMap Map<String, String> params
    );
}
