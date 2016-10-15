package com.seki.noteasklite.Controller;

import com.seki.noteasklite.DBHelpers.NoteRecycleBinHelper;
import com.seki.noteasklite.DataUtil.BusEvent.InsertRecycleBinEvent;
import com.seki.noteasklite.DataUtil.BusEvent.RemoveAllRecycleBinEvent;
import com.seki.noteasklite.DataUtil.BusEvent.RemoveRecycleBinEvent;
import com.seki.noteasklite.DataUtil.BusEvent.RemoveRecycleBinNotes;
import com.seki.noteasklite.DataUtil.BusEvent.RestoreRecycleBinNotes;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yuan on 2016/4/27.
 */
public class RecycleBinController {
    public static void addRecycleBinNote(NoteDatabaseArray array){
        long id  = NoteRecycleBinHelper.getInstance().insertNote(array);
        EventBus.getDefault().post(new InsertRecycleBinEvent(
                array,id));
    }
    //永久删除单个
    public static void removeRecycleBinNote(long id){
         NoteRecycleBinHelper.getInstance().deleteRecycleNoteById(id);
        EventBus.getDefault().post(new RemoveRecycleBinEvent(
                id));
    }
    //恢复单个
    public static void restoreRecycleBinNote(long id){
        NoteAllArray array = NoteRecycleBinHelper.getInstance().getNoteById(id);
        //插入了笔记
        NoteController.insertNote(new NoteDatabaseArray(array));
        NoteRecycleBinHelper.getInstance().deleteRecycleNoteById(id);
        EventBus.getDefault().post(new RemoveRecycleBinEvent(
                id));
    }
    public static void removeAllRecycleBinNote(){
        NoteRecycleBinHelper.getInstance().deleteAllRecycleNote();
        EventBus.getDefault().post(new RemoveAllRecycleBinEvent(
                ));
    }

    public static void restoreAllRecycleBinNote() {
        List<NoteAllArray> recycleBinNoteList= NoteRecycleBinHelper.getInstance().getHistoryNote();
        for (NoteAllArray array:recycleBinNoteList){
            NoteController.insertNote(new NoteDatabaseArray(array));
        }
        NoteRecycleBinHelper.getInstance().deleteAllRecycleNote();
        EventBus.getDefault().post(new RemoveAllRecycleBinEvent(
        ));
    }

    public static void removeRecycleBinNotes(List<Long> recycleNoteIDs) {
        for( long id : recycleNoteIDs   ) {
            NoteRecycleBinHelper.getInstance().deleteRecycleNoteById(id);
        }
        EventBus.getDefault().post(new RemoveRecycleBinNotes(recycleNoteIDs));
    }

    public static void restoreRecycleBinNotes(List<Long> idList) {
        List<NoteAllArray >  noteEntityList  = new ArrayList<>();
        for (Long id:
             idList) {
            NoteAllArray array = NoteRecycleBinHelper.getInstance().getNoteById(id);
            if(array ==null){
                continue;
            }
            long noteNewId =  NoteController.insertNote(new NoteDatabaseArray(array));
            array.sdfId  = noteNewId;
            NoteRecycleBinHelper.getInstance().deleteRecycleNoteById(id);
            noteEntityList.add(array);
        }
        //插入了笔记
        EventBus.getDefault().post(new RestoreRecycleBinNotes(idList));
    }
}
