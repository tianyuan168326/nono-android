package com.seki.noteasklite.Controller;

/**
 * Created by yuan-tian01 on 2016/4/24.
 */
public interface ImportNoteListener {
    public void beforeImport();
    public void afterImport(int num);
}
