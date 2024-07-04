package com.jxp.component.liteflow.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-04 15:38
 */
public class PriceContext {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 是否境外购
     */
    private boolean oversea;

    /**
     * 商品包
     */
    private List<String> productPackList;

    /**
     * 订单渠道
     */
    private String orderChannel;

    /**
     * 会员CODE
     */
    private String memberCode;

    /**
     * 优惠券
     */
    private Long couponId;

    /**
     * 优惠信息
     */
    private List<String> promotionPackList;

    /**
     * 价格步骤
     */
    private List<String> priceStepList = new ArrayList<>();

    /**
     * 订单原始价格
     */
    private BigDecimal originalOrderPrice;

    /**
     * 订单最终价格
     */
    private BigDecimal finalOrderPrice;

    /**
     * 步骤日志
     */
    private String printLog;
}
