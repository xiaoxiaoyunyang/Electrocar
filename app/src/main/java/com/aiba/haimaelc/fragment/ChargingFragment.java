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
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.activity.ChargeDetailActivity;
import com.aiba.haimaelc.http.ApiList;
import com.aiba.haimaelc.http.ApiRequest;
import com.aiba.haimaelc.http.ReturnCallBack;
import com.aiba.haimaelc.model.ChargeOrder;
import com.aiba.haimaelc.tools.ToastUtil;

import java.util.LinkedHashMap;

public class ChargingFragment extends BaseFragment implements View.OnClickListener {

    private ChargeDetailActivity activity;
    private TextView tv_station_name, tv_pile_name;
    private Button bt_charging;
    private boolean isCharging;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ChargeDetailActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charging, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        if (activity.chargeOrder != null) {
            getChargeDetail(true);
        }
    }

    public void initView() {
        bt_charging = (Button) view.findViewById(R.id.bt_charging);
        bt_charging.setOnClickListener(this);
        tv_station_name = (TextView) view.findViewById(R.id.tv_station_name);
        tv_pile_name = (TextView) view.findViewById(R.id.tv_pile_name);
        setCharging();
    }

    private void setCharging() {
        if (activity.chargeOrder == null) {
            isCharging = false;
            tv_station_name.setText(activity.chargePile.station_name);
            tv_pile_name.setText(activity.chargePile.pile_name);
        } else {
            isCharging = true;
            tv_station_name.setText(activity.chargeOrder.station_name);
            tv_pile_name.setText(activity.chargeOrder.pile_name);
        }
        bt_charging.setText(isCharging ? "结束充电" : "开始充电");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_charging:
                if (isCharging) {
                    stopCharge(true);
                } else {
                    startCharge(true);
                }
                break;
        }
    }

    private void stopCharge(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("state", "1");
        ApiRequest request = new ApiRequest(activity, ApiList.UpdateChargeInfo.getUrl().replace("ID", activity.chargeOrder.id), ApiList.UpdateChargeInfo.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<ChargeOrder>() {
            @Override
            public void onSuccess(int from, ChargeOrder object) {
                activity.isChanged = true;
                activity.chargeOrder.order_state = "1";
                activity.addFragment(new ChargeInfoFragment(), ChargeDetailActivity.CHARGE_INFO);
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }

    private void startCharge(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("aid", SysModel.user.aid);
        values.put("charge_station_id", activity.chargePile.station_id);
        values.put("stationName", activity.chargePile.station_name);
        values.put("charge_pile_id", activity.chargePile.pile_id);
        values.put("pileName", activity.chargePile.pile_name);
        ApiRequest request = new ApiRequest(activity, ApiList.Charge.getUrl(), ApiList.Charge.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<ChargeOrder>() {
            @Override
            public void onSuccess(int from, ChargeOrder object) {
                activity.chargeOrder = object;
                isCharging = true;
                bt_charging.setText("结束充电");
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }

    private void getChargeDetail(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        ApiRequest request = new ApiRequest(activity, ApiList.GetChargeInfo.getUrl().replace("ID", activity.chargeOrder.id), ApiList.GetChargeInfo.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<ChargeOrder>() {
            @Override
            public void onSuccess(int from, ChargeOrder object) {
                activity.chargeOrder = object;
                setCharging();
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }
}
