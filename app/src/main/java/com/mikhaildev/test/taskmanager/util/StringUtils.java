package com.mikhaildev.test.taskmanager.util;

/**
 * Created by E.Mikhail on 23.08.2015.
 */
public class StringUtils {

    public static final String MAP_FRAGMENT  = "map_fragment";
    public static final String EXTRA_TASK_OBJECT = "extra_task_object";

    private StringUtils() {
        //Empty
    }

    public static boolean isNullOrEmpty(final String string) {
        return string == null || string.length()==0;
    }
}
