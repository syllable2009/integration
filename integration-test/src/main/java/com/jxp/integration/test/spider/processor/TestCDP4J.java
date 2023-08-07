package com.jxp.integration.test.spider.processor;

import java.net.URL;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-04 11:45
 */

@Slf4j
public class TestCDP4J {

    public static void main(String[] args) throws Exception {
        // 这里是访问本地磁盘文件
        // 如果需要访问互联网地址，直接改为该地址就行了，例如访问百度主页https://www.baidu.com
        URL url = new URL("https://www.mojidoc.com/cb6882feeec64357bbe932edd4d9646f-00b");


        // 创建浏览器启动器
        Launcher launcher = new Launcher();

        try (SessionFactory factory = launcher.launch();
                Session session = factory.create()) {
            log.info("isHeadless:{}",factory.isHeadless());
            session.navigate(url.toString());
            session.waitDocumentReady();
            String content = session.getContent();
            log.info("result:{}",content);
        }

    }



}
