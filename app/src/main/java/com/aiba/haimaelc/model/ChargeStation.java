package com.aiba.haimaelc.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChargeStation implements Serializable {

    private static final long serialVersionUID = 1L;
    public String station_id = ""; //电站id
    public String station_name = ""; // 电站名
    public String station_address = ""; //电站地址
    public String latitude = "0"; //电站纬度
    public String longitude = "0"; //电站经度
    @SerializedName("online")
    public String station_online = "0"; //电站是否联网（0.未联网;1.已联网）
    @SerializedName("state")
    public String station_state = "0"; //电站状态（0.建设中;1.空闲;2.使用）
    @SerializedName("type")
    public String station_type = "0"; //电站类型（0.国标;1.特斯拉）
    public String open_time = ""; //开放时间
    public String score = "0"; //电站评分
    public String charge_cost = ""; //充电价格
    public String park_cost = ""; //停车价格
    public String open_outside = "0"; //0.对外开放,1.不开放
    public String charge_slow_num = ""; //慢充
    public String charge_slow_free_num = ""; //慢充空闲
    public String charge_fast_num = ""; //快充
    public String charge_fast_free_num = ""; //快充空闲
    public String surround = ""; //周边(1.咖啡,2.西餐,3.宾馆,4.饭店,5.超时,6.KTV)
    public String surround_describe = ""; //周边描述
}