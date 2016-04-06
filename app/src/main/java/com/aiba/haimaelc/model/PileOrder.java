package com.aiba.haimaelc.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PileOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    public String id = "";//预约id
    @SerializedName("orderDate")
    public String order_time = "";//预约发起时间
    @SerializedName("orderOverDate")
    public String over_time = "";//预约过期时间
    @SerializedName("stationId")
    public String station_id = ""; //电站id
    @SerializedName("stationName")
    public String station_name = "";//电站名
    public String station_address = "";//电站地址
    @SerializedName("pileId")
    public String pile_id = ""; // 电桩id
    @SerializedName("pileName")
    public String pile_name = ""; // 电桩名
    @SerializedName("state")
    public String order_state = "0"; // 预约状态0.成功;1.完成;2.过期;3.失败
    public String cost = "0"; // 预约费用
}