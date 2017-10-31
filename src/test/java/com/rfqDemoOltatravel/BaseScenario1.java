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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.rfqDemoOltatravel.NewQuotationPage.*;


public class BaseScenario1 {

   public ChromeDriver driver;

    CommonCode commonCode = new CommonCode();
    private SoftAssertions softAssertions;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Automation\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void scenario1() {
        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();

        Properties props=new Properties();
        try {
            props.load(new InputStreamReader(new FileInputStream("src\\test\\resources\\application.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        selenideConfig.timeout = 30000;

        System.out.print("Получил проперти ");
        String baseURL = props.getProperty("baseURL");
        System.out.println(baseURL);

        /*System.out.print("[-] Открываем URL: http://"++");
        open("http://rfq-demo.oltatravel.com/");
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
        CommonCode.WaitForProgruzkaSilent();


        //Открываем Quotation приложение
        System.out.println("[-] Открываем Quotation приложение");
        open("http://rfq-demo.oltatravel.com/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(" - Готово");

        //Создаём новый Quotation
        NewQuotationPage.CreateQuotation("PTestQuotation1", "Тест компания");
        NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем валюту в USD
        System.out.println("[-] Выставляем валюту в USD");
        $(By.cssSelector(OptionsTable.currency)).selectOptionContainingText("USD");
        CommonCode.WaitForProgruzkaSilent();

        //Выставляем курс доллара 60
        System.out.println("[-] Выставляем курс доллара 60");
        $(By.cssSelector(OptionsTable.rubUsdRate)).setValue("60").pressEnter();
        CommonCode.WaitForProgruzkaSilent();
        Double rubUsd = 0.0;
        rubUsd = Double.valueOf($(By.cssSelector(OptionsTable.rubUsdRate)).getText());
        //System.out.println(rubUsd);

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(" - Готово");


        //Меняем колличество ночей на 3
        System.out.print("[-] Меняем количество ночей на 3");
        $(By.cssSelector(OptionsTable.numberOfNights)).click();
        CommonCode.WaitForProgruzkaSilent();
        $(By.cssSelector(OptionsTable.numberOfNights)).setValue("3");
        $(By.cssSelector(OptionsTable.numberOfNights)).pressEnter();
        System.out.println(" - Готово");

        CommonCode.WaitForProgruzkaSilent();

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
        System.out.println(" - Готово");

        //$$(By.cssSelector("table[id=\"table-groups\"]"));

        //Добавляем новый Город
        System.out.print("[-] Добавляем город: MSK");
        //Кликаем Add
        $(By.cssSelector(AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.cssSelector("div[id=\"modal-cityselector\"] div[class=\"modal-dialog\"]")).shouldBe(visible);
        //Ждём появления кнопки MSK
        $(By.xpath(newQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).shouldHave(text("MSK")).click();
        System.out.println(" - Готово");

        //Ждём пока страница прогрузится
        CommonCode.WaitForProgruzkaSilent();


        //Считаем суммы для проверки
        System.out.print("[-] Считаваем поле Sum в столбце Price DBL");
        //Кликаем на кнопку Show All Prices
        $(By.cssSelector(AccomodationsTable.showAllPricesButton)).click();
       
        //Кликаем на кнопку prices
        $(By.cssSelector("table[id=\"table-accommodations\"] a[class=\"qbtn qbtn-prices\"]")).click();
        //Ждём появления модального окна с ценами отеля
        $(By.cssSelector("div#modal-accommodation-days-prices div[class=\"modal-dialog\"] div[class=\"modal-content\"]")).shouldBe(visible);
        //Сохраняем сумму дабл в переменную
        String priceSGL = "";
        String priceDBL = "";
        priceSGL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[2]")).getText();
        priceDBL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[3]")).getText();
        //System.out.println(priceDBL);
        Double priceSGLD = Double.valueOf(priceSGL);
        Double priceDBLD = Double.valueOf(priceDBL);
        priceDBLD = priceDBLD / 2;
        Double priceSS = priceSGLD - priceDBLD;
        //System.out.println(priceDBLD);
        //Закрываем модальное окно
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).click();
        System.out.println(" - Готово");


        Double programFor15 = 0.0;
        Double programFor20 = 0.0;
        Double programFor25 = 0.0;

        //Считаем суммы для 3-х групп: 15, 20, 25
        System.out.println("[-] Считаем суммы для 3-х групп: 15, 20, 25");
        int dayCounterMax = 3 + 1;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            System.out.print("      - считаем для дня номер "+ dayCounter);

            int cityCounterMax = $$(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)+"//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++){

                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo();
                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).click();

                int mainServiceCounterMax= $$(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) +
                        ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= mainServiceCounterMax; serviceCounter++) {

                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter) + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(1))).scrollTo();

                    programFor15 = programFor15 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForUnitREG(1))).getText());

                    programFor20 = programFor20 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForUnitREG(2))).getText());

                    programFor25 = programFor25 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForUnitREG(3))).getText());
                }

                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-hideallprices\"]")).shouldBe(visible);
                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-hideallprices\"]")).click();
            }
            System.out.println(" - готово");


        }
        //System.out.println(programFor15 + " " + programFor20 + " " + programFor25);
        System.out.println("[-] Суммы за Services посчитаны");


        //Запускаем Расчёт
        System.out.println("[-] Запускаем Расчёт");
        $(By.id("qbtn-execute")).scrollTo();
        $(By.id("qbtn-execute")).click();
        CommonCode.WaitForProgruzkaSilent();


        //Сравниваем цену за номер
        System.out.println("[-] Проверяем результаты расчёта:");
        $(By.id("table-result-hotels-wo-margin-we")).scrollTo();

        //Проверяем таблицу Hotels (WE) w/o margin
        System.out.println("    Проверяем таблицу Hotels (WE) w/o margin:");
        String hotelsWE15womS = $(By.xpath("//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr//td[1]")).getText();
        String hotelsWEwomSSS = $(By.xpath("//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr//td[4]")).getText();
        hotelsWE15womS = hotelsWE15womS.substring(0, hotelsWE15womS.indexOf(' '));
        hotelsWEwomSSS = hotelsWEwomSSS.substring(0, hotelsWEwomSSS.indexOf(' '));
        //System.out.println("hotelsWE15 15 w/o marge: " + hotelsWE15womS);
        String priceSGLDS = String.valueOf((int) new BigDecimal(priceSS).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println(priceSS);
        String priceDBLDS = String.valueOf((int) new BigDecimal(priceDBLD).setScale(0, RoundingMode.HALF_UP).floatValue());
        //Assert.assertEquals(priceDBLDS, hotelsWE15womS);

        if(priceDBLDS.equals(hotelsWE15womS)) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значения для группы 15 верное + "+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения для группы 15 неверное: "
                + priceDBLDS + " не равен " + hotelsWE15womS + " -"+commonCode.ANSI_RESET);
            softAssertions.assertThat(priceDBLDS)
                    .as("Check that value in Hotels (WE) w/o margin for 15 is correct")
                    .isEqualTo(hotelsWE15womS);
        }

        if(priceSGLDS.equals(hotelsWEwomSSS)) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значения для SS верное + "+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения SS неверное: "
                + priceSGLDS + " не равен " + hotelsWEwomSSS + " -");
            softAssertions.assertThat(priceSGLDS)
                    .as("Check that value in Hotels (WE) w/o margin for SS is correct"+commonCode.ANSI_RESET)
                    .isEqualTo(hotelsWEwomSSS);
        }


        //Проверяем таблицу Hotels (WE)
        System.out.println("    Проверяем таблицу Hotels (WE):");

        Double hotelsWE15 = 0.0;
        hotelsWE15 = priceDBLD;
        hotelsWE15 = hotelsWE15 / rubUsd;
        hotelsWE15 = hotelsWE15 / generalMarge;

        Double hotelsWESS=0.0;
        hotelsWESS = priceSS;
        hotelsWESS = hotelsWESS / rubUsd /generalMarge;

        String hotelsWES = String.valueOf((int) new BigDecimal(hotelsWE15).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-we\"]")).scrollTo();
        String hotelsWER = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-we\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        hotelsWER = hotelsWER.substring(1, hotelsWER.length());
        if(hotelsWES.equals(hotelsWER)) {
            System.out.println(commonCode.ANSI_GREEN+"      - Значения для группы 15 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения для группы 15 неверное: "
                + hotelsWES + " не равен " + hotelsWER + " -"+commonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWES)
                    .as("Check that value in Hotels (WE) for 15 is correct")
                    .isEqualTo(hotelsWER);

        }

        String hotelsWESSS = String.valueOf((int) new BigDecimal(hotelsWESS).setScale(0, RoundingMode.DOWN).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-we\"]")).scrollTo();
        hotelsWER = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-we\"]//tbody//tr//th/following-sibling::td[4]")).getText();
        hotelsWER = hotelsWER.substring(1, hotelsWER.length());
        if(hotelsWESSS.equals(hotelsWER)) {
            System.out.println(commonCode.ANSI_GREEN+"      - Значения для группы SS верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      - Значения для группы SS неверное: "
                + hotelsWESSS + " не равен " + hotelsWER + " -"+commonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWESSS)
                    .as("Check that value in Hotels (WE) for SS is correct")
                    .isEqualTo(hotelsWER);
        }


        //Проверяем таблицу Services
        System.out.println("    Проверяем таблицу Services:");
        Double services15 = 0.0;
        services15 = programFor15;
        services15 = services15 / rubUsd;
        services15 = services15 / generalMarge;
        //System.out.println("Services WE w/om 15: " + (new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-services\"]")).scrollTo();
        String services15S = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-services\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        services15S = services15S.substring(1, services15S.length());
        if(services15S.equals(String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 15 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + services15S + " не равен " + String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()) + " -"+commonCode.ANSI_RESET);
            softAssertions.assertThat(services15S)
                    .as("Check that value in Services for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }



        //Проверяем таблицу Totals (WE)
        System.out.println("    Проверяем таблицу Totals (WE):");
        Double totalWE15 = 0.0;
        totalWE15 = priceDBLD + programFor15;
        totalWE15 = totalWE15 / rubUsd;
        totalWE15 = totalWE15 / generalMarge;
        //System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-totals\"]")).scrollTo();
        String totalWE15S = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-totals\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        totalWE15S = totalWE15S.substring(1, totalWE15S.length());
        if(totalWE15S.equals(String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 15 верное +"+commonCode.ANSI_RESET);
        }
        else System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + totalWE15S + " не равен " + String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()) + " -"+commonCode.ANSI_RESET);

        String totalWESSS = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-totals\"]//tbody//tr//th/following-sibling::td[4]")).getText();
        totalWESSS = totalWESSS.substring(1, totalWESSS.length());
        if(totalWESSS.equals(hotelsWESSS)) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы SS верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы SS неверное: "
                + totalWESSS + " не равен " + hotelsWESSS + " -"+commonCode.ANSI_RESET);
            softAssertions.assertThat(totalWESSS)
                    .as("Check that value in Totals (WE) for 15 is correct")
                    .isEqualTo(hotelsWESSS);
        }*/



        /*Double totalWE20 = 0.0;
        totalWE20 = priceDBLD + programFor20;
        totalWE20 = totalWE20 / rubUsd;
        totalWE20 = totalWE20 / generalMarge;
        System.out.println("Total WE 20: " + new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue());

        Double totalWE25 = 0.0;
        totalWE25 = priceDBLD + programFor20;
        totalWE25 = totalWE25 / rubUsd;
        totalWE25 = totalWE25 / generalMarge;
        System.out.println("Total WE 35: " + new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue());*/


    }

   @After
    public void close() {

       driver.quit();
       softAssertions.assertAll();
    }

}
