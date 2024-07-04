package com.jxp.page;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import cn.hutool.json.JSONUtil;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Power;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.ViewType;
import xyz.erupt.annotation.sub_field.sub_edit.AttachmentType;
import xyz.erupt.annotation.sub_field.sub_edit.BoolType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.upms.model.base.HyperModel;

/**
 * Created by liyuepeng on 2019-09-18.
 */
@Erupt(name = "文章发布",
        power = @Power(importable = true, export = true),
        orderBy = "Article.topUp desc"
)
@Entity
@Table(name = "jxp_article")
public class Article extends HyperModel {

    @EruptField(
            views = @View(title = "封面图"),
            edit = @Edit(title = "封面图", type = EditType.ATTACHMENT,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE, maxLimit = 3))
    )
    private String pic;

    @EruptField(
            views = @View(title = "标题"),
            edit = @Edit(title = "标题", notNull = true, search = @Search(vague = true))
    )
    private String title;

    @EruptField(
            views = @View(title = "置顶"),
            edit = @Edit(title = "置顶", notNull = true, search = @Search)
    )
    private Boolean topUp = false;

    @EruptField(
            views = @View(title = "发布状态"),
            edit = @Edit(title = "发布状态", notNull = true, boolType = @BoolType(trueText = "发布", falseText = "草稿"), search = @Search)
    )
    private Boolean publish;

    @Lob
    @EruptField(
            views = @View(title = "内容(UEditor)", type = ViewType.HTML, export = false),
            edit = @Edit(title = "内容(UEditor)", type = EditType.HTML_EDITOR, notNull = true)
    )
    private String content;

    @Column(length = 5000)
    @EruptField(
            views = @View(title = "备注"),
            edit = @Edit(title = "备注", type = EditType.TEXTAREA)
    )
    private String remark;


    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getTop() {
        return topUp;
    }

    public void setTop(Boolean top) {
        this.topUp = top;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
