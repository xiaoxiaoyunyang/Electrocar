package com.aiba.haimaelc.model;

import java.io.Serializable;

public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    public String name = "";
    public String address = "";
    public double latitude = 0;
    public double longitude = 0;
    public boolean selected = false;
}
