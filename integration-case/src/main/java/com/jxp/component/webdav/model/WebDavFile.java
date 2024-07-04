package com.jxp.component.webdav.model;

import java.util.Date;

import io.milton.annotations.ContentLength;
import io.milton.annotations.CreatedDate;
import io.milton.annotations.ModifiedDate;
import io.milton.annotations.Name;
import io.milton.annotations.UniqueId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-03 20:53
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebDavFile {
    private String name;
    private Date createdDate;
    private Date modifiedDate;
    private Long contentLength;
    private byte[] bytes;

    public WebDavFile(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
        this.createdDate = new Date();
        this.modifiedDate = new Date();
        this.contentLength = (long) bytes.length;
    }

    @Name
    public String getName() {
        return name;
    }

    @UniqueId
    public String getUniqueId() {
        return name;
    }

    @ModifiedDate
    public Date getModifiedDate() {
        return modifiedDate;
    }

    @CreatedDate
    public Date getCreatedDate() {
        return createdDate;
    }

    @ContentLength
    public Long getContentLength() {
        return (long) bytes.length;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
