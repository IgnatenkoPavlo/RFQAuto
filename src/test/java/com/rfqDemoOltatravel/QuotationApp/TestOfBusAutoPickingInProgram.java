package com.rfqDemoOltatravel.QuotationApp;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.open;
import static com.rfqDemoOltatravel.QuotationApp.NewQuotationPage.*;
import static com.rfqDemoOltatravel.QuotationApp.NewQuotationPage.AddCityToAccomodationByName;
import static com.rfqDemoOltatravel.QuotationApp.NewQuotationPage.CreateQuotation;

public class TestOfBusAutoPickingInProgram {

    public ChromeDriver driver;

    QuotationAppCommonCode quotationAppCommonCode = new QuotationAppCommonCode();
    private SoftAssertions softAssertions;

    @Before
    public void setUp() {

        driver = quotationAppCommonCode.InitializeChromeDriver();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void Test1() {

        boolean isWindows=false;
        if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){isWindows=true;}
        WebDriverRunner.setWebDriver(driver);

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

        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;
        int popUpTimeOut = (int) (selenideConfig.timeout / 1000);
        System.out.print("[-] Открываем URL: "+props.getProperty("baseURL"));
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
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open(props.getProperty("baseURL")+"/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        quotationAppCommonCode.WaitForPageToLoad(driver);
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(QuotationAppCommonCode.OK);

        //Создаём новый Quotation
        CreateQuotation("PTestQuotation1", "Тест компания");
        //NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем валюту в USD
        OptionsTable.SetCurrencyInOptions("USD");

        //Выставляем курс доллара 60
        OptionsTable.SetCurrencyRateForUSD(60.0);

        //Меняем колличество ночей на 1
        final int nightNumber = 1;
        OptionsTable.SetNumberOfNightsInOptions(nightNumber);

        //Выставляем Present Meal Services = FB
        System.out.print("[-] Выставляем Present Meal Services = FB ");
        $(By.cssSelector(NewQuotationPage.OptionsTable.presentMealServices)).selectOptionContainingText("FB");
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        System.out.print("[-] Удаляем группу - 15 человек:");
        $(By.xpath(GroupsTable.GetGroupByNumberDeleteButtonREG(1))).scrollTo().click();
        Alert alert = (new WebDriverWait(driver, popUpTimeOut))
                .until(ExpectedConditions.alertIsPresent());
        confirm();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        System.out.print("[-] Удаляем группу - 20 человек:");
        $(By.xpath(GroupsTable.GetGroupByNumberDeleteButtonREG(1))).scrollTo().click();
        alert = (new WebDriverWait(driver, popUpTimeOut))
                .until(ExpectedConditions.alertIsPresent());
        confirm();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        int[] groupsToAdd = {45, 46, 52, 57, 58, 78, 88, 89, 104, 114, 115, 124, 132, 133, 155, 171, 172, 174, 176, 193, 220, 228, 229, 333, 464};

        //Добавляем группы из массива groupsToAdd
        for(int groupsCounter=0; groupsCounter<groupsToAdd.length; groupsCounter++){

            System.out.print("[-] Добавляем новую группу - "+groupsToAdd[groupsCounter]+" человек:");
            $(By.xpath(GroupsTable.groupsAddButtonREG)).scrollTo().click();
            alert = (new WebDriverWait(driver, popUpTimeOut))
                    .until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().sendKeys(String.valueOf(groupsToAdd[groupsCounter]));
            confirm();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            System.out.println(QuotationAppCommonCode.OK);
        }

        //Добавляем новую дату, дата берётся "сегодня"
        //Получаем текущую дату
        Date nowDate = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print("[-] Добавляем новую дату: " + formatForDateNow.format(nowDate));
        //Кликаем на кнопку Add
        $(By.cssSelector(DatesPeriodsTable.addDateButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).click();

        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).setValue(formatForDateNow.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(DatesPeriodsTable.saveDateButton)).click();
        System.out.println(QuotationAppCommonCode.OK);

        //Добавляем новый Город MSK
        AddCityToAccomodationByName("MSK", 1);

        //Добавить в первый день экскурсию Bunker-42
        System.out.print("[-] В первый день добавляем экскурсию \"Бункер-42\":");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.addServiceButtonREG)).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[3]//select[@class=\"serviceType\"]")).selectOptionContainingText("Excursion");
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[4]//select[@class=\"serviceName\"]")).selectOptionContainingText("BUNKER-42");
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Открываем цены на транспорт у экскурсии
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetAutoServiceByNumberREG(2)
                + "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();

        System.out.println("[-] Проверяем что автобусы побобраны правильно:");
        //Получаем колличество групп в Programm
        int numberToCount = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetAutoServiceByNumberREG(2)
                + "//td[@class=\"featureds\"]//table//tbody//tr")).size();
        //System.out.println(numberToCount);
        int numberOfPeople;
        /*int numberOfBuses;*/
        final int bus1MaxLoad = 46;
        final int bus2MaxLoad = 57;
        int temp1bus1; int temp2bus1;
        int temp1bus2; int temp2bus2;
        String busType;
        String resBusType;
        for(int groupCounter=1; groupCounter <= numberToCount; groupCounter++){
            numberOfPeople = Integer.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                    + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetAutoServiceByNumberREG(2)
                    + "//td[@class=\"featureds\"]//table//tbody//tr["+String.valueOf(groupCounter)+"]"
                    + "//td")).scrollTo().getText());
            //System.out.println("    Проверяем для группы - "+numberOfPeople);

            temp1bus1 = numberOfPeople / bus1MaxLoad;
            temp1bus2 = numberOfPeople / bus2MaxLoad;
            temp2bus1 = bus1MaxLoad*temp1bus1 - numberOfPeople;
            temp2bus2 = bus2MaxLoad*temp1bus2 - numberOfPeople;
            /*if (temp1bus1==0){temp2bus1 = bus1MaxLoad - numberOfPeople;}
            if (temp1bus2==0){temp2bus2 = bus2MaxLoad - numberOfPeople;}*/
            if(temp2bus1<0){
                temp1bus1++;
                temp2bus1 = bus1MaxLoad*temp1bus1 - numberOfPeople;
            }
            if(temp2bus2<0){
                temp1bus2++;
                temp2bus2 = bus2MaxLoad*temp1bus2 - numberOfPeople;
            }

            busType="Bus-ДТР-46";
            if (temp1bus1 < temp1bus2 & temp2bus1>=0) { busType = "Bus-ДТР-46"; }
            if (temp1bus1 == temp1bus2) {
                if ((temp2bus1 > temp2bus2 | temp2bus1 < 0) & temp2bus2 >= 0) { busType = "Bus-КЭБМЕН-57"; }
                }
            if(temp1bus1 > temp1bus2 & temp2bus2>=0){ busType = "Bus-КЭБМЕН-57"; }

            if(numberOfPeople<=bus1MaxLoad){busType="Bus-ДТР-46";}
            if(bus1MaxLoad<numberOfPeople & numberOfPeople<=bus2MaxLoad){busType="Bus-КЭБМЕН-57";}

            //System.out.println(temp1bus1+" "+temp2bus1+" "+temp1bus2+" "+temp2bus2+" res - "+busType);


            /*numberOfBuses = Integer.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                    + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetAutoServiceByNumberREG(2)
                    + "//td[@class=\"featureds\"]//table//tbody//tr["+String.valueOf(groupCounter)+"]"
                    + "//td[2]")).scrollTo().getText());*/
            resBusType = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                    + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                    + ProgrammSection.GetAutoServiceByNumberREG(2)
                    + "//td[@class=\"featureds\"]//table//tbody//tr["+String.valueOf(groupCounter)+"]"
                    + "//td[3]//span//select[@class=\"transport\"]")).scrollTo().getSelectedText();
            //System.out.println("        К-во автобусов "+numberOfBuses+" тип автобуса "+busType+'\n');

            if(busType.equals(resBusType)) {
                System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Значения для группы "+numberOfPeople+" верное + "+ QuotationAppCommonCode.ANSI_RESET);
            }
            else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Значения для группы "+numberOfPeople+" неверное: "
                    + busType + " не равен " + resBusType + " -"+ QuotationAppCommonCode.ANSI_RESET);
                softAssertions.assertThat(busType)
                        .as("Check that bus autopicking for "+numberOfPeople+" is correct")
                        .isEqualTo(resBusType);
            }
        }

    }

    @After
    public void close() {

        driver.quit();
        softAssertions.assertAll();
    }

}
