package com.jxp.api;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jxp.service.ImportService;

/**
 * @author jiaxiaopeng
 * Created on 2024-08-22 17:34
 */

@RestController
public class ImportController {

    @Resource
    private ImportService importService;

    // 根据配置将excel数据生成在线doc文档，kconf的配置为md
    @PostMapping("/excelToDoc")
    public ResponseEntity<String> excelToDoc(
            @RequestParam(value = "docTitle", required = false) String docTitle,
            @RequestParam(value = "file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(importService.excelToDoc(docTitle, file));
    }
}
