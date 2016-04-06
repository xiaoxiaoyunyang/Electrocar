package com.aiba.haimaelc.model;

import java.io.Serializable;

/**
 * Created by zhu on 16/3/25.
 */
public class WeekAverageCost implements Serializable{
    private static final long serialVersionUID = 1L;
    public String date;
    public String distance;
    public String elc;
    public String cost;


    public WeekAverageCost(){

    }

    public WeekAverageCost(String date, String distance, String elc ,String cost){
        this.date = date;
        this.distance = distance;
        this.elc = elc;
        this.cost = cost;
    }

}
