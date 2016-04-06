package com.aiba.haimaelc.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RescueOrder implements Serializable {

    private static final long serialVersionUID = 1L;@SerializedName("id")
    public String order_id = "";//救援订单id
    @SerializedName("rescue_number")
    public String order_num = "";//救援订单编号
    @SerializedName("name")
    public String contact_name = "";//联系人
    @SerializedName("phone")
    public String contact_phone = "";//联系电话
    @SerializedName("plate")
    public String plate_number = ""; //车牌
    @SerializedName("position")
    public String order_address = "";//救援地址
    public String latitude = "0";
    public String longitude = "0";
    @SerializedName("start_time")
    public String order_time = ""; //提交订单时间
    @SerializedName("state")
    public String order_state = "0"; //救援状态0.发起;1.在途;2.充电;3.完成;4.失败
    public String finish_time = ""; //订单完成时间
    public String charge_start_time = ""; //充电开始时间
    public String charge_finish_time = ""; //充电完成时间
    @SerializedName("chargingkwh")
    public String charge_quantity = ""; //充电电量
    public String charge_cost = ""; //充电费用
    public String rescue_cost = ""; //救援费用
    @SerializedName("rescueVehicle")
    public RescueDriver rescueDriver = new RescueDriver();
}