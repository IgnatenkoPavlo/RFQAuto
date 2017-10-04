package com.rfqDemoOltatravel;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;

public class CommonCode {

    public void WaitForProgruzka() {

        System.out.print("[-] Ждём прогрузку...");
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(" - Готово");
    }

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
            Alert alert = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.alertIsPresent());
            result = driver.switchTo().alert().getText();
            driver.switchTo().alert().dismiss();
        } catch (NoAlertPresentException e) {
            result = "none";
        }

        return result;
    }

}
