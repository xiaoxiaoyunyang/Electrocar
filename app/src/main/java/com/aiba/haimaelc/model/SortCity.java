package com.aiba.haimaelc.model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SortCity implements Serializable {

    private static final long serialVersionUID = 1L;
    @SerializedName("cid")
    private String cityId = "";
    private String name = "";
    @SerializedName("pid")
    private String provinceId = "";
    @SerializedName("citykey")
    private String cityKey = "";
    @SerializedName("weathercode")
    private String weatherCode = "";

    public SortCity() {

    }

    public SortCity(String cityId, String cityKey, String provinceId, String name) {
        super();
        this.cityId = cityId;
        this.name = name;
        this.provinceId = provinceId;
//        this.cityKey = cityKey.substring(0, 1).toUpperCase();
        this.cityKey = cityKey;
    }

    @SuppressLint("DefaultLocale")
    public SortCity(String cityId, String cityKey, String provinceId,
                    String name, String weatherCode) {
        super();
        this.cityId = cityId;
        this.name = name;
        this.provinceId = provinceId;
//        this.cityKey = cityKey.substring(0, 1).toUpperCase();
        this.weatherCode = weatherCode;
        this.cityKey = cityKey;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    @Override
    public String toString() {
        return "SortCity{" +
                "cityId='" + cityId + '\'' +
                ", name='" + name + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", cityKey='" + cityKey + '\'' +
                ", weatherCode='" + weatherCode + '\'' +
                '}';
    }
}
