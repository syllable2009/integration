package com.jxp.integration.test.auth.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-11 11:48
 *
 * 角色权限的唯一性：一个角色对一个实体为一条记录还是多条记录？
 * 权限的操作只是当时的场景，例如g1赋给g2的a的权限，当时a节点下b没有管理权限，那么g2没有b的权限，即时后面g1拥有了b的权限，还是原来的场景
 *
 * a         read
 * -b         1
 *  -c             edit
 *
 * 阻断记录不参与时间比较，只关注阻断的原始数据记录，此时value为失效的aid记录，如果重新赋权呢？
 *
 *
 * 文档权限 = entityId 对当前文档权限 = 遍历父级，找出权限设置，当遇见失效记录应该立即停止
 *
 * 所有的entityId集合起来
 *
 *
 */

@Data
public class CosmoEntityValue implements Serializable {

    private Long aid;
    private String id;
    private String cosmoId;
    private Long entityId; // groupInfoId
    private Long value; // roleCode
    private Integer invalidType; // 0=继承记录 1=阻断记录时，

    // db datetime(6)
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createId;
    private String updateId;

//    private Long entityDomainId;
//    private Boolean inherit;
//
//    private Integer groupType;
//    private Integer subVersion;
}
