package com.jxp.integration.test.spider;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 14:45
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
//        String urlStr = "https://mp.weixin.qq.com/s/BSjdf09Zu9gLscMRhtkXOA";
//        URL url = URLUtil.url(urlStr);
//        url.getHost();
//        log.info("{}",url.getHost());
//        String title;
//        String link;
//        String description;
//        String domain;
//        String biztype;
//        String bizid;
//        String category; // 分类
//        String aid;
//        String id;
//        String md5;
//        String vector;
//        String cover;
//        Integer state;  // 0 初始申请状态 // 1处理完成 2 删除

        String str =
                "_did=web_35512701325345A6; ksCorpDeviceid=d33d31d0-d6e4-41dd-9840-32b1cb60a43e; "
                        + "hdige2wqwoino=GPr7AfktnrCh3NZrN7te6wSctAe6zX7R084e139b; "
                        + "did=web_b0b09f485cdbba02cc31ba7871dcf1359720; "
                        + "Hm_lvt_86a27b7db2c5c0ae37fee4a8a35033ee=1677650476; _ga_VKXBFL78SD=GS1.1.1678333927.1.1"
                        + ".1678333945.42.0.0; intercom-device-id-pjrf3upr=81a2f76b-6b30-4c72-9dc2-b7023170abc6; "
                        + "logged_out_marketing_header_id"
                        +
                        "=eyJfcmFpbHMiOnsibWVzc2FnZSI6IkltTXhZemd5TnpBd0xUZGtPR1l0TkRBd05pMDRabUppTFRWbU1USXlNRGN4TkRKallpST0iLCJleHAiOm51bGwsInB1ciI6ImNvb2tpZS5sb2dnZWRfb3V0X21hcmtldGluZ19oZWFkZXJfaWQifX0%3D--dcc568a3031a2656a358922ad27c8e29f93fcc80; apdid=686ab5e0-f6cb-4c7d-bd2d-eb8bb27baf317567b145a767badb03931f8b6013184c:1687682245:1; accessproxy_session=80d98724-29e6-4e50-8268-7a4624d638f4; didv=1689240364810; userSource=others; _gid=GA1.2.874043532.1691048681; k-token=a07f37b10c38491e79672c29361be4cf; _ga=GA1.2.2027780773.1664422375; _ga_F6CM1VE30P=GS1.1.1691048681.4.1.1691048760.0.0.0";

        List<String> split = StrUtil.split(str, ";");
        split.forEach(e ->
        {
            List<String> split1 = StrUtil.split(e, "=");
            log.info("e:{}", split1.get(0), split1.get(1));
        });


    }
}
