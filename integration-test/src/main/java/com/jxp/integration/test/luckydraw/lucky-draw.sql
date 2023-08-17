create table draw_activity_config
(
    aid                 bigint(20) unsigned auto_increment comment '自增主键' primary key,
    id                  varchar(36)  NOT NULL COMMENT '唯一ID',
    state               tinyint(4)   null default 0 comment '状态',
    update_time         datetime          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    create_time         datetime          DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    create_id           varchar(36)  null default null comment '创建人id',
    update_id           varchar(36)  null default null comment '更新人id',
    title               varchar(255) null default null comment '活动标题',
    link                varchar(255) null default null comment '活动链接',
    description         text         null comment '活动描述',
    if_repeat_award     tinyint(1)        default 0 comment '是否可以重复中奖',
    if_award_accumulate tinyint(1)        default 0 comment '是否奖励累计',
    start_time          datetime     null DEFAULT null comment '开始时间',
    end_time            datetime     null DEFAULT null comment '结束时间',
    default_award_id    varchar(36)  NULL COMMENT '阳光普照奖品ID',
    draw_type           varchar(36)  null default null comment '抽奖类型：按次数，按时间',
    draw_count          int(4)       null default 0 comment '最多抽奖次数',
    UNIQUE KEY `uniq_id` (`id`)
)
    comment '抽奖活动配置表' default charset = utf8mb4
                      engine = InnoDB;

create table draw_prize_config
(
    aid               bigint(20) unsigned auto_increment comment '自增主键' primary key,
    id                varchar(36)  NOT NULL COMMENT '唯一ID',
    state             tinyint(4)   null default 0 comment '状态',
    activity_id       varchar(36)  NOT NULL COMMENT '活动ID',
    update_time       datetime          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    create_time       datetime          DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    create_id         varchar(36)  null default null comment '创建人id',
    update_id         varchar(36)  null default null comment '更新人id',
    award_id          varchar(36)  NULL COMMENT '奖品ID',
    award_title       varchar(255) null default null comment '奖品标题',
    award_link        varchar(255) null default null comment '奖品链接',
    award_description text         null comment '奖品描述',
    award_count       int(11)           default 0 comment '奖品数量',
    start_time        datetime     null DEFAULT null comment '开始时间',
    end_time          datetime     null DEFAULT null comment '结束时间',
    UNIQUE KEY `uniq_id` (`id`),
    KEY idx_activity_id_award_id (activity_id, award_id)
)
    comment '活动奖品配置表' default charset = utf8mb4
                      engine = InnoDB;

create table draw_prize_config
(
    aid         bigint(20) unsigned auto_increment comment '自增主键' primary key,
    id          varchar(36)  NOT NULL COMMENT '唯一ID',
    state       tinyint(4)   null default 0 comment '状态',
    activity_id varchar(36)  NOT NULL COMMENT '活动ID',
    update_time datetime          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    create_time datetime          DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    create_id   varchar(36)  null default null comment '创建人id',
    update_id   varchar(36)  null default null comment '更新人id',
    award_id    varchar(36)  NOT NULL COMMENT '奖品ID',
    user_id     varchar(255) null default null comment '用户id',
    UNIQUE KEY `uniq_id` (`id`),
    KEY idx_activity_id_user_id (activity_id, user_id)
)
    comment '中奖记录表' default charset = utf8mb4
                    engine = InnoDB;

