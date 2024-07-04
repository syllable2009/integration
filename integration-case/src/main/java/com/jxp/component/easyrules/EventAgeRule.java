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
 */
@Slf4j
@Component
@Rule(name = "EventAgeRule", description = "偶数判断", priority = 1)
public class EventAgeRule {

    @Condition
    public boolean when(@Fact("age") int age) {
        log.info("EventAgeRule when");
        return age % 2 == 0;
    }

    @Action(order = 1)
    public void then(Facts facts) {
        log.info("EventAgeRule then");
        facts.put("result1", "偶数");
    }
}
