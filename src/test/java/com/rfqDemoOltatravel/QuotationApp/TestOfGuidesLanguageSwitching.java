package com.rfqDemoOltatravel.QuotationApp;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.rfqDemoOltatravel.RFQApp.RFQAppCommonCode;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.rfqDemoOltatravel.QuotationApp.NewQuotationPage.*;

public class TestOfGuidesLanguageSwitching {

    public ChromeDriver driver;

    QuotationAppCommonCode quotationAppCommonCode = new QuotationAppCommonCode();
    private SoftAssertions softAssertions;
    boolean isWindows=false;

    @Before
    public void setUp() {

        driver = quotationAppCommonCode.InitializeChromeDriver();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void testOfGuidesLanguageSwitching() {

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
        System.out.print("[-] Открываем приложение Prices");
        open(props.getProperty("baseURL")+"/application/olta.prices");
        //Ждём пока загрузится страница и проподёт "Loading..."
        quotationAppCommonCode.WaitForPageToLoad(driver);
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Открываем цены на гида
        System.out.print("[-] Открываем раздел цены на гида");
        $(By.cssSelector("li[id=\"guides\"]")).click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Выбираем город - MSK
        System.out.print("[-] Выбираем город - MSK");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-city\"] button[data-switch-value=\"MSK\"]")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Выбираем год - 2018
        System.out.print("[-] Выбираем год - 2018");
        $(By.xpath("//div[@id=\"title-bar\"]//div[@id=\"switch-year\"]//button[text()=\"2018\"]")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Выбираем язык - English
        System.out.print("[-] Выбираем язык - English");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-lang\"] button[data-switch-value=\"English\"]")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        System.out.print("[-] Сохраняем цену для 1/2 day (4 hours)");
        String guidPriceForEnglish = $(By.xpath("//div[@id=\"content\"]//table[@id=\"service-prices\"]//tbody//tr//td//a[contains(text(),'1/2 DAY (4 HOURS)')]//..//..//td[3]")).getText();
        //System.out.println(guidPriceForEnglish);
        System.out.println(QuotationAppCommonCode.OK);

        //Выбираем язык - English
        System.out.print("[-] Выбираем язык - Russian");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-lang\"] button[data-switch-value=\"Russian\"]")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        System.out.print("[-] Сохраняем цену для 1/2 day (4 hours)");
        String guidPriceForRussian = $(By.xpath("//div[@id=\"content\"]//table[@id=\"service-prices\"]//tbody//tr//td//a[contains(text(),'1/2 DAY (4 HOURS)')]//..//..//td[3]")).getText();
        //System.out.println(guidPriceForRussian);
        System.out.println(QuotationAppCommonCode.OK);

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open(props.getProperty("baseURL")+"/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        quotationAppCommonCode.WaitForPageToLoad(driver);
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(QuotationAppCommonCode.OK);

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(QuotationAppCommonCode.OK);

        //Создаём новый Quotation
        CreateQuotation("PTestQuotation1", "Тест компания");
        //NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем курс Евро - 70.0
        Double rubEur = 70.0;
        OptionsTable.SetCurrencyRateForEUR(rubEur);

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 2;
        OptionsTable.SetNumberOfNightsInOptions(nightInOptionsCounter);

        //Сохраняем значение комиссии за бронь в SPB
        System.out.print("[-] Сохраняем значение комиссии за бронь в SPB");
        Double registrationFeeForSPB = Double.valueOf($(By.cssSelector(OptionsTable.registrationFeeForSPB)).getText());
        System.out.println(QuotationAppCommonCode.OK);

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(QuotationAppCommonCode.OK);

        //Выставляем Present Meal Services = FB
        System.out.print("[-] Выставляем Present Meal Services = FB ");
        $(By.cssSelector(NewQuotationPage.OptionsTable.presentMealServices)).selectOptionContainingText("FB");
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

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

        //Получаем цену которая выставилась для гида
        System.out.print("[-] Получаем цену которая выставилась для гида");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1) + ProgrammSection.GetACityByNumberREG(1)
                + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

        String priceForGuideInProgram = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetAutoServiceByNumberREG(1)
                + ProgrammSection.GetSumForPeopleREG(1))).getText();
        System.out.println(QuotationAppCommonCode.OK);

        //Проверяем, что цена гида корректная
        System.out.print("[-] Проверяем, что цена гида корректная:");
        if(priceForGuideInProgram.equals(guidPriceForEnglish)) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Цена для гида English верная +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Цена для гида English неверная - "
                + "" + priceForGuideInProgram + ", а ожадалось " + guidPriceForEnglish + " -"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(priceForGuideInProgram)
                    .as("Check that price for english guide is correct")
                    .isEqualTo(guidPriceForEnglish);
        }

        //Меняем язык гида на Russian
        System.out.print("[-] Меняем язык гида на Russian");
        $(By.cssSelector(OptionsTable.guidesLanguage)).scrollTo().selectOptionContainingText("Russian");
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(QuotationAppCommonCode.OK);

        //Получаем новую цену для гида
        System.out.print("[-] Получаем цену которая выставилась для гида");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1) + ProgrammSection.GetACityByNumberREG(1)
                + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

        priceForGuideInProgram = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetAutoServiceByNumberREG(2)
                + ProgrammSection.GetSumForPeopleREG(1))).scrollTo().getText();
        System.out.println(QuotationAppCommonCode.OK);

        //Проверяем, что новая цена гида корректная
        System.out.print("[-] Проверяем, что новая цена гида корректная:");
        if(priceForGuideInProgram.equals(guidPriceForRussian)) {
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      -  Цена для гида Russian верная +"+ QuotationAppCommonCode.ANSI_RESET);
        }
        else {System.out.println(QuotationAppCommonCode.ANSI_RED+"      -  Цена для гида Russian неверная - "
                + "" + priceForGuideInProgram + ", а ожадалось " + guidPriceForRussian + " -"+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat(priceForGuideInProgram)
                    .as("Check that price for english guide is correct")
                    .isEqualTo(guidPriceForRussian);
        }

    }


    @After
    public void close() {

        driver.quit();
        softAssertions.assertAll();
    }

}
