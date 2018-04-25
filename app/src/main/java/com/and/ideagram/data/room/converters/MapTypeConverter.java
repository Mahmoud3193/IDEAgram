package com.and.ideagram.data.room.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by file1 on 13/04/2018.
 */

public class MapTypeConverter {

    @TypeConverter
    public static String toString(Map<String, Boolean> map) {
        String sMap = "";
        if(map != null) {
            for(String key : map.keySet()) {
                sMap = sMap + "[<NEXTSTRING>]" + key;
            }
        }else return null;
        return sMap;
    }

    @TypeConverter
    public static Map<String, Boolean> toMap(String string) {
        Map<String, Boolean> mString = new HashMap<>();
        if(string != null) {
            String[] keys = string.split("[<NEXTMAP>]");
            for(String key : keys) {
                mString.put(key,  true);
            }
        }else return null;
        return mString;
    }

}
