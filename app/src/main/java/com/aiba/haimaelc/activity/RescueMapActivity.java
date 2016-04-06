package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.aiba.haimaelc.AppConfig;
import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.model.Address;
import com.aiba.haimaelc.tools.CommonTools;
import com.aiba.haimaelc.widget.CustomerAlertDialog;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

public class RescueMapActivity extends BaseActivity implements LocationSource, View.OnClickListener,
        GeocodeSearch.OnGeocodeSearchListener {

    private TextureMapView mapView;
    private AMap aMap;
    private AMapLocationClient mLocationClient;
    private LocationSource.OnLocationChangedListener mListener;
    private TextView tv_location_address;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_rescue_map);
        mapView = (TextureMapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 必须要写
        initView();
        initMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
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
        setTitleBarBack();
        setTitleText("充电救援");
        tv_location_address = (TextView) findViewById(R.id.tv_location_address);
        findViewById(R.id.order_rescue).setOnClickListener(this);
    }

    private void initMap() {
        aMap = mapView.getMap();
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mUiSettings.setScaleControlsEnabled(true); // 设置显示地图的默认比例尺
        mUiSettings.setRotateGesturesEnabled(false);// 禁用手势旋转地图
        mUiSettings.setTiltGesturesEnabled(false);// 禁用倾斜手势。
        mUiSettings.setZoomControlsEnabled(false);//缩放控件
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
        GeocodeSearch search = new GeocodeSearch(this);
        search.setOnGeocodeSearchListener(this);
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(Double.valueOf(SysModel.carGps.latitude),
                Double.valueOf(SysModel.carGps.longitude)), 200, GeocodeSearch.AMAP);
        search.getFromLocationAsyn(query);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        Location location = new Location(LocationManager.NETWORK_PROVIDER);
        location.setLatitude(Double.valueOf(SysModel.carGps.latitude));
        location.setLongitude(Double.valueOf(SysModel.carGps.longitude));
        if (!CommonTools.isSameLocation(location, aMap.getMyLocation())) {
            mListener.onLocationChanged(location);
        }
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
//                tv_location_address.setText(aMapLocation.getAddress());
//                Dictionary.saveLocation(aMapLocation);
//                address = new Address();
//                address.latitude = aMapLocation.getLatitude();
//                address.longitude = aMapLocation.getLongitude();
//                address.name = aMapLocation.getPoiName();
//                address.address = aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getRoad();
//            }
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_rescue:
                new CustomerAlertDialog(this).builder()
                        .setImg(R.mipmap.icon_charge_rescue)
                        .setMsg("本地区提供有偿充电救援服务，确认是否需要充电救援？")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(RescueMapActivity.this, OrderRescueActivity.class);
                                intent.putExtra(AppConst.SELECT_ADDRESS, address);
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                break;
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (regeocodeResult != null && i == 1000) {
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            PoiItem item = regeocodeAddress.getPois().get(0);
            address = new Address();
            address.latitude = item.getLatLonPoint().getLatitude();
            address.longitude = item.getLatLonPoint().getLongitude();
            address.name = item.getTitle();
            address.address = item.getCityName() + item.getAdName() + item.getSnippet();
            tv_location_address.setText(String.format("%s%s", address.name, address.address));
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
