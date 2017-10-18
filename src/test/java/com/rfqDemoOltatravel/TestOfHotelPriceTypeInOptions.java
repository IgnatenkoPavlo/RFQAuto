package com.rfqDemoOltatravel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestOfHotelPriceTypeInOptions {

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
    public void testOfHotelPriceTypeInOptions() {

        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;
        System.out.print("[-] Открываем URL: http://rfq-demo.oltatravel.com/");
        open("http://rfq-demo.oltatravel.com/");
        commonCode.WaitForPageToLoad(driver);
        System.out.println(" - готово");


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("pavel.sales");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(" - готово");

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        commonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open("http://rfq-demo.oltatravel.com/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(" - готово");

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).isDisplayed();
        System.out.println(" - готово");

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
        System.out.print("[-] Выставляем курс евро 70");
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");
        Double rubEur = 0.0;
        rubEur = Double.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText());

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 1;
        System.out.print("[-] Меняем количество ночей на " + nightInOptionsCounter+ ": ");
        NewQuotationPage.OptionsTable.SetNumberOfNights(nightInOptionsCounter);
        System.out.println(" - готово");

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(NewQuotationPage.OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(" - готово");

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
        System.out.println(" - готово");

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
        System.out.println(" - готово");

        //Проганяем стили css для контрола Hotel Price Type
        $(By.cssSelector(NewQuotationPage.OptionsTable.hotelPriceType)).scrollTo().selectOptionContainingText("Individual");
        commonCode.WaitForProgruzkaSilent();
        $(By.cssSelector(NewQuotationPage.OptionsTable.hotelPriceType)).scrollTo().selectOptionContainingText("Group");
        commonCode.WaitForProgruzkaSilent();

        //Проверяем, что Hotel Price Type стоит DBL
        System.out.println("[-] Проверяем, что Hotel Price Type стоит DBL:");
        String result = $(By.cssSelector(NewQuotationPage.OptionsTable.hotelPriceType)).scrollTo().getSelectedText();
        if (result.equals("Group")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Hotel Price Type in Options set as Group")
                    .isEqualTo(String.valueOf("Group"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что заголовок таблицы Accommodations корректный
        System.out.println("[-] Проверяем, что заголовок таблицы Accommodations корректный:");
        result = $(By.xpath(NewQuotationPage.accomodationsTableREG + "//thead//th[@class=\"visibility-group\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: table-cell;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo("display: table-cell;");
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что заголовок таблицы Accommodations для Individual скрыт
        System.out.println("[-] Проверяем, что заголовок таблицы Accommodations для Individual скрыт:");
        result = $(By.xpath(NewQuotationPage.accomodationsTableREG + "//thead//th[@class=\"visibility-individual\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: none;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo(String.valueOf("display: none;"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что дропдаун Hotel Type активен для Group
        System.out.println("[-] Проверяем, что дропдаун Hotel Type для Group:");
        result = $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotelType visibility-group\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: table-cell;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo("display: table-cell;");
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что дропдаун Hotel Type активен для Individual
        System.out.println("[-] Проверяем, что дропдаун Hotel Type для Individual скрыт:");
        result = $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotel visibility-individual\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: none;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo(String.valueOf("display: none;"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проставляем Hotel Price Type в Individual
        System.out.print("[-] Проставляем Hotel Price Type в Individual");
        $(By.cssSelector(NewQuotationPage.OptionsTable.hotelPriceType)).scrollTo().selectOptionContainingText("Individual");
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Проверяем, что заголовок таблицы Accommodations корректный
        System.out.println("[-] Проверяем, что заголовок таблицы Accommodations корректный:");
        result = $(By.xpath(NewQuotationPage.accomodationsTableREG + "//thead//th[@class=\"visibility-group\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: none;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo("display: none;");
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что заголовок таблицы Accommodations для Individual скрыт
        System.out.println("[-] Проверяем, что заголовок таблицы Accommodations для Individual скрыт:");
        result = $(By.xpath(NewQuotationPage.accomodationsTableREG + "//thead//th[@class=\"visibility-individual\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: table-cell;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo(String.valueOf("display: table-cell;"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что дропдаун Hotel Type активен для Group
        System.out.println("[-] Проверяем, что дропдаун Hotel Type для Group:");
        result = $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotelType visibility-group\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: none;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo("display: none;");
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что дропдаун Hotel Type активен для Individual
        System.out.println("[-] Проверяем, что дропдаун Hotel Type для Individual скрыт:");
        result = $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotel visibility-individual\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: table-cell;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo(String.valueOf("display: table-cell;"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();

    }
}
