package com.jxp.dto.bo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 精选爬虫定时任务数据表
 * @author jiaxiaopeng
 * Created on 2024-10-14 20:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@TableName("recommend_crawler_task_data")
public class RecommendCrawlerTaskData implements Serializable {
    private static final long serialVersionUID = 1L;

    //    @TableId(value = "aid", type = IdType.AUTO)
    private Long aid;


    private String uid;

    // 标题
    private String title;

    @NotEmpty
    // 原始链接
    private String link;

    // 域
    private String domain;

    // 状态
    private Integer state;

    // 解析器名称
    private String processor;

    // 用户id
    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    // 创建时间
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    // 更新时间
    private LocalDateTime updateTime;

    // cron
    private String cron;

    // 只有当post时才有效：default = application/x-www-form-urlencoded  multipart/form-data"
    private String requestContentType;

    // default = text/html  application/json
    private String responseContentType;

    // default = GET or POST
    private String requestMethod;

    // 数据body,para在url上
    private String requestBody;
}
