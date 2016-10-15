package com.seki.noteasklite.DataUtil;

/**
 * Created by 七升 on 2015/9/10.
 */
public class NoteSimpleArray {
	public String title;
	public String newestNote;
	public int counts;
	public NoteSimpleArray(String title, String newestNote, int counts){
		this.counts=counts;
		this.newestNote=newestNote;
		this.title=title;
	};
}
