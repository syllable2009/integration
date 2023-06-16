package com.jxp.integration.base.valid.impl;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.jxp.integration.base.valid.SignUpCommand;
import com.jxp.integration.base.valid.SignUpValidationService;
import com.jxp.integration.base.valid.ValidationResult;
import com.jxp.integration.base.valid.ValidationStep;
import com.jxp.integration.base.valid.ValidationStepExcutor;

import lombok.AllArgsConstructor;

// 验证同一个对象的方法放到一个类中实现，可以自动放到spring容器中
@AllArgsConstructor
public class DefaultSignUpValidationService implements SignUpValidationService {

    //    @Resource
    //    private final UserRepository userRepository;

    @Override
    public ValidationResult validate(SignUpCommand command) {

        // 这里可以统一查询结果，然后分发到各自的验证器中,也可以构造各个验证器的参数
        return new ValidationStepExcutor()
                .linkWith(new CommandConstraintsValidationStep(command))
                //                .linkWith(new UsernameDuplicationValidationStep(command, userRepository))
                //                .linkWith(new EmailDuplicationValidationStep(command, userRepository))
                .checkAllWhenErr();
    }

    @AllArgsConstructor
    private static class CommandConstraintsValidationStep implements ValidationStep<SignUpCommand> {

        private final SignUpCommand toValidate;

        @Override
        public ValidationResult validate() {
            try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
                final Validator validator = validatorFactory.getValidator();
                final Set<ConstraintViolation<SignUpCommand>> constraintsViolations = validator.validate(toValidate);
                if (!constraintsViolations.isEmpty()) {
                    return ValidationResult.invalid(constraintsViolations.iterator().next().getMessage());
                }
            }
            return ValidationResult.valid();
        }
    }

    @AllArgsConstructor
    private static class UsernameDuplicationValidationStep implements ValidationStep<SignUpCommand> {

        private final SignUpCommand toValidate;
        //        private final UserRepository userRepository;


        @Override
        public ValidationResult validate() {
            //            if (userRepository.findByUsername(toValidate.getUsername()).isPresent()) {
            //                return ValidationResult.invalid(String.format("Username [%s] is already taken", command
            //                .getUsername()));
            //            }
            return ValidationResult.valid();
        }

    }

    @AllArgsConstructor
    private static class EmailDuplicationValidationStep implements ValidationStep<SignUpCommand> {

        private final SignUpCommand toValidate;
        //        private final UserRepository userRepository;

        @Override
        public ValidationResult validate() {
            //            if (userRepository.findByEmail(command.getEmail()).isPresent()) {
            //                return ValidationResult.invalid(String.format("Email [%s] is already taken", command.getEmail()));
            //            }
            return ValidationResult.valid();
        }
    }
}
