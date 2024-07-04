package com.jxp.component.liteflow.cmp;

import org.springframework.stereotype.Component;

import com.yomahub.liteflow.core.NodeSwitchComponent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-04 15:35
 */
@Slf4j
@Component("p")
public class PostageCondCmp extends NodeSwitchComponent {
    @Override
    public String processSwitch() throws Exception {
        log.info("p");
        return "p";
    }
}
