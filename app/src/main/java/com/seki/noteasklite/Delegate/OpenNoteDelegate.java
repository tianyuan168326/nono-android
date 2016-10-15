package com.seki.noteasklite.Delegate;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.seki.noteasklite.Activity.Note.MarkDownNoteDetailActivity;
import com.seki.noteasklite.Activity.Note.NoteDetailActivity;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.Util.ColorUtils;

/**
 * Created by yuan on 2016/5/25.
 */
public class OpenNoteDelegate {
    public static Intent start(Context context, NoteAllArray currentNoteInfo, int indexInList){
        Intent i;
        if(currentNoteInfo.title.endsWith(".md")){
            i = MarkDownNoteDetailActivity.start(context,currentNoteInfo);
        }else{
            i = NoteDetailActivity.start(context, currentNoteInfo, indexInList);
        }
        return i;
    }
    public static Intent start(Context context, NoteAllArray currentNoteInfo, String k){
        Intent i;
        if(currentNoteInfo.title.endsWith(".md")){
            i = MarkDownNoteDetailActivity.start(context,currentNoteInfo,k);
        }else{
            i = NoteDetailActivity.start(context, currentNoteInfo, k);
        }
        return i;
    }
    public static Intent start(Context context,NoteAllArray currentNoteInfo){
        Intent i;
        if(currentNoteInfo.title.endsWith(".md")){
            i = MarkDownNoteDetailActivity.start(context,currentNoteInfo);
        }else{
            i = NoteDetailActivity.start(context, currentNoteInfo);
        }
        return i;
    }
    public static class NoteUIControl implements Parcelable {
       public  int toolBarColor;
        public  int statusBarColor = -1;
        public NoteUIControl(int color) {
            if(color == -1){
                color = ThemeController.getCurrentColor().getMainColor();
            }
            this.toolBarColor = color;
        }

        public int getToolBarColor() {
            return toolBarColor;
        }

        public int getStatusBarColor() {
            if(statusBarColor == -1){
                return ColorUtils.getDarkerColor(toolBarColor,0.1f);
            }
            return statusBarColor;
        }

        public NoteUIControl(int toolBarColor, int statusBarColor) {
            this.toolBarColor = toolBarColor;
            this.statusBarColor = statusBarColor;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.toolBarColor);
            dest.writeInt(this.statusBarColor);
        }

        protected NoteUIControl(Parcel in) {
            this.toolBarColor = in.readInt();
            this.statusBarColor = in.readInt();
        }

        public static final Creator<NoteUIControl> CREATOR = new Creator<NoteUIControl>() {
            @Override
            public NoteUIControl createFromParcel(Parcel source) {
                return new NoteUIControl(source);
            }

            @Override
            public NoteUIControl[] newArray(int size) {
                return new NoteUIControl[size];
            }
        };
    }
    public static Intent start(Context context,NoteAllArray currentNoteInfo,NoteUIControl ui){
        Intent i;
        if(currentNoteInfo.title.endsWith(".md")){
            i = MarkDownNoteDetailActivity.start(context,currentNoteInfo, ui);
        }else{
            i = NoteDetailActivity.start(context, currentNoteInfo, ui);
        }
        return i;
    }
}
