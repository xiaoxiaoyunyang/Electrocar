package com.aiba.haimaelc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class MyBaseAdapter<VH extends MyBaseAdapter.ViewHolder> extends BaseAdapter {

    protected List list;
    protected Context context;
    private int resourceId;

    public MyBaseAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    public MyBaseAdapter(Context context, List list, int resourceId) {
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final VH viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder = onCreateViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (VH) view.getTag();
        }
        onBindViewHolder(viewHolder, position);
        return view;
    }

    protected abstract VH onCreateViewHolder(View view);

    protected abstract void onBindViewHolder(VH viewHolder, int position);

    protected void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public static abstract class ViewHolder {

        public ViewHolder() {

        }
    }
}
