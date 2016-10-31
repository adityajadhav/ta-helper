package com.cs442.team4.tahelper.model;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class ModuleEntity {

    private static HashMap<String,String> nametoDBKey = new HashMap<>();


    public static void addKeyValue(String name,String dbKey){
        nametoDBKey.put(name,dbKey);
    }

    public static String getDBKey(String name){
        return nametoDBKey.get(name);
    }

    public static void removeKeyValue(String name){
        nametoDBKey.remove(name);
    }


}
