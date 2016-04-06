package com.aiba.haimaelc.model;

import java.io.Serializable;

public class CarGpsInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    public String carId = "";//车辆id
    public String latitude = "0";
    public String longitude = "0";
    public String endurance = "";//续航
    public String dumpEnergy = "";//剩余电量%

}
