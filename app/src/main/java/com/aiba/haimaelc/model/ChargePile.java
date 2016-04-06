package com.aiba.haimaelc.model;

import java.io.Serializable;

public class ChargePile implements Serializable {

    private static final long serialVersionUID = 1L;
    public String station_id = ""; //电站id
    public String station_name = ""; // 电站名
    public String pile_id = ""; // 电桩id
    public String pile_name = ""; // 电桩名
    public String pile_state = "0";//状态（0.建设中;1.空闲;2.使用）
}