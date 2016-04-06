package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiba.haimaelc.AppConfig;
import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.adapter.ChargeStationAdapter;
import com.aiba.haimaelc.model.ChargePile;
import com.aiba.haimaelc.model.ChargeStation;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.tools.ToastUtil;
import com.aiba.haimaelc.widget.CustomerChoiceDialog;
import com.aiba.haimaelc.widget.RefreshLayout;
import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

public class ChargeStationCollectionActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private ChargeStation chargeStation;
    private ListView listView;
    private ChargeStationAdapter stationAdapter;
    private List<ChargeStation> stationList;
    private RefreshLayout mRefreshLayout;
    private TextView tv_no_data;
    private boolean isHasLoadedAll = true;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_station_collection);
        initView();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("我的收藏");
        initListView();
        new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
    }

    private void initListView() {
        stationList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.refresh_list_view);
        listView.setOnItemClickListener(this);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        stationAdapter = new ChargeStationAdapter(this, stationList, R.layout.item_charge_station);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.rl_service_refreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                isHasLoadedAll = true;
                new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
            }
        });
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
            }
        });
        mRefreshLayout.setListView(listView, stationAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.navi_charge_station:
                chargeStation = (ChargeStation) v.getTag();
                final CustomerChoiceDialog dialog = new CustomerChoiceDialog(ChargeStationCollectionActivity.this);
                dialog.setButton1Text("使用软件自带导航", R.color.green_dark);
                dialog.setButton2Text("使用高德地图导航", R.color.green_dark);
                dialog.setButton1OnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(ChargeStationCollectionActivity.this, NaviActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        // TODO: 2016/3/25 车辆位置
                        LatLonPoint mStartPoint = new LatLonPoint(SysModel.latitude, SysModel.longitude);
                        LatLonPoint mEndPoint = new LatLonPoint(Double.valueOf(chargeStation.latitude),
                                Double.valueOf(chargeStation.longitude));
                        intent.putExtra(AppConst.NAVI_START_POINT, mStartPoint);
                        intent.putExtra(AppConst.NAVI_END_POINT, mEndPoint);
                        startActivity(intent);
                    }
                });
                dialog.setButton2OnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        openAmap();
                    }
                });
                dialog.show();
                break;
            case R.id.tv_order_pile:
                ChargeStation item = (ChargeStation) v.getTag();
                // TODO: 2016/3/18 随机分配空闲电桩
                ChargePile pile = new ChargePile();
                pile.pile_name = "2号桩";
                pile.pile_state = "2";
                intent = new Intent(ChargeStationCollectionActivity.this, OrderPileActivity.class);
                intent.putExtra(AppConst.PILE_DETAIL, pile);
                intent.putExtra(AppConst.STATION_DETAIL, item);
                startActivity(intent);
                break;
            case R.id.tv_order_rescue:
                intent = new Intent(ChargeStationCollectionActivity.this, RescueMapActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 打开高德地图导航
     */
    private void openAmap() {
        try {
            Intent intent = new Intent("android.intent.action.VIEW",
                    android.net.Uri
                            .parse("androidamap://navi?sourceApplication="
                                    + R.string.app_name + "&lat="
                                    + chargeStation.latitude + "&lon="
                                    + chargeStation.longitude + "&dev=0"));
            intent.setPackage("com.autonavi.minimap");
            startActivity(intent);
        } catch (Exception e) {
            try {
                Intent viewIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse("market://details?id=com.autonavi.minimap"));
                startActivity(viewIntent);
            } catch (Exception e1) {
                e1.printStackTrace();
                ToastUtil.makeText("未检测到应用市场");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ChargeStationCollectionActivity.this, ChargeStationDetailActivity.class);
        intent.putExtra(AppConst.STATION_DETAIL, stationList.get(position));
        startActivity(intent);
    }

    private class MyTask extends AsyncTask<Integer, Void, Boolean> {

        private int type = 0;
        List<ChargeStation> list = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRefreshLayout.setIsHasLoadedAll(true);
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            switch (type = params[0]) {
                case 0:
//                    try {
//                        ChargeStation station = new ChargeStation();
//                        station.station_id = "2";
//                        station.station_name = "测试充电站1";
//                        station.station_address = "上海市杨浦区五角场1";
//                        station.latitude = String.valueOf(31.3051128258);
//                        station.longitude = String.valueOf(121.5259484902);
//                        station.station_state = "2";
//                        station.station_online = "1";
//                        list.add(station);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return false;
//                    }
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mRefreshLayout.setLoading(false);
            mRefreshLayout.setRefreshing(false);
            isHasLoadedAll = list != null && list.size() < AppConfig.LIST_NUM;
            if (result) {
                switch (type) {
                    case 0:
                        if (page == 0) {
                            stationList.clear();
                        }
                        page++;
                        if (list != null && list.size() > 0) {
                            stationList.addAll(list);
                            stationAdapter.notifyDataSetChanged();
                        } else {
                            tv_no_data.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
            } else {
                tv_no_data.setVisibility(View.GONE);
            }
            mRefreshLayout.setIsHasLoadedAll(isHasLoadedAll);
        }
    }
}
