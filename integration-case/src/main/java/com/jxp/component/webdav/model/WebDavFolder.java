package com.jxp.component.webdav.model;

import java.util.Date;

import io.milton.annotations.CreatedDate;
import io.milton.annotations.Name;
import io.milton.annotations.UniqueId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-03 20:55
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebDavFolder {
    private String name;
    private Date createdDate;

    public WebDavFolder(String name) {
        this.name = name;
    }

    @Name
    public String getName() {
        return name;
    }

    @UniqueId
    public String getUniqueId() {
        return name;
    }

    @CreatedDate
    public Date getCreatedDate() {
        return createdDate;
    }
}
