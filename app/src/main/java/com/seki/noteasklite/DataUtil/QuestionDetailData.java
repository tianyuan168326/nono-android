package com.seki.noteasklite.DataUtil;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by yuan on 2015/9/6.
 */
public class QuestionDetailData {
    private static LinkedList<QuestionDetailData> container = new LinkedList<>();
    private static int validNum = 0;
    private static ArrayList<Integer> validPosition = new ArrayList<>(10);

    private int indexInContainer;
    public String questionId;
    public String questionHotDegree;
    public String questionVoteType;
    public String questionRaiserName;
    public String questionTitle;
    public String questionRaiseTime;
    public String questionRaiserCollege;
    public String questionRaiserSchool;
    public String questionDetail;
    public String questionOuterCategory;
    public String questionInnerCategory;
    public String questionAnswerNum;
    public String questionNoticeType;
    public String questionNoticedNum;
    public String questionRaiserId;
    public String questionRaiserTime;
    public String questionRaiserHeadImage;
    public static QuestionDetailData obtain(){
        if(validNum ==0){
            QuestionDetailData newInstance = new QuestionDetailData();
            container.add(newInstance);
            newInstance.indexInContainer = 0;
            return newInstance;
        }else{
            int indexInContainer = validPosition.get(validPosition.size()-1);
            QuestionDetailData oldInstance= container.get(indexInContainer);
            oldInstance.indexInContainer = indexInContainer;
            return oldInstance;
        }
    }
    public void recycle(){
        validNum++;
        validPosition.add(container.indexOf(this));
    }
}
