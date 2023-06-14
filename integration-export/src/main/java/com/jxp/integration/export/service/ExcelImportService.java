package com.jxp.integration.export.service;

import java.io.InputStream;

import com.alibaba.excel.read.listener.ReadListener;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-14 17:45
 */
public interface ExcelImportService {
    void importExcel(InputStream in, Class head, ReadListener listener);
}
