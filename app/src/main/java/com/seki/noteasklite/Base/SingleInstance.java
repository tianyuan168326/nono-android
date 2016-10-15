package com.seki.noteasklite.Base;

/**
 * Created by yuan on 2015/10/29.
 */
public class SingleInstance {
    protected static SingleInstance instance = null;
    public static  SingleInstance getInstance(){
        if(instance == null){
        }
        return instance;
    }
}
