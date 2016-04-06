package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.http.ApiList;
import com.aiba.haimaelc.http.ApiRequest;
import com.aiba.haimaelc.http.ReturnCallBack;
import com.aiba.haimaelc.model.RescueOrder;
import com.aiba.haimaelc.tools.ToastUtil;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Locale;

public class RescueDetailActivity extends BaseActivity implements View.OnClickListener,
        RouteSearch.OnRouteSearchListener, AMap.InfoWindowAdapter {

    private String rescueOrderId;
    private RescueOrder rescueOrder;
    private TextureMapView mapView;
    private View rescue_state, rescue_map, rescue_driver, rescue_charge, rescue_info, charging_time, timer, driver_phone;
    private ImageView iv_rescue_state_icon, iv_driver_head_photo;
    private TextView tv_state_title, tv_state_describe, tv_timer_second, tv_timer_minute;
    private TextView tv_rescue_num, tv_name, tv_phone, tv_plate, tv_address, tv_start_time, tv_rescue_info_last_item;
    private TextView tv_driver_name, tv_rescue_vehicle_plate, tv_rescue_distance, tv_rescue_time, tv_rescue_cost_estimate;
    private TextView tv_charge_start_time, tv_charge_finish_time, tv_charge_time, tv_charge_quantity;
    private TextView tv_charge_remain_hour, tv_charge_remain_min;
    private TextView tv_rescue_cost, tv_charge_cost, tv_all_cost;
    private RatingBar rb_rescue_star;
    private Button bt_left, bt_right, bt_submit;
    private View rescue_info_last_item, rescue_pay_actual, rescue_pay_expect;
    private AMap aMap;
    private Marker marker;
    private View infoWindow;
    private RouteSearch routeSearch;
    private RouteSearch.DriveRouteQuery query;
    private boolean isChanged;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ADD_UP:
                    int sec = Integer.valueOf(tv_timer_second.getText().toString());
                    int min = Integer.valueOf(tv_timer_minute.getText().toString());
                    if (++sec == 60) {
                        sec = 0;
                        min++;
                    }
                    tv_timer_second.setText(new DecimalFormat("00").format(sec));
                    tv_timer_minute.setText(new DecimalFormat("00").format(min));
                    mHandler.sendEmptyMessageDelayed(ADD_UP, 1000);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_detail);
        mapView = (TextureMapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 必须要写
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        rescueOrder = (RescueOrder) intent.getSerializableExtra(AppConst.RESCUE_ORDER);
        if (rescueOrder == null || TextUtils.isEmpty(rescueOrderId = rescueOrder.order_id)) {
            finish();
            return;
        }
        initView();
        getRescueDetail(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        findViewById(R.id.iv_title_bar_back).setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void initView() {
        rescue_state = findViewById(R.id.rescue_state);
        rescue_driver = findViewById(R.id.rescue_driver);
        rescue_map = findViewById(R.id.rescue_map);
        rescue_charge = findViewById(R.id.rescue_charge);
        rescue_info = findViewById(R.id.rescue_info);
        charging_time = findViewById(R.id.charging_time);
        timer = findViewById(R.id.timer);
        iv_rescue_state_icon = (ImageView) findViewById(R.id.iv_rescue_state_icon);
        findViewById(R.id.show_state_detail).setOnClickListener(this);
        tv_state_title = (TextView) findViewById(R.id.tv_state_title);
        tv_state_describe = (TextView) findViewById(R.id.tv_state_describe);
        bt_left = (Button) findViewById(R.id.bt_left);
        bt_right = (Button) findViewById(R.id.bt_right);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        tv_timer_second = (TextView) findViewById(R.id.tv_timer_second);
        tv_timer_minute = (TextView) findViewById(R.id.tv_timer_minute);
        rescue_info_last_item = findViewById(R.id.rescue_info_last_item);
        rescue_pay_actual = findViewById(R.id.rescue_pay_actual);
        rescue_pay_expect = findViewById(R.id.rescue_pay_expect);
        tv_rescue_num = (TextView) findViewById(R.id.tv_rescue_num);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_plate = (TextView) findViewById(R.id.tv_plate);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_rescue_info_last_item = (TextView) findViewById(R.id.tv_rescue_info_last_item);
        iv_driver_head_photo = (ImageView) findViewById(R.id.iv_driver_head_photo);
        tv_driver_name = (TextView) findViewById(R.id.tv_driver_name);
        rb_rescue_star = (RatingBar) findViewById(R.id.rb_rescue_star);
        tv_rescue_vehicle_plate = (TextView) findViewById(R.id.tv_rescue_vehicle_plate);
        tv_rescue_distance = (TextView) findViewById(R.id.tv_rescue_distance);
        tv_rescue_time = (TextView) findViewById(R.id.tv_rescue_time);
        tv_rescue_cost_estimate = (TextView) findViewById(R.id.tv_rescue_cost_estimate);
        driver_phone = findViewById(R.id.driver_phone);
        tv_charge_start_time = (TextView) findViewById(R.id.tv_charge_start_time);
        tv_charge_finish_time = (TextView) findViewById(R.id.tv_charge_finish_time);
        tv_charge_time = (TextView) findViewById(R.id.tv_charge_time);
        tv_charge_quantity = (TextView) findViewById(R.id.tv_charge_quantity);
        tv_charge_remain_hour = (TextView) findViewById(R.id.tv_charge_remain_hour);
        tv_charge_remain_min = (TextView) findViewById(R.id.tv_charge_remain_min);
        tv_rescue_cost = (TextView) findViewById(R.id.tv_rescue_cost);
        tv_charge_cost = (TextView) findViewById(R.id.tv_charge_cost);
        tv_all_cost = (TextView) findViewById(R.id.tv_all_cost);
        setRescueInfoContent();
        setRescueDetail();
    }

    private void setRescueDetail() {
        initViewVisibility();
        switch (rescueOrder.order_state) {
            case "0":
                setRescueWaitContent();
                break;
            case "1":
                setRescueOnTheWayContent();
                break;
            case "2":
                setRescueChargeContent();
                break;
            case "3":
                setRescueFinishContent();
                break;
            case "4":
                setRescueFailContent();
                break;
        }
    }

    private void setRescueInfoContent() {
        tv_rescue_num.setText(String.format("订单编号：%s", rescueOrder.order_num));
        tv_name.setText(String.format("联系人：%s", rescueOrder.contact_name));
        tv_phone.setText(String.format("手机号码：%s", rescueOrder.contact_phone));
        tv_plate.setText(String.format("车牌号码：%s", rescueOrder.plate_number));
        tv_address.setText(String.format("救援地址：%s", rescueOrder.order_address));
        tv_start_time.setText(String.format("提交时间：%s", rescueOrder.order_time));
    }

    private void setRescueWaitContent() {
        setTitleText("正在等待救援");
        rescue_state.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        rescue_info.setVisibility(View.VISIBLE);
        iv_rescue_state_icon.setImageResource(R.mipmap.icon_rescue_wait);
        tv_state_title.setText("等待救援车接单");
        tv_state_describe.setText("请耐心等待救援车接单");
        bt_left.setVisibility(View.VISIBLE);
        bt_left.setOnClickListener(this);
        bt_left.setText("取消订单");
        bt_right.setVisibility(View.GONE);
        try {//计时
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            long time = System.currentTimeMillis() / 1000 - dateFormat.parse(rescueOrder.order_time).getTime() / 1000;
            time = time < 0 ? 0 : time;
            tv_timer_second.setText(new DecimalFormat("00").format(time % 60));
            tv_timer_minute.setText(new DecimalFormat("00").format(time / 60));
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(ADD_UP, 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rescue_info_last_item.setVisibility(View.GONE);
    }

    private void setRescueOnTheWayContent() {
        setTitleText("正在途中");
        rescue_state.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        rescue_map.setVisibility(View.VISIBLE);
        rescue_map.setOnClickListener(this);
        rescue_driver.setVisibility(View.VISIBLE);
        rescue_info.setVisibility(View.VISIBLE);
        iv_rescue_state_icon.setImageResource(R.mipmap.icon_rescue_on_the_way);
        tv_state_title.setText("救援车正在途中");
        tv_state_describe.setText("救援车已经出发了，请耐心等待哦~");
        bt_left.setVisibility(View.VISIBLE);
        bt_left.setOnClickListener(this);
        bt_left.setText("催一催");
        bt_right.setVisibility(View.VISIBLE);
        bt_right.setOnClickListener(this);
        bt_right.setText("救援车到了");
        try {//计时
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            long time = System.currentTimeMillis() / 1000 - dateFormat.parse(rescueOrder.order_time).getTime() / 1000;
            time = time < 0 ? 0 : time;
            tv_timer_second.setText(new DecimalFormat("00").format(time % 60));
            tv_timer_minute.setText(new DecimalFormat("00").format(time / 60));
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(ADD_UP, 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rescue_info_last_item.setVisibility(View.GONE);
        rescue_pay_actual.setVisibility(View.GONE);
        rescue_pay_expect.setVisibility(View.VISIBLE);
        initMap();
        tv_driver_name.setText(rescueOrder.rescueDriver.name);
        tv_rescue_vehicle_plate.setText(rescueOrder.rescueDriver.plate_number);
        rb_rescue_star.setRating(Float.valueOf(rescueOrder.rescueDriver.score));
        driver_phone.setOnClickListener(this);
        tv_rescue_distance.setText("--");
        tv_rescue_time.setText("--");
        tv_rescue_cost_estimate.setText("--");
    }

    private void initMap() {
        aMap = mapView.getMap();
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mUiSettings.setRotateGesturesEnabled(false);// 禁用手势旋转地图
        mUiSettings.setTiltGesturesEnabled(false);// 禁用倾斜手势。
        mUiSettings.setZoomControlsEnabled(false);//缩放控件
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        aMap.clear();
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(Double.valueOf(rescueOrder.latitude), Double.valueOf(rescueOrder.longitude)))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_location))
                .draggable(true)
                .anchor(0.5f, 0.5f);
        aMap.addMarker(options);
        options = new MarkerOptions();
        options.position(new LatLng(Double.valueOf(rescueOrder.rescueDriver.latitude), Double.valueOf(rescueOrder.rescueDriver.longitude)))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_rescue_vehicle))
                .snippet("snippet")
                .title("title")
                .draggable(true)
                .anchor(0.5f, 0.5f);
        marker = aMap.addMarker(options);
        aMap.setInfoWindowAdapter(this);
        setCameraCenter();
        calculateRoute();
    }

    private void setCameraCenter() {
        double distance = getDistance(Double.valueOf(rescueOrder.rescueDriver.longitude), Double.valueOf(rescueOrder.rescueDriver.latitude),
                Double.valueOf(rescueOrder.longitude), Double.valueOf(rescueOrder.latitude));
        float zoom;
        if (distance > 20000) {
            zoom = 9;
        } else if (distance > 10000) {
            zoom = 10;
        } else if (distance > 5000) {
            zoom = 11;
        } else {
            zoom = 12;
        }
        LatLng latLng = new LatLng((Double.valueOf(rescueOrder.rescueDriver.latitude) + Double.valueOf(rescueOrder.latitude)) / 2,
                (Double.valueOf(rescueOrder.rescueDriver.longitude) + Double.valueOf(rescueOrder.longitude)) / 2);
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void calculateRoute() {
        LatLonPoint mStartPoint = new LatLonPoint(Double.valueOf(rescueOrder.rescueDriver.latitude),
                Double.valueOf(rescueOrder.rescueDriver.longitude));
        LatLonPoint mEndPoint = new LatLonPoint(Double.valueOf(rescueOrder.latitude),
                Double.valueOf(rescueOrder.longitude));
        query = new RouteSearch.DriveRouteQuery(
                new RouteSearch.FromAndTo(mStartPoint, mEndPoint), RouteSearch.DrivingShortDistance, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    private void setRescueChargeContent() {
        setTitleText("正在充电");
        rescue_state.setVisibility(View.VISIBLE);
        charging_time.setVisibility(View.VISIBLE);
        rescue_charge.setVisibility(View.VISIBLE);
        rescue_info.setVisibility(View.VISIBLE);
        iv_rescue_state_icon.setImageResource(R.mipmap.icon_rescue_charging);
        tv_state_title.setText("正在充电");
        tv_state_describe.setText("您的爱车正在充电了哦~");
        rescue_info_last_item.setVisibility(View.GONE);
        tv_charge_start_time.setText(String.format("充电开始时间：%s", rescueOrder.charge_start_time));
        tv_charge_finish_time.setText(String.format("预计充满时间：%s", rescueOrder.charge_finish_time));
        tv_charge_time.setText("充满剩余时间：");
        tv_charge_quantity.setText("充电电量：");
        tv_charge_remain_hour.setText("");
        tv_charge_remain_min.setText("");
        bt_submit.setVisibility(View.VISIBLE);
        bt_submit.setText("结束充电");
        bt_submit.setOnClickListener(this);
    }

    private void setRescueFinishContent() {
        setTitleText("救援完成");
        rescue_driver.setVisibility(View.VISIBLE);
        rescue_charge.setVisibility(View.VISIBLE);
        rescue_info.setVisibility(View.VISIBLE);
        rescue_pay_actual.setVisibility(View.VISIBLE);
        rescue_pay_expect.setVisibility(View.GONE);
        rescue_info_last_item.setVisibility(View.VISIBLE);
        tv_rescue_info_last_item.setText(String.format("完成时间：%s", rescueOrder.finish_time));
        tv_driver_name.setText(rescueOrder.rescueDriver.name);
        tv_rescue_vehicle_plate.setText(rescueOrder.rescueDriver.plate_number);
        driver_phone.setOnClickListener(this);
        tv_rescue_cost.setText(rescueOrder.rescue_cost);
        tv_charge_cost.setText(rescueOrder.charge_cost);
        tv_all_cost.setText(String.valueOf(Integer.valueOf(rescueOrder.rescue_cost) +
                Integer.valueOf(rescueOrder.charge_cost)));
        tv_charge_start_time.setText(String.format("充电开始时间：%s", rescueOrder.charge_start_time));
        tv_charge_finish_time.setText(String.format("充电结束时间：%s", rescueOrder.charge_finish_time));
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            long chargeTime = dateFormat.parse(rescueOrder.charge_finish_time).getTime() / 1000 -
                    dateFormat.parse(rescueOrder.charge_start_time).getTime() / 1000;
            String sec = new DecimalFormat("00").format(chargeTime % 60);
            String min;
            if (chargeTime > 3600) {
                int hour = (int) (chargeTime / 3600);
                min = new DecimalFormat("00").format(chargeTime % 3600 / 60);
                tv_charge_time.setText(String.format(Locale.CHINA, "充电时长：%d:%s:%s", hour, min, sec));
            } else {
                min = new DecimalFormat("00").format(chargeTime / 60);
                tv_charge_time.setText(String.format("充电时长：%s:%s", min, sec));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_charge_quantity.setText(String.format("充电电量：%skWh", rescueOrder.charge_quantity));
    }

    private void setRescueFailContent() {
        setTitleText("救援失败");
        rescue_info.setVisibility(View.VISIBLE);
        rescue_info_last_item.setVisibility(View.VISIBLE);
        tv_rescue_info_last_item.setText("失败原因：订单被取消");
    }

    private void initViewVisibility() {
        rescue_map.setVisibility(View.GONE);
        rescue_state.setVisibility(View.GONE);
        rescue_driver.setVisibility(View.GONE);
        rescue_charge.setVisibility(View.GONE);
        rescue_info.setVisibility(View.GONE);
        charging_time.setVisibility(View.GONE);
        timer.setVisibility(View.GONE);
        bt_submit.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (isChanged) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_bar_back:
                if (isChanged) {
                    setResult(RESULT_OK);
                }
                finish();
                break;
            case R.id.driver_phone:
                // TODO: 2016/4/1 拨打电话
                break;
            case R.id.rescue_map:
                // TODO: 2016/4/1 显示地图详情
                break;
            case R.id.show_state_detail:
                // TODO: 2016/3/24 救援状态列表
                break;
            case R.id.bt_left:
                switch (rescueOrder.order_state) {
                    case "0"://取消救援
                        updateRescue(true, "4");
                        break;
                    case "1"://催一催
                        ToastUtil.makeText("救援车正在途中");
                        break;
                }
                break;
            case R.id.bt_right://救援车到达
                updateRescue(true, "2");
                break;
            case R.id.bt_submit://结束充电
                updateRescue(true, "3");
                break;
        }
    }

    private void getRescueDetail(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        ApiRequest request = new ApiRequest(this, ApiList.GetRescueInfo.getUrl().replace("ID", rescueOrderId), ApiList.GetRescueInfo.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<RescueOrder>() {
            @Override
            public void onSuccess(int from, RescueOrder object) {
                rescueOrder = object;
                setRescueDetail();
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }

    private void updateRescue(boolean showProgress, final String state) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("state", state);
        ApiRequest request = new ApiRequest(this, ApiList.UpdateRescueInfo.getUrl().replace("ID", rescueOrderId), ApiList.UpdateRescueInfo.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<RescueOrder>() {
            @Override
            public void onSuccess(int from, RescueOrder object) {
                // TODO: 2016/4/5 没有返回详情
//                rescueOrder = object;
                isChanged = true;
                rescueOrder.order_state = state;
                setRescueDetail();
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }

    private static final double EARTH_RADIUS = 6378137;

    private double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == 1000 && driveRouteResult != null && driveRouteResult.getDriveQuery().equals(query)
                && driveRouteResult.getPaths() != null && driveRouteResult.getPaths().size() > 0) {
            float distance = 0;
            for (DrivePath path : driveRouteResult.getPaths()) {
                distance += path.getDistance();
            }
            if (infoWindow == null) {
                infoWindow = getLayoutInflater().inflate(R.layout.view_map_info_window, null);
            }
            TextView tv_distance = (TextView) infoWindow.findViewById(R.id.station_drive_distance);
            tv_distance.setText(Html.fromHtml("距离客户<font color = '#F5A623' >" + new DecimalFormat("0.0").format(distance / 1000) + "千米</font>"));
            if (infoWindow.getParent() != null) {
                ((ViewGroup) infoWindow.getParent()).removeView(infoWindow);
            }
            marker.showInfoWindow();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
