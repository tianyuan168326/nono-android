package com.seki.noteasklite.DataUtil;

/**
 * Created by 七升 on 2015/9/10.
 */
public class NoteDateItemArray {
	public String title;
	public String detail;
	public String time;
	public String group;
	public long sdfId;
	public String isOnCloud;

	public NoteDateItemArray(String title,String time,String detail,String group,long sdfId, String  isOnCloud){
		this.title=title;
		this.isOnCloud=isOnCloud;
		this.detail=detail;
		this.time=time;
		this.group=group;
		this.sdfId=sdfId;
	}
}
