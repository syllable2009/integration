package com.jxp.integration.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.jxp.event.publisher.EventPublisher;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Slf4j
class IntegrationTestApplicationTests {

    @Resource
    private EventPublisher eventPublisher;

    @Test
    void contextLoads() {
    }

    @Test
    void testSendMsg(){
        log.info("start publish->>>>>>>>>>>>>>>>>>");
        try {
            eventPublisher.publish("hello");
        } catch (Exception e) {
            log.error("err", e);
        }

//        eventPublisher.publish(new ApplicationEvent() {
//            @Override
//            public Object getSource() {
//                return "999";
//            }
//        });
    }
}
