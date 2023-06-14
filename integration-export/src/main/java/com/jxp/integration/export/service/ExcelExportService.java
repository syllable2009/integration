package com.jxp.integration.export.service;

import java.io.OutputStream;
import java.util.Collection;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-14 17:45
 */
public interface ExcelExportService {
    void exportExcel(OutputStream outputStream, Class head, Collection dataList);
}
