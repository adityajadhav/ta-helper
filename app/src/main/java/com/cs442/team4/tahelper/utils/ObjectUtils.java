package com.cs442.team4.tahelper.utils;

import java.util.Collection;

/**
 * Created by neo on 05-11-2016.
 */

final public class ObjectUtils {


    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }


    public static boolean isEmpty(String value) {
        if (value != null && value.length() > 0) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(Collection<?> collection) {
        if (collection != null && collection.size() > 0) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(Object obj) {
        if (obj != null) {
            return false;
        }
        return true;
    }


}
