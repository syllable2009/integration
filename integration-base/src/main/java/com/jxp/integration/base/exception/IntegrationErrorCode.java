package com.jxp.integration.base.exception;

/**
 * @author lwt
 */
public enum IntegrationErrorCode {
    REQUEST_NO_RESPONSE(10000, "request_no_response", "请求三方服务无响应");

    private final int code;
    private final String desc;
    private final String descZh;

    IntegrationErrorCode(int code, String desc, String descZh) {
        this.code = code;
        this.desc = desc;
        this.descZh = descZh;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getDescZh() {
        return descZh;
    }
}
