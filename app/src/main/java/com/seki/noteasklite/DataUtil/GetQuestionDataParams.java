package com.seki.noteasklite.DataUtil;

import java.util.Observable;

/**
 * Created by yuan on 2015/9/2.
 */
public class GetQuestionDataParams  {
    public boolean isRefresh;
    public int maxQuestionId;
    public int stateCode;
    public GetQuestionDataParams()
    {
        super();
        isRefresh = true;
        maxQuestionId = 0;
        stateCode = -1;
    }
}
