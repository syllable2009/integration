package com.jxp.integration.test.spider.pipeline;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 16:35
 */
@Slf4j
public class PeopleNotExistPipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> all = resultItems.getAll();
        log.info("all:{}",all);
    }
}
