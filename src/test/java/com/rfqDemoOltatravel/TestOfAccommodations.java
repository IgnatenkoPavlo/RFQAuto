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
        $(By.id("username")).setValue("pavel.sales");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(" - Готово");

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        commonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open("http://rfq-demo.oltatravel.com/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(" - Готово");

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).isDisplayed();
        System.out.println(" - Готово");

        //Создаём новый Quotation
        System.out.println("[-] Создаём новый Quotation:");
        $(By.id("qbtn-create")).click();
        $(By.xpath(QuotationListPage.newQuotationPopapREG)).isDisplayed();
        $(By.xpath(QuotationListPage.newQuotationNameREG)).setValue("PTestQuotation1");
        System.out.println("      Имя - PTestQuotation1");
        $(By.xpath(QuotationListPage.newQuotationClientNameREG)).selectOptionContainingText("Тест компания");
        System.out.println("      Клиент - Тест компания");
        $(By.xpath(QuotationListPage.newQuotationPopapOkButtonREG)).click();

        NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Ждём пока страница прогрузится
        commonCode.WaitForProgruzka();

        //Выставляем курс Евро
        System.out.println("[-] Выставляем курс евро 70");
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
        commonCode.WaitForProgruzkaSilent();
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
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.addButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).click();
        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));

        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).setValue(formatForDate.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.saveButton)).click();
        System.out.println(" - Готово");

        //Добавляем город
        System.out.print("[-] Добавляем город: MSK");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).isDisplayed();
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).isDisplayed();
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).click();
        commonCode.WaitForProgruzkaSilent();
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
        System.out.print("[-]Выставляем значение ночей для размещения в 1");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +NewQuotationPage.AccomodationsTable.nightsCounterForCityREG)).scrollTo().setValue("1").pressEnter();
        Alert alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        commonCode.WaitForProgruzkaSilent();
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

        //Проверяем что к-во дней в Program верное
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

        //Выставляем имя отеля1
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +NewQuotationPage.AccomodationsTable.togglePricesOfCityREG)).scrollTo().click();
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                +"//td[@class=\"prices\"]//table//tbody//tr//select[@class=\"hotel\"]")).scrollTo().selectOptionContainingText("Alfa");
        commonCode.WaitForProgruzkaSilent();

        //Добавляем MSK
        System.out.print("[-] Добавляем город: MSK");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).isDisplayed();
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).isDisplayed();
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).click();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Выставляем для него значение ночей в 1
        System.out.print("[-]Выставляем значение ночей для размещения два в 1");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(2)
                +NewQuotationPage.AccomodationsTable.nightsCounterForCityREG)).scrollTo().setValue("1").pressEnter();
        alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        commonCode.WaitForProgruzkaSilent();
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
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(2)
                +NewQuotationPage.AccomodationsTable.togglePricesOfCityREG)).scrollTo().click();
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(2)
                +"//td[@class=\"prices\"]//table//tbody//tr//select[@class=\"hotel\"]")).scrollTo().selectOptionContainingText("Mandarin");
        commonCode.WaitForProgruzkaSilent();

        //Проверяем что к-во дней в Program верное
        //Получаем колличество дней
        System.out.println("[-] Проверяем, что количество дней,после изменения Nights, в секции Program корректное:");
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

        //Добавляем Питер
        //Добавляем MSK
        System.out.print("[-] Добавляем город: SPB");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).isDisplayed();
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).isDisplayed();
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).click();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Выставляем для него значение ночей в 1
        System.out.print("[-]Выставляем значение ночей для размещения два в 1");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(3)
                +NewQuotationPage.AccomodationsTable.nightsCounterForCityREG)).scrollTo().setValue("2").pressEnter();
        alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        commonCode.WaitForProgruzkaSilent();
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
        System.out.println("[-] Проверяем, что количество дней,после изменения Nights, в секции Program корректное:");
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

        //Выставляем для него значение ночей в 2
        //Проверяем что к-во дней в Program верное
        //Добавляем перед SPB поезд
        //Проверяем что к-во дней в Program верное
        //Меняем отели в MSK местами
        //Проверяем что к-во дней в Program верное
        //Удаляем первый в MSK отель
        //Проверяем что к-во дней в Program верное
        //Пробуем двинуть Питер вверх
        //Проверяем что к-во дней в Program верное

    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
