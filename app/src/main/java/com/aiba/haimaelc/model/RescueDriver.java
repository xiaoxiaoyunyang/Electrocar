package com.aiba.haimaelc.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RescueDriver implements Serializable {

    private static final long serialVersionUID = 1L;
    public String latitude = "0";
    public String longitude = "0";
    public String name = "";//救援司机姓名
    public String driver_photo = ""; //救援司机头像
    @SerializedName("plate")
    public String plate_number = ""; //救援车牌照
    public String phone = ""; //救援司机电话
    @SerializedName("score")
    public String score = "0"; //救援司机打分
}
