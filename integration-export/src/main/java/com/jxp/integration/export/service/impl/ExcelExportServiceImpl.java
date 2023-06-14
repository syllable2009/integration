package com.jxp.integration.export.service.impl;

import java.io.OutputStream;
import java.util.Collection;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.jxp.integration.export.service.ExcelExportService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-14 17:51
 */

@Slf4j
public class ExcelExportServiceImpl implements ExcelExportService {

    @Override
    public void exportExcel(OutputStream outputStream, Class head, Collection dataList) {
        log.info("ExcelExportService export");
        ExcelWriter write = EasyExcel.write(outputStream, head)
                .build();
        WriteSheet sheet = EasyExcel.writerSheet().build();
        //        write.fill(dataList, sheet);
        write.write(dataList, sheet);
        write.finish();
        //        EasyExcel.write(fileName, ConverterData.class).sheet("模板").doWrite(data());
    }

}
