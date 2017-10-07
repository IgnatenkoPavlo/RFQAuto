package com.rfqDemoOltatravel;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.rfqDemoOltatravel.NewQuotationPage.*;

//Базовый сценарий + добавление SPB
public class BaseScenario2 {
    public ChromeDriver driver;

    CommonCode commonCode = new CommonCode();

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Automation\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void scenario2() {
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

        //Выставляем валюту в USD
        System.out.println("[-] Выставляем валюту в USD");
        $(By.cssSelector(OptionsTable.currency)).selectOptionContainingText("USD");
        commonCode.WaitForProgruzka();

        //Выставляем курс доллара 60
        System.out.println("[-] Выставляем курс доллара 60");
        $(By.cssSelector(OptionsTable.rubUsdRate)).setValue("60").pressEnter();
        commonCode.WaitForProgruzka();
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
        commonCode.WaitForProgruzka();
        $(By.cssSelector(OptionsTable.numberOfNights)).setValue(String.valueOf(nightNumber)).pressEnter();
        System.out.println(" - Готово");

        commonCode.WaitForProgruzka();

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
        $(By.cssSelector(DatesPeriodsTable.addButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).click();

        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).setValue(formatForDateNow.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(DatesPeriodsTable.saveButton)).click();
        System.out.println(" - Готово");

        //Добавляем новый Город MSK
        System.out.print("[-] Добавляем город: MSK");
        //Кликаем Add
        $(By.cssSelector(AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).isDisplayed();
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).isDisplayed();
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).click();
        System.out.println(" - Готово");

        //Изменяем количество дней в MSK на 2
        System.out.print("[-] Изменяем количество дней в MSK на 2");
        $(By.cssSelector(AccomodationsTable.accomodationsTable)).scrollTo();
        $(By.cssSelector(AccomodationsTable.accomodationsTable + " tbody tr td[class=\"editable editable-accommodation-nights nights\"]")).click();
        $(By.cssSelector(AccomodationsTable.accomodationsTable + " tbody tr td[class=\"editable editable-accommodation-nights nights\"]")).setValue("2").pressEnter();
        driver.switchTo().alert().accept();
        System.out.println(" - Готово");

        commonCode.WaitForProgruzka();

        //Добавляем новый Город SPB
        System.out.print("[-] Добавляем город: SPB");
        //Кликаем Add
        $(By.cssSelector(AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).isDisplayed();
        //Ждём появления кнопки SPB
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("SPB"))).click();
        System.out.println(" - Готово");

        //Ждём пока страница прогрузится
        commonCode.WaitForProgruzka();

        //Переходим к 3-му дню
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3))).scrollTo();

        //Удаляем обед в MSK
        System.out.print("[-] Удаляем обед в MSK");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3) + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetAServiceByNumberREG(2) + "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]")).scrollTo().click();

        driver.switchTo().alert().accept();
        System.out.println(" - Готово");

        commonCode.WaitForProgruzka();

        //Удаляем ужин в MSK
        System.out.print("[-] Удаляем ужин в MSK");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3) + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAServiceByNumberREG(2) + "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]")).click();
        driver.switchTo().alert().accept();
        System.out.println(" - Готово");

        commonCode.WaitForProgruzka();

        //Удаляем завтрак в SPB
        System.out.print("[-] Удаляем завтрак в SPB");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(3) + ProgrammSection.GetACityByNumberREG(2) +
                ProgrammSection.GetAServiceByNumberREG(1) + "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]")).click();
        driver.switchTo().alert().accept();
        System.out.println(" - Готово");


        commonCode.WaitForProgruzka();

        //Считаем суммы для проверки
        System.out.print("[-] Считаваем полz Sum в столбце Price DBL");
        //Кликаем на кнопку Show All Prices
        $(By.cssSelector(AccomodationsTable.showAllPricesButton)).click();

        String priceDBL = "";
        Double priceDBLD = 0.0;
        //Кликаем на кнопку prices первого отеля
        $(By.xpath("//table[@id=\"table-accommodations\"]//tbody//tr[1]//table[@class=\"prices\"]//tbody//tr//a[@class=\"qbtn qbtn-prices\"]")).click();
        //Ждём появления модального окна с ценами отеля
        $(By.cssSelector("div[id=\"modal-dialog\"]")).isDisplayed();
        //Сохраняем сумму дабл в переменную
        priceDBL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[3]")).getText();
        //System.out.println(priceDBL);
        priceDBLD = priceDBLD + Double.valueOf(priceDBL);
        //System.out.println("priceDBLD = "+priceDBLD);
        //Закрываем модальное окно
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).click();
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).shouldNotBe(visible);


        //Кликаем на кнопку prices вротого отеля
        //$(By.xpath("//table[@id=\"table-accommodations\"]//tbody//tr[2]//table[@class=\"prices\"]//tbody//tr//a[@class=\"qbtn qbtn-prices\"]")).isDisplayed();
        $(By.xpath("//table[@id=\"table-accommodations\"]//tbody//tr[2]//table[@class=\"prices\"]//tbody//tr//a[@class=\"qbtn qbtn-prices\"]")).click();
        //Ждём появления модального окна с ценами отеля
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] div[class=\"modal-dialog\"] div[class=\"modal-content\"]")).shouldBe(visible);
        //Сохраняем сумму дабл в переменную
        priceDBL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[3]")).getText();
        //System.out.println(priceDBL);
        priceDBLD = priceDBLD + Double.valueOf(priceDBL);
        priceDBLD = priceDBLD/2 + registrationFeeForSPB;
        //System.out.println("priceDBLD = "+priceDBLD);
        //Закрываем модальное окно
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).shouldBe(visible).click();
        $(By.cssSelector("div[id=\"modal-dialog\"]")).shouldNotBe(visible);

        System.out.println(" - Готово");


        Double programFor15 = 10.0;
        Double programFor20 = 20.0;
        Double programFor25 = 25.0;

        //Считаем суммы для 3-х групп: 15, 20, 25
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
                            + ProgrammSection.GetAServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(1))).scrollTo().click();


                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetAServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForUnitREG(1))).setValue(programFor15.toString()).pressEnter();

                    programFor15 = programFor15 + 5.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);



                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                            + ProgrammSection.GetAServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(2))).click();

                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetAServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForUnitREG(2))).setValue(programFor20.toString()).pressEnter();

                    programFor20 = programFor20 + 6.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);


                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                            + ProgrammSection.GetAServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(3))).click();

                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetAServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForUnitREG(3))).setValue(programFor25.toString()).pressEnter();


                    programFor25 = programFor25 + 7.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
                }

                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//tr//td//a[@class=\"qbtn qbtn-hideallprices\"]")).shouldBe(visible).click();

                }
            System.out.println(" - готово");

        }


        programFor15 = 0.0;
        programFor20 = 0.0;
        programFor25 = 0.0;

        //Считаем суммы для 3-х групп: 15, 20, 25
        System.out.println("[-] Считаем суммы для 3-х групп: 15, 20, 25");
        dayCounterMax = nightNumber + 1;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            System.out.print("      - считаем для дня номер "+ dayCounter);

            int cityCounterMax = $$(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter)+"//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++){

                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo();
                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).click();

                int serviceCounterMax= $$(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) +
                        ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= serviceCounterMax; serviceCounter++) {

                    $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                            + ProgrammSection.GetAServiceByNumberREG(serviceCounter)
                            + ProgrammSection.GetSumForUnitREG(1))).scrollTo();

                    programFor15 = programFor15 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetAServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForUnitREG(1))).getText());

                    programFor20 = programFor20 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetAServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForUnitREG(2))).getText());

                    programFor25 = programFor25 +
                            Double.valueOf($(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + ProgrammSection.GetAServiceByNumberREG(serviceCounter)
                                    + ProgrammSection.GetSumForUnitREG(3))).getText());
                }

                $(By.xpath(ProgrammSection.GetADayByNumberREG(dayCounter) + ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-hideallprices\"]")).shouldBe(visible).click();

                }
            System.out.println(" - готово");


        }
        //System.out.println(programFor15 + " " + programFor20 + " " + programFor25);
        System.out.println("[-] Суммы за Services посчитаны");


        //Запускаем Расчёт
        System.out.println("[-] Запускаем Расчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzka();


        //Сравниваем цену за номер
        System.out.println("[-] Проверяем результаты расчёта:");
        $(By.id("table-result-hotels-wo-margin-we")).scrollTo();

        String hotelsWE15womS = $(By.cssSelector("table[id=\"table-result-hotels-wo-margin-we\"] tbody tr td")).getText();
        hotelsWE15womS = hotelsWE15womS.substring(0, hotelsWE15womS.indexOf(' '));
        //System.out.println("hotelsWE 15 w/o marge: " + hotelsWE15womS);
        String priceDBLDS = String.valueOf((int) new BigDecimal(priceDBLD).setScale(0, RoundingMode.HALF_UP).floatValue());
        //Assert.assertEquals(priceDBLDS, hotelsWE15womS);
        if(priceDBLDS.equals(hotelsWE15womS)) {
            System.out.println("      -  Таблица Hotels (WE) w/o margin содержит верные значения +");
        }
        else System.out.println("      -  Таблица Hotels (WE) w/o margin содержит неверные значения: "
                + priceDBLDS + " не равен " + hotelsWE15womS + " -");


        /*Double hotelsWE15wom = 0.0;
        hotelsWE15wom = priceDBLD/2;
        System.out.println("Hotels WE w/om 15: " + (new BigDecimal(hotelsWE15wom).setScale(0, RoundingMode.HALF_UP).floatValue()));*/


        Double hotelsWE = priceDBLD;
        hotelsWE = hotelsWE / rubUsd;
        hotelsWE = hotelsWE / generalMarge;
        String hotelsWES = String.valueOf((int) new BigDecimal(hotelsWE).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]")).scrollTo();
        String hotelsWER = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        hotelsWER = hotelsWER.substring(1, hotelsWER.length());
        if(hotelsWES.equals(hotelsWER)) {
            System.out.println("      -  Таблица Hotels (WE) содержит верные значения +");
        }
        else System.out.println("      -  Таблица Hotels (WE) содержит неверные значения: "
                + hotelsWES + " не равен " + hotelsWER + "-");


        Double services15 = 0.0;
        services15 = programFor15;
        services15 = services15 / rubUsd;
        services15 = services15 / generalMarge;
        //System.out.println("Services WE w/om 15: " + (new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]")).scrollTo();
        String services15S = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        services15S = services15S.substring(1, services15S.length());
        if(services15S.equals(String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println("      -  Таблица Services содержит верные значения +");
        }
        else System.out.println("      -  Таблица Services содержит неверные значения: "
                + services15S + " не равен " + String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-");



        Double totalWE15 = 0.0;
        totalWE15 = priceDBLD + programFor15;
        totalWE15 = totalWE15 / rubUsd;
        totalWE15 = totalWE15 / generalMarge;
        //System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-totals\"]")).scrollTo();
        String totalWE15S = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-totals\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        totalWE15S = totalWE15S.substring(1, totalWE15S.length());
        if(totalWE15S.equals(String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println("      -  Таблица Totals (WE) содержит верные значения +");
        }
        else System.out.println("      -  Таблица Totals (WE) содержит неверные значения: "
                + totalWE15S + " не равен " + String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-");


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
    }

}
