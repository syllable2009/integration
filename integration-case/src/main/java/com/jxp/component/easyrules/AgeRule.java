package com.jxp.component.easyrules;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-04 11:22
 * priority值越低，优先级越高
 */
@Slf4j
@Component
@Rule(name = "AgeRule", description = "奇数判断", priority = 2)
public class AgeRule {

    @Condition
    public boolean when(@Fact("age") int age) {
        log.info("AgeRule when");
        return age % 2 != 0;
    }

    @Action(order = 1)
    public void then(Facts facts) {
        log.info("AgeRule then1");
        facts.put("result0", "奇数");
    }

    @Action(order = 2)
    public void then2(Facts facts) {
        log.info("AgeRule then2");
        facts.put("result2", "奇数");
    }
}
