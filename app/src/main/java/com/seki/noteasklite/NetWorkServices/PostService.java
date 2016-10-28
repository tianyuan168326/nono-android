package com.seki.noteasklite.NetWorkServices;

import com.seki.noteasklite.DataUtil.Bean.AllCommentListBean;
import com.seki.noteasklite.DataUtil.Bean.AllNoteListBean;
import com.seki.noteasklite.DataUtil.Bean.AuthorBean;
import com.seki.noteasklite.DataUtil.Bean.OldNotificationsBean;
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
    @FormUrlEncoded
    @POST("/quickask_answer_add_comment.php")
    rx.Observable<WonderFull> newReplyComment(@FieldMap Map<String, String> params
    );

    @FormUrlEncoded
    @POST("/quickask_get_answercomment.php")
    rx.Observable<AllCommentListBean> getReplyCommentAll(@FieldMap Map<String, String> params
    );

    @FormUrlEncoded
    @POST("/Notice/quickask_notice.php")
    rx.Observable<WonderFull> noticeOther(@FieldMap Map<String, String> params
    );

    @FormUrlEncoded
    @POST("/quickask_old_notifications.php")
    rx.Observable<OldNotificationsBean> getOldNotifications(@FieldMap Map<String, String> params
    );

    @FormUrlEncoded
    @POST("/quickask_vote_for_answer.php")
    rx.Observable<WonderFull> voteForReply(@FieldMap Map<String, String> params
    );

}
