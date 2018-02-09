package com.rfqDemoOltatravel.QuotationApp;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
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

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.open;
import static com.rfqDemoOltatravel.QuotationApp.NewQuotationPage.AddCityToAccomodationByName;

public class TestOfDates {

    public ChromeDriver driver;

    private SoftAssertions softAssertions;
    QuotationAppCommonCode quotationAppCommonCode = new QuotationAppCommonCode();
    boolean isWindows=false;

    @Before
    public void setUp() {

        driver = quotationAppCommonCode.InitializeChromeDriver();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void testOfDates() {
        isWindows=false;
        if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){isWindows=true;}
        WebDriverRunner.setWebDriver(driver);

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

        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;
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
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(QuotationAppCommonCode.OK);

        //Создаём новый Quotation
        NewQuotationPage.CreateQuotation("PTestQuotation1", "Тест компания");

        //Выставляем курс Евро
        Double rubEur = 70.0;
        NewQuotationPage.OptionsTable.SetCurrencyRateForEUR(rubEur);

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 2;
        NewQuotationPage.OptionsTable.SetNumberOfNightsInOptions(nightInOptionsCounter);

        //Выставляем Present Meal Services = FB
        System.out.print("[-] Выставляем Present Meal Services = FB ");
        $(By.cssSelector(NewQuotationPage.OptionsTable.presentMealServices)).selectOptionContainingText("FB");
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

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
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        DateTimeFormatter formatDayOnly = DateTimeFormatter.ofPattern("d")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatDayMonthOnly = DateTimeFormatter.ofPattern("d MMMM")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatFull = DateTimeFormatter.ofPattern("d MMMM yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        //Добавляем город
        AddCityToAccomodationByName("MSK", 1);

        String composedPeriodValue;
        if(nowDate.getMonth() == tillDate1.getMonth())
            composedPeriodValue = nowDate.format(formatDayOnly)+" - "+tillDate1.format(formatFull);
        else
            composedPeriodValue = nowDate.format(formatDayMonthOnly)+" - "+tillDate1.format(formatFull);

        if(nowDate.getYear() != tillDate1.getYear())
            composedPeriodValue = nowDate.format(formatFull)+" - "+tillDate1.format(formatFull);

        //Проверяем что даты в Accommodations верные
        System.out.println("[-] Проверяем что даты в Accommodations верные:");
        String accommodationDate1 =
                $(By.xpath(NewQuotationPage.AccomodationsTable.AccommodationDateByNumberREG(2)))
                        .scrollTo().getText();

        //System.out.println(datesTillDate);
        if (accommodationDate1.equals(composedPeriodValue)){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, даты корректная + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(accommodationDate1)
                    .as("Check that dates in Accommodation are set correctly")
                    .isEqualTo(composedPeriodValue);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата До некорректна: " + QuotationAppCommonCode.ANSI_RESET
                    + accommodationDate1);
        }

        //Проверяем, что в Results даты тоже корректные
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что даты в Results верные:");
        String resultsDates1 = $(By.xpath(NewQuotationPage.ResultsSection.hotelsWOMTableREG+"//tbody//tr[1]//th")).scrollTo().getText();

        if(nowDate.getMonth() == tillDate1.getMonth())
            composedPeriodValue = nowDate.format(formatDayOnly)+" - "+tillDate1.format(formatFull);
        else
            composedPeriodValue = nowDate.format(formatDayMonthOnly)+" - "+tillDate1.format(formatFull);

        if(nowDate.getYear() != tillDate1.getYear())
            composedPeriodValue = nowDate.format(formatFull)+" - "+tillDate1.format(formatFull);

        if (resultsDates1.equals(composedPeriodValue)){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(resultsDates1)
                    .as("Check that dates in Results are set correctly")
                    .isEqualTo(composedPeriodValue);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Даты в Results некорректные: " + QuotationAppCommonCode.ANSI_RESET
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
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Проверяем Dates
        System.out.println("[-] Проверяем, что промежуток дат в таблице Date на своём месте:");
        //Проверяем От в Dates
        String datesFromDate = NewQuotationPage.DatesPeriodsTable.GetFromDateByPeriodCounter(2);
        if (datesFromDate.equals(String.valueOf(formatForDate.format(fromDate3)))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, дата От корректная + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesFromDate)
                    .as("Check that From date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(fromDate3)));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата От некорректна: " + QuotationAppCommonCode.ANSI_RESET+ datesFromDate);
        }
        //Проверяем До в Dates
        String datesTillDate = NewQuotationPage.DatesPeriodsTable.GetTillDateByPeriodCounter(2);
        if (datesTillDate.equals(String.valueOf(formatForDate.format(tillDate3)))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, дата До корректная + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesTillDate)
                    .as("Check that Till date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(tillDate3)));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата До некорректна: " + QuotationAppCommonCode.ANSI_RESET+ datesTillDate);
        }

        //Проверяем Accommodations
        System.out.println("[-] Проверяем что новые даты в Accommodations на своём месте:");
        accommodationDate1 =
                $(By.xpath(NewQuotationPage.AccomodationsTable.AccommodationDateByNumberREG(3)))
                        .scrollTo().getText();
        String composedPeriodValue3;
        if(fromDate3.getMonth() == tillDate3.getMonth())
            composedPeriodValue3 = fromDate3.format(formatDayOnly)+" - "+tillDate3.format(formatFull);
        else
            composedPeriodValue3 = fromDate3.format(formatDayMonthOnly)+" - "+tillDate3.format(formatFull);

        if(fromDate3.getYear() != tillDate3.getYear())
            composedPeriodValue3 = fromDate3.format(formatFull)+" - "+tillDate3.format(formatFull);

        //System.out.println(datesTillDate);
        if (accommodationDate1.equals(composedPeriodValue3)){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(accommodationDate1)
                    .as("Check that dates in Accommodation are set correctly")
                    .isEqualTo(composedPeriodValue3);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата некорректные: " + QuotationAppCommonCode.ANSI_RESET
                    + accommodationDate1);
        }
        //Проверяем, что в Results даты тоже корректные
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что даты в Results верные:");
        resultsDates1 = $(By.xpath(NewQuotationPage.ResultsSection.hotelsWOMTableREG+"//tbody//tr[2]//th")).scrollTo().getText();
        if (resultsDates1.equals(composedPeriodValue3)){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(resultsDates1)
                    .as("Check that dates in Results are set correctly")
                    .isEqualTo(composedPeriodValue3);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Даты в Results некорректные: " + QuotationAppCommonCode.ANSI_RESET
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
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Проверяем Dates
        System.out.println("[-] Проверяем, что промежуток дат в таблице Date на своём месте:");
        //Проверяем От в Dates
        datesFromDate = NewQuotationPage.DatesPeriodsTable.GetFromDateByPeriodCounter(2);
        if (datesFromDate.equals(String.valueOf(formatForDate.format(fromDate2)))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, дата От корректная + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesFromDate)
                    .as("Check that From date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(fromDate2)));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата От некорректна: " + QuotationAppCommonCode.ANSI_RESET+ datesFromDate);
        }
        //Проверяем До в Dates
        datesTillDate = NewQuotationPage.DatesPeriodsTable.GetTillDateByPeriodCounter(2);
        if (datesTillDate.equals(String.valueOf(formatForDate.format(tillDate2)))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, дата До корректная + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesTillDate)
                    .as("Check that Till date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(tillDate2)));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата До некорректна: " + QuotationAppCommonCode.ANSI_RESET+ datesTillDate);
        }

        //Проверяем Accommodations
        System.out.println("[-] Проверяем что новые даты в Accommodations на своём месте:");
        accommodationDate1 =
                $(By.xpath(NewQuotationPage.AccomodationsTable.AccommodationDateByNumberREG(3)))
                        .scrollTo().getText();
        if(fromDate2.getMonth() == tillDate2.getMonth())
            composedPeriodValue = fromDate2.format(formatDayOnly)+" - "+tillDate2.format(formatFull);
        else
            composedPeriodValue = fromDate2.format(formatDayMonthOnly)+" - "+tillDate2.format(formatFull);

        if(fromDate2.getYear() != tillDate2.getYear())
            composedPeriodValue = fromDate2.format(formatFull)+" - "+tillDate2.format(formatFull);
        //System.out.println(datesTillDate);
        if (accommodationDate1.equals(composedPeriodValue)){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(accommodationDate1)
                    .as("Check that dates in Accommodation are set correctly")
                    .isEqualTo(composedPeriodValue);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата некорректные: " + QuotationAppCommonCode.ANSI_RESET
                    + accommodationDate1);
        }
        //Проверяем Results
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что даты в Results верные:");
        resultsDates1 = $(By.xpath(NewQuotationPage.ResultsSection.hotelsWOMTableREG+"//tbody//tr[2]//th")).scrollTo().getText();
        if (resultsDates1.equals(composedPeriodValue)){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(resultsDates1)
                    .as("Check that dates in Results are set correctly")
                    .isEqualTo(composedPeriodValue);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Даты в Results некорректные: " + QuotationAppCommonCode.ANSI_RESET
                    + resultsDates1);
        }

        //Удаляем второй промежуток дат
        System.out.println("[-] Удаляем второй промежуток дат");
        $(By.xpath(NewQuotationPage.DatesPeriodsTable.datesTableREG
                + "//tbody//tr[2]//a[@class=\"qbtn qbtn-delete\"]")).scrollTo().click();
        confirm();
        QuotationAppCommonCode.WaitForProgruzka();

        //Проверяем Dates
        System.out.println("[-] Проверяем, что третий промежуток дат в таблице Date стал вторым:");
        //Проверяем От в Dates
        datesFromDate = NewQuotationPage.DatesPeriodsTable.GetFromDateByPeriodCounter(2);
        if (datesFromDate.equals(String.valueOf(formatForDate.format(fromDate3)))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, дата От корректная + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesFromDate)
                    .as("Check that From date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(fromDate3)));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата От некорректна: " + QuotationAppCommonCode.ANSI_RESET+ datesFromDate);
        }
        //Проверяем До в Dates
        datesTillDate = NewQuotationPage.DatesPeriodsTable.GetTillDateByPeriodCounter(2);
        if (datesTillDate.equals(String.valueOf(formatForDate.format(tillDate3)))){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, дата До корректная + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(datesTillDate)
                    .as("Check that Till date is set correctly, in Dates")
                    .isEqualTo(String.valueOf(formatForDate.format(tillDate3)));
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата До некорректна: " + QuotationAppCommonCode.ANSI_RESET+ datesTillDate);
        }

        //Проверяем Accommodations
        System.out.println("[-] Проверяем что даты в Accommodations на своём месте:");
        accommodationDate1 =
                $(By.xpath(NewQuotationPage.AccomodationsTable.AccommodationDateByNumberREG(3)))
                        .scrollTo().getText();
        //System.out.println(datesTillDate);
        if (accommodationDate1.equals(composedPeriodValue3)){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(accommodationDate1)
                    .as("Check that dates in Accommodation are set correctly")
                    .isEqualTo(composedPeriodValue3);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Дата некорректные: " + QuotationAppCommonCode.ANSI_RESET
                    + accommodationDate1);
        }

        //Проверяем Results
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что даты в Results верные:");
        resultsDates1 = $(By.xpath(NewQuotationPage.ResultsSection.hotelsWOMTableREG+"//tbody//tr[2]//th")).scrollTo().getText();
        if (resultsDates1.equals(composedPeriodValue3)){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Ошибки нет, даты корректные + "+ QuotationAppCommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(resultsDates1)
                    .as("Check that dates in Results are set correctly")
                    .isEqualTo(composedPeriodValue3);
            System.out.println(QuotationAppCommonCode.ANSI_RED +"      Даты в Results некорректные: " + QuotationAppCommonCode.ANSI_RESET
                    + resultsDates1);
        }
    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
