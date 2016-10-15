package com.seki.noteasklite.DataUtil;

/**
 * Created by 七升 on 2015/9/13.
 */
public  class NoteDatabaseArray {
	public static  final String  MARK_FIRST_IN = "MARK_FIRST_IN";
	public static  final String  MARK_FIRST_IN2 = "MARK_FIRST_IN2";
	public String group="";
	public String date="";
	public String time="";
	public String Title = "";
	public String content="";
	public String is_on_cloud = "";
	public String notifyTime="";
	public String uuid="";
	public NoteDatabaseArray(String group,String date,String time,String Title,String content,String is_on_cloud,String notifyTime,String uuid){
		this.group=group;
		this.date=date;
		this.Title = Title;
		this.content=content;
		this.time=time;
		this.is_on_cloud = is_on_cloud;
		this.notifyTime=notifyTime;
		this.uuid = uuid;
	}
//	public NoteDatabaseArray(String group,String date,String time,String Title,String content,String is_on_cloud,String notifyTime,String uuid){
//		this.group=group;
//		this.date=date;
//		this.Title = Title;
//		this.content=content;
//		this.time=time;
//		this.is_on_cloud = is_on_cloud;
//		this.notifyTime=notifyTime;
//	}
	public NoteDatabaseArray(NoteAllArray noteAllArray){
		this.content =noteAllArray.content;
		this.date = noteAllArray.date;
		this.group = noteAllArray.group;
		this.is_on_cloud = noteAllArray.isOnCloud;
		this.time = noteAllArray.time;
		this.Title = noteAllArray.title;
		this.uuid = noteAllArray.uuid;
	}
}
