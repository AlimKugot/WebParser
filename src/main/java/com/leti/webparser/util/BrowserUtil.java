package com.leti.webparser.util;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

/**
 * Util that holds a lot of function to work with Browser via JS
 */
public class BrowserUtil {

    /**
     * First steps to download and install selenium firefox browser
     * @return ready driver to works with firefox
     */
    public static WebDriver setUpFirefoxBrowser() {
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5_000L, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().pageLoadTimeout(20_000L, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().setScriptTimeout(5_000L, TimeUnit.MILLISECONDS);
        return driver;
    }


    /**
     * Checks if end of page was done
     * @param driver ready driver to works with firefox
     * @param percentToScroll by currentScrollingHeight / docHeight
     * @return is percentage to scroll of page was achieved
     */
    public static boolean isScrolledPage(WebDriver driver, int percentToScroll) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        double docHeight = Double.parseDouble(String.valueOf(js.executeScript("return Math.max(\n" +
                "  document.body.scrollHeight, document.documentElement.scrollHeight,\n" +
                "  document.body.offsetHeight, document.documentElement.offsetHeight,\n" +
                "  document.body.clientHeight, document.documentElement.clientHeight\n" +
                ");")));
        double scrolledY = Double.parseDouble(String.valueOf(js.executeScript("return $(window).scrollTop() + $(window).height()")));
        docHeight = (docHeight * percentToScroll) / 100;
        return scrolledY >= docHeight;
    }


    /**
     * Scroll to the top of page (document)
     * @param driver from selenium to works with browser
     */
    public static void scrollToTop(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0)");
    }

    /**
     * Scroll to the bottom of page (document)
     * @param driver from selenium to works with browser
     */
    public static void scrollToBottom(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    /**
     * Scroll down by default value
     * @param driver from selenium to works with browser
     */
    public static void scrollDown(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 450)");
    }

    /**
     * Scroll down by y value
     * @param driver from selenium to works with browser
     * @param y to scroll down in pixels
     */
    public static void scrollDown(WebDriver driver, int y) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, " + y + ");");
    }


    /**
     * Scroll up by default value
     * @param driver from selenium to works with browser
     */
    public static void scrollUp(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, -450)");
    }

    /**
     * Scroll up by y value
     * @param driver from selenium to works with browser
     * @param y to scroll up in pixels
     */
    public static void scrollUp(WebDriver driver, int y) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, -" + y + ")");
    }
}
