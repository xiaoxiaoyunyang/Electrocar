package com.aiba.haimaelc.http;

import com.aiba.haimaelc.model.ApiInfo;

public class ApiList {

//    public final static String basicUrl = "http://101.200.233.117:8088/hmxny/";
    public final static String basicUrl = "http://172.16.30.112:8080/hmxny/";
    //用户信息
    public final static ApiInfo GetUserInfo = new ApiInfo("actor/ID", "GET");
    public final static ApiInfo UpdateUserInfo = new ApiInfo("actor/ID", "POST");
    //预约信息
    public final static ApiInfo GetReservation = new ApiInfo("reservation/ID", "GET");
    public final static ApiInfo GetReservationList = new ApiInfo("reservation", "GET");
    public final static ApiInfo ReservePile = new ApiInfo("reservation", "POST");
    public final static ApiInfo UpdateReservation = new ApiInfo("reservation/ID/status", "PUT");
    //救援信息
    public final static ApiInfo GetRescueInfo = new ApiInfo("rescues/ID", "GET");
    public final static ApiInfo GetRescueList = new ApiInfo("rescues", "GET");
    public final static ApiInfo Rescue = new ApiInfo("rescues", "POST");
    public final static ApiInfo UpdateRescueInfo = new ApiInfo("rescues/ID/status", "PUT");
    //充电信息
    public final static ApiInfo GetChargeInfo = new ApiInfo("charging/ID", "GET");
    public final static ApiInfo GetChargeList = new ApiInfo("charging", "GET");
    public final static ApiInfo Charge = new ApiInfo("charging", "POST");
    public final static ApiInfo UpdateChargeInfo = new ApiInfo("charging/ID/status", "PUT");
    //电站电桩
    public final static ApiInfo GetStationInfo = new ApiInfo("charging_station/ID", "GET");
    public final static ApiInfo GetStationList = new ApiInfo("charging_station", "GET");
}
