package com.jxp.integration.test.main;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.seg.common.Term;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-05 11:26
 */
@Slf4j
public class WordVectorService {

    private static final String WORD_VECTOR_MODEL = "/model/hanlp-wiki-vec-zh.txt";
    private WordVectorModel wordVectorModel;
    private static WordVectorService wordVectorComponent;

    private WordVectorService() {
        try {
            String savePath = System.getProperty("java.io.tmpdir") + File.separator + "hanlp-wiki-vec-zh.txt";
            log.info("savepath:{}",savePath);
            File localWordVectorModel = new File(savePath);
            if (!localWordVectorModel.exists()) {
                FileUtils.copyURLToFile(new URL(WORD_VECTOR_MODEL), localWordVectorModel);
            }
            wordVectorModel = new WordVectorModel(savePath);
        } catch (Exception e) {
            log.error("load vector model err", e);
        }
    }

    public WordVectorModel getWordVectorModel() {
        return wordVectorModel;
    }

    public float[] getDocVector(Collection<String> terms) {
        Vector result = getDocVectorObj(terms);
        return result.getElementArray();
    }

    public float[] getWordVector(String term) {
        Vector vector = wordVectorModel.vector(term.trim());
        if (vector == null) {
            return null;
        }
        return vector.getElementArray();
    }

    public Vector getDocVectorObj(Collection<String> terms) {
        Vector result = new Vector(wordVectorModel.dimension());
        int n = 0;
        for (String term : terms) {
            Vector vector = wordVectorModel.vector(term.trim());
            if (vector == null) {
                continue;
            }
            ++n;
            result.addToSelf(vector);
        }
        if (n == 0) {
            return result;
        }
        result.normalize();
        return result;
    }

    public Vector getDocVectorStr(String str) {
        List<Term> segment = HanLP.segment(str);
        List<String> collect = segment.stream()
                .map(e -> e.word)
                .collect(Collectors.toList());
        return this.getDocVectorObj(collect);

    }

    public static synchronized WordVectorService getWordVectorComponentInstance() {
        if (Objects.isNull(wordVectorComponent)) {
            wordVectorComponent = new WordVectorService();
        }
        return wordVectorComponent;
    }
}
