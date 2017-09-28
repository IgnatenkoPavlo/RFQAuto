package com.rfqDemoOltatravel;

import com.codeborne.selenide.WebDriverRunner;
import org.apache.bcel.generic.RETURN;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverRunner.*;
import static com.rfqDemoOltatravel.NewQuotationPage.*;


public class BaseScenario1 {

   public ChromeDriver driver;

    public void waitForPageToLoad() {
        JavascriptExecutor js = driver;

        if(js.executeScript("return document.readyState").toString().equals("compleate")) {
            System.out.println("[-] страница загружена - URL: " + url());
            return;
        }

        int totalTime = 0;
        int numberOfIterations = 0;

        for (int i=0; i < 120; i++) {
            try {
                Thread.sleep(250);
                totalTime = totalTime + 1;
                numberOfIterations = numberOfIterations + 1;

            } catch (InterruptedException e) {
            }
            if (js.executeScript("return document.readyState").toString().equals("complete")) break;
        }
        System.out.println("[-] ждали открытия страницы - URL: " + url() + " - " + totalTime + " сек., кол-во повторений: " + numberOfIterations);
    }



    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Automation\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void scenario1() {
        WebDriverRunner.setWebDriver(driver);
        open("http://rfq-demo.oltatravel.com/");
        waitForPageToLoad();

        //Вводим логин с паролем и кликаем Логин
        $(By.id("username")).setValue("pavel.sales");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();

        //Ждём пока загрузится страница и проподёт "Loading..."
        waitForPageToLoad();
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);


        //Открываем Quotation приложение
        open("http://rfq-demo.oltatravel.com/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        waitForPageToLoad();
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);

        //Ждём доступности "Create New Quotation"
        $(By.id("qbtn-create")).isDisplayed();

        //Создаём новый Quotation
        $(By.id("qbtn-create")).click();
        driver.switchTo().alert().sendKeys("PTestQuotation1");
        driver.switchTo().alert().accept();
        driver.switchTo().alert().sendKeys("PTestClient1");
        driver.switchTo().alert().accept();

        NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Ждём пока страница прогрузится
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);

        Double rubUsd = 0.0;
        rubUsd = Double.valueOf($(By.cssSelector("table[id=\"table-options\"] tr[data-key=\"rub_usd_rate\"] td[class=\"value editable editable-quotatoin-option-value\"]")).getText());
        System.out.println(rubUsd);

        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector("table[id=\"table-options\"] tr[data-key=\"general_marge\"] td[class=\"value editable editable-quotatoin-option-value\"]")).getText()).replace(',', '.'));
        System.out.println(generalMarge);


        //Меняем колличество ночей на 3
        $(By.cssSelector("table[id=\"table-options\"] tr[data-key=\"number_of_nights\"] td[class=\"value editable editable-quotatoin-option-value\"]")).click();
        $(By.cssSelector("table[id=\"table-options\"] tr[data-key=\"number_of_nights\"] td[class=\"value editable editable-quotatoin-option-value\"]")).setValue("3");
        $(By.cssSelector("table[id=\"table-options\"] tr[data-key=\"number_of_nights\"] td[class=\"value editable editable-quotatoin-option-value\"]")).pressEnter();
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);

        //Добавляем новую дату, дата берётся "сегодня"
        //Кликаем на кнопку Add
        $(By.cssSelector(DatesPeriodsTable.addButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).click();
        //Получаем текущую дату
        Date nowDate = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).setValue(formatForDateNow.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(DatesPeriodsTable.saveButton)).click();

        //$$(By.cssSelector("table[id=\"table-groups\"]"));

        //Добавляем новый Город
        //Кликаем Add
        $(By.cssSelector(AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.cssSelector("div[id=\"modal-dialog\"]")).isDisplayed();
        //Ждём появления кнопки MSK
        $(By.cssSelector(newQuotationPage.cityNameButton)).isDisplayed();
        //Кликаем по кнопке с MSK
        $(By.cssSelector(newQuotationPage.cityNameButton)).shouldHave(text("MSK")).click();
        //Ждём пока страница прогрузится
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);


        //Считаем суммы для проверки
        //Кликаем на кнопку Show All Prices
        $(By.cssSelector(AccomodationsTable.showAllPricesButton)).click();
       
        //Кликаем на кнопку prices
        $(By.cssSelector("table[id=\"table-accommodations\"] a[class=\"qbtn qbtn-prices\"]")).click();
        //Ждём появления модального окна с ценами отеля
        $(By.cssSelector("div[id=\"modal-dialog\"]")).isDisplayed();
        //Сохраняем сумму дабл в переменную
        String priceDBL = "";
        priceDBL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[3]")).getText();
        //System.out.println(priceDBL);
        Double priceDBLD = Double.valueOf(priceDBL);
        priceDBLD = priceDBLD / 2;
        System.out.println(priceDBLD);
        //Закрываем модальное окно
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).click();


        Double programFor15 = 0.0;
        Double programFor20 = 0.0;
        Double programFor25 = 0.0;

        //Считаем суммы для 3-х групп: 15, 20, 25
        for (int dayCounter = 1; dayCounter <= 4; dayCounter++) {

            $(By.xpath(ProgrammSection.GetADay(dayCounter) + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo();
            $(By.xpath(ProgrammSection.GetADay(dayCounter) + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).click();

            for (int serviceCounter = 1; serviceCounter <= 3; serviceCounter++) {
                $(By.xpath(ProgrammSection.GetADay(dayCounter) + "//tr[@class=\"service\"][" + String.valueOf(serviceCounter) + "]"
                        + ProgrammSection.GetSumForUnit(1))).scrollTo();
                programFor15 = programFor15 +
                        Double.valueOf($(By.xpath(ProgrammSection.GetADay(dayCounter) + "//tr[@class=\"service\"][" + String.valueOf(serviceCounter) + "]"
                                + ProgrammSection.GetSumForUnit(1))).getText());

                programFor20 = programFor20 +
                        Double.valueOf($(By.xpath(ProgrammSection.GetADay(dayCounter) + "//tr[@class=\"service\"][" + String.valueOf(serviceCounter) + "]"
                                + ProgrammSection.GetSumForUnit(2))).getText());

                programFor25 = programFor25 +
                        Double.valueOf($(By.xpath(ProgrammSection.GetADay(dayCounter) + "//tr[@class=\"service\"][" + String.valueOf(serviceCounter) + "]"
                                + ProgrammSection.GetSumForUnit(3))).getText());

            }

            $(By.xpath(ProgrammSection.GetADay(dayCounter) + "//tfoot//a[@class=\"qbtn qbtn-hideallprices\"]")).click();
        }
        //System.out.println(programFor15 + " " + programFor20 + " " + programFor25);


        //Запускаем Расчёт
        $(By.id("qbtn-execute")).scrollTo();
        $(By.id("qbtn-execute")).click();
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);

        //Сравниваем цену за номер
        /*Assert.assertEquals(new BigDecimal(priceDBLD).setScale(0, RoundingMode.HALF_UP).floatValue(),
                );*/
        $(By.id("table-result-hotels-wo-margin-we")).scrollTo();

        String hotelsWE15womS = $(By.cssSelector("table[id=\"table-result-hotels-wo-margin-we\"] tbody tr td")).getText();
        hotelsWE15womS = hotelsWE15womS.substring(0, hotelsWE15womS.indexOf(' '));
        System.out.println("hotelsWE 15 w/o marge: " + hotelsWE15womS);
        String priceDBLDS = String.valueOf((int) new BigDecimal(priceDBLD).setScale(0, RoundingMode.HALF_UP).floatValue());
        //Assert.assertEquals(priceDBLDS, hotelsWE15womS);
        if(priceDBLDS.equals(hotelsWE15womS)) {
            System.out.println("Таблица Hotels (WE) w/o margin содержит верные значения");
        }
        else System.out.println("Таблица Hotels (WE) w/o margin содержит неверные значения: "
                + priceDBLDS + " не равен " + hotelsWE15womS + "+");


        Double hotelsWE15wom = 0.0;
        hotelsWE15wom = priceDBLD;
        System.out.println("Hotels WE w/om 15: " + (new BigDecimal(hotelsWE15wom).setScale(0, RoundingMode.HALF_UP).floatValue()));



        Double hotelsWE15 = 0.0;
        hotelsWE15 = priceDBLD;
        hotelsWE15 = hotelsWE15 / rubUsd;
        hotelsWE15 = hotelsWE15 / generalMarge;
        System.out.println("Hotels WE 15: " + (new BigDecimal(hotelsWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));


        Double services15 = 0.0;
        services15 = programFor15;
        services15 = services15 / rubUsd;
        services15 = services15 / generalMarge;
        System.out.println("Services WE w/om 15: " + (new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));


        Double totalWE15 = 0.0;
        totalWE15 = priceDBLD + programFor15;
        totalWE15 = totalWE15 / rubUsd;
        totalWE15 = totalWE15 / generalMarge;
        System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));

        Double totalWE20 = 0.0;
        totalWE20 = priceDBLD + programFor20;
        totalWE20 = totalWE20 / rubUsd;
        totalWE20 = totalWE20 / generalMarge;
        System.out.println("Total WE 20: " + new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue());

        Double totalWE25 = 0.0;
        totalWE25 = priceDBLD + programFor20;
        totalWE25 = totalWE25 / rubUsd;
        totalWE25 = totalWE25 / generalMarge;
        System.out.println("Total WE 35: " + new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue());


    }

   @After
    public void close() {

       driver.quit();
    }

}
