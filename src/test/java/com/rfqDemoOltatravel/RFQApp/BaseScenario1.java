package com.rfqDemoOltatravel.RFQApp;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.rfqDemoOltatravel.PricesApp.PricesAppCommonCode;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;

public class BaseScenario1 {

    public ChromeDriver driver;

    RFQAppCommonCode rfqAppCommonCode = new RFQAppCommonCode();
    private SoftAssertions softAssertions;
    boolean isWindows=false;

    @Before
    public void setUp() {
        isWindows=false;
        if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){isWindows=true;}

        if(isWindows){
            System.setProperty("webdriver.chrome.driver", "C:\\Automation\\chromedriver.exe");
            driver = new ChromeDriver();
            driver.manage().window().maximize();}
        else{driver = new ChromeDriver();}

        softAssertions = new SoftAssertions();
    }



    @Test
    public void test1(){

        class PeriodsCollection{
            public LocalDate dateFrom;
            public LocalDate dateTo;
            public int priceSGL;
            public int priceDBL;
            public int priceSGLWE;
            public int priceDBLWE;

            PeriodsCollection(String ... data) {
                if(data.length>=1)
                    dateFrom = LocalDate.of(Integer.valueOf(data[0].substring(6,data[0].length())), Integer.valueOf(data[0].substring(3,5)), Integer.valueOf(data[0].substring(0,2)));
                if(data.length>=2)
                    dateTo = LocalDate.of(Integer.valueOf(data[1].substring(6,data[1].length())), Integer.valueOf(data[1].substring(3,5)), Integer.valueOf(data[1].substring(0,2)));
                if(data.length>=3)
                    priceSGL = Integer.valueOf(data[2]);
                if(data.length>=4)
                    priceDBL = Integer.valueOf(data[3]);
                if(data.length>=5)
                    priceSGLWE = Integer.valueOf(data[4]);
                if(data.length>=6)
                    priceDBLWE = Integer.valueOf(data[5]);
            }
        }

        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;

        isWindows=false;
        if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){isWindows=true;}

        String propertiesPath;
        if(isWindows){propertiesPath="target\\test-classes\\application.properties";}
        else{propertiesPath="target//test-classes//application.properties";}
        Properties props=new Properties();
        try {
            props.load(new InputStreamReader(new FileInputStream(propertiesPath), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Получил проперти ");
        System.out.println(props.getProperty("baseURL"));
        System.out.print("[-] Открываем URL: "+props.getProperty("baseURL"));
        open(props.getProperty("baseURL"));
        rfqAppCommonCode.WaitForPageToLoad(driver);
        System.out.println(RFQAppCommonCode.OK);


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue(PricesAppCommonCode.PRICESAPPLOGIN);
        $(By.id("password")).setValue(PricesAppCommonCode.PRICESAPPPASSWORD);
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(RFQAppCommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        rfqAppCommonCode.WaitForPageToLoad(driver);
        RFQAppCommonCode.WaitForProgruzkaSilent();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем приложение Prices");
        open(String.valueOf(props.getProperty("baseURL"))+"/application/olta.prices");
        //Ждём пока загрузится страница и проподёт "Loading..."
        rfqAppCommonCode.WaitForPageToLoad(driver);
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);


        //Открываем групповые цены на отели
        System.out.print("[-] Открываем групповые цены");
        $(By.id("group")).click();
        //Открываем текущий день
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatForPrices = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        LocalDate nowDate = LocalDate.now();
        LocalDate tommorrow = nowDate.plus(1, ChronoUnit.DAYS);
        System.out.println(RFQAppCommonCode.OK);

        //Открываем Москву
        System.out.print("[-] Открываем Москву");
        $(By.xpath("//div[@id=\"switch-city\"]//button[@data-switch-value=\"MSK\"]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем текущий год
        System.out.print("[-] Открываем текущий год");
        $(By.xpath("//div[@id=\"switch-year\"]//button[contains(text(),'2018')]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Выставляем тип отеля - Hotel 4* central
        System.out.print("[-] Выставляем тип отеля - Hotel 4* central");
        //$(By.xpath("//select[@id=\"hotel-type-filter\"]")).selectOptionContainingText("Hotel 4* central");
        $(By.xpath("//div[@id=\"switch-hotel-type\"]//button[contains(text(),'Hotel 4* central')]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем попап с ценами
        System.out.print("[-] Открываем попап с ценами");
        $(By.xpath("//div[@id=\"content\"]//div[@id=\"hotel-calendar\"]//div[@data-year=\"2018\"]" +
                "//div//table//tbody//tr" +
                "//td[@data-date=\""+nowDate.format(formatForPrices)+"\"]")).click();
        System.out.println(RFQAppCommonCode.OK);

        //Сохраняем цены
        System.out.print("[-] Сохраняем цены");
        ArrayList<PeriodsCollection> prices = new ArrayList<>();

        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);
        //Сохраняем значения из попапа
        String dateFrom = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
        String dateTo = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
        String priceSGL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
        String priceDBL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
        String priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
        String priceDBLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();

        //Сохраняем значения в новый элемент списка
        prices.add(new PeriodsCollection(dateFrom, dateTo, priceSGL, priceDBL, priceSGLWE, priceDBLWE));


        //System.out.println(dateFrom+" "+dateTo+" "+priceSGL+" "+priceDBL+" "+priceSGLWE+" "+priceDBLWE);
        //Закрываем попап
        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);
        System.out.println(RFQAppCommonCode.OK);

        //Открываем Экскурсии
        System.out.print("[-] Открываем Экскурсии");
        $(By.id("excursions")).click();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем Москву
        System.out.print("[-] Открываем Москву");
        $(By.xpath("//div[@id=\"switch-city\"]//button[@data-switch-value=\"MSK\"]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем текущий год
        System.out.print("[-] Открываем текущий год");
        $(By.xpath("//div[@id=\"switch-year\"]//button[contains(text(),'2018')]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Сохраняем цену за экскурсию - Бункер 42
        System.out.print("[-] Сохраняем цену за экскурсию - Бункер 42");
        String priceForBunker42 = $(By.xpath("//table[@id=\"service-prices\"]//tbody//tr[@data-excursion-id=\"3\"]" +
                "//td[@class=\"editable editable-service-price price\"]")).getText();
        System.out.println(" "+priceForBunker42+" ");
        System.out.println(RFQAppCommonCode.OK);

        //Открываем Шоу
        System.out.print("[-] Открываем Шоу");
        $(By.id("shows")).click();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем Москву
        System.out.print("[-] Открываем Москву");
        $(By.xpath("//div[@id=\"switch-city\"]//button[@data-switch-value=\"MSK\"]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем текущий год
        System.out.print("[-] Открываем текущий год");
        $(By.xpath("//div[@id=\"switch-year\"]//button[contains(text(),'2018')]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Сохраняем цену за Шоу - Большой Театр
        System.out.print("[-] Сохраняем цену за Шоу - Большой Театр");
        String priceForBolshoiTheatre = $(By.xpath("//table[@id=\"service-prices\"]//tbody//tr[@data-excursion-id=\"75\"]" +
                "//td[@class=\"editable editable-service-price price\"]")).getText();
        System.out.println(" "+priceForBolshoiTheatre+" ");
        System.out.println(RFQAppCommonCode.OK);

        //Сохраняем цену за Гида
        System.out.print("[-] Открываем Цены для Гида");
        $(By.id("guides")).click();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем Москву
        System.out.print("[-] Открываем Москву");
        $(By.xpath("//div[@id=\"switch-city\"]//button[@data-switch-value=\"MSK\"]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем текущий год
        System.out.print("[-] Открываем текущий год");
        $(By.xpath("//div[@id=\"switch-year\"]//button[contains(text(),'2018')]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        System.out.print("[-] Открываем язык Английский:");
        $(By.xpath("//div[@id=\"switch-lang\"]//button[@data-switch-value=\"English\"]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        System.out.print("[-] Сохраняем сумму за - 1/2 DAY (4 HOURS):");
        double guidePriceforHalfDay = Double.valueOf($(By.xpath("//table[@id=\"service-prices\"]//tbody//tr//td//a[contains(text(),'1/2 DAY (4 HOURS)')]/../../td[3]")).getText());
        //System.out.println(guidePriceforHalfDay);
        System.out.println(RFQAppCommonCode.OK);

        //Сохраняем цену за Транспорт
        System.out.print("[-] Открываем цены на транспорт:");
        $(By.id("transport")).click();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем Москву
        System.out.print("[-] Открываем Москву");
        $(By.xpath("//div[@id=\"switch-city\"]//button[@data-switch-value=\"MSK\"]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем текущий год
        System.out.print("[-] Открываем текущий год");
        $(By.xpath("//div[@id=\"switch-year\"]//button[contains(text(),'2018')]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        System.out.print("[-] Сохраняем сумму за 1 час для автобуса до 18 человек:");
        double transportPriceHourly18 = Double.valueOf($(By.xpath("//table[@id=\"service-prices\"]//tbody//tr[@data-max-load=\"18\"]//td[@class=\"prices\"]/table/tbody//tr/td[@class=\"service-name\"]/span[contains(text(),'HOURLY')]/../..//td[2]")).getText());
        //System.out.println(transportPriceHourly18);
        System.out.println(RFQAppCommonCode.OK);

        System.out.print("[-] Сохраняем сумму за 1 час для автобуса до 44 человек:");
        double transportPriceHourly44 = Double.valueOf($(By.xpath("//table[@id=\"service-prices\"]//tbody//tr[@data-max-load=\"44\"]//td[@class=\"prices\"]/table/tbody//tr/td[@class=\"service-name\"]/span[contains(text(),'HOURLY')]/../..//td[2]")).getText());
        //System.out.println(transportPriceHourly44);
        System.out.println(RFQAppCommonCode.OK);

        //Выходим из Prices
        System.out.print("[-] Выходим из Prices");
        $(By.xpath("//div[@id=\"profile\"]")).click();
        $(By.xpath("//button[@id=\"btn-logout\"]")).shouldBe(Condition.visible).click();
        System.out.println(RFQAppCommonCode.OK);

        //Открываем клиентский RFQ
        System.out.print("[-] Открываем URL: "+props.getProperty("baseURL")+"/application/rfq.rfq");
        open(props.getProperty("baseURL")+"/application/rfq.rfq");
        rfqAppCommonCode.WaitForPageToLoad(driver);
        System.out.println(RFQAppCommonCode.OK);

        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue(RFQAppCommonCode.RFQAPPLOGIN);
        $(By.id("password")).setValue(RFQAppCommonCode.RFQAPPPASSWORD);
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(RFQAppCommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        rfqAppCommonCode.WaitForPageToLoad(driver);
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println("[-] страница загружена - URL: " + url());

        //Создаём новый Quotation
        System.out.print("[-] Создаём новый Quotation, ID = ");
        $(By.cssSelector(QuotationListPage.newQuotationButton)).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();

        //Получаем Id новой квотации
        String newQuotationID = $(By.cssSelector(NewQuotationPage.quotationId)).getText();
        newQuotationID = newQuotationID.substring(1, newQuotationID.length());
        System.out.println(RFQAppCommonCode.ANSI_GREEN+newQuotationID+RFQAppCommonCode.ANSI_RESET);

        //Выставляем имя клиента
        System.out.print("[-] Выставляем имя клиента, как "+"Test Client: ");
        $(By.cssSelector(NewQuotationPage.clientName)).click();
        $(By.cssSelector(NewQuotationPage.chooseClientNamePopup)).shouldBe(Condition.visible);
        //$(By.cssSelector(NewQuotationPage.ChooseClientNamePopup.searchField)).sendKeys("Test Client\n");
        $(By.cssSelector(NewQuotationPage.chooseClientNamePopup
                + " div[class=\"check-list scroll-pane\"] div[class=\"jspContainer\"] div[class=\"jspPane\"]"
                + " div[group-value=\"T\"] div[class=\"check-wrap\"] span")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Выбираем Currency
        System.out.print("[-] Выставляем валюту - RUB: ");
        $(By.cssSelector(NewQuotationPage.Options.currencyButton)).scrollTo().click();
        $(By.cssSelector(NewQuotationPage.Options.currencySelectors)).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Options.currencyRUBXP)).click();
        $(By.xpath(NewQuotationPage.Options.currencyRUBXP)).click();
        $(By.xpath(NewQuotationPage.Options.currencyRUBXP)).shouldNotBe(Condition.visible);
        RFQAppCommonCode.WaitForProgruzkaSilent();

        System.out.println(RFQAppCommonCode.OK);

        //Выставляем Nights
        System.out.print("[-] Выставляем 1 ночь: ");
        $(By.xpath(NewQuotationPage.Options.nightsButtonXP)).scrollTo().click();
        $(By.xpath(NewQuotationPage.Options.nightsInputXP)).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Options.nightsInputXP)).setValue("1");
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Выбираем Present Meal Service
        System.out.print("[-] Выставляем Preset Meal Services - FB: ");
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesButton)).scrollTo().click();
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesSelectors)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.presentMealServiceFullBoard)).click();
        //$(By.cssSelector(NewQuotationPage.Options.presentMealServiceNO)).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Выбираем Additional Service
        /*System.out.print("[-] Выставляем Additional Service - Headphones: ");
        $(By.cssSelector(NewQuotationPage.Options.additionalServicesButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.additionalServicesSelectors)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.additionalServicesHeadphones)).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);*/

        //Выбираем Guides language
        /*System.out.print("[-] Выставляем Guides language - English: ");
        $(By.cssSelector(NewQuotationPage.Options.guidesLanguageButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.guidesLanguageArea)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.guidesLanguageEnglish)).click();
        $(By.cssSelector(NewQuotationPage.Options.guidesLanguageEnglish)).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);*/

        //Выставляем Free Tour Leaders
        /*System.out.print("[-] Выставляем Free Tour Leaders - 1:");
        $(By.cssSelector(NewQuotationPage.Options.freeTourLeadersButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.freeTourLeadersInput)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.freeTourLeadersInput)).setValue("1");
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);*/

        //Заполняем даты
        System.out.print("[-] Заполняем дату From: " +formatForDate.format(nowDate)+ " ");
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.Dates.firstIntervalFromInput)).click();

        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.Dates.firstIntervalFromInput)).setValue(formatForDate.format(nowDate)).pressEnter();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Выбираем Москву
        System.out.print("[-] Добавляем размещение - Москва: ");
        $(By.xpath("//div[@id=\"accommodationsBlock\"]//div[@class=\"info-row empty-accommodation\"]")).scrollTo().click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        $(By.xpath("//div[@id=\"accommodationsBlock\"]//div[@class=\"info-row empty-accommodation\"]//div[@class=\"check-wrapper city-selector\"]")).shouldBe(Condition.visible);
        $(By.xpath("//div[@id=\"accommodationsBlock\"]//div[@class=\"info-row empty-accommodation\"]//div[@class=\"check-wrapper city-selector\"]/div/div/div/div/div")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //В первый день добавить экскурсию Бункер-42
        System.out.print("[-] В первый день добавляем экскурсию - Бункер-42: ");
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.ServiceAddButton)).scrollTo().click();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.ServiceAddButton
                + "/div/div[@class=\"icons-block\"]")).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.ServiceAddButton
                + "/div/div[@class=\"icons-block\"]//div[@data-service-type-id=\"3\"]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"click-service-area\"]")).scrollTo().click();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"service-names-list check-wrapper\"]")).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"service-names-list check-wrapper\"]"
                + "//div[@class=\"check-list scroll-pane jspScrollable\"]/div[@class=\"jspContainer\"]"
                + "//div[@group-value=\"B\"]//div[@data-value=\"BUNKER-42\"]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //В второй день добавить экскурсию Большой театр
        /*System.out.print("[-] Во второй день добавляем экскурсию - Большой Театр: ");
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.ServiceAddButton)).scrollTo().click();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.ServiceAddButton
                + "/div/div[@class=\"icons-block\"]")).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.ServiceAddButton
                + "/div/div[@class=\"icons-block\"]//div[@data-service-type-id=\"7\"]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"click-service-area\"]")).scrollTo().click();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"check-wrapper service-names-list\"]")).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"check-wrapper service-names-list\"]"
                + "//div[@class=\"check-list scroll-pane\"]/div[@class=\"jspContainer\"]/div[@class=\"jspPane\"]"
                + "//div[@data-value=\"BOLSHOI THEATRE\"]")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);*/



        //Запускаем расчёт
        System.out.print("[-] Запускаем расчёт: ");
        $(By.xpath(NewQuotationPage.Results.calculateButton)).scrollTo().click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);
        /*
        Заполнить все данные для расчета (маршрут с программой тура)
        Расчитать стоимость тура
        Сгенерировать программу (web-версия + doc-версия)
        */

        $(By.id("result")).scrollTo();
        Double hotelsWE;
        Double hotelsWESS;
        System.out.println("[-] Проверяем, что цены в Totals верные:");

        //hotelsWE = (Double.valueOf(prices.get(0).priceDBLWE)+Double.valueOf(prices.get(1).priceDBLWE))/4.0;
        if(nowDate.getDayOfWeek().getValue() >= 1 & nowDate.getDayOfWeek().getValue()<=4){
            //otelsWE = Double.valueOf((new BigDecimal(Double.valueOf(prices.get(0).priceDBL)/2.0).setScale(0, RoundingMode.DOWN).floatValue()) + (new BigDecimal(Double.valueOf(prices.get(0).priceSGL)/15.0).setScale(0, RoundingMode.DOWN).floatValue()));
            hotelsWE = Double.valueOf((new BigDecimal(Double.valueOf(prices.get(0).priceDBL)/2.0).setScale(0, RoundingMode.DOWN).floatValue()));
            hotelsWESS = Double.valueOf(prices.get(0).priceSGL) - (Double.valueOf(prices.get(0).priceDBL)/2.0);

        }
        else{
            //hotelsWE = Double.valueOf((new BigDecimal(Double.valueOf(prices.get(0).priceDBLWE)/2.0).setScale(0, RoundingMode.DOWN).floatValue()) + (new BigDecimal(Double.valueOf(prices.get(0).priceSGLWE)/15.0).setScale(0, RoundingMode.DOWN).floatValue()));
            hotelsWE = Double.valueOf((new BigDecimal(Double.valueOf(prices.get(0).priceDBLWE)/2.0).setScale(0, RoundingMode.DOWN).floatValue()));
            hotelsWESS = Double.valueOf(prices.get(0).priceSGLWE) - (Double.valueOf(prices.get(0).priceDBLWE)/2.0);
        }
            /*hotelsWE = hotelsWE + Double.valueOf(priceForBunker42) +  Double.valueOf(priceForBunker42)/15.0
                    + Double.valueOf(priceForBolshoiTheatre) + Double.valueOf(priceForBolshoiTheatre)/15.0
                    + 2500.0/15.0 + 850.0/15.0
                    + 2500.0/15.0 + 850.0/15.0;*/
        // добавляем: цену экскурсии, цену гида и цену транфера
        /*hotelsWE = hotelsWE + Double.valueOf(priceForBunker42) + Double.valueOf(priceForBunker42)/15.0
                + 3000.0/15.0 + 3000.0/15.0;*/
        double hotelsWE15 = hotelsWE + Double.valueOf(priceForBunker42)
                + Double.valueOf((new BigDecimal(guidePriceforHalfDay/15.0).setScale(0, RoundingMode.DOWN).floatValue()))
                + Double.valueOf((new BigDecimal(transportPriceHourly18/15.0*4.0).setScale(0, RoundingMode.DOWN).floatValue()));
        double hotelsWE20 = hotelsWE + Double.valueOf(priceForBunker42)
                + Double.valueOf((new BigDecimal(guidePriceforHalfDay/20.0).setScale(0, RoundingMode.DOWN).floatValue()))
                + Double.valueOf((new BigDecimal(transportPriceHourly44/20.0*6.0).setScale(0, RoundingMode.DOWN).floatValue()));
        double hotelsWE25 = hotelsWE + Double.valueOf(priceForBunker42)
                + Double.valueOf((new BigDecimal(guidePriceforHalfDay/25.0).setScale(0, RoundingMode.DOWN).floatValue()))
                + Double.valueOf((new BigDecimal(transportPriceHourly44/25.0*6.0).setScale(0, RoundingMode.DOWN).floatValue()));

        //hotelsWE = Double.valueOf(new BigDecimal(hotelsWE).setScale(0, RoundingMode.DOWN).floatValue());
        //hotelsWE = hotelsWE / rubEur;
        hotelsWE15 = hotelsWE15 / 0.85;
        hotelsWE20 = hotelsWE20 / 0.85;
        hotelsWE25 = hotelsWE25 / 0.85;
        hotelsWESS = hotelsWESS / 0.85;
        String priceDBLDS15 = String.valueOf((int) new BigDecimal(hotelsWE15).setScale(0, RoundingMode.HALF_UP).floatValue());
        String priceDBLDS20 = String.valueOf((int) new BigDecimal(hotelsWE20).setScale(0, RoundingMode.HALF_UP).floatValue());
        String priceDBLDS25 = String.valueOf((int) new BigDecimal(hotelsWE25).setScale(0, RoundingMode.HALF_UP).floatValue());
        String priceDBLDSSS = String.valueOf((int) new BigDecimal(hotelsWESS).setScale(0, RoundingMode.HALF_UP).floatValue());

        //Проверяем для группы 15 человек
        String result = $(By.cssSelector("table#table-result-totals tbody tr:nth-of-type(2) td:nth-of-type(2)")).getText();
        result = result.substring(0, result.indexOf(' '));
        //System.out.println("Из Prices получили:"+priceDBLDS15+" в Totals:"+ result);
        if (result.equals(priceDBLDS15)){
            System.out.println(RFQAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение для группы 15 корректное + "+RFQAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that value in Hotels for 15, is correct")
                    .isEqualTo(priceDBLDS15);
            System.out.println(RFQAppCommonCode.ANSI_RED +"      Значение для группы 15 не некорректное: " + RFQAppCommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем для группы 20 человек
        result = $(By.cssSelector("table#table-result-totals tbody tr:nth-of-type(2) td:nth-of-type(3)")).getText();
        result = result.substring(0, result.indexOf(' '));
        //System.out.println("Из Prices получили:"+priceDBLDS20+" в Totals:"+ result);
        if (result.equals(priceDBLDS20)){
            System.out.println(RFQAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение для группы 20 корректное + "+RFQAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that value in Hotels for 20, is correct")
                    .isEqualTo(priceDBLDS20);
            System.out.println(RFQAppCommonCode.ANSI_RED +"      Значение для группы 20 не некорректное: " + RFQAppCommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем для группы 25 человек
        result = $(By.cssSelector("table#table-result-totals tbody tr:nth-of-type(2) td:nth-of-type(4)")).getText();
        result = result.substring(0, result.indexOf(' '));
        //System.out.println("Из Prices получили:"+priceDBLDS25+" в Totals:"+ result);
        if (result.equals(priceDBLDS25)){
            System.out.println(RFQAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение для группы 25 корректное + "+RFQAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that value in Hotels for 25, is correct")
                    .isEqualTo(priceDBLDS25);
            System.out.println(RFQAppCommonCode.ANSI_RED +"      Значение для группы 25 не некорректное: " + RFQAppCommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем для SS
        result = $(By.cssSelector("table#table-result-totals tbody tr:nth-of-type(2) td:nth-of-type(5)")).getText();
        result = result.substring(0, result.indexOf(' '));
        //System.out.println("Из Prices получили:"+priceDBLDS+" в Totals:"+ result);
        if (result.equals(priceDBLDSSS)){
            System.out.println(RFQAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение SS корректное + "+RFQAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that value in Hotels for SS, is correct")
                    .isEqualTo(priceDBLDSSS);
            System.out.println(RFQAppCommonCode.ANSI_RED +"      Значение SS не некорректное: " + RFQAppCommonCode.ANSI_RESET
                    + result + " -");
        }


    }


    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
