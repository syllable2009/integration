create table recommend_spider_task
(
    aid         bigint(20) unsigned auto_increment comment '自增主键' primary key,
    id          varchar(36)              NOT NULL DEFAULT '' COMMENT '唯一ID',
    state       tinyint(4)    default 0  null comment '状态',
    update_time datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    create_time datetime      DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    create_id   varchar(36)   default '' null comment '业务id',
    update_id   varchar(36)   default '' null comment '业务id',
    title       varchar(255)  default '' null comment '标题',
    link        varchar(255)  default '' null comment '链接',
    cover       varchar(255)  default '' null comment '封面地址',
    raw_text    text                     null comment 'html内容',
    description text                     null comment '文档摘要',
    domain      varchar(255)  default '' null comment 'domain',
    biz_type    varchar(255)  default '' null comment '业务类型',
    biz_id      varchar(255)  default '' null comment '业务id',
    category    varchar(255)  default '' null comment '分类',
    md5         varchar(255)  default '' null comment '根据摘要生成md5',
    vector      varchar(2000) default '' null comment 'vector',
    UNIQUE KEY `uniq_id` (`id`),
    KEY idx_md5 (md5),
    KEY idx_domain_biz_id (domain, biz_id)
)
    comment '爬虫任务表' default charset = utf8mb4
                    engine = InnoDB;