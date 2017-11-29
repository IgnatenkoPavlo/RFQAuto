package com.rfqDemoOltatravel;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.ElementNotFound;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.rfqDemoOltatravel.NewQuotationPage.ProgrammSection;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.xml.bind.Element;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.rfqDemoOltatravel.NewQuotationPage.AddCityToAccomodationByName;

public class TestOfServicesAddition {

    public ChromeDriver driver;

    private SoftAssertions softAssertions;
    CommonCode commonCode = new CommonCode();

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Automation\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void test1() {

        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();

        Properties props = new Properties();
        try {
            props.load(new InputStreamReader(new FileInputStream("target\\test-classes\\application.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Получил проперти ");
        System.out.println(props.getProperty("baseURL"));

        selenideConfig.timeout = 30000;
        System.out.print("[-] Открываем URL: " + props.getProperty("baseURL"));
        open(props.getProperty("baseURL"));
        commonCode.WaitForPageToLoad(driver);
        System.out.println(CommonCode.OK);


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("test");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(CommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open(props.getProperty("baseURL") + "/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(CommonCode.OK);

        //Создаём новый Quotation
        NewQuotationPage.CreateQuotation("PTestQuotation1", "Тест компания");
        NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем курс Евро
        Double rubEur = 70.0;
        NewQuotationPage.OptionsTable.SetCurrencyRateForEUR(rubEur);

        //Выставляем колество ночей - 1
        int nightInOptionsCounter = 1;
        NewQuotationPage.OptionsTable.SetNumberOfNightsInOptions(nightInOptionsCounter);

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
        System.out.println(CommonCode.OK);

        //Добавляем город
        AddCityToAccomodationByName("MSK", 1);

        Boolean temp1;
        Boolean temp2;
        String result;
        String result2;

        //Удаляем лишние Service из дня
        for (int i = 1; i <= 3; i++) {
            ProgrammSection.DeleteLastMainService(1, 1);
        }

        String[] dayNight = {"Night", "Day"};
        //Добавляем Guide
        System.out.print("[-] Добавляем сервис: Guide");
        ProgrammSection.AddServiceByName(1,1, "Guide");
        List dropDownValues = new ArrayList();
        dropDownValues.addAll(Arrays.asList("1/2 day (4 hours)","Aeroexpress", "City by night", "Day (24 hours)",
                "Full day (8 hours)", "Golden Ring", "Transfer to the airport (no excursion)",
                "Transfer to the airport + City tour", "Transfer to the dinner/theatre (no excursion)",
                "Transfer to the railway station (no excursion)"));
        temp1=true;
        for(int dropDownValuesCounter=0; dropDownValuesCounter<dropDownValues.size(); dropDownValuesCounter++){
            System.out.println("    - Пробуем выставить: "+dropDownValues.get(dropDownValuesCounter));
            temp1=true;
            try{ $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    +ProgrammSection.GetACityByNumberREG(1)
                    +ProgrammSection.GetMainServiceByNumberREG(1)
                    +"/td[@class=\"criteria\"]/select[@class=\"serviceName\"]")).scrollTo().selectOptionContainingText(String.valueOf(dropDownValues.get(dropDownValuesCounter)));
            }catch (ElementNotFound e){
                //e.printStackTrace();
                //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to add service "+dropDownValues.get(dropDownValuesCounter))
                        .isEqualTo("Yes");
                temp1=false;

                //System.out.println(e);
            }

            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                temp2=true;
                for(int i=0; i<dayNight.length;i++) {
                    System.out.print("        - Пробуем выставить: "+dayNight[i]);
                    temp2=true;
                    try {
                        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                                + ProgrammSection.GetACityByNumberREG(1)
                                + ProgrammSection.GetMainServiceByNumberREG(1)
                                + "/td[@class=\"criteria\"]/select[@class=\"rate2\"]")).scrollTo().selectOptionContainingText(dayNight[i]);
                    } catch (ElementNotFound e) {
                        //e.printStackTrace();
                        temp2=false;
                        System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dayNight[i]+CommonCode.ANSI_RESET);
                        softAssertions.assertThat("No")
                                .as("Try to add service " + dayNight[i])
                                .isEqualTo("Yes");


                        //System.out.println(e);
                    }
                    if(temp2==true){
                        CommonCode.WaitForProgruzkaSilent();
                        System.out.println(CommonCode.OK);}
                    else{
                        System.out.println(CommonCode.ANSI_RED+"    - При добавлении были ошибки."+CommonCode.ANSI_RESET);
                    }
                }

            }
            else{System.out.println(CommonCode.ANSI_RED+"    - Не смог выбрать "+dropDownValues.get(dropDownValuesCounter)+CommonCode.ANSI_RESET);}

        }

        dropDownValues.clear();
        dropDownValues.addAll(Arrays.asList("Escort for dinner/theatre", "Hourly payment"));
        temp1=true;
        for(int dropDownValuesCounter=0; dropDownValuesCounter<dropDownValues.size(); dropDownValuesCounter++){
            System.out.println("    - Пробуем выставить: "+dropDownValues.get(dropDownValuesCounter));
            temp1=true;
            try{ $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    +ProgrammSection.GetACityByNumberREG(1)
                    +ProgrammSection.GetMainServiceByNumberREG(1)
                    +"/td[@class=\"criteria\"]/select[@class=\"serviceName\"]")).scrollTo().selectOptionContainingText(String.valueOf(dropDownValues.get(dropDownValuesCounter)));
            }catch (ElementNotFound e){
                //e.printStackTrace();
                //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to add service "+dropDownValues.get(dropDownValuesCounter))
                        .isEqualTo("Yes");
                temp1=false;

                //System.out.println(e);
            }

            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                temp2=true;
                for(int i=0; i<dayNight.length;i++) {
                    System.out.print("        - Пробуем выставить: "+dayNight[i]);
                    temp2=true;
                    try {
                        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                                + ProgrammSection.GetACityByNumberREG(1)
                                + ProgrammSection.GetMainServiceByNumberREG(1)
                                + "/td[@class=\"criteria\"]/select[@class=\"rate2\"]")).scrollTo().selectOptionContainingText(dayNight[i]);
                    } catch (ElementNotFound e) {
                        //e.printStackTrace();
                        temp2=false;
                        System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dayNight[i]+CommonCode.ANSI_RESET);
                        softAssertions.assertThat("No")
                                .as("Try to add service " + dayNight[i])
                                .isEqualTo("Yes");


                        //System.out.println(e);
                    }
                    if(temp2==true){
                        CommonCode.WaitForProgruzkaSilent();
                        System.out.println(CommonCode.OK);}
                    else{
                        System.out.println(CommonCode.ANSI_RED+"    - При добавлении были ошибки."+CommonCode.ANSI_RESET);
                    }
                }

                System.out.print("        - Пробуем выставить часы в 5");
                temp2=true;
                try {
                    $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetMainServiceByNumberREG(1)
                            + "/td[@class=\"criteria\"]/input[@name=\"hours2\"]")).scrollTo().setValue("5").pressEnter();
                } catch (ElementNotFound e) {
                    //e.printStackTrace();
                    temp2=false;
                    System.out.println(CommonCode.ANSI_RED+" - Не задать часы как 5 "+CommonCode.ANSI_RESET);
                    softAssertions.assertThat("No")
                            .as("Try to add hours as 5")
                            .isEqualTo("Yes");


                    //System.out.println(e);
                }
                if(temp2==true){
                    CommonCode.WaitForProgruzkaSilent();
                    System.out.println(CommonCode.OK);}
                else{
                    System.out.println(CommonCode.ANSI_RED+"    - При добавлении были ошибки."+CommonCode.ANSI_RESET);
                }
            }
            else{System.out.println(CommonCode.ANSI_RED+"    - Не смог выбрать "+dropDownValues.get(dropDownValuesCounter)+CommonCode.ANSI_RESET);}

        }

        CommonCode.WaitForProgruzkaSilent();
        ProgrammSection.DeleteLastMainService(1,1);

        //Добавляем Excursion
        System.out.print("[-] Добавляем сервис: Excursion");
        ProgrammSection.AddServiceByName(1, 1, "Excursion");
        dropDownValues.clear();
        dropDownValues.addAll(Arrays.asList("Alexander Gardens", "Arbat Street", "Bolshoi theatre", "Bunker-42",
                "Cathedral of Christ the Saviour", "Churches of Moscow Tour", "City tour", "City tour by night",
                "Cosmonaut Museum", "Free time", "Izmailovo market", "Kolomenskoe", "Kremlin",
                "Kremlin (Armory Chamber)", "Kremlin (Diamond Fund)", "Lenin Mausoleum", "Matryoshka painting",
                "Metro tour", "Monino", "Museum of the Great Patriotic War", "National Dance Show \"Kostroma\"",
                "Novodevichy Convent", "Panoramic City tour", "Pushkin Museum of Fine Arts", "Red Square",
                "River Cruise", "River Cruise on the Radisson ice-breaker ", "Saint-Basil’s Cathedral",
                "Sergiev Posad", "Shopping", "Sparrow Hills", "Star City", "Taganka tour", "Tretyakov Gallery",
                "Tsaritsyno", "VDNKh", "Victory Park", "Visit to Vodka Museum", "Walking tour through the city center"));

        System.out.println("    - Пробуем выставить экскурсию: " + "Cathedral of Christ the Saviour");
        temp1 = true;
        try {
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText("Cathedral of Christ the Saviour");
        } catch (ElementNotFound e) {
            //e.printStackTrace();
            //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Try to add service " + "Cathedral of Christ the Saviour")
                    .isEqualTo("Yes");
            temp1 = false;

            //System.out.println(e);
        }

        if (temp1 == true) {
            CommonCode.WaitForProgruzkaSilent();
            temp2 = true;
            System.out.print("        - Пробуем выставить Duration 10");
            temp2 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + "/td[@class=\"options\"]/span/input[@name=\"duration\"]")).scrollTo().setValue("10");
                CommonCode.WaitForProgruzkaSilent();
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp2 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог Duration 10" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set Duration as 10 ")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            if (temp2 == true) {
                CommonCode.WaitForProgruzkaSilent();
                System.out.println(CommonCode.OK);
            } else {
                System.out.println(CommonCode.ANSI_RED + "    - При добавлении были ошибки." + CommonCode.ANSI_RESET);
            }

        } else {
            System.out.println(CommonCode.ANSI_RED + "    - Не смог выбрать " + "Cathedral of Christ the Saviour" + CommonCode.ANSI_RESET);
        }

        //Проверяем что добавились нужные автосервисы
        System.out.println("    - Проверяем что добавились нужные автосервисы:");

        //Проверяем что добавился Гид
        temp1=true;
        result = "none";
        try {
            result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                    ProgrammSection.GetACityByNumberREG(1) +
                    ProgrammSection.GetAutoServiceByNumberREG(1) +
                    ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
        } catch (ElementNotFound e) {
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
            softAssertions.assertThat(result)
                    .as("Check that Guide is automatically added to Excursion")
                    .isEqualTo(String.valueOf("Guide"));
        }
        if (temp1 == true) {
            if (result.equals("Guide")) {
                System.out.println(CommonCode.ANSI_GREEN + "      - Гид добавлен " + CommonCode.ANSI_RESET);

            } else {
                System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide is automatically added to Excursion")
                        .isEqualTo(String.valueOf("Transport"));
            }
        }
        //Проверяем что часы для гида выставлены в 10
        if (temp1 == true) {
            temp2 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
            } catch (ElementNotFound e) {
                System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Hourse for Guide is automatically set equally to Excursion")
                        .isEqualTo(String.valueOf("10"));
                temp2 = false;
            }
            if (temp2==true) {
                if (result.equals("10")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Гида выставлены корректно " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hourse for Guide is automatically set equally to Excursion")
                            .isEqualTo(String.valueOf("10"));
                }
            }
        }

        //Проверяем что добавился транспорт
        result = "none";
        temp1 = true;
        try {
            result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                    ProgrammSection.GetACityByNumberREG(1) +
                    ProgrammSection.GetAutoServiceByNumberREG(2) +
                    ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
        } catch (ElementNotFound e) {
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
            softAssertions.assertThat(result)
                    .as("Check that Transport is automatically added to Excursion")
                    .isEqualTo(String.valueOf("Transport"));
        }
        if (temp1 == true) {
            if (result.equals("Transport")) {
                System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт добавлен " + CommonCode.ANSI_RESET);

            } else {
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport is automatically added to Excursion")
                        .isEqualTo(String.valueOf("Transport"));
            }
        }
        //Проверяем что часы для транспорта выставлены в 10
        if (temp1 == true) {
            temp2 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(2)
                        + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
            } catch (ElementNotFound e) {
                System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Hourse for Transport is automatically set equally to Excursion")
                        .isEqualTo(String.valueOf("10"));
                temp2 = false;
            }
            if (temp2==true) {
                if (result.equals("10")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Транспорта выставлены корректно " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hourse for Transport is automatically set equally to Excursion")
                            .isEqualTo(String.valueOf("10"));
                }
            }
        }
        System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
        //Проверяем что можно отключить Гида
        temp1=true;
        try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(1)
                + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
        }
        catch(ElementNotFound e){
            e.printStackTrace();
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Гида" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Can`t find checkbox to disable Guide autoservice")
                    .isEqualTo(String.valueOf("Yes"));
        }

        if(temp1==true){
            CommonCode.WaitForProgruzkaSilent();
            result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetAutoServiceByNumberREG(1)
                    + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
            if (result.equals("Transport")){System.out.println(CommonCode.ANSI_GREEN + "      - Гида можно корректно отключить " + CommonCode.ANSI_RESET);}
            else{System.out.println(CommonCode.ANSI_RED + "      - Гид не отключился через чекбокс" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide autoservice can turned off by checkbox")
                        .isEqualTo(String.valueOf("Yes"));}
        }

        //Проверяем что можно отключить Транспорт
        temp1=true;
        try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(1)
                + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
        }
        catch(ElementNotFound e){
            e.printStackTrace();
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Can`t find checkbox to disable Guide autoservice")
                    .isEqualTo(String.valueOf("Yes"));
        }

        if(temp1==true){
            CommonCode.WaitForProgruzkaSilent();
            temp2=true;
            try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
            catch(ElementNotFound e){
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                temp2=false;
            }
            CommonCode.WaitForProgruzkaSilent();
            if (temp2==true){
                if(result.equals("0")){
                    System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                }
                else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Transport autoservice can turned off by checkbox")
                            .isEqualTo(String.valueOf("0"));}
            }
        }

        //Проверяем что можно влючить Наушники
        temp1=true;
        try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(1)
                + "/td[@class=\"options\"]/span[7]/input[@name=\"headphones\"]")).click();
            CommonCode.WaitForProgruzkaSilent();
        }
        catch(ElementNotFound e){
            e.printStackTrace();
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс включения Наушников" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Can`t find checkbox to enable Headphones autoservice")
                    .isEqualTo(String.valueOf("Yes"));
        }
        result = "none";
        if(temp1==true){
            CommonCode.WaitForProgruzkaSilent();
            try{result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();}
            catch(ElementNotFound e){
                System.out.println(CommonCode.ANSI_RED + "      - Наушники не получилось корректно включить " + CommonCode.ANSI_RESET);
            }
            if (result.equals("Special Services")){
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).getSelectedText();
                if(result.equals("Headphones")) System.out.println(CommonCode.ANSI_GREEN + "      - Наушники включились через чекбокс" + CommonCode.ANSI_RESET);
                else {
                System.out.println(CommonCode.ANSI_RED + "      - Наушники не получилось корректно включить " + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport autoservice can turned off by checkbox")
                        .isEqualTo(String.valueOf("Headphones"));}
            }
            else{
                System.out.println(CommonCode.ANSI_RED + "      - Наушники не получилось корректно включить " + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                    .as("Check that Transport autoservice can turned off by checkbox")
                    .isEqualTo(String.valueOf("Special Services"));}
        }
        //Удаляем экскурсию
        System.out.println("    - Проверяем что автосервисы автоматически удалятся после удаления основного сервиса:");
        CommonCode.WaitForProgruzkaSilent();
        ProgrammSection.DeleteLastMainService(1,1);
        //Проверяем что после удаления экскурсии удалились автосервисы
        temp2=true;
        result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());

        if(result.equals("0")){
            System.out.println(CommonCode.ANSI_GREEN + "      - Автосервисы удалены " + CommonCode.ANSI_RESET);
        }
        else{System.out.println(CommonCode.ANSI_RED + "      - Автосервисы не удалены" + CommonCode.ANSI_RESET);
            throw new IllegalArgumentException("Autoservices were not deleted automatically, there are "+result+" autoservices still there");}


        //Добавляем Meal
        System.out.print("[-] Добавляем сервис: Meal");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Meal");
        //List dropDownValues = new ArrayList();
        dropDownValues.clear();
        dropDownValues.addAll(Arrays.asList("Breakfast at the hotel","Dinner at the hotel", "Lunch at the hotel", "Lunch Box"));
        //Проверяем, что для "ресторанной еды" выставляется класс отеля
        temp1=true;
        for(int dropDownValuesCounter=0; dropDownValuesCounter<dropDownValues.size(); dropDownValuesCounter++){
            System.out.println("    - Пробуем выставить: "+dropDownValues.get(dropDownValuesCounter));
            temp1=true;
            try{ $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    +ProgrammSection.GetACityByNumberREG(1)
                    +ProgrammSection.GetMainServiceByNumberREG(1)
                    +ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText(String.valueOf(dropDownValues.get(dropDownValuesCounter)));
            }catch (ElementNotFound e){
                //e.printStackTrace();
                System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues.get(dropDownValuesCounter)+CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to add service "+dropDownValues.get(dropDownValuesCounter))
                        .isEqualTo("Yes");
                temp1=false;

                //System.out.println(e);
            }

            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                System.out.println("        - Проверяем, что класс отеля и настройки Meal совпадают: ");
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        +ProgrammSection.GetACityByNumberREG(1)
                        +ProgrammSection.GetMainServiceByNumberREG(1)
                        +ProgrammSection.serviceCriteriaRestaurantTypeREG)).getSelectedText();
                result2 = $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                            +NewQuotationPage.AccomodationsTable.hotelTypeForCityREG)).getSelectedText();
                result2=result2.substring(0, result2.indexOf('*')+1);
                if(result.equals(result2)){
                    System.out.println(CommonCode.ANSI_GREEN + "            - Изначальные настройки совпадают" + CommonCode.ANSI_RESET);
                }
                else{
                    System.out.println(CommonCode.ANSI_RED + "            - Изначальные настройки не совпадают" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that initial hotel type for Meal is equal to accommodation setting")
                            .isEqualTo(result2);
                }
                $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                        +NewQuotationPage.AccomodationsTable.hotelTypeForCityREG)).selectOptionContainingText("Hotel 3* central");
                CommonCode.WaitForProgruzkaSilent();
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        +ProgrammSection.GetACityByNumberREG(1)
                        +ProgrammSection.GetMainServiceByNumberREG(1)
                        +ProgrammSection.serviceCriteriaRestaurantTypeREG)).getSelectedText();
                result2 = $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                        +NewQuotationPage.AccomodationsTable.hotelTypeForCityREG)).getSelectedText();
                result2=result2.substring(0, result2.indexOf('*')+1);
                if(result.equals(result2)){
                    System.out.println(CommonCode.ANSI_GREEN + "            - Настройки после изменения Accommodation совпадают" + CommonCode.ANSI_RESET);
                }
                else{
                    System.out.println(CommonCode.ANSI_RED + "            - Настройки после изменения Accommodation не совпадают" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that after changes in Accommodations hotel type for Meal is equal to accommodation setting")
                            .isEqualTo(result2);
                }
                $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                        +NewQuotationPage.AccomodationsTable.hotelTypeForCityREG)).selectOptionContainingText("Hotel 4* central");
                CommonCode.WaitForProgruzkaSilent();
            }
        }

        dropDownValues.clear();
        //Breakfast at the restaurant
        System.out.println("        - Пробуем выставить: "+"Breakfast at the restaurant");
        temp1=true;
        try{ $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                +ProgrammSection.GetACityByNumberREG(1)
                +ProgrammSection.GetMainServiceByNumberREG(1)
                +ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText(String.valueOf("Breakfast at the restaurant"));
        }catch (ElementNotFound e){
            //e.printStackTrace();
            System.out.println(CommonCode.ANSI_RED+" - Не смог выстивить "+CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Try to add service "+"Breakfast at the restaurant")
                    .isEqualTo("Yes");
            temp1=false;

            //System.out.println(e);
        }
        if(temp1==true){
            CommonCode.WaitForProgruzkaSilent();
            result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    +ProgrammSection.GetACityByNumberREG(1)
                    +ProgrammSection.GetMainServiceByNumberREG(1)
                    +ProgrammSection.serviceCriteriaNameREG)).getSelectedText();
            if(result.equals("Breakfast at the restaurant")){
                System.out.println(CommonCode.ANSI_GREEN + "            - Сервис выставлен успешно" + CommonCode.ANSI_RESET);
                temp2 = true;
            }
            else{
                System.out.println(CommonCode.ANSI_RED + "            - Сервис не выставлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that after meal out of hotel is chosen, autoservices are added")
                        .isEqualTo("Breakfast at the restaurant");
                temp2=false;
            }
            if(temp2==true){
                result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());
                //System.out.println(result);
                if(result.equals("2")){
                    //System.out.println(CommonCode.ANSI_GREEN + "            - Автосервисы выставлены успешно" + CommonCode.ANSI_RESET);
                    result2 = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            +ProgrammSection.GetACityByNumberREG(1)
                            +ProgrammSection.GetAutoServiceByNumberREG(1)
                            +ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                    if(result2.equals("Guide")){
                        System.out.println(CommonCode.ANSI_GREEN + "            - Автосервис Гид добавлен успешно" + CommonCode.ANSI_RESET);
                    }
                    else {
                        System.out.println(CommonCode.ANSI_RED + "            - Автосервис Гид не добавлен" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that after meal out of hotel is chosen, autoservice Guide is added")
                                .isEqualTo("Guide");
                    }
                    result2 = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            +ProgrammSection.GetACityByNumberREG(1)
                            +ProgrammSection.GetAutoServiceByNumberREG(2)
                            +ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                    if(result2.equals("Transport")){
                        System.out.println(CommonCode.ANSI_GREEN + "            - Автосервис Транспорт добавлен успешно" + CommonCode.ANSI_RESET);
                    }
                    else {
                        System.out.println(CommonCode.ANSI_RED + "            - Автосервис Транспорт не добавлен" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that after meal out of hotel is chosen, autoservice Tranport is added")
                                .isEqualTo("Transport");
                    }
                }
                else {
                    System.out.println(CommonCode.ANSI_RED + "            - Автосервисы не выставлены" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that after meal out of hotel is chosen, autoservices are added")
                            .isEqualTo("2");
                }
            }
        }

        System.out.print("        - Пробуем выставить Duration 11");
        temp2 = true;
        try {
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span/input[@name=\"duration\"]")).scrollTo().sendKeys("1");
            CommonCode.WaitForProgruzkaSilent();
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span/input[@name=\"duration\"]")).pressEnter();
            CommonCode.WaitForProgruzkaSilent();
        } catch (ElementNotFound e) {
            //e.printStackTrace();
            temp2 = false;
            System.out.println(CommonCode.ANSI_RED + " - Не смог Duration 11" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Try to set Duration as 11 ")
                    .isEqualTo("Yes");


            //System.out.println(e);
        }
        //sleep(5000);
        if (temp2 == true) {
            System.out.println(CommonCode.OK);
        } else {
            System.out.println(CommonCode.ANSI_RED + "    - При добавлении были ошибки." + CommonCode.ANSI_RESET);
        }
        result="none";
        temp2 = true;
        try {
            CommonCode.WaitForProgruzkaSilent();
            result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetAutoServiceByNumberREG(1)
                    + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
            //System.out.println(result);
        } catch (ElementNotFound e) {
            System.out.println(CommonCode.ANSI_RED + "            - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
            softAssertions.assertThat(result)
                    .as("Check that Hours for Guide is automatically set equally to Excursion")
                    .isEqualTo(String.valueOf("11"));
            temp2 = false;
        }
        if (temp2==true) {
            if (result.equals("11")) {
                System.out.println(CommonCode.ANSI_GREEN + "            - Часы для Гида выставлены корректно " + CommonCode.ANSI_RESET);

            } else {
                System.out.println(CommonCode.ANSI_RED + "            - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Hours for Guide is automatically set equally to Meal")
                        .isEqualTo(String.valueOf("11"));
            }
        }

        //Проверяем что автосервисы можно отключить/включить
        System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
        //Проверяем что можно отключить Гида
        temp1=true;
        try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(1)
                + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
        }
        catch(ElementNotFound e){
            e.printStackTrace();
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Гида" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Can`t find checkbox to disable Guide autoservice")
                    .isEqualTo(String.valueOf("Yes"));
        }

        if(temp1==true){
            CommonCode.WaitForProgruzkaSilent();
            result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetAutoServiceByNumberREG(1)
                    + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
            if (result.equals("Transport")){System.out.println(CommonCode.ANSI_GREEN + "      - Гида можно корректно отключить " + CommonCode.ANSI_RESET);}
            else{System.out.println(CommonCode.ANSI_RED + "      - Гид не отключился через чекбокс" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide autoservice can turned off by checkbox")
                        .isEqualTo(String.valueOf("Yes"));}
        }

        //Проверяем что можно отключить Транспорт
        temp1=true;
        try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(1)
                + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
        }
        catch(ElementNotFound e){
            e.printStackTrace();
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Can`t find checkbox to disable Guide autoservice")
                    .isEqualTo(String.valueOf("Yes"));
        }
        CommonCode.WaitForProgruzkaSilent();
        if(temp1==true){
            CommonCode.WaitForProgruzkaSilent();
            temp2=true;
            try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
            catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    temp2=false;
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp2==true){
                    if(result.equals("0")){
                        System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                    }
                    else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("0"));}
                }
            }

        CommonCode.WaitForProgruzkaSilent();
        ProgrammSection.DeleteLastMainService(1,1);

        //Добавляем Train Tickets
        System.out.print("[-] Добавляем сервис: Train Tickets");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Train Tickets");

        System.out.println("    - Пробуем выставить экскурсию: " + "Night train Moscow - Saint Petersburg");
        temp1 = true;
        try {
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText("Night train Moscow - Saint Petersburg");
        } catch (ElementNotFound e) {
            //e.printStackTrace();
            //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
            System.out.println(CommonCode.ANSI_RED + "    - Не смог выбрать: " + "Night train Moscow - Saint Petersburg" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Try to add service " + "Night train Moscow - Saint Petersburg")
                    .isEqualTo("Yes");
            temp1 = false;

            //System.out.println(e);
        }
        CommonCode.WaitForProgruzkaSilent();
        if (temp1 == true) {
            CommonCode.WaitForProgruzkaSilent();
            System.out.print("        - Пробуем выставить - 1st");
            temp2 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaClassREG)).scrollTo().selectOptionContainingText("1st");
                CommonCode.WaitForProgruzkaSilent();
            } catch (ElementNotFound e) {
                e.printStackTrace();
                temp2 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог выставить 1st" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set class for tickets as 1st")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            if (temp2 == true) {
                CommonCode.WaitForProgruzkaSilent();
                System.out.println(CommonCode.OK);
            } else {
                System.out.println(CommonCode.ANSI_RED + "    - При добавлении были ошибки." + CommonCode.ANSI_RESET); }
        }
        CommonCode.WaitForProgruzkaSilent();
        if (temp1 == true) {
            CommonCode.WaitForProgruzkaSilent();
            System.out.print("    - Пробуем выставить - Custom");
            temp2 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText("Custom");
                CommonCode.WaitForProgruzkaSilent();
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp2 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог выставить Custom" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set class for tickets as Custom")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            if (temp2 == true) {
                CommonCode.WaitForProgruzkaSilent();
                System.out.println(CommonCode.OK);

            } else {
                System.out.println(CommonCode.ANSI_RED + "    - При добавлении были ошибки." + CommonCode.ANSI_RESET); }
            if(temp2 == true){
                CommonCode.WaitForProgruzkaSilent();
                System.out.print("        - Пробуем выставить - business");
                temp2 = true;
                try {
                    $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetMainServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaClassREG)).scrollTo().selectOptionContainingText("business");
                    CommonCode.WaitForProgruzkaSilent();
                } catch (ElementNotFound e) {
                    e.printStackTrace();
                    temp2 = false;
                    System.out.println(CommonCode.ANSI_RED + " - Не смог выставить business" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat("No")
                            .as("Try to set class for tickets as business")
                            .isEqualTo("Yes");


                    //System.out.println(e);
                }
                if (temp2 == true) {
                    CommonCode.WaitForProgruzkaSilent();
                    System.out.println(CommonCode.OK);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "    - При добавлении были ошибки." + CommonCode.ANSI_RESET); }
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp2 == true){
                CommonCode.WaitForProgruzkaSilent();
                System.out.print("        - Пробуем заполнить Train Info");
                temp2 = true;
                try {
                    $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetMainServiceByNumberREG(1)
                            + ProgrammSection.serviceOptionsAddInfoREG)).scrollTo().setValue("Test info").pressEnter();
                    CommonCode.WaitForProgruzkaSilent();
                } catch (ElementNotFound e) {
                    e.printStackTrace();
                    temp2 = false;
                    System.out.println(CommonCode.ANSI_RED + " - Не смог заполнить" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat("No")
                            .as("Try to fill Train Info")
                            .isEqualTo("Yes");


                    //System.out.println(e);
                }
                if (temp2 == true) {
                    System.out.println(CommonCode.OK);
                }
            }

        }

        CommonCode.WaitForProgruzkaSilent();
        ProgrammSection.DeleteLastMainService(1,1);

        //Добавляем Flight Tickets
        System.out.print("[-] Добавляем сервис: Flight Tickets");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Flight Tickets");

        System.out.println("    - Пробуем выставить экскурсию: " + "Flight Moscow-Saint Petersburg");
        temp1 = true;
        try {
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText("Flight Moscow-Saint Petersburg");
        } catch (ElementNotFound e) {
            //e.printStackTrace();
            //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
            System.out.println(CommonCode.ANSI_RED + "    - Не смог выбрать: " + "Flight Moscow-Saint Petersburg" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Try to add service " + "Flight Moscow-Saint Petersburg")
                    .isEqualTo("Yes");
            temp1 = false;

            //System.out.println(e);
        }
        CommonCode.WaitForProgruzkaSilent();
        if (temp1 == true) {
            CommonCode.WaitForProgruzkaSilent();
            System.out.print("        - Пробуем выставить - 1st");
            temp2 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaClassREG)).scrollTo().selectOptionContainingText("1st");
                CommonCode.WaitForProgruzkaSilent();
            } catch (ElementNotFound e) {
                e.printStackTrace();
                temp2 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог выставить 1st" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set class for tickets as 1st")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            if (temp2 == true) {
                CommonCode.WaitForProgruzkaSilent();
                System.out.println(CommonCode.OK);
            } else {
                System.out.println(CommonCode.ANSI_RED + "    - При добавлении были ошибки." + CommonCode.ANSI_RESET); }
        }
        CommonCode.WaitForProgruzkaSilent();
        if (temp1 == true) {
            CommonCode.WaitForProgruzkaSilent();
            System.out.print("    - Пробуем выставить - Custom");
            temp2 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText("Custom");
                CommonCode.WaitForProgruzkaSilent();
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp2 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог выставить Custom" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set class for tickets as Custom")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            if (temp2 == true) {
                CommonCode.WaitForProgruzkaSilent();
                System.out.println(CommonCode.OK);

            } else {
                System.out.println(CommonCode.ANSI_RED + "    - При добавлении были ошибки." + CommonCode.ANSI_RESET); }
            if(temp2 == true){
                CommonCode.WaitForProgruzkaSilent();
                System.out.print("        - Пробуем выставить - business");
                temp2 = true;
                try {
                    $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetMainServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaClassREG)).scrollTo().selectOptionContainingText("business");
                    CommonCode.WaitForProgruzkaSilent();
                } catch (ElementNotFound e) {
                    e.printStackTrace();
                    temp2 = false;
                    System.out.println(CommonCode.ANSI_RED + " - Не смог выставить business" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat("No")
                            .as("Try to set class for tickets as business")
                            .isEqualTo("Yes");


                    //System.out.println(e);
                }
                if (temp2 == true) {
                    CommonCode.WaitForProgruzkaSilent();
                    System.out.println(CommonCode.OK);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "    - При добавлении были ошибки." + CommonCode.ANSI_RESET); }
            }
            if(temp2 == true){
                CommonCode.WaitForProgruzkaSilent();
                System.out.print("        - Пробуем заполнить Flight Info");
                temp2 = true;
                try {
                    $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetMainServiceByNumberREG(1)
                            + ProgrammSection.serviceOptionsAddInfoREG)).scrollTo().setValue("Test info").pressEnter();
                    CommonCode.WaitForProgruzkaSilent();
                } catch (ElementNotFound e) {
                    e.printStackTrace();
                    temp2 = false;
                    System.out.println(CommonCode.ANSI_RED + " - Не смог заполнить" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat("No")
                            .as("Try to fill Train Info")
                            .isEqualTo("Yes");


                    //System.out.println(e);
                }
                if (temp2 == true) {
                    CommonCode.WaitForProgruzkaSilent();
                    System.out.println(CommonCode.OK);
                }
            }

        }

        CommonCode.WaitForProgruzkaSilent();
        ProgrammSection.DeleteLastMainService(1,1);

        //Добавляем Show
        System.out.print("[-] Добавляем сервис: Show");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Show");
        System.out.println("    - Пробуем выставить шоу: " + "Bolshoi Theatre (opera)");
        temp1 = true;
        try {
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText("Bolshoi Theatre (opera)");
        } catch (ElementNotFound e) {
            //e.printStackTrace();
            //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Try to add service " + "Bolshoi Theatre (opera)")
                    .isEqualTo("Yes");
            temp1 = false;

            //System.out.println(e);
        }

        if (temp1 == true) {
            CommonCode.WaitForProgruzkaSilent();
            temp2 = true;
            System.out.print("        - Пробуем выставить Duration 10");
            temp2 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + "/td[@class=\"options\"]/span/input[@name=\"duration\"]")).scrollTo().setValue("10");
                CommonCode.WaitForProgruzkaSilent();
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp2 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог Duration 10" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set Duration as 10 ")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            if (temp2 == true) {
                CommonCode.WaitForProgruzkaSilent();
                System.out.println(CommonCode.OK);
            } else {
                System.out.println(CommonCode.ANSI_RED + "    - При добавлении были ошибки." + CommonCode.ANSI_RESET);
            }

        } else {
            System.out.println(CommonCode.ANSI_RED + "    - Не смог выбрать " + "Bolshoi Theatre (opera)" + CommonCode.ANSI_RESET);
        }

        //Проверяем что добавились нужные автосервисы
        System.out.println("    - Проверяем что добавились нужные автосервисы:");

        //Проверяем что добавился Гид
        temp1=true;
        result = "none";
        try {
            result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                    ProgrammSection.GetACityByNumberREG(1) +
                    ProgrammSection.GetAutoServiceByNumberREG(1) +
                    ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
        } catch (ElementNotFound e) {
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
            softAssertions.assertThat(result)
                    .as("Check that Guide is automatically added to Show")
                    .isEqualTo(String.valueOf("Guide"));
        }
        if (temp1 == true) {
            if (result.equals("Guide")) {
                System.out.println(CommonCode.ANSI_GREEN + "      - Гид добавлен " + CommonCode.ANSI_RESET);

            } else {
                System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide is automatically added to Show")
                        .isEqualTo(String.valueOf("Transport"));
            }
        }
        //Проверяем что часы для гида выставлены в 10
        if (temp1 == true) {
            temp2 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
            } catch (ElementNotFound e) {
                System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Hours for Guide is automatically set equally to Show")
                        .isEqualTo(String.valueOf("10"));
                temp2 = false;
            }
            if (temp2==true) {
                if (result.equals("10")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Гида выставлены корректно " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Guide is automatically set equally to Show")
                            .isEqualTo(String.valueOf("10"));
                }
            }
        }

        //Проверяем что добавился транспорт
        result = "none";
        temp1 = true;
        try {
            result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                    ProgrammSection.GetACityByNumberREG(1) +
                    ProgrammSection.GetAutoServiceByNumberREG(2) +
                    ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
        } catch (ElementNotFound e) {
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
            softAssertions.assertThat(result)
                    .as("Check that Transport is automatically added to Show")
                    .isEqualTo(String.valueOf("Transport"));
        }
        if (temp1 == true) {
            if (result.equals("Transport")) {
                System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт добавлен " + CommonCode.ANSI_RESET);

            } else {
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport is automatically added to Show")
                        .isEqualTo(String.valueOf("Transport"));
            }
        }
        //Проверяем что часы для транспорта выставлены в 10
        if (temp1 == true) {
            temp2 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(2)
                        + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
            } catch (ElementNotFound e) {
                System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Hours for Transport is automatically set equally to Show")
                        .isEqualTo(String.valueOf("10"));
                temp2 = false;
            }
            if (temp2==true) {
                if (result.equals("10")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Транспорта выставлены корректно " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Transport is automatically set equally to Show")
                            .isEqualTo(String.valueOf("10"));
                }
            }
        }
        System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
        //Проверяем что можно отключить Гида
        temp1=true;
        try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(1)
                + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
        }
        catch(ElementNotFound e){
            e.printStackTrace();
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Гида" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Can`t find checkbox to disable Guide autoservice")
                    .isEqualTo(String.valueOf("Yes"));
        }

        if(temp1==true){
            CommonCode.WaitForProgruzkaSilent();
            result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetAutoServiceByNumberREG(1)
                    + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
            if (result.equals("Transport")){System.out.println(CommonCode.ANSI_GREEN + "      - Гида можно корректно отключить " + CommonCode.ANSI_RESET);}
            else{System.out.println(CommonCode.ANSI_RED + "      - Гид не отключился через чекбокс" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide autoservice can turned off by checkbox")
                        .isEqualTo(String.valueOf("Yes"));}
        }

        //Проверяем что можно отключить Транспорт
        temp1=true;
        try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(1)
                + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
        }
        catch(ElementNotFound e){
            e.printStackTrace();
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Can`t find checkbox to disable Transport autoservice")
                    .isEqualTo(String.valueOf("Yes"));
        }

        if(temp1==true){
            CommonCode.WaitForProgruzkaSilent();
            temp2=true;
            try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
            catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    temp2=false;
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp2==true){
                    if(result.equals("0")){
                        System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                    }
                    else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("0"));}
                }
            }

            //Проверяем что можно влючить Наушники
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[7]/input[@name=\"headphones\"]")).click();
                CommonCode.WaitForProgruzkaSilent();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс включения Наушников" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to enable Headphones autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            result = "none";
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                try{result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();}
                catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Наушники не получилось корректно включить " + CommonCode.ANSI_RESET);
                }
                if (result.equals("Special Services")){
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaNameREG)).getSelectedText();
                    if(result.equals("Headphones")) System.out.println(CommonCode.ANSI_GREEN + "      - Наушники включились через чекбокс" + CommonCode.ANSI_RESET);
                    else {
                        System.out.println(CommonCode.ANSI_RED + "      - Наушники не получилось корректно включить " + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("Headphones"));}
                }
                else{
                    System.out.println(CommonCode.ANSI_RED + "      - Наушники не получилось корректно включить " + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Transport autoservice can turned off by checkbox")
                            .isEqualTo(String.valueOf("Special Services"));}
            }

            System.out.println("    - Проверяем что тип временного периода выставляется корректно:");
            Map<String, String> guideHirePeriods = new HashMap<>();
            guideHirePeriods.put("4", "1/2 day (4 hours)");
            guideHirePeriods.put("8", "Full day (8 hours)");
            guideHirePeriods.put("24", "Day (24 hours)");
            //Включаем Гида
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(1)
                + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
            CommonCode.WaitForProgruzkaSilent();
            //Выключаем наушники
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(1)
                + "/td[@class=\"options\"]/span[7]/input[@name=\"headphones\"]")).click();
            CommonCode.WaitForProgruzkaSilent();

            for (Map.Entry<String, String> entry : guideHirePeriods.entrySet()) {
                String hours = entry.getKey();
                String dropDowdValue = entry.getValue();

                System.out.println("        - Пробуем выставить Duration - "+hours);
                temp1 = true;
                try {
                    $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetMainServiceByNumberREG(1)
                            + ProgrammSection.serviceOptionsDurationREG)).scrollTo().click();
                    $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetMainServiceByNumberREG(1)
                            + ProgrammSection.serviceOptionsDurationREG)).clear();
                    confirm();
                    $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetMainServiceByNumberREG(1)
                            + ProgrammSection.serviceOptionsDurationREG)).sendKeys(hours+"\13");
                    $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetMainServiceByNumberREG(1)
                            + ProgrammSection.serviceOptionsDurationREG)).pressEnter();
                    CommonCode.WaitForProgruzkaSilent();
                } catch (ElementNotFound e) {
                    //e.printStackTrace();
                    temp1 = false;
                    System.out.println(CommonCode.ANSI_RED + " - Не смог найти Duration - "+hours + CommonCode.ANSI_RESET);
                    softAssertions.assertThat("No")
                            .as("Try to set Duration as "+hours)
                            .isEqualTo("Yes");


                    //System.out.println(e);
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp1 == true) {
                    CommonCode.WaitForProgruzkaSilent();
                    temp2 = true;
                    try {
                        result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                                + ProgrammSection.GetACityByNumberREG(1)
                                + ProgrammSection.GetAutoServiceByNumberREG(1)
                                + ProgrammSection.serviceCriteriaNameREG)).getSelectedText();
                    } catch (ElementNotFound e) {
                        System.out.println(CommonCode.ANSI_RED + "      - Не смог получить значение дропдауна у Гида" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Can't get value of Duration for Guide")
                                .isEqualTo(String.valueOf(dropDowdValue));
                        temp2 = false;
                    }
                    if (temp2==true) {
                        if (result.equals(dropDowdValue)) {
                            System.out.println(CommonCode.ANSI_GREEN + "            - Тип часов для Гида выставлен корректно " + CommonCode.ANSI_RESET);

                        } else {
                            System.out.println(CommonCode.ANSI_RED + "              - Тип часов для Гида выставлен некорректно" + CommonCode.ANSI_RESET);
                            softAssertions.assertThat(result)
                                    .as("Check that type of Hours for Guide")
                                    .isEqualTo(String.valueOf(dropDowdValue));
                        }
                    }


                } else {
                    System.out.println(CommonCode.ANSI_RED + "          - При добавлении были ошибки." + CommonCode.ANSI_RESET);
                }
            }
            //Удаляем экскурсию
            System.out.println("    - Проверяем что автосервисы автоматически удалятся после удаления основного сервиса:");
            CommonCode.WaitForProgruzkaSilent();
            ProgrammSection.DeleteLastMainService(1,1);
            //Проверяем что после удаления экскурсии удалились автосервисы
            temp2=true;
            result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());

            if(result.equals("0")){
                System.out.println(CommonCode.ANSI_GREEN + "      - Автосервисы удалены " + CommonCode.ANSI_RESET);
            }
            else{System.out.println(CommonCode.ANSI_RED + "      - Автосервисы не удалены" + CommonCode.ANSI_RESET);
                throw new IllegalArgumentException("Autoservices were not deleted automatically, there are "+result+" autoservices still there");}


        CommonCode.WaitForProgruzkaSilent();

        //Добавляем Transport
        System.out.print("[-] Добавляем сервис: Transport");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Transport");
        dropDownValues.clear();
        //dropDownValues.addAll(Arrays.asList("City trip","Hourly", "To/from airport"));
        dropDownValues.addAll(Arrays.asList("City trip"));
        temp1=true;
        for(int dropDownValuesCounter=0; dropDownValuesCounter<dropDownValues.size(); dropDownValuesCounter++) {
            System.out.println("    - Пробуем выставить: " + dropDownValues.get(dropDownValuesCounter));
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText(String.valueOf(dropDownValues.get(dropDownValuesCounter)));
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to add service " + dropDownValues.get(dropDownValuesCounter))
                        .isEqualTo("Yes");
                temp1 = false;

                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).scrollTo().setValue("4");

            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог найти Duration - "+"4" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set Duration as "+"4")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            result = "none";
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(1) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide is automatically added to Show")
                        .isEqualTo(String.valueOf("Guide"));
            }
            if (temp1 == true) {
                if (result.equals("Guide")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Гид добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для гида выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Guide is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Гида выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Guide is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }

            //Проверяем что добавился транспорт
            result = "none";
            temp1 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(2) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport is automatically added to Show")
                        .isEqualTo(String.valueOf("Transport"));
            }
            if (temp1 == true) {
                if (result.equals("Transport")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Transport is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для транспорта выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(2)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Transport is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Транспорта выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Transport is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }
            System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
            //Проверяем что можно отключить Гида
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Гида" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Guide autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                if (result.equals("Transport")){System.out.println(CommonCode.ANSI_GREEN + "      - Гида можно корректно отключить " + CommonCode.ANSI_RESET);}
                else{System.out.println(CommonCode.ANSI_RED + "      - Гид не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide autoservice can turned off by checkbox")
                            .isEqualTo(String.valueOf("Yes"));}
            }

            //Проверяем что можно отключить Транспорт
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Transport autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                temp2=true;
                try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
                catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    temp2=false;
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp2==true){
                    if(result.equals("0")){
                        System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                    }
                    else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("0"));}
                }
            }
        }

        ProgrammSection.DeleteLastMainService(1,1);
        CommonCode.WaitForProgruzkaSilent();

        //Добавляем Special Services
        System.out.print("[-] Добавляем сервис: Special Services");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Special Services");
        System.out.print("      - Пробуем добавить новый Special Service:");
        temp1=true;
        try{
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"criteria\"]//a[@class=\"qbtn qbtn-add-custom-service\"]")).scrollTo().click();
        }
        catch (ElementNotFound e) {
            //e.printStackTrace();
            System.out.println(CommonCode.ANSI_RED+" - Нет кнопки Add"+CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("There is no button to add new special service " )
                    .isEqualTo("Yes");
            temp1 = false;

            //System.out.println(e);
        }
        if(temp1==true){
            $(By.xpath("//div[@id=\"modal-createcustomservice\"]//div[@class=\"modal-content\"]" +
                    "//div[@class=\"modal-body\"]/form//div[@class=\"form-group\"]")).shouldBe(Condition.visible);

            $(By.xpath("//div[@id=\"modal-createcustomservice\"]//div[@class=\"modal-content\"]" +
                    "//div[@class=\"modal-footer\"]//button[@class=\"btn btn-default btn-cancel\"]")).click();

            $(By.xpath("//div[@id=\"modal-createcustomservice\"]//div[@class=\"modal-content\"]" +
                    "//div[@class=\"modal-body\"]/form//div[@class=\"form-group\"]")).shouldNotBe(Condition.visible);
            System.out.println(CommonCode.ANSI_GREEN+" - Popup - ok"+CommonCode.ANSI_RESET);
        }

        dropDownValues.clear();
        dropDownValues.addAll(Arrays.asList("AK 47 shooting"));
        temp1=true;
        for(int dropDownValuesCounter=0; dropDownValuesCounter<dropDownValues.size(); dropDownValuesCounter++) {
            System.out.println("    - Пробуем выставить: " + dropDownValues.get(dropDownValuesCounter));
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText(String.valueOf(dropDownValues.get(dropDownValuesCounter)));
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to add service " + dropDownValues.get(dropDownValuesCounter))
                        .isEqualTo("Yes");
                temp1 = false;

                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).scrollTo().setValue("4");

            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог найти Duration - "+"4" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set Duration as "+"4")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            result = "none";
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(1) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide is automatically added to Show")
                        .isEqualTo(String.valueOf("Guide"));
            }
            if (temp1 == true) {
                if (result.equals("Guide")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Гид добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для гида выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Guide is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Гида выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Guide is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }

            //Проверяем что добавился транспорт
            result = "none";
            temp1 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(2) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport is automatically added to Show")
                        .isEqualTo(String.valueOf("Transport"));
            }
            if (temp1 == true) {
                if (result.equals("Transport")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Transport is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для транспорта выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(2)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Transport is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Транспорта выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Transport is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }
            System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
            //Проверяем что можно отключить Гида
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Гида" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Guide autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                if (result.equals("Transport")){System.out.println(CommonCode.ANSI_GREEN + "      - Гида можно корректно отключить " + CommonCode.ANSI_RESET);}
                else{System.out.println(CommonCode.ANSI_RED + "      - Гид не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide autoservice can turned off by checkbox")
                            .isEqualTo(String.valueOf("Yes"));}
            }

            //Проверяем что можно отключить Транспорт
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Transport autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                temp2=true;
                try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
                catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    temp2=false;
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp2==true){
                    if(result.equals("0")){
                        System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                    }
                    else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("0"));}
                }
            }
        }
        ProgrammSection.DeleteLastMainService(1,1);
        CommonCode.WaitForProgruzkaSilent();

        //Добавляем Meeting
        System.out.print("[-] Добавляем сервис: Meeting");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Meeting");
        dropDownValues.clear();
        //dropDownValues.addAll(Arrays.asList("City trip","Hourly", "To/from airport"));
        dropDownValues.addAll(Arrays.asList("Meeting with Guide"));
        temp1=true;
        for(int dropDownValuesCounter=0; dropDownValuesCounter<dropDownValues.size(); dropDownValuesCounter++) {
            System.out.println("    - Пробуем выставить: " + dropDownValues.get(dropDownValuesCounter));
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText(String.valueOf(dropDownValues.get(dropDownValuesCounter)));
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to add service " + dropDownValues.get(dropDownValuesCounter))
                        .isEqualTo("Yes");
                temp1 = false;

                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).scrollTo().setValue("4");

            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог найти Duration - "+"4" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set Duration as "+"4")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            result = "none";
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(1) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide is automatically added to Show")
                        .isEqualTo(String.valueOf("Guide"));
            }
            if (temp1 == true) {
                if (result.equals("Guide")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Гид добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для гида выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Guide is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Гида выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Guide is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }

            //Проверяем что добавился транспорт
            result = "none";
            temp1 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(2) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport is automatically added to Show")
                        .isEqualTo(String.valueOf("Transport"));
            }
            if (temp1 == true) {
                if (result.equals("Transport")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Transport is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для транспорта выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(2)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Transport is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Транспорта выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Transport is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }
            System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
            //Проверяем что можно отключить Гида
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Гида" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Guide autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                if (result.equals("Transport")){System.out.println(CommonCode.ANSI_GREEN + "      - Гида можно корректно отключить " + CommonCode.ANSI_RESET);}
                else{System.out.println(CommonCode.ANSI_RED + "      - Гид не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide autoservice can turned off by checkbox")
                            .isEqualTo(String.valueOf("Yes"));}
            }

            //Проверяем что можно отключить Транспорт
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Transport autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                temp2=true;
                try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
                catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    temp2=false;
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp2==true){
                    if(result.equals("0")){
                        System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                    }
                    else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("0"));}
                }
            }
        }
        ProgrammSection.DeleteLastMainService(1,1);
        CommonCode.WaitForProgruzkaSilent();

        //Добавляем Arrival/Depature
        System.out.print("[-] Добавляем сервис: Arrival/Depature");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Arrival/Depature");
        dropDownValues.clear();
        //dropDownValues.addAll(Arrays.asList("City trip","Hourly", "To/from airport"));
        dropDownValues.addAll(Arrays.asList("Arriving at the airport"));
        temp1=true;
        for(int dropDownValuesCounter=0; dropDownValuesCounter<dropDownValues.size(); dropDownValuesCounter++) {
            System.out.println("    - Пробуем выставить: " + dropDownValues.get(dropDownValuesCounter));
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText(String.valueOf(dropDownValues.get(dropDownValuesCounter)));
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to add service " + dropDownValues.get(dropDownValuesCounter))
                        .isEqualTo("Yes");
                temp1 = false;

                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).scrollTo().setValue("4");

            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог найти Duration - "+"4" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set Duration as "+"4")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            result = "none";
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(1) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide is automatically added to Show")
                        .isEqualTo(String.valueOf("Guide"));
            }
            if (temp1 == true) {
                if (result.equals("Guide")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Гид добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для гида выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Guide is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Гида выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Guide is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }

            //Проверяем что добавился транспорт
            result = "none";
            temp1 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(2) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport is automatically added to Show")
                        .isEqualTo(String.valueOf("Transport"));
            }
            if (temp1 == true) {
                if (result.equals("Transport")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Transport is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для транспорта выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(2)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Transport is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Транспорта выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Transport is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }
            System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
            //Проверяем что можно отключить Гида
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Гида" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Guide autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                if (result.equals("Transport")){System.out.println(CommonCode.ANSI_GREEN + "      - Гида можно корректно отключить " + CommonCode.ANSI_RESET);}
                else{System.out.println(CommonCode.ANSI_RED + "      - Гид не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide autoservice can turned off by checkbox")
                            .isEqualTo(String.valueOf("Yes"));}
            }

            //Проверяем что можно отключить Транспорт
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Transport autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                temp2=true;
                try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
                catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    temp2=false;
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp2==true){
                    if(result.equals("0")){
                        System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                    }
                    else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("0"));}
                }
            }
        }
        ProgrammSection.DeleteLastMainService(1,1);
        CommonCode.WaitForProgruzkaSilent();

        //Добавляем Check-In/Check-Out
        System.out.print("[-] Добавляем сервис: Check-In/Check-Out");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Check-In/Check-Out");
        dropDownValues.clear();
        //dropDownValues.addAll(Arrays.asList("City trip","Hourly", "To/from airport"));
        dropDownValues.addAll(Arrays.asList("Check-in at the hotel"));
        temp1=true;
        for(int dropDownValuesCounter=0; dropDownValuesCounter<dropDownValues.size(); dropDownValuesCounter++) {
            System.out.println("    - Пробуем выставить: " + dropDownValues.get(dropDownValuesCounter));
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText(String.valueOf(dropDownValues.get(dropDownValuesCounter)));
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to add service " + dropDownValues.get(dropDownValuesCounter))
                        .isEqualTo("Yes");
                temp1 = false;

                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).scrollTo().setValue("4");

            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог найти Duration - "+"4" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set Duration as "+"4")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            result = "none";
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(1) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide is automatically added to Show")
                        .isEqualTo(String.valueOf("Guide"));
            }
            if (temp1 == true) {
                if (result.equals("Guide")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Гид добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для гида выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Guide is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Гида выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Guide is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }

            //Проверяем что добавился транспорт
            result = "none";
            temp1 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(2) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport is automatically added to Show")
                        .isEqualTo(String.valueOf("Transport"));
            }
            if (temp1 == true) {
                if (result.equals("Transport")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Transport is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для транспорта выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(2)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Transport is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Транспорта выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Transport is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }
            System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
            //Проверяем что можно отключить Гида
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Гида" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Guide autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                if (result.equals("Transport")){System.out.println(CommonCode.ANSI_GREEN + "      - Гида можно корректно отключить " + CommonCode.ANSI_RESET);}
                else{System.out.println(CommonCode.ANSI_RED + "      - Гид не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide autoservice can turned off by checkbox")
                            .isEqualTo(String.valueOf("Yes"));}
            }

            //Проверяем что можно отключить Транспорт
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Transport autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                temp2=true;
                try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
                catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    temp2=false;
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp2==true){
                    if(result.equals("0")){
                        System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                    }
                    else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("0"));}
                }
            }
        }
        ProgrammSection.DeleteLastMainService(1,1);
        CommonCode.WaitForProgruzkaSilent();

        //Добавляем Transfer
        System.out.print("[-] Добавляем сервис: Transfer");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Transfer");
        dropDownValues.clear();
        //dropDownValues.addAll(Arrays.asList("City trip","Hourly", "To/from airport"));
        dropDownValues.addAll(Arrays.asList("Transfer from the Airport"));
        temp1=true;
        for(int dropDownValuesCounter=0; dropDownValuesCounter<dropDownValues.size(); dropDownValuesCounter++) {
            System.out.println("    - Пробуем выставить: " + dropDownValues.get(dropDownValuesCounter));
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText(String.valueOf(dropDownValues.get(dropDownValuesCounter)));
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to add service " + dropDownValues.get(dropDownValuesCounter))
                        .isEqualTo("Yes");
                temp1 = false;

                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).scrollTo().setValue("4");

            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог найти Duration - "+"4" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set Duration as "+"4")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            result = "none";
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(1) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide is automatically added to Show")
                        .isEqualTo(String.valueOf("Guide"));
            }
            if (temp1 == true) {
                if (result.equals("Guide")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Гид добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для гида выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Guide is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Гида выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Guide is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }

            //Проверяем что добавился транспорт
            result = "none";
            temp1 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(2) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport is automatically added to Show")
                        .isEqualTo(String.valueOf("Transport"));
            }
            if (temp1 == true) {
                if (result.equals("Transport")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Transport is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для транспорта выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(2)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Transport is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Транспорта выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Transport is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }
            System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
            //Проверяем что можно отключить Гида
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Гида" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Guide autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                if (result.equals("Transport")){System.out.println(CommonCode.ANSI_GREEN + "      - Гида можно корректно отключить " + CommonCode.ANSI_RESET);}
                else{System.out.println(CommonCode.ANSI_RED + "      - Гид не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide autoservice can turned off by checkbox")
                            .isEqualTo(String.valueOf("Yes"));}
            }

            //Проверяем что можно отключить Транспорт
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Transport autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                temp2=true;
                try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
                catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    temp2=false;
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp2==true){
                    if(result.equals("0")){
                        System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                    }
                    else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("0"));}
                }
            }
        }
        ProgrammSection.DeleteLastMainService(1,1);
        CommonCode.WaitForProgruzkaSilent();

        //Добавляем Overnight
        System.out.print("[-] Добавляем сервис: Overnight");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Overnight");
        dropDownValues.clear();
        temp1=true;
        try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).scrollTo().setValue("4");

            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог найти Duration - "+"4" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set Duration as "+"4")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();

            //Проверяем что добавился транспорт
            result = "none";
            temp1 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(1) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport is automatically added to Show")
                        .isEqualTo(String.valueOf("Transport"));
            }
            if (temp1 == true) {
                if (result.equals("Transport")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Transport is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для транспорта выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Transport is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Транспорта выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Transport is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }
            System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
            //Проверяем что можно отключить Транспорт
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Transport autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                temp2=true;
                try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
                catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    temp2=false;
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp2==true){
                    if(result.equals("0")){
                        System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                    }
                    else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("0"));}
                }
            }
        //}
        ProgrammSection.DeleteLastMainService(1,1);
        CommonCode.WaitForProgruzkaSilent();

        //Добавляем Intercity Transfer
        System.out.print("[-] Добавляем сервис: Intercity Transfer");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Intercity Transfer");
        dropDownValues.clear();
        //dropDownValues.addAll(Arrays.asList("City trip","Hourly", "To/from airport"));
        dropDownValues.addAll(Arrays.asList("VLG"));
        temp1=true;
        for(int dropDownValuesCounter=0; dropDownValuesCounter<dropDownValues.size(); dropDownValuesCounter++) {
            System.out.println("    - Пробуем выставить: " + dropDownValues.get(dropDownValuesCounter));
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceCriteriaNameREG)).scrollTo().selectOptionContainingText(String.valueOf(dropDownValues.get(dropDownValuesCounter)));
            } catch (ElementNotFound e) {
                //e.printStackTrace();
                //System.out.println(CommonCode.ANSI_RED+" - Не смог выбрать "+dropDownValues[dropDownValuesCounter]+CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to add service " + dropDownValues.get(dropDownValuesCounter))
                        .isEqualTo("Yes");
                temp1 = false;

                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            temp1 = true;
            try {
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + ProgrammSection.serviceOptionsDurationREG)).scrollTo().setValue("4");

            } catch (ElementNotFound e) {
                //e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + " - Не смог найти Duration - "+"4" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Try to set Duration as "+"4")
                        .isEqualTo("Yes");


                //System.out.println(e);
            }
            CommonCode.WaitForProgruzkaSilent();
            result = "none";
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(1) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Guide is automatically added to Show")
                        .isEqualTo(String.valueOf("Guide"));
            }
            if (temp1 == true) {
                if (result.equals("Guide")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Гид добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Гид не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для гида выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(1)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Guide is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Гида выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Гида выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Guide is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }

            //Проверяем что добавился транспорт
            result = "none";
            temp1 = true;
            try {
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1) +
                        ProgrammSection.GetACityByNumberREG(1) +
                        ProgrammSection.GetAutoServiceByNumberREG(2) +
                        ProgrammSection.serviceNameDropDownREG)).scrollTo().getSelectedText();
            } catch (ElementNotFound e) {
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Check that Transport is automatically added to Show")
                        .isEqualTo(String.valueOf("Transport"));
            }
            if (temp1 == true) {
                if (result.equals("Transport")) {
                    System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт добавлен " + CommonCode.ANSI_RESET);

                } else {
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не добавлен" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Transport is automatically added to Show")
                            .isEqualTo(String.valueOf("Transport"));
                }
            }
            //Проверяем что часы для транспорта выставлены в 4
            if (temp1 == true) {
                temp2 = true;
                try {
                    result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                            + ProgrammSection.GetACityByNumberREG(1)
                            + ProgrammSection.GetAutoServiceByNumberREG(2)
                            + ProgrammSection.serviceCriteriaDurationREG)).getAttribute("value");
                } catch (ElementNotFound e) {
                    System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Hours for Transport is automatically set equally to Show")
                            .isEqualTo(String.valueOf("4"));
                    temp2 = false;
                }
                if (temp2==true) {
                    if (result.equals("4")) {
                        System.out.println(CommonCode.ANSI_GREEN + "      - Часы для Транспорта выставлены корректно " + CommonCode.ANSI_RESET);

                    } else {
                        System.out.println(CommonCode.ANSI_RED + "      - Часы для Транспорта выставлены некорректно" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Hours for Transport is automatically set equally to Show")
                                .isEqualTo(String.valueOf("4"));
                    }
                }
            }
            System.out.println("    - Проверяем что автосервисы можно отключить/включить:");
            //Проверяем что можно отключить Гида
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
                $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetMainServiceByNumberREG(1)
                        + "/td[@class=\"options\"]/span[3]/input[@name=\"guide\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Гида" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Guide autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + ProgrammSection.GetAutoServiceByNumberREG(1)
                        + ProgrammSection.serviceNameDropDownREG)).getSelectedText();
                if (result.equals("Transport")){System.out.println(CommonCode.ANSI_GREEN + "      - Гида можно корректно отключить " + CommonCode.ANSI_RESET);}
                else{System.out.println(CommonCode.ANSI_RED + "      - Гид не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    softAssertions.assertThat(result)
                            .as("Check that Guide autoservice can turned off by checkbox")
                            .isEqualTo(String.valueOf("Yes"));}
            }

            //Проверяем что можно отключить Транспорт
            temp1=true;
            try{$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + "/td[@class=\"options\"]/span[5]/input[@name=\"transport\"]")).click();
            }
            catch(ElementNotFound e){
                e.printStackTrace();
                temp1 = false;
                System.out.println(CommonCode.ANSI_RED + "      - Не смог найти чекбокс отключения Транспорта" + CommonCode.ANSI_RESET);
                softAssertions.assertThat("No")
                        .as("Can`t find checkbox to disable Transport autoservice")
                        .isEqualTo(String.valueOf("Yes"));
            }
            CommonCode.WaitForProgruzkaSilent();
            if(temp1==true){
                CommonCode.WaitForProgruzkaSilent();
                temp2=true;
                try{result = String.valueOf($$(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                        + ProgrammSection.GetACityByNumberREG(1)
                        + "//tbody[@class=\"auto\"]/tr[@class=\"service\"]")).size());}
                catch(ElementNotFound e){
                    System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                    temp2=false;
                }
                CommonCode.WaitForProgruzkaSilent();
                if (temp2==true){
                    if(result.equals("0")){
                        System.out.println(CommonCode.ANSI_GREEN + "      - Транспорт можно корректно отключить " + CommonCode.ANSI_RESET);
                    }
                    else{System.out.println(CommonCode.ANSI_RED + "      - Транспорт не отключился через чекбокс" + CommonCode.ANSI_RESET);
                        softAssertions.assertThat(result)
                                .as("Check that Transport autoservice can turned off by checkbox")
                                .isEqualTo(String.valueOf("0"));}
                }
            }
        }
        ProgrammSection.DeleteLastMainService(1,1);
        CommonCode.WaitForProgruzkaSilent();

        //Добавляем Intercity Guide
        System.out.print("[-] Добавляем сервис: Intercity Guide");
        NewQuotationPage.ProgrammSection.AddServiceByName(1,1, "Intercity Guide");
        System.out.print("        - Пробуем выставить Days 10");
        temp1 = true;
        try {
            $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + ProgrammSection.serviceCriteriaDaysREG)).scrollTo().setValue("10").pressEnter();
            CommonCode.WaitForProgruzkaSilent();
        } catch (ElementNotFound e) {
            //e.printStackTrace();
            temp1 = false;
            System.out.println(CommonCode.ANSI_RED + " - Не найти контрол Days 10" + CommonCode.ANSI_RESET);
            softAssertions.assertThat("No")
                    .as("Can`t find input Days")
                    .isEqualTo("Yes");
            //System.out.println(e);
        }
        CommonCode.WaitForProgruzkaSilent();
        if(temp1){
            result = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                    + ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetMainServiceByNumberREG(1)
                    + ProgrammSection.serviceCriteriaDaysREG)).getValue();
            if (result.equals("10")){System.out.println(CommonCode.ANSI_GREEN + "      - Дни выставились корректно " + CommonCode.ANSI_RESET);}
            else{System.out.println(CommonCode.ANSI_RED + "      - Дни не выставились" + CommonCode.ANSI_RESET);
                softAssertions.assertThat(result)
                        .as("Can`t fill input Days")
                        .isEqualTo(String.valueOf("10"));}
        }
        ProgrammSection.DeleteLastMainService(1,1);
        CommonCode.WaitForProgruzkaSilent();

    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
