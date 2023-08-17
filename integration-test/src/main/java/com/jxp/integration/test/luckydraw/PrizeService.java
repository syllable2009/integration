package com.jxp.integration.test.luckydraw;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-17 11:01
 */

@Slf4j
public class PrizeService {

    @Resource
    private RedisListTools<String> listTools;

    /**
     * 累计的key
     */
    public static final String PRIZE_COUNT = "PRIZE_COUNT";

    /**
     * 前面200的数据存储集合
     */
    public static final String PRIZE_COUNT_200 = "PRIZE_COUNT_200";

    /**
     * 前面201-400的数据存储集合
     */
    public static final String PRIZE_COUNT_400 = "PRIZE_COUNT_400";

    /**
     * 前面401-570的数据存储集合
     */
    public static final String PRIZE_COUNT_570= "PRIZE_COUNT_570";

    public static void main(String[] args) {


    }

    public void luckDraw(){
        // 活动时间有效性判断
        // 判断该用户是否已经中奖
        // 是否可以重复抽奖配置
        // 分布式数据需要再redis中初始化，通过redis拿到当前的prize_count的大小
        Long prizeCount = listTools.increCount(PRIZE_COUNT);

    }
}
