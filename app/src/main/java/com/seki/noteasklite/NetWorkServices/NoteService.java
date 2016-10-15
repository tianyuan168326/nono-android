package com.seki.noteasklite.NetWorkServices;

import com.seki.noteasklite.DataUtil.Bean.AllNoteListBean;
import com.seki.noteasklite.DataUtil.Bean.WonderFull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by yuan-tian01 on 2016/3/30.
 */
public interface NoteService {
    @FormUrlEncoded
    @POST("/note/getAllNote.php")
    rx.Observable<AllNoteListBean> getAllNote(@FieldMap Map<String, String> params );

    @FormUrlEncoded
    @POST("/note/deleteNote.php")
    rx.Observable<WonderFull> deleteNote(@FieldMap Map<String, String> params
    );

    @FormUrlEncoded
    @POST("/note/deleteNotes.php")
    rx.Observable<WonderFull> deleteNotes(@FieldMap HashMap<String, String> params);

}
