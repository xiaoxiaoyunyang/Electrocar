package com.aiba.haimaelc.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.ChargePile;

import java.util.List;

public class ChargePileAdapter extends MyBaseAdapter<ChargePileAdapter.ViewHolder> {

    public ChargePileAdapter(Context context, List list, int resourceId) {
        super(context, list, resourceId);
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.pile_name = (TextView) view.findViewById(R.id.pile_name);
        holder.iv_order_pile = (ImageView) view.findViewById(R.id.iv_order_pile);
        holder.pile_state = (TextView) view.findViewById(R.id.pile_state);
        return holder;
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChargePile data = (ChargePile) list.get(position);
        viewHolder.pile_name.setText(data.pile_name);
        viewHolder.iv_order_pile.setEnabled("1".equals(data.pile_state));
        viewHolder.iv_order_pile.setOnClickListener((View.OnClickListener) context);
        viewHolder.iv_order_pile.setTag(data);
        setPileState(data, viewHolder);
    }

    private void setPileState(ChargePile item, ViewHolder viewHolder) {
        switch (Integer.valueOf(item.pile_state)) {
            case 0:
                viewHolder.pile_state.setText("建设中");
                viewHolder.pile_state.setTextColor(context.getResources().getColor(R.color.text_gray));
                break;
            case 1:
                viewHolder.pile_state.setText("空闲中");
                viewHolder.pile_state.setTextColor(context.getResources().getColor( R.color.green_dark));
                break;
            case 2:
                viewHolder.pile_state.setText("充电中");
                viewHolder.pile_state.setTextColor(context.getResources().getColor( R.color.text_red));
                break;
        }
    }

    class ViewHolder extends MyBaseAdapter.ViewHolder {

        TextView pile_name;
        ImageView iv_order_pile;
        TextView pile_state;

        public ViewHolder() {

        }
    }
}
