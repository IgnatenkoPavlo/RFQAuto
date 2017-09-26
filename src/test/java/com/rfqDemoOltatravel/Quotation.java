package com.rfqDemoOltatravel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverRunner.*;
import static java.lang.Thread.*;

public class Quotation {

    public ChromeDriver driver;

    public void waitForPageToLoad() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

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
        System.out.println("[-] ждали открытия страницы: " + totalTime + " сек., кол-во повторений: " + numberOfIterations);
    }

    LoginPage loginPage;
    QuotationListPage quotationListPage;
    NewQuotationPage newQuotationPage;


    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Automation\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void quotation() throws InterruptedException {

        String temp="none";
        System.out.println(temp);

        driver.get("http://rfq-demo.oltatravel.com/");

        WebDriverWait wait = new WebDriverWait(driver, 30);

        loginPage = new LoginPage(driver);

        loginPage.DoLoginWithCreds("pavel.sales", "password");

        driver.get("http://rfq-demo.oltatravel.com/application/olta.quotation");
        waitForPageToLoad();

        quotationListPage = new QuotationListPage(driver);

        quotationListPage.CreateNewQuotation("PtestQuotation", "PtestClient");

        newQuotationPage = new NewQuotationPage(driver);
        waitForPageToLoad();

        //wait.until(ExpectedConditions.visibilityOf(newQuotationPage.datesPeriodsAddButton));

        //Thread.sleep(1000);
        //newQuotationPage.datesPeriodsAddButton.click();


        //newQuotationPage.datesPeriodsInputField.sendKeys("22-09-2017");

        newQuotationPage.SetAddNewDates("25-09-2017");

        //LocalDate localDate = LocalDate.now();
        //newQuotationPage.SetAddNewDates("22-09-2017"); //DateTimeFormatter.ofPattern("dd-MM-yyyy").format(localDate)

        /*wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt=\"Quotation\"]")));
        driver.findElement(By.cssSelector("img[alt=\"Quotation\"]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("qbtn-create"))); //

        driver.findElement(By.id("qbtn-create")).getCssValue(temp);
        System.out.println(temp);


        driver.switchTo().alert().sendKeys("PtestQuotation1");
        driver.switchTo().alert().accept();

        driver.switchTo().alert().sendKeys("PtestClient1");
        driver.switchTo().alert().accept();*/

/*// ERROR: Caught exception [ERROR: Unsupported command [answerOnNextPrompt | PTestClient | ]]
// ERROR: Caught exception [ERROR: Unsupported command [getPrompt |  | ]]
        driver.findElement(By.cssSelector("#sizzle-1505929043676 > tr > td > a.qbtn.qbtn-add")).click();
        driver.findElement(By.id("dp1505929043728")).click();
        driver.findElement(By.id("dp1505929043728")).clear();
        driver.findElement(By.id("dp1505929043728")).sendKeys("20-09-2017");
        driver.findElement(By.linkText("save")).click();
        driver.findElement(By.xpath("(//a[contains(text(),'add')])[5]")).click();
        driver.findElement(By.cssSelector("div.btn-group.btn-group-city > button.btn.btn-default")).click();
// ERROR: Caught exception [ERROR: Unsupported command [getPrompt |  | ]]*/



    }

    @After
    public void close() {

        driver.quit();
    }
}
