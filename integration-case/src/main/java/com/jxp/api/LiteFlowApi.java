package com.jxp.api;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.slot.DefaultContext;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-04 11:13
 */
@Slf4j
@RestController
@RequestMapping("/liteflow")
public class LiteFlowApi {

    @Resource
    private FlowExecutor flowExecutor;

    @GetMapping("/test1")
    public ResponseEntity<?> test1(@RequestParam("chain") String chain) {
        Assert.notNull(chain);
        final DefaultContext defaultContext = new DefaultContext();
        final LiteflowResponse liteflowResponse = flowExecutor.execute2Resp(chain, IdUtil.fastSimpleUUID(), defaultContext);
        final DefaultContext contextBean = liteflowResponse.getContextBean(DefaultContext.class);
        return ResponseEntity.ok(contextBean.getData("a"));
    }

    @GetMapping("/refresh")
    public ResponseEntity<Boolean> refresh() {
        flowExecutor.reloadRule();
        return ResponseEntity.ok(true);
    }

}
