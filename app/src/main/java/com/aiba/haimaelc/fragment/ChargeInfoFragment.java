package com.aiba.haimaelc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.activity.ChargeDetailActivity;
import com.aiba.haimaelc.http.ApiList;
import com.aiba.haimaelc.http.ApiRequest;
import com.aiba.haimaelc.http.ReturnCallBack;
import com.aiba.haimaelc.model.ChargeOrder;
import com.aiba.haimaelc.tools.ToastUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

public class ChargeInfoFragment extends BaseFragment {

    private ChargeDetailActivity activity;
    private TextView tv_station_name, tv_pile_name, tv_order_time, tv_finish_time, tv_charge_time, tv_charge_cost;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ChargeDetailActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charge_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getChargeDetail(true);
    }

    public void initView() {
        tv_station_name = (TextView) view.findViewById(R.id.tv_station_name);
        tv_pile_name = (TextView) view.findViewById(R.id.tv_pile_name);
        tv_order_time = (TextView) view.findViewById(R.id.tv_order_time);
        tv_finish_time = (TextView) view.findViewById(R.id.tv_finish_time);
        tv_charge_time = (TextView) view.findViewById(R.id.tv_charge_time);
        tv_charge_cost = (TextView) view.findViewById(R.id.tv_charge_cost);
        setChargeInfo();
    }

    private void setChargeInfo() {
        tv_station_name.setText(activity.chargeOrder.station_name);
        tv_pile_name.setText(activity.chargeOrder.pile_name);
        tv_order_time.setText(activity.chargeOrder.order_time);
        tv_finish_time.setText(activity.chargeOrder.finish_time);
        tv_charge_cost.setText(String.format("%s元", activity.chargeOrder.cost));
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            long chargeTime = dateFormat.parse(activity.chargeOrder.finish_time).getTime() / 1000 -
                    dateFormat.parse(activity.chargeOrder.order_time).getTime() / 1000;
            String sec = new DecimalFormat("00").format(chargeTime % 60);
            String min;
            if (chargeTime > 3600) {
                int hour = (int) (chargeTime / 3600);
                min = new DecimalFormat("00").format(chargeTime % 3600 / 60);
                tv_charge_time.setText(String.format(Locale.CHINA, "%d:%s:%s", hour, min, sec));
            } else {
                min = new DecimalFormat("00").format(chargeTime / 60);
                tv_charge_time.setText(String.format("%s:%s", min, sec));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getChargeDetail(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        ApiRequest request = new ApiRequest(activity, ApiList.GetChargeInfo.getUrl().replace("ID", activity.chargeOrder.id), ApiList.GetChargeInfo.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<ChargeOrder>() {
            @Override
            public void onSuccess(int from, ChargeOrder object) {
                activity.chargeOrder = object;
                setChargeInfo();
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }
}
