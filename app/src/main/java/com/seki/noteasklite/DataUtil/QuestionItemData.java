package com.seki.noteasklite.DataUtil;

/**
 * Created by yuan on 2015/9/2.
 */
public class QuestionItemData {
    public interface  QuestionItemType
    {
        public int TYPE_FETCHING = 0 ;
        public int TYPE_NORMAL = 1;
    }
    public QuestionItemData setQuestionItemType(int questionItemType)
    {
        this.questionItemType = questionItemType;
        return this;
    }
    public int questionItemType ;
    public String questionItemId ;
    public String questionRaiserId ;
    public String questionItemTitle;
    public String questionItemDetail;
    public String questionRaiserUniversity;
    public String questionRaiserSchool;
    public String questionRaiserRealname;
    public String questionRaiserHeadImg;
    public String questionRaiseTime;
    public String questionOuterCategory;
    public String questionInnerCategory;
    public String questionAnswerNum;
    public String questionHotDegree;
    public String questionAbstract;
    public String questionTagInJson;
}
