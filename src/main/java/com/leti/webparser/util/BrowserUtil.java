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


    public static boolean isScrolledPage(WebDriver driver, int percentToScroll) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        double docHeight = Double.parseDouble(String.valueOf(js.executeScript("return Math.max(\n" +
                "  document.body.scrollHeight, document.documentElement.scrollHeight,\n" +
                "  document.body.offsetHeight, document.documentElement.offsetHeight,\n" +
                "  document.body.clientHeight, document.documentElement.clientHeight\n" +
                ");")));
        double scrolledY = Double.parseDouble(String.valueOf(js.executeScript("return $(window).scrollTop() + $(window).height()")));
//        double scrolledY = Double.parseDouble(String.valueOf(js.executeScript("return window.pageYOffset;")));
        docHeight = (docHeight * percentToScroll) / 100;
        return scrolledY >= docHeight;
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
        js.executeScript("window.scrollBy(0, -450)");
    }

    public static void scrollUp(WebDriver driver, int y) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, -" + y + ")");
    }
}
