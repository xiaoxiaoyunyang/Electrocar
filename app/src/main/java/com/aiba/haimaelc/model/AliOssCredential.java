package com.aiba.haimaelc.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AliOssCredential implements Serializable {

    private static final long serialVersionUID = 1L;
    @SerializedName("AccessKeyId")
    public String accessKeyId = "";
    @SerializedName("AccessKeySecret")
    public String accessKeySecret = "";
    @SerializedName("SecurityToken")
    public String securityToken = "";
    @SerializedName("Expiration")
    public String expiration = "";
}
