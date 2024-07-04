package com.jxp.component.webdav.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jxp.component.webdav.model.WebDavFile;
import com.jxp.component.webdav.model.WebDavFolder;

import io.milton.annotations.ChildrenOf;
import io.milton.annotations.CreatedDate;
import io.milton.annotations.Delete;
import io.milton.annotations.DisplayName;
import io.milton.annotations.Get;
import io.milton.annotations.ModifiedDate;
import io.milton.annotations.Name;
import io.milton.annotations.PutChild;
import io.milton.annotations.ResourceController;
import io.milton.annotations.Root;
import io.milton.annotations.UniqueId;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-03 20:52
 */
@Slf4j
@ResourceController
public class HelloWorldController{

    private static HashMap<String, WebDavFile> webDavFiles = new HashMap<>();

    static {
        try {
            byte[] bytes = "Hello World".getBytes("UTF-8");
            webDavFiles.put("file1.txt", new WebDavFile("file1.txt", bytes));
            webDavFiles.put("file2.txt", new WebDavFile("file2.txt", bytes));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Root
    public HelloWorldController getRoot() {
        return this;
    }

    @ChildrenOf
    public List<WebDavFolder> getWebDavFolders(HelloWorldController root) {
        List<WebDavFolder> webDavFolders = new ArrayList<WebDavFolder>();
        webDavFolders.add(new WebDavFolder("folder1"));
        webDavFolders.add(new WebDavFolder("folder2"));
        return webDavFolders;
    }

    @ChildrenOf
    public Collection<WebDavFile> getWebDavFiles(WebDavFolder webDavFolder) {
        return webDavFiles.values();
    }

    @Get
    public InputStream getChild(WebDavFile webDavFile) {
        return new ByteArrayInputStream(webDavFiles.get(webDavFile.getName()).getBytes());
    }

    @PutChild
    public void putChild(WebDavFile parent, String name, byte[] bytes) {
        if (name != null) {
            webDavFiles.put(name, new WebDavFile(name, bytes));
        } else {
            parent.setBytes(bytes);
            webDavFiles.put(parent.getName(), parent);
        }
    }

    @Delete
    public void delete(WebDavFile webDavFile) {
        webDavFiles.remove(webDavFile.getName());
    }

    @Name
    public String getWebDavFile(WebDavFile webDavFile) {
        return webDavFile.getName();
    }

    @DisplayName
    public String getDisplayName(WebDavFile webDavFile) {
        return webDavFile.getName();
    }

    @UniqueId
    public String getUniqueId(WebDavFile webDavFile) {
        return webDavFile.getName();
    }

    @ModifiedDate
    public Date getModifiedDate(WebDavFile webDavFile) {
        return webDavFile.getModifiedDate();
    }

    @CreatedDate
    public Date getCreatedDate(WebDavFile webDavFile) {
        return webDavFile.getCreatedDate();
    }

}
