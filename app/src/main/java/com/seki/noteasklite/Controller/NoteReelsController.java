package com.seki.noteasklite.Controller;

import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DBHelpers.NoteReelsDBHelper;
import com.seki.noteasklite.DataUtil.BusEvent.AddReelEvent;
import com.seki.noteasklite.DataUtil.BusEvent.UpdateReelEvent;
import com.seki.noteasklite.DataUtil.NoteReelArray;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yuan on 2016/6/4.
 */
public class NoteReelsController {
    private static List<NoteReelArray> reelArray;

    public static void deleteReels(String[] strings) {
        NoteReelsDBHelper.getInstance().deleteReels(strings);
        NoteDBHelper.getInstance().deleteNoteByReels(strings);
    }

    public static void reelAddNote(String groupName, int noteNum) {
        NoteReelsDBHelper.getInstance().reelAddNote(groupName,noteNum);
    }
    public static List<String> getReels() {
        return NoteReelsDBHelper.getInstance().getReels();
    }

    public static void reelDeleteNote(String groupName, int noteNum) {
        NoteReelsDBHelper.getInstance().reelDeleteNote(groupName,noteNum);
    }

    public static void reelNoteExchange(String oldReel, String currentReel, int i) {
        NoteReelsDBHelper.getInstance().reelNoteExchange(oldReel,currentReel,i);
    }
    public static void reelNoteExchangeAll(String oldReel, String newReel ) {
        NoteReelsDBHelper.getInstance().reelNoteExchangeAll(oldReel,newReel);
    }

    public static void addReel(NoteReelArray array) {
        NoteReelsDBHelper.getInstance().addReel(array);
        EventBus.getDefault().post(new AddReelEvent(array));
    }

    public static void alterReelName(String oldReel, String newReel) {
        NoteReelsDBHelper.getInstance().alterReelName(oldReel,newReel);
    }

    public static void updateReel(int id, NoteReelArray noteReelArray) {
        EventBus.getDefault().post(new UpdateReelEvent(id,noteReelArray));
        NoteReelsDBHelper.getInstance().updateReel(id,noteReelArray);
    }
}
