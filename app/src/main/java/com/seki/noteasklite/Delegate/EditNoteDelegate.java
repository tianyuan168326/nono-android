package com.seki.noteasklite.Delegate;

import android.content.Context;
import android.content.Intent;

import com.seki.noteasklite.Activity.Note.MarkdownNoteEditActivity;
import com.seki.noteasklite.Activity.Note.NoteEditActivity;
import com.seki.noteasklite.DataUtil.NoteAllArray;

/**
 * Created by yuan on 2016/5/25.
 */
public class EditNoteDelegate {
    public static  final int MD_MODE = 0x01;
    public static  final int RICH_MODE = 0x02;
    public static Intent start(Context context,int mode){
        Intent intent;
        switch (mode){
            case MD_MODE:
                intent = MarkdownNoteEditActivity.editNote(context);
                break;
            case RICH_MODE:
                intent = NoteEditActivity.start(context);
                break;
            default:
                intent = null;
                break;
        }
        return  intent;
    }
    public static Intent start(Context context,NoteAllArray currentNoteInfo){
        Intent i;
        if(currentNoteInfo.title.endsWith(".md")){
            i = MarkdownNoteEditActivity.editNote(context,currentNoteInfo);
        }else{
            i = NoteEditActivity.start(context, currentNoteInfo);
        }
        return i;
    }

    public static Intent start(Context context,NoteAllArray currentNoteInfo,int index,int sel){
        Intent i;
        if(currentNoteInfo.title.endsWith(".md")){
            i = MarkdownNoteEditActivity.editNote(context,currentNoteInfo);
        }else{
            i = NoteEditActivity.start(context, currentNoteInfo,index,sel);
        }
        return i;
    }
}
