package com.aiba.haimaelc.model;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    public String aid = "";
    public String name = ""; //姓名
    public String gender = ""; //姓别（0,男;1,女）
    public String nickname = ""; //昵称
    public String phone = "";//手机号
    public String imgsrc = "";//头像地址
}
