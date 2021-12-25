package com.leti.webparser.parser;

import com.leti.webparser.entity.CategoryEntity;
import com.leti.webparser.entity.ProductEntity;
import com.leti.webparser.exception.ShopException;
import com.leti.webparser.repositories.CategoryRepository;
import com.leti.webparser.repositories.ProductRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;



@Component
@Getter
@RequiredArgsConstructor
@EnableAsync
@Log4j2
public class MvideoParser {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public static final String[] IGNORE_TARGET = {"apple", "samsung", "акци", "кэш", "скидк", "распродаж", "premium", "лучш"};
    public static final String onlyPricesOn = "/f/tolko-v-nalichii=da";
    public static final String SHOW_COUNT_72 = "showCount=72";


    public final String SHOP_NAME_MVIDEO = "https://www.mvideo.ru";
    private final String MAIN_LINK = "https://www.mvideo.ru/catalog?from=under_search";
    private final String MAIN_CATEGORY_XPATH_FORMAT = "//a[text()[contains(.,'%s')]]/parent::h3/parent::div/div[@class='c-catalog-item__links']/div/a";
    private final String SUBCATEGORY_XPATH = "//div[@class='sidebar-categories-wrapper']/ul/li/a";
    private final String SUB_SUBCATEGORY_XPATH = "";
    private WebDriver driver;
    private WebDriverWait wait;

    private final Stack<String> subcategoryLinks = new Stack<>();

    private final Stack<CategoryEntity> linkSubcategoryStack = new Stack<>();

    @PostConstruct
    void setUpBrowser() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5_000L, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().pageLoadTimeout(20_000L, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().setScriptTimeout(5_000L, TimeUnit.MILLISECONDS);
        wait = new WebDriverWait(driver, 5);
    }

    @Async
    @Scheduled(cron = "0 12 * * * *")
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


    @Transactional
//    @Scheduled(fixedDelay = 5_000L)
    public void parseSubCategoryPage() {
        CategoryEntity categoryEntity = categoryRepository.findFirstBySubCategoryIsNull();
        if (categoryEntity == null || categoryEntity.getCategoryLink() == null) return;
        String pageLink = categoryEntity.getCategoryLink();
        String showAllButtonClass = "//a[@class='expandable-list-link']";
        driver.get(pageLink);

        // show all button
        try {
            WebElement button = driver.findElement(By.xpath(showAllButtonClass));
            button.click();
        } catch (NoSuchElementException exception) {
            log.info("Unable to locate element {}. Link: {}", showAllButtonClass, pageLink);
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
                subCategoryLink += onlyPricesOn + "?" + SHOW_COUNT_72;
            }

            if (!categoryRepository.existsBySubCategoryLink(subCategoryLink)) {
                CategoryEntity copyWithSubcategory = categoryEntity.toBuilder()
                        .subCategory(subcategoryText)
                        .subCategoryLink(subCategoryLink)
                        .build();
                categoryRepository.save(copyWithSubcategory);
            }
        }
        categoryRepository.deleteDistinctByIdAndSubCategoryIsNull(categoryEntity.getId());
    }


    @Scheduled(fixedDelay = 5_000L)
    void parseProducts() {
        if (subcategoryLinks.isEmpty()) {
            List<String> links = StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                    .map(CategoryEntity::getSubCategoryLink)
                    .filter(link -> !link.isEmpty())
                    .collect(Collectors.toList());
            subcategoryLinks.addAll(links);
        }
        String subcategoryLink = subcategoryLinks.pop();
        final String XPATH_ROOT = "//mvid-product-cards-row[@class='ng-star-inserted']/div";
        final String XPATH_TITLE_PATTERN = "/div[@class='product-card__title-line-container ng-star-inserted']";
        final String XPATH_PRICE_PATTERN = "/div[@class='product-card__price-block-container ng-star-inserted']";

        final String HTML_TITLE = "/mvid-plp-product-title/div/a";
        final String HTML_PRICE = "/mvid-plp-price-block//span";

        try {
            int pageMaxNumber = 1;

            // page
            for (int page = 1; page <= pageMaxNumber; page++) {
                String path = subcategoryLink + "&page=" + page;
                driver.get(path);

                int blockSize = driver.findElements(By.xpath(XPATH_ROOT)).size();
                // grid-block
                for (int i = 1; i <= blockSize; i++) {
                    String blockI = XPATH_ROOT + "[" + i + "]";
                    int productSize = driver.findElements(By.xpath(blockI + XPATH_TITLE_PATTERN)).size();

                    // products in grid-block
                    for (int j = 1; j <= productSize; j++) {
                        String productTitleXpath = blockI + XPATH_TITLE_PATTERN + "[" + j + "]/" + HTML_TITLE;
                        String productPriceXpath = blockI + XPATH_PRICE_PATTERN + "[" + j + "]/" + HTML_PRICE;

                        WebElement title = driver.findElement(By.xpath(productTitleXpath));
                        WebElement price = driver.findElement(By.xpath(productPriceXpath));

                        String productName = title.getText();
                        String productLink = title.getAttribute("href");
                        Double productPrice = parsePrice(price.getText());
                        if (productPrice == null)
                            throw new ShopException("Cannot parse product price for " + productLink);
                        if (!productRepository.existsByLinkAndPrice(productLink, productPrice)) {
                            ProductEntity product = ProductEntity.builder()
                                    .name(productName)
                                    .link(productLink)
                                    .categoryLink(subcategoryLink)
                                    .price(productPrice)
                                    .build();
                            productRepository.save(product);
                        }
                    }
                    scrollDown();
                }
                List<WebElement> pages = driver.findElements(By.xpath("//mvid-pagination/ul/li/a[text()[contains(.,*)]]"));
                if (!pages.isEmpty()) {
                    pageMaxNumber = Integer.parseInt(pages.get(pages.size() - 1).getText());
                }
            }

            System.out.println("!".repeat(20));
            System.out.println("Success");
            System.out.println("!".repeat(20));
        } catch (NoSuchElementException exception) {
            System.out.println(exception.getMessage());
        }
    }

    protected Double parsePrice(String price) {
        StringBuilder priceBuilder = new StringBuilder(price);
        if (!priceBuilder.toString().isEmpty()) {
            int length = priceBuilder.length();
            // delete currency
            priceBuilder.delete(length - 2, length);
            // delete whitespaces
            String priceString = priceBuilder.toString().replaceAll("\\s", "");
            return Double.parseDouble(priceString);
        } else {
            return null;
        }
    }

    protected void scrollBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    protected void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 450)");
    }

    protected void scrollUp() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 450)");
    }

    /**
     * Close browser on destroying program
     */
    @PreDestroy
    public void onDestroy() {
        driver.quit();
    }
}
