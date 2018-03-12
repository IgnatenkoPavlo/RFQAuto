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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
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
        System.out.println(PricesAppCommonCode.OK);

        //Открываем групповые цены
        System.out.print("[-] Открываем групповые цены");
        $(By.cssSelector("li[id=\"group\"]")).click();
        /*System.out.print("[-] Открываем средне-индивидуальные цены");
        $(By.cssSelector("li[id=\"individualAverage\"]")).click();*/
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(PricesAppCommonCode.OK);

        //Открываем текущий день
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatForPrices = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        //System.out.println(ldt.format(formatForDate));
        //System.out.println(ldt.format(formatForPrices));

        System.out.println("        Сохраняем значения для периодов для SPB, Hotel 4* central");
        List<PricesAppCommonCode.PeriodsCollection> periodsListSPB
                = pricesAppCommonCode.SavePeriodsForACityAndHotelType("SPB", "Hotel 4* central");

        /*System.out.print("[-] Открываем средне-индивидуальные цены");
        $(By.cssSelector("li[id=\"individualAverage\"]")).click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");*/

        System.out.println("        Сохраняем значения для периодов для MSK, Hotel 4* central");
        List<PricesAppCommonCode.PeriodsCollection> periodsListMSK
                = pricesAppCommonCode.SavePeriodsForACityAndHotelType("MSK", "Hotel 4* central");

        //Открываем цены на поезда
        System.out.print("[-] Получаем цену на поезда MSK - SPB");
        $(By.cssSelector("li[id=\"train-tickets\"]")).click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(PricesAppCommonCode.OK);

        String trainTicketsRegular = "none";

        //Выбираем город - MSK
        System.out.print("        Выбираем город - MSK");
        $(By.cssSelector(PricesAppCommonCode.CitiesSelection.MSKButton)).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(PricesAppCommonCode.OK);

        //Выбираем год - 2018
        System.out.print("        Выбираем год - 2018");
        $(By.xpath(PricesAppCommonCode.YearSelection.year2018XP)).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(PricesAppCommonCode.OK);

        //Сохраняем Train Tickets с типом питания - кухни мира
        System.out.print("        Сохраняем Train Tickets с типом Night Train ");
        trainTicketsRegular = "SAPSAN TRAIN MOSCOW - SAINT PETERSBURG";
        trainTicketsRegular = $(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                    + PricesAppCommonCode.ServicesPriceTable.ServiceStringByName("SAPSAN TRAIN MOSCOW - SAINT PETERSBURG")
                    + "//td[@data-class=\"2nd\"]/table/tbody/tr/td")).getText();
        //System.out.println(trainTicketsRegular);
        System.out.println(PricesAppCommonCode.OK);

        //Выходим из Prices
        System.out.print("[-] Выходим из Prices");
        $(By.xpath("//div[@id=\"profile\"]")).click();
        $(By.xpath("//button[@id=\"btn-logout\"]")).shouldBe(Condition.visible).click();
        System.out.println(PricesAppCommonCode.OK);

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
        $(By.cssSelector(NewQuotationPage.Options.currencyButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.currencySelectors)).shouldBe(Condition.visible).click();
        $(By.xpath(NewQuotationPage.Options.currencyRUBXP)).shouldBe(Condition.visible).hover().click();
        //if(isWindows){$(By.xpath(NewQuotationPage.Options.currencyRUBXP)).click();}
        $(By.xpath(NewQuotationPage.Options.currencyRUBXP)).shouldNotBe(Condition.visible);
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Выставляем Nights
        System.out.print("[-] Выставляем  ночи: ");
        $(By.xpath(NewQuotationPage.Options.nightsButtonXP)).scrollTo().click();
        $(By.xpath(NewQuotationPage.Options.nightsInputXP)).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Options.nightsInputXP)).setValue("2");
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

        //Выбираем Present Menu
        System.out.print("[-] Выставляем Preset Menu - Indian: ");
        $(By.cssSelector(NewQuotationPage.Options.presentMenuButton)).scrollTo().click();
        $(By.cssSelector(NewQuotationPage.Options.presentMenuButton)).hover().click();
        $(By.cssSelector(NewQuotationPage.Options.presentMenuSelectors)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.presentMenuIndian)).hover().click();
        //$(By.cssSelector(NewQuotationPage.Options.presentMealServiceNO)).click();
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

        //Выбираем Москву
        System.out.print("[-] Добавляем размещение - Москва: ");
        $(By.xpath(NewQuotationPage.Accommodations.addCityToLastPossiblePositionControlXP)).scrollTo().click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        $(By.xpath(NewQuotationPage.accommodationsAreaXP+"//div[@class=\"info-row empty-accommodation\"]//div[@class=\"check-wrapper city-selector\"]")).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.accommodationsAreaXP+"//div[@class=\"info-row empty-accommodation\"]//div[@class=\"check-wrapper city-selector\"]/div/div/div/div/div")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Меняем для MSK к-во ночей на 1
        System.out.print("[-] Меняем для MSK к-во ночей на 1");
        $(By.xpath(NewQuotationPage.Accommodations.CityByNumberXP(1)
                +NewQuotationPage.Accommodations.cityNightXP)).scrollTo().setValue("1").pressEnter();
        //confirm();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Добавляем город
        System.out.print("[-] Добавляем город: SPB");
        //Кликаем Add
        $(By.xpath(NewQuotationPage.Accommodations.addCityToLastPossiblePositionControlXP)).click();
        //Ждём появления меню
        $(By.xpath(NewQuotationPage.accommodationsAreaXP+"//div[@class=\"info-row empty-accommodation\"]//div[@class=\"check-wrapper city-selector\"]")).shouldBe(Condition.visible);
        //Кликаем по кнопке с SPB
        $(By.xpath(NewQuotationPage.accommodationsAreaXP+"//div[@class=\"info-row empty-accommodation\"]//div[@class=\"check-wrapper city-selector\"]/div/div/div//div[2]/div")).click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Запускаем расчёт
        System.out.print("[-] Запускаем расчёт: ");
        $(By.xpath(NewQuotationPage.Results.calculateButton)).scrollTo().click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Проверяем, что колличество периодов верное
        $(By.xpath(NewQuotationPage.Results.totalsWE)).scrollTo();
        int periodsInResult = $$(By.xpath(NewQuotationPage.Results.totalsWE +"//tr")).size()-1;
        int periodsFromPrices = periodsListSPB.size();
        System.out.println("[-] Проверяем, что колличество периодов в Results верное:");
        if (periodsInResult == periodsFromPrices){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(periodsInResult)
                    .as("Check that number of periods in Results is correct")
                    .isEqualTo(periodsFromPrices);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение не некорректные: " + QuotationAppCommonCode.ANSI_RESET
                    + periodsInResult + " -");
        }

        //Проверяем, что даты периодов верные
        System.out.println("[-] Проверяем, что даты периодов верные:");
        DateTimeFormatter formatForResultsDayOnly = DateTimeFormatter.ofPattern("d")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatForResultsDayMonthOnly = DateTimeFormatter.ofPattern("d MMMM")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatForResultsFull = DateTimeFormatter.ofPattern("d MMMM yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        String result;
        String composedPeriodValue;

        for(int periodsCounter=1; periodsCounter <= periodsListSPB.size(); periodsCounter++){

            result = $(By.xpath(NewQuotationPage.Results.totalsWE
                    + NewQuotationPage.Results.DatesOfPeriodByNumber(periodsCounter))).getText();

            if(periodsListSPB.get(periodsCounter-1).dateFrom.getMonth() == periodsListSPB.get(periodsCounter-1).dateTo.getMonth())
                composedPeriodValue = periodsListSPB.get(periodsCounter-1).dateFrom.format(formatForResultsDayOnly)+" - "+periodsListSPB.get(periodsCounter-1).dateTo.format(formatForResultsFull);
            else
                composedPeriodValue = periodsListSPB.get(periodsCounter-1).dateFrom.format(formatForResultsDayMonthOnly)+" - "+periodsListSPB.get(periodsCounter-1).dateTo.format(formatForResultsFull);

            if(periodsListSPB.get(periodsCounter-1).dateFrom.getYear() != periodsListSPB.get(periodsCounter-1).dateTo.getYear())
                composedPeriodValue = periodsListSPB.get(periodsCounter-1).dateFrom.format(formatForResultsFull)+" - "+periodsListSPB.get(periodsCounter-1).dateTo.format(formatForResultsFull);

            if (result.equals(composedPeriodValue)){
                System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение у периода "+periodsCounter+" корректное + "+ QuotationAppCommonCode.ANSI_RESET);
            } else {
                softAssertions.assertThat(result)
                        .as("Check that dates of periods in Results is correct")
                        .isEqualTo(composedPeriodValue);
                System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение у периода "+periodsCounter+" не некорректные: " + QuotationAppCommonCode.ANSI_RESET
                        + result + " -");
            }
        }

        //Проверяем, что цены в Hotels (WE) w/o margin верные
        double hotelsWEMSK;
        double hotelsWESPB;
        double hotelsWE;
        String priceDBLDS;
        System.out.println("[-] Проверяем, что цены в Hotels (WE) верные:");
        for(int periodsCounter=1; periodsCounter <= periodsListSPB.size(); periodsCounter++){

            hotelsWESPB = new BigDecimal(periodsListSPB.get(periodsCounter-1).priceDBLWE/2.0).setScale(0, RoundingMode.DOWN).floatValue();
            hotelsWEMSK = new BigDecimal(periodsListMSK.get(0).priceDBLWE/2.0).setScale(0, RoundingMode.DOWN).floatValue();

            hotelsWE = hotelsWESPB + hotelsWEMSK + 300.0 + Double.valueOf(trainTicketsRegular);

            hotelsWE = hotelsWE / 0.85;

            priceDBLDS = String.valueOf((int) new BigDecimal(hotelsWE).setScale(0, RoundingMode.HALF_UP).floatValue());

            //priceDBLDS = String.valueOf((int) new BigDecimal(Double.valueOf(periodsListSPB.get(periodsCounter-1).priceDBLWE)/2.0/0.85+200.0).setScale(0, RoundingMode.HALF_UP).floatValue());

            //System.out.println(priceDBLDS);
            result = $(By.xpath(NewQuotationPage.Results.totalsWE
                    + NewQuotationPage.Results.PeriodByNumber(periodsCounter)
                    + NewQuotationPage.Results.GroupByNumber(1))).getText();
            //System.out.println(result);
            result = result.substring(0, result.indexOf(' '));
            //System.out.println(result);

            if (result.equals(priceDBLDS)){
                System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение у периода "+periodsCounter+" корректное + "+ QuotationAppCommonCode.ANSI_RESET);
            } else {
                softAssertions.assertThat(result)
                        .as("Check that value in Hotels(WE) for 15, for period "+periodsCounter+",is correct")
                        .isEqualTo(priceDBLDS);
                System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение у периода "+periodsCounter+" не некорректные: " + QuotationAppCommonCode.ANSI_RESET
                        + result + " -");
            }
        }

        //Проверяем, что цены в Hotels (WD) w/o margin верные
        $(By.xpath(NewQuotationPage.Results.totalsWD
                + NewQuotationPage.Results.PeriodByNumber(1))).scrollTo();
        System.out.println("[-] Проверяем, что цены в Hotels (WD) верные:");
        for(int periodsCounter=1; periodsCounter <= periodsListSPB.size(); periodsCounter++){
            /*priceDBLDS = String.valueOf((int) new BigDecimal(Double.valueOf(periodsListSPB.get(periodsCounter-1).priceDBL)/2.0/0.85
                    +Double.valueOf(235)+Double.valueOf(periodsListMSK.get(0).priceDBL)/2.0/0.85).setScale(0, RoundingMode.HALF_UP).floatValue());*/

            hotelsWESPB = new BigDecimal(periodsListSPB.get(periodsCounter-1).priceDBL/2.0).setScale(0, RoundingMode.DOWN).floatValue();
            hotelsWEMSK = new BigDecimal(periodsListMSK.get(0).priceDBL/2.0).setScale(0, RoundingMode.DOWN).floatValue();

            hotelsWE = hotelsWESPB + hotelsWEMSK + 300.0 + Double.valueOf(trainTicketsRegular);

            hotelsWE = hotelsWE / 0.85;

            priceDBLDS = String.valueOf((int) new BigDecimal(hotelsWE).setScale(0, RoundingMode.HALF_UP).floatValue());
            result = $(By.xpath(NewQuotationPage.Results.totalsWD
                    + NewQuotationPage.Results.PeriodByNumber(periodsCounter)
                    + NewQuotationPage.Results.GroupByNumber(1))).getText();
            result = result.substring(0, result.indexOf(' '));

            if (result.equals(priceDBLDS)){
                System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение у периода "+periodsCounter+" корректное + "+ QuotationAppCommonCode.ANSI_RESET);
            } else {
                softAssertions.assertThat(result)
                        .as("Check that value in Hotels (WD) for 15, for period "+periodsCounter+",is correct")
                        .isEqualTo(priceDBLDS);
                System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение у периода "+periodsCounter+" не некорректные: " + QuotationAppCommonCode.ANSI_RESET
                        + result + " -");
            }
        }

    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
