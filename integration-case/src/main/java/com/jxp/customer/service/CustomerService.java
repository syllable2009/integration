package com.jxp.customer.service;

import java.util.List;

/**
 * @author jiaxiaopeng
 * Created on 2024-09-02 17:36
 */
public interface CustomerService {

    boolean manageSession(String appId, String askId, String messageKey,
            Long timeStamp);

    // 分配客服：去掉相同的
    String distributeCustomer(String distributeCustomerStrategy, List<String> customerList);
}
