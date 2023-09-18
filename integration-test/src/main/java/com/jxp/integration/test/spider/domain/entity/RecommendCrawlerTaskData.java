package com.jxp.integration.test.spider.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 16:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@TableName("recommend_crawler_task_data")
@ApiModel(value = "RecommendCrawlerTaskData", description = "精选爬虫定时任务数据表")
public class RecommendCrawlerTaskData implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键自增id")
    //    @TableId(value = "aid", type = IdType.AUTO)
    private Long aid;

    @ApiModelProperty(value = "uuid")
    private String id;

    @NotEmpty
    @ApiModelProperty(value = "标题")
    private String title;

    @NotEmpty
    @ApiModelProperty(value = "原始链接")
    private String link;

    @NotEmpty
    @ApiModelProperty(value = "域")
    private String domain;

    @NotNull
    @ApiModelProperty(value = "状态")
    private Integer state;

    @NotEmpty
    @ApiModelProperty(value = "解析器名称")
    private String processor;

    @NotEmpty
    @ApiModelProperty(value = "用户邮箱前缀")
    private String loginId;

    @NotEmpty
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @NotEmpty
    @ApiModelProperty(value = "cron")
    private String cron;

    @ApiModelProperty(value = "只有当post时才有效：default = application/x-www-form-urlencoded  multipart/form-data")
    private String requestContentType;

    @ApiModelProperty(value = "default = text/html  application/json")
    private String responseContentType;

    @ApiModelProperty(value = "default = GET or POST")
    private String requestMethod;

    @ApiModelProperty(value = "数据body,para在url上")
    private String requestBody;
}
