package com.jxp.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Joiner;
import com.jxp.dto.DocMarkDownConfig;
import com.jxp.service.ImportService;
import com.jxp.util.TemplateUtil;

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

        final List<Map<String, Object>> dataMapList = analysisExcel(file);


        final List<String> contentList = Lists.newArrayList();
        contentList.add("<font color=\"#1f2329\"> **Q：${问题内容!}** </font>");
        contentList.add("<font color=\"#1f2329\"> **相似问：${相似问!}** </font>");
        contentList.add("<font color=\"#1f2329\">```${答案内容!}```</font>");
        contentList.add("<font color=\"#1f2329\">还有其他相似问题：${相似问!}</font>");

        final DocMarkDownConfig config = DocMarkDownConfig.builder()
                .title("# <font color=\"#171a1f\"><center>**AI机器人知识库**</center></font>")
                .header("## <font color=\"#171a1f\">PR模块</font>")
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
            sb.append(config.getTitle()).append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
        }
        if (StringUtils.isNotBlank(config.getHeader())) {
            sb.append(config.getHeader()).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
        }
        final List<String> contentList = config.getContent();
        final String segment = config.getSegment();
        dataList.forEach(e -> {
            final List<String> result = contentList.stream()
                    .map(c -> TemplateUtil.render(c, e))
                    .collect(Collectors.toList());
            sb.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(LINE_SEPARATOR)
                    .append(Joiner.on(LINE_SEPARATOR).join(result));
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
}
