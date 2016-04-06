package com.aiba.haimaelc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.WeekAverageCost;

import java.util.List;

/**
 * Created by zhu on 16/3/25.
 */
public class WeekAverageCostAdapter extends MyBaseAdapter<WeekAverageCostAdapter.ViewHolder> {

    public WeekAverageCostAdapter(Context context, List list, int resourceId) {
        super(context, list, resourceId);
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.date = (TextView) view.findViewById(R.id.date);
        holder.distance = (TextView) view.findViewById(R.id.travll_distance);
        holder.elc = (TextView) view.findViewById(R.id.elc);
        holder.cost = (TextView) view.findViewById(R.id.cost);
        holder.view = view.findViewById(R.id.week_average_cost_view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, int position) {
        if ((position + 1) % 2 == 0) {
            viewHolder.view.setBackgroundResource(R.color.item_green);
        } else {
            viewHolder.view.setBackgroundColor(Color.WHITE);
        }
        WeekAverageCost cost = (WeekAverageCost) list.get(position);
        viewHolder.date.setText(cost.date);
        viewHolder.distance.setText(cost.distance);
        viewHolder.elc.setText(cost.elc);
        viewHolder.cost.setText(cost.cost);
    }

    class ViewHolder extends MyBaseAdapter.ViewHolder {

        TextView date;
        TextView distance;
        TextView elc;
        TextView cost;
        View view;

        public ViewHolder() {

        }
    }
}
