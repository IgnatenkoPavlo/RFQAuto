package com.rfqDemoOltatravel;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
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

    public static final String overalLoadingIndicatorREG = "//span[contains(text(),'Loading')]";

    public void WaitForProgruzka() {

        System.out.print("[-] Ждём прогрузку...");
        $(By.xpath(overalLoadingIndicatorREG)).shouldNot(exist);
        System.out.println(" - Готово");
    }

    public void WaitForProgruzkaSilent() {$(By.xpath(overalLoadingIndicatorREG)).shouldNot(exist); }

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

    public void GetSumsForServices(double... programServicesGroups) {

        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        int dayCounterMax = $$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size();
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            System.out.print("      - считаем для дня номер "+ dayCounter);

            //Считаем для Services
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)+"//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++){
                System.out.print("         - считаем для города номер "+ cityCounter);
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

                int serviceCounterMax= $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) +
                        NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= serviceCounterMax; serviceCounter++) {

                    for (int serviceSumCounter = 0; serviceSumCounter < programServicesGroups.length; serviceSumCounter++) {
                        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(serviceSumCounter+1))).scrollTo();

                        programServicesGroups[serviceSumCounter] = programServicesGroups[serviceSumCounter] +
                                Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                        + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(serviceSumCounter+1))).getText());

                    }
                }

                int autoServiceCounterMax= $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) +
                        NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"auto\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= autoServiceCounterMax; serviceCounter++) {

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(serviceCounter+1))).scrollTo();

                    for (int serviceSumCounter = 0; serviceSumCounter < programServicesGroups.length; serviceSumCounter++) {
                        programServicesGroups[serviceSumCounter] = programServicesGroups[serviceSumCounter] +
                                Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                                        + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(serviceSumCounter+1))).getText());

                    }
                }

                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//table[@class=\"services\"]//tfoot//a[@class=\"qbtn qbtn-hideallprices\"]")).scrollTo().click();

            }

        }

    }

    public void GetSumsForDailyServices(double... programDailyServicesGroups) {

        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        int dayCounterMax = $$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size();
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            System.out.print("      - считаем для дня номер "+ dayCounter);

            //Считаем для Daily Services
            $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)+ NewQuotationPage.ProgrammSection.showAllDailyPricesREG)).scrollTo().click();
            if(($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                    + NewQuotationPage.ProgrammSection.guideFromMoscowREG)).isSelected())
                    || ($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                    + NewQuotationPage.ProgrammSection.goldenRingGuide)).isSelected())
                    || ($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                    + NewQuotationPage.ProgrammSection.transportFromMoscowREG)).isSelected())){

                int dalyServicesMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                        +"//div[@class=\"serviceByDayInfo\"]//table[@class=\"serviceByDayTable services\"]//tbody//tr[@class=\"service_by_day\"]")).size();
                //System.out.println(dalyServicesMax+"");
                for(int dailyServiceCounter=1;dailyServiceCounter<=dalyServicesMax;dailyServiceCounter++){

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                            + NewQuotationPage.ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                            + NewQuotationPage.ProgrammSection.GetDailySumForPeopleREG(1))).scrollTo();
                    for(int serviceSumCounter = 0; serviceSumCounter < programDailyServicesGroups.length; serviceSumCounter++) {
                        programDailyServicesGroups[serviceSumCounter] = programDailyServicesGroups[serviceSumCounter] +
                                Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                                        + NewQuotationPage.ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                                        + NewQuotationPage.ProgrammSection.GetDailySumForPeopleREG(serviceSumCounter+1))).getText());
                    }

                }
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)+ NewQuotationPage.ProgrammSection.hideAllDailyPricesREG)).click();
            }


        }

    }

}
