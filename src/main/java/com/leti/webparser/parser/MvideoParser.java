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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.leti.webparser.util.BrowserUtil.isScrolledPage;
import static com.leti.webparser.util.BrowserUtil.scrollDown;


/**
 * Contains all logic to parse products from www.mvideo.ru
 */
@Service
@Getter
@RequiredArgsConstructor
@Log4j2
public class MvideoParser {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private WebDriver driver;
    private WebDriverWait wait;

    /**
     * Ignore this unique target words
     */
    public static final String[] IGNORE_TARGET = {"apple", "samsung", "акци", "кэш", "скидк", "распродаж", "premium", "лучш"};
    /**
     * Check products only with price
     */
    private static final String onlyPricesOn = "/f/tolko-v-nalichii=da";
    /**
     * Attribute to see 72 product at once on the page
     */
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

    /**
     * At the start fill stack with null categories and step by step pop stack
     */
    private final Stack<CategoryEntity> categoryIsNotNullStack = new Stack<>();
    /**
     * At the start fill stack with exists categories and step by step pop stack
     */
    private final Stack<String> productCategoryLinks = new Stack<>();


    /**
     * Starts program
     */
    //@PostConstruct
    void start() {
        driver = BrowserUtil.setUpFirefoxBrowser();
        wait = new WebDriverWait(driver, 5);
        parseMainCategoryPage();

        if (categoryIsNotNullStack.isEmpty()) {
            List<CategoryEntity> entities = StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                    .filter(c -> c.getSubCategoryLink() == null || c.getSubCategoryLink().isEmpty())
                    .collect(Collectors.toList());
            categoryIsNotNullStack.addAll(entities);
        }
        parseSubCategoryPage();

        if (productCategoryLinks.isEmpty()) {
            List<String> links = StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                    .map(CategoryEntity::getSubCategoryLink)
                    .filter(link -> link != null && !link.isEmpty())
                    .collect(Collectors.toList());
            productCategoryLinks.addAll(links);
        }
        parseProducts();
    }

    /**
     * Parse links to categories from page with all categories in the web-site every day at 9 o'clock
     */
    //@Scheduled(cron = "0 9 * * * *")
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

    /**
     *  Parse links with subcategories for products every day at 10 o'clock
     */
    //@Scheduled(cron = "0 10 * * * *")
    public void parseSubCategoryPage() {
        while (!categoryIsNotNullStack.isEmpty()) {
            CategoryEntity categoryEntity = categoryIsNotNullStack.pop();
            if (categoryEntity != null && !categoryEntity.getCategoryLink().isEmpty()) {
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
                try {
                    Thread.sleep(7_000L);
                } catch (InterruptedException e) {
                    log.warn(e.getMessage());
                }
            }
        }
    }


    /**
     * Parse prices by links and turn off program every day at 11 o'clock
     */
    @Scheduled(cron = "0 11 * * * *")
    void parseProducts() {
        while (!productCategoryLinks.isEmpty()) {
            String subcategoryLink = productCategoryLinks.pop();
            try {
                int pageMaxNumber = 1;
                String path = subcategoryLink;
                for (int page = 1; page <= pageMaxNumber; ) {
                    driver.get(path);
                    do {
                        scrollDown(driver, 30);
                    } while (!isScrolledPage(driver, 95));

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
                    try {
                        Thread.sleep(8_000L);
                    } catch (InterruptedException e) {
                        log.warn(e.getMessage());
                    }
                }
            } catch (NoSuchElementException exception) {
                System.out.println(exception.getMessage());
            }
        }
        log.info("Parsing is end successfully!");
        onDestroy();
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
