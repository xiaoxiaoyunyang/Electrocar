package com.aiba.haimaelc.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.Address;
import com.aiba.haimaelc.model.Message;

import java.util.List;

public class MessageAdapter extends MyBaseAdapter<MessageAdapter.ViewHolder> {

    public MessageAdapter(Context context, List list, int resourceId) {
        super(context, list, resourceId);
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.tv_message_time = (TextView) view.findViewById(R.id.tv_message_time);
        holder.tv_message_title = (TextView) view.findViewById(R.id.tv_message_title);
        holder.tv_message_data = (TextView) view.findViewById(R.id.tv_message_data);
        holder.tv_message_content = (TextView) view.findViewById(R.id.tv_message_content);
        holder.tv_to_detail = view.findViewById(R.id.tv_to_detail);
        return holder;
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message data = (Message) list.get(position);
        viewHolder.tv_message_title.setText(data.title);
        viewHolder.tv_message_content.setText(data.content);
    }

    class ViewHolder extends MyBaseAdapter.ViewHolder {

        TextView tv_message_time;
        TextView tv_message_title;
        TextView tv_message_data;
        TextView tv_message_content;
        View tv_to_detail;

        public ViewHolder() {

        }
    }
}
