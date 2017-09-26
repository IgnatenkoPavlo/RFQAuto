package com.rfqDemoOltatravel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class NewQuotationPage {

    private WebDriver webDriver;
    private WebDriverWait wait;

    @FindBy(css = "div[id=\"quotation\"] button[id=\"qbtn-duplicate\"]")
    WebElement quotationDuplicateButton;

    @FindBy(css = "table[id=\"table-dates\"] a[class=\"qbtn qbtn-add\"]")
    WebElement datesPeriodsAddButton;

    @FindBy(css = "table[id=\"table-dates\"] input[class=\"input-date hasDatepicker\"]")
    WebElement datesPeriodsInputField;

    @FindBy(css = "table[id=\"table-dates\"] a[class=\"qbtn qbtn-save\"]")
    WebElement datesPeriodsSaveButton;

    public NewQuotationPage(WebDriver driver){

        webDriver = driver;
        wait = new WebDriverWait(webDriver, 30);

        PageFactory.initElements(webDriver, this);

    }

    public void SetAddNewDates(String dateForQuotation) {

        wait.until(ExpectedConditions.visibilityOf(datesPeriodsAddButton));
        wait.until(ExpectedConditions.elementToBeClickable(datesPeriodsAddButton));

        datesPeriodsAddButton.click();

        wait.until(ExpectedConditions.visibilityOf(datesPeriodsInputField));
        wait.until(ExpectedConditions.elementToBeClickable(datesPeriodsInputField));

        datesPeriodsInputField.click();
        datesPeriodsInputField.clear();
        datesPeriodsInputField.sendKeys(dateForQuotation);
        datesPeriodsInputField.click();

        datesPeriodsSaveButton.click();

    }
}
