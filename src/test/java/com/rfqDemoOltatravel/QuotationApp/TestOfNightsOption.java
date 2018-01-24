package com.rfqDemoOltatravel.QuotationApp;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import org.assertj.core.api.SoftAssertions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Properties;


import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.rfqDemoOltatravel.QuotationApp.NewQuotationPage.*;

public class TestOfNightsOption {

    public ChromeDriver driver;

    private SoftAssertions softAssertions;
    QuotationAppCommonCode quotationAppCommonCode = new QuotationAppCommonCode();
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
    public void testOfNightsOption(){

        isWindows=false;
        if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){isWindows=true;}
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
        quotationAppCommonCode.WaitForPageToLoad(driver);
        System.out.println(QuotationAppCommonCode.OK);


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue(QuotationAppCommonCode.QUOTATIONAPPLOGIN);
        $(By.id("password")).setValue(QuotationAppCommonCode.QUOTATIONAPPPASSWORD);
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(QuotationAppCommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        quotationAppCommonCode.WaitForPageToLoad(driver);
        QuotationAppCommonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open(props.getProperty("baseURL")+"/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        quotationAppCommonCode.WaitForPageToLoad(driver);
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(QuotationAppCommonCode.OK);

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(QuotationAppCommonCode.OK);

        //Создаём новый Quotation
        CreateQuotation("PTestQuotation1", "Тест компания");
        //NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем курс Евро - 70.0
        Double rubEur = 70.0;
        OptionsTable.SetCurrencyRateForEUR(rubEur);

        //Выставляем курс евро как "test" - получаем ошибку
        /*System.out.println("[-] Пробуем выставить курс евро как 'test'");
        String temp = $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText();
        String errorText = "";
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("test").pressEnter();
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        //System.out.println(errorText);
        QuotationAppCommonCode.WaitForProgruzka();
        if (errorText.equals("none")){
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "+QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(errorText)
                    .as("Check that Rub-Euro rate can`t be 'test'")
                    .isEqualTo(String.valueOf("Invalid argument ('value'). Must be positive integer."));
            $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue(temp).pressEnter();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Валидация отработала, текст ошибки: "
                    + QuotationAppCommonCode.ANSI_RESET + errorText);
        }*/

        //Выставляем колество ночей как "test"
        System.out.println("[-] Пробуем выставить количество ночей как 'test'");
        String temp = $(By.cssSelector(OptionsTable.numberOfNights)).getText();
        $(By.cssSelector(OptionsTable.numberOfNights)).setValue("test").pressEnter();
        String errorText = quotationAppCommonCode.GetJSErrorText(driver);
        //System.out.println(errorText);
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        if (errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Check that Night can`t be 'test'")
                    .isEqualTo(String.valueOf("Invalid argument ('value'). Must be positive integer."));
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "
                    + QuotationAppCommonCode.ANSI_RESET);
            $(By.cssSelector(OptionsTable.numberOfNights)).setValue(temp);
            QuotationAppCommonCode.WaitForProgruzkaSilent();
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Валидация отработала, текст ошибки: "
                    + QuotationAppCommonCode.ANSI_RESET + errorText);
        }

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 2;
        OptionsTable.SetNumberOfNightsInOptions(nightInOptionsCounter);

        //Выставляем дату
        Instant nowDate = Instant.now();
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        System.out.print("[-] Добавляем новую дату: " + formatForDate.format(nowDate));
        //Кликаем на кнопку Add
        $(By.cssSelector(DatesPeriodsTable.addDateButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).click();
        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));

        //Вводим дату в поле
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).setValue(formatForDate.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(DatesPeriodsTable.saveDateButton)).click();
        System.out.println(QuotationAppCommonCode.OK);

        //Добавляем город
        AddCityToAccomodationByName("MSK", 1);

        //Добавляем город - получаем ошибку
        System.out.println("[-] Добавляем город: SPB");
        //Кликаем Add
        $(By.cssSelector(AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с SPB
        $(By.xpath(GetCityNameButtonREG("SPB"))).shouldBe(visible);
        $(By.xpath(GetCityNameButtonREG("SPB"))).click();
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        //System.out.println(errorText);

        if (errorText.equals("none")){
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "+ QuotationAppCommonCode.ANSI_RESET);
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            softAssertions.assertThat(errorText)
                    .as("Check that city can`t be added if all night are filled")
                    .isEqualTo(String.valueOf("No more nights left."));
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN +"      Валидация отработала, текст ошибки: "
                    + QuotationAppCommonCode.ANSI_RESET + errorText);
        }
        QuotationAppCommonCode.WaitForProgruzka();


        //Выставляем колество ночей 1 - получаем ошибку
        System.out.println("[-] Выставляем количество ночей - 1");
        temp=$(By.cssSelector(OptionsTable.numberOfNights)).getText();
        $(By.cssSelector(OptionsTable.numberOfNights)).click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        $(By.cssSelector(OptionsTable.numberOfNights)).setValue("1").pressEnter();
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        //System.out.println(errorText);

        if (errorText.equals("none")){
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(errorText)
                    .as("Check that night number can`t be decreased if all already used")
                    .isEqualTo(String.valueOf("Accommodations total nights number exceeds quotation nights number. " +
                            "Please, descrease nights number or delete some accommodation records first."));
            $(By.cssSelector(OptionsTable.numberOfNights)).setValue(temp).pressEnter();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN +"      Валидация отработала, текст ошибки: "
                    + QuotationAppCommonCode.ANSI_RESET + errorText);
        }

        //Проверяем что даты в таблице Dates стоят правильные
        System.out.println("[-] Проверяем, что дата До выставлена корректно:");
        int currentNightNumber =
                Integer.valueOf($(By.cssSelector(OptionsTable.numberOfNights)).scrollTo().getText());
        Instant tillDate = nowDate.plus(currentNightNumber, ChronoUnit.DAYS);
        String datesTillDate =
                $(By.cssSelector(DatesPeriodsTable.tillDateInput)).scrollTo().getText();
        //System.out.println(datesTillDate);
        if (datesTillDate.equals(formatForDate.format(tillDate))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, дата корректная + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesTillDate)
                    .as("Check that To date is set correctly")
                    .isEqualTo(formatForDate.format(tillDate));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата До некорректна: " + QuotationAppCommonCode.ANSI_RESET+ datesTillDate);
        }

        //Проверяем значения Nights в таблице Accommodations
        //Сохраняем Nights Total из таблицы Accommodations
        String nightsTotalIndicator =
                $(By.cssSelector(AccomodationsTable.nightsAvailableUsedIndicator)).scrollTo().getText();
        String nightsUsed = nightsTotalIndicator.substring(0, (nightsTotalIndicator.indexOf('/')));
        String nightsTotal =
                nightsTotalIndicator.substring(nightsTotalIndicator.indexOf('/')+1, nightsTotalIndicator.length());
        System.out.println("[-] Проверяем, что Night Total в таблице Accommodation посчитан корректно:");

        if (nightsUsed.equals(String.valueOf(nightInOptionsCounter))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение использованных ночей корректное + "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsUsed)
                    .as("Check that number of total used night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(nightInOptionsCounter));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение использованных ночей некорректное: "
                    + QuotationAppCommonCode.ANSI_RESET+ nightsUsed);
        }

        if (nightsTotal.equals(String.valueOf(nightInOptionsCounter))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение общего количества ночей корректное + "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsTotal)
                    .as("Check that number of total number of night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(nightInOptionsCounter));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение общего количества ночей некорректное: "
                    + QuotationAppCommonCode.ANSI_RESET+ nightsTotal);
        }

        //Проверяем колличество дней в Program
        //Получаем колличество дней
        System.out.println("[-] Проверяем, что количество дней в секции Program корректное:");
        int numberOfDaysInProgram = $$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size();
        if (numberOfDaysInProgram == nightInOptionsCounter+1){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(nightInOptionsCounter+1);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + QuotationAppCommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }

        //Выставляем колество ночей 3
        nightInOptionsCounter = 3;
        OptionsTable.SetNumberOfNightsInOptions(nightInOptionsCounter);

        //Проверяем что дата До в таблице Dates стоит правильная
        System.out.println("[-] Проверяем, что после изменения Nights дата До выставлена корректно:");
        currentNightNumber =
                Integer.valueOf($(By.cssSelector(OptionsTable.numberOfNights)).scrollTo().getText());
        tillDate = nowDate.plus(currentNightNumber, ChronoUnit.DAYS);
        datesTillDate = DatesPeriodsTable.GetTillDateByPeriodCounter(1);
        //System.out.println(datesTillDate);
        if (datesTillDate.equals(formatForDate.format(tillDate))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, дата корректная + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesTillDate)
                    .as("Check that To date is set correctly")
                    .isEqualTo(formatForDate.format(tillDate));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата До некорректна: " + QuotationAppCommonCode.ANSI_RESET+ datesTillDate);
        }

        //Проверяем значения Nights в таблице Accommodations
        //Сохраняем Nights Total из таблицы Accommodations
        nightsTotalIndicator = $(By.cssSelector(AccomodationsTable.nightsAvailableUsedIndicator)).scrollTo().getText();
        nightsUsed = nightsTotalIndicator.substring(0, (nightsTotalIndicator.indexOf('/')));
        nightsTotal = nightsTotalIndicator.substring(nightsTotalIndicator.indexOf('/')+1, nightsTotalIndicator.length());
        System.out.println("[-] Проверяем, что после изменений Night Total в таблице Accommodation посчитан корректно:");

        if (nightsUsed.equals(String.valueOf(nightInOptionsCounter-1))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение использованных ночей корректное + "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsUsed)
                    .as("Check that number of total used night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(nightInOptionsCounter-1));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение использованных ночей некорректное: "
                    + QuotationAppCommonCode.ANSI_RESET+ nightsUsed);
        }

        if (nightsTotal.equals(String.valueOf(nightInOptionsCounter))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение общего количества ночей корректное + "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsTotal)
                    .as("Check that number of total number of night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(nightInOptionsCounter));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение общего количества ночей некорректное: "
                    + QuotationAppCommonCode.ANSI_RESET+ nightsTotal);
        }

        //Проверяем колличество дней в Program
        //Получаем колличество дней
        System.out.println("[-] Проверяем, что количество дней,после изменения Nights, в секции Program корректное:");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        numberOfDaysInProgram = $$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size();
        if (numberOfDaysInProgram == nightInOptionsCounter){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(nightInOptionsCounter);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + QuotationAppCommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }

        //Добавляем ещё один город
        AddCityToAccomodationByName("VLG", 2);

        //Проверяем значения Nights в таблице Accommodations
        //Сохраняем Nights Total из таблицы Accommodations
        nightsTotalIndicator = $(By.cssSelector(AccomodationsTable.nightsAvailableUsedIndicator)).scrollTo().getText();
        nightsUsed = nightsTotalIndicator.substring(0, (nightsTotalIndicator.indexOf('/')));
        nightsTotal = nightsTotalIndicator.substring(nightsTotalIndicator.indexOf('/')+1, nightsTotalIndicator.length());
        System.out.println("[-] Проверяем, что после добавления города Night Total в таблице Accommodation посчитан корректно:");

        if (nightsUsed.equals(String.valueOf(nightInOptionsCounter))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение использованных ночей корректное + "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsUsed)
                    .as("Check that number of total used night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(nightInOptionsCounter));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение использованных ночей некорректное: "
                    + QuotationAppCommonCode.ANSI_RESET+ nightsUsed);
        }

        if (nightsTotal.equals(String.valueOf(nightInOptionsCounter))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, значение общего количества ночей корректное + "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsTotal)
                    .as("Check that number of total number of night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(nightInOptionsCounter));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение общего количества ночей некорректное: "
                    + QuotationAppCommonCode.ANSI_RESET+ nightsTotal);
        }

        //Проверяем колличество дней в Program
        //Получаем колличество дней
        System.out.println("[-] Проверяем, что количество дней,после изменения Nights, в секции Program корректное:");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        numberOfDaysInProgram = $$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size();
        if (numberOfDaysInProgram == nightInOptionsCounter+1){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(nightInOptionsCounter+1);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + QuotationAppCommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }


    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();

    }
}
