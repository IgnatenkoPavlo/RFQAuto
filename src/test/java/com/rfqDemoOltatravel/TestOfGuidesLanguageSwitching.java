package com.rfqDemoOltatravel;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.rfqDemoOltatravel.NewQuotationPage.*;

public class TestOfGuidesLanguageSwitching {

    public ChromeDriver driver;

    CommonCode commonCode = new CommonCode();
    private SoftAssertions softAssertions;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Automation\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void testOfGuidesLanguageSwitching() {

        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;

        Properties props=new Properties();
        try {
            props.load(new InputStreamReader(new FileInputStream("target\\test-classes\\application.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Получил проперти ");
        System.out.println(props.getProperty("baseURL"));
        System.out.print("[-] Открываем URL: "+props.getProperty("baseURL"));
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
        CommonCode.WaitForProgruzkaSilent();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем приложение Prices");
        open(props.getProperty("baseURL")+"/application/olta.prices");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Открываем цены на гида
        System.out.print("[-] Открываем раздел цены на гида");
        $(By.cssSelector("li[id=\"guides\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выбираем город - MSK
        System.out.print("[-] Выбираем город - MSK");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-city\"] button[data-switch-value=\"MSK\"]")).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выбираем год - 2017
        System.out.print("[-] Выбираем год - 2017");
        $(By.xpath("//div[@id=\"title-bar\"]//div[@id=\"switch-year\"]//button[text()=\"2017\"]")).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выбираем язык - English
        System.out.print("[-] Выбираем язык - English");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-lang\"] button[data-switch-value=\"English\"]")).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        System.out.print("[-] Сохраняем цену для 1/2 day (4 hours)");
        String guidPriceForEnglish = $(By.xpath("//div[@id=\"content\"]//table[@id=\"service-prices\"]//tbody//tr//td//a[contains(text(),'1/2 day (4 hours)')]//..//..//td[2]")).getText();
        //System.out.println(guidPriceForEnglish);
        System.out.println(CommonCode.OK);

        //Выбираем язык - English
        System.out.print("[-] Выбираем язык - Russian");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-lang\"] button[data-switch-value=\"Russian\"]")).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        System.out.print("[-] Сохраняем цену для 1/2 day (4 hours)");
        String guidPriceForRussian = $(By.xpath("//div[@id=\"content\"]//table[@id=\"service-prices\"]//tbody//tr//td//a[contains(text(),'1/2 day (4 hours)')]//..//..//td[2]")).getText();
        //System.out.println(guidPriceForRussian);
        System.out.println(CommonCode.OK);

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open(props.getProperty("baseURL")+"/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(CommonCode.OK);

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(CommonCode.OK);

        //Создаём новый Quotation
        CreateQuotation("PTestQuotation1", "Тест компания");
        //NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем курс Евро - 70.0
        Double rubEur = 70.0;
        OptionsTable.SetCurrencyRateForEUR(rubEur);

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 2;
        OptionsTable.SetNumberOfNightsInOptions(nightInOptionsCounter);

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(CommonCode.OK);

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
        System.out.println(CommonCode.OK);

        //Добавляем новый Город MSK
        AddCityToAccomodationByName("MSK", 1);

        //Добавить в первый день экскурсию Bunker-42
        System.out.print("[-] В первый день добавляем экскурсию \"Бункер-42\":");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.addServiceButtonREG)).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[3]//select[@class=\"serviceType\"]")).selectOptionContainingText("Excursion");
        CommonCode.WaitForProgruzkaSilent();

        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[4]//select[@class=\"serviceName\"]")).selectOptionContainingText("Bunker-42");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Получаем цену которая выставилась для гида
        System.out.print("[-] Получаем цену которая выставилась для гида");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1) + ProgrammSection.GetACityByNumberREG(1)
                + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

        String priceForGuideInProgram = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetAutoServiceByNumberREG(1)
                + ProgrammSection.GetSumForPeopleREG(1))).getText();
        System.out.println(CommonCode.OK);

        //Проверяем, что цена гида корректная
        System.out.print("[-] Проверяем, что цена гида корректная:");
        if(priceForGuideInProgram.equals(guidPriceForEnglish)) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Цена для гида English верная +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Цена для гида English неверная - "
                + "" + priceForGuideInProgram + ", а ожадалось " + guidPriceForEnglish + " -"+CommonCode.ANSI_RESET);
            softAssertions.assertThat(priceForGuideInProgram)
                    .as("Check that price for english guide is correct")
                    .isEqualTo(guidPriceForEnglish);
        }

        //Меняем язык гида на Russian
        System.out.print("[-] Меняем язык гида на Russian");
        $(By.cssSelector(OptionsTable.guidesLanguage)).scrollTo().selectOptionContainingText("Russian");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Получаем новую цену для гида
        System.out.print("[-] Получаем цену которая выставилась для гида");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1) + ProgrammSection.GetACityByNumberREG(1)
                + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

        priceForGuideInProgram = $(By.xpath(ProgrammSection.GetADayByNumberREG(1)
                + ProgrammSection.GetACityByNumberREG(1)
                + ProgrammSection.GetAutoServiceByNumberREG(2)
                + ProgrammSection.GetSumForPeopleREG(1))).scrollTo().getText();
        System.out.println(CommonCode.OK);

        //Проверяем, что новая цена гида корректная
        System.out.print("[-] Проверяем, что новая цена гида корректная:");
        if(priceForGuideInProgram.equals(guidPriceForRussian)) {
            System.out.println(CommonCode.ANSI_GREEN+"      -  Цена для гида Russian верная +"+CommonCode.ANSI_RESET);
        }
        else {System.out.println(CommonCode.ANSI_RED+"      -  Цена для гида Russian неверная - "
                + "" + priceForGuideInProgram + ", а ожадалось " + guidPriceForRussian + " -"+CommonCode.ANSI_RESET);
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
