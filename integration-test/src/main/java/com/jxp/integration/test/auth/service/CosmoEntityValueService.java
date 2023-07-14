package com.jxp.integration.test.auth.service;

import java.util.Collection;
import java.util.List;

import com.jxp.integration.test.auth.bean.CosmoEntityValue;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-11 14:33
 */
public interface CosmoEntityValueService {

    List<CosmoEntityValue> getValue(String cosmoId, Collection<String> entityIds);

    List<CosmoEntityValue> getValueList(Collection<String> cosmoIds, Collection<String> entityIds);

}
