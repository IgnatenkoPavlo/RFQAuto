package com.rfqDemoOltatravel;

import com.codeborne.selenide.WebDriverRunner;
import org.apache.bcel.generic.RETURN;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;

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

       /* //Открываем Quotation приложение
        $(By.cssSelector("a[href=\"/application/olta.quotation\"]")).click();*/


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
        System.out.println("Текущая дата " + formatForDateNow.format(nowDate));
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
        System.out.println(priceDBL);
        Double priceDBLD = Double.valueOf(priceDBL);
        System.out.println(priceDBLD);
        //Закрываем модальное окно
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).click();


        $(By.xpath("//div[@id=\"program\"]//div[@class=\"days\"]//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).click();
        Double programFor15, programFor20, programFor25 = 0.0;



        //

        //$();

        //$(By.id("qbtn-execute")).click();


    }

   @After
    public void close() {

       driver.quit();
    }

}
