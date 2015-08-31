package com.mikhaildev.test.taskmanager.ui.activity;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mikhaildev.test.taskmanager.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    ActionBar mActionBar = null;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.toolbar_progress_bar) View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
        if (mToolbar!=null) {
            setSupportActionBar(mToolbar);
            styleActionBar();
            showBackButton(true); // By default, we show back button
        }
    }

    protected abstract int getLayoutResource();

    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * show back button in ToolBar's view
     * @param show show back button when true
     */
    public void showBackButton(boolean show) {
        if (mActionBar!=null) {
            mActionBar.setDisplayHomeAsUpEnabled(show);
        }
    }

    /**
     * Set title for ToolBar
     * @param title
     */
    public void setTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

    /**
     * Show progress bar in activity
     */
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide progress bar in activity.
     */
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Set a style with action bar (ToolBar)
     */
    private void styleActionBar() {
        mActionBar = getSupportActionBar();
    }

}
