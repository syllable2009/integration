package com.jxp.integration.base.valid;

import lombok.Value;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-16 10:23
 */

@Value
public class ValidationResult {

    private final boolean isValid;
    private final String errorMsg;

    public static ValidationResult valid() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult invalid(String errorMsg) {
        return new ValidationResult(false, errorMsg);
    }

    public boolean notValid() {
        return !isValid;
    }
}
