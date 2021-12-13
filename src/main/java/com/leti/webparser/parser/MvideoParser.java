package com.leti.webparser.parser;

import com.leti.webparser.entity.CategoryEntity;
import com.leti.webparser.repositories.CategoryRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;


@Component
@Getter
@RequiredArgsConstructor
@EnableAsync
public class MvideoParser {

    private final CategoryRepository categoryRepository;

    public static final String[] IGNORE_TARGET = {"apple", "samsung", "акци", "кэш", "скидк", "распродаж", "premium", "лучш"};
    public static final String onlyPricesOn = "/f/tolko-v-nalichii=da";

    public final String SHOP_NAME_MVIDEO = "https://www.mvideo.ru";
    private final String MAIN_LINK = "https://www.mvideo.ru/catalog?from=under_search";
    private final String MAIN_CATEGORY_XPATH_FORMAT = "//a[text()[contains(.,'%s')]]/parent::h3/parent::div/div[@class='c-catalog-item__links']/div/a";
    private final String SUBCATEGORY_XPATH = "//div[@class='sidebar-categories-wrapper']/ul/li/a";
    private final String SUB_SUBCATEGORY_XPATH = "";
    private WebDriver driver;

    private final Stack<CategoryEntity> linkSubcategoryStack = new Stack<>();

    @PostConstruct
    void setUpBrowser() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5_000L, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().pageLoadTimeout(20_000L, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().setScriptTimeout(5_000L, TimeUnit.MILLISECONDS);
    }


    @Scheduled(cron = "0 12 * * * *")
    @Async
    public void parseMainCategoryPage() {
        driver.get(MAIN_LINK);
        List<WebElement> mainCategories = driver.findElements(By.xpath("//h3/a"));

        // level 1 : main category
        for (WebElement element : mainCategories) {
            String mainCategoryText = element.getText();
            String mainCategoryLink = element.getAttribute("href");

            // ignore sales, cashback etc.
            if (Arrays.stream(IGNORE_TARGET).anyMatch(word -> mainCategoryText.toLowerCase().contains(word))) {
                continue;
            }
            String xpath = MAIN_CATEGORY_XPATH_FORMAT.replaceAll("%s", mainCategoryText);
            List<WebElement> categories = driver.findElements(By.xpath(xpath));

            // level 2 : category
            for (WebElement category : categories) {
                String categoryText = category.getText();
                String categoryLink = category.getAttribute("href").replaceAll("[?].+", "");

                if (Arrays.stream(IGNORE_TARGET).anyMatch(word -> categoryText.toLowerCase().contains(word))) {
                    continue;
                }

                if (!categoryRepository.existsByCategoryLink(categoryLink)) {
                    CategoryEntity categoryEntity = CategoryEntity.builder()
                            .shopName(SHOP_NAME_MVIDEO)
                            .mainCategory(mainCategoryText)
                            .mainCategoryLink(mainCategoryLink)
                            .category(categoryText)
                            .categoryLink(categoryLink)
                            .build();
                    categoryRepository.save(categoryEntity);
                }
            }
        }
    }


    @Async
    @Transactional
    @Scheduled(fixedDelay = 60_000L)
    public void parseSubCategoryPage() {
        CategoryEntity categoryEntity = categoryRepository.findFirstBySubCategoryIsNull();
        if (categoryEntity == null || categoryEntity.getCategoryLink() == null) return;
        driver.get(categoryEntity.getCategoryLink());

        // show all button
        try {
            driver.findElement(By.xpath("//a[@class='expandable-list-link']")).click();
        } catch (NoSuchElementException exception) {
            System.out.println(exception.getMessage());
        }

        List<WebElement> subCategories = driver.findElements(By.xpath(SUBCATEGORY_XPATH));
        for (WebElement element : subCategories) {
            String subcategoryText = element.getText();
            if (subcategoryText.equals("- Скрыть")) {
                continue;
            }

            String subCategoryLink = element.getAttribute("href");
            if (subCategoryLink.contains("/f/")) {
                continue;
            } else {
                subCategoryLink += onlyPricesOn;
            }

            if (!categoryRepository.existsBySubCategoryLink(subCategoryLink)) {
                CategoryEntity copyWithSubcategory = categoryEntity.toBuilder()
                        .subCategory(subcategoryText)
                        .subCategoryLink(subCategoryLink)
                        .build();
                categoryRepository.save(copyWithSubcategory);
            }
        }
        if (!subCategories.isEmpty()) {
            categoryRepository.deleteDistinctByIdAndSubCategoryIsNull(categoryEntity.getId());
        }
    }


    /**
     * Close browser on destroying program
     */
    @PreDestroy
    public void onDestroy() {
        driver.quit();
    }
}
