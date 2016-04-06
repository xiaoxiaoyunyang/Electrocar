package com.aiba.haimaelc.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.ChargeOrder;
import com.aiba.haimaelc.model.PileOrder;
import com.aiba.haimaelc.model.RescueOrder;

import java.util.List;

public class OrderAdapter extends MyBaseAdapter<OrderAdapter.ViewHolder> {

    public OrderAdapter(Context context, List list, int resourceId) {
        super(context, list, resourceId);
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.tv_order_title = (TextView) view.findViewById(R.id.tv_order_title);
        holder.tv_order_time = (TextView) view.findViewById(R.id.tv_order_time);
        holder.tv_order_state = (TextView) view.findViewById(R.id.tv_order_state);
        return holder;
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, int position) {
        Object data = list.get(position);
        if (data instanceof PileOrder) {
            viewHolder.tv_order_title.setText(String.format("%s（%s）", ((PileOrder) data).station_name, ((PileOrder) data).pile_name));
            viewHolder.tv_order_time.setText(((PileOrder) data).order_time);
            switch (((PileOrder) data).order_state) {
                case "0":
                    viewHolder.tv_order_state.setText("预约成功");
                    viewHolder.tv_order_state.setTextColor(context.getResources().getColor(R.color.green_dark));
                    break;
                case "1":
                    viewHolder.tv_order_state.setText("预约完成");
                    viewHolder.tv_order_state.setTextColor(context.getResources().getColor(R.color.text_gray));
                    break;
                case "2":
                    viewHolder.tv_order_state.setText("预约已过期");
                    viewHolder.tv_order_state.setTextColor(context.getResources().getColor(R.color.text_gray));
                    break;
                case "3":
                    viewHolder.tv_order_state.setText("预约失败");
                    viewHolder.tv_order_state.setTextColor(context.getResources().getColor(R.color.text_gray));
                    break;
            }
        } else if (data instanceof ChargeOrder) {
            viewHolder.tv_order_title.setText(String.format("%s（%s）", ((ChargeOrder) data).station_name, ((ChargeOrder) data).pile_name));
            viewHolder.tv_order_time.setText(((ChargeOrder) data).order_time);
            switch (((ChargeOrder) data).order_state) {
                case "0":
                    viewHolder.tv_order_state.setText("正在充电");
                    viewHolder.tv_order_state.setTextColor(context.getResources().getColor(R.color.green_dark));
                    break;
                case "1":
                    viewHolder.tv_order_state.setText("充电完成");
                    viewHolder.tv_order_state.setTextColor(context.getResources().getColor(R.color.text_gray));
                    break;
            }
        } else if (data instanceof RescueOrder) {
            viewHolder.tv_order_title.setText(((RescueOrder) data).order_address);
            viewHolder.tv_order_time.setText(((RescueOrder) data).order_time);
            switch (((RescueOrder) data).order_state) {
                case "0":
                case "1":
                case "2":
                    viewHolder.tv_order_state.setText("正在救援");
                    viewHolder.tv_order_state.setTextColor(context.getResources().getColor(R.color.green_dark));
                    break;
                case "3":
                    viewHolder.tv_order_state.setText("救援完成");
                    viewHolder.tv_order_state.setTextColor(context.getResources().getColor(R.color.text_gray));
                    break;
                case "4":
                    viewHolder.tv_order_state.setText("救援失败");
                    viewHolder.tv_order_state.setTextColor(context.getResources().getColor(R.color.text_gray));
                    break;
            }
        }
    }

    class ViewHolder extends MyBaseAdapter.ViewHolder {

        TextView tv_order_title;
        TextView tv_order_time;
        TextView tv_order_state;

        public ViewHolder() {

        }
    }
}
