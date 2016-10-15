package com.seki.noteasklite.DataUtil;

/**
 * Created by 七升 on 2015/8/1.
 */
public class InfoArray {
    private int TYPE_SHOW=0;
    private int TYPE_DELETE=1;
    private int TYPE_NEW=2;
    private int type=0;
    private String[] category=new String[2];

    public InfoArray(int type,String outCategory,String innerCategory){
        this.type=type;
        if(type!=TYPE_NEW){
            this.category[0]=outCategory;
            this.category[1]=innerCategory;
        }
    }

    public String[] getCategory(){
        return  category;
    }

    public int getType(){
        return type;
    }
}
