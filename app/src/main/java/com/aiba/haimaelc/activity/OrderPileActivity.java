package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.http.ApiList;
import com.aiba.haimaelc.http.ApiRequest;
import com.aiba.haimaelc.http.ReturnCallBack;
import com.aiba.haimaelc.model.ChargePile;
import com.aiba.haimaelc.model.ChargeStation;
import com.aiba.haimaelc.model.PileOrder;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.tools.ToastUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Locale;

public class OrderPileActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private TextView tv_station_name, tv_pile_name, tv_order_cost, tv_over_time;
    private ChargePile currentPile;
    private ChargeStation currentStation;
    //    private RadioButton rb_15_min, rb_30_min, rb_45_min, rb_60_min;
    private RadioGroup rg_time_select;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private String cost;
    private double baseCost = 0.8;//每15分钟预约费用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pile);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        currentPile = (ChargePile) intent.getSerializableExtra(AppConst.PILE_DETAIL);
        if (currentPile == null) {
            finish();
            return;
        }
        currentPile.pile_id = "2";
        currentStation = (ChargeStation) intent.getSerializableExtra(AppConst.STATION_DETAIL);
        if (currentStation == null) {
            finish();
            return;
        }
        currentStation.station_id = "3";
        initView();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("预约电桩");
        tv_station_name = (TextView) findViewById(R.id.tv_station_name);
        tv_pile_name = (TextView) findViewById(R.id.tv_pile_name);
        tv_order_cost = (TextView) findViewById(R.id.tv_order_cost);
        tv_over_time = (TextView) findViewById(R.id.tv_over_time);
        tv_station_name.setText(currentStation.station_name);
        tv_pile_name.setText(currentPile.pile_name);
        rg_time_select = (RadioGroup) findViewById(R.id.rg_time_select);
        rg_time_select.setOnCheckedChangeListener(this);
//        rb_15_min = (RadioButton) findViewById(R.id.rb_15_min);
//        rb_15_min.setOnCheckedChangeListener(this);
//        rb_30_min = (RadioButton) findViewById(R.id.rb_30_min);
//        rb_30_min.setOnCheckedChangeListener(this);
//        rb_45_min = (RadioButton) findViewById(R.id.rb_45_min);
//        rb_45_min.setOnCheckedChangeListener(this);
//        rb_60_min = (RadioButton) findViewById(R.id.rb_60_min);
//        rb_60_min.setOnCheckedChangeListener(this);
        Button bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_submit.setText("立即预约");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                // TODO: 2016/3/15 预约
                if (TextUtils.isEmpty(cost)) {
                    ToastUtil.makeText("请选择时间");
                    return;
                }
                orderPile(true);
                break;
        }
    }

    private void orderPile(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("aid", SysModel.user.aid);
        values.put("stationId", currentStation.station_id);
        values.put("stationName", currentStation.station_name);
        values.put("pileId", currentPile.pile_id);
        values.put("pileName", currentPile.pile_name);
        values.put("orderOverDate", tv_over_time.getText().toString().trim());
        values.put("cost", cost);
        ApiRequest request = new ApiRequest(this, ApiList.ReservePile.getUrl(), ApiList.ReservePile.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<PileOrder>() {
            @Override
            public void onSuccess(int from, PileOrder object) {
                ToastUtil.makeText("预约成功");
                Intent intent = new Intent(OrderPileActivity.this, PileOrderDetailActivity.class);
                intent.putExtra(AppConst.PILE_ORDER, object);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        long startTime = System.currentTimeMillis();
        switch (checkedId) {
            case R.id.rb_15_min:
                setOverTime(startTime, startTime + 15 * 60 * 1000);
                break;
            case R.id.rb_30_min:
                setOverTime(startTime, startTime + 30 * 60 * 1000);
                break;
            case R.id.rb_45_min:
                setOverTime(startTime, startTime + 45 * 60 * 1000);
                break;
            case R.id.rb_60_min:
                setOverTime(startTime, startTime + 60 * 60 * 1000);
                break;
        }
    }

    private void setOverTime(long startTime, long overTime) {
        tv_over_time.setText(dateFormat.format(overTime));
        cost = new DecimalFormat("0.00").format(((overTime - startTime) /
                (15 * 60 * 1000) + ((overTime - startTime) % (15 * 60 * 1000) == 0 ? 0 : 1)) * baseCost);
        tv_order_cost.setText(String.format("%s元", cost));
    }
}
