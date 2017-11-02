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


public class AdvancedScenario1 {
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
    public void scenario2() {
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
        CommonCode.WaitForProgruzkaSilent();


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
        NewQuotationPage.CreateQuotation(driver, "PTestQuotation1", "Тест компания");
        NewQuotationPage newQuotationPage = new NewQuotationPage();


        //Выставляем валюту в USD
        System.out.print("[-] Выставляем валюту в USD");
        $(By.cssSelector(OptionsTable.currency)).selectOptionContainingText("USD");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Выставляем курс доллара 60
        System.out.print("[-] Выставляем курс доллара 60");
        $(By.cssSelector(OptionsTable.rubUsdRate)).setValue("60").pressEnter();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");
        Double rubUsd = 0.0;
        rubUsd = Double.valueOf($(By.cssSelector(OptionsTable.rubUsdRate)).getText());
        //System.out.println(rubUsd);

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(" - Готово");


        //Меняем колличество ночей на 3
        final int nightNumber = 3;
        System.out.print("[-] Меняем количество ночей на " + nightNumber);
        $(By.cssSelector(OptionsTable.numberOfNights)).click();
        CommonCode.WaitForProgruzkaSilent();
        $(By.cssSelector(OptionsTable.numberOfNights)).setValue(String.valueOf(nightNumber)).pressEnter();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Сохраняем значение комиссии за бронь в SPB
        System.out.print("[-] Сохраняем значение комиссии за бронь в SPB");
        Double registrationFeeForSPB = Double.valueOf($(By.cssSelector(OptionsTable.registrationFeeForSPB)).getText());
        System.out.println(" - Готово");


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

        //Добавляем новый Город MSK
        System.out.print("[-] Добавляем город: MSK");
        //Кликаем Add
        $(By.cssSelector(AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(NewQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с MSK
        $(By.xpath(NewQuotationPage.GetCityNameButtonREG("MSK"))).shouldBe(visible);
        $(By.xpath(NewQuotationPage.GetCityNameButtonREG("MSK"))).click();
        System.out.println(" - Готово");

        //Изменяем количество дней в MSK на 2
        System.out.print("[-] Изменяем количество дней в MSK на 2");
        $(By.cssSelector(NewQuotationPage.accomodationsTable)).scrollTo();
        $(By.cssSelector(NewQuotationPage.accomodationsTable + " tbody tr td[class=\"editable editable-accommodation-nights nights\"]")).click();
        $(By.cssSelector(NewQuotationPage.accomodationsTable + " tbody tr td[class=\"editable editable-accommodation-nights nights\"]")).setValue("2").pressEnter();
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Добавляем новый Город SPB
        System.out.print("[-] Добавляем город: SPB");
        //Кликаем Add
        $(By.cssSelector(AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(NewQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Ждём появления кнопки SPB
        $(By.xpath(NewQuotationPage.GetCityNameButtonREG("SPB"))).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Переходим к 3-му дню
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3))).scrollTo();

        //Удаляем обед в MSK
        System.out.print("[-] Удаляем обед в MSK");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3) + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(2) + "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]")).scrollTo().click();
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Удаляем ужин в MSK
        System.out.print("[-] Удаляем ужин в MSK");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3) + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(2) + "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]")).click();
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Удаляем завтрак в SPB
        System.out.print("[-] Удаляем завтрак в SPB");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3) + ProgrammSection.GetACityByNumberREG(2) +
                ProgrammSection.GetMainServiceByNumberREG(1) + "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]")).click();
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Считаем суммы для проверки
        System.out.print("[-] Считаваем поле Sum в столбце Price DBL");
        //Кликаем на кнопку Show All Prices
        $(By.cssSelector(AccomodationsTable.showAllPricesButton)).click();

        String priceDBL = "";
        String priceSGL = "";
        Double priceSGLD = 0.0;
        Double priceDBLD = 0.0;
        //Кликаем на кнопку prices первого отеля
        $(By.xpath("//table[@id=\"table-accommodations\"]//tbody//tr[1]//table[@class=\"prices\"]//tbody//tr//a[@class=\"qbtn qbtn-prices\"]")).click();
        //Ждём появления модального окна с ценами отеля
        $(By.cssSelector("div#modal-accommodation-days-prices div[class=\"modal-dialog\"] div[class=\"modal-content\"]")).shouldBe(visible);
        //Сохраняем сумму дабл в переменную
        priceSGL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[2]")).shouldBe(visible).getText();
        priceDBL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[3]")).getText();
        //System.out.println(priceDBL);
        priceSGLD = priceSGLD + Double.valueOf(priceSGL);
        priceDBLD = priceDBLD + Double.valueOf(priceDBL);
        //System.out.println("priceDBLD = "+priceDBLD);
        //Закрываем модальное окно
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).click();
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).shouldNotBe(visible);


        //Кликаем на кнопку prices вротого отеля
        //$(By.xpath("//table[@id=\"table-accommodations\"]//tbody//tr[2]//table[@class=\"prices\"]//tbody//tr//a[@class=\"qbtn qbtn-prices\"]")).shouldBe(visible);
        $(By.xpath("//table[@id=\"table-accommodations\"]//tbody//tr[2]//table[@class=\"prices\"]//tbody//tr//a[@class=\"qbtn qbtn-prices\"]")).click();
        //Ждём появления модального окна с ценами отеля
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] div[class=\"modal-dialog\"] div[class=\"modal-content\"]")).shouldBe(visible);
        //Сохраняем сумму дабл в переменную
        priceSGL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[2]")).shouldBe(visible).getText();
        priceDBL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[3]")).getText();
        //System.out.println(priceDBL);
        priceSGLD = priceSGLD + Double.valueOf(priceSGL);
        priceDBLD = priceDBLD + Double.valueOf(priceDBL);
        Double priceSS = priceSGLD - priceDBLD/2;
        priceDBLD = priceDBLD/2 + registrationFeeForSPB;

        //System.out.println("priceDBLD = "+priceDBLD);
        //Закрываем модальное окно
        $(By.cssSelector("div[id=\"modal-dialog\"]")).shouldNotBe(visible);
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).shouldBe(visible).click();

        System.out.println(" - Готово");


        Double programServicesFor15 = 15.0;
        Double programServicesFor20 = 20.0;
        Double programServicesFor25 = 25.0;

        //Выставляем суммы для 3-х групп: 15, 20, 25
        System.out.println("[-] Выставляем суммы для 3-х групп: 15, 20, 25");
        int dayCounterMax = nightNumber + 1;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            System.out.print("      - для дня номер " + dayCounter);
            int cityCounterMax = $$(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)+"//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++){

                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

                int serviceCounterMax= $$(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) +
                        ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= serviceCounterMax; serviceCounter++) {

                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                            + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(1))).scrollTo().click();


                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                            + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(1))).setValue(programServicesFor15.toString()).pressEnter();

                    programServicesFor15 = programServicesFor15 + 5.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);



                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                            + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(2))).click();

                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                            + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(2))).setValue(programServicesFor20.toString()).pressEnter();

                    programServicesFor20 = programServicesFor20 + 6.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);


                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                            + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(3))).click();

                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                            + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(3))).setValue(programServicesFor25.toString()).pressEnter();


                    programServicesFor25 = programServicesFor25 + 7.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
                }

                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//tr//td//a[@class=\"qbtn qbtn-hideallprices\"]")).shouldBe(visible).click();

            }
            System.out.println(" - готово");

        }

        //Добавить в первый день экскурсию Bunker-42
        System.out.print("[-] В первый день добавляем экскурсию \"Бункер-42\":");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                +NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.addServiceButtonREG)).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                +ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[3]//select[@class=\"serviceType\"]")).selectOptionContainingText("Excursion");
        CommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                +ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[4]//select[@class=\"serviceName\"]")).selectOptionContainingText("Bunker-42");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Добавить во второй день шоу Большой Театр
        System.out.print("[-] Во второй день добавляем шоу \"Большой театр\":");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(2)
                +NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.addServiceButtonREG)).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(2)
                +ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[3]//select[@class=\"serviceType\"]")).selectOptionContainingText("Show");
        CommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(2)
                +ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[4]//select[@class=\"serviceName\"]")).selectOptionContainingText("Bolshoi theatre");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Добавить в третий день Гид из Москвы и Транспорт из Москвы
        System.out.print("[-] В третий день добавляем \"Гид из Москвы\":");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(3))).scrollTo();
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3)
                +ProgrammSection.guideFromMoscowREG)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        System.out.print("[-] В третий день добавляем \"Транспорт из Москвы\":");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3)
                +ProgrammSection.transportFromMoscowREG)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Добавить в четвёртый день Гид из Москвы
        System.out.print("[-] В четвёртый день добавляем \"Гид из Москвы\":");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(4))).scrollTo();
        $(By.xpath(ProgrammSection.GetADayByNumberREG(4)
                +ProgrammSection.guideFromMoscowREG)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        programServicesFor15 = 0.0;
        programServicesFor20 = 0.0;
        programServicesFor25 = 0.0;

        Double programDailyServicesFor15 = 0.0;
        Double programDailyServicesFor20 = 0.0;
        Double programDailyServicesFor25 = 0.0;

        //Считаем суммы для 3-х групп: 15, 20, 25
        System.out.println("[-] Считаем суммы для 3-х групп: 15, 20, 25");
        dayCounterMax = nightNumber + 1;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            System.out.print("      - считаем для дня номер "+ dayCounter);

            //Считаем для Daily Services
            $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)+ProgrammSection.showAllDailyPricesREG)).scrollTo().click();
            if(($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)
                    +ProgrammSection.guideFromMoscowREG)).isSelected())
                    || ($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)
                    +ProgrammSection.goldenRingGuide)).isSelected())
                    || ($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)
                    +ProgrammSection.transportFromMoscowREG)).isSelected())){

                int dalyServicesMax = $$(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)
                        +"//div[@class=\"serviceByDayInfo\"]//table[@class=\"serviceByDayTable services\"]//tbody//tr[@class=\"service_by_day\"]")).size();
                //System.out.println(dalyServicesMax+"");
                for(int dailyServiceCounter=1;dailyServiceCounter<=dalyServicesMax;dailyServiceCounter++){

                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)
                            + ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                            + ProgrammSection.GetDailySumForPeopleREG(1))).scrollTo();

                    programDailyServicesFor15 = programDailyServicesFor15 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)
                                    + ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                                    + ProgrammSection.GetDailySumForPeopleREG(1))).getText());


                    programDailyServicesFor20 = programDailyServicesFor20 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)
                                    + ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                                    + ProgrammSection.GetDailySumForPeopleREG(2))).getText());

                    programDailyServicesFor25 = programDailyServicesFor25 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)
                                    + ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                                    + ProgrammSection.GetDailySumForPeopleREG(3))).getText());
                }
                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)+ProgrammSection.hideAllDailyPricesREG)).click();
            }

            //Считаем для Services
            int cityCounterMax = $$(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)+"//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++){

                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

                int serviceCounterMax= $$(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) +
                        ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= serviceCounterMax; serviceCounter++) {

                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                            + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForPeopleREG(1))).scrollTo();

                    programServicesFor15 = programServicesFor15 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForPeopleREG(1))).getText());
                    //System.out.println(programServicesFor15);

                    programServicesFor20 = programServicesFor20 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForPeopleREG(2))).getText());

                    programServicesFor25 = programServicesFor25 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForPeopleREG(3))).getText());
                }

                int autoServiceCounterMax= $$(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) +
                        ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"auto\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= autoServiceCounterMax; serviceCounter++) {

                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter) + ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(1))).scrollTo();

                    programServicesFor15 = programServicesFor15 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForPeopleREG(1))).getText());

                    programServicesFor20 = programServicesFor20 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForPeopleREG(2))).getText());

                    programServicesFor25 = programServicesFor25 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForPeopleREG(3))).getText());
                }

                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//table[@class=\"services\"]//tfoot//a[@class=\"qbtn qbtn-hideallprices\"]")).scrollTo().click();

            }
            System.out.println(" - готово");


        }
        programServicesFor15 = programServicesFor15/15.0;
        programServicesFor20 = programServicesFor20/20.0;
        programServicesFor25 = programServicesFor25/25.0;

        programDailyServicesFor15 = programDailyServicesFor15/15.0;
        programDailyServicesFor20 = programDailyServicesFor20/20.0;
        programDailyServicesFor25 = programDailyServicesFor25/25.0;

        //System.out.println(programServicesFor15 + " " + programServicesFor20 + " " + programServicesFor25);
        System.out.println("[-] Суммы за Daily Services и Services посчитаны");


        //Запускаем Расчёт
        System.out.println("[-] Запускаем Расчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzka();


        //Сравниваем цену за номер
        System.out.println("[-] Проверяем результаты расчёта:");
        System.out.println("    Проверяем таблицу Hotels (WE) w/o margin:");
        $(By.id("table-result-hotels-wo-margin-we")).scrollTo();

        String hotelsWE15womS = $(By.cssSelector("table[id=\"table-result-hotels-wo-margin-we\"] tbody tr td")).getText();
        String hotelsWEwomSSS = $(By.xpath("//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr//td[4]")).getText();
        hotelsWE15womS = hotelsWE15womS.substring(0, hotelsWE15womS.indexOf(' '));
        hotelsWEwomSSS = hotelsWEwomSSS.substring(0, hotelsWEwomSSS.indexOf(' '));
        //System.out.println("hotelsWE 15 w/o marge: " + hotelsWE15womS);
        String priceDBLDS = String.valueOf((int) new BigDecimal(priceDBLD).setScale(0, RoundingMode.HALF_UP).floatValue());
        String priceSGLDS = String.valueOf((int) new BigDecimal(priceSS).setScale(0, RoundingMode.HALF_UP).floatValue());
        //Assert.assertEquals(priceDBLDS, hotelsWE15womS);
        if(priceDBLDS.equals(hotelsWE15womS)) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значения для группы 15 верное +"+commonCode.ANSI_RESET);
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


        /*Double hotelsWE15wom = 0.0;
        hotelsWE15wom = priceDBLD/2;
        System.out.println("Hotels WE w/om 15: " + (new BigDecimal(hotelsWE15wom).setScale(0, RoundingMode.HALF_UP).floatValue()));*/

        System.out.println("    Проверяем таблицу Hotels (WE):");
        Double hotelsWE = priceDBLD;
        hotelsWE = hotelsWE / rubUsd;
        hotelsWE = hotelsWE / generalMarge;
        String hotelsWES = String.valueOf((int) new BigDecimal(hotelsWE).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-we\"]")).scrollTo();
        String hotelsWER = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-we\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        hotelsWER = hotelsWER.substring(1, hotelsWER.length());
        if(hotelsWES.equals(hotelsWER)) {
            System.out.println(commonCode.ANSI_GREEN+"      - Значения для группы 15 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения для группы 15 неверное: "
                + hotelsWES + " не равен " + hotelsWER + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWES)
                    .as("Check that value in Hotels (WE) for 15 is correct")
                    .isEqualTo(hotelsWER);
        }

        Double hotelsWESS=0.0;
        hotelsWESS = priceSS;
        hotelsWESS = hotelsWESS / rubUsd /generalMarge;
        String hotelsWESSS = String.valueOf((int) new BigDecimal(hotelsWESS).setScale(0, RoundingMode.HALF_UP).floatValue());
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

        System.out.println("    Проверяем таблицу Services:");
        Double services15 = 0.0;
        services15 = programServicesFor15;
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
                + services15S + " не равен " + String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(services15S)
                    .as("Check that value in Services for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double services20 = 0.0;
        services20 = programServicesFor20;
        services20 = services20 / rubUsd;
        services20 = services20 / generalMarge;
        //$(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]")).scrollTo();
        String services20S = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-services\"]//tbody//tr//th/following-sibling::td[2]")).getText();
        services20S = services20S.substring(1, services20S.length());
        if(services20S.equals(String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 20 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 20 неверное: "
                + services20S + " не равен " + String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(services20S)
                    .as("Check that value in Services for 20 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double services25 = 0.0;
        services25 = programServicesFor25;
        services25 = services25 / rubUsd;
        services25 = services25 / generalMarge;
        //$(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]")).scrollTo();
        String services25S = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-services\"]//tbody//tr//th/following-sibling::td[3]")).getText();
        services25S = services25S.substring(1, services25S.length());
        if(services25S.equals(String.valueOf((int) new BigDecimal(services25).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 25 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 25 неверное: "
                + services25S + " не равен " + String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(services25S)
                    .as("Check that value in Services for 25 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services25).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        System.out.println("    Проверяем таблицу Day-Related Services:");
        Double dailyServices15 = 0.0;
        dailyServices15 = programDailyServicesFor15;
        dailyServices15 = dailyServices15 / rubUsd;
        dailyServices15 = dailyServices15 / generalMarge;
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-services-by-days\"]")).scrollTo();
        String dailyServices15S = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-services-by-days\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        dailyServices15S = dailyServices15S.substring(1, dailyServices15S.length());
        if(dailyServices15S.equals(String.valueOf((int) new BigDecimal(dailyServices15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 15 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + dailyServices15S + " не равен " + String.valueOf((int) new BigDecimal(dailyServices15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(dailyServices15S)
                    .as("Check that value in Day-Related Services for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(dailyServices15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double dailyServices20 = 0.0;
        dailyServices20 = programDailyServicesFor20;
        dailyServices20 = dailyServices20 / rubUsd;
        dailyServices20 = dailyServices20 / generalMarge;
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-services-by-days\"]")).scrollTo();
        String dailyServices20S = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-services-by-days\"]//tbody//tr//th/following-sibling::td[2]")).getText();
        dailyServices20S = dailyServices20S.substring(1, dailyServices20S.length());
        if(dailyServices20S.equals(String.valueOf((int) new BigDecimal(dailyServices20).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 20 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 20 неверное: "
                + dailyServices20S + " не равен " + String.valueOf((int) new BigDecimal(dailyServices20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(dailyServices20S)
                    .as("Check that value in Day-Related Services for 20 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(dailyServices20).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double dailyServices25 = 0.0;
        dailyServices25 = programDailyServicesFor25;
        dailyServices25 = dailyServices25 / rubUsd;
        dailyServices25 = dailyServices25 / generalMarge;
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-services-by-days\"]")).scrollTo();
        String dailyServices25S = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-services-by-days\"]//tbody//tr//th/following-sibling::td[3]")).getText();
        dailyServices25S = dailyServices25S.substring(1, dailyServices25S.length());
        if(dailyServices25S.equals(String.valueOf((int) new BigDecimal(dailyServices25).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 25 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 25 неверное: "
                + dailyServices25S + " не равен " + String.valueOf((int) new BigDecimal(dailyServices25).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(dailyServices25S)
                    .as("Check that value in Day-Related Services for 25 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(dailyServices25).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        System.out.println("    Проверяем таблицу Totals (WE):");
        Double totalWE15 = 0.0;
        totalWE15 = priceDBLD + programServicesFor15 + programDailyServicesFor15;
        totalWE15 = totalWE15 / rubUsd;
        totalWE15 = totalWE15 / generalMarge;
        //System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-totals\"]")).scrollTo();
        String totalWE15S = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-totals\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        totalWE15S = totalWE15S.substring(1, totalWE15S.length());
        if(totalWE15S.equals(String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 15 верное "+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + totalWE15S + " не равен " + String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE15S)
                    .as("Check that value in Totals (WE) for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double totalWE20 = 0.0;
        totalWE20 = priceDBLD + programServicesFor20 + programDailyServicesFor20;
        totalWE20 = totalWE20 / rubUsd;
        totalWE20 = totalWE20 / generalMarge;
        //System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-totals\"]")).scrollTo();
        String totalWE20S = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-totals\"]//tbody//tr//th/following-sibling::td[2]")).getText();
        totalWE20S = totalWE20S.substring(1, totalWE20S.length());
        if(totalWE20S.equals(String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 20 верное "+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 20 неверное: "
                + totalWE20S + " не равен " + String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE20S)
                    .as("Check that value in Totals (WE) for 20 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double totalWE25 = 0.0;
        totalWE25 = priceDBLD + programServicesFor25 + programDailyServicesFor25;
        totalWE25 = totalWE25 / rubUsd;
        totalWE25 = totalWE25 / generalMarge;
        //System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-totals\"]")).scrollTo();
        String totalWE25S = $(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-totals\"]//tbody//tr//th/following-sibling::td[3]")).getText();
        totalWE25S = totalWE25S.substring(1, totalWE25S.length());
        if(totalWE25S.equals(String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 25 верное "+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 25 неверное: "
                + totalWE25S + " не равен " + String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
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
