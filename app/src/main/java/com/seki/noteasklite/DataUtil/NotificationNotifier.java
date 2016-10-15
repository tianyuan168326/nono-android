package com.seki.noteasklite.DataUtil;

import android.util.Log;

import java.util.Observable;

/**
 * Created by yuan on 2015/10/29.
 */
public class NotificationNotifier extends Observable {
    public void notifyUI(String mes){
        setChanged();
        super.notifyObservers(mes);
        Log.e("hehe","notify main actiitvty");
    }
}
