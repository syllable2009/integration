package com.jxp.integration.base.valid;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-16 10:34
 */
public interface SignUpValidationService {
    ValidationResult validate(SignUpCommand command);
}
