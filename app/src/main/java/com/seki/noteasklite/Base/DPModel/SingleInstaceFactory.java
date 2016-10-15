package com.seki.noteasklite.Base.DPModel;

import java.util.HashMap;

/**
 * Created by yuan on 2015/10/29.
 */
public class SingleInstaceFactory {
    private static HashMap<String,Object> objectSet = new HashMap();
    private static Class[] classes;
    public static void setConstructTypes(Class... classes){
        SingleInstaceFactory.classes = classes;
     }
    public static Object getInstanceByCLass(Class<?> cls,Object ...args){
        String className = cls.getName();
        Object o = null;
        if(objectSet.keySet().contains(className)){
            return objectSet.get(className);
        }else{
            try{
                o = cls.getConstructor(classes).newInstance(args);
                objectSet.put(className,o);
            }catch ( Exception i){
                i.printStackTrace();
            }
            return o;
        }
    }
}
