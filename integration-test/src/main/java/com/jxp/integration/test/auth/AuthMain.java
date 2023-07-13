package com.jxp.integration.test.auth;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.primitives.Longs;
import com.jxp.integration.test.auth.service.ApiService;
import com.jxp.integration.test.auth.service.CosmoEntityValueService;
import com.jxp.integration.test.auth.service.CosmoService;
import com.jxp.integration.test.auth.service.ShortCutService;
import com.jxp.integration.test.auth.service.impl.ApiServiceImpl;
import com.jxp.integration.test.auth.service.impl.CosmoEntityValueServiceImpl;
import com.jxp.integration.test.auth.service.impl.CosmoServiceImpl;
import com.jxp.integration.test.auth.service.impl.ShortCutServiceImpl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-11 11:44
 */

@Slf4j
public class AuthMain {

    private static CosmoEntityValueService cosmoEntityValueService = new CosmoEntityValueServiceImpl();
    private static ShortCutService shortCutService = new ShortCutServiceImpl();
    private static CosmoService cosmoService = new CosmoServiceImpl();
    private static ApiService apiService = new ApiServiceImpl();

    public static void main(String[] args) {
        long viewId = stringToViewId("VPYEDr9b8Jk0");
        log.info("viewId:{}", viewId);
    }


    public static void create() {
        // 创建一个节点
        // 是否创建一个cosmo
    }

    public static long stringToViewId(String v) {
        if (StrUtil.isBlank(v)) {
            return 0;
        }
        return v.charAt(0) == 'V' ? Longs.fromByteArray(java.util.Base64.getUrlDecoder().decode(v.substring(1)))
                                  : NumberUtils.toLong(v);
    }
}
