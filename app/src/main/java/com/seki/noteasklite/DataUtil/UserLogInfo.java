package com.seki.noteasklite.DataUtil;

import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by yuan on 2015/10/27.
 */
public class UserLogInfo
{
    public UserLogInfo( SimpleDraweeView menuUserHeadimg,
                        TextView userUserName,
                        TextView menuLogOnOrUniversityName,
                        TextView menuSignUpOrSchoolName)
    {
        this. menuUserHeadimg = menuUserHeadimg;
        this. userUserName = userUserName;
        this. menuLogOnOrUniversityName = menuLogOnOrUniversityName;
        this. menuSignUpOrSchoolName = menuSignUpOrSchoolName;
    }
    public SimpleDraweeView menuUserHeadimg;
    public TextView userUserName;
    public TextView menuLogOnOrUniversityName;
    public TextView menuSignUpOrSchoolName;
}
