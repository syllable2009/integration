package com.jxp.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Joiner;
import com.jxp.dto.DocMarkDownConfig;
import com.jxp.service.ImportService;
import com.jxp.util.TemplateUtil;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-08-22 17:39
 */
@Slf4j
@Service
public class ImportServiceImpl implements ImportService {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @SuppressWarnings("checkstyle:OperatorWrap")
    @Override
    public String excelToDoc(String docTitle, MultipartFile file) throws IOException {

        List<Map<String, Object>> dataMapList = analysisExcel(file);
        dataMapList = fillExcel(dataMapList);

        final List<String> contentList = Lists.newArrayList();
        contentList.add("<font color=\"#1f2329\"> **Q：${问题!}** </font>");
        contentList.add("<font color=\"#1f2329\"> **相似问：${相似问!}** </font>");
        contentList.add("<font color=\"#1f2329\"> **关键词：${关键词!}** </font>");
        contentList.add("<font color=\"#1f2329\">`${答案!}`</font>");


        final DocMarkDownConfig config = DocMarkDownConfig.builder()
                .title("# <font color=\"#171a1f\"><center>**新模板**</center></font>")
//                .header("## <font color=\"#171a1f\">PR模块</font>")
                .content(contentList)
                .segment("<font color=\"#1f2329\">=========================================================================</font><br>")
                .build();
        //
        // 生成文档
        String content = generateMdContent(config, dataMapList);
        // 导入文档
//        MockMultipartFile multipartFile = new MockMultipartFile("Filedata", StringUtils.left(docTitle, 150) + ".md"
//                , "APPLICATION_OCTET_STREAM", content.getBytes(Charset.forName("UTF-8")));
        return content;
    }

    private String generateMdContent(DocMarkDownConfig config, List<Map<String, Object>> dataList) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(config.getTitle())) {
            sb.append(config.getTitle());
        }
        if (StringUtils.isNotBlank(config.getHeader())) {
            sb.append(config.getHeader());
        }
        final List<String> contentList = config.getContent();
        final String segment = config.getSegment();
        dataList.forEach(e -> {
            final List<String> result = contentList.stream()
                    .map(c -> {
                                final String render = TemplateUtil.render(c, e);
                                return render.trim();
                            }
                    )
                    .collect(Collectors.toList());
            sb.append(LINE_SEPARATOR)
                    .append(Joiner.on(LINE_SEPARATOR).skipNulls().join(result));
            if (StringUtils.isNotBlank(segment)) {
                sb.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(segment);
            }
        });
        return sb.toString();
    }


    private static List<Map<String, Object>> analysisExcel(MultipartFile file) throws IOException {

        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        return reader.readAll();
    }

    private static List<Map<String, Object>> fillExcel(List<Map<String, Object>> dataList) throws IOException {
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        dataList.forEach(e -> {
            // 修改关键字
            if (null == e) {
                log.info("出现空值");
                return;
            }
            if (e.containsKey("关键词")) {
                final Object value = e.get("关键词");
                if (null != value) {
                    final String keyword = value.toString();
                    if (StrUtil.isNotBlank(keyword)) {
                        final List<String> split = StrUtil.split(keyword, ",");
                        e.put("关键词", Joiner.on("/").skipNulls().join(split));
                    }
                }
            }
            // 移除答案中的`
            if (e.containsKey("答案")) {
                final Object answer = e.get("答案");
                if (null != answer) {
                    String answerStr = answer.toString();
                    if (StringUtils.isNotBlank(answerStr)) {
                        answerStr = answerStr.replaceAll("`", "");
                        answerStr = removeEmptyLines(answerStr);
                        e.put("答案", answerStr);
                    }
                }
            }
            // 相似问
//            if (e.containsKey("相似问")) {
//                final String related = e.getOrDefault("相似问", "").toString();
//                final List<String> split = StrUtil.split(related, "|");
//                e.put("相似问", Joiner.on("/").skipNulls().join(split));
//            }
        });
        return dataList;
    }

    private static String removeEmptyLines(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        final List<String> split = StrUtil.split(str, "\n")
                .stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        return Joiner.on(LINE_SEPARATOR).skipNulls().join(split);
    }
}
