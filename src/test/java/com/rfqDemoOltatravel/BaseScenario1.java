package com.rfqDemoOltatravel;

import com.codeborne.selenide.WebDriverRunner;
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

        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);

        //Добавляем новую дату, дата берётся "сегодня"
        $(By.cssSelector("table[id=\"table-dates\"] a[class=\"qbtn qbtn-add\"]")).click();
        $(By.cssSelector("table[id=\"table-dates\"] input[class=\"input-date hasDatepicker\"]")).click();
        Date nowDate = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println("Текущая дата " + formatForDateNow.format(nowDate));
        $(By.cssSelector("table[id=\"table-dates\"] input[class=\"input-date hasDatepicker\"]")).setValue(formatForDateNow.format(nowDate));
        $(By.cssSelector("table[id=\"table-dates\"] a[class=\"qbtn qbtn-save\"]")).click();

        //$$(By.cssSelector("table[id=\"table-groups\"]"));

        //Добавляем новый Город
        $(By.cssSelector("table[id=\"table-accommodations\"] a[class=\"qbtn qbtn-add\"]")).click();
        $(By.cssSelector("div[id=\"modal-dialog\"]")).isDisplayed();
        $(By.cssSelector("div[id=\"modal-cityselector\"] button[class=\"btn btn-default\"]")).isDisplayed();
        $(By.cssSelector("div[id=\"modal-cityselector\"] button[class=\"btn btn-default\"]")).shouldHave(text("MSK")).click();
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);


        //Считаем суммы для проверки
        $(By.cssSelector("table[id=\"table-accommodations\"] a[class=\"qbtn qbtn-showallprices\"]")).click();
        String priceSGL = "";
        priceSGL = $(By.cssSelector("table[id=\"table-accommodations\"] td[class=\"editable editable-accommodation-date-price-sgl priceSgl\"]")).getText();
        System.out.println(priceSGL);

        String priceDBL = "";
        priceDBL = $(By.cssSelector("table[id=\"table-accommodations\"] td[class=\"editable editable-accommodation-date-price-dbl priceDbl\"]")).getText();
        System.out.println(priceDBL);

        //$();

        //$(By.id("qbtn-execute")).click();


    }

   @After
    public void close() {

       driver.quit();
    }

}
