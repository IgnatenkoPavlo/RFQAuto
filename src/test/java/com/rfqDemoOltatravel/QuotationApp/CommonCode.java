package com.rfqDemoOltatravel.QuotationApp;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
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

    public static final String OK = ANSI_GREEN + " - Готово" + ANSI_RESET;

    public static final String overalLoadingIndicatorREG = "//span[contains(text(),'Loading')]";

    public static void WaitForProgruzka() {

        System.out.print("[-] Ждём прогрузку...");
        $(By.xpath(overalLoadingIndicatorREG)).shouldNot(exist);
        System.out.println(OK);
    }

    public static void WaitForProgruzkaSilent() {$(By.xpath(overalLoadingIndicatorREG)).shouldNot(exist); }

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
            System.out.println("      - считаем для дня номер "+ dayCounter);

            //Считаем для Services
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)+"//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++){
                System.out.println("         - считаем для города номер "+ cityCounter);
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
                        //System.out.println(programServicesGroups[serviceSumCounter]);

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
            System.out.println("      - считаем для дня номер "+ dayCounter);

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

    public void SetValuesForServicesInProgram(int ... values) {

        System.out.println("[-] Выставляем суммы для 3-х групп: 15, 20, 25");
        int dayCounterMax = $$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size();
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            System.out.print("      - для дня номер " + dayCounter);
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)+"//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++){

                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

                int serviceCounterMax= $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) +
                        NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= serviceCounterMax; serviceCounter++) {

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(1))).scrollTo().click();


                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(1))).setValue(String.valueOf(values[0])).pressEnter();

                    CommonCode.WaitForProgruzkaSilent();


                    if(values.length>=2)
                    {$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(2))).click();

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(2))).setValue(String.valueOf(values[1])).pressEnter();

                    CommonCode.WaitForProgruzkaSilent();}


                    if(values.length>=3)
                    {$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(3))).click();

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(3))).setValue(String.valueOf(values[2])).pressEnter();

                    CommonCode.WaitForProgruzkaSilent();}
                }

                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//tr//td//a[@class=\"qbtn qbtn-hideallprices\"]")).shouldBe(visible).click();

            }
            System.out.println(CommonCode.OK);

        }

    }


    public class PeriodsCollection{
        public LocalDate dateFrom;
        public LocalDate dateTo;
        public int priceSGL;
        public int priceDBL;
        public int priceSGLWE;
        public int priceDBLWE;

        PeriodsCollection(String ... data) {
            if(data.length>=1)
                dateFrom = LocalDate.of(Integer.valueOf(data[0].substring(6,data[0].length())), Integer.valueOf(data[0].substring(3,5)), Integer.valueOf(data[0].substring(0,2)));
            if(data.length>=2)
                dateTo = LocalDate.of(Integer.valueOf(data[1].substring(6,data[1].length())), Integer.valueOf(data[1].substring(3,5)), Integer.valueOf(data[1].substring(0,2)));
            if(data.length>=3)
                priceSGL = Integer.valueOf(data[2]);
            if(data.length>=4)
                priceDBL = Integer.valueOf(data[3]);
            if(data.length>=5)
                priceSGLWE = Integer.valueOf(data[4]);
            if(data.length>=6)
                priceDBLWE = Integer.valueOf(data[5]);
        }
    }

    public List<PeriodsCollection> SavePeriodsForACityAndHotelType(String cityName, String hotelType) {

        //Выбираем город
        System.out.print("[-] Выбираем город - "+cityName);
        //$(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-city\"] button[data-switch-value=\""+cityName+"\"]")).scrollTo().click();
        $(By.xpath("//div[@id=\"switch-city\"]//button[@data-switch-value=\""+cityName+"\"]")).click();
        WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем текущий год
        System.out.print("[-] Открываем текущий год");
        $(By.xpath("//div[@id=\"switch-year\"]//button[contains(text(),'2018')]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выбираем тип отеля
        System.out.print("[-] Выбираем "+hotelType);
        $(By.xpath("//div[@id=\"filters-bar\"]//div[@id=\"switch-hotel-type\"]//button[contains(text(),'"+hotelType+"')]")).scrollTo().click();
        WaitForProgruzkaSilent();
        System.out.println(" - готово");

        DateTimeFormatter formatForPrices = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        $(By.xpath("//div[@id=\"content\"]//center[contains(text(),'2018')]")).scrollTo();
        //Откраваем 01-01-2018
        $(By.xpath("//div[@id=\"content\"]//div[@id=\"hotel-calendar\"]//div[@data-year=\"2018\"]" +
                "//div//table//tbody//tr" +
                "//td[@data-date=\"2018-01-01\"]")).click();
        WaitForProgruzkaSilent();


        List<PeriodsCollection> result = new ArrayList<>();

        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);
        //$(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);
        //Сохраняем значения из попапа
        String dateFrom = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
        //String dateFrom = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
        String dateTo = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
        //String dateTo = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
        String priceSGL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
        //String priceSGL = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
        String priceDBL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
        //String priceDBL = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
        String priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
        //String priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
        String priceDBLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();
        //tring priceDBLWE = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();

        //Сохраняем значения в новый элемент списка
        result.add(new PeriodsCollection(dateFrom, dateTo, priceSGL, priceDBL, priceSGLWE, priceDBLWE));

        //System.out.println(dateFrom+" "+dateTo+" "+priceSGL+" "+priceDBL+" "+priceSGLWE+" "+priceDBLWE);
        //Закрываем попап
        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);
        //$(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
        //$(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);

        //Получаем дату начала следующего периода
        LocalDate dateToNext = LocalDate.of(Integer.valueOf(dateTo.substring(6,dateTo.length())), Integer.valueOf(dateTo.substring(3,5)), Integer.valueOf(dateTo.substring(0,2))).plusDays(1);

        //Проходим по всем периодам и сохраняем значения в список
        System.out.print("[-] Проходим по всем периодам и сохраняем значения в список");
        while(!dateTo.equals("31-12-2018")){
            $(By.xpath("//div[@id=\"content\"]//div[@id=\"hotel-calendar\"]//div[@data-year=\"2018\"]" +
                    "//div//table//tbody//tr" +
                    "//td[@data-date=\""+dateToNext.format(formatForPrices)+"\"]")).scrollTo().click();

            $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);
            //$(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);

            dateFrom = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
            dateTo = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
            priceSGL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
            priceDBL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
            priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
            priceDBLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();
            //System.out.println(dateFrom+" "+dateTo+" "+priceSGL+" "+priceDBL+" "+priceSGLWE+" "+priceDBLWE);

            /*dateFrom = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
            dateTo = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
            priceSGL = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
            priceDBL = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
            priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
            priceDBLWE = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();*/

            result.add(new PeriodsCollection(dateFrom, dateTo, priceSGL, priceDBL, priceSGLWE, priceDBLWE));

            $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
            $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);
            /*$(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
            $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);*/

            dateToNext = LocalDate.of(Integer.valueOf(dateTo.substring(6,dateTo.length())), Integer.valueOf(dateTo.substring(3,5)), Integer.valueOf(dateTo.substring(0,2))).plusDays(1);

        }
        System.out.println(" - готово");
        return result;
    }

}
