package com.rfqDemoOltatravel.RFQApp;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.rfqDemoOltatravel.PricesApp.PricesAppCommonCode;
import com.rfqDemoOltatravel.QuotationApp.QuotationAppCommonCode;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;

public class QuotationByPeriods {
    public ChromeDriver driver;

    RFQAppCommonCode rfqAppCommonCode = new RFQAppCommonCode();
    PricesAppCommonCode pricesAppCommonCode = new PricesAppCommonCode();
    private SoftAssertions softAssertions;
    boolean isWindows=false;

    @Before
    public void setUp() {

        driver = rfqAppCommonCode.InitializeChromeDriver();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void test(){

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
        $(By.id("username")).setValue(props.getProperty("pricesAppLogin"));
        $(By.id("password")).setValue(props.getProperty("pricesAppPassword"));
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

        //Открываем групповые цены
        System.out.print("[-] Открываем групповые цены");
        $(By.cssSelector("li[id=\"group\"]")).click();
        /*System.out.print("[-] Открываем средне-индивидуальные цены");
        $(By.cssSelector("li[id=\"individualAverage\"]")).click();*/
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем текущий день
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatForPrices = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        //System.out.println(ldt.format(formatForDate));
        //System.out.println(ldt.format(formatForPrices));

        System.out.println("[-] Сохраняем значения для периодов для SPB, Hotel 4* central");
        List<PricesAppCommonCode.PeriodsCollection> periodsListSPB;
        periodsListSPB = pricesAppCommonCode.SavePeriodsForACityAndHotelType("SPB", "Hotel 4* central");

        System.out.println("[-] Сохраняем значения для периодов для MSK, Hotel 4* central");
        List<PricesAppCommonCode.PeriodsCollection> periodsListMSK
                = pricesAppCommonCode.SavePeriodsForACityAndHotelType("MSK", "Hotel 4* central");

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
        $(By.id("username")).setValue(props.getProperty("rfqAppLogin"));
        $(By.id("password")).setValue(props.getProperty("rfqAppPassword"));
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
        $(By.cssSelector(NewQuotationPage.ChooseClientNamePopup.searchField)).sendKeys("test client");
        $(By.xpath(NewQuotationPage.chooseClientNamePopupXP
                + "//div[@class=\"check-list scroll-pane\"]//div[@class=\"jspContainer\"]//div[@class=\"jspPane\"]"
                + "//div[@group-value=\"T\"]//div[@class=\"check-wrap\"]//span[text()=\"Test Client\"]")).shouldBe(Condition.visible).hover().click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Выбираем Currency
        System.out.print("[-] Выставляем валюту - RUB: ");
        $(By.cssSelector(NewQuotationPage.Options.currencyButton)).scrollTo().click();
        $(By.cssSelector(NewQuotationPage.Options.currencySelectors)).shouldBe(Condition.visible).click();
        $(By.xpath(NewQuotationPage.Options.currencyRUBXP)).shouldBe(Condition.visible).hover().click();
        //if(isWindows){$(By.xpath(NewQuotationPage.Options.currencyRUBXP)).click();}
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
        System.out.print("[-] Выставляем Preset Meal Services - NO: ");
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesButton)).scrollTo().click();
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesButton)).hover().click();
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesSelectors)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.presentMealServiceNO)).hover().click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Переключаемся на периоды
        System.out.print("[-] Переключаемся на периоды");
        $(By.xpath(NewQuotationPage.DatesPeriods.periodsSwitchButton)).scrollTo().hover().click();
        //$(By.xpath(NewQuotationPage.Dates.periodsSwitchButton)).hover().click();
        confirm();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Заполняем даты
        System.out.print("[-] Заполняем период: 01.01.2018 - 31.12.2018");
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.DatesPeriods.firstIntervalPeriodsFromInput)).click();

        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.DatesPeriods.firstIntervalPeriodsFromInput)).setValue("01.01.2018");
        $(By.cssSelector(NewQuotationPage.DatesPeriods.firstIntervalPeriodsToInput)).setValue("31.12.2018").pressEnter();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);


    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
