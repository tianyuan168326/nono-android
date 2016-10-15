package com.seki.noteasklite.DataUtil;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.facebook.common.util.UriUtil;
import com.seki.noteasklite.R;

import java.io.File;

/**
 * Created by 七升 on 2015/9/10.
 */
public class NoteReelArray implements Parcelable {

	public int id;
	public String reel_title;
	public String reel_abstract;
	public String reel_create_time;
	public int reel_note_num;
	public String reel_title_pic;

	public NoteReelArray(int id,String reel_title, String reel_abstract, String reel_create_time, int reel_note_num, String reel_title_pic) {
		this.id = id;
		if(TextUtils.isEmpty(reel_create_time)){
			reel_create_time = String.valueOf(System.currentTimeMillis());
		}
		if(TextUtils.isEmpty(reel_title_pic)){
			reel_title_pic = "";
		}
		this.reel_title = reel_title;
		this.reel_abstract = reel_abstract;
		this.reel_create_time = reel_create_time;
		this.reel_note_num = reel_note_num;
		this.reel_title_pic = reel_title_pic;
	}
	public NoteReelArray(String reel_title, String reel_abstract, String reel_create_time, int reel_note_num, String reel_title_pic) {
		this.id = -1;
		if(TextUtils.isEmpty(reel_create_time)){
			reel_create_time = String.valueOf(System.currentTimeMillis());
		}
		if(TextUtils.isEmpty(reel_title_pic)){
			reel_title_pic = "";
		}
		this.reel_title = reel_title;
		this.reel_abstract = reel_abstract;
		this.reel_create_time = reel_create_time;
		this.reel_note_num = reel_note_num;
		this.reel_title_pic = reel_title_pic;
	}
	public static String getUriString(String reel_title_pic){
		int [] bannerResArray = new int[]{
				R.drawable.banner,
				R.drawable.banner3
		};
		Uri uri = new Uri.Builder()
				.scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
				.path(String.valueOf(
						bannerResArray[(int)(Math.random()*2)]
						//	R.drawable.banner
				))
				.build();
		String uriString;
		uriString = uri.toString();
		if(!reel_title_pic.endsWith("http") &&!TextUtils.isEmpty(reel_title_pic)){
			uriString  = Uri.fromFile(new File(reel_title_pic)).toString();
		}
		return uriString;
	}
	public String getUriString(){
		int [] bannerResArray = new int[]{
				R.drawable.banner,
				R.drawable.banner3
		};
		Uri uri = new Uri.Builder()
				.scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
				.path(String.valueOf(
						bannerResArray[(int)(id%2)]
					//	R.drawable.banner
				))
				.build();
		String uriString;
		uriString = uri.toString();
		if(reel_title_pic.startsWith("http") ){
			uriString = reel_title_pic;
		}else if(!TextUtils.isEmpty(reel_title_pic)){
			uriString  = Uri.fromFile(new File(reel_title_pic)).toString();
		}
		return uriString;
	}
	public NoteReelArray(String reel_title, String reel_abstract, String reel_title_pic) {
		this.id = -1;
			reel_create_time = String.valueOf(System.currentTimeMillis());
		if(TextUtils.isEmpty(reel_title_pic)){
			reel_title_pic = "";
		}
		this.reel_note_num = -1;
		this.reel_title = reel_title;
		this.reel_abstract = reel_abstract;
		this.reel_title_pic = reel_title_pic;
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.reel_title);
		dest.writeString(this.reel_abstract);
		dest.writeString(this.reel_create_time);
		dest.writeInt(this.reel_note_num);
		dest.writeString(this.reel_title_pic);
	}

	protected NoteReelArray(Parcel in) {
		this.id = in.readInt();
		this.reel_title = in.readString();
		this.reel_abstract = in.readString();
		this.reel_create_time = in.readString();
		this.reel_note_num = in.readInt();
		this.reel_title_pic = in.readString();
	}

	public static final Parcelable.Creator<NoteReelArray> CREATOR = new Parcelable.Creator<NoteReelArray>() {
		@Override
		public NoteReelArray createFromParcel(Parcel source) {
			return new NoteReelArray(source);
		}

		@Override
		public NoteReelArray[] newArray(int size) {
			return new NoteReelArray[size];
		}
	};
}
