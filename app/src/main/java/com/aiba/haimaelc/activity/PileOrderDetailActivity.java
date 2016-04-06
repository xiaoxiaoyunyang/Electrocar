package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.http.ApiList;
import com.aiba.haimaelc.http.ApiRequest;
import com.aiba.haimaelc.http.ReturnCallBack;
import com.aiba.haimaelc.model.PileOrder;
import com.aiba.haimaelc.tools.ToastUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Locale;

public class PileOrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private PileOrder mOrder;
    //    private String orderId;
    private TextView tv_order_state, tv_over_time, tv_order_cost, tv_station_name, tv_pile_name;
    private View bts_submit;
    private long overTime;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CUT_DOWN:
                    setOverTime();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pile_detail);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        mOrder = (PileOrder) intent.getSerializableExtra(AppConst.PILE_ORDER);
        if (mOrder == null) {
            finish();
            return;
        }
        initView();
//        orderId = mOrder.id;
//        getReservationDetail(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("预约信息");
        Button bt_left = (Button) findViewById(R.id.bt_left);
        bt_left.setOnClickListener(this);
        bt_left.setText("取消预约");
        Button bt_right = (Button) findViewById(R.id.bt_right);
        bt_right.setOnClickListener(this);
        bt_right.setText("开始充电");
        tv_station_name = (TextView) findViewById(R.id.tv_station_name);
        tv_pile_name = (TextView) findViewById(R.id.tv_pile_name);
        tv_order_state = (TextView) findViewById(R.id.tv_order_state);
        tv_order_cost = (TextView) findViewById(R.id.tv_order_cost);
        tv_over_time = (TextView) findViewById(R.id.tv_over_time);
        bts_submit = findViewById(R.id.bts_submit);
        setOrderDetail();
    }

    private void setOrderDetail() {
        tv_over_time.setText(mOrder.over_time);
        tv_order_cost.setText(String.format("%s元", mOrder.cost));
        tv_station_name.setText(mOrder.station_name);
        tv_pile_name.setText(mOrder.pile_name);
        switch (mOrder.order_state) {
            case "0":
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    overTime = dateFormat.parse(mOrder.over_time).getTime() / 1000 - System.currentTimeMillis() / 1000;
                    setOverTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                bts_submit.setVisibility(View.VISIBLE);
                break;
            case "1":
                tv_order_state.setText("预约完成");
                bts_submit.setVisibility(View.GONE);
                break;
            case "2":
                tv_order_state.setText("预约已过期");
                bts_submit.setVisibility(View.GONE);
                break;
            case "3":
                tv_order_state.setText("预约失败");
                bts_submit.setVisibility(View.GONE);
                break;
        }
    }

    private void setOverTime() {
        if (overTime < 0) {
            failOrder(true);
            return;
        }
        String sec = new DecimalFormat("00").format(overTime % 60);
        String min;
        if (overTime / 60 > 60) {
            int hour = (int) (overTime / 3600);
            min = new DecimalFormat("00").format(overTime % 3600 / 60);
            tv_order_state.setText(String.format(Locale.CHINA, "%d小时%s分%s秒后您的预约将过期", hour, min, sec));
        } else {
            min = new DecimalFormat("00").format(overTime / 60);
            tv_order_state.setText(String.format("%s分%s秒后您的预约将过期", min, sec));
        }
        overTime--;
        mHandler.sendEmptyMessageDelayed(CUT_DOWN, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                cancelOrder(true);
                break;
            case R.id.bt_right:
                Intent intent = new Intent(PileOrderDetailActivity.this, PileScanActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void cancelOrder(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("state", "3");
        ApiRequest request = new ApiRequest(this, ApiList.UpdateReservation.getUrl().replace("ID", mOrder.id), ApiList.UpdateReservation.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<PileOrder>() {
            @Override
            public void onSuccess(int from, PileOrder object) {
                ToastUtil.makeText("取消成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }

    private void failOrder(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("state", "2");
        ApiRequest request = new ApiRequest(this, ApiList.UpdateReservation.getUrl().replace("ID", mOrder.id), ApiList.UpdateReservation.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<PileOrder>() {
            @Override
            public void onSuccess(int from, PileOrder object) {
                ToastUtil.makeText("预约过期");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }

    private void getReservationDetail(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        ApiRequest request = new ApiRequest(this, ApiList.GetReservation.getUrl().replace("ID", mOrder.id), ApiList.GetReservation.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<PileOrder>() {
            @Override
            public void onSuccess(int from, PileOrder object) {
                mOrder = object;
                setOrderDetail();
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }
}
