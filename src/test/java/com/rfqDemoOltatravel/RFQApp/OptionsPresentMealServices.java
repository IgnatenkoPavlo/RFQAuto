package com.rfqDemoOltatravel.RFQApp;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static com.rfqDemoOltatravel.RFQApp.NewQuotationPage.*;

public class OptionsPresentMealServices {

    public ChromeDriver driver;

    RFQAppCommonCode rfqAppCommonCode = new RFQAppCommonCode();
    private SoftAssertions softAssertions;
    boolean isWindows=false;

    @Before
    public void setUp() {

        driver = rfqAppCommonCode.InitializeChromeDriver();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void test1(){

        isWindows=false;
        if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){isWindows=true;}
        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;

        String propertiesPath;
        if(isWindows){propertiesPath="target\\test-classes\\application.properties";}
        else{propertiesPath="target//test-classes//application.properties";}
        Properties props=new Properties();
        try {
            props.load(new InputStreamReader(new FileInputStream(propertiesPath), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Получил проперти ");
        System.out.println(props.getProperty("baseURL"));
        System.out.print("[-] Открываем URL: "+props.getProperty("baseURL")+"/application/rfq.rfq");
        open(props.getProperty("baseURL")+"/application/rfq.rfq");
        rfqAppCommonCode.WaitForPageToLoad(driver);
        System.out.println(RFQAppCommonCode.OK);

        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue(props.getProperty("rfqAppLogin"));
        $(By.id("password")).setValue(props.getProperty("rfqAppPassword"));
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(RFQAppCommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        rfqAppCommonCode.WaitForPageToLoad(driver);
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println("[-] страница загружена - URL: " + url());

        //Создаём новый Quotation
        System.out.print("[-] Создаём новый Quotation, ID = ");
        $(By.cssSelector(QuotationListPage.newQuotationButton)).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();

        //Получаем Id новой квотации
        String newQuotationID = $(By.cssSelector(quotationId)).getText();
        newQuotationID = newQuotationID.substring(1, newQuotationID.length());
        System.out.println(RFQAppCommonCode.ANSI_GREEN+newQuotationID+RFQAppCommonCode.ANSI_RESET);

        //Выставляем имя клиента
        /*System.out.print("[-] Выставляем имя клиента, как "+"Test Client: ");
        $(By.cssSelector(NewQuotationPage.clientName)).click();
        $(By.cssSelector(NewQuotationPage.chooseClientNamePopup)).shouldBe(Condition.visible);
        //$(By.cssSelector(NewQuotationPage.ChooseClientNamePopup.searchField)).sendKeys("Test Client\n");
        $(By.cssSelector(NewQuotationPage.chooseClientNamePopup
                + " div[class=\"check-list scroll-pane\"] div[class=\"jspContainer\"] div[class=\"jspPane\"]"
                + " div[group-value=\"T\"] div[class=\"check-wrap\"] span")).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);*/

        //Выставляем Nights
        System.out.print("[-] Выставляем 1 ночь: ");
        $(By.xpath(Options.nightsButtonXP)).click();
        $(By.xpath(Options.nightsInputXP)).shouldBe(Condition.visible);
        $(By.xpath(Options.nightsInputXP)).setValue("1");
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        System.out.print("[-] Выставляем Preset Meal Services - FB: ");
        $(By.cssSelector(Options.presentMealServicesButton)).click();
        $(By.cssSelector(Options.presentMealServicesButton)).click();
        $(By.cssSelector(Options.presentMealServicesSelectors)).shouldBe(Condition.visible);
        $(By.cssSelector(Options.presentMealServiceFullBoard)).click();
        //$(By.cssSelector(NewQuotationPage.Options.presentMealServiceNO)).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Выбираем Present Menu
        System.out.print("[-] Выставляем Preset Menu - Indian: ");
        $(By.cssSelector(Options.presentMealServicesButton)).scrollTo().click();
        $(By.cssSelector(Options.presentMealServicesButton)).hover().click();
        $(By.cssSelector(Options.presentMealServicesSelectors)).shouldBe(Condition.visible);
        $(By.cssSelector(Options.presentMealServiceFullBoard)).hover().click();
        //$(By.cssSelector(NewQuotationPage.Options.presentMealServiceNO)).click();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Заполняем даты
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        LocalDate nowDate = LocalDate.now();
        System.out.print("[-] Заполняем дату From: " +formatForDate.format(nowDate)+ " ");
        //Кликаем на поле для ввода даты
        $(By.cssSelector(DatesPeriods.firstIntervalFromInput)).click();

        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(DatesPeriods.firstIntervalFromInput)).setValue(formatForDate.format(nowDate)).pressEnter();
        RFQAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(RFQAppCommonCode.OK);

        //Проверяем, что типы питания выставляются корректно и происходит автогенерация сервисов
        System.out.println("[-] Проверяем, что типы питания выставляются корректно и происходит автогенерация сервисов:");
        String[] presentMealServices = {"BB", "HB", "NO", "FB"};
        String errorText;
        //boolean additionIsCorrect;
        for(int i=0; i<presentMealServices.length; i++){

            //additionIsCorrect=true;
            errorText = "none";
            //Выбираем Present Meal Service
            System.out.println("[-] Выставляем Preset Meal Services - "+presentMealServices[i]+": ");
            $(By.cssSelector(Options.presentMealServicesButton)).scrollTo().click();
            $(By.cssSelector(Options.presentMealServicesButton)).click();
            $(By.cssSelector(Options.presentMealServicesSelectors)).shouldBe(Condition.visible);
            $(By.cssSelector(Options.presentMealServicesSelectors+ " div[data-value=\""+presentMealServices[i]+"\"]")).click();
            //$(By.cssSelector(NewQuotationPage.Options.presentMealServiceNO)).click();
            RFQAppCommonCode.WaitForProgruzkaSilent();
            //System.out.println(RFQAppCommonCode.OK);


            //Выбираем Москву
            //System.out.print("[-] Добавляем размещение - Москва: ");
            $(By.xpath(Accommodations.addCityToLastPossiblePositionControlXP)).scrollTo().click();
            //$(By.cssSelector(NewQuotationPage.Accommodations.cityAddButton)).click();
            RFQAppCommonCode.WaitForProgruzkaSilent();
            $(By.xpath(accommodationsAreaXP+"//div[@class=\"info-row empty-accommodation\"]//div[@class=\"check-wrapper city-selector\"]")).shouldBe(Condition.visible);
            //$(By.cssSelector(NewQuotationPage.Accommodations.cityList)).shouldBe(Condition.visible);
            $(By.xpath(accommodationsAreaXP+"//div[@class=\"info-row empty-accommodation\"]//div[@class=\"check-wrapper city-selector\"]/div/div/div/div/div")).click();
            //$(By.cssSelector(NewQuotationPage.Accommodations.moscowButton)).click();
            errorText = rfqAppCommonCode.GetJSErrorText(driver);
            //System.out.println(errorText);
            RFQAppCommonCode.WaitForProgruzkaSilent();

            if(errorText.equals("none")){
                System.out.println(RFQAppCommonCode.ANSI_GREEN+"      Базовая генерация прошла + "+RFQAppCommonCode.ANSI_RESET);

                int servicesCount1=0;
                int servicesCount2=0;
                servicesCount1 = $$(By.xpath(Itinerary.DayCityByNumberXP(1, 1)
                        +"//div[2]//div[@class=\"dayCityServices ui-sortable\"]/div")).size();
                //System.out.println(servicesCount1);
                servicesCount2 = $$(By.xpath(Itinerary.DayCityByNumberXP(2, 1)
                        +"//div[2]//div[@class=\"dayCityServices ui-sortable\"]/div")).size();
                //System.out.println(servicesCount2);

                int servicesCountMust1 = 0;
                int servicesCountMust2 = 0;

                if(presentMealServices[i].equals("NO")){
                    //Проверить что питание не добавилось
                    servicesCountMust1 = 0;
                    servicesCountMust2 = 0;
                }

                if(presentMealServices[i].equals("BB")){
                    //Проверить что добавились BREAKFAST + LUNCH
                    servicesCountMust1 = 1;
                    servicesCountMust2 = 2;
                }

                if(presentMealServices[i].equals("HB")){
                    //Проверить что добавились BREAKFAST + DINNER
                    servicesCountMust1 = 1;
                    servicesCountMust2 = 2;
                }

                if(presentMealServices[i].equals("FB")){
                    //Проверить что добавились BREAKFAST + LUNCH + DINNER
                    servicesCountMust1 = 2;
                    servicesCountMust2 = 3;
                }

                //Проверяем что добавились нужные автосервисы
                //System.out.println("Из Prices получили:"+priceDBLDS15+" в Totals:"+ result);
                if ((servicesCount1-2) == servicesCountMust1){
                    System.out.println(RFQAppCommonCode.ANSI_GREEN+"      Колличество сервисов питания в первом дне верное + "+RFQAppCommonCode.ANSI_RESET);
                } else {
                    softAssertions.assertThat((servicesCount1-2))
                            .as("Check that value of meal services in day 1 is correct")
                            .isEqualTo(servicesCountMust1);
                    System.out.println(RFQAppCommonCode.ANSI_RED +"      Колличество сервисов питания в первом дне неверное: " + RFQAppCommonCode.ANSI_RESET
                            + (servicesCount1-2) + " -");
                }

                if ((servicesCount2-2) == servicesCountMust2){
                    System.out.println(RFQAppCommonCode.ANSI_GREEN+"      Колличество сервисов питания во втором дне верное + "+RFQAppCommonCode.ANSI_RESET);
                } else {
                    softAssertions.assertThat((servicesCount2-2))
                            .as("Check that value of meal services in day 2 is correct")
                            .isEqualTo(servicesCountMust2);
                    System.out.println(RFQAppCommonCode.ANSI_RED +"      Колличество сервисов питания во втором дне неверное: " + RFQAppCommonCode.ANSI_RESET
                            + (servicesCount2-2) + " -");
                }

                //Удаляем город из Accomodations
                $(By.xpath(Accommodations.CityByNumberXP(1))).scrollTo().hover();
                $(By.xpath(Accommodations.CityByNumberXP(1)+"//div[@class=\"delete-btn\"]")).hover().click();
                confirm();
                RFQAppCommonCode.WaitForProgruzkaSilent();

            }
            else{
                softAssertions.assertThat(errorText)
                        .as("Try to use "+ presentMealServices[i] +" for quotation generation: ")
                        .isEqualTo("Ok");
                System.out.println(RFQAppCommonCode.ANSI_RED +"      Добавление: " + presentMealServices[i] + " упало -"+ RFQAppCommonCode.ANSI_RESET);
                RFQAppCommonCode.WaitForProgruzkaSilent();
            }



        }


    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
