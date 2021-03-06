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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.rfqDemoOltatravel.QuotationApp.NewQuotationPage.*;


public class AdvancedScenario1 {
    public ChromeDriver driver;

    QuotationAppCommonCode quotationAppCommonCode = new QuotationAppCommonCode();
    private SoftAssertions softAssertions;
    boolean isWindows=false;

    @Before
    public void setUp() {

        driver = quotationAppCommonCode.InitializeChromeDriver();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void scenario2() {
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
        $(By.id("username")).setValue(props.getProperty("quotationAppLogin"));
        $(By.id("password")).setValue(props.getProperty("quotationAppPassword"));
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(QuotationAppCommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        quotationAppCommonCode.WaitForPageToLoad(driver);
        QuotationAppCommonCode.WaitForProgruzkaSilent();

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
        CreateQuotation("PTestQuotation1", "Тест компания");
        //NewQuotationPage newQuotationPage = new NewQuotationPage();


        //Выставляем валюту в USD
        OptionsTable.SetCurrencyInOptions("USD");

        //Выставляем курс доллара 60
        Double rubUsd = 60.0;
        OptionsTable.SetCurrencyRateForUSD(rubUsd);

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(QuotationAppCommonCode.OK);


        //Меняем колличество ночей на 3
        final int nightNumber = 3;
        OptionsTable.SetNumberOfNightsInOptions(nightNumber);

        //Сохраняем значение комиссии за бронь в SPB
        System.out.print("[-] Сохраняем значение комиссии за бронь в SPB");
        Double registrationFeeForSPB = Double.valueOf($(By.cssSelector(OptionsTable.registrationFeeForSPB)).getText());
        System.out.println(QuotationAppCommonCode.OK);

        //Выставляем Present Meal Services = FB
        System.out.print("[-] Выставляем Present Meal Services = FB ");
        $(By.cssSelector(NewQuotationPage.OptionsTable.presentMealServices)).selectOptionContainingText("FB");
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Добавляем новую дату, дата берётся "сегодня"
        //Получаем текущую дату
        Date nowDate = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print("[-] Добавляем новую дату: " + formatForDateNow.format(nowDate));
        //Кликаем на кнопку Add
        $(By.cssSelector(DatesPeriodsTable.addDateButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).click();

        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).setValue(formatForDateNow.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(DatesPeriodsTable.saveDateButton)).click();
        System.out.println(QuotationAppCommonCode.OK);

        //Добавляем новый Город MSK
        AddCityToAccomodationByName("MSK", 1);

        //Изменяем количество дней в MSK на 2
        AccomodationsTable.SetNightForCityByNumber("MSK", 1, "2");

        //Добавляем новый Город SPB
        AddCityToAccomodationByName("SPB", 2);

        //Переходим к 3-му дню
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3))).scrollTo();

        //Удаляем обед в MSK
        System.out.print("[-] Удаляем обед в MSK");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3) + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(2) + "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]")).scrollTo().click();
        confirm();
        com.rfqDemoOltatravel.CommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Удаляем ужин в MSK
        System.out.print("[-] Удаляем ужин в MSK");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3) + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(2) + "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]")).click();
        confirm();
        com.rfqDemoOltatravel.CommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Удаляем завтрак в SPB
        /*System.out.print("[-] Удаляем завтрак в SPB");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3) + ProgrammSection.GetACityByNumberREG(2) +
                ProgrammSection.GetMainServiceByNumberREG(1) + "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]")).click();
        confirm();
        com.rfqDemoOltatravel.CommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);*/

        //Считаем суммы для проверки
        System.out.print("[-] Считаваем поле Sum в столбце Price DBL");
        //Кликаем на кнопку Show All Prices
        $(By.cssSelector(AccomodationsTable.showAllPricesButton)).click();
        //Считываем значения
        Double priceSGLD = AccomodationsTable.GroupGetPriceSGLForCityForPeriod(1,1)*2;
        Double priceDBLD = AccomodationsTable.GroupGetPriceDBLForCityForPeriod(1,1)*2;

        priceSGLD = priceSGLD + AccomodationsTable.GroupGetPriceSGLForCityForPeriod(2,1);
        priceDBLD = priceDBLD + AccomodationsTable.GroupGetPriceDBLForCityForPeriod(2,1);
        priceDBLD = priceDBLD / 2;
        Double priceSS = priceSGLD - (new BigDecimal(priceDBLD).setScale(0, RoundingMode.DOWN).floatValue());
        priceDBLD = priceDBLD + registrationFeeForSPB;

        System.out.println(QuotationAppCommonCode.OK);

        //Выбор первого класса для поезда Москва->Питер
        /*System.out.print("[-] Выбор первого класса для поезда Москва->Питер");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(3)
                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(2)
                + "//td[@class=\"criteria\"]//select[@name=\"class\"]")).scrollTo().selectOptionByValue("1st");
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);*/

        //Выставляем суммы для 3-х групп: 15, 20, 25
       quotationAppCommonCode.SetValuesForServicesInProgram(250, 200, 150);

        //Добавить в первый день экскурсию Bunker-42
        System.out.print("[-] В первый день добавляем экскурсию \"Бункер-42\":");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.addServiceButtonREG)).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                +ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[3]//select[@class=\"serviceType\"]")).selectOptionContainingText("Excursion");
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                +ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[4]//select[@class=\"serviceName\"]")).selectOptionContainingText("BUNKER-42");
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Добавить во второй день шоу Большой Театр
        System.out.print("[-] Во второй день добавляем шоу \"Большой театр\":");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(2)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.addServiceButtonREG)).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(2)
                +ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[3]//select[@class=\"serviceType\"]")).selectOptionContainingText("Show");
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(2)
                +ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[4]//select[@class=\"serviceName\"]")).selectOptionContainingText("BOLSHOI THEATRE");
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Добавить в третий день Гид из Москвы и Транспорт из Москвы
        System.out.print("[-] В третий день добавляем \"Гид из Москвы\":");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3))).scrollTo();
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3)
                +ProgrammSection.guideFromMoscowREG)).click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        System.out.print("[-] В третий день добавляем \"Транспорт из Москвы\":");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3)
                +ProgrammSection.transportFromMoscowREG)).click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Добавить в четвёртый день Гид из Москвы
        System.out.print("[-] В четвёртый день добавляем \"Гид из Москвы\":");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(4))).scrollTo();
        $(By.xpath(ProgrammSection.GetADayByNumberREG(4)
                +ProgrammSection.guideFromMoscowREG)).click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        double[] sumFromProgram = new double[3];
        double[] sumFromDailyProgram = new double[3];
        //System.out.println(sumFromProgram[0] + " " + sumFromProgram[1] + " " + sumFromProgram[2]);
        //Считаем суммы для 3-х групп: 15, 20, 25
        System.out.println("[-] Считаем суммы для 3-х групп: 15, 20, 25");
        System.out.println("[-] Считаем для Services:");
        quotationAppCommonCode.GetSumsForServices(sumFromProgram);
        System.out.println(com.rfqDemoOltatravel.CommonCode.OK);
        System.out.println("[-] Считаем для Daily Services:");
        quotationAppCommonCode.GetSumsForDailyServices(sumFromDailyProgram);
        //System.out.println(sumFromProgram[0] + " " + sumFromProgram[1] + " " + sumFromProgram[2]);
        System.out.println(QuotationAppCommonCode.OK);
        System.out.println("[-] Суммы за Services посчитаны");

        sumFromProgram[0] = sumFromProgram[0]/15.0;
        sumFromProgram[1] = sumFromProgram[1]/20.0;
        sumFromProgram[2] = sumFromProgram[2]/25.0;

        sumFromDailyProgram[0] = sumFromDailyProgram[0]/15.0;
        sumFromDailyProgram[1] = sumFromDailyProgram[1]/20.0;
        sumFromDailyProgram[2] = sumFromDailyProgram[2]/25.0;

        //System.out.println(programServicesFor15 + " " + programServicesFor20 + " " + programServicesFor25);
        System.out.println("[-] Суммы за Daily Services и Services посчитаны");


        //Запускаем Расчёт
        System.out.println("[-] Запускаем Расчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzka();


        //Сравниваем цену за номер
        System.out.println("[-] Проверяем результаты расчёта:");
        System.out.println("    Проверяем таблицу Hotels (WE) w/o margin:");
        $(By.xpath(ResultsSection.hotelsWOMTableREG)).scrollTo();

        String hotelsWE15womS = $(By.xpath(ResultsSection.hotelsWOMTableREG +"//tbody//tr//td")).getText();
        String hotelsWEwomSSS = $(By.xpath(ResultsSection.hotelsWOMTableREG +"//tbody//tr//td[4]")).getText();
        hotelsWE15womS = hotelsWE15womS.substring(0, hotelsWE15womS.indexOf(' '));
        hotelsWEwomSSS = hotelsWEwomSSS.substring(0, hotelsWEwomSSS.indexOf(' '));
        //System.out.println("hotelsWE 15 w/o marge: " + hotelsWE15womS);
        String priceDBLDS = String.valueOf((int) new BigDecimal(priceDBLD).setScale(0, RoundingMode.DOWN).floatValue());
        String priceSGLDS = String.valueOf((int) new BigDecimal(priceSS).setScale(0, RoundingMode.DOWN).floatValue());
        //Assert.assertEquals(priceDBLDS, hotelsWE15womS);
        if(priceDBLDS.equals(hotelsWE15womS)) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значения для группы 15 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значения для группы 15 неверное: "
                + priceDBLDS + " не равен " + hotelsWE15womS + " -"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(priceDBLDS)
                    .as("Check that value in Hotels (WE) w/o margin for 15 is correct")
                    .isEqualTo(hotelsWE15womS);
        }
        if(priceSGLDS.equals(hotelsWEwomSSS)) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значения для SS верное + "+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значения SS неверное: "
                + priceSGLDS + " не равен " + hotelsWEwomSSS + " -");
            softAssertions.assertThat(priceSGLDS)
                    .as("Check that value in Hotels (WE) w/o margin for SS is correct"+ QuotationAppCommonCode.ANSI_RESET)
                    .isEqualTo(hotelsWEwomSSS);
        }


        /*Double hotelsWE15wom = 0.0;
        hotelsWE15wom = priceDBLD/2;
        System.out.println("Hotels WE w/om 15: " + (new BigDecimal(hotelsWE15wom).setScale(0, RoundingMode.HALF_UP).floatValue()));*/

        System.out.println("    Проверяем таблицу Hotels:");
        Double hotelsWE = priceDBLD;
        hotelsWE = hotelsWE / rubUsd;
        hotelsWE = hotelsWE / generalMarge;
        String hotelsWES = String.valueOf((int) new BigDecimal(hotelsWE).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath(ResultsSection.hotelsTableREG)).scrollTo();
        String hotelsWER = $(By.xpath(ResultsSection.hotelsTableREG+"//tbody//tr//th/following-sibling::td[1]")).getText();
        hotelsWER = hotelsWER.substring(1, hotelsWER.length());
        if(hotelsWES.equals(hotelsWER)) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      - Значения для группы 15 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значения для группы 15 неверное: "
                + hotelsWES + " не равен " + hotelsWER + "-"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWES)
                    .as("Check that value in Hotels (WE) for 15 is correct")
                    .isEqualTo(hotelsWER);
        }

        Double hotelsWESS=0.0;
        hotelsWESS = priceSS;
        hotelsWESS = hotelsWESS / rubUsd /generalMarge;
        String hotelsWESSS = String.valueOf((int) new BigDecimal(hotelsWESS).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath(ResultsSection.hotelsTableREG)).scrollTo();
        hotelsWER = $(By.xpath(ResultsSection.hotelsTableREG+"//tbody//tr//th/following-sibling::td[4]")).getText();
        hotelsWER = hotelsWER.substring(1, hotelsWER.length());
        if(hotelsWESSS.equals(hotelsWER)) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      - Значения для группы SS верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      - Значения для группы SS неверное: "
                + hotelsWESSS + " не равен " + hotelsWER + " -"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWESSS)
                    .as("Check that value in Hotels (WE) for SS is correct")
                    .isEqualTo(hotelsWER);
        }

        System.out.println("    Проверяем таблицу Services:");
        Double services15 = 0.0;
        services15 = sumFromProgram[0];
        services15 = services15 / rubUsd;
        services15 = services15 / generalMarge;

        //System.out.println("Services WE w/om 15: " + (new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath(ResultsSection.servicesTableREG)).scrollTo();
        String services15S = $(By.xpath(ResultsSection.servicesTableREG +"//tbody//tr//th/following-sibling::td[1]")).getText();
        services15S = services15S.substring(1, services15S.length());
        if(services15S.equals(String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значение для группы 15 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + services15S + " не равен " + String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(services15S)
                    .as("Check that value in Services for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double services20 = 0.0;
        services20 = sumFromProgram[1];
        services20 = services20 / rubUsd;
        services20 = services20 / generalMarge;
        //$(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]")).scrollTo();
        String services20S = $(By.xpath(ResultsSection.servicesTableREG +"//tbody//tr//th/following-sibling::td[2]")).getText();
        services20S = services20S.substring(1, services20S.length());
        if(services20S.equals(String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значение для группы 20 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значение для группы 20 неверное: "
                + services20S + " не равен " + String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(services20S)
                    .as("Check that value in Services for 20 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double services25 = 0.0;
        services25 = sumFromProgram[2];
        services25 = services25 / rubUsd;
        services25 = services25 / generalMarge;
        //$(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]")).scrollTo();
        String services25S = $(By.xpath(ResultsSection.servicesTableREG +"//tbody//tr//th/following-sibling::td[3]")).getText();
        services25S = services25S.substring(1, services25S.length());
        if(services25S.equals(String.valueOf((int) new BigDecimal(services25).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значение для группы 25 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значение для группы 25 неверное: "
                + services25S + " не равен " + String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(services25S)
                    .as("Check that value in Services for 25 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services25).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        System.out.println("    Проверяем таблицу Day-Related Services:");
        Double dailyServices15 = 0.0;
        dailyServices15 = sumFromDailyProgram[0];
        dailyServices15 = dailyServices15 / rubUsd;
        dailyServices15 = dailyServices15 / generalMarge;
        $(By.xpath(ResultsSection.dayRelatedServicesTableREG)).scrollTo();
        String dailyServices15S = $(By.xpath(ResultsSection.dayRelatedServicesTableREG +"//tbody//tr//th/following-sibling::td[1]")).getText();
        dailyServices15S = dailyServices15S.substring(1, dailyServices15S.length());
        if(dailyServices15S.equals(String.valueOf((int) new BigDecimal(dailyServices15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значение для группы 15 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + dailyServices15S + " не равен " + String.valueOf((int) new BigDecimal(dailyServices15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(dailyServices15S)
                    .as("Check that value in Day-Related Services for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(dailyServices15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double dailyServices20 = 0.0;
        dailyServices20 = sumFromDailyProgram[1];
        dailyServices20 = dailyServices20 / rubUsd;
        dailyServices20 = dailyServices20 / generalMarge;
        $(By.xpath(ResultsSection.dayRelatedServicesTableREG)).scrollTo();
        String dailyServices20S = $(By.xpath(ResultsSection.dayRelatedServicesTableREG +"//tbody//tr//th/following-sibling::td[2]")).getText();
        dailyServices20S = dailyServices20S.substring(1, dailyServices20S.length());
        if(dailyServices20S.equals(String.valueOf((int) new BigDecimal(dailyServices20).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значение для группы 20 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значение для группы 20 неверное: "
                + dailyServices20S + " не равен " + String.valueOf((int) new BigDecimal(dailyServices20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(dailyServices20S)
                    .as("Check that value in Day-Related Services for 20 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(dailyServices20).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double dailyServices25 = 0.0;
        dailyServices25 = sumFromDailyProgram[2];
        dailyServices25 = dailyServices25 / rubUsd;
        dailyServices25 = dailyServices25 / generalMarge;
        $(By.xpath(ResultsSection.dayRelatedServicesTableREG)).scrollTo();
        String dailyServices25S = $(By.xpath(ResultsSection.dayRelatedServicesTableREG +"//tbody//tr//th/following-sibling::td[3]")).getText();
        dailyServices25S = dailyServices25S.substring(1, dailyServices25S.length());
        if(dailyServices25S.equals(String.valueOf((int) new BigDecimal(dailyServices25).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значение для группы 25 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значение для группы 25 неверное: "
                + dailyServices25S + " не равен " + String.valueOf((int) new BigDecimal(dailyServices25).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(dailyServices25S)
                    .as("Check that value in Day-Related Services for 25 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(dailyServices25).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        System.out.println("    Проверяем таблицу Totals (WE):");
        Double totalWE15 = 0.0;
        totalWE15 = priceDBLD + sumFromProgram[0] + sumFromDailyProgram[0];
        totalWE15 = totalWE15 / rubUsd;
        totalWE15 = totalWE15 / generalMarge;
        //System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath(ResultsSection.totalsTableREG)).scrollTo();
        String totalWE15S = $(By.xpath(ResultsSection.totalsTableREG +"//tbody//tr//th/following-sibling::td[1]")).getText();
        totalWE15S = totalWE15S.substring(1, totalWE15S.length());
        if(totalWE15S.equals(String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значение для группы 15 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + totalWE15S + " не равен " + String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE15S)
                    .as("Check that value in Totals (WE) for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double totalWE20 = 0.0;
        totalWE20 = priceDBLD + sumFromProgram[1] + sumFromDailyProgram[1];
        totalWE20 = totalWE20 / rubUsd;
        totalWE20 = totalWE20 / generalMarge;
        //System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath(ResultsSection.totalsTableREG)).scrollTo();
        String totalWE20S = $(By.xpath(ResultsSection.totalsTableREG +"//tbody//tr//th/following-sibling::td[2]")).getText();
        totalWE20S = totalWE20S.substring(1, totalWE20S.length());
        if(totalWE20S.equals(String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значение для группы 20 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значение для группы 20 неверное: "
                + totalWE20S + " не равен " + String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE20S)
                    .as("Check that value in Totals (WE) for 20 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double totalWE25 = 0.0;
        totalWE25 = priceDBLD + sumFromProgram[2] + sumFromDailyProgram[2];
        totalWE25 = totalWE25 / rubUsd;
        totalWE25 = totalWE25 / generalMarge;
        //System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath(ResultsSection.totalsTableREG)).scrollTo();
        String totalWE25S = $(By.xpath(ResultsSection.totalsTableREG +"//tbody//tr//th/following-sibling::td[3]")).getText();
        totalWE25S = totalWE25S.substring(1, totalWE25S.length());
        if(totalWE25S.equals(String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значение для группы 25 верное +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значение для группы 25 неверное: "
                + totalWE25S + " не равен " + String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE25S)
                    .as("Check that value in Totals (WE) for 25 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

    }

    @After
    public void close() {

        driver.quit();
        softAssertions.assertAll();
    }

}
