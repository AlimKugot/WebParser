package com.leti.webparser.util;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class BrowserUtil {

    public static WebDriver setUpFirefoxBrowser() {
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5_000L, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().pageLoadTimeout(20_000L, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().setScriptTimeout(5_000L, TimeUnit.MILLISECONDS);
        return driver;
    }

    public static long getHeightOfPage(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long num = (long) js.executeScript("return window.innerHeight");
        return num;
    }


    public static void scrollToTop(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0)");
    }

    public static void scrollToBottom(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public static void scrollDown(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 450)");
    }

    public static void scrollDown(WebDriver driver, int y) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, " + y + ");");
    }


    public static void scrollUp(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 450)");
    }

}
