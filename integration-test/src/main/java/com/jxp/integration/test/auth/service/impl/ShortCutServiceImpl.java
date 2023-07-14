package com.jxp.integration.test.auth.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jxp.integration.test.auth.bean.ShortCut;
import com.jxp.integration.test.auth.service.ShortCutService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-11 15:31
 */
@Service
@Slf4j
public class ShortCutServiceImpl implements ShortCutService {
    @Override
    public List<ShortCut> getByIds(Collection<String> ids) {
        return null;
    }

    @Override
    public ShortCut getById(String id) {
        return null;
    }

    @Override
    public List<ShortCut> getByParentIds(Collection<String> pids) {
        return null;
    }

    @Override
    public List<ShortCut> getByParentId(String pid) {
        return null;
    }

    @Override
    public List<ShortCut> getByCosmoIds(Collection<String> cids) {
        return null;
    }

    @Override
    public ShortCut getByCosmoId(String cid) {
        return null;
    }
}
