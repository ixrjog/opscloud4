package com.baiyi.opscloud.jumpserver.base;

public enum ApiType {

    API_GET("get"),

    API_POST("post"),

    API_PUT("put"),

    API_DELETE("delete"),

    API_PATCH("patch"),

    API_TOKEN("token");

    private String type;

    ApiType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
