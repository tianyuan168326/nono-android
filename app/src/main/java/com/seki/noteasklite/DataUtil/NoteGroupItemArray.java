package com.seki.noteasklite.DataUtil;

/**
 * Created by 七升 on 2015/9/10.
 */
public class NoteGroupItemArray {
	public String title;
	public String detail;
	public String time;
	public long sdfId;
	public String isOnCloud;

	public NoteGroupItemArray(String title,String time,String detail,long sdfId,String isOnCloud){
		this.title=title;
		this.isOnCloud=isOnCloud;
		this.detail=detail;
		this.time=time;
		this.sdfId=sdfId;
	}
}
