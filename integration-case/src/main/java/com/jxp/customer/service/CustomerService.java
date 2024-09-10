package com.jxp.customer.service;

/**
 * @author jiaxiaopeng
 * Created on 2024-09-02 17:36
 */
public interface CustomerService {

    boolean manageSession(String appId, String askId, String messageKey,
            Long timeStamp);
}
