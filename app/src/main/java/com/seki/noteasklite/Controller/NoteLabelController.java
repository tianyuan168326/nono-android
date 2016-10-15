package com.seki.noteasklite.Controller;

import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DBHelpers.NoteLabelDBHelper;
import com.seki.noteasklite.DataUtil.Bean.NoteLabelBean;
import com.seki.noteasklite.DataUtil.BusEvent.AddNewLabelEvent;
import com.seki.noteasklite.DataUtil.NoteAllArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Created by yuan on 2016/6/8.
 */
public class NoteLabelController {
    public static List<NoteLabelBean > getHistoryLabels(){
        return NoteLabelDBHelper.getInstance().getHistoryLabels();
    }
    public static List<String > getHistoryLabelNames(){
        return NoteLabelDBHelper.getInstance().getHistoryLabelNames();
    }
    public static Collection<? extends NoteAllArray> getNotesByLabel(String label) {
        List<NoteAllArray> list = new ArrayList<>();
        List<Integer > noteIdList =  NoteLabelDBHelper.getInstance().getNotesByLabel(label);
        for (int noteId:
                noteIdList ) {
            NoteAllArray array = NoteDBHelper.getInstance().getNoteById(noteId);
            if(array !=null){
                list.add(array);
            }
        }
        return list;
    }

    public static Set<String> getLabelList(long sdfId) {
        return NoteLabelDBHelper.getInstance().getGroupList(sdfId);
    }

    public static void addLabel(String newLabel) {
        long id = NoteLabelDBHelper.getInstance().addLabel(newLabel);
        EventBus.getDefault().post(new AddNewLabelEvent(
                new NoteLabelBean(
                id,
                newLabel,
                0,
                String.valueOf(System.currentTimeMillis())
        )));
    }

    public static void setNoteLabelList(long id, Collection<? extends String> currentNoteLabels, List<String> oldLabels) {
        NoteLabelDBHelper.getInstance().setNoteLabelList(id,currentNoteLabels);
        if(oldLabels != null){
            NoteLabelDBHelper.getInstance().labelsRemoveNote(oldLabels);
        }
        if(currentNoteLabels!=null){
            NoteLabelDBHelper.getInstance().labelsAddNote(currentNoteLabels);
        }
    }

}
