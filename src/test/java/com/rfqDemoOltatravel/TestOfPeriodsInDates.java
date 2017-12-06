package com.rfqDemoOltatravel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class TestOfPeriodsInDates {

    public ChromeDriver driver;

    private SoftAssertions softAssertions;
    CommonCode commonCode = new CommonCode();



    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Automation\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void testOfPeriodsInDates() {

        WebDriverRunner.setWebDriver(driver);
        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;

        Properties props=new Properties();
        try {
            props.load(new InputStreamReader(new FileInputStream("target\\test-classes\\application.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Получил проперти ");
        System.out.println(props.getProperty("baseURL"));
        System.out.print("[-] Открываем URL: "+props.getProperty("baseURL"));
        open(props.getProperty("baseURL"));
        commonCode.WaitForPageToLoad(driver);
        System.out.println(" - готово");


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("test");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(" - готово");

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем приложение Prices");
        open(props.getProperty("baseURL")+"/application/olta.prices");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем групповые цены
        System.out.print("[-] Открываем групповые цены");
        $(By.cssSelector("li[id=\"group\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем текущий день
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatForPrices = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        //System.out.println(ldt.format(formatForDate));
        //System.out.println(ldt.format(formatForPrices));

        //Скролим к 2017 году
        System.out.println("[-] Сохраняем значения для периодов для SPB, Hotel 4* central");
        List<CommonCode.PeriodsCollection> periodsListSPB
                = commonCode.SavePeriodsForACityAndHotelType("SPB", "Hotel 4* central");

        System.out.println("[-] Сохраняем значения для периодов для MSK, Hotel 4* central");
        List<CommonCode.PeriodsCollection> periodsListMSK
                = commonCode.SavePeriodsForACityAndHotelType("MSK", "Hotel 4* central");;


        /*System.out.println("For SPB");
        for(int i=0;i<periodsListSPB.size();i++) {
            System.out.println(periodsListSPB.get(i).dateTo);
            System.out.println(periodsListSPB.get(i).priceDBLWE);
        }
        System.out.println("");
        System.out.println("For MSK");
        for(int i=0;i<periodsListMSK.size();i++) {
            System.out.println(periodsListMSK.get(i).dateTo);
            System.out.println(periodsListMSK.get(i).priceDBLWE);
        }*/

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение ");
        open(props.getProperty("baseURL")+"/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(" - готово");

        //Создаём новый Quotation
        NewQuotationPage.CreateQuotation("PTestQuotation1", "Тест компания");

        NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Ждём пока страница прогрузится
        commonCode.WaitForProgruzka();

        //Выставляем курс Евро
        System.out.print("[-] Выставляем курс евро 70");
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");
        Double rubEur = 0.0;
        rubEur = Double.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText());

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 2;
        System.out.print("[-] Меняем количество ночей на " + nightInOptionsCounter+ ": ");
        NewQuotationPage.OptionsTable.SetNumberOfNightsInOptions(nightInOptionsCounter);
        System.out.println(" - готово");

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(NewQuotationPage.OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(" - готово");

        //Сохраняем значение комиссии за бронь в SPB
        System.out.print("[-] Сохраняем значение комиссии за бронь в SPB");
        int registrationFeeForSPB = Integer.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.registrationFeeForSPB)).getText());
        System.out.println(" - Готово");

        //Переключаемся на Periods в Dates/Periods
        System.out.print("[-] Переключаемся на Periods в Dates/Periods");
        $(By.cssSelector("span#qbtn-periods")).click();
        driver.switchTo().alert().accept();
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Выставляем период с 01-01-2017 до 31-12-2017
        System.out.print("[-] Выставляем период с 01-01-2017 до 31-12-2017");
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.addPeriodButton)).click();

        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.fromPeriodInputField)).click();
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.fromPeriodInputField)).sendKeys("01-01-2017");

        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.toPeriodInputField)).click();
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.toPeriodInputField)).sendKeys("31-12-2017");

        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.savePeriodButton)).click();

        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Добавляем город
        System.out.print("[-] Добавляем город: MSK");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).shouldBe(visible);
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Меняем для MSK к-во ночей на 1
        System.out.print("[-] Меняем для MSK к-во ночей на 1");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +NewQuotationPage.AccomodationsTable.nightsCounterForCityREG)).scrollTo().setValue("1").pressEnter();
        Alert alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Добавляем город
        System.out.print("[-] Добавляем город: SPB");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).shouldBe(visible);
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        System.out.print("[-] Запускаем расчёт ");
        $(By.id("qbtn-execute")).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Проверяем, что колличество периодов верное
        $(By.cssSelector("div#results table#table-result-hotels-wo-margin-we tbody tr")).scrollTo();
        int periodsInResult = $$(By.cssSelector("div#results table#table-result-hotels-wo-margin-we tbody tr")).size();
        int periodsFromPrices = periodsListSPB.size();
        System.out.println("[-] Проверяем, что колличество периодов в Results верное:");
        if (periodsInResult == periodsFromPrices){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(periodsInResult)
                    .as("Check that number of periods in Results is correct")
                    .isEqualTo(periodsFromPrices);
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
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

            result = $(By.cssSelector("div#results table#table-result-hotels-wo-margin-we tbody tr:nth-of-type("+(periodsCounter)+") th")).getText();

            if(periodsListSPB.get(periodsCounter-1).dateFrom.getMonth() == periodsListSPB.get(periodsCounter-1).dateTo.getMonth())
                composedPeriodValue = periodsListSPB.get(periodsCounter-1).dateFrom.format(formatForResultsDayOnly)+" - "+periodsListSPB.get(periodsCounter-1).dateTo.format(formatForResultsFull);
            else
                composedPeriodValue = periodsListSPB.get(periodsCounter-1).dateFrom.format(formatForResultsDayMonthOnly)+" - "+periodsListSPB.get(periodsCounter-1).dateTo.format(formatForResultsFull);

            if(periodsListSPB.get(periodsCounter-1).dateFrom.getYear() != periodsListSPB.get(periodsCounter-1).dateTo.getYear())
                composedPeriodValue = periodsListSPB.get(periodsCounter-1).dateFrom.format(formatForResultsFull)+" - "+periodsListSPB.get(periodsCounter-1).dateTo.format(formatForResultsFull);

            if (result.equals(composedPeriodValue)){
                System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение у периода "+periodsCounter+" корректное + "+CommonCode.ANSI_RESET);
            } else {
                softAssertions.assertThat(result)
                        .as("Check that dates of periods in Results is correct")
                        .isEqualTo(composedPeriodValue);
                System.out.println(CommonCode.ANSI_RED +"      Значение у периода "+periodsCounter+" не некорректные: " + CommonCode.ANSI_RESET
                        + result + " -");
            }
        }

        //Проверяем, что цены в Hotels (WE) w/o margin верные
        String priceDBLDS;
        System.out.println("[-] Проверяем, что цены в Hotels (WE) w/o margin верные:");
        for(int periodsCounter=2; periodsCounter <= periodsListSPB.size(); periodsCounter++){
            priceDBLDS = String.valueOf((int) new BigDecimal(Double.valueOf(periodsListSPB.get(periodsCounter-1).priceDBLWE)/2.0
                    +Double.valueOf(registrationFeeForSPB)+Double.valueOf(periodsListMSK.get(0).priceDBLWE)/2.0).setScale(0, RoundingMode.HALF_UP).floatValue());

            result = $(By.cssSelector("div#results table#table-result-hotels-wo-margin-we tbody tr:nth-of-type("+(periodsCounter)+") td")).getText();
            result = result.substring(0, result.indexOf(' '));

            if (result.equals(priceDBLDS)){
                System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение у периода "+periodsCounter+" корректное + "+CommonCode.ANSI_RESET);
            } else {
                softAssertions.assertThat(result)
                        .as("Check that value in Hotels (WE) w/o margin for 15, for period "+periodsCounter+",is correct")
                        .isEqualTo(priceDBLDS);
                System.out.println(CommonCode.ANSI_RED +"      Значение у периода "+periodsCounter+" не некорректные: " + CommonCode.ANSI_RESET
                        + result + " -");
            }
        }

        //Проверяем, что цены в Hotels (WD) w/o margin верные

        System.out.println("[-] Проверяем, что цены в Hotels (WD) w/o margin верные:");
        for(int periodsCounter=2; periodsCounter <= periodsListSPB.size(); periodsCounter++){
            priceDBLDS = String.valueOf((int) new BigDecimal(Double.valueOf(periodsListSPB.get(periodsCounter-1).priceDBL)/2.0
                    +Double.valueOf(registrationFeeForSPB)+Double.valueOf(periodsListMSK.get(0).priceDBL)/2.0).setScale(0, RoundingMode.HALF_UP).floatValue());

            result = $(By.cssSelector("div#results table#table-result-hotels-wo-margin-wd tbody tr:nth-of-type("+(periodsCounter)+") td")).getText();
            result = result.substring(0, result.indexOf(' '));

            if (result.equals(priceDBLDS)){
                System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение у периода "+periodsCounter+" корректное + "+CommonCode.ANSI_RESET);
            } else {
                softAssertions.assertThat(result)
                        .as("Check that value in Hotels (WD) w/o margin for 15, for period "+periodsCounter+",is correct")
                        .isEqualTo(priceDBLDS);
                System.out.println(CommonCode.ANSI_RED +"      Значение у периода "+periodsCounter+" не некорректные: " + CommonCode.ANSI_RESET
                        + result + " -");
            }
        }


        //Проверяем, что цены в Hotels (WE) верные
        Double hotelsWE;
        System.out.println("[-] Проверяем, что цены в Hotels (WE) верные:");
        for(int periodsCounter=2; periodsCounter <= periodsListSPB.size(); periodsCounter++){
            hotelsWE = Double.valueOf(periodsListSPB.get(periodsCounter-1).priceDBLWE)/2.0
                    +Double.valueOf(registrationFeeForSPB)+Double.valueOf(periodsListMSK.get(0).priceDBLWE)/2.0;
            hotelsWE = hotelsWE / rubEur;
            hotelsWE = hotelsWE / generalMarge;
            priceDBLDS = String.valueOf((int) new BigDecimal(hotelsWE).setScale(0, RoundingMode.HALF_UP).floatValue());

            result = $(By.cssSelector("div#results table#table-result-hotels-we tbody tr:nth-of-type("+(periodsCounter)+") td")).getText();
            result = result.substring(0, result.indexOf('€'));

            if (result.equals(priceDBLDS)){
                System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение у периода "+periodsCounter+" корректное + "+CommonCode.ANSI_RESET);
            } else {
                softAssertions.assertThat(result)
                        .as("Check that value in Hotels (WE) for 15, for period "+periodsCounter+",is correct")
                        .isEqualTo(priceDBLDS);
                System.out.println(CommonCode.ANSI_RED +"      Значение у периода "+periodsCounter+" не некорректные: " + CommonCode.ANSI_RESET
                        + result + " -");
            }
        }

        System.out.println("[-] Проверяем, что цены в Hotels (WD) верные:");
        for(int periodsCounter=2; periodsCounter <= periodsListSPB.size(); periodsCounter++){
            hotelsWE = Double.valueOf(periodsListSPB.get(periodsCounter-1).priceDBL)/2.0
                    +Double.valueOf(registrationFeeForSPB)+Double.valueOf(periodsListMSK.get(0).priceDBL)/2.0;
            hotelsWE = hotelsWE / rubEur;
            hotelsWE = hotelsWE / generalMarge;
            priceDBLDS = String.valueOf((int) new BigDecimal(hotelsWE).setScale(0, RoundingMode.HALF_UP).floatValue());

            result = $(By.cssSelector("div#results table#table-result-hotels-wd tbody tr:nth-of-type("+(periodsCounter)+") td")).getText();
            result = result.substring(0, result.indexOf('€'));

            if (result.equals(priceDBLDS)){
                System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение у периода "+periodsCounter+" корректное + "+CommonCode.ANSI_RESET);
            } else {
                softAssertions.assertThat(result)
                        .as("Check that value in Hotels (WD) for 15, for period "+periodsCounter+",is correct")
                        .isEqualTo(priceDBLDS);
                System.out.println(CommonCode.ANSI_RED +"      Значение у периода "+periodsCounter+" не некорректные: " + CommonCode.ANSI_RESET
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
