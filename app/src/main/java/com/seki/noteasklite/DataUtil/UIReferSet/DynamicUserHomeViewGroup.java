package com.seki.noteasklite.DataUtil.UIReferSet;

import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by yuan on 2015/10/30.
 */
public class DynamicUserHomeViewGroup {
    public SimpleDraweeView activityUserInfoHeadimg;
    public TextView activityUserInfoRealName;
    public TextView activityUserInfoSex;
    public TextView activityUserInfoAbstract;
    public TextView activityUserInfoUniversity;
    public TextView activityUserInfoSchool;
    public TextView activityUserInfoNotice;

    public DynamicUserHomeViewGroup(SimpleDraweeView activityUserInfoHeadimg,
                                    TextView activityUserInfoRealName,
                                    TextView activityUserInfoSex,
                                    TextView activityUserInfoAbstract,
                                    TextView activityUserInfoUniversity,
                                    TextView activityUserInfoSchool,
                                    TextView activityUserInfoNotice) {
        this.activityUserInfoNotice = activityUserInfoNotice;

        this.activityUserInfoSchool = activityUserInfoSchool;
        this.activityUserInfoUniversity = activityUserInfoUniversity;
        this.activityUserInfoAbstract = activityUserInfoAbstract;
        this.activityUserInfoSex = activityUserInfoSex;
        this.activityUserInfoRealName = activityUserInfoRealName;
        this.activityUserInfoHeadimg = activityUserInfoHeadimg;
    }

    public DynamicUserHomeViewGroup() {
    }
}
