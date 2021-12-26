package com.leti.webparser.parser;

import com.leti.webparser.entity.CategoryEntity;
import com.leti.webparser.entity.ProductEntity;
import com.leti.webparser.exception.ShopException;
import com.leti.webparser.repositories.CategoryRepository;
import com.leti.webparser.repositories.ProductRepository;
import com.leti.webparser.util.BrowserUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.leti.webparser.util.BrowserUtil.isScrolledPage;
import static com.leti.webparser.util.BrowserUtil.scrollDown;


@Component
@Getter
@RequiredArgsConstructor
@EnableAsync
@Log4j2
public class MvideoParser {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private WebDriver driver;
    private WebDriverWait wait;

    public static final String[] IGNORE_TARGET = {"apple", "samsung", "акци", "кэш", "скидк", "распродаж", "premium", "лучш"};
    private static final String onlyPricesOn = "/f/tolko-v-nalichii=da";
    private static final String SHOW_COUNT_72 = "showCount=72";


    public final String SHOP_NAME_MVIDEO = "https://www.mvideo.ru";
    private final String MAIN_LINK = "https://www.mvideo.ru/catalog?from=under_search";
    private final String MAIN_CATEGORY_XPATH_FORMAT = "//a[text()[contains(.,'%s')]]/parent::h3/parent::div/div[@class='c-catalog-item__links']/div/a";

    private final String XPATH_SUBCATEGORY = "//div[@class='sidebar-categories-wrapper']/ul/li/a";
    private final String XPATH_SHOW_ALL_SUBCATEGORY_BUTTON = "//a[@class='expandable-list-link']";

    private final String XPATH_PRODUCT_TITLE_PATTERN = "//div[@class='product-title product-title--grid']/a";
    private final String XPATH_PRODUCT_PRICE_PATTERN = "//span[@class='price__main-value']";
    private final String XPATH_PRODUCT_PAGES = "//mvid-pagination/ul/li/a[text()[contains(.,*)]]";
    private final String XPATH_PRODUCT_END_OF_PAGE_FROM = "//div[@class='bottom-controls']";

    private final Stack<String> productCategoryLinks = new Stack<>();


    @PostConstruct
    void setUpBrowser() {
        driver = BrowserUtil.setUpFirefoxBrowser();
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
        driver.get(pageLink);

        // show all button
        try {
            driver.findElement(By.xpath(XPATH_SHOW_ALL_SUBCATEGORY_BUTTON)).click();
        } catch (NoSuchElementException exception) {
            log.info("Unable to locate element {}. Link: {}", XPATH_SHOW_ALL_SUBCATEGORY_BUTTON, pageLink);
        }
        List<WebElement> subCategories = driver.findElements(By.xpath(XPATH_SUBCATEGORY));
        for (WebElement element : subCategories) {
            String subcategoryText = element.getText();
            if (!subcategoryText.equals("- Скрыть")) {

                String subCategoryLink = element.getAttribute("href");
                if (!subCategoryLink.contains("/f/")) {

                    subCategoryLink += onlyPricesOn + "?" + SHOW_COUNT_72;
                    if (!categoryRepository.existsBySubCategoryLink(subCategoryLink)) {
                        CategoryEntity copyWithSubcategory = categoryEntity.toBuilder()
                                .subCategory(subcategoryText)
                                .subCategoryLink(subCategoryLink)
                                .build();
                        categoryRepository.save(copyWithSubcategory);
                    }
                }
            }

        }
        categoryRepository.deleteDistinctByIdAndSubCategoryIsNull(categoryEntity.getId());
    }


    @Scheduled(fixedDelay = 8_000L)
    void parseProducts() {
        if (productCategoryLinks.isEmpty()) {
            List<String> links = StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                    .map(CategoryEntity::getSubCategoryLink)
                    .filter(link -> !link.isEmpty())
                    .collect(Collectors.toList());
            productCategoryLinks.addAll(links);
        }
        String subcategoryLink = productCategoryLinks.pop();
        try {
            int pageMaxNumber = 1;
            String path = subcategoryLink;

            for (int page = 1; page <= pageMaxNumber; ) {
                driver.get(path);
                do {
                    scrollDown(driver, 30);
                } while (!isScrolledPage(driver, 80));

                List<WebElement> titles = driver.findElements(By.xpath(XPATH_PRODUCT_TITLE_PATTERN));
                List<WebElement> prices = driver.findElements(By.xpath(XPATH_PRODUCT_PRICE_PATTERN));

                if (titles.size() != prices.size()) {
                    scrollDown(driver, 200);
                    log.warn("Titles size '" + titles.size() + "' != prices size '" + prices.size() + "'");
                } else {
                    for (int j = 0; j < titles.size(); j++) {
                        WebElement title = titles.get(j);
                        WebElement price = prices.get(j);

                        String productName = title.getText();
                        String productLink = title.getAttribute("href");
                        Double productPrice = parsePrice(price.getText());
                        if (productPrice == null) {
                            throw new ShopException("Cannot parse product price for " + productLink);
                        }
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
                }
                List<WebElement> pageCount = driver.findElements(By.xpath(XPATH_PRODUCT_PAGES));
                if (!pageCount.isEmpty()) {
                    pageMaxNumber = Integer.parseInt(pageCount.get(pageCount.size() - 1).getText());
                }

                page++;
                path = subcategoryLink + "&page=" + page;
            }
        } catch (NoSuchElementException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private Double parsePrice(String price) {
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

    /**
     * Close browser on destroying program
     */
    @PreDestroy
    public void onDestroy() {
        driver.quit();
    }
}
