package com.seki.noteasklite.DataUtil.BusEvent;

/**
 * Created by tianyuan on 16/10/27.
 */
public class UpDateUserInfoSuccessEvent {
    public String name;
    public String _Abstract;
    public String headImg;
    public UpDateUserInfoSuccessEvent(String name, String _Abstract, String headImg) {
        this.name = name;
        this._Abstract = _Abstract;
        this.headImg = headImg;
    }
}
