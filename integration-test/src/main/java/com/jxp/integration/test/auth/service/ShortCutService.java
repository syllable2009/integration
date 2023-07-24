package com.jxp.integration.test.auth.service;

import java.util.Collection;
import java.util.List;

import com.jxp.integration.test.auth.bean.ShortCut;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-11 15:31
 */
public interface ShortCutService {

    List<ShortCut> getByIds(Collection<String> ids);

    ShortCut getById(String id);

    List<ShortCut> getByTreeId(String treeId);

    List<ShortCut> getByParentIds(Collection<String> pids);

    List<ShortCut> getByParentId(String pid);

    List<ShortCut> getByCosmoIds(Collection<String> cids);

    ShortCut getByCosmoId(String cid);
}
