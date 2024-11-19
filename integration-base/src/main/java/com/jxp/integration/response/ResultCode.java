package com.jxp.integration.response;

/**
 * @author jiaxiaopeng
 * Created on 2024-11-19 10:58
 */
public interface ResultCode {

    @ExceptionCode(
            code = 0,
            message = "OK",
            zhCN = "成功",
            enUS = "ok"
    )
    int OK = 0;
}
