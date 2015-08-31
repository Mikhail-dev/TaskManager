package com.mikhaildev.test.taskmanager.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mikhaildev.test.taskmanager.R;
import com.mikhaildev.test.taskmanager.model.Task;
import com.mikhaildev.test.taskmanager.ui.view.DividerItemDecoration;
import com.mikhaildev.test.taskmanager.ui.view.adapter.TaskAdapter;
import com.mikhaildev.test.taskmanager.util.UiUtils;

import butterknife.Bind;

/**
 * Created by E.Mikhail on 23.08.2015.
 */
public class DetailActivity extends BaseActivity {

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        task = (Task) getIntent().getSerializableExtra(UiUtils.EXTRA_TASK_OBJECT);
        if (task==null) {
            finish();
            return;
        }

        getLayoutResource();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new TaskAdapter(task));

        if (task.getPrices()!=null && task.getPrices().length>0)
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.a_detail;
    }
}
