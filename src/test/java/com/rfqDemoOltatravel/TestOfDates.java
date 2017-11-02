package com.rfqDemoOltatravel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Properties;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
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

        Properties props=new Properties();
        try {
            props.load(new InputStreamReader(new FileInputStream("target\\test-classes\\application.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Получил проперти ");
        System.out.println(props.getProperty("baseURL"));

        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;
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
        NewQuotationPage.CreateQuotation(driver,"PTestQuotation1", "Тест компания");
        NewQuotationPage newQuotationPage = new NewQuotationPage();


        //Выставляем курс Евро
        System.out.println("[-] Выставляем курс евро 70");
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
        CommonCode.WaitForProgruzkaSilent();
        Double rubEur = 0.0;
        rubEur = Double.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText());

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 2;
        System.out.println("[-] Меняем количество ночей на " + nightInOptionsCounter);
        NewQuotationPage.OptionsTable.SetNumberOfNightsInOptions(nightInOptionsCounter);

        //Выставляем дату
        //Получаем первую пару дат
        LocalDate nowDate = LocalDate.now();
        LocalDate tillDate1 = nowDate.plus(nightInOptionsCounter, ChronoUnit.DAYS);

        //Получаем вторую пару дат
        LocalDate fromDate2 = nowDate.plus(nightInOptionsCounter+2, ChronoUnit.DAYS);
        LocalDate tillDate2 = nowDate.plus(nightInOptionsCounter+4, ChronoUnit.DAYS);

        //Получаем третью пару дат
        LocalDate fromDate3 = nowDate.plus(nightInOptionsCounter+6, ChronoUnit.DAYS);
        LocalDate tillDate3 = nowDate.plus(nightInOptionsCounter+8, ChronoUnit.DAYS);

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
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        DateTimeFormatter formatForAccommodations1 = DateTimeFormatter.ofPattern("d")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        DateTimeFormatter formatForAccommodations2 = DateTimeFormatter.ofPattern("d MMMM yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        DateTimeFormatter formatForResultsDayOnly = DateTimeFormatter.ofPattern("d")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatForResultsDayMonthOnly = DateTimeFormatter.ofPattern("d MMMM")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatForResultsFull = DateTimeFormatter.ofPattern("d MMMM yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

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

        String composedPeriodValue;
        if(nowDate.getMonth() == tillDate1.getMonth())
            composedPeriodValue = nowDate.format(formatForResultsDayOnly)+" - "+tillDate1.format(formatForResultsFull);
        else
            composedPeriodValue = nowDate.format(formatForResultsDayMonthOnly)+" - "+tillDate1.format(formatForResultsFull);

        if(nowDate.getYear() != tillDate1.getYear())
            composedPeriodValue = nowDate.format(formatForResultsFull)+" - "+tillDate1.format(formatForResultsFull);

        //Проверяем что даты в Accommodations верные
        System.out.println("[-] Проверяем что даты в Accommodations верные:");
        String accommodationDate1 =
                $(By.xpath(NewQuotationPage.AccomodationsTable.AccommodationDateByNumberREG(2)))
                        .scrollTo().getText();

        //System.out.println(datesTillDate);
        if (accommodationDate1.equals(composedPeriodValue)){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, даты корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(accommodationDate1)
                    .as("Check that dates in Accommodation are set correctly")
                    .isEqualTo(composedPeriodValue);
            System.out.println(CommonCode.ANSI_RED +"      Дата До некорректна: " + CommonCode.ANSI_RESET
                    + accommodationDate1);
        }

        //Проверяем, что в Results даты тоже корректные
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что даты в Results верные:");
        String resultsDates1 = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr[1]//th")).scrollTo().getText();

        if(nowDate.getMonth() == tillDate1.getMonth())
            composedPeriodValue = nowDate.format(formatForResultsDayOnly)+" - "+tillDate1.format(formatForResultsFull);
        else
            composedPeriodValue = nowDate.format(formatForResultsDayMonthOnly)+" - "+tillDate1.format(formatForResultsFull);

        if(nowDate.getYear() != tillDate1.getYear())
            composedPeriodValue = nowDate.format(formatForResultsFull)+" - "+tillDate1.format(formatForResultsFull);

        if (resultsDates1.equals(composedPeriodValue)){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(resultsDates1)
                    .as("Check that dates in Results are set correctly")
                    .isEqualTo(composedPeriodValue);
            System.out.println(CommonCode.ANSI_RED +"      Даты в Results некорректные: " + CommonCode.ANSI_RESET
                    + String.valueOf(composedPeriodValue));
        }

        //Добавляем второй промежуток дат, берём третьи даты
        System.out.print("[-] Добавляем новую дату: " + formatForDate.format(fromDate3));
        //Кликаем на кнопку Add
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.addDateButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).click();
        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).setValue(formatForDate.format(fromDate3));
        //Кликаем кнопку сохранить
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.saveDateButton)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Проверяем Dates
        System.out.println("[-] Проверяем, что промежуток дат в таблице Date на своём месте:");
        //Проверяем От в Dates
        String datesFromDate = NewQuotationPage.DatesPeriodsTable.GetFromDateByPeriodCounter(2);
        if (datesFromDate.equals(String.valueOf(formatForDate.format(fromDate3)))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, дата От корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesFromDate)
                    .as("Check that From date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(fromDate3)));
            System.out.println(CommonCode.ANSI_RED +"      Дата От некорректна: " + CommonCode.ANSI_RESET+ datesFromDate);
        }
        //Проверяем До в Dates
        String datesTillDate = NewQuotationPage.DatesPeriodsTable.GetTillDateByPeriodCounter(2);
        if (datesTillDate.equals(String.valueOf(formatForDate.format(tillDate3)))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, дата До корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesTillDate)
                    .as("Check that Till date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(tillDate3)));
            System.out.println(CommonCode.ANSI_RED +"      Дата До некорректна: " + CommonCode.ANSI_RESET+ datesTillDate);
        }

        //Проверяем Accommodations
        System.out.println("[-] Проверяем что новые даты в Accommodations на своём месте:");
        accommodationDate1 =
                $(By.xpath(NewQuotationPage.AccomodationsTable.AccommodationDateByNumberREG(3)))
                        .scrollTo().getText();
        String composedPeriodValue3;
        if(fromDate3.getMonth() == tillDate3.getMonth())
            composedPeriodValue3 = fromDate3.format(formatForResultsDayOnly)+" - "+tillDate3.format(formatForResultsFull);
        else
            composedPeriodValue3 = fromDate3.format(formatForResultsDayMonthOnly)+" - "+tillDate3.format(formatForResultsFull);

        if(fromDate3.getYear() != tillDate3.getYear())
            composedPeriodValue3 = fromDate3.format(formatForResultsFull)+" - "+tillDate3.format(formatForResultsFull);

        //System.out.println(datesTillDate);
        if (accommodationDate1.equals(composedPeriodValue3)){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(accommodationDate1)
                    .as("Check that dates in Accommodation are set correctly")
                    .isEqualTo(composedPeriodValue3);
            System.out.println(CommonCode.ANSI_RED +"      Дата некорректные: " + CommonCode.ANSI_RESET
                    + accommodationDate1);
        }
        //Проверяем, что в Results даты тоже корректные
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что даты в Results верные:");
        resultsDates1 = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr[2]//th")).scrollTo().getText();
        if (resultsDates1.equals(composedPeriodValue3)){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(resultsDates1)
                    .as("Check that dates in Results are set correctly")
                    .isEqualTo(composedPeriodValue3);
            System.out.println(CommonCode.ANSI_RED +"      Даты в Results некорректные: " + CommonCode.ANSI_RESET
                    + resultsDates1);
        }

        //Добавляем второй промежуток дат, берём вторые даты
        System.out.print("[-] Добавляем новую дату: " + formatForDate.format(fromDate2));
        //Кликаем на кнопку Add
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.addDateButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).click();
        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).setValue(formatForDate.format(fromDate2));
        //Кликаем кнопку сохранить
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.saveDateButton)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Проверяем Dates
        System.out.println("[-] Проверяем, что промежуток дат в таблице Date на своём месте:");
        //Проверяем От в Dates
        datesFromDate = NewQuotationPage.DatesPeriodsTable.GetFromDateByPeriodCounter(2);
        if (datesFromDate.equals(String.valueOf(formatForDate.format(fromDate2)))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, дата От корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesFromDate)
                    .as("Check that From date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(fromDate2)));
            System.out.println(CommonCode.ANSI_RED +"      Дата От некорректна: " + CommonCode.ANSI_RESET+ datesFromDate);
        }
        //Проверяем До в Dates
        datesTillDate = NewQuotationPage.DatesPeriodsTable.GetTillDateByPeriodCounter(2);
        if (datesTillDate.equals(String.valueOf(formatForDate.format(tillDate2)))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, дата До корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesTillDate)
                    .as("Check that Till date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(tillDate2)));
            System.out.println(CommonCode.ANSI_RED +"      Дата До некорректна: " + CommonCode.ANSI_RESET+ datesTillDate);
        }

        //Проверяем Accommodations
        System.out.println("[-] Проверяем что новые даты в Accommodations на своём месте:");
        accommodationDate1 =
                $(By.xpath(NewQuotationPage.AccomodationsTable.AccommodationDateByNumberREG(3)))
                        .scrollTo().getText();
        if(fromDate2.getMonth() == tillDate2.getMonth())
            composedPeriodValue = fromDate2.format(formatForResultsDayOnly)+" - "+tillDate2.format(formatForResultsFull);
        else
            composedPeriodValue = fromDate2.format(formatForResultsDayMonthOnly)+" - "+tillDate2.format(formatForResultsFull);

        if(fromDate2.getYear() != tillDate2.getYear())
            composedPeriodValue = fromDate2.format(formatForResultsFull)+" - "+tillDate2.format(formatForResultsFull);
        //System.out.println(datesTillDate);
        if (accommodationDate1.equals(composedPeriodValue)){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(accommodationDate1)
                    .as("Check that dates in Accommodation are set correctly")
                    .isEqualTo(composedPeriodValue);
            System.out.println(CommonCode.ANSI_RED +"      Дата некорректные: " + CommonCode.ANSI_RESET
                    + accommodationDate1);
        }
        //Проверяем Results
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что даты в Results верные:");
        resultsDates1 = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr[2]//th")).scrollTo().getText();
        if (resultsDates1.equals(composedPeriodValue)){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(resultsDates1)
                    .as("Check that dates in Results are set correctly")
                    .isEqualTo(composedPeriodValue);
            System.out.println(CommonCode.ANSI_RED +"      Даты в Results некорректные: " + CommonCode.ANSI_RESET
                    + resultsDates1);
        }

        //Удаляем второй промежуток дат
        System.out.println("[-] Удаляем второй промежуток дат");
        $(By.xpath(NewQuotationPage.DatesPeriodsTable.datesTableREG
                + "//tbody//tr[2]//a[@class=\"qbtn qbtn-delete\"]")).scrollTo().click();
        driver.switchTo().alert().accept();
        commonCode.WaitForProgruzka();

        //Проверяем Dates
        System.out.println("[-] Проверяем, что третий промежуток дат в таблице Date стал вторым:");
        //Проверяем От в Dates
        datesFromDate = NewQuotationPage.DatesPeriodsTable.GetFromDateByPeriodCounter(2);
        if (datesFromDate.equals(String.valueOf(formatForDate.format(fromDate3)))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, дата От корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesFromDate)
                    .as("Check that From date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(fromDate3)));
            System.out.println(CommonCode.ANSI_RED +"      Дата От некорректна: " + CommonCode.ANSI_RESET+ datesFromDate);
        }
        //Проверяем До в Dates
        datesTillDate = NewQuotationPage.DatesPeriodsTable.GetTillDateByPeriodCounter(2);
        if (datesTillDate.equals(String.valueOf(formatForDate.format(tillDate3)))){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, дата До корректная + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesTillDate)
                    .as("Check that Till date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(tillDate3)));
            System.out.println(CommonCode.ANSI_RED +"      Дата До некорректна: " + CommonCode.ANSI_RESET+ datesTillDate);
        }

        //Проверяем Accommodations
        System.out.println("[-] Проверяем что даты в Accommodations на своём месте:");
        accommodationDate1 =
                $(By.xpath(NewQuotationPage.AccomodationsTable.AccommodationDateByNumberREG(3)))
                        .scrollTo().getText();
        //System.out.println(datesTillDate);
        if (accommodationDate1.equals(composedPeriodValue3)){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(accommodationDate1)
                    .as("Check that dates in Accommodation are set correctly")
                    .isEqualTo(composedPeriodValue3);
            System.out.println(CommonCode.ANSI_RED +"      Дата некорректные: " + CommonCode.ANSI_RESET
                    + accommodationDate1);
        }

        //Проверяем Results
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что даты в Results верные:");
        resultsDates1 = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr[2]//th")).scrollTo().getText();
        if (resultsDates1.equals(composedPeriodValue3)){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(resultsDates1)
                    .as("Check that dates in Results are set correctly")
                    .isEqualTo(composedPeriodValue3);
            System.out.println(CommonCode.ANSI_RED +"      Даты в Results некорректные: " + CommonCode.ANSI_RESET
                    + resultsDates1);
        }
    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
