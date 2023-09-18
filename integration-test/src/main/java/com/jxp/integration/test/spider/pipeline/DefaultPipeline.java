package com.jxp.integration.test.spider.pipeline;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 16:16
 */
@Service
@Slf4j
public class DefaultPipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
    }
}
