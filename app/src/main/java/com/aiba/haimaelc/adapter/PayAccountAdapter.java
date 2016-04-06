package com.aiba.haimaelc.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.ChargePile;
import com.aiba.haimaelc.model.PayAccount;
import com.aiba.haimaelc.tools.CommonTools;

import java.util.List;

public class PayAccountAdapter extends MyBaseAdapter<PayAccountAdapter.ViewHolder> {

    public PayAccountAdapter(Context context, List list, int resourceId) {
        super(context, list, resourceId);
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.tv_pay_account = (TextView) view.findViewById(R.id.tv_pay_account);
        return holder;
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, int position) {
        PayAccount data = (PayAccount) list.get(position);
        viewHolder.tv_pay_account.setText(data.account);
        if ("0".equals(data.account_type)) {
            CommonTools.setDrawableLeft(context, viewHolder.tv_pay_account, R.mipmap.icon_pay_ali);
        }
    }

    class ViewHolder extends MyBaseAdapter.ViewHolder {

        TextView tv_pay_account;

        public ViewHolder() {

        }
    }
}
