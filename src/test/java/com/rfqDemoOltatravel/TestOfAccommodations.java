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

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class TestOfAccommodations {
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
    public void testOfAccommodations() {

        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;
        System.out.print("[-] Открываем URL: http://rfq-demo.oltatravel.com/");
        open("http://rfq-demo.oltatravel.com/");
        commonCode.WaitForPageToLoad(driver);
        System.out.println(" - Готово");


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("test");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(" - Готово");

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open("http://rfq-demo.oltatravel.com/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(" - Готово");

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(" - Готово");

        //Создаём новый Quotation
        NewQuotationPage.CreateQuotation("PTestQuotation1", "Тест компания");
        NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем курс Евро
        System.out.println("[-] Выставляем курс евро 70");
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
        CommonCode.WaitForProgruzkaSilent();
        Double rubEur = 0.0;
        rubEur = Double.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText());

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 5;
        System.out.println("[-] Меняем количество ночей на " + nightInOptionsCounter);
        NewQuotationPage.OptionsTable.SetNumberOfNights(nightInOptionsCounter);

        //Выставляем дату
        Instant nowDate = Instant.now();
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        System.out.print("[-] Добавляем новую дату: " + formatForDate.format(nowDate));
        //Кликаем на кнопку Add
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.addDateButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).click();
        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));

        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).setValue(formatForDate.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.saveDateButton)).click();
        System.out.println(" - Готово");

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
        System.out.println(" - Готово");

        //Проверяем значения Nights в таблице Accommodations
        //Сохраняем Nights Total из таблицы Accommodations
        String nightsTotalIndicator =
                $(By.cssSelector(NewQuotationPage.AccomodationsTable.nightsAvailableUsedIndicator)).scrollTo().getText();
        String nightsUsed = nightsTotalIndicator.substring(0, (nightsTotalIndicator.indexOf('/')));
        String nightsTotal =
                nightsTotalIndicator.substring(nightsTotalIndicator.indexOf('/')+1, nightsTotalIndicator.length());
        System.out.println("[-] Проверяем, что Night Total в таблице Accommodation посчитан корректно:");

        if (nightsUsed.equals(String.valueOf(nightInOptionsCounter))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение использованных ночей корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsUsed)
                    .as("Check that number of total used night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(nightInOptionsCounter));
            System.out.println(CommonCode.ANSI_RED +"      Значение использованных ночей некорректное: "
                    + CommonCode.ANSI_RESET+ nightsUsed);
        }

        if (nightsTotal.equals(String.valueOf(nightInOptionsCounter))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение общего количества ночей корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsTotal)
                    .as("Check that number of total number of night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(nightInOptionsCounter));
            System.out.println(CommonCode.ANSI_RED +"      Значение общего количества ночей некорректное: "
                    + CommonCode.ANSI_RESET+ nightsTotal);
        }

        //Выставляем значение ночей для отеля в 1
        System.out.print("[-] Выставляем значение ночей для первого размещения в 1");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +NewQuotationPage.AccomodationsTable.nightsCounterForCityREG)).scrollTo().setValue("1").pressEnter();
        Alert alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        nightsTotalIndicator =
                $(By.cssSelector(NewQuotationPage.AccomodationsTable.nightsAvailableUsedIndicator)).scrollTo().getText();
        nightsUsed = nightsTotalIndicator.substring(0, (nightsTotalIndicator.indexOf('/')));
        nightsTotal =
                nightsTotalIndicator.substring(nightsTotalIndicator.indexOf('/')+1, nightsTotalIndicator.length());
        System.out.println("[-] Проверяем, что Night Total в таблице Accommodation посчитан корректно:");

        if (nightsUsed.equals(String.valueOf(1))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение использованных ночей корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsUsed)
                    .as("Check that number of total used night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(1));
            System.out.println(CommonCode.ANSI_RED +"      Значение использованных ночей некорректное: "
                    + CommonCode.ANSI_RESET+ nightsUsed);
        }

        if (nightsTotal.equals(String.valueOf(5))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение общего количества ночей корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsTotal)
                    .as("Check that number of total number of night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(5));
            System.out.println(CommonCode.ANSI_RED +"      Значение общего количества ночей некорректное: "
                    + CommonCode.ANSI_RESET+ nightsTotal);
        }

        //Проверяем что в Program верные значения
        //Получаем колличество дней
        System.out.println("[-] Проверяем, что количество дней,после изменения Nights, в секции Program корректное:");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        int numberOfDaysInProgram = Integer.valueOf($$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size());
        if (numberOfDaysInProgram == 2){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(2);
            System.out.println(CommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + CommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }

        System.out.println("[-] Проверяем, что количество сервисов в днях корректное :");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        int dayCounterMax = 2;
        int numberOfServices;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            numberOfServices = 0;
            System.out.println("      - для дня номер " + dayCounter+":");
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + "//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++) {
                System.out.print("          - для города номер " + cityCounter+": ");
                numberOfServices = Integer.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)+"//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size());
                if(numberOfServices==3){
                    System.out.println(CommonCode.ANSI_GREEN+"3 сервиса как и должно быть"+ CommonCode.ANSI_RESET);
                }else{
                    System.out.println(CommonCode.ANSI_RED+numberOfServices+" сервиса, что является ошибкой"+ CommonCode.ANSI_RESET);
                    softAssertions.assertThat(numberOfServices)
                            .as("Check that number of services in day[%s] in city[%s] is correct.", String.valueOf(dayCounter), String.valueOf(cityCounter))
                            .isEqualTo(3);
                }
            }
        }

        //Выставляем имя отеля в размещении 1
        System.out.print("[-] Выставляем отель Alfa для первого размещения:");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +NewQuotationPage.AccomodationsTable.togglePricesOfCityREG)).scrollTo().click();
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +"//td[@class=\"prices\"]//table//tbody//tr//select[@class=\"hotel\"]")).scrollTo().selectOptionContainingText("Alfa");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Добавляем MSK
        System.out.print("[-] Добавляем город: MSK");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).shouldBe(visible);
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Выставляем для него значение ночей в 1
        System.out.print("[-] Выставляем значение ночей для размещения два в 1");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(2)
                +NewQuotationPage.AccomodationsTable.nightsCounterForCityREG)).scrollTo().setValue("1").pressEnter();
        alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        nightsTotalIndicator =
                $(By.cssSelector(NewQuotationPage.AccomodationsTable.nightsAvailableUsedIndicator)).scrollTo().getText();
        nightsUsed = nightsTotalIndicator.substring(0, (nightsTotalIndicator.indexOf('/')));
        nightsTotal =
                nightsTotalIndicator.substring(nightsTotalIndicator.indexOf('/')+1, nightsTotalIndicator.length());
        System.out.println("[-] Проверяем, что Night Total в таблице Accommodation посчитан корректно:");

        if (nightsUsed.equals(String.valueOf(2))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение использованных ночей корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsUsed)
                    .as("Check that number of total used night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(2));
            System.out.println(CommonCode.ANSI_RED +"      Значение использованных ночей некорректное: "
                    + CommonCode.ANSI_RESET+ nightsUsed);
        }

        if (nightsTotal.equals(String.valueOf(5))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение общего количества ночей корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsTotal)
                    .as("Check that number of total number of night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(5));
            System.out.println(CommonCode.ANSI_RED +"      Значение общего количества ночей некорректное: "
                    + CommonCode.ANSI_RESET+ nightsTotal);
        }

        //Выставляем имя отеля2
        System.out.print("[-] Выставляем отель Mandarin для второго размещения:");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(2)
                +NewQuotationPage.AccomodationsTable.togglePricesOfCityREG)).scrollTo().click();
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(2)
                +"//td[@class=\"prices\"]//table//tbody//tr//select[@class=\"hotel\"]")).scrollTo().selectOptionContainingText("Mandarin");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Проверяем что к-во дней в Program верное
        //Получаем колличество дней
        System.out.println("[-] Проверяем, что количество дней в секции Program корректное:");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        numberOfDaysInProgram = Integer.valueOf($$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size());
        if (numberOfDaysInProgram == 3){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(3);
            System.out.println(CommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + CommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }
        System.out.println("[-] Проверяем, что количество сервисов в днях корректное :");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        dayCounterMax = 3;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            numberOfServices = 0;
            System.out.println("      - для дня номер " + dayCounter+":");
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + "//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++) {
                System.out.print("          - для города номер " + cityCounter+": ");
                numberOfServices = Integer.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)+"//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size());
                if(numberOfServices==3){
                    System.out.println(CommonCode.ANSI_GREEN+"3 сервиса как и должно быть"+ CommonCode.ANSI_RESET);
                }else{
                    System.out.println(CommonCode.ANSI_RED+numberOfServices+" сервиса, что является ошибкой"+ CommonCode.ANSI_RESET);
                    softAssertions.assertThat(numberOfServices)
                            .as("Check that number of services in day[%s] in city[%s] is correct.", String.valueOf(dayCounter), String.valueOf(cityCounter))
                            .isEqualTo(3);
                }
            }
        }

        //Добавляем Питер
        System.out.print("[-] Добавляем город: SPB");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).shouldBe(visible);
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Выставляем для него значение ночей в 2
        System.out.print("[-] Выставляем значение ночей для SPB два в 2");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(3)
                +NewQuotationPage.AccomodationsTable.nightsCounterForCityREG)).scrollTo().setValue("2").pressEnter();
        alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        nightsTotalIndicator =
                $(By.cssSelector(NewQuotationPage.AccomodationsTable.nightsAvailableUsedIndicator)).scrollTo().getText();
        nightsUsed = nightsTotalIndicator.substring(0, (nightsTotalIndicator.indexOf('/')));
        nightsTotal =
                nightsTotalIndicator.substring(nightsTotalIndicator.indexOf('/')+1, nightsTotalIndicator.length());
        System.out.println("[-] Проверяем, что Night Total в таблице Accommodation посчитан корректно:");

        if (nightsUsed.equals(String.valueOf(4))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение использованных ночей корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsUsed)
                    .as("Check that number of total used night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(4));
            System.out.println(CommonCode.ANSI_RED +"      Значение использованных ночей некорректное: "
                    + CommonCode.ANSI_RESET+ nightsUsed);
        }

        if (nightsTotal.equals(String.valueOf(5))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение общего количества ночей корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsTotal)
                    .as("Check that number of total number of night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(5));
            System.out.println(CommonCode.ANSI_RED +"      Значение общего количества ночей некорректное: "
                    + CommonCode.ANSI_RESET+ nightsTotal);
        }
        //Проверяем что к-во дней в Program верное
        //Получаем колличество дней
        System.out.println("[-] Проверяем, что количество дней в секции Program корректное:");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        numberOfDaysInProgram = Integer.valueOf($$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size());
        if (numberOfDaysInProgram == 5){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(5);
            System.out.println(CommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + CommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }
        System.out.println("[-] Проверяем, что количество сервисов в днях корректное :");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        dayCounterMax = 5;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            numberOfServices = 0;
            System.out.println("      - для дня номер " + dayCounter+":");
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + "//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++) {
                System.out.print("          - для города номер " + cityCounter+": ");
                numberOfServices = Integer.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)+"//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size());
                if(numberOfServices==3){
                    System.out.println(CommonCode.ANSI_GREEN+"3 сервиса как и должно быть"+ CommonCode.ANSI_RESET);
                }else{
                    System.out.println(CommonCode.ANSI_RED+numberOfServices+" сервиса, что является ошибкой"+ CommonCode.ANSI_RESET);
                    softAssertions.assertThat(numberOfServices)
                            .as("Check that number of services in day[%s] in city[%s] is correct.", String.valueOf(dayCounter), String.valueOf(cityCounter))
                            .isEqualTo(3);
                }
            }
        }

        //Добавляем перед SPB поезд
        System.out.print("[-] Добавляем перед SPB поезд");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(3)
                +NewQuotationPage.AccomodationsTable.insertBeforeOfCityREG)).scrollTo().click();
        $(By.xpath(newQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("TRAIN"))).shouldBe(visible);
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("TRAIN"))).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");
        //Проверяем что поезд встал на своё место
        System.out.println("[-] Проверяем что поезд встал на своё место:");
        if ($(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(3)
                +"//td[1]")).getText().equals("TRAIN")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, поезд на своём месте + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat($(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(3)
                    +"//td[1]")).getText())
                    .as("Check that train is on right place")
                    .isEqualTo("TRAIN");
            System.out.println(CommonCode.ANSI_RED +"      Поезд не на своём месте: "
                    + CommonCode.ANSI_RESET+ $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(3)
                    +"//td[1]")).getText());
        }


        //Меняем отели в MSK местами
        System.out.println("[-] Меняем первые MSK местами: ");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(2)
                +NewQuotationPage.AccomodationsTable.moveUpOfCityREG)).scrollTo().click();
        alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        if ($(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +"//td[@class=\"prices\"]//table//tbody//tr//select[@class=\"hotel\"]")).getSelectedValue().equals("127")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, дни поменялись местами + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat($(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                    +"//td[@class=\"prices\"]//table//tbody//tr//select[@class=\"hotel\"]")).getSelectedValue())
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo("127");
            System.out.println(CommonCode.ANSI_RED +"      Дни не поменялись местами: "
                    + CommonCode.ANSI_RESET+ $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                    +"//td[@class=\"prices\"]//table//tbody//tr//select[@class=\"hotel\"]")).getSelectedValue());
        }

        //Проверяем что к-во дней в Program верное
        System.out.println("[-] Проверяем, что количество дней в секции Program корректное:");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        numberOfDaysInProgram = Integer.valueOf($$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size());
        if (numberOfDaysInProgram == 6){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(6);
            System.out.println(CommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + CommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }
        System.out.println("[-] Проверяем, что количество сервисов в днях корректное :");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        dayCounterMax = 6;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            numberOfServices = 0;
            System.out.println("      - для дня номер " + dayCounter+":");
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + "//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++) {
                System.out.print("          - для города номер " + cityCounter+": ");
                numberOfServices = Integer.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)+"//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size());
                if(numberOfServices==3){
                    System.out.println(CommonCode.ANSI_GREEN+"3 сервиса как и должно быть"+ CommonCode.ANSI_RESET);
                }else{
                    System.out.println(CommonCode.ANSI_RED+numberOfServices+" сервиса, что является ошибкой"+ CommonCode.ANSI_RESET);
                    softAssertions.assertThat(numberOfServices)
                            .as("Check that number of services in day[%s] in city[%s] is correct.", String.valueOf(dayCounter), String.valueOf(cityCounter))
                            .isEqualTo(3);
                }
            }
        }
        //Удаляем первый в MSK отель
        System.out.println("[-] Удаляем первый MSK: ");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +NewQuotationPage.AccomodationsTable.deleteOfCityREG)).scrollTo().click();
        alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        if ($(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +"//td[@class=\"prices\"]//table//tbody//tr//select[@class=\"hotel\"]")).getValue().equals("131")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, день удалён + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat($(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                    +"//td[@class=\"prices\"]//table//tbody//tr//select[@class=\"hotel\"]")).getValue())
                    .as("Check that day is deleted")
                    .isEqualTo("131");
            System.out.println(CommonCode.ANSI_RED +"      День не удалён: "
                    + CommonCode.ANSI_RESET+ $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                    +"//td[@class=\"prices\"]//table//tbody//tr//select[@class=\"hotel\"]")).getValue());
        }

        //Проверяем что к-во дней в Program верное
        System.out.println("[-] Проверяем, что количество дней в секции Program корректное:");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        numberOfDaysInProgram = Integer.valueOf($$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size());
        if (numberOfDaysInProgram == 5){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(5);
            System.out.println(CommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + CommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }
        System.out.println("[-] Проверяем, что количество сервисов в днях корректное :");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        dayCounterMax = 5;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            numberOfServices = 0;
            System.out.println("      - для дня номер " + dayCounter+":");
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + "//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++) {
                System.out.print("          - для города номер " + cityCounter+": ");
                numberOfServices = Integer.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)+"//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size());
                if(numberOfServices==3){
                    System.out.println(CommonCode.ANSI_GREEN+"3 сервиса как и должно быть"+ CommonCode.ANSI_RESET);
                }else{
                    System.out.println(CommonCode.ANSI_RED+numberOfServices+" сервиса, что является ошибкой"+ CommonCode.ANSI_RESET);
                    softAssertions.assertThat(numberOfServices)
                            .as("Check that number of services in day[%s] in city[%s] is correct.", String.valueOf(dayCounter), String.valueOf(cityCounter))
                            .isEqualTo(3);
                }
            }
        }

        //Пробуем двинуть Питер вверх
        System.out.print("[-] Двигаем SPB вверх: ");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(3)
                +NewQuotationPage.AccomodationsTable.moveUpOfCityREG)).scrollTo().click();
        alert = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");
        System.out.println("[-] Проверяем что SPB встал на своё место:");
        if ($(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +"//td[1]")).getText().equals("SPB")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, SPB на своём месте + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat($(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                    +"//td[1]")).getText())
                    .as("Check that SPB is on right place")
                    .isEqualTo("TRAIN");
            System.out.println(CommonCode.ANSI_RED +"      SPB не на своём месте: "
                    + CommonCode.ANSI_RESET+ $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                    +"//td[1]")).getText());
        }
        //Проверяем что к-во дней в Program верное
        System.out.println("[-] Проверяем, что количество дней в секции Program корректное:");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        numberOfDaysInProgram = Integer.valueOf($$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size());
        if (numberOfDaysInProgram == 5){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(5);
            System.out.println(CommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + CommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }
        System.out.println("[-] Проверяем, что количество сервисов в днях корректное :");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        dayCounterMax = 5;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            numberOfServices = 0;
            System.out.println("      - для дня номер " + dayCounter+":");
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + "//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++) {
                System.out.print("          - для города номер " + cityCounter+": ");
                numberOfServices = Integer.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)+"//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size());
                if(numberOfServices==3){
                    System.out.println(CommonCode.ANSI_GREEN+"3 сервиса как и должно быть"+ CommonCode.ANSI_RESET);
                }else{
                    System.out.println(CommonCode.ANSI_RED+numberOfServices+" сервиса, что является ошибкой"+ CommonCode.ANSI_RESET);
                    softAssertions.assertThat(numberOfServices)
                            .as("Check that number of services in day[%s] in city[%s] is correct.", String.valueOf(dayCounter), String.valueOf(cityCounter))
                            .isEqualTo(3);
                }
            }
        }
    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
