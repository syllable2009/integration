package com.jxp.integration.test.api;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.icepear.echarts.Bar;
import org.icepear.echarts.Line;
import org.icepear.echarts.Option;
import org.icepear.echarts.Pie;
import org.icepear.echarts.Scatter;
import org.icepear.echarts.charts.bar.BarSeries;
import org.icepear.echarts.charts.line.LineAreaStyle;
import org.icepear.echarts.charts.line.LineSeries;
import org.icepear.echarts.charts.pie.PieDataItem;
import org.icepear.echarts.components.coord.CategoryAxisLabel;
import org.icepear.echarts.components.coord.cartesian.CategoryAxis;
import org.icepear.echarts.components.coord.cartesian.ValueAxis;
import org.icepear.echarts.origin.util.SeriesOption;
import org.icepear.echarts.render.Engine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.Lists;
import com.jxp.integration.test.echarts.EchartType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-14 19:22
 */
@Slf4j
@RestController
@RequestMapping("/api/eharts")
public class EchartsController {

    @RequestMapping(value = "/getEcharts", produces = "text/html;charset=UTF-8")
    public String getEcharts(@RequestParam("bizType") String bizType, @RequestParam("echartType") String echartType) {

        // @RequestBody @Validated EchartsReq req
        // 获取数据
        //        String bizType = req.getBizType();
        // 图标类型
        //        @NotNull EchartType echartType = req.getEchartType();
        EchartType echartTypeEnum = EchartType.of(echartType);
        if (null == echartTypeEnum) {
            throw new RuntimeException("不支持的业务类型");
        }
        return renderHtml(echartTypeEnum, Lists.newArrayList());
    }

    private String renderHtml(EchartType echartType, List<T> dataList) {

        //        if (T.class.isAssignableFrom(Map.class)) {
        //
        //        }
        Engine engine = new Engine();
        switch (echartType) {
            case Line:
                Line line = new Line()
                        .addXAxis(new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"})
                        .addYAxis()
                        .addSeries(new Number[] {150, 230, 224, 218, 135, 147, 260});
                return engine.renderHtml(line);
            case Bar:
                Bar bar = new Bar()
                        .addXAxis(new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"})
                        .addYAxis()
                        .addSeries(new Number[] {120, 200, 150, 80, 70, 110, 130});
                return engine.renderHtml(bar);
            case Pie:
                Pie pie = new Pie()
                        .setTitle("Basic Pie")
                        .setTooltip("item")
                        .setLegend()
                        .addSeries(new PieDataItem[] {
                                new PieDataItem().setValue(1048).setName("Search Engine"),
                                new PieDataItem().setValue(735).setName("Direct"),
                                new PieDataItem().setValue(580).setName("Email"),
                                new PieDataItem().setValue(484).setName("Union Ads"),
                                new PieDataItem().setValue(300).setName("Video Ads")
                        });
                return engine.renderHtml(pie);
            case Scatter:
                Scatter scatter = new Scatter()
                        .addXAxis()
                        .addYAxis()
                        .addSeries(new Number[][] {
                                {10.0, 8.04}, {8.07, 6.95}, {13.0, 7.58}, {9.05, 8.81}, {11.0, 8.33}, {14.0, 7.66},
                                {13.4, 6.81}, {10.0, 6.33}, {14.0, 8.96}, {12.5, 6.82}, {9.15, 7.2}, {11.5, 7.2},
                                {3.03, 4.23}, {12.2, 7.83}, {2.02, 4.47}, {1.05, 3.33}, {4.05, 4.96}, {6.03, 7.24},
                                {12.0, 6.26}, {12.0, 8.84}, {7.08, 5.82}, {5.02, 5.68}
                        });
                return engine.renderHtml(scatter);
            default:
                throw new RuntimeException("不支持的业务类型");
        }
    }


    @GetMapping(value = "/option", produces = "text/html;charset=UTF-8")
    public String index() {
        Line lineChart = new Line()
                .addXAxis(new CategoryAxis()
                        .setData(new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"})
                        .setBoundaryGap(false))
                .addYAxis()
                .addSeries(new LineSeries()
                        .setData(new Number[] {820, 932, 901, 934, 1290, 1330, 1320})
                        .setAreaStyle(new LineAreaStyle()));
        Engine engine = new Engine();
        Handlebars handlebars = new Handlebars();
        String html = "";
        try {
            Template template = handlebars.compile("templates/index2");
            html = template.apply(engine.renderJsonOption(lineChart));
        } catch (IOException e) {
            System.out.println("template file not found");
        }
        return html;
    }

    @GetMapping("/option2")
    public String index2() {
        Bar bar = new Bar()
                .setTitle("我是标题")
                .setLegend()
                .setTooltip("item")
                .addXAxis(new String[] {"Matcha Latte", "Milk Tea", "Cheese Cocoa", "Walnut Brownie"})
                .addYAxis()
                .addSeries("2015", new Number[] {43.3, 83.1, 86.4, 72.4})
                .addSeries("2016", new Number[] {85.8, 73.4, 65.2, 53.9})
                .addSeries("2017", new Number[] {93.7, 55.1, 82.5, 39.1});
        Engine engine = new Engine();
        // The render method will generate our EChart into a HTML file saved locally in the current directory.
        // The name of the HTML can also be set by the first parameter of the function.
        //        engine.render("index.html", bar);
        return engine.renderHtml(bar);
    }

    @GetMapping("/option3")
    public String index3() {
        Pie pie = new Pie()
                .setTitle("Basic Pie")
                .setTooltip("item")
                .setLegend()
                .addSeries(new PieDataItem[] {
                        new PieDataItem().setValue(1048).setName("Search Engine"),
                        new PieDataItem().setValue(735).setName("Direct"),
                        new PieDataItem().setValue(580).setName("Email"),
                        new PieDataItem().setValue(484).setName("Union Ads"),
                        new PieDataItem().setValue(300).setName("Video Ads")
                });
        Engine engine = new Engine();
        return engine.renderHtml(pie);
    }

    public static void main(String[] args) {
        CategoryAxisLabel categoryAxisLabel = new CategoryAxisLabel();
        categoryAxisLabel.setRotate(25);
        CategoryAxis xAxis = new CategoryAxis()
                .setType("category")
                .setData(new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"})
                .setNameRotate(Double.valueOf(0.45))
                .setNameLocation("start")
                .setAxisLabel(categoryAxisLabel)
                .setMin("dataMin");

        ValueAxis yAxis = new ValueAxis().setType("value")
                .setNameRotate(25);

        BarSeries series = new BarSeries()
                .setData(new Number[] {120, 200, 150, 80, 70, 110, 130})
                .setType("bar");

        Option option = new Option()
                .setXAxis(xAxis)
                .setYAxis(yAxis)
                .setSeries(new SeriesOption[] {series});

        Engine engine = new Engine();
        String s = engine.renderHtml(option);
        log.info("s:{}", s);

    }

}
