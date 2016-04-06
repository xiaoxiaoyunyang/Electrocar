package com.aiba.haimaelc;

import com.aiba.haimaelc.model.CarGpsInfo;
import com.aiba.haimaelc.model.CarInfo;
import com.aiba.haimaelc.model.SortCity;
import com.aiba.haimaelc.model.UserInfo;

public class SysModel {

    public static double latitude, longitude;//手机定位位置
    public static double searchStationLat, searchStationLng;//查找电桩中心位置
    public static SortCity currentCity = new SortCity();
    public static boolean isLogin = true;
    public static boolean isForceUpdate = false;
    public static int imageLoaderType;//ImageLoader类型，0:默认，1:加载sd卡缓存不进子线程

    public static UserInfo user;
    public static CarInfo car;
    public static CarGpsInfo carGps;
}
