package com.rfqDemoOltatravel;

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

public class TestOfNightsOption {

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
    public void testOfNightsOption(){

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
        System.out.println(" - Готово");


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("test");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(" - Готово");

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        commonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open(props.getProperty("baseURL")+"/application/olta.quotation");
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

        //Выставляем курс евро как "test" - получаем ошибку
        /*System.out.println("[-] Пробуем выставить курс евро как 'test'");
        String temp = $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText();
        String errorText = "";
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("test").pressEnter();
        errorText = commonCode.GetJSErrorText(driver);
        //System.out.println(errorText);
        commonCode.WaitForProgruzka();
        if (errorText.equals("none")){
            System.out.println(CommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "+CommonCode.ANSI_RESET);
            softAssertions.assertThat(errorText)
                    .as("Check that Rub-Euro rate can`t be 'test'")
                    .isEqualTo(String.valueOf("Invalid argument ('value'). Must be positive integer."));
            $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue(temp).pressEnter();
            CommonCode.WaitForProgruzkaSilent();
        } else {
            System.out.println(CommonCode.ANSI_GREEN+"      Валидация отработала, текст ошибки: "
                    + CommonCode.ANSI_RESET + errorText);
        }*/

        //Выставляем колество ночей как "test"
        System.out.println("[-] Пробуем выставить количество ночей как 'test'");
        String temp = $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).getText();
        $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).setValue("test").pressEnter();
        String errorText = commonCode.GetJSErrorText(driver);
        //System.out.println(errorText);
        CommonCode.WaitForProgruzkaSilent();

        if (errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Check that Night can`t be 'test'")
                    .isEqualTo(String.valueOf("Invalid argument ('value'). Must be positive integer."));
            System.out.println(CommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "
                    +CommonCode.ANSI_RESET);
            $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).setValue(temp);
            CommonCode.WaitForProgruzkaSilent();
        } else {
            System.out.println(CommonCode.ANSI_GREEN+"      Валидация отработала, текст ошибки: "
                    + CommonCode.ANSI_RESET + errorText);
        }

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 2;
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
        System.out.println(" - Готово");

        //Добавляем город - получаем ошибку
        System.out.println("[-] Добавляем город: SPB");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с SPB
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).shouldBe(visible);
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).click();
        errorText = commonCode.GetJSErrorText(driver);
        //System.out.println(errorText);

        if (errorText.equals("none")){
            System.out.println(CommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "+CommonCode.ANSI_RESET);
            CommonCode.WaitForProgruzkaSilent();
            softAssertions.assertThat(errorText)
                    .as("Check that city can`t be added if all night are filled")
                    .isEqualTo(String.valueOf("No more nights left."));
        } else {
            System.out.println(CommonCode.ANSI_GREEN +"      Валидация отработала, текст ошибки: "
                    + CommonCode.ANSI_RESET + errorText);
        }
        commonCode.WaitForProgruzka();


        //Выставляем колество ночей 1 - получаем ошибку
        System.out.println("[-] Выставляем количество ночей - 1");
        temp=$(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).getText();
        $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).click();
        CommonCode.WaitForProgruzkaSilent();
        $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).setValue("1").pressEnter();
        errorText = commonCode.GetJSErrorText(driver);
        //System.out.println(errorText);

        if (errorText.equals("none")){
            System.out.println(CommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "+CommonCode.ANSI_RESET);
            softAssertions.assertThat(errorText)
                    .as("Check that night number can`t be decreased if all already used")
                    .isEqualTo(String.valueOf("Accommodations total nights number exceeds quotation nights number. " +
                            "Please, descrease nights number or delete some accommodation records first."));
            $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).setValue(temp).pressEnter();
            CommonCode.WaitForProgruzkaSilent();
        } else {
            System.out.println(CommonCode.ANSI_GREEN +"      Валидация отработала, текст ошибки: "
                    + CommonCode.ANSI_RESET + errorText);
        }

        //Проверяем что даты в таблице Dates стоят правильные
        System.out.println("[-] Проверяем, что дата До выставлена корректно:");
        int currentNightNumber =
                Integer.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).scrollTo().getText());
        Instant tillDate = nowDate.plus(currentNightNumber, ChronoUnit.DAYS);
        String datesTillDate =
                $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.tillDateInput)).scrollTo().getText();
        //System.out.println(datesTillDate);
        if (datesTillDate.equals(formatForDate.format(tillDate))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, дата корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesTillDate)
                    .as("Check that To date is set correctly")
                    .isEqualTo(formatForDate.format(tillDate));
            System.out.println(CommonCode.ANSI_RED +"      Дата До некорректна: " + CommonCode.ANSI_RESET+ datesTillDate);
        }

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

        //Проверяем колличество дней в Program
        //Получаем колличество дней
        System.out.println("[-] Проверяем, что количество дней в секции Program корректное:");
        int numberOfDaysInProgram = Integer.valueOf($$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size());
        if (numberOfDaysInProgram == nightInOptionsCounter+1){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(nightInOptionsCounter+1);
            System.out.println(CommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + CommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }

        //Выставляем колество ночей 3
        nightInOptionsCounter = 3;
        System.out.println("[-] Меняем количество ночей на " + nightInOptionsCounter);
        NewQuotationPage.OptionsTable.SetNumberOfNights(nightInOptionsCounter);

        //Проверяем что дата До в таблице Dates стоит правильная
        System.out.println("[-] Проверяем, что после изменения Nights дата До выставлена корректно:");
        currentNightNumber =
                Integer.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).scrollTo().getText());
        tillDate = nowDate.plus(currentNightNumber, ChronoUnit.DAYS);
        datesTillDate = NewQuotationPage.DatesPeriodsTable.GetTillDateByPeriodCounter(1);
        //System.out.println(datesTillDate);
        if (datesTillDate.equals(formatForDate.format(tillDate))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, дата корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesTillDate)
                    .as("Check that To date is set correctly")
                    .isEqualTo(formatForDate.format(tillDate));
            System.out.println(CommonCode.ANSI_RED +"      Дата До некорректна: " + CommonCode.ANSI_RESET+ datesTillDate);
        }

        //Проверяем значения Nights в таблице Accommodations
        //Сохраняем Nights Total из таблицы Accommodations
        nightsTotalIndicator = $(By.cssSelector(NewQuotationPage.AccomodationsTable.nightsAvailableUsedIndicator)).scrollTo().getText();
        nightsUsed = nightsTotalIndicator.substring(0, (nightsTotalIndicator.indexOf('/')));
        nightsTotal = nightsTotalIndicator.substring(nightsTotalIndicator.indexOf('/')+1, nightsTotalIndicator.length());
        System.out.println("[-] Проверяем, что после изменений Night Total в таблице Accommodation посчитан корректно:");

        if (nightsUsed.equals(String.valueOf(nightInOptionsCounter-1))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение использованных ночей корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(nightsUsed)
                    .as("Check that number of total used night in Accommodation table is set correctly")
                    .isEqualTo(String.valueOf(nightInOptionsCounter-1));
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

        //Проверяем колличество дней в Program
        //Получаем колличество дней
        System.out.println("[-] Проверяем, что количество дней,после изменения Nights, в секции Program корректное:");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        numberOfDaysInProgram = Integer.valueOf($$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size());
        if (numberOfDaysInProgram == nightInOptionsCounter){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(nightInOptionsCounter);
            System.out.println(CommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + CommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }

        //Добавляем ещё один город
        System.out.print("[-] Добавляем город: SPB");
        NewQuotationPage.AddCityToAccomodationByName("VLG");
        System.out.println(" - Готово");

        //Проверяем значения Nights в таблице Accommodations
        //Сохраняем Nights Total из таблицы Accommodations
        nightsTotalIndicator = $(By.cssSelector(NewQuotationPage.AccomodationsTable.nightsAvailableUsedIndicator)).scrollTo().getText();
        nightsUsed = nightsTotalIndicator.substring(0, (nightsTotalIndicator.indexOf('/')));
        nightsTotal = nightsTotalIndicator.substring(nightsTotalIndicator.indexOf('/')+1, nightsTotalIndicator.length());
        System.out.println("[-] Проверяем, что после добавления города Night Total в таблице Accommodation посчитан корректно:");

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

        //Проверяем колличество дней в Program
        //Получаем колличество дней
        System.out.println("[-] Проверяем, что количество дней,после изменения Nights, в секции Program корректное:");
        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        numberOfDaysInProgram = Integer.valueOf($$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size());
        if (numberOfDaysInProgram == nightInOptionsCounter+1){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество дней в секции Program корректное + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfDaysInProgram)
                    .as("Check that number of days in Program section is correct")
                    .isEqualTo(nightInOptionsCounter+1);
            System.out.println(CommonCode.ANSI_RED +"      Значение количества дней в секции Program некорректное: "
                    + CommonCode.ANSI_RESET+ numberOfDaysInProgram);
        }


    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();

    }
}
