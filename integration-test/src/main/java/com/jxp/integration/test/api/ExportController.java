package com.jxp.integration.test.api;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.integration.export.service.ExcelExportService;
import com.jxp.integration.export.service.ExcelImportService;

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
    public ResponseEntity<String> testExport() {
        excelExportService.exportExcel();
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/import")
    public ResponseEntity<String> testImport() {
        excelImportService.importExcel();
        return ResponseEntity.ok("ok");
    }

}
