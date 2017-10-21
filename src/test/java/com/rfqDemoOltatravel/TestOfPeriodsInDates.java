package com.rfqDemoOltatravel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class TestOfPeriodsInDates {

    public ChromeDriver driver;

    private SoftAssertions softAssertions;
    CommonCode commonCode = new CommonCode();

    public class PeriodsCollection{
        public LocalDate dateFrom;
        public LocalDate dateTo;
        public int priceSGL;
        public int priceDBL;
        public int priceSGLWE;
        public int priceDBLWE;

        PeriodsCollection(String ... data) {
          if(data.length>=1)
              dateFrom = LocalDate.of(Integer.valueOf(data[0].substring(6,data[0].length())), Integer.valueOf(data[0].substring(3,5)), Integer.valueOf(data[0].substring(0,2)));
          if(data.length>=2)
              dateTo = LocalDate.of(Integer.valueOf(data[1].substring(6,data[1].length())), Integer.valueOf(data[1].substring(3,5)), Integer.valueOf(data[1].substring(0,2)));
          if(data.length>=3)
              priceSGL = Integer.valueOf(data[2]);
          if(data.length>=4)
              priceDBL = Integer.valueOf(data[3]);
          if(data.length>=5)
              priceSGLWE = Integer.valueOf(data[4]);
          if(data.length>=6)
              priceDBLWE = Integer.valueOf(data[5]);
        }
    }

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
        commonCode.WaitForProgruzkaSilent();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем приложение Prices");
        open("http://rfq-demo.oltatravel.com/application/olta.prices");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем групповые цены
        System.out.print("[-] Открываем групповые цены");
        $(By.cssSelector("li[id=\"group\"]")).click();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Выбираем город - SPB
        System.out.print("[-] Выбираем город - SPB");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-city\"] button[data-switch-value=\"SPB\"]")).click();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Выбираем Hotel 4* central
        System.out.print("[-] Выбираем Hotel 4* central");
        $(By.cssSelector("div[id=\"filters-bar\"] select[id=\"hotel-type-filter\"]")).selectOptionContainingText("Hotel 4* central");
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем текущий день
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatForPrices = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        LocalDate ldt = LocalDate.now();
        System.out.println(ldt.format(formatForDate));
        System.out.println(ldt.format(formatForPrices));

        //Скролим к 2017 году
        $(By.xpath("//div[@id=\"content\"]//center[contains(text(),'2017')]")).scrollTo();
        //Откраваем 01-01-2017
        $(By.xpath("//div[@id=\"content\"]//div[@id=\"hotel-calendar\"]//div[@data-year=\"2017\"]" +
                "//div//table//tbody//tr" +
                "//td[@data-date=\"2017-01-01\"]")).click();
        commonCode.WaitForProgruzkaSilent();


        List<PeriodsCollection> periodsList = new ArrayList<>();

        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);
        //Сохраняем значения из попапа
        String dateFrom = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
        String dateTo = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
        String priceSGL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
        String priceDBL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
        String priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
        String priceDBLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();

        //Сохраняем значения в новый элемент списка
        periodsList.add(new PeriodsCollection(dateFrom, dateTo, priceSGL, priceDBL, priceSGLWE, priceDBLWE));

        //System.out.println(dateFrom+" "+dateTo+" "+priceSGL+" "+priceDBL+" "+priceSGLWE+" "+priceDBLWE);
        //Закрываем попап
        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);

        //Получаем дату начала следующего периода
        LocalDate dateToNext = LocalDate.of(Integer.valueOf(dateTo.substring(6,dateTo.length())), Integer.valueOf(dateTo.substring(3,5)), Integer.valueOf(dateTo.substring(0,2))).plusDays(1);

        //Проходим по всем периодам и сохраняем значения в список
        System.out.print("[-] Проходим по всем периодам и сохраняем значения в список");
        while(!dateTo.equals("31-12-2017")){
            $(By.xpath("//div[@id=\"content\"]//div[@id=\"hotel-calendar\"]//div[@data-year=\"2017\"]" +
                    "//div//table//tbody//tr" +
                    "//td[@data-date=\""+dateToNext.format(formatForPrices)+"\"]")).scrollTo().click();

            $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);

            dateFrom = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
            dateTo = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
            priceSGL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
            priceDBL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
            priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
            priceDBLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();

            periodsList.add(new PeriodsCollection(dateFrom, dateTo, priceSGL, priceDBL, priceSGLWE, priceDBLWE));

            $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
            $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);

            dateToNext = LocalDate.of(Integer.valueOf(dateTo.substring(6,dateTo.length())), Integer.valueOf(dateTo.substring(3,5)), Integer.valueOf(dateTo.substring(0,2))).plusDays(1);

        }
        System.out.println(" - готово");

        /*for(int i=0;i<periodsList.size();i++) {
            System.out.println(periodsList.get(i).dateTo);
            System.out.println(periodsList.get(i).priceDBLWE);
        }*/

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

        //Сохраняем значение комиссии за бронь в SPB
        System.out.print("[-] Сохраняем значение комиссии за бронь в SPB");
        int registrationFeeForSPB = Integer.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.registrationFeeForSPB)).getText());
        System.out.println(" - Готово");

        //Переключаемся на Periods в Dates/Periods
        System.out.print("[-] Переключаемся на Periods в Dates/Periods");
        $(By.cssSelector("span#qbtn-periods")).click();
        driver.switchTo().alert().accept();
        driver.switchTo().alert().accept();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Выставляем период с 01-01-2017 до 31-12-2017
        System.out.print("[-] Выставляем период с 01-01-2017 до 31-12-2017");
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.addPeriodButton)).click();

        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.fromPeriodInputField)).click();
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.fromPeriodInputField)).sendKeys("01-01-2017");

        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.toPeriodInputField)).click();
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.toPeriodInputField)).sendKeys("31-12-2017");

        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.savePeriodButton)).click();

        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Добавляем город
        System.out.print("[-] Добавляем город: SPB");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).isDisplayed();
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).isDisplayed();
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).click();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        System.out.print("[-] Запускаем расчёт ");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Проверяем, что колличество периодов верное
        $(By.cssSelector("div#result table#table-result-hotels-wo-margin-we tbody tr")).scrollTo();
        int periodsInResult = $$(By.cssSelector("div#result table#table-result-hotels-wo-margin-we tbody tr")).size();
        int periodsFromPrices = periodsList.size();
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

        for(int periodsCounter=1; periodsCounter <= periodsList.size(); periodsCounter++){

            result = $(By.cssSelector("div#result table#table-result-hotels-wo-margin-we tbody tr:nth-of-type("+(periodsCounter)+") th")).getText();

            if(periodsList.get(periodsCounter-1).dateFrom.getMonth() == periodsList.get(periodsCounter-1).dateTo.getMonth())
                composedPeriodValue = periodsList.get(periodsCounter-1).dateFrom.format(formatForResultsDayOnly)+" - "+periodsList.get(periodsCounter-1).dateTo.format(formatForResultsFull);
            else
                composedPeriodValue = periodsList.get(periodsCounter-1).dateFrom.format(formatForResultsDayMonthOnly)+" - "+periodsList.get(periodsCounter-1).dateTo.format(formatForResultsFull);

            if(periodsCounter==1)
                composedPeriodValue = periodsList.get(periodsCounter-1).dateFrom.format(formatForResultsFull)+" - "+periodsList.get(periodsCounter-1).dateTo.format(formatForResultsFull);

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
        for(int periodsCounter=1; periodsCounter <= periodsList.size(); periodsCounter++){
            priceDBLDS = String.valueOf((int) new BigDecimal(Double.valueOf(periodsList.get(periodsCounter-1).priceDBL)/2.0
                    +Double.valueOf(registrationFeeForSPB)).setScale(0, RoundingMode.HALF_UP).floatValue());

            result = $(By.cssSelector("div#result table#table-result-hotels-wo-margin-we tbody tr:nth-of-type("+(periodsCounter)+") td")).getText();
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


        //Проверяем, что цены в Hotels (WE) верные
        Double hotelsWE;
        System.out.println("[-] Проверяем, что цены в Hotels (WE) верные:");
        for(int periodsCounter=1; periodsCounter <= periodsList.size(); periodsCounter++){
            hotelsWE = Double.valueOf(periodsList.get(periodsCounter-1).priceDBL)/2.0+Double.valueOf(registrationFeeForSPB);
            hotelsWE = hotelsWE / rubEur;
            hotelsWE = hotelsWE / generalMarge;
            priceDBLDS = String.valueOf((int) new BigDecimal(hotelsWE).setScale(0, RoundingMode.HALF_UP).floatValue());

            result = $(By.cssSelector("div#result table#table-result-hotels-we tbody tr:nth-of-type("+(periodsCounter)+") td")).getText();
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
    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();

    }
}
