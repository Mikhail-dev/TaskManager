package com.mikhaildev.test.taskmanager.util;

/**
 * Created by E.Mikhail on 23.08.2015.
 */
public class StringUtils {

    private StringUtils() {
        //Empty
    }

    public static boolean isNullOrEmpty(final String string) {
        return string == null || string.length()==0;
    }
}
