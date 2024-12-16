package com.jxp.integration.base.exception;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jxp
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

    private static final Map<Integer, IntegrationErrorCode> CODE_MAP =
            Arrays.stream(IntegrationErrorCode.values())
                    .collect(Collectors.toMap(IntegrationErrorCode::getCode, Function.identity()));

    public static IntegrationErrorCode of(String code) {
        return CODE_MAP.get(code);
    }
}
