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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.rfqDemoOltatravel.NewQuotationPage.*;

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
        System.out.println(CommonCode.OK);


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("test");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(CommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open(props.getProperty("baseURL")+"/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(CommonCode.OK);

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(CommonCode.OK);

        //Создаём новый Quotation
        CreateQuotation("PTestQuotation1", "Тест компания");
        //NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем курс Евро - 70.0
        Double rubEur = 70.0;
        OptionsTable.SetCurrencyRateForEUR(rubEur);

        //Выставляем колество ночей - 1
        int nightInOptionsCounter = 1;
        OptionsTable.SetNumberOfNightsInOptions(nightInOptionsCounter);

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(CommonCode.OK);

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
        System.out.println(CommonCode.OK);

        //Добавляем город
        AddCityToAccomodationByName("MSK", 1);

        //Проганяем стили css для контрола Hotel Price Type
        $(By.cssSelector(OptionsTable.hotelPriceType)).scrollTo().selectOptionContainingText("Individual");
        CommonCode.WaitForProgruzkaSilent();
        $(By.cssSelector(OptionsTable.hotelPriceType)).scrollTo().selectOptionContainingText("Group");
        CommonCode.WaitForProgruzkaSilent();

        //Проверяем, что Hotel Price Type стоит DBL
        System.out.println("[-] Проверяем, что Hotel Price Type стоит DBL:");
        String result = $(By.cssSelector(OptionsTable.hotelPriceType)).scrollTo().getSelectedText();
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
        result = $(By.xpath(accomodationsTableREG + "//thead//th[@class=\"visibility-group hotelType\"]")).scrollTo().getAttribute("style");
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
        result = $(By.xpath(accomodationsTableREG + "//thead//th[@class=\"visibility-individual hotel roomCategory\"]")).scrollTo().getAttribute("style");
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
        result = $(By.xpath(AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotelType visibility-group\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: table-cell;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo("display: table-cell;");
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что дропдаун Hotel Type для Individual скрыт
        System.out.println("[-] Проверяем, что дропдаун Hotel Type для Individual скрыт:");
        result = $(By.xpath(AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotel visibility-individual\"]")).scrollTo().getAttribute("style");
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
        $(By.cssSelector(OptionsTable.hotelPriceType)).scrollTo().selectOptionContainingText("Individual");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Проверяем, что заголовок таблицы Accommodations корректный
        System.out.println("[-] Проверяем, что заголовок таблицы Accommodations корректный:");
        result = $(By.xpath(accomodationsTableREG + "//thead//th[@class=\"visibility-group hotelType\"]")).scrollTo().getAttribute("style");
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
        System.out.println("[-] Проверяем, что заголовок таблицы Accommodations отображается для Individual:");
        result = $(By.xpath(accomodationsTableREG + "//thead//th[@class=\"visibility-individual hotel roomCategory\"]")).scrollTo().getAttribute("style");
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
        System.out.println("[-] Проверяем, что дропдаун Hotel Type для Group скрыт:");
        result = $(By.xpath(AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotelType visibility-group\"]")).scrollTo().getAttribute("style");
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
        System.out.println("[-] Проверяем, что дропдаун Hotel Name для Individual отображается:");
        result = $(By.xpath(AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotel visibility-individual\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: table-cell;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo(String.valueOf("display: table-cell;"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что можно выставить отель
        System.out.print("[-] Проверяем, что можно выставить отель");
        $(By.xpath(AccomodationsTable.CityByNumberREG(1)
                + "//td[@class=\"hotel visibility-individual\"]"
                + "//select[@class=\"hotel\"]")).selectOptionContainingText("COSMOS Hotel");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.ANSI_GREEN+CommonCode.OK+CommonCode.ANSI_RESET);

        //Проверяем, что можно выставить номер
        System.out.print("[-] Проверяем, что можно выставить тип номера");
        $(By.xpath(AccomodationsTable.CityByNumberREG(1)
                + "//td[@class=\"hotel visibility-individual\"]"
                + "//select[@class=\"roomCategory\"]")).selectOptionContainingText("STANDARD");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.ANSI_GREEN+CommonCode.OK+CommonCode.ANSI_RESET);

        //Считаем суммы для проверки
        System.out.print("[-] Считаваем поле Sum в столбце Price DBL");
        //Кликаем на кнопку Show All Prices
        $(By.cssSelector(AccomodationsTable.showAllPricesButton)).click();
        sleep(5000);
        //Считываем значения
        Double priceSGLD = AccomodationsTable.GetPriceSGLForCityForPeriod(1,1)
                *nightInOptionsCounter;
        Double priceDBLD = AccomodationsTable.GetPriceDBLForCityForPeriod(1,1)
                *nightInOptionsCounter;
        //priceDBLD = priceDBLD / 2;
        Double priceSS = priceSGLD - (new BigDecimal(priceDBLD/2).setScale(0, RoundingMode.DOWN).floatValue());
        //System.out.println(priceDBLD);
        //Закрываем модальное окно
        //$(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).click();
        System.out.println(CommonCode.OK);

        //Выставляем суммы для 3-х групп: 15, 20, 25
        commonCode.SetValuesForServicesInProgram(150, 140, 130);

        double programServices[] = {0.0, 0.0, 0.0};
        double programDailyServices[] = {0.0, 0.0, 0.0};

        System.out.println("[-] Считаем суммы для 3-х групп: 15, 20, 25");
        System.out.println("[-] Считаем для Services:");
        commonCode.GetSumsForServices(programServices);
        System.out.println(CommonCode.OK);
        System.out.println("[-] Считаем для Daily Services:");
        commonCode.GetSumsForDailyServices(programDailyServices);
        System.out.println(CommonCode.OK);

        programServices[0] = programServices[0]/15.0;
        programServices[1] = programServices[1]/20.0;
        programServices[2] = programServices[2]/25.0;

        System.out.print("[-] Запускаем перерасчёт: ");
        $(By.id("qbtn-execute")).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        System.out.println("[-] Проверяем результаты расчёта.");

        System.out.println("    Проверяем таблицу Hotels w/o margin:");
        String hotelsWE15womS = $(By.xpath(ResultsSection.hotelsWOMTableREG+"//tbody//tr//td")).getText();
        String hotelsWE20womS = $(By.xpath(ResultsSection.hotelsWOMTableREG+"//tbody//tr//td[2]")).getText();
        String hotelsWE25womS = $(By.xpath(ResultsSection.hotelsWOMTableREG+"//tbody//tr//td[3]")).getText();
        String hotelsWEwomSSS = $(By.xpath(ResultsSection.hotelsWOMTableREG+"//tbody//tr//td[4]")).getText();
        hotelsWE15womS = hotelsWE15womS.substring(0, hotelsWE15womS.indexOf(' '));
        hotelsWE20womS = hotelsWE20womS.substring(0, hotelsWE20womS.indexOf(' '));
        hotelsWE25womS = hotelsWE25womS.substring(0, hotelsWE25womS.indexOf(' '));
        hotelsWEwomSSS = hotelsWEwomSSS.substring(0, hotelsWEwomSSS.indexOf(' '));
        //System.out.println("hotelsWE 15 w/o marge: " + hotelsWE15womS);
        String priceDBLDS = String.valueOf((int) new BigDecimal(priceDBLD/2).setScale(0, RoundingMode.HALF_UP).floatValue());
        String priceDBLDS20 = String.valueOf((int) new BigDecimal(priceDBLD/2).setScale(0, RoundingMode.HALF_UP).floatValue());
        String priceDBLDS25 = String.valueOf((int) new BigDecimal(priceDBLD/2).setScale(0, RoundingMode.HALF_UP).floatValue());
        String priceSGLDS = String.valueOf((int) new BigDecimal(priceSS).setScale(0, RoundingMode.HALF_UP).floatValue());
        //Assert.assertEquals(priceDBLDS, hotelsWE15womS);
        if(priceDBLDS.equals(hotelsWE15womS)) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Значения для группы 15 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значения для группы 15 неверное: "
                + priceDBLDS + " не равен " + hotelsWE15womS + " -"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(priceDBLDS)
                    .as("Check that value in Hotels w/o margin for 15 is correct")
                    .isEqualTo(hotelsWE15womS);
        }
        if(priceDBLDS20.equals(hotelsWE20womS)) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Значения для группы 20 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значения для группы 20 неверное: "
                + priceDBLDS20 + " не равен " + hotelsWE20womS + " -"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(priceDBLDS20)
                    .as("Check that value in Hotels w/o margin for 20 is correct")
                    .isEqualTo(hotelsWE20womS);
        }
        if(priceDBLDS25.equals(hotelsWE25womS)) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Значения для группы 25 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значения для группы 25 неверное: "
                + priceDBLDS25 + " не равен " + hotelsWE25womS + " -"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(priceDBLDS25)
                    .as("Check that value in Hotels w/o margin for 25 is correct")
                    .isEqualTo(hotelsWE25womS);
        }
        if(priceSGLDS.equals(hotelsWEwomSSS)) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Значения для SS верное + "+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значения SS неверное: "
                + priceSGLDS + " не равен " + hotelsWEwomSSS + " -");
            softAssertions.assertThat(priceSGLDS)
                    .as("Check that value in Hotels w/o margin for SS is correct"+CommonCode.ANSI_RESET)
                    .isEqualTo(hotelsWEwomSSS);
        }

        System.out.println("    Проверяем таблицу Hotels:");
        Double hotelsWE15 = priceDBLD/2;
        hotelsWE15 = hotelsWE15 / rubEur;
        hotelsWE15 = hotelsWE15 / generalMarge;
        String hotelsWE15S = String.valueOf((int) new BigDecimal(hotelsWE15).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath(ResultsSection.hotelsTableREG)).scrollTo();
        String hotelsWER = $(By.xpath(ResultsSection.hotelsTableREG+"//tbody//tr//th/following-sibling::td[1]")).getText();
        hotelsWER = hotelsWER.substring(0, hotelsWER.indexOf('€'));//€
        if(hotelsWE15S.equals(hotelsWER)) {
            System.out.println(CommonCode.ANSI_GREEN+"      - Значения для группы 15 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значения для группы 15 неверное: "
                + hotelsWE15S + " не равен " + hotelsWER + "-"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWE15S)
                    .as("Check that value in Hotels for 15 is correct")
                    .isEqualTo(hotelsWER);
        }

        Double hotelsWE20 = priceDBLD/2;
        hotelsWE20 = hotelsWE20 / rubEur;
        hotelsWE20 = hotelsWE20 / generalMarge;
        String hotelsWE20S = String.valueOf((int) new BigDecimal(hotelsWE20).setScale(0, RoundingMode.HALF_UP).floatValue());
        $(By.xpath(ResultsSection.hotelsTableREG)).scrollTo();
        hotelsWER = $(By.xpath(ResultsSection.hotelsTableREG+"//tbody//tr//th/following-sibling::td[2]")).getText();
        hotelsWER = hotelsWER.substring(0, hotelsWER.indexOf('€'));//€
        if(hotelsWE20S.equals(hotelsWER)) {
            System.out.println(CommonCode.ANSI_GREEN+"      - Значения для группы 20 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значения для группы 20 неверное: "
                + hotelsWE20S + " не равен " + hotelsWER + "-"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWE20S)
                    .as("Check that value in Hotels for 20 is correct")
                    .isEqualTo(hotelsWER);
        }

        Double hotelsWE25 = priceDBLD/2;
        hotelsWE25 = hotelsWE25 / rubEur;
        hotelsWE25 = hotelsWE25 / generalMarge;
        String hotelsWE25S = String.valueOf((int) new BigDecimal(hotelsWE25).setScale(0, RoundingMode.HALF_UP).floatValue());
        $(By.xpath(ResultsSection.hotelsTableREG)).scrollTo();
        hotelsWER = $(By.xpath(ResultsSection.hotelsTableREG+"//tbody//tr//th/following-sibling::td[3]")).getText();
        hotelsWER = hotelsWER.substring(0, hotelsWER.indexOf('€'));//€
        if(hotelsWE25S.equals(hotelsWER)) {
            System.out.println(CommonCode.ANSI_GREEN+"      - Значения для группы 25 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значения для группы 25 неверное: "
                + hotelsWE25S + " не равен " + hotelsWER + "-"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWE25S)
                    .as("Check that value in Hotels for 25 is correct")
                    .isEqualTo(hotelsWER);
        }

        Double hotelsWESS=0.0;
        hotelsWESS = priceSS;
        hotelsWESS = hotelsWESS / rubEur /generalMarge;
        String hotelsWESSS = String.valueOf((int) new BigDecimal(hotelsWESS).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath(ResultsSection.hotelsTableREG)).scrollTo();
        hotelsWER = $(By.xpath(ResultsSection.hotelsTableREG+"//tbody//tr//th/following-sibling::td[4]")).getText();
        hotelsWER = hotelsWER.substring(0, hotelsWER.indexOf('€'));
        if(hotelsWESSS.equals(hotelsWER)) {
            System.out.println(CommonCode.ANSI_GREEN+"      - Значения для SS верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      - Значения для SS неверное: "
                + hotelsWESSS + " не равен " + hotelsWER + " -"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWESSS)
                    .as("Check that value in Hotels for SS is correct")
                    .isEqualTo(hotelsWER);
        }

        System.out.println("    Проверяем таблицу Services:");
        Double services15 = 0.0;
        services15 = programServices[0];
        services15 = services15 / rubEur;
        services15 = services15 / generalMarge;

        //System.out.println("Services WE w/om 15: " + (new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath(ResultsSection.servicesTableREG)).scrollTo();
        String services15S = $(By.xpath(ResultsSection.servicesTableREG+"//tbody//tr//th/following-sibling::td[1]")).getText();
        services15S = services15S.substring(0, services15S.indexOf('€'));
        if(services15S.equals(String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Значение для группы 15 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + services15S + " не равен " + String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(services15S)
                    .as("Check that value in Services for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double services20 = 0.0;
        services20 = programServices[1];
        services20 = services20 / rubEur;
        services20 = services20 / generalMarge;

        $(By.xpath(ResultsSection.servicesTableREG)).scrollTo();
        String services20S = $(By.xpath(ResultsSection.servicesTableREG+"//tbody//tr//th/following-sibling::td[2]")).getText();
        services20S = services20S.substring(0, services20S.indexOf('€'));
        if(services20S.equals(String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Значение для группы 20 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значение для группы 20 неверное: "
                + services20S + " не равен " + String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(services20S)
                    .as("Check that value in Services for 20 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double services25 = 0.0;
        services25 = programServices[2];
        services25 = services25 / rubEur;
        services25 = services25 / generalMarge;

        $(By.xpath(ResultsSection.servicesTableREG)).scrollTo();
        String services25S = $(By.xpath(ResultsSection.servicesTableREG+"//tbody//tr//th/following-sibling::td[3]")).getText();
        services25S = services25S.substring(0, services25S.indexOf('€'));
        if(services25S.equals(String.valueOf((int) new BigDecimal(services25).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Значение для группы 25 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значение для группы 25 неверное: "
                + services25S + " не равен " + String.valueOf((int) new BigDecimal(services25).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(services25S)
                    .as("Check that value in Services for 25 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services25).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        System.out.println("    Проверяем таблицу Totals:");
        Double totalWE15 = 0.0;
        totalWE15 = priceDBLD/2 + programServices[0] + programDailyServices[0];
        totalWE15 = totalWE15 / rubEur;
        totalWE15 = totalWE15 / generalMarge;
        //System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath(ResultsSection.totalsTableREG)).scrollTo();
        String totalWE15S = $(By.xpath(ResultsSection.totalsTableREG+"//tbody//tr//th/following-sibling::td[1]")).getText();
        totalWE15S = totalWE15S.substring(0, totalWE15S.indexOf('€'));
        if(totalWE15S.equals(String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Значение для группы 15 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + totalWE15S + " не равен " + String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE15S)
                    .as("Check that value in Totals for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double totalWE20 = 0.0;
        totalWE20 = priceDBLD/2 + programServices[1] + programDailyServices[1];
        totalWE20 = totalWE20 / rubEur;
        totalWE20 = totalWE20 / generalMarge;

        $(By.xpath(ResultsSection.totalsTableREG)).scrollTo();
        String totalWE20S = $(By.xpath(ResultsSection.totalsTableREG+"//tbody//tr//th/following-sibling::td[2]")).getText();
        totalWE20S = totalWE20S.substring(0, totalWE20S.indexOf('€'));
        if(totalWE20S.equals(String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Значение для группы 20 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значение для группы 20 неверное: "
                + totalWE20S + " не равен " + String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE20S)
                    .as("Check that value in Totals for 20 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double totalWE25 = 0.0;
        totalWE25 = priceDBLD/2 + programServices[2] + programDailyServices[2];
        totalWE25 = totalWE25 / rubEur;
        totalWE25 = totalWE25 / generalMarge;

        $(By.xpath(ResultsSection.totalsTableREG)).scrollTo();
        String totalWE25S = $(By.xpath(ResultsSection.totalsTableREG+"//tbody//tr//th/following-sibling::td[3]")).getText();
        totalWE25S = totalWE25S.substring(0, totalWE25S.indexOf('€'));
        if(totalWE25S.equals(String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Значение для группы 25 верное +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Значение для группы 25 неверное: "
                + totalWE25S + " не равен " + String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE25S)
                    .as("Check that value in Totals for 25 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();

    }
}
