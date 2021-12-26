package com.leti.webparser.util;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static com.leti.webparser.util.BrowserUtil.scrollToBottom;
import static com.leti.webparser.util.BrowserUtil.scrollUp;
import static com.leti.webparser.util.BrowserUtil.setUpFirefoxBrowser;

public class BrowserUtilTest {

    private static final String LINK = "https://stackoverflow.com/questions/9439725/how-to-detect-if-browser-window-is-scrolled-to-bottom";

    @Test
    void checkEndOfPage() {
        WebDriver driver = setUpFirefoxBrowser();
        driver.get(LINK);
        scrollToBottom(driver);
//        assertFalse(BrowserUtil.isEndOfPage(driver));
    }

    @Test
    void checkEnd() {
        WebDriver driver = setUpFirefoxBrowser();
        driver.get(LINK);
        scrollToBottom(driver);
        scrollUp(driver, 100);
//        assertFalse(BrowserUtil.isEndOfPage(driver));
    }
}
