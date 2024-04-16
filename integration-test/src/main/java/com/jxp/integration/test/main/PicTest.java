package com.jxp.integration.test.main;

import org.apache.commons.lang3.StringUtils;

import com.jxp.integration.test.model.DemoData;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-12-28 15:15
 */

@Slf4j
@Data
public class PicTest {

    private DemoData demoData;

    public static final String COVER_PREFIX = "/";

    public static void main(String[] args) {
        String str =
                "https://ssss.com/bs2/docsfile/spider/abcc32525bda45e1b10529bb6830dc5b?x-imaginary"
                        + "-from=VOcjc+5AmminY+m71tGVzzalrzGzXX70eQ==&x-space-id=100000005";
        String split2 = StringUtils.substringAfterLast(str, COVER_PREFIX);
        log.info("split2:{}", split2);

        String split3 = StringUtils.substringBefore("avc", "?");
        log.info("split4:{}", split3);


        PicTest picTest = new PicTest();
        picTest.test();
    }


    public void test() {
        demoData = new DemoData();
        log.info("1:{}", this.demoData);
        demoData = null;
        log.info("2:{}", this.getDemoData());
        this.setDemoData(null);
        log.info("3:{}", this.getDemoData());
    }
}
