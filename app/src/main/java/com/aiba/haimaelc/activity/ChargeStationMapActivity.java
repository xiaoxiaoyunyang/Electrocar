package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.aiba.haimaelc.AppConfig;
import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.adapter.ChargeStationAdapter;
import com.aiba.haimaelc.model.Address;
import com.aiba.haimaelc.model.ChargePile;
import com.aiba.haimaelc.model.ChargeStation;
import com.aiba.haimaelc.tools.CommonTools;
import com.aiba.haimaelc.tools.MarkerCluster;
import com.aiba.haimaelc.tools.ToastUtil;
import com.aiba.haimaelc.widget.CustomerChoiceDialog;
import com.aiba.haimaelc.widget.RefreshLayout;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChargeStationMapActivity extends BaseActivity implements View.OnClickListener,
        LocationSource, AdapterView.OnItemClickListener,
        AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, AMap.OnMapClickListener,
        CompoundButton.OnCheckedChangeListener, RouteSearch.OnRouteSearchListener,
        OnSeekBarChangeListener {

    private TextureMapView mapView;
    private AMap aMap;
    private AMapLocationClient mLocationClient;
    private OnLocationChangedListener mListener;
    private RouteSearch routeSearch;
    private float distance;//到电站路径距离
    private Polyline polyline;
    private RouteSearch.DriveRouteQuery query;
    private View infoWindow;
    private SeekBar sb_station_range;
    private TextView tv_station_range;
    private String cityName;
    private AutoCompleteTextView destination;
    private View iv_title_bar_right, rl_list, ll_filter, ll_charge_station, iv_appointment;
    private TextView tv_title_bar_right, tv_no_data;
    private ListView listView;
    private List<ChargeStation> chargeStationList;
    private List<Marker> markers = new ArrayList<>();
    private Map<ChargeStation, MarkerOptions> markerOptionsMap = new HashMap<>();// 所有的marker
    private Map<ChargeStation, MarkerOptions> markerOptionsMapInView = new HashMap<>();// 视野内的marker
    private ChargeStationAdapter chargePileAdapter;
    private RefreshLayout mRefreshLayout;
    private boolean isHasLoadedAll = true;
    private int page = 0;
    private TranslateAnimation mShowAction, mHiddenAction;
    private ChargeStationAdapter.ViewHolder viewHolder;
    private ChargeStation currentChargeStation;//当前选中电站
    private Marker currentMarker;//当前选中电站的marker
    private CheckBox cb_pile_speed_slow, cb_pile_speed_fast, cb_pile_speed_faster;
    private CheckBox cb_pile_free, cb_pile_use, cb_pile_constructing;
    private CheckBox cb_pile_dc, cb_pile_ac, cb_pile_industry;
    private boolean canInfoWindowShow;
    private static final int SHOW_INFO_WINDOW = 1;
//    private boolean isAllOperator = true;//全部运营商

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_INFO_WINDOW:
                    if (canInfoWindowShow && currentMarker != null) {
                        currentMarker.showInfoWindow();
                    } else {
                        canInfoWindowShow = true;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_map);
        mapView = (TextureMapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 必须要写
        initView();
        initMap();
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
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();//销毁定位客户端。
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void initView() {
        findViewById(R.id.iv_location).setOnClickListener(this);//定位按钮
        findViewById(R.id.iv_filter).setOnClickListener(this);//筛选
        findViewById(R.id.iv_scan).setOnClickListener(this);//扫码充电
        iv_appointment = findViewById(R.id.iv_appointment);//预约
        iv_appointment.setOnClickListener(this);
        destination = (AutoCompleteTextView) findViewById(R.id.destination);
        destination.setOnClickListener(this);
        destination.setFocusable(false);
        destination.setFocusableInTouchMode(false);
        iv_title_bar_right = findViewById(R.id.iv_title_bar_right);
        tv_title_bar_right = (TextView) findViewById(R.id.tv_title_bar_right);
        findViewById(R.id.tv_title).setVisibility(View.GONE);
        findViewById(R.id.et_title).setVisibility(View.VISIBLE);
        findViewById(R.id.station_list_divider).setVisibility(View.GONE);
        rl_list = findViewById(R.id.rl_list);
        ll_filter = findViewById(R.id.ll_filter);
        rl_list.setVisibility(View.GONE);
        ll_filter.setVisibility(View.GONE);
        iv_title_bar_right.setVisibility(View.VISIBLE);
        tv_title_bar_right.setVisibility(View.GONE);
        iv_title_bar_right.setOnClickListener(this);
        tv_title_bar_right.setOnClickListener(this);
        tv_title_bar_right.setText("筛选");
        initChargeStationView();
        initFilter();
        initListView();
        initAnimation();
    }

    private void initMap() {
        aMap = mapView.getMap();
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mUiSettings.setScaleControlsEnabled(true); // 设置显示地图的默认比例尺
        mUiSettings.setRotateGesturesEnabled(false);// 禁用手势旋转地图
        mUiSettings.setTiltGesturesEnabled(false);// 禁用倾斜手势。
        mUiSettings.setCompassEnabled(true);//显示指南针
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.marker_location))
                .anchor(0.5f, 0.5f);// 设置小蓝点的图标
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(SysModel.carGps.latitude),
                Double.valueOf(SysModel.carGps.longitude)), AppConfig.ZOOM));
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                resetMarks();
            }
        });
        aMap.setInfoWindowAdapter(this);
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
    }


    private void initChargeStationView() {
        ll_charge_station = findViewById(R.id.ll_charge_station);
        ll_charge_station.setAlpha(0.95f);
        viewHolder = ChargeStationAdapter.createViewHolder(ll_charge_station);
        ll_charge_station.setVisibility(View.GONE);
        ll_charge_station.setOnClickListener(this);
    }

    private void initFilter() {
//        SlipButton bt_all_operator = (SlipButton) findViewById(R.id.bt_all_operator);
//        bt_all_operator.setOnChangedListener("", new SlipOnChangedListener(bt_all_operator));
//        bt_all_operator.setChecked(isAllOperator);
        cb_pile_speed_slow = (CheckBox) findViewById(R.id.cb_pile_speed_slow);
        cb_pile_speed_fast = (CheckBox) findViewById(R.id.cb_pile_speed_fast);
        cb_pile_speed_faster = (CheckBox) findViewById(R.id.cb_pile_speed_faster);
        cb_pile_free = (CheckBox) findViewById(R.id.cb_pile_free);
        cb_pile_use = (CheckBox) findViewById(R.id.cb_pile_use);
        cb_pile_constructing = (CheckBox) findViewById(R.id.cb_pile_constructing);
        cb_pile_dc = (CheckBox) findViewById(R.id.cb_pile_dc);
        cb_pile_ac = (CheckBox) findViewById(R.id.cb_pile_ac);
        cb_pile_industry = (CheckBox) findViewById(R.id.cb_pile_industry);
        cb_pile_speed_slow.setOnCheckedChangeListener(this);
        cb_pile_speed_fast.setOnCheckedChangeListener(this);
        cb_pile_speed_faster.setOnCheckedChangeListener(this);
        cb_pile_free.setOnCheckedChangeListener(this);
        cb_pile_use.setOnCheckedChangeListener(this);
        cb_pile_constructing.setOnCheckedChangeListener(this);
        cb_pile_dc.setOnCheckedChangeListener(this);
        cb_pile_ac.setOnCheckedChangeListener(this);
        cb_pile_industry.setOnCheckedChangeListener(this);
        cb_pile_speed_slow.setChecked(true);
        cb_pile_speed_fast.setChecked(true);
        cb_pile_speed_faster.setChecked(true);
        cb_pile_free.setChecked(true);
        cb_pile_use.setChecked(true);
        cb_pile_constructing.setChecked(true);
        cb_pile_dc.setChecked(true);
        cb_pile_ac.setChecked(true);
        cb_pile_industry.setChecked(true);
        tv_station_range = (TextView) findViewById(R.id.tv_station_range);
        sb_station_range = (SeekBar) findViewById(R.id.sb_station_range);
        sb_station_range.setOnSeekBarChangeListener(this);
        sb_station_range.setProgress(50);
        tv_station_range.setText(String.format(Locale.CHINA, "%d公里", sb_station_range.getProgress()));
        Button bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_submit.setText("确定");
    }

    private void initListView() {
        chargeStationList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.refresh_list_view);
        listView.setOnItemClickListener(this);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        chargePileAdapter = new ChargeStationAdapter(this, chargeStationList, R.layout.item_charge_station);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.rl_service_refreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                isHasLoadedAll = true;
                getStationList();
            }
        });
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                getStationList();
            }
        });
        mRefreshLayout.setListView(listView, chargePileAdapter);
    }

    private void initAnimation() {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(300);
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(300);
    }

    private void turnBack() {
        if (ll_filter.getVisibility() == View.VISIBLE) {
            ll_filter.startAnimation(mHiddenAction);
            ll_filter.setVisibility(View.GONE);
            if (rl_list.getVisibility() == View.VISIBLE) {
                iv_title_bar_right.setVisibility(View.GONE);
                tv_title_bar_right.setVisibility(View.VISIBLE);
            } else {
                iv_title_bar_right.setVisibility(View.VISIBLE);
                tv_title_bar_right.setVisibility(View.GONE);
            }
        } else if (rl_list.getVisibility() == View.VISIBLE) {
            rl_list.startAnimation(mHiddenAction);
            rl_list.setVisibility(View.GONE);
            iv_title_bar_right.setVisibility(View.VISIBLE);
            tv_title_bar_right.setVisibility(View.GONE);
        } else if (ll_charge_station.getVisibility() == View.VISIBLE) {
            ll_charge_station.startAnimation(mHiddenAction);
            ll_charge_station.setVisibility(View.GONE);
            removeLineAndInfoWindow();
        } else {
            finish();
        }
    }

    /**
     * 获取视野内的marker 根据聚合算法合成自定义的marker 显示视野内的marker
     */
    private void resetMarks() {
        // 开始刷新车辆
        Projection projection = aMap.getProjection();
        Point p;
        markerOptionsMapInView.clear();
        // 获取在当前视野内的marker;提高效率
        for (Map.Entry entry : markerOptionsMap.entrySet()) {
            ChargeStation station = (ChargeStation) entry.getKey();
            MarkerOptions options = (MarkerOptions) entry.getValue();
            p = projection.toScreenLocation(options.getPosition());
            if (p.x > 0 && p.y > 0 && p.x < CommonTools.getScreenWidth(this)
                    && p.y < CommonTools.getScreenHeight(this)) {
                markerOptionsMapInView.put(station, options);
            }
        }
        // 自定义的聚合类MyMarkerCluster
        ArrayList<MarkerCluster> clustersMarker = new ArrayList<>();
        for (Map.Entry entry : markerOptionsMapInView.entrySet()) {
            ChargeStation station = (ChargeStation) entry.getKey();
            MarkerOptions options = (MarkerOptions) entry.getValue();
            //不合并marker
            clustersMarker.add(new MarkerCluster(ChargeStationMapActivity.this, options, projection,
                    CommonTools.dpToPx(this, 15), station));// 根据自己需求调整
            //合并marker
//            if (clustersMarker.size() == 0) {
//                clustersMarker.add(new MarkerCluster(ChargeStationMapActivity.this, options, projection,
//                        CommonTools.dpToPx(this, 15), station));// 根据自己需求调整
//            } else {
//                boolean isIn = false;
//                for (MarkerCluster cluster : clustersMarker) {
//                    if (cluster.getBounds().contains(options.getPosition())) {
//                        cluster.addMarker(options);
//                        isIn = true;
//                        break;
//                    }
//                }
//                if (!isIn) {
//                    clustersMarker.add(new MarkerCluster(ChargeStationMapActivity.this, options, projection,
//                            CommonTools.dpToPx(this, 15), station));
//                }
//            }
        }
        // 设置聚合点的位置和icon
        for (MarkerCluster mmc : clustersMarker) {
            mmc.setPositionAndIcon();
        }
        clearMarkers();
        // 重新添加
        for (MarkerCluster cluster : clustersMarker) {
            if (cluster.getSize() == 1) {
                if (!cluster.getObject().equals(currentChargeStation)) {
                    Marker marker = aMap.addMarker(cluster.getOptions());
                    marker.setObject(cluster.getObject());
                    markers.add(marker);
                }
            } else {
                markers.add(aMap.addMarker(cluster.getOptions()));
            }
        }
    }

    private void clearMarkers() {
        for (Marker marker : markers) {
            if (marker != null && !marker.equals(currentMarker)) {
                marker.remove();
            }
        }
        markers.clear();
        if (currentMarker != null) {
            markers.add(currentMarker);
        }
    }

    private void addPoiMarkers() {
        markerOptionsMap.clear();
        for (ChargeStation item : chargeStationList) {
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(new LatLng(Double.valueOf(item.latitude), Double.valueOf(item.longitude)))
                    .snippet("snippet")
                    .title("title")
                    .draggable(true)
                    .anchor(0.5f, 1f);
            switch (Integer.valueOf(item.station_state)) {
                case 0:
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_station_constructing));
                    break;
                case 1:
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_station_empty));
                    break;
                case 2:
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_station_full));
                    break;
            }
            markerOptionsMap.put(item, markerOption);
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        Location location = new Location(LocationManager.NETWORK_PROVIDER);
        location.setLatitude(Double.valueOf(SysModel.carGps.latitude));
        location.setLongitude(Double.valueOf(SysModel.carGps.longitude));
        if (!CommonTools.isSameLocation(location, aMap.getMyLocation())) {
            SysModel.searchStationLat = location.getLatitude();
            SysModel.searchStationLng = location.getLongitude();
            mListener.onLocationChanged(location);
        }
        page = 0;
        isHasLoadedAll = true;
        getStationList();
//        if (mLocationClient == null) {
//            mLocationClient = new AMapLocationClient(this);
//            //初始化定位参数
//            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
//            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//            //设置是否返回地址信息（默认返回地址信息）
//            mLocationOption.setNeedAddress(true);
//            //设置是否只定位一次,默认为false
//            mLocationOption.setOnceLocation(true);
//            //设置是否强制刷新WIFI，默认为强制刷新
//            mLocationOption.setWifiActiveScan(true);
//            //设置是否允许模拟位置,默认为false，不允许模拟位置
//            mLocationOption.setMockEnable(false);
//            //设置定位间隔,单位毫秒,默认为2000ms
//            mLocationOption.setInterval(60 * 1000);
//            //给定位客户端对象设置定位参数
//            mLocationClient.setLocationOption(mLocationOption);
//            mLocationClient.setLocationListener(this);
//            mLocationClient.startLocation();//启动定位
//        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (mListener != null && aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
//                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//                Dictionary.saveLocation(aMapLocation);
//                lat = aMapLocation.getLatitude();
//                lng = aMapLocation.getLongitude();
//                cityName = aMapLocation.getCity();
//                getStationList();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        turnBack();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        final ChargeStation item;
        switch (v.getId()) {
            case R.id.iv_location:
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(SysModel.carGps.latitude),
                        Double.valueOf(SysModel.carGps.longitude)), aMap.getCameraPosition().zoom));
                destination.setText("");
                SysModel.searchStationLat = Double.valueOf(SysModel.carGps.latitude);
                SysModel.searchStationLng = Double.valueOf(SysModel.carGps.longitude);
                page = 0;
                isHasLoadedAll = true;
                getStationList();
                break;
            case R.id.iv_title_bar_back:
                turnBack();
                break;
            case R.id.iv_title_bar_right:
                mRefreshLayout.setListView(listView, chargePileAdapter);
                iv_title_bar_right.setVisibility(View.GONE);
                tv_title_bar_right.setVisibility(View.VISIBLE);
                rl_list.startAnimation(mShowAction);
                rl_list.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_title_bar_right:
            case R.id.iv_filter:
                iv_title_bar_right.setVisibility(View.GONE);
                tv_title_bar_right.setVisibility(View.GONE);
                ll_filter.startAnimation(mShowAction);
                ll_filter.setVisibility(View.VISIBLE);
                break;
            case R.id.destination:
                intent = new Intent(ChargeStationMapActivity.this, SearchMapActivity.class);
                intent.putExtra(AppConst.CITY_NAME, cityName);
                startActivityForResult(intent, SEARCH_ADDRESS);
                break;
            case R.id.ll_charge_station:
                intent = new Intent(ChargeStationMapActivity.this, ChargeStationDetailActivity.class);
                intent.putExtra(AppConst.STATION_DETAIL, currentChargeStation);
                startActivity(intent);
                break;
            case R.id.iv_scan:
                intent = new Intent(ChargeStationMapActivity.this, PileScanActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_submit:
                // TODO: 2016/3/16 确认筛选条件
                turnBack();
                break;
            case R.id.navi_charge_station:
                item = (ChargeStation) v.getTag();
                final CustomerChoiceDialog dialog = new CustomerChoiceDialog(ChargeStationMapActivity.this);
                dialog.setButton1Text("使用软件自带导航", R.color.green_dark);
                dialog.setButton2Text("使用高德地图导航", R.color.green_dark);
                dialog.setButton1OnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(ChargeStationMapActivity.this, NaviActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        LatLonPoint mStartPoint = new LatLonPoint(SysModel.searchStationLat, SysModel.searchStationLng);
                        LatLonPoint mEndPoint = new LatLonPoint(Double.valueOf(item.latitude),
                                Double.valueOf(item.longitude));
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
                item = (ChargeStation) v.getTag();
                // TODO: 2016/3/18 随机分配空闲电桩
                ChargePile pile = new ChargePile();
                pile.pile_name = "2号桩";
                pile.pile_state = "2";
                intent = new Intent(ChargeStationMapActivity.this, OrderPileActivity.class);
                intent.putExtra(AppConst.PILE_DETAIL, pile);
                intent.putExtra(AppConst.STATION_DETAIL, item);
                startActivity(intent);
                break;
            case R.id.tv_order_rescue:
                intent = new Intent(ChargeStationMapActivity.this, RescueMapActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 到电站路径计算
     */
    private void calculateRoute() {
        removeLineAndInfoWindow();
        LatLonPoint mStartPoint = new LatLonPoint(SysModel.searchStationLat, SysModel.searchStationLng);
        LatLonPoint mEndPoint = new LatLonPoint(Double.valueOf(currentChargeStation.latitude),
                Double.valueOf(currentChargeStation.longitude));
        query = new RouteSearch.DriveRouteQuery(
                new RouteSearch.FromAndTo(mStartPoint, mEndPoint), RouteSearch.DrivingShortDistance, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    private void removeLineAndInfoWindow() {
        if (polyline != null) {
            polyline.remove();
            polyline = null;
        }
        if (infoWindow != null && infoWindow.getParent() != null) {
            ((ViewGroup) infoWindow.getParent()).removeView(infoWindow);
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
                                    + currentChargeStation.latitude + "&lon="
                                    + currentChargeStation.longitude + "&dev=0"));
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
        Intent intent = new Intent(ChargeStationMapActivity.this, ChargeStationDetailActivity.class);
        intent.putExtra(AppConst.STATION_DETAIL, chargeStationList.get(position));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SEARCH_ADDRESS:
                    Address address = (Address) data.getSerializableExtra(AppConst.SELECT_ADDRESS);
                    SysModel.searchStationLat = address.latitude;
                    SysModel.searchStationLng = address.longitude;
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.latitude, address.longitude), AppConfig.ZOOM));
                    destination.setText(address.name);
                    page = 0;
                    isHasLoadedAll = true;
                    getStationList();
                    break;
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        float currentZoom = aMap.getCameraPosition().zoom;
        ChargeStation station = (ChargeStation) marker.getObject();
        if (station != null) {
            if (ll_charge_station.getVisibility() == View.GONE) {
                ll_charge_station.startAnimation(mShowAction);
                ll_charge_station.setVisibility(View.VISIBLE);
            }
            ChargeStationAdapter.setContent(station, viewHolder, this);
            currentChargeStation = station;
            currentMarker = marker;
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.valueOf(station.latitude), Double.valueOf(station.longitude)), currentZoom));
            canInfoWindowShow = false;
            calculateRoute();
            handler.sendEmptyMessageDelayed(SHOW_INFO_WINDOW, 500);
        } else {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ++currentZoom));
        }
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (ll_charge_station.getVisibility() == View.VISIBLE) {
            ll_charge_station.startAnimation(mHiddenAction);
            ll_charge_station.setVisibility(View.GONE);
        }
        if (currentMarker != null) {
            currentMarker.hideInfoWindow();
        }
        currentChargeStation = null;
        currentMarker = null;
        removeLineAndInfoWindow();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_pile_speed_slow:
            case R.id.cb_pile_speed_fast:
            case R.id.cb_pile_speed_faster:
                if (isNoneChoose(cb_pile_speed_slow, cb_pile_speed_fast, cb_pile_speed_faster)) {
                    buttonView.setChecked(true);
                    ToastUtil.makeText("请至少选择一项速率");
                }
                break;
            case R.id.cb_pile_free:
            case R.id.cb_pile_use:
            case R.id.cb_pile_constructing:
                if (isNoneChoose(cb_pile_free, cb_pile_use, cb_pile_constructing)) {
                    buttonView.setChecked(true);
                    ToastUtil.makeText("请至少选择一项状态");
                }
                break;
            case R.id.cb_pile_dc:
            case R.id.cb_pile_ac:
            case R.id.cb_pile_industry:
                if (isNoneChoose(cb_pile_dc, cb_pile_ac, cb_pile_industry)) {
                    buttonView.setChecked(true);
                    ToastUtil.makeText("请至少选择一项类型");
                }
                break;
        }
    }

    private boolean isNoneChoose(CheckBox cb1, CheckBox cb2, CheckBox cb3) {
        return !(cb1.isChecked() || cb2.isChecked() || cb3.isChecked());
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == 1000 && driveRouteResult != null && driveRouteResult.getDriveQuery().equals(query)
                && driveRouteResult.getPaths() != null && driveRouteResult.getPaths().size() > 0) {
            distance = 0;
            for (DrivePath path : driveRouteResult.getPaths()) {
                distance += path.getDistance();
            }
            drawDriveRoute(driveRouteResult);
            if (infoWindow == null) {
                infoWindow = getLayoutInflater().inflate(R.layout.view_map_info_window, null);
            }
            TextView tv_distance = (TextView) infoWindow.findViewById(R.id.station_drive_distance);
            tv_distance.setText(String.format("路程%skm", new DecimalFormat("0.00").format(distance / 1000)));
            handler.sendEmptyMessage(SHOW_INFO_WINDOW);
        }
    }

    private void drawDriveRoute(DriveRouteResult result) {
        if (polyline != null) {
            polyline.remove();
            polyline = null;
        }
        PolylineOptions options = new PolylineOptions();
        for (int i = 0; i < result.getPaths().size(); i++) {
            for (int j = 0; j < result.getPaths().get(i).getSteps().size(); j++) {
                for (int k = 0; k < result.getPaths().get(i).getSteps().get(j).getPolyline().size(); k++) {
                    double latitude = result.getPaths().get(i).getSteps().get(j).getPolyline().get(k).getLatitude();
                    double longitude = result.getPaths().get(i).getSteps().get(j).getPolyline().get(k).getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    options.add(latLng);
                }
            }
        }
        options.width(CommonTools.dpToPx(this, 5))
                .setDottedLine(true)
                .color(getResources().getColor(R.color.text_red));
        polyline = aMap.addPolyline(options);
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv_station_range.setText(String.format(Locale.CHINA, "%d公里", progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
                    try {
                        // TODO: 2016/3/11 测试数据
                        ChargeStation pile = new ChargeStation();
                        pile.station_id = "1";
                        pile.station_name = "凤城花园充电站";
                        pile.station_address = "上海市杨浦区五角场街道延吉西路凤城花园";
                        pile.latitude = String.valueOf(31.2915549295);
                        pile.longitude = String.valueOf(121.5090578081);
                        pile.station_state = "1";
                        pile.station_online = "1";
                        pile.station_id = "5";
                        list.add(pile);
                        pile = new ChargeStation();
                        pile.station_id = "2";
                        pile.station_name = "测试充电站1";
                        pile.station_address = "上海市杨浦区五角场1";
                        pile.latitude = String.valueOf(31.3051128258);
                        pile.longitude = String.valueOf(121.5259484902);
                        pile.station_state = "2";
                        pile.station_online = "1";
                        pile.station_id = "1";
                        list.add(pile);
                        pile = new ChargeStation();
                        pile.station_id = "2";
                        pile.station_name = "测试充电站2";
                        pile.station_address = "上海市杨浦区五角场2";
                        pile.latitude = String.valueOf(31.2960970618);
                        pile.longitude = String.valueOf(121.4941673240);
                        pile.station_state = "0";
                        pile.station_online = "0";
                        pile.station_id = "2";
                        list.add(pile);
                        pile = new ChargeStation();
                        pile.station_id = "3";
                        pile.station_name = "海马测试充电站";
                        pile.station_address = "郑州市经南三路";
                        pile.latitude = String.valueOf(34.7143921687);
                        pile.longitude = String.valueOf(113.8010475029);
                        pile.station_state = "1";
                        pile.station_online = "1";
                        pile.station_id = "3";
                        list.add(pile);
                        pile = new ChargeStation();
                        pile.station_id = "3";
                        pile.station_name = "测试充电站3";
                        pile.station_address = "北京市国展中心";
                        pile.latitude = String.valueOf(39.959779274);
                        pile.longitude = String.valueOf(116.4419818698);
                        pile.station_state = "1";
                        pile.station_online = "1";
                        pile.station_id = "4";
                        list.add(pile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                            chargeStationList.clear();
                        }
                        page++;
                        if (list != null && list.size() > 0) {
                            chargeStationList.addAll(list);
                            chargePileAdapter.notifyDataSetChanged();
                            addPoiMarkers();
                            resetMarks();
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

    private void getStationList() {
        new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
    }

//    class SlipOnChangedListener implements SlipButton.OnChangedListener {
//
//        final SlipButton slipButton;
//
//        private SlipOnChangedListener(SlipButton slipButton) {
//            super();
//            this.slipButton = slipButton;
//        }
//
//        @Override
//        public void OnChanged(String strName, boolean check) {
//            isAllOperator = slipButton.NowChoose;
//        }
//    }
}
