package com.aiba.haimaelc.model;

import com.aiba.haimaelc.http.ApiList;

public class ApiInfo {

    private String url;
    private String method;

    public ApiInfo(String url, String method) {
        this.url = ApiList.basicUrl + url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }
}
