package com.jxp.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    // 根据配置将excel数据生成在线doc文档，kconf的配置为md
    @PostMapping("/down-md")
    public ResponseEntity<InputStreamResource> downMd(
            @RequestParam(value = "docTitle", required = false) String docTitle,
            @RequestParam(value = "file") MultipartFile file) throws IOException {

        final String markdownContent = importService.excelToDoc(docTitle, file);
        InputStream inputStream = new ByteArrayInputStream(markdownContent.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=doc.md");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(inputStream));
    }
}
