package com.jxp.integration.base.valid;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Value;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-16 10:27
 */
@Value
public class SignUpCommand {
    @Min(2)
    @Max(40)
    @NotBlank
    private final String firstName;

    @Min(2)
    @Max(40)
    @NotBlank
    private final String lastName;

    @Min(2)
    @Max(40)
    @NotBlank
    private final String username;

    @NotBlank
    @Size(max = 60)
    @Email
    private final String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private final String rawPassword;
}
