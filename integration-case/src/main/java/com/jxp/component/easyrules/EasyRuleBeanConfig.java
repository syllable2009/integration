package com.jxp.component.easyrules;

import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-04 11:11
 */

@Configuration
public class EasyRuleBeanConfig {

    @Bean
    public RulesEngine rulesEngine() {
        return new DefaultRulesEngine();
    }
}
