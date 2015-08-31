package com.mikhaildev.test.taskmanager.ui.activity;

import com.mikhaildev.test.taskmanager.model.Task;

/**
 * Created by E.Mikhail on 23.08.2015.
 */
public interface DataListener {

    Task[] getTasks();
    void showTaskDetails(int taskId);
}
