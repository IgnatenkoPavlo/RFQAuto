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
        System.out.println(" - готово");


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("test");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(" - готово");

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем приложение Prices");
        open(props.getProperty("baseURL")+"/application/olta.prices");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем цены на гида
        System.out.print("[-] Открываем раздел цены на гида");
        $(By.cssSelector("li[id=\"guides\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Выбираем город - MSK
        System.out.print("[-] Выбираем город - MSK");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-city\"] button[data-switch-value=\"MSK\"]")).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Выбираем год - 2017
        System.out.print("[-] Выбираем год - 2017");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-year\"] button[data-switch-value=\"2017\"]")).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Выбираем язык - English
        System.out.print("[-] Выбираем язык - English");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-lang\"] button[data-switch-value=\"English\"]")).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        System.out.print("[-] Сохраняем цену для 1/2 day (4 hours)");
        String guidPriceForEnglish = $(By.xpath("//div[@id=\"content\"]//table[@id=\"service-prices\"]//tbody//tr//td//a[contains(text(),'1/2 day (4 hours)')]//..//..//td[2]")).getText();
        //System.out.println(guidPriceForEnglish);
        System.out.println(" - готово");

        //Выбираем язык - English
        System.out.print("[-] Выбираем язык - Russian");
        $(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-lang\"] button[data-switch-value=\"Russian\"]")).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        System.out.print("[-] Сохраняем цену для 1/2 day (4 hours)");
        String guidPriceForRussian = $(By.xpath("//div[@id=\"content\"]//table[@id=\"service-prices\"]//tbody//tr//td//a[contains(text(),'1/2 day (4 hours)')]//..//..//td[2]")).getText();
        //System.out.println(guidPriceForRussian);
        System.out.println(" - готово");

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open(props.getProperty("baseURL")+"/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(" - Готово");

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(" - Готово");

        //Создаём новый Quotation
        NewQuotationPage.CreateQuotation("PTestQuotation1", "Тест компания");
        NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем курс Евро
        System.out.print("[-] Выставляем курс евро 70");
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");
        Double rubEur = 0.0;
        rubEur = Double.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText());

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 2;
        System.out.print("[-] Меняем количество ночей на " + nightInOptionsCounter+ ": ");
        NewQuotationPage.OptionsTable.SetNumberOfNights(nightInOptionsCounter);
        System.out.println(" - готово");

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(NewQuotationPage.OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(" - готово");

        //Добавляем новую дату, дата берётся "сегодня"
        //Получаем текущую дату
        Date nowDate = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print("[-] Добавляем новую дату: " + formatForDateNow.format(nowDate));
        //Кликаем на кнопку Add
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.addDateButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).click();

        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).setValue(formatForDateNow.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.saveDateButton)).click();
        System.out.println(" - Готово");

        //Добавляем новый Город MSK
        System.out.print("[-] Добавляем город: MSK");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).shouldBe(visible);
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).click();
        System.out.println(" - Готово");

        //Добавить в первый день экскурсию Bunker-42
        System.out.print("[-] В первый день добавляем экскурсию \"Бункер-42\":");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                +NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + NewQuotationPage.ProgrammSection.addServiceButtonREG)).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();

        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[3]//select[@class=\"serviceType\"]")).selectOptionContainingText("Excursion");
        CommonCode.WaitForProgruzkaSilent();

        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(4)
                + "//td[4]//select[@class=\"serviceName\"]")).selectOptionContainingText("Bunker-42");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Получаем цену которая выставилась для гида
        System.out.print("[-] Получаем цену которая выставилась для гида");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

        String priceForGuideInProgram = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(1))).getText();
        System.out.println(" - Готово");

        //Проверяем, что цена гида корректная
        System.out.print("[-] Проверяем, что цена гида корректная:");
        if(priceForGuideInProgram.equals(guidPriceForEnglish)) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Цена для гида English верная +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Цена для гида English неверная - "
                + "" + priceForGuideInProgram + ", а ожадалось " + guidPriceForEnglish + " -"+commonCode.ANSI_RESET);
            softAssertions.assertThat(priceForGuideInProgram)
                    .as("Check that price for english guide is correct")
                    .isEqualTo(guidPriceForEnglish);
        }

        //Меняем язык гида на Russian
        System.out.print("[-] Меняем язык гида на Russian");
        $(By.cssSelector(NewQuotationPage.OptionsTable.guidesLanguage)).scrollTo().selectOptionContainingText("Russian");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Получаем новую цену для гида
        System.out.print("[-] Получаем цену которая выставилась для гида");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

        priceForGuideInProgram = $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(2)
                + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(1))).scrollTo().getText();
        System.out.println(" - Готово");

        //Проверяем, что новая цена гида корректная
        System.out.print("[-] Проверяем, что новая цена гида корректная:");
        if(priceForGuideInProgram.equals(guidPriceForRussian)) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Цена для гида Russian верная +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Цена для гида Russian неверная - "
                + "" + priceForGuideInProgram + ", а ожадалось " + guidPriceForRussian + " -"+commonCode.ANSI_RESET);
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
