package com.seki.noteasklite.OnGetImagePolicy;

/**
 * Created by yuan-tian01 on 2016/2/29.
 */

public abstract class IOnGetImagePolicy {
    public static final int SRC_TYPE_LOCAL = 0;
    public static final int SRC_TYPE_INTERNET= 1;
    protected OnRealPath onRealPath  =null;
    public IOnGetImagePolicy(OnRealPath onRealPath) {
        this.onRealPath = onRealPath;
    }

    //method for translate src-path to dest-path
    public abstract void  getRealImagePath(String path, int src_pathType);
    //an particular implementation for src on local
    public abstract void getRealImagePath(String path);
    public String preImageProcess(String srcPath){
        return srcPath;
    }
    public interface OnRealPath{
        public void realPath(String realPath);
    }
}
