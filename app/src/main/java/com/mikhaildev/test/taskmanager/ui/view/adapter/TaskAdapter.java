package com.mikhaildev.test.taskmanager.ui.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaildev.test.taskmanager.R;
import com.mikhaildev.test.taskmanager.model.Task;
import com.mikhaildev.test.taskmanager.util.DateHelper;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by E.Mikhail on 23.08.2015.
 */
public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Task mTask;

    public TaskAdapter(Task task) {
        this.mTask = task;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == TYPE_ITEM) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_price_item, parent, false);
            return new VHItem(v);
        } else if (viewType == TYPE_HEADER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_price_header, parent, false);
            return new VHHeader(v);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem) {
            VHItem item = (VHItem) holder;
            item.price.setText(String.valueOf(getItem(position).getPrice()));
            item.description.setText(getItem(position).getDescription());
        } else if (holder instanceof VHHeader) {
            VHHeader header = (VHHeader) holder;
            header.text.setText(mTask.getText());
            header.longText.setText(mTask.getLongText());
            String str = mTask.getLocationText() + " \u2022 " + DateHelper.formatShortDate(new Date(mTask.getDate()));
            header.dateAndLocationTv.setText(str);
        }
    }

    @Override
    public int getItemCount() {
        return mTask.getPrices()==null ? 1 : mTask.getPrices().length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private Task.Price getItem(int position) {
        return mTask.getPrices()[position - 1];
    }

    class VHItem extends RecyclerView.ViewHolder {

        @Bind(R.id.price_tv) TextView price;
        @Bind(R.id.description_tv) TextView description;

        public VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {

        @Bind(R.id.text_tv) TextView text;
        @Bind(R.id.long_text_tv) TextView longText;
        @Bind(R.id.date_and_location_tv) TextView dateAndLocationTv;

        public VHHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
