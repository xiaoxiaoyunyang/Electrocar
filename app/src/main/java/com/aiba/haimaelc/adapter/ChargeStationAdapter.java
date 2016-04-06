package com.aiba.haimaelc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.model.ChargeStation;
import com.aiba.haimaelc.tools.CommonTools;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.List;

public class ChargeStationAdapter extends MyBaseAdapter<ChargeStationAdapter.ViewHolder> {

    public ChargeStationAdapter(Context context, List list, int resourceId) {
        super(context, list, resourceId);
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return createViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChargeStation data = (ChargeStation) list.get(position);
        setContent(data, viewHolder, context);
    }

    public static ViewHolder createViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.station_name = (TextView) view.findViewById(R.id.station_name);
        holder.station_type = (ImageView) view.findViewById(R.id.station_type);
        holder.station_address = (TextView) view.findViewById(R.id.station_address);
        holder.station_distance = (TextView) view.findViewById(R.id.station_distance);
        holder.station_state = (TextView) view.findViewById(R.id.station_state);
        holder.station_online = (TextView) view.findViewById(R.id.station_online);
        holder.navi_charge_station = view.findViewById(R.id.navi_charge_station);
        holder.tv_order_pile = view.findViewById(R.id.tv_order_pile);
        holder.tv_order_rescue = view.findViewById(R.id.tv_order_rescue);
        holder.tv_can_arrive_station = (TextView) view.findViewById(R.id.tv_can_arrive_station);
        return holder;
    }

    public static void setContent(ChargeStation data, ViewHolder viewHolder, Context context) {
        viewHolder.station_name.setText(data.station_name);
        viewHolder.station_address.setText(data.station_address);
        viewHolder.station_distance.setText(String.format("%skm", new DecimalFormat("0.00").format(getDistance(data) / 1000)));
        if (canArriveStation(data)) {
            viewHolder.tv_can_arrive_station.setText("可以到达充电桩");
            viewHolder.tv_can_arrive_station.setTextColor(context.getResources().getColor(R.color.green_dark));
            viewHolder.tv_order_pile.setVisibility(View.VISIBLE);
            viewHolder.tv_order_rescue.setVisibility(View.GONE);
        } else {
            viewHolder.tv_can_arrive_station.setText("不能到达充电桩");
            viewHolder.tv_can_arrive_station.setTextColor(context.getResources().getColor(R.color.text_red));
            viewHolder.tv_order_pile.setVisibility(View.GONE);
            viewHolder.tv_order_rescue.setVisibility(View.VISIBLE);
        }
        switch (data.station_state) {
            case "0":
                viewHolder.station_state.setText("建设中");
                CommonTools.setDrawableLeft(context, viewHolder.station_state, R.mipmap.icon_station_offline);
                viewHolder.tv_order_pile.setEnabled(false);
                break;
            case "1":
                viewHolder.station_state.setText("有空闲电桩");
                CommonTools.setDrawableLeft(context, viewHolder.station_state, R.mipmap.icon_station_free);
                viewHolder.tv_order_pile.setEnabled(true);
                break;
            case "2":
                viewHolder.station_state.setText("无空闲电桩");
                CommonTools.setDrawableLeft(context, viewHolder.station_state, R.mipmap.icon_station_full);
                viewHolder.tv_order_pile.setEnabled(false);
                break;
        }
        switch (data.station_online) {
            case "0":
                viewHolder.station_online.setText("未联网");
                CommonTools.setDrawableLeft(context, viewHolder.station_state, R.mipmap.icon_station_offline);
                break;
            case "1":
                viewHolder.station_online.setText("已联网");
                CommonTools.setDrawableLeft(context, viewHolder.station_state, R.mipmap.icon_station_online);
                break;
        }
        viewHolder.navi_charge_station.setOnClickListener((View.OnClickListener) context);
        viewHolder.navi_charge_station.setTag(data);
        viewHolder.tv_order_pile.setOnClickListener((View.OnClickListener) context);
        viewHolder.tv_order_pile.setTag(data);
        viewHolder.tv_order_rescue.setOnClickListener((View.OnClickListener) context);
    }

    private static double getDistance(ChargeStation item) {
        LatLng startLatlng = new LatLng(Double.parseDouble(item.latitude),
                Double.parseDouble(item.longitude));
        LatLng endLatlng = new LatLng(SysModel.searchStationLat, SysModel.searchStationLng);
        return AMapUtils.calculateLineDistance(endLatlng, startLatlng);
    }

    private static boolean canArriveStation(ChargeStation item) {
        double dis = getDistance(item);
        return Double.valueOf(SysModel.carGps.endurance) > (dis / 1000);
    }

    public static class ViewHolder extends MyBaseAdapter.ViewHolder {

        TextView station_name;
        ImageView station_type;
        TextView station_address;
        TextView station_distance;
        TextView station_state;
        TextView station_online;
        TextView tv_can_arrive_station;//是否能到达电站
        View navi_charge_station;
        View tv_order_pile;//预约充电
        View tv_order_rescue;//充电救援

        public ViewHolder() {

        }
    }
}
