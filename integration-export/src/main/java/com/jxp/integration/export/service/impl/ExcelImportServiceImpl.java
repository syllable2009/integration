package com.jxp.integration.export.service.impl;

import java.io.InputStream;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.jxp.integration.export.service.ExcelImportService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-14 17:51
 */

@Slf4j
public class ExcelImportServiceImpl implements ExcelImportService {
    @Override
    public void importExcel(InputStream in, Class head, ReadListener listener) {
        log.info("ExcelImportService import");

        EasyExcel.read(in, head, listener).sheet()
                // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
                .headRowNumber(1).doRead();
    }
}
