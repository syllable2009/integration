package com.jxp.integration.test.auth.enums;

import java.util.Set;

import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-11 14:15
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RoleEnum {

    NONE(0, Sets.newHashSet(), "无", "none"),
    OWNER(1, Sets.newHashSet(), "所有者", "owner"),
    MANAGER(2, Sets.newHashSet(), "管理", "manager"),
    EDIT(3, Sets.newHashSet(), "编辑", "edit"),
    COMMENT(4, Sets.newHashSet(), "评论", "comment"),
    VIEW(5, Sets.newHashSet(), "阅读", "view"),
    ;

    private Integer code;
    private Set<String> permissions;
    private String cnName;
    private String enName;

}
