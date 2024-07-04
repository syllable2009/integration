package com.jxp.component.liteflow.cmp;

import org.springframework.stereotype.Component;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.slot.DefaultContext;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-04 15:34
 */
@Component("b")
@Slf4j
public class BCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        log.info("b");
        final DefaultContext contextBean = this.getContextBean(DefaultContext.class);
        contextBean.setData("b", IdUtil.fastSimpleUUID());
    }

}
