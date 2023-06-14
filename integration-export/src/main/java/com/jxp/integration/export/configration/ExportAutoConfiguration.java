package com.jxp.integration.export.configration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jxp.integration.export.service.ExcelExportService;
import com.jxp.integration.export.service.ExcelImportService;
import com.jxp.integration.export.service.impl.ExcelExportServiceImpl;
import com.jxp.integration.export.service.impl.ExcelImportServiceImpl;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-14 17:30
 */
@Configuration
@ConditionalOnProperty(prefix = "integration-export", name = "enable", havingValue = "true")
public class ExportAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ExcelExportService excelExportService() {
        return new ExcelExportServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExcelImportService excelImportService() {
        return new ExcelImportServiceImpl();
    }
}
