package com.jxp.page;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Power;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.BoolType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.jpa.model.BaseModel;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-02 15:03
 */
@Data
@Erupt(name = "学生表", // 功能名称
        desc = "功能描述", // 功能描述
        primaryKeyCol = "id", // 主键列名称，默认值为id
        orderBy = "id desc", // 排序规则，参照HQL语句 order by 语法
        power = @Power(importable = true, export = true) // 控制增删改查导入导出功能
)
@Entity
@Table(name = "t_student")
public class Student extends BaseModel {

    @EruptField(
            views = @View(title = "学生姓名"),
            edit = @Edit(title = "学生姓名", notNull = true, search = @Search(vague = true))
    )
    private String studentName;

    @EruptField(
            views = @View(title = "所属班级"),
            edit = @Edit(title = "所属班级", notNull = true)
    )
    private String studentClass;

    @EruptField(
            views = @View(title = "学生年龄"),
            edit = @Edit(title = "学生年龄", notNull = true)
    )
    private String studentAge;

    @Lob
    @EruptField(
            views = @View(title = "学生性别"),
            edit = @Edit(title = "学生性别", notNull = true)
    )
    private String studentSex;
    @EruptField(
            views = @View(title = "考核状态"),
            edit = @Edit(title = "考核状态", notNull = true, boolType = @BoolType(trueText = "通过", falseText = "挂科"), search = @Search)
    )
    private Boolean status;
}
