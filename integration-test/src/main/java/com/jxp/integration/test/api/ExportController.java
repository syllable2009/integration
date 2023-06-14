package com.jxp.integration.test.api;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jxp.integration.export.service.ExcelExportService;
import com.jxp.integration.export.service.ExcelImportService;
import com.jxp.integration.test.model.IndexData;
import com.jxp.integration.test.model.IndexDataListener;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-14 19:22
 */
@Slf4j
@RestController
@RequestMapping("/api/excel")
public class ExportController {

    @Resource
    ExcelExportService excelExportService;
    @Resource
    ExcelImportService excelImportService;

    @GetMapping("/export")
    public String testExport(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("试验.xlsx", "UTF-8"));

        excelExportService.exportExcel(response.getOutputStream(), IndexData.class, Lists.newArrayList
                (IndexData.builder()
                        .string(IdUtil.fastSimpleUUID())
                        .date(new Date())
                        .doubleData(9D)
                        .build()));
        // 返回null避免多次获取outwriter
        return null;
    }

    @PostMapping("/import")
    public ResponseEntity<Boolean> testImport(@RequestParam(value = "file") MultipartFile file) throws IOException {
        excelImportService.importExcel(file.getInputStream(), IndexData.class, new IndexDataListener());
        return ResponseEntity.ok(true);
    }


}
