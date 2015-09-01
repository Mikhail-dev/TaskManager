package com.mikhaildev.test.taskmanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikhaildev.test.taskmanager.R;
import com.mikhaildev.test.taskmanager.exception.ApiException;
import com.mikhaildev.test.taskmanager.exception.NetworkConnectionException;
import com.mikhaildev.test.taskmanager.model.Task;
import com.mikhaildev.test.taskmanager.network.AsyncResult;
import com.mikhaildev.test.taskmanager.ui.fragment.MapListener;
import com.mikhaildev.test.taskmanager.ui.fragment.YandexMapFragment;
import com.mikhaildev.test.taskmanager.util.StringUtils;

import java.io.Serializable;


public class MapActivity extends BaseActivity
        implements
            LoaderManager.LoaderCallbacks,
            DataListener {

    private final int LOADER_ID_GET_TASKS = 1;
    private static String EXTRA_TASKS = "extra_tasks";

    private Task[] mTasks;

    private MenuItem refreshBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutResource();
        showMapFragment();
        showBackButton(false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.a_map;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_TASKS, mTasks);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null && savedInstanceState.containsKey(EXTRA_TASKS)) {
            mTasks = (Task[])savedInstanceState.getSerializable(EXTRA_TASKS);
            updateMapMarkers(mTasks);
        }
    }

    @Override
    public Loader onCreateLoader(int loaderId, Bundle bundle) {
        showProgressBar();
        switch (loaderId) {
            case LOADER_ID_GET_TASKS:
                return new TaskLoader(getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        hideProgressBar();
        switch (loader.getId()) {
            case LOADER_ID_GET_TASKS:
                handleData(data);
                break;
        }
    }

    private void handleData(Object data) {
        AsyncResult<Task[]> result = (AsyncResult<Task[]>) data;
        if (result.getData() != null) {
            mTasks = result.getData();
            updateMapMarkers(mTasks);
        } else {
            handleException(result.getException());
        }
    }

    private void handleException(Exception e) {
        if (e.getClass().equals(NetworkConnectionException.class)) {
            Toast.makeText(this, R.string.internet_connection_error, Toast.LENGTH_LONG).show();
        } else if (e.getClass().equals(ApiException.class)) {
            ApiException apiException = (ApiException) e;
            Toast.makeText(this, getString(apiException.getMessageResourceId()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
        refreshBtn.setVisible(false);
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
        refreshBtn.setVisible(true);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        refreshBtn = menu.findItem(R.id.action_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            getSupportLoaderManager().restartLoader(LOADER_ID_GET_TASKS, null, this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMapFragment() {
        YandexMapFragment yandexMapFragment = (YandexMapFragment) getSupportFragmentManager().findFragmentByTag(StringUtils.MAP_FRAGMENT);
        if (yandexMapFragment == null) {
            yandexMapFragment = new YandexMapFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map, yandexMapFragment, StringUtils.MAP_FRAGMENT);
        transaction.commit();
    }

    private void updateMapMarkers(Task[] tasks) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(StringUtils.MAP_FRAGMENT);
        if (fragment!=null && (fragment instanceof MapListener) ) {
            ((MapListener)fragment).updateMarkers(tasks);
        }
    }

    @Override
    public Task[] getTasks() {
        return mTasks;
    }

    @Override
    public void showTaskDetails(int taskId) {
        Task task = getTaskById(taskId);
        if (task==null)
            return;
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(StringUtils.EXTRA_TASK_OBJECT, (Serializable) task);
        startActivity(intent);
    }

    private Task getTaskById(int taskId) {
        if (mTasks==null || mTasks.length==0)
            return null;

        for (int i = 0; i < mTasks.length; i++) {
            if (mTasks[i].getId()==taskId)
                return mTasks[i];
        }
        return null;
    }
}
