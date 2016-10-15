package com.seki.noteasklite.DataUtil;

/**
 * Created by 七升 on 2015/7/26.
 */
public class ChatArray {
	public String text;//消息内容包括图片使用html解析
	public Boolean who;//true 自己  false 对方
	public String video="";//视频地址（本地）
	public String time="";//时间

	public ChatArray(String text, Boolean who, String video, String time){
		this.text=text;
		this.who=who;
		this.video=video;
		this.time=time;
	}
}
