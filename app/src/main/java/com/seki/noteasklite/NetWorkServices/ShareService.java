package com.seki.noteasklite.NetWorkServices;

import com.seki.noteasklite.DataUtil.Bean.ShareNoteResultBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by yuan on 2016/6/16.
 */
public interface ShareService {
    @FormUrlEncoded
    @POST("/Share/getShareNoteLink.php")
    rx.Observable<ShareNoteResultBean> getShareLink(@FieldMap Map<String, String> params);
}
