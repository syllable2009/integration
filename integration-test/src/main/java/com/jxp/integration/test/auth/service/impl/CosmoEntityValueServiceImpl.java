package com.jxp.integration.test.auth.service.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import com.jxp.integration.test.auth.bean.CosmoEntityValue;
import com.jxp.integration.test.auth.service.CosmoEntityValueService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-11 14:33
 */
@Slf4j
@Service
public class CosmoEntityValueServiceImpl implements CosmoEntityValueService {

    @Override
    public List<CosmoEntityValue> getValueByCosmoId(String cosmoId) {
        List<CosmoEntityValue> ret = Lists.newArrayList();
//        Mockito.mock();
        return ret;
    }

    @Override
    public List<CosmoEntityValue> getValueByCosmoIds(Collection<String> cosmoIds) {
        List<CosmoEntityValue> ret = Lists.newArrayList();
        return ret;
    }
}
