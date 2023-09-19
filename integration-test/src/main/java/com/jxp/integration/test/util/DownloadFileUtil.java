package com.jxp.integration.test.util;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

import com.jxp.integration.test.config.PlaywrightConfig;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 17:43
 */

@Slf4j
public class DownloadFileUtil {

    public static boolean downloadByHutool(String fileUrl, String path, String fileName) {
        if (StrUtil.hasBlank(fileUrl, path, fileName)) {
            return false;
        }
        long contentLength = HttpUtil.downloadFile(fileUrl, path + File.separator + fileName);
        log.info("downloadByHutool,contentLength:{}", contentLength);
        return true;
    }

    public static boolean downloadByPlaywright(String fileUrl, String path, String fileName) {
        if (StrUtil.hasBlank(fileUrl, path, fileName)) {
            return false;
        }
        Page page = PlaywrightConfig.BROWSER_CONTEXT.newPage();
        Response navigate = page.navigate(fileUrl);
        downloadByPlaywright(navigate, path, fileName);
        return true;
    }

    // 调用此方法一定要确保是可链接的文件链接
    public static boolean downloadByPlaywright(Page page, Runnable callback, String path, String fileName) {
        log.info("download page:{}", page.url());
        Download download = page.waitForDownload(callback);
        if (StringUtils.isBlank(fileName)) {
            fileName = download.suggestedFilename();
        }
        download.saveAs(Paths.get(path, fileName));
        return true;
    }

    public static boolean downloadByPlaywright(Response navigate, String path, String fileName) {
        saveFileBytes(navigate.body(), path, fileName);
        return true;
    }

    public static void main(String[] args) {
        String fileUrl = "http://img.netbian.com/file/2023/0918/2022362nAbF.jpg";
        String path = "/Users/jiaxiaopeng";
        String fineName = IdUtil.simpleUUID() + ".jpg";
        downloadByPlaywright(fileUrl, path, fineName);
        DownloadFileUtil.downloadByHutool(fileUrl, "/Users/jiaxiaopeng/", "222.jpg");
        PlaywrightConfig.close();
    }

    public static boolean saveFileBytes(byte[] body, String path, String fileName) {
        if (null == body || StringUtils.isBlank(path) || StringUtils.isBlank(fileName)) {
            return false;
        }
        File file = FileUtil.writeBytes(body, path + File.separator + fileName);
        return file.exists();
    }
}
