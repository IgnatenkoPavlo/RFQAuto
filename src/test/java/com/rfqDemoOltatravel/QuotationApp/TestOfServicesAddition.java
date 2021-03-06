package com.rfqDemoOltatravel.QuotationApp;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.ElementNotFound;
import com.rfqDemoOltatravel.PricesApp.PricesAppCommonCode;
import com.rfqDemoOltatravel.QuotationApp.NewQuotationPage.ProgrammSection;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.rfqDemoOltatravel.QuotationApp.NewQuotationPage.AddCityToAccomodationByName;

public class TestOfServicesAddition {

    public ChromeDriver driver;

    private SoftAssertions softAssertions;
    QuotationAppCommonCode quotationAppCommonCode = new QuotationAppCommonCode();
    boolean isWindows=false;

    @Before
    public void setUp() {

        driver = quotationAppCommonCode.InitializeChromeDriver();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void test1() {

        isWindows=false;
        if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){isWindows=true;}
        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();

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

        selenideConfig.timeout = 30000;
        System.out.print("[-] Открываем URL: " + props.getProperty("baseURL"));
        open(props.getProperty("baseURL"));
        quotationAppCommonCode.WaitForPageToLoad(driver);
        System.out.println(QuotationAppCommonCode.OK);


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue(props.getProperty("quotationAppLogin"));
        $(By.id("password")).setValue(props.getProperty("quotationAppPassword"));
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(QuotationAppCommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        quotationAppCommonCode.WaitForPageToLoad(driver);
        QuotationAppCommonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Prices приложение");
        open(props.getProperty("baseURL") + "/application/olta.prices");
        //Ждём пока загрузится страница и проподёт "Loading..."
        quotationAppCommonCode.WaitForPageToLoad(driver);
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);
        
        String errorText;
        int numberOfValues=0;

        String giudFixedPrice = "none";
        String guidHourlyPrice = "none";
        
        //Открываем Открываем сервисы Guides
        /*System.out.println("[-] Открываем сервисы Guides:");
        $(By.cssSelector("li[id=\"guides\"]")).click();
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Guides list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Guides пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            //Выбираем город - MSK
            System.out.print("        Выбираем город - MSK");
            $(By.cssSelector(PricesAppCommonCode.CitiesSelection.MSKButton)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Выбираем год - 2018
            System.out.print("        Выбираем год - 2018");
            $(By.xpath(PricesAppCommonCode.YearSelection.year2018XP)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Выбираем язык - English
            System.out.print("        Выбираем язык - English");
            $(By.cssSelector(PricesAppCommonCode.LanguageSelection.englishButton)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Сохраняем позицию для Guid с fixed price
            System.out.print("        Сохраняем позицию для Guid с fixed price ");
            $(By.xpath(PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(1))).scrollTo();
            numberOfValues = $$(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                    +"//tbody//tr")).size();
            int iterator=1;
            while(iterator<=numberOfValues & giudFixedPrice.equals("none")){
                if($(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                        + PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(iterator)
                        + "//td[5]/select")).getSelectedText().equals("Fixed")){
                    giudFixedPrice = $(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                            + PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(iterator)
                            + "//td/a")).getText();}
                iterator++;
                //System.out.println("fixed = "+giudFixedPrice);
            }
            System.out.println(PricesAppCommonCode.OK);

            //Сохраняем позицию для Guid с hourly price
            System.out.print("        Сохраняем позицию для Guid с hourly price ");
            $(By.xpath(PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(1))).scrollTo();
            numberOfValues = $$(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                    +"//tbody//tr")).size();
            iterator=1;
            while(iterator<=numberOfValues & guidHourlyPrice.equals("none")){
                if($(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                        + PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(iterator)
                        + "//td[5]/select")).getSelectedValue().equals("Hourly")){
                    guidHourlyPrice = $(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                            + PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(iterator)
                            + "//td/a")).getText();}
                iterator++;
            }
            System.out.println(PricesAppCommonCode.OK);
        }
        QuotationAppCommonCode.WaitForProgruzkaSilent();*/
        //System.out.println(PricesAppCommonCode.OK);

        //Открываем Открываем сервисы Excursions
        System.out.println("[-] Открываем сервисы Excursions:");
        String excurtionWithHeadphones = "none";
        String excurtionWithoutHeadphones = "none";
        $(By.cssSelector("li[id=\"excursions\"]")).click();
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        List<String> excurcionsValuesList = new ArrayList<>();
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Excursions list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Excursions пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            //Выбираем город - MSK
            System.out.print("        Выбираем город - MSK");
            $(By.cssSelector(PricesAppCommonCode.CitiesSelection.MSKButton)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Выбираем год - 2018
            System.out.print("        Выбираем год - 2018");
            $(By.xpath(PricesAppCommonCode.YearSelection.year2018XP)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Сохраняем позицию для Excursion с Headphones
            System.out.print("        Сохраняем позицию для Excursion с Headphones ");
            $(By.xpath(PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(1))).scrollTo();
            numberOfValues = $$(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                    +"//tbody//tr")).size();
            //System.out.println(numberOfValues);
            int iterator=1;
            String isChecked="no";
            while(iterator<=numberOfValues & isChecked.equals("no")){
                isChecked=$(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                        + PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(iterator)
                        + "//td[10]/input")).getAttribute("checked");
                //System.out.println(isChecked);
                if(isChecked != null){
                    excurtionWithHeadphones = $(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                            + PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(iterator)
                            + "//td/a")).getText();}
                else {isChecked="no";}
                iterator++;
            }
            //System.out.println(excurtionWithHeadphones);
            System.out.println(PricesAppCommonCode.OK);

            //Сохраняем позицию для Excursion без Headphones
            System.out.print("        Сохраняем позицию для Excursion без Headphones ");
            $(By.xpath(PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(1))).scrollTo();
            numberOfValues = $$(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                    +"//tbody//tr")).size();
            //System.out.println(numberOfValues);
            iterator=1;
            isChecked="yes";
            while(iterator<=numberOfValues & isChecked.equals("yes")){
                isChecked=$(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                        + PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(iterator)
                        + "//td[10]/input")).getAttribute("checked");
                //System.out.println(isChecked);
                if(isChecked == null){
                    excurtionWithoutHeadphones = $(By.xpath(PricesAppCommonCode.servicesPriceTableXP
                            + PricesAppCommonCode.ServicesPriceTable.ServiceStringByNumber(iterator)
                            + "//td/a")).getText();
                    isChecked="no";}
                else {isChecked="yes";}
                iterator++;
            }
            //System.out.println(excurtionWithoutHeadphones);
            System.out.println(PricesAppCommonCode.OK);
        }
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        //Открываем Открываем сервисы Meal
        /*System.out.println("[-] Открываем сервисы Meal:");
        $(By.cssSelector("li[id=\"meal\"]")).click();
        String mealTypeHotelType = "none";
        String mealTypeWorldKitchenType = "none";
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Meal list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Meal пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {

            //Выбираем город - MSK
            System.out.print("        Выбираем город - MSK");
            $(By.cssSelector(PricesAppCommonCode.CitiesSelection.MSKButton)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Выбираем год - 2018
            System.out.print("        Выбираем год - 2018");
            $(By.xpath(PricesAppCommonCode.YearSelection.year2018XP)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Сохраняем Meal с типом питания аналогично типам отелей
            System.out.print("        Сохраняем Meal с типом питания аналогично типам отелей ");
            mealTypeHotelType = "BREAKFAST AT THE HOTEL";
            System.out.print(" хардкод ");
            System.out.println(QuotationAppCommonCode.OK);

            //Сохраняем Meal с типом питания - кухни мира
            System.out.print("        Сохраняем Meal с типом питания - кухни мира ");
            mealTypeWorldKitchenType = "BREAKFAST AT THE RESTAURANT";
            System.out.print(" хардкод ");
            System.out.println(QuotationAppCommonCode.OK);
        }
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        //Открываем Открываем сервисы Train Tickets
        System.out.println("[-] Открываем сервисы Train Tickets:");
        $(By.cssSelector("li[id=\"train-tickets\"]")).click();
        String trainTicketsCustom = "none";
        String trainTicketsRegular = "none";
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Train Tickets list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Train Tickets пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {

            //Выбираем город - MSK
            System.out.print("        Выбираем город - MSK");
            $(By.cssSelector(PricesAppCommonCode.CitiesSelection.MSKButton)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Выбираем год - 2018
            System.out.print("        Выбираем год - 2018");
            $(By.xpath(PricesAppCommonCode.YearSelection.year2018XP)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Сохраняем Train Tickets с типом питания аналогично типам отелей
            System.out.print("        Сохраняем Train Tickets с типом Custom ");
            trainTicketsCustom = "CUSTOM";
            System.out.print(" хардкод ");
            System.out.println(QuotationAppCommonCode.OK);

            //Сохраняем Train Tickets с типом питания - кухни мира
            System.out.print("        Сохраняем Train Tickets с типом Night Train ");
            trainTicketsRegular = "NIGHT TRAIN MOSCOW - SAINT PETERSBURG";
            System.out.print(" хардкод ");
            System.out.println(QuotationAppCommonCode.OK);

        }
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        //Открываем Открываем сервисы Flight Tickets
        System.out.println("[-] Открываем сервисы Flight Tickets:");
        $(By.cssSelector("li[id=\"flight-tickets\"]")).click();
        String flightTicketsRegular = "none";
        String flightTicketsCustom = "none";
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Flight Tickets list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Flight Tickets пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {

            //Выбираем город - MSK
            System.out.print("        Выбираем город - MSK");
            $(By.cssSelector(PricesAppCommonCode.CitiesSelection.MSKButton)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Выбираем год - 2018
            System.out.print("        Выбираем год - 2018");
            $(By.xpath(PricesAppCommonCode.YearSelection.year2018XP)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Сохраняем Flight Tickets с типом питания аналогично типам отелей
            //System.out.print("[-] Сохраняем Train Tickets с типом Custom ");
            //flightTicketsCustom = "CUSTOM";
            //System.out.println("хардкод");
            //System.out.println(" - готово");

            //Сохраняем Flight Tickets с типом питания - кухни мира
            System.out.print("        Сохраняем Flight Tickets с типом FLIGHT MOSCOW ");
            flightTicketsRegular = "FLIGHT MOSCOW-SAINT PETERSBURG";
            System.out.print(" хардкод ");
            System.out.println(QuotationAppCommonCode.OK);

        }
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        //Открываем Открываем сервисы Shows
        System.out.println("[-] Открываем сервисы Shows:");
        $(By.cssSelector("li[id=\"shows\"]")).click();
        String showWithHeadphones = "none";
        String showWithoutheadphones = "none";
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Shows list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Shows пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {

            //Выбираем город - MSK
            System.out.print("        Выбираем город - MSK");
            $(By.cssSelector(PricesAppCommonCode.CitiesSelection.MSKButton)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Выбираем год - 2018
            System.out.print("        Выбираем год - 2018");
            $(By.xpath(PricesAppCommonCode.YearSelection.year2018XP)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);

            //Сохраняем позицию для Excursion без Headphones
            System.out.print("        Сохраняем позицию для Excursion без Headphones ");
            showWithoutheadphones = "BOLSHOI THEATRE (BALLET)";
            System.out.print(" хардкод ");
            System.out.println(QuotationAppCommonCode.OK);

        }
        */

        String tempValue;
        //Открываем Intercity Guides
        /*System.out.println("[-] Открываем сервисы Intercity Guides:");
        $(By.cssSelector("li[id=\"guides\"]:nth-of-type(2)")).click();
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Intercity Guides list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Intercity Guides пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {

        }
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем Открываем сервисы Wow Services
        System.out.print("[-] Открываем сервисы Wow Services:");
        $(By.cssSelector("li[id=\"wow-services\"]")).click();
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Wow Services list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Wow Services пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {

        }
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем Открываем сервисы Transfers
        System.out.println("[-] Открываем сервисы Transfers:");
        $(By.cssSelector("li[id=\"transfers\"]")).click();
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Transfers list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Transfers пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {

        }
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем Открываем сервисы Transport
        System.out.print("[-] Открываем сервисы Transport:");
        $(By.cssSelector("li[id=\"transport\"]")).click();
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Transport list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Transport пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {

        }
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем Открываем сервисы Special
        System.out.println("[-] Открываем сервисы Special:");
        $(By.cssSelector("li[id=\"custom\"]")).click();
        errorText = quotationAppCommonCode.GetJSErrorText(driver);
        if (!errorText.equals("none")){
            softAssertions.assertThat(errorText)
                    .as("Try to open Special list")
                    .isEqualTo("none");
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Список Special пуст, или ошибка при открытии - "
                    + QuotationAppCommonCode.ANSI_RESET);
        } else {

        }
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");*/


        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open(props.getProperty("baseURL") + "/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        quotationAppCommonCode.WaitForPageToLoad(driver);
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(QuotationAppCommonCode.OK);

        //Создаём новый Quotation
        NewQuotationPage.CreateQuotation("PTestQuotation1", "Тест компания");
        NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем курс Евро
        Double rubEur = 70.0;
        NewQuotationPage.OptionsTable.SetCurrencyRateForEUR(rubEur);

        //Выставляем колество ночей - 1
        int nightInOptionsCounter = 1;
        NewQuotationPage.OptionsTable.SetNumberOfNightsInOptions(nightInOptionsCounter);

        //Выставляем Present Meal Services = FB
        System.out.print("[-] Выставляем Present Meal Services = FB ");
        $(By.cssSelector(NewQuotationPage.OptionsTable.presentMealServices)).selectOptionContainingText("FB");
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Выставляем дату
        Instant nowDate = Instant.now();
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        System.out.print("[-] Добавляем новую дату: " + formatForDate.format(nowDate));
        //Кликаем на кнопку Add
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.addDateButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).click();
        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));

        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).setValue(formatForDate.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.saveDateButton)).click();
        System.out.println(QuotationAppCommonCode.OK);

        //Добавляем город
        AddCityToAccomodationByName("MSK", 1);

        Boolean temp1;
        Boolean temp2;
        String result;
        String result2;

        //Удаляем лишние Service из дня 1
        for (int i = 1; i <= 3; i++) {
            ProgrammSection.DeleteLastMainService(1, 1);
        }

        //Проверка сервиса Guid
        System.out.print("[-] Проверяем сервис: Guide");
        if(!giudFixedPrice.equals("none") | !giudFixedPrice.equals("none")){
            System.out.println();
            //Добавляем Guide
            System.out.print("[-] Добавляем сервис: Guide");
            ProgrammSection.AddServiceByName(1, 1, "Guide");

            if(!giudFixedPrice.equals("none")){
                System.out.print("    Выставляем Guide fixed price");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).selectOptionContainingText(giudFixedPrice);
                QuotationAppCommonCode.WaitForProgruzkaSilent();
                System.out.println(QuotationAppCommonCode.OK);

                //Выставляем rate - night
                System.out.print("        Выставляем rate - night");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaRateREG)).selectOptionContainingText("Night");
                QuotationAppCommonCode.WaitForProgruzkaSilent();
                System.out.println(QuotationAppCommonCode.OK);

                //Выставляем rate - day
                System.out.print("        Выставляем rate - day");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaRateREG)).selectOptionContainingText("Day");
                QuotationAppCommonCode.WaitForProgruzkaSilent();
                System.out.println(QuotationAppCommonCode.OK);

                //Проверяем что поле hours скрыто
                System.out.print("        Проверяем что поле hours скрыто");
                result="none1";
                result = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("style");
                //System.out.println("result= "+result);
                if(result.toLowerCase().indexOf("display: none")>=0){
                    System.out.println(QuotationAppCommonCode.OK);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+" Поле hours не скрыто"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that hours field is hidden for "+ giudFixedPrice)
                            .isEqualTo("none");
                }
            }

            if(!guidHourlyPrice.equals("none")){
                System.out.print("    Выставляем Guide hourly price");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).selectOptionContainingText(guidHourlyPrice);
                QuotationAppCommonCode.WaitForProgruzkaSilent();
                System.out.println(QuotationAppCommonCode.OK);

                //Выставляем rate - night
                System.out.print("        Выставляем rate - night");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaRateREG)).selectOptionContainingText("Night");
                QuotationAppCommonCode.WaitForProgruzkaSilent();
                System.out.println(QuotationAppCommonCode.OK);

                //Выставляем rate - day
                System.out.print("        Выставляем rate - day");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaRateREG)).selectOptionContainingText("Day");
                QuotationAppCommonCode.WaitForProgruzkaSilent();
                System.out.println(QuotationAppCommonCode.OK);

                //Проверяем что поле hours доступно
                System.out.print("        Проверяем что поле hours доступно");
                result="none";
                result = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("style");
                if(result.toLowerCase().indexOf("display: inline-block")>=0){
                    System.out.println(QuotationAppCommonCode.OK);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+" Поле hours скрыто"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that hours field is enabled for "+ giudFixedPrice)
                            .isEqualTo("inline-block");
                }
            }
            NewQuotationPage.ProgrammSection.DeleteLastMainService(1, 1);
            QuotationAppCommonCode.WaitForProgruzkaSilent();
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+" - Значения для Гида получить не удалось"+QuotationAppCommonCode.ANSI_RESET);}

        // excurtionWithHeadphones = "none";
        //String excurtionWithoutHeadphones = "none";

        //Проверка сервиса Excursion
        System.out.println("[-] Проверяем сервис: Excursion");
        if(!excurtionWithHeadphones.equals("none") | !excurtionWithoutHeadphones.equals("none")){
            System.out.print("    Добавляем сервис: Excursion");
            ProgrammSection.AddServiceByName(1, 1, "Excursion");

            if(!excurtionWithHeadphones.equals("none")){
                System.out.print("    Выставляем Excursion c обязательным Headphones");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).selectOptionContainingText(excurtionWithHeadphones);
                QuotationAppCommonCode.WaitForProgruzkaSilent();
                System.out.println(QuotationAppCommonCode.OK);

                //Проверяем что добавились автосервисы
                System.out.print("        Считаем колличество автосервисов:");
                result = String.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + "//table[@class=\"services\"]/tbody[@class=\"auto\"]/tr")).size());
                //System.out.println(result);
                if(result.equals("3")){
                    System.out.println(QuotationAppCommonCode.OK);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+" Колличество автосервисов не верно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check number of autoservices of excursion with headphones")
                            .isEqualTo("3");
                }
                //Проверяем что количество часов в автотсервисах соответствует Duration экскурси
                System.out.println("        Проверяем что количество часов в автотсервисах соответствует Duration экскурси:");
                result = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).getValue();

                result2 = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaDurationREG)).getValue();
                String autoServiceName = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                //System.out.println("res1 "+result+" res2 "+result2);
                if(result2.equals(result)){
                    System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            Часы для "+autoServiceName+" подгрузились корректно"+QuotationAppCommonCode.ANSI_RESET);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+"            Часы для "+autoServiceName+" подгрузились некорректно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result2)
                            .as("Check number of hours of "+autoServiceName+" is equal to main service")
                            .isEqualTo(result);
                }

                result2 = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(2)
                        + ProgrammSection.serviceCriteriaDurationREG)).getValue();
                autoServiceName = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(2)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                //System.out.println("res1 "+result+" res2 "+result2);
                if(result2.equals(result)){
                    System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            Часы для "+autoServiceName+" подгрузились корректно"+QuotationAppCommonCode.ANSI_RESET);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+"            Часы для "+autoServiceName+" подгрузились некорректно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result2)
                            .as("Check number of hours of "+autoServiceName+" is equal to main service")
                            .isEqualTo(result);
                }

                //Проверяем что при изменении Duration меняется hours у guid и transport
                System.out.println("        Проверяем что при изменении Duration меняется hours у guid и transport:");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).sendKeys("3\n");
                QuotationAppCommonCode.WaitForProgruzkaSilent();

                result = "13";
                result2 = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaDurationREG)).getValue();
                autoServiceName = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                //System.out.println("res1 "+result+" res2 "+result2);
                if(result2.equals(result)){
                    System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            Часы для "+autoServiceName+" подгрузились корректно"+QuotationAppCommonCode.ANSI_RESET);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+"            Часы для "+autoServiceName+" подгрузились некорректно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result2)
                            .as("Check number of hours of "+autoServiceName+" is equal to main service")
                            .isEqualTo(result);
                }

                result2 = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(3)
                        + ProgrammSection.serviceCriteriaDurationREG)).getValue();
                autoServiceName = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(3)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                //System.out.println("res1 "+result+" res2 "+result2);
                if(result2.equals(result)){
                    System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            Часы для "+autoServiceName+" подгрузились корректно"+QuotationAppCommonCode.ANSI_RESET);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+"            Часы для "+autoServiceName+" подгрузились некорректно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result2)
                            .as("Check number of hours of "+autoServiceName+" is equal to main service")
                            .isEqualTo(result);
                }

                //Проверяем что автосервисы можно отключить
                System.out.println("        Проверяем что автосервисы можно отключить:");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.mainServiceGuideCheckbox)).click();
                QuotationAppCommonCode.WaitForProgruzkaSilent();
                //System.out.println(QuotationAppCommonCode.OK);
                result = String.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + "//table[@class=\"services\"]/tbody[@class=\"auto\"]/tr")).size());
                //System.out.println(result);
                if(result.equals("2")){
                    System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            - Guide отключен"+QuotationAppCommonCode.ANSI_RESET);

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                            + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                            + ProgrammSection.mainServiceTransportCheckbox)).click();
                    QuotationAppCommonCode.WaitForProgruzkaSilent();
                    result = String.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                            + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                            + "//table[@class=\"services\"]/tbody[@class=\"auto\"]/tr")).size());
                    if(result.equals("1")){
                        System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            - Transport отключен"+QuotationAppCommonCode.ANSI_RESET);

                        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                                + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                                + ProgrammSection.mainServiceHeadphonesCheckbox)).click();
                        QuotationAppCommonCode.WaitForProgruzkaSilent();
                        result = String.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                                + "//table[@class=\"services\"]/tbody[@class=\"auto\"]/tr")).size());
                        if(result.equals("0")){
                            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            - Headphones отключен"+QuotationAppCommonCode.ANSI_RESET);}
                        else{
                            System.out.println(QuotationAppCommonCode.ANSI_RED+"            - Автосервисы не отключаются корректно"+QuotationAppCommonCode.ANSI_RESET);
                            softAssertions.assertThat(result)
                                    .as("Check number of autoservices of excursion with headphones, try to uncheck Headphones")
                                    .isEqualTo("0");
                        }
                    }
                    else{
                        System.out.println(QuotationAppCommonCode.ANSI_RED+"            - Автосервисы не отключаются корректно"+QuotationAppCommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check number of autoservices of excursion with headphones, try to uncheck Transport")
                                .isEqualTo("1");
                    }
                }
                else{
                    System.out.println(QuotationAppCommonCode.ANSI_RED+"            - Автосервисы не отключаются корректно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check number of autoservices of excursion with headphones, try to uncheck Guide")
                            .isEqualTo("2");
                }

                NewQuotationPage.ProgrammSection.DeleteLastMainService(1, 1);
                QuotationAppCommonCode.WaitForProgruzkaSilent();

            }

            if(!excurtionWithoutHeadphones.equals("none")){
                System.out.print("    Выставляем Excursion c обязательным Headphones");
                ProgrammSection.AddServiceByName(1, 1, "Excursion");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).selectOptionContainingText(excurtionWithoutHeadphones);
                QuotationAppCommonCode.WaitForProgruzkaSilent();
                System.out.println(QuotationAppCommonCode.OK);

                //Проверяем что добавились автосервисы
                System.out.print("        Считаем колличество автосервисов:");
                result = String.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + "//table[@class=\"services\"]/tbody[@class=\"auto\"]/tr")).size());
                //System.out.println(result);
                if(result.equals("2")){
                    System.out.println(QuotationAppCommonCode.OK);}
                else{
                    System.out.println(QuotationAppCommonCode.ANSI_RED+" Колличество автосервисов не верно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check number of autoservices of excursion without headphones"+ giudFixedPrice)
                            .isEqualTo("2");
                }

                //Проверяем что количество часов в автотсервисах соответствует Duration экскурси
                System.out.println("        Проверяем что количество часов в автотсервисах соответствует Duration экскурси:");
                result = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).getValue();

                result2 = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaDurationREG)).getValue();
                String autoServiceName = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                //System.out.println("res1 "+result+" res2 "+result2);
                if(result2.equals(result)){
                    System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            Часы для "+autoServiceName+" подгрузились корректно"+QuotationAppCommonCode.ANSI_RESET);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+"            Часы для "+autoServiceName+" подгрузились некорректно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result2)
                            .as("Check number of hours of "+autoServiceName+" is equal to main service")
                            .isEqualTo(result);
                }

                result2 = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(2)
                        + ProgrammSection.serviceCriteriaDurationREG)).getValue();
                autoServiceName = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(2)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                //System.out.println("res1 "+result+" res2 "+result2);
                if(result2.equals(result)){
                    System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            Часы для "+autoServiceName+" подгрузились корректно"+QuotationAppCommonCode.ANSI_RESET);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+"            Часы для "+autoServiceName+" подгрузились некорректно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result2)
                            .as("Check number of hours of "+autoServiceName+" is equal to main service")
                            .isEqualTo(result);
                }

                //Ролверяем что при изменении Duration меняется hours у guid и transport
                System.out.println("        Проверяем что при изменении Duration меняется hours у guid и transport:");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).sendKeys("3\n");
                QuotationAppCommonCode.WaitForProgruzkaSilent();

                result = "13";
                result2 = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaDurationREG)).getValue();
                autoServiceName = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                //System.out.println("res1 "+result+" res2 "+result2);
                if(result2.equals(result)){
                    System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            Часы для "+autoServiceName+" подгрузились корректно"+QuotationAppCommonCode.ANSI_RESET);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+"            Часы для "+autoServiceName+" подгрузились некорректно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result2)
                            .as("Check number of hours of "+autoServiceName+" is equal to main service")
                            .isEqualTo(result);
                }

                result2 = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(3)
                        + ProgrammSection.serviceCriteriaDurationREG)).getValue();
                autoServiceName = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(3)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                //System.out.println("res1 "+result+" res2 "+result2);
                if(result2.equals(result)){
                    System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            Часы для "+autoServiceName+" подгрузились корректно"+QuotationAppCommonCode.ANSI_RESET);}
                else{
                    System.out.print(QuotationAppCommonCode.ANSI_RED+"            Часы для "+autoServiceName+" подгрузились некорректно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result2)
                            .as("Check number of hours of "+autoServiceName+" is equal to main service")
                            .isEqualTo(result);
                }

                //Проверяем что автосервисы можно отключить
                System.out.println("        Проверяем что автосервисы можно отключить:");
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.mainServiceGuideCheckbox)).click();
                QuotationAppCommonCode.WaitForProgruzkaSilent();
                //System.out.println(QuotationAppCommonCode.OK);
                result = String.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                        + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                        + "//table[@class=\"services\"]/tbody[@class=\"auto\"]/tr")).size());
                //System.out.println(result);
                if(result.equals("1")){
                    System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            - Guide отключен"+QuotationAppCommonCode.ANSI_RESET);

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                            + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                            + ProgrammSection.mainServiceTransportCheckbox)).click();
                    QuotationAppCommonCode.WaitForProgruzkaSilent();
                    result = String.valueOf($$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                            + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                            + "//table[@class=\"services\"]/tbody[@class=\"auto\"]/tr")).size());
                    if(result.equals("0")){
                        System.out.println(QuotationAppCommonCode.ANSI_GREEN+"            - Transport отключен"+QuotationAppCommonCode.ANSI_RESET);
                    }
                    else{
                        System.out.println(QuotationAppCommonCode.ANSI_RED+"            - Автосервисы не отключаются корректно"+QuotationAppCommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check number of autoservices of excursion with headphones, try to uncheck Transport")
                                .isEqualTo("0");
                    }
                }
                else{
                    System.out.println(QuotationAppCommonCode.ANSI_RED+"            - Автосервисы не отключаются корректно"+QuotationAppCommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check number of autoservices of excursion with headphones, try to uncheck Guide")
                            .isEqualTo("1");
                }

            }

            NewQuotationPage.ProgrammSection.DeleteLastMainService(1, 1);
            QuotationAppCommonCode.WaitForProgruzkaSilent();
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+" - Значения для Экскурсии получить не удалось"+QuotationAppCommonCode.ANSI_RESET);}

    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
