package com.jxp.integration.base.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lwt
 */
@Getter
@Setter
@SuppressWarnings("serial")
public class PspIntegrationException extends RuntimeException {

    private final String code;
    private final String msg;

    public PspIntegrationException(String code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public PspIntegrationException(IntegrationErrorCode integrationErrorCode) {
        super(integrationErrorCode.getDesc());
        this.code = String.valueOf(integrationErrorCode.getCode());
        this.msg = integrationErrorCode.getDesc();
    }

}
