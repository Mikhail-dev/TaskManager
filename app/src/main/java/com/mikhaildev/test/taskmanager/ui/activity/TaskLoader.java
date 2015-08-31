package com.mikhaildev.test.taskmanager.ui.activity;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.mikhaildev.test.taskmanager.model.Task;
import com.mikhaildev.test.taskmanager.network.ApiManager;
import com.mikhaildev.test.taskmanager.network.AsyncResult;

/**
 * Created by E.Mikhail on 23.08.2015.
 */
public class TaskLoader extends AsyncTaskLoader<AsyncResult<Task[]>> {

    private AsyncResult<Task[]> mResult;


    public TaskLoader(Context context) {
        super(context);
    }

    @Override
    public AsyncResult<Task[]> loadInBackground() {
        AsyncResult<Task[]> data = new AsyncResult<Task[]>();
        try {
            data.setData(ApiManager.getTasks(getContext()));
        } catch (Exception e) {
            data.setException(e);
        }
        return data;
    }

    @Override
    public void deliverResult(AsyncResult<Task[]> result) {
        mResult = result;
        if (isStarted()) {
            super.deliverResult(result);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mResult != null) {
            deliverResult(mResult);
        }

        if (takeContentChanged() || mResult == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        mResult = null;
    }
}
