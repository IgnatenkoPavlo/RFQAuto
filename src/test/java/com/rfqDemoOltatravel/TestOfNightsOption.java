package com.rfqDemoOltatravel;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.assertj.core.api.SoftAssertions;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;


import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
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
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70");
        commonCode.WaitForProgruzka();
        Double rubEur = 0.0;
        rubEur = Double.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText());

        //Выставляем курс евро как "test" - получаем ошибку
        System.out.println("[-] Пробуем выставить курс евро как 'test'");
        String errorText = "";
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("test").pressEnter();
        errorText = commonCode.GetJSErrorText(driver);
        //System.out.println(errorText);
        commonCode.WaitForProgruzka();
        if (errorText.equals("none")){
            System.out.println(CommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "+CommonCode.ANSI_RESET);
            softAssertions.assertThat(errorText)
                    .as("Check that Rub-Euro rate can`t be 'test'").isEqualTo(String.valueOf("Invalid argument ('value'). Must be positive integer."));
            $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
            commonCode.WaitForProgruzka();
        } else {
            System.out.println(CommonCode.ANSI_GREEN+"      Валидация отработала, текст ошибки: " + CommonCode.ANSI_RESET + errorText);
        }

        //Выставляем колество ночей как "test"
        System.out.println("[-] Пробуем выставить количество ночей как 'test'");
        $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).setValue("test").pressEnter();
        errorText = commonCode.GetJSErrorText(driver);
        //System.out.println(errorText);
        commonCode.WaitForProgruzka();
        softAssertions.assertThat(errorText)
                .as("Check that Night can`t be 'test'").isEqualTo(String.valueOf("Invalid argument ('value'). Must be positive integer."));
        if (errorText.equals("none")){
            System.out.println(CommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "+CommonCode.ANSI_RESET);
            $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).setValue("7");
            commonCode.WaitForProgruzka();
        } else {
            System.out.println(CommonCode.ANSI_GREEN+"      Валидация отработала, текст ошибки: " + CommonCode.ANSI_RESET + errorText);
        }

        //Выставляем колество ночей - 2
        System.out.println("[-] Выставляем количество ночей - 2");
        $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).click();
        commonCode.WaitForProgruzka();
        $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).setValue("2").pressEnter();
        commonCode.WaitForProgruzka();

        //Выставляем дату
        Instant nowDate = Instant.now();
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy").withLocale(Locale.UK).withZone(ZoneOffset.UTC);
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
        System.out.println(" - Готово");

        //Добавляем город - получаем ошибку
        System.out.println("[-] Добавляем город: SPB");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).isDisplayed();
        //Кликаем по кнопке с SPB

        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).isDisplayed();
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).click();
        errorText = commonCode.GetJSErrorText(driver);
        //System.out.println(errorText);

        if (errorText.equals("none")){
            System.out.println(CommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "+CommonCode.ANSI_RESET);
            commonCode.WaitForProgruzka();
            softAssertions.assertThat(errorText)
                    .as("Check that city can`t be added if all night are filled").isEqualTo(String.valueOf("No more nights left."));
        } else {
            System.out.println(CommonCode.ANSI_GREEN +"      Валидация отработала, текст ошибки: " + CommonCode.ANSI_RESET + errorText);
        }
        commonCode.WaitForProgruzka();


        //Выставляем колество ночей 1 - получаем ошибку
        System.out.println("[-] Выставляем количество ночей - 1");
        $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).click();
        commonCode.WaitForProgruzka();
        $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).setValue("1").pressEnter();
        errorText = commonCode.GetJSErrorText(driver);
        //System.out.println(errorText);

        if (errorText.equals("none")){
            System.out.println(CommonCode.ANSI_RED+"      Ошибки нет, валидация не отработала - "+CommonCode.ANSI_RESET);
            softAssertions.assertThat(errorText)
                    .as("Check that night number can`t be decreased if all already used")
                    .isEqualTo(String.valueOf("Accommodations total nights number exceeds quotation nights number. Please, descrease nights number or delete some accommodation records first."));
            commonCode.WaitForProgruzka();
        } else {
            System.out.println(CommonCode.ANSI_GREEN +"      Валидация отработала, текст ошибки: " + CommonCode.ANSI_RESET + errorText);
        }
        commonCode.WaitForProgruzka();

        //Проверяем что даты в таблице Dates стоят правильные
        System.out.println("[-] Проверяем, что дата До выставлена корректно:");
        int currentNightNumber = Integer.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).scrollTo().getText());
        Instant tillDate = nowDate.plus(currentNightNumber, ChronoUnit.DAYS);
        String datesTillDate = $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.tillDateInputField)).scrollTo().getText();
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

        //Проверяем колличество дней в Program

        //Выставляем колество ночей 3

        //Проверяем что даты в таблице Dates стоят правильные

        //Проверяем значения Nights в таблице Accommodations

        //Проверяем колличество дней в Program

        //Добавляем ещё один город

        //Проверяем что даты в таблице Dates стоят правильные

        //Проверяем значения Nights в таблице Accommodations

        //Проверяем колличество дней в Program



    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();

    }
}
