package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.adapter.ChargePileAdapter;
import com.aiba.haimaelc.http.ApiList;
import com.aiba.haimaelc.http.ApiRequest;
import com.aiba.haimaelc.http.ReturnCallBack;
import com.aiba.haimaelc.model.ChargeOrder;
import com.aiba.haimaelc.model.ChargePile;
import com.aiba.haimaelc.model.ChargeStation;
import com.aiba.haimaelc.tools.CommonTools;
import com.aiba.haimaelc.tools.ToastUtil;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ChargeStationDetailActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private ImageView iv_title_bar_right, station_type, open_outside;
    private TextView station_name, station_address, tv_charge_cost, tv_park_cost, station_distance;
    private TextView tv_fast_pile, tv_slow_pile, tv_weekday_work_time, tv_weekend_work_time;
    private TextView tv_surround_coffee, tv_surround_western_food, tv_surround_hotel,
            tv_surround_restaurant, tv_surround_supermarket, tv_surround_ktv, tv_surround_desc;
    private RatingBar station_star;
    private ChargeStation currentStation;
    private List<ChargePile> pileList = new ArrayList<>();
    private ListView pile_list_view;
    private ChargePileAdapter chargePileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        currentStation = (ChargeStation) intent.getSerializableExtra(AppConst.STATION_DETAIL);
        if (currentStation == null) {
            finish();
            return;
        }
        initView();
        getStationDetail(true);
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("电桩详情");
        iv_title_bar_right = (ImageView) findViewById(R.id.iv_title_bar_right);
        iv_title_bar_right.setVisibility(View.VISIBLE);
        iv_title_bar_right.setOnClickListener(this);
        iv_title_bar_right.setImageResource(R.mipmap.bt_station_not_collect);
        station_name = (TextView) findViewById(R.id.station_name);
        station_address = (TextView) findViewById(R.id.station_address);
        station_star = (RatingBar) findViewById(R.id.station_star);
        station_type = (ImageView) findViewById(R.id.station_type);
        tv_charge_cost = (TextView) findViewById(R.id.tv_charge_cost);
        tv_park_cost = (TextView) findViewById(R.id.tv_park_cost);
        station_distance = (TextView) findViewById(R.id.station_distance);
        open_outside = (ImageView) findViewById(R.id.open_outside);
        tv_fast_pile = (TextView) findViewById(R.id.tv_fast_pile);
        tv_slow_pile = (TextView) findViewById(R.id.tv_slow_pile);
        tv_weekday_work_time = (TextView) findViewById(R.id.tv_weekday_work_time);
        tv_weekend_work_time = (TextView) findViewById(R.id.tv_weekend_work_time);
        tv_surround_coffee = (TextView) findViewById(R.id.tv_surround_coffee);
        tv_surround_western_food = (TextView) findViewById(R.id.tv_surround_western_food);
        tv_surround_hotel = (TextView) findViewById(R.id.tv_surround_hotel);
        tv_surround_restaurant = (TextView) findViewById(R.id.tv_surround_restaurant);
        tv_surround_supermarket = (TextView) findViewById(R.id.tv_surround_supermarket);
        tv_surround_ktv = (TextView) findViewById(R.id.tv_surround_ktv);
        tv_surround_desc = (TextView) findViewById(R.id.tv_surround_desc);
        pile_list_view = (ListView) findViewById(R.id.pile_list_view);
        pile_list_view.setOnItemClickListener(this);
        findViewById(R.id.tv_error_correction).setOnClickListener(this);
        findViewById(R.id.tv_station_comment).setOnClickListener(this);
        setStationContent();
        // TODO: 2016/3/15 电桩假数据
        ChargePile pile = new ChargePile();
        pile.pile_name = "1号桩";
        pile.pile_state = "1";
        pileList.add(pile);
        pile = new ChargePile();
        pile.pile_name = "2号桩";
        pile.pile_state = "2";
        pileList.add(pile);
        pile = new ChargePile();
        pile.pile_name = "3号桩";
        pile.pile_state = "1";
        pileList.add(pile);
        chargePileAdapter = new ChargePileAdapter(this, pileList, R.layout.item_charge_pile);
        pile_list_view.setAdapter(chargePileAdapter);
        CommonTools.setListViewHeightBasedOnChildren(pile_list_view);
    }

    private void setStationContent() {
        station_name.setText(currentStation.station_name);
        station_address.setText(currentStation.station_address);
        station_star.setRating(Float.valueOf(currentStation.score));
        // TODO: 2016/4/6 特斯拉图标还没有
        station_type.setImageResource("0".equals(currentStation.station_type) ?
                R.mipmap.icon_station_national_standard : R.mipmap.icon_station_national_standard);
        tv_fast_pile.setVisibility("0".equals(currentStation.open_outside) ? View.VISIBLE : View.GONE);
        tv_charge_cost.setText(String.format("充电费：%s元/度", currentStation.charge_cost));
        tv_park_cost.setText(String.format("充电费：%s元/小时", currentStation.park_cost));
        station_distance.setText(String.format("%skm", new DecimalFormat("0.00").format(getDistance(currentStation) / 1000)));
        tv_fast_pile.setText(String.format("空闲%s/共%s个", currentStation.charge_fast_free_num, currentStation.charge_fast_num));
        tv_slow_pile.setText(String.format("空闲%s/共%s个", currentStation.charge_slow_free_num, currentStation.charge_slow_num));
        tv_weekday_work_time.setText(String.format("工作日：%s", currentStation.open_time));
        tv_weekend_work_time.setText(String.format("节假日：%s", currentStation.open_time));
        setSurround();
    }

    private void setSurround() {
        if (currentStation.surround.contains("1")) {
            CommonTools.setDrawableRight(this, tv_surround_coffee, R.mipmap.icon_surround_exist);
        } else {
            CommonTools.setDrawableRight(this, tv_surround_coffee, R.mipmap.icon_surround_not_exist);
        }
        if (currentStation.surround.contains("2")) {
            CommonTools.setDrawableRight(this, tv_surround_western_food, R.mipmap.icon_surround_exist);
        } else {
            CommonTools.setDrawableRight(this, tv_surround_western_food, R.mipmap.icon_surround_not_exist);
        }
        if (currentStation.surround.contains("3")) {
            CommonTools.setDrawableRight(this, tv_surround_hotel, R.mipmap.icon_surround_exist);
        } else {
            CommonTools.setDrawableRight(this, tv_surround_hotel, R.mipmap.icon_surround_not_exist);
        }
        if (currentStation.surround.contains("4")) {
            CommonTools.setDrawableRight(this, tv_surround_restaurant, R.mipmap.icon_surround_exist);
        } else {
            CommonTools.setDrawableRight(this, tv_surround_restaurant, R.mipmap.icon_surround_not_exist);
        }
        if (currentStation.surround.contains("5")) {
            CommonTools.setDrawableRight(this, tv_surround_supermarket, R.mipmap.icon_surround_exist);
        } else {
            CommonTools.setDrawableRight(this, tv_surround_supermarket, R.mipmap.icon_surround_not_exist);
        }
        if (currentStation.surround.contains("6")) {
            CommonTools.setDrawableRight(this, tv_surround_ktv, R.mipmap.icon_surround_exist);
        } else {
            CommonTools.setDrawableRight(this, tv_surround_ktv, R.mipmap.icon_surround_not_exist);
        }
        tv_surround_desc.setText(currentStation.surround_describe);
    }

    private static double getDistance(ChargeStation item) {
        LatLng startLatlng = new LatLng(Double.parseDouble(item.latitude),
                Double.parseDouble(item.longitude));
        LatLng endLatlng = new LatLng(SysModel.searchStationLat, SysModel.searchStationLng);
        return AMapUtils.calculateLineDistance(endLatlng, startLatlng);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_title_bar_right:
                // TODO: 2016/3/14 收藏电桩
                ToastUtil.makeText("收藏电桩");
                break;
            case R.id.iv_order_pile:
                ChargePile pile = (ChargePile) v.getTag();
                intent = new Intent(ChargeStationDetailActivity.this, OrderPileActivity.class);
                intent.putExtra(AppConst.PILE_DETAIL, pile);
                intent.putExtra(AppConst.STATION_DETAIL, currentStation);
                startActivity(intent);
                break;
            case R.id.tv_station_comment:
                intent = new Intent(ChargeStationDetailActivity.this, CommentStationActivity.class);
                intent.putExtra(AppConst.STATION_ID, currentStation.station_id);
                startActivity(intent);
                break;
            case R.id.tv_error_correction:
                intent = new Intent(ChargeStationDetailActivity.this, ErrorCorrectionActivity.class);
                intent.putExtra(AppConst.STATION_ID, currentStation.station_id);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void getStationDetail(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        ApiRequest request = new ApiRequest(this, ApiList.GetStationInfo.getUrl().replace("ID", currentStation.station_id), ApiList.GetStationInfo.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<ChargeStation>() {
            @Override
            public void onSuccess(int from, ChargeStation object) {
                currentStation = object;
                setStationContent();
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }
}
