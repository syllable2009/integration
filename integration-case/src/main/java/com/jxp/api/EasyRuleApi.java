package com.jxp.api;

import javax.annotation.Resource;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.support.composite.ActivationRuleGroup;
import org.jeasy.rules.support.composite.CompositeRule;
import org.jeasy.rules.support.composite.ConditionalRuleGroup;
import org.jeasy.rules.support.composite.UnitRuleGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.component.easyrules.AgeRule;
import com.jxp.component.easyrules.AgeRuleLow;
import com.jxp.component.easyrules.EventAgeRule;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-04 11:13
 */
@Slf4j
@RestController
@RequestMapping("/jeasy")
public class EasyRuleApi {

    @Resource
    private RulesEngine rulesEngine;
    @Resource
    private EventAgeRule eventAgeRule;
    @Resource
    private AgeRule ageRule;
    @Resource
    private AgeRuleLow ageRuleLow;

    @GetMapping("/age")
    public ResponseEntity<?> age(@RequestParam("age") Integer age) {
        Assert.notNull(age);
        final Facts facts = new Facts();
        facts.put("age", age);
        final Rules rules = new Rules();
        rules.register(eventAgeRule);
        rules.register(ageRule);
        rulesEngine.fire(rules, facts);
        return ResponseEntity.ok(facts);
    }


    @GetMapping("/group")
    public ResponseEntity<?> group(@RequestParam("age") Integer age,
            @RequestParam(value = "type", required = false) String type) {
        Assert.notNull(age);
        final Facts facts = new Facts();
        facts.put("age", age);
        final Rules rules = new Rules();

        // 支持的三种group
        // and
        UnitRuleGroup unitRuleGroup = new UnitRuleGroup();
        // or first
        ConditionalRuleGroup conditionalRuleGroup = new ConditionalRuleGroup();
        // or all
        ActivationRuleGroup activationRuleGroup = new ActivationRuleGroup();
        CompositeRule compositeRule = null;
        if (StrUtil.equals("unitRuleGroup", type)) {
            compositeRule = unitRuleGroup;
        } else if (StrUtil.equals("activationRuleGroup", type)) {
            compositeRule = activationRuleGroup;
        } else {
            compositeRule = conditionalRuleGroup;
        }
        compositeRule.addRule(eventAgeRule);
        compositeRule.addRule(ageRule);
        compositeRule.addRule(ageRuleLow);
        rules.register(compositeRule);
        rulesEngine.fire(rules, facts);
        return ResponseEntity.ok(facts);
    }
}
