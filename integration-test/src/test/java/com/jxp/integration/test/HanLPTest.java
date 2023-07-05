package com.jxp.integration.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.jxp.integration.test.main.WordVectorService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-05 10:42
 */
@Slf4j
public class HanLPTest {


    @Test
    void segment() {

        String str = "中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程";
//        List<Term> terms = HanLP.segment(str);
//        log.info("terms:{}", terms);

        // 标准分词
//        List<Term> termList = StandardTokenizer.segment(str);
//        log.info("标准分词:{}", termList);
//        // NLP分词
//        termList = NLPTokenizer.segment(str);
//        log.info("NLP分词:{}", termList);
//        // 索引分词
//        termList = IndexTokenizer.segment(str);
//        log.info("索引分词:{}", termList);
//        // 最短路径分词
//        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true)
//                .enableOrganizationRecognize(true);
//        Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(false).enablePlaceRecognize(true)
//                .enableOrganizationRecognize(true);
//        log.info("nShortSegment:{},shortestSegment:{}", nShortSegment.seg(str),shortestSegment.seg(str));
        // CRF分词
//        Segment segment = new CRFLexicalAnalyzer();
//        segment.enablePartOfSpeechTagging(true);
//        log.info("CRF分词:{}", segment.seg(str));
//        // 极速词典分词
//        log.info("极速词典分词:{}", SpeedTokenizer.segment(str));
        // 关键词提取
        List<String> keywordList = HanLP.extractKeyword(str, 50);
        log.info("关键词提取:{}",keywordList);
        WordVectorService wordVectorService = WordVectorService.getWordVectorComponentInstance();
        float[] wordVector = wordVectorService.getDocVector(keywordList);
        log.info("wordVector:{}",wordVector);

        // 语义距离
        long distance = CoreSynonymDictionary.distance("考试", "比赛");
        log.info("distance:{}", distance);
//        long distance2 = CoreSynonymDictionary.distance("公积金", "住房公积金");
//        log.info("distance2:{}", distance2);
        Vector what = wordVectorService.getDocVectorStr("中国人好");
        log.info("whatVec:{}", what);
        Vector with = wordVectorService.getDocVectorStr("中国人民好");
        log.info("withVec:{}", with);
        log.info("similarity:{}", what.cosineForUnitVector(with));


    }


    public static double getSimilarity(String sentence1, String sentence2) {

        List<String> sent1Words = getSplitWords(sentence1);

        System.out.println(sent1Words);

        List<String> sent2Words = getSplitWords(sentence2);

        System.out.println(sent2Words);

        List<String> allWords = mergeList(sent1Words, sent2Words);



        int[] statistic1 = statistic(allWords, sent1Words);

        int[] statistic2 = statistic(allWords, sent2Words);



        double dividend = 0;

        double divisor1 = 0;

        double divisor2 = 0;

        for (int i = 0; i < statistic1.length; i++) {

            dividend += statistic1[i] * statistic2[i];

            divisor1 += Math.pow(statistic1[i], 2);

            divisor2 += Math.pow(statistic2[i], 2);

        }

        return dividend / (Math.sqrt(divisor1) * Math.sqrt(divisor2));

    }


    private static int[] statistic(List<String> allWords, List<String> sentWords) {

        int[] result = new int[allWords.size()];

        for (int i = 0; i < allWords.size(); i++) {

            result[i] = Collections.frequency(sentWords, allWords.get(i));

        }

        return result;

    }



    private static List<String> mergeList(List<String> list1, List<String> list2) {

        List<String> result = new ArrayList<>();

        result.addAll(list1);

        result.addAll(list2);

        return result.stream().distinct().collect(Collectors.toList());

    }



    private static List<String> getSplitWords(String sentence) {

        // 去除掉 html 标签

//        sentence = Jsoup.parse(sentence.replace(" ","")).body().text();

        // 标点符号会被单独分为一个 Term ，去除之

        return HanLP.segment(sentence).stream().map(a -> a.word).filter(s -> !"`~!@#$^&*()=|{}':;',\\[\\].<>/?~ ！ @# ￥…… &* （）—— |{} 【】‘；：”“ ' 。，、？ ".contains(s)).collect(Collectors.toList());

    }


}
