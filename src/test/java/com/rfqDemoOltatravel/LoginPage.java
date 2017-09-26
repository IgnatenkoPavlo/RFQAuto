package com.rfqDemoOltatravel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver webDriver;
    private WebDriverWait wait;

    @FindBy(id = "username")
    WebElement loginField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy (css = "button[type=\"submit\"]")
    WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.webDriver = driver;
        wait = new WebDriverWait(webDriver, 30);

        PageFactory.initElements(webDriver, this);
    }

    public void FillLogin(String login) {
        loginField.clear();
        loginField.sendKeys(login);
    }

    public void FillPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void DoLogin() {

        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    public void DoLoginWithCreds(String login, String password) {
        FillLogin(login);
        FillPassword(password);
        DoLogin();
    }

}
