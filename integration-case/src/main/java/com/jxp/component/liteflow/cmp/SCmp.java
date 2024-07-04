package com.jxp.component.liteflow.cmp;

import org.springframework.stereotype.Component;

import com.yomahub.liteflow.core.NodeSwitchComponent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-04 15:34
 */
@Component("s")
@Slf4j
public class SCmp extends NodeSwitchComponent {

    @Override
    public String processSwitch() throws Exception {
        log.info("s");
        return "a";
    }
}
