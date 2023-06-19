package com.jxp.es.service;

import java.io.IOException;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-19 15:25
 */
@SpringBootTest
@Slf4j
class IndexServiceTest {

    @Resource
    IndexService indexService;

    @Test
    void addIndex() {
    }

    @Test
    void indexExists() throws IOException {
        boolean exists = indexService.indexExists("cosmo");
        log.info("exists:{}", exists);
    }

    @Test
    void delIndex() {
    }

    @Test
    void create() {
    }
}