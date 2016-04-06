package com.aiba.haimaelc.model;

import java.io.Serializable;

/**
 * Created by zhu on 16/3/25.
 */
public class AverageCost implements Serializable{
    private static final long serialVersionUID = 1L;
    public String name;
    public String averageElc;
    public String averageCost;

    public AverageCost(){

    }

    public AverageCost(String name, String averageElc, String averageCost){
        this.name = name;
        this.averageCost = averageCost;
        this.averageElc = averageElc;
    }

}
