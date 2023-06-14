package com.jxp.integration.export.service.impl;

import com.jxp.integration.export.service.ExcelExportService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-14 17:51
 */

@Slf4j
public class ExcelExportServiceImpl implements ExcelExportService {

    @Override
    public void exportExcel() {
        log.info("ExcelExportService export");
    }

}
