package com.jxp.integration.base.valid;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cn.hutool.core.collection.CollectionUtil;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-16 11:02
 */
public class ValidationStepExcutor<T> {

    private List<ValidationStep<T>> stepList = new ArrayList<>();

    public ValidationStepExcutor<T> linkWith(ValidationStep<T> next) {
        stepList.add(next);
        return this;
    }

    // 如果没有错误返回null
    public ValidationResult checkAllWhenErr() {
        if (CollectionUtil.isEmpty(stepList)) {
            return null;
        }
        ValidationResult validate = null;
        for (ValidationStep vs : stepList) {
            validate = vs.validate();
            if (validate.notValid()) {
                return validate;
            }
        }
        return null;
    }

    public List<ValidationResult> checkAll() {
        List<ValidationResult> ret = Lists.newArrayList();
        if (CollectionUtil.isEmpty(stepList)) {
            return ret;
        }
        stepList.forEach(e -> {
            ValidationResult validate = e.validate();
            if (validate.notValid()) {
                ret.add(validate);
            }
        });
        return ret;
    }
}
