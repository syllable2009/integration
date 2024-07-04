package com.jxp.component.liteflow.cmp;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-04 17:24
 */

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@LiteflowComponent
public class CmpBean {

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "checkCmp", nodeType =
            NodeTypeEnum.COMMON)
    public void processCheckCmp(NodeComponent bindCmp) {
        //拿到请求参数
        final String requestData = bindCmp.getSlot().getRequestData();
        log.info("checkCmp,requestData:{}", requestData);
        //参数验证完成
    }
}
