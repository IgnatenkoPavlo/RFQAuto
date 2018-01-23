package com.rfqDemoOltatravel;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;

public class CommonCode {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String OK = ANSI_GREEN + " - Готово" + ANSI_RESET;

    public static final String overallLoadingIndicatorREG = "//span[contains(text(),'Loading')]";

    public static void WaitForProgruzka() {

        System.out.print("[-] Ждём прогрузку...");
        $(By.xpath(overallLoadingIndicatorREG)).shouldNot(exist);
        System.out.println(OK);
    }

    public static void WaitForProgruzkaSilent() {$(By.xpath(overallLoadingIndicatorREG)).shouldNot(exist); }

    public void WaitForPageToLoad(ChromeDriver driver) {
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

    public String GetJSErrorText(ChromeDriver driver) {
        String result;

        try {
            Alert alert = (new WebDriverWait(driver, 4))
                    .until(ExpectedConditions.alertIsPresent());
            result = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
        } catch (TimeoutException e) {
            result = "none";
            //System.err.println("Проверка не отработала");
        }

        return result;
    }

}
