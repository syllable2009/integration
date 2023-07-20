package com.jxp.integration.test.spider;

import java.net.URL;

import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 14:45
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
        String urlStr = "https://mp.weixin.qq.com/s/BSjdf09Zu9gLscMRhtkXOA";
        URL url = URLUtil.url(urlStr);
        url.getHost();
        log.info("{}",url.getHost());
        String title;
        String link;
        String description;
        String domain;
        String biztype;
        String bizid;
        String category; // 分类
        String aid;
        String id;
        String md5;
        String vector;
        String cover;
        Integer state;  // 0 初始申请状态 // 1处理完成 2 删除

    }
}
