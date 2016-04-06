package com.aiba.haimaelc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.AverageCost;

import java.util.List;

/**
 * Created by zhu on 16/3/25.
 */
public class AverageCostAdapter extends MyBaseAdapter<AverageCostAdapter.ViewHolder> {

    public AverageCostAdapter(Context context, List list, int resourceId) {
        super(context, list, resourceId);
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.cup = (ImageView) view.findViewById(R.id.cup);
        holder.averageCost = (TextView) view.findViewById(R.id.average_cost);
        holder.averageElc = (TextView) view.findViewById(R.id.average_elc);
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.view = view.findViewById(R.id.average_cost_view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, int position) {
        if((position + 1) % 2 == 0){
            viewHolder.view.setBackgroundResource(R.color.item_green);
        }else{
            viewHolder.view.setBackgroundColor(Color.WHITE);
        }
        if(position == 0){
            viewHolder.cup.setBackgroundResource(R.mipmap.gold);
        }else if(position == 1){
            viewHolder.cup.setBackgroundResource(R.mipmap.silver);
        }else if(position == 2){
            viewHolder.cup.setBackgroundResource(R.mipmap.copper);
        }else{
            viewHolder.cup.setBackgroundResource(0);
        }
        AverageCost cost = (AverageCost) list.get(position);
        viewHolder.name.setText(cost.name);
        viewHolder.averageElc.setText(cost.averageElc);
        viewHolder.averageCost.setText(cost.averageCost);
    }

    class ViewHolder extends MyBaseAdapter.ViewHolder {

        TextView name;
        TextView averageCost;
        TextView averageElc;
        ImageView cup;
        View view;

        public ViewHolder() {

        }
    }
}
