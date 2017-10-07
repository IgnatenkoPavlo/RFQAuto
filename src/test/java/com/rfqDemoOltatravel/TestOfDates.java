package com.rfqDemoOltatravel;

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
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestOfDates {

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
    public void testOfDates() {
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
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
        commonCode.WaitForProgruzkaSilent();
        Double rubEur = 0.0;
        rubEur = Double.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText());

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 2;
        System.out.println("[-] Меняем количество ночей на " + nightInOptionsCounter);
        NewQuotationPage.OptionsTable.SetNumberOfNights(nightInOptionsCounter);

        //Выставляем дату
        //Получаем первую пару дат
        Instant nowDate = Instant.now();
        Instant tillDate1 = nowDate.plus(nightInOptionsCounter, ChronoUnit.DAYS);

        //Получаем вторую пару дат
        Instant fromDate2 = nowDate.plus(nightInOptionsCounter+2, ChronoUnit.DAYS);
        Instant tillDate2 = nowDate.plus(nightInOptionsCounter+4, ChronoUnit.DAYS);

        //Получаем третью пару дат
        Instant fromDate3 = nowDate.plus(nightInOptionsCounter+6, ChronoUnit.DAYS);
        Instant tillDate3 = nowDate.plus(nightInOptionsCounter+8, ChronoUnit.DAYS);

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

        DateTimeFormatter formatForAccommodations1 = DateTimeFormatter.ofPattern("d")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        DateTimeFormatter formatForAccommodations2 = DateTimeFormatter.ofPattern("d MMMM yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

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

        //Проверяем что даты в Accommodations верные
        System.out.println("[-] Проверяем что даты в Accommodations верные:");
        String accommodationDate1 =
                $(By.xpath(NewQuotationPage.AccomodationsTable.GetAccommodationDateByNumberREG(2)))
                        .scrollTo().getText();
        //System.out.println(datesTillDate);
        if (accommodationDate1.equals(formatForAccommodations1.format(nowDate)+" - "
                +formatForAccommodations2.format(tillDate1))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, даты корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(accommodationDate1)
                    .as("Check that dates in Accommodation are set correctly")
                    .isEqualTo(String.valueOf(formatForAccommodations1.format(nowDate)+" "
                            +formatForAccommodations2.format(tillDate1)));
            System.out.println(CommonCode.ANSI_RED +"      Дата До некорректна: " + CommonCode.ANSI_RESET
                    + String.valueOf(formatForAccommodations1.format(nowDate)+" - "
                    +formatForAccommodations2.format(tillDate1)));
        }

        //Проверяем, что в Results даты тоже корректные
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что даты в Results верные:");
        String resultsDates1 = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr[1]//th")).scrollTo().getText();
        if (resultsDates1.equals(formatForAccommodations1.format(nowDate)+" - "
                +formatForAccommodations2.format(tillDate1))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, даты корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(resultsDates1)
                    .as("Check that dates in Results are set correctly")
                    .isEqualTo(String.valueOf(formatForAccommodations1.format(nowDate)+" "
                            +formatForAccommodations2.format(tillDate1)));
            System.out.println(CommonCode.ANSI_RED +"      Даты в Results некорректна: " + CommonCode.ANSI_RESET
                    + String.valueOf(formatForAccommodations1.format(nowDate)+" - "
                    +formatForAccommodations2.format(tillDate1)));
        }

        //Добавляем второй промежуток дат, берём третьи даты
        //Проверяем Dates
        //Проверяем Accommodations
        //Проверяем Results

        //Добавляем второй промежуток дат, берём вторые даты
        //Проверяем Dates
        //Проверяем Accommodations
        //Проверяем Results

    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
