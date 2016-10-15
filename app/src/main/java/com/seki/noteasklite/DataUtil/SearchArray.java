package com.seki.noteasklite.DataUtil;

import com.seki.noteasklite.Fragment.Ask.ContentFragment;

/**
 * Created by 七升 on 2015/8/4.
 */
public class SearchArray {
	public enum ContentCategory
	{
		CONTENT_QUESTION,
		CONTENT_ANSWER;
	}
	public int searchType=1;//content 0  ;  tag 1  ;  user 2
	/**
	 * contentCategory to decide is whether question or answer
	 */
	public String contentTitle,contentDetail;
	public ContentCategory contentCategory;
	public String userName,userAbstract,userHeadImgRUL;

	public String questionRaiserTime,questionInnerCategory,questionOuterCategory;

	public String tag="";
	public String Id;
	public SearchArray(String contentTitle, String contentDetail, ContentCategory contentCategory, String Id){
		this.searchType= ContentFragment.TYPE_CONTENT;
		this.contentTitle=contentTitle;

		this.contentDetail=contentDetail;
		this.contentCategory = contentCategory;
		this.Id = Id;
	}
	public SearchArray(String userName, String userAbstract, String userHeadImgRUL, String userId)
	{
		this.searchType =  ContentFragment.TYPE_USER;
		this.userName = userName;
		this.userAbstract = userAbstract;
		this.userHeadImgRUL = userHeadImgRUL;
		this.Id=userId;
	}
	public SearchArray(String tag){
		this.searchType= ContentFragment.TYPE_TOPIC;
		this.tag=tag;
	}
	public void setQuestionOtherValue(String questionRaiserTime,String questionInnerCategory,String questionOuterCategory)
	{
		this.questionInnerCategory = questionInnerCategory;
		this.questionOuterCategory = questionOuterCategory;
		this.questionRaiserTime =questionRaiserTime;
	}

}
