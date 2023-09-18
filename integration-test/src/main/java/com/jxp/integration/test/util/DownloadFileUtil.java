package com.jxp.integration.test.util;

import java.io.File;
import java.nio.file.Paths;

import com.jxp.integration.test.config.PlaywrightConfig;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 17:43
 */

@Slf4j
public class DownloadFileUtil {

    public static boolean downloadByHutool(String fileUrl, String path, String fileName) {
        long contentLength = HttpUtil.downloadFile(fileUrl, path + File.separator + IdUtil.simpleUUID() + ".jpg");
        log.info("downloadByHutool,contentLength:{}", contentLength);
        return true;
    }

    public static boolean downloadByPlaywright2(String fileUrl, String path, String fileName) {
        Page page = PlaywrightConfig.BROWSER_CONTEXT.newPage();
        Response navigate = page.navigate(fileUrl);
        saveFileBytes(navigate.body(), path, fileName);
        return true;
    }

    public static boolean downloadByPlaywright(String fileUrl, String path, String fileName) {
        Page page = PlaywrightConfig.BROWSER_CONTEXT.newPage();

        Response navigate = page.navigate(fileUrl);
        Download download = page.waitForDownload(() -> {
            page.getByAltText("原创 生活百般滋味人生需要笑对2023年10月日历高清壁纸").click();

        });

        download.saveAs(Paths.get(path, download.suggestedFilename()));
        return true;
    }

    public static void main(String[] args) {
        String fileUrl = "http://www.netbian.com/2560x1440/";
        String path = "/Users/jiaxiaopeng";
        String fineName = IdUtil.simpleUUID() + ".jpg";
        downloadByPlaywright(fileUrl, path, fineName);
        PlaywrightConfig.close();
    }

    public static boolean saveFileBytes(byte[] body, String path, String fileName) {
        File file = FileUtil.writeBytes(body, path + File.separator + fileName);
        return file.exists();
    }
}
