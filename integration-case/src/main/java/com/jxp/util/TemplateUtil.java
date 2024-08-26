package com.jxp.util;

/**
 * @author jiaxiaopeng
 * Created on 2024-08-22 17:44
 */

import java.io.StringWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * @author jiaxiaopeng
 * Created on 2024-08-22 17:17
 */
@UtilityClass
public class TemplateUtil {

    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_28);

    @SneakyThrows
    public static String render(String template, Object variable) {
        try (StringWriter stringWriter = new StringWriter()) {
            Template t = new Template("ddd", template, CONFIGURATION);
            t.setNumberFormat("0.##########");
            t.process(variable, stringWriter);
            return stringWriter.toString();
        }
    }

}
