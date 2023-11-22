package com.jxp.integration.test.spider.selector;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.selector.Selector;
import us.codecraft.webmagic.utils.Experimental;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-29 11:41
 */
@Slf4j
@Experimental
@Service
public class CustomSelector implements Selector {

    private static final int THRESHOLD = 86;
    private static final int BLOCK_WIDTH = 3;

    public CustomSelector() {
    }

    @Override
    public String select(String html) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> selectList(String html) {
        html = html.replaceAll("(?is)<!DOCTYPE.*?>", "");
        html = html.replaceAll("(?is)<!--.*?-->", "");                // remove html comment
        html = html.replaceAll("(?is)<script.*?>.*?</script>", ""); // remove javascript
        html = html.replaceAll("(?is)<style.*?>.*?</style>", "");   // remove css
        html = html.replaceAll("&.{2,5};|&#.{2,5};", " ");            // remove special char
        html = html.replaceAll("(?is)<.*?>", "");
        int start;
        int end;
        List<String> ret = Lists.newArrayList();
        List<Integer> indexDistribution = new ArrayList<Integer>();

        List<String> lines = Arrays.asList(html.split("\n"));

        for (int i = 0; i < lines.size() - BLOCK_WIDTH; i++) {
            int wordsNum = 0;
            for (int j = i; j < i + BLOCK_WIDTH; j++) {
                lines.set(j, lines.get(j).trim());
                wordsNum += lines.get(j).length();
            }
            indexDistribution.add(wordsNum);
        }

        start = -1;
        end = -1;
        boolean boolstart = false, boolend = false;

        for (int i = 0; i < indexDistribution.size() - 1; i++) {
            if (indexDistribution.get(i) > THRESHOLD && !boolstart) {
                if (indexDistribution.get(i + 1).intValue() != 0
                        || indexDistribution.get(i + 2).intValue() != 0
                        || indexDistribution.get(i + 3).intValue() != 0) {
                    boolstart = true;
                    start = i;
                    continue;
                }
            }
            if (boolstart) {
                if (indexDistribution.get(i).intValue() == 0
                        || indexDistribution.get(i + 1).intValue() == 0) {
                    end = i;
                    boolend = true;
                }
            }

            if (boolend) {
                for (int ii = start; ii <= end; ii++) {
                    if (lines.get(ii).length() < 5) {
                        continue;
                    }
                    if (lines.get(ii).contains("Copyright")) {
                        break;
                    }
                    ret.add(lines.get(ii));
                }
                boolstart = false;
                boolend = false;
            }
        }
        return ret;
    }
}
