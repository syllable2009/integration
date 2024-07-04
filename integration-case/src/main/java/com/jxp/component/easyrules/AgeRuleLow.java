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
@Rule(name = "AgeRuleLow", description = "奇数判断", priority = 0)
public class AgeRuleLow {

    @Condition
    public boolean when(@Fact("age") int age) {
        log.info("AgeRuleLow when");
        return age % 2 != 0;
    }

    @Action(order = 1)
    public void then(Facts facts) {
        log.info("AgeRuleLow then");
        facts.put("result-1", "奇数");
    }
}
