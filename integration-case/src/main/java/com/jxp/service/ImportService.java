package com.jxp.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author jiaxiaopeng
 * Created on 2024-08-22 17:37
 */
public interface ImportService {

    String excelToDoc(String docTitle, MultipartFile file) throws IOException;
}
