package com.aiba.haimaelc.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.Address;

import java.util.List;

public class AddressAdapter extends MyBaseAdapter<AddressAdapter.ViewHolder> {

    public AddressAdapter(Context context, List list, int resourceId) {
        super(context, list, resourceId);
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.address = (TextView) view.findViewById(R.id.address);
        holder.select = (ImageView) view.findViewById(R.id.select);
        return holder;
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, int position) {
        Address data = (Address) list.get(position);
        viewHolder.name.setText(data.name);
        viewHolder.address.setText(data.address);
        if (((Address) list.get(position)).selected) {
            viewHolder.select.setVisibility(View.VISIBLE);
            viewHolder.name.setTextColor(context.getResources().getColor( R.color.green_dark));
        } else {
            viewHolder.select.setVisibility(View.GONE);
            viewHolder.name.setTextColor(context.getResources().getColor( R.color.text_gray_dark));
        }
    }

    class ViewHolder extends MyBaseAdapter.ViewHolder {

        TextView name;
        TextView address;
        ImageView select;

        public ViewHolder() {

        }
    }
}
