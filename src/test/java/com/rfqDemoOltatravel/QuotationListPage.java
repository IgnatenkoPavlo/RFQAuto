package com.rfqDemoOltatravel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QuotationListPage {
    private WebDriver webDriver;
    private WebDriverWait wait;

    @FindBy(id = "qbtn-create")
    WebElement qoutationCreateButton;

    public QuotationListPage(WebDriver driver) {
        this.webDriver = driver;
        wait = new WebDriverWait(webDriver, 30);

        PageFactory.initElements(webDriver, this);
    }

    public void CreateNewQuotation(String quotationName, String clientName) {

        wait.until(ExpectedConditions.visibilityOf(qoutationCreateButton));
        wait.until(ExpectedConditions.elementToBeClickable(qoutationCreateButton));

        qoutationCreateButton.click();

        webDriver.switchTo().alert().sendKeys(quotationName);
        webDriver.switchTo().alert().accept();

        webDriver.switchTo().alert().sendKeys(clientName);
        webDriver.switchTo().alert().accept();
    }
}
