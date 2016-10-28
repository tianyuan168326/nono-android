package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.Bean.NoteDateItemArray;

import java.util.List;

/**
 * Created by tianyuan on 16/10/27.
 */
public class RefreshNoteByDateDoneEvent {
    public  List<NoteDateItemArray> array;
    public String date;
    public RefreshNoteByDateDoneEvent(List<NoteDateItemArray> noteDateItemArrays, String date) {
        this.array = noteDateItemArrays;
        this.date = date;
    }
}
