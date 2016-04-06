package com.aiba.haimaelc.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChargeOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    public String id = "";//充电id
    public String order_time = "";//充电开始时间
    @SerializedName("charge_station_id")
    public String station_id = ""; //电站id
    @SerializedName("stationName")
    public String station_name = "";//电站名
    public String station_address = "";//电站地址
    @SerializedName("charge_pile_id")
    public String pile_id = ""; // 电桩id
    @SerializedName("pileName")
    public String pile_name = ""; // 电桩名
    public String finish_time = "";//充电完成时间
    @SerializedName("state")
    public String order_state = "0";//0.充电中;1.充电完成
    public String cost = "";//充电费用
}