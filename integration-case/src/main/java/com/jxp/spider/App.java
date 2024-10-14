package com.jxp.spider;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.regex.Pattern;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

import lombok.extern.slf4j.Slf4j;

/**
 * mvn compile exec:java -D exec.mainClass="com.jxp.spider.App"
 * @author jiaxiaopeng
 * Created on 2024-09-27 16:28
 */
@Slf4j
public class App {

//    public static void install(String[] args) {
//        Playwright.create().chromium().
//        try (Playwright playwright = Playwright.create()) {
//            // 安装所有浏览器
//            playwright.install();
//        }
//    }


    public static void main(String[] args) {
        log.info("dir:{}", System.getProperty("playwright.cli.dir"));
        String alternativeTmpdir = System.getProperty("playwright.driver.tmpdir");
        log.info("alternativeTmpdir:{}", alternativeTmpdir);
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate("http://playwright.dev");

            log.info("{}", page.content());
            // Expect a title "to contain" a substring.
            assertThat(page).hasTitle(Pattern.compile("Playwright"));

            // create a locator
            Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));

            // Expect an attribute "to be strictly equal" to the value.
            assertThat(getStarted).hasAttribute("href", "/docs/intro");

            // Click the get started link.
            getStarted.click();

            // Expects page to have a heading with the name of Installation.
            assertThat(page.getByRole(AriaRole.HEADING,
                    new Page.GetByRoleOptions().setName("Installation"))).isVisible();
        }
    }
}
