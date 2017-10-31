package com.rfqDemoOltatravel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestOfGroups {

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
    public void testOfGoups() {

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
        System.out.println(" - Готово");


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("test");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(" - Готово");

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        commonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open(props.getProperty("baseURL")+"/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).shouldBe(visible);
        System.out.println(" - Готово");

        //Создаём новый Quotation
        NewQuotationPage.CreateQuotation("PTestQuotation1", "Тест компания");
        NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Выставляем курс Евро
        System.out.println("[-] Выставляем курс евро 70");
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
        CommonCode.WaitForProgruzkaSilent();
        Double rubEur = 0.0;
        rubEur = Double.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText());

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
        System.out.println(" - Готово");

        //Добавляем город
        System.out.print("[-] Добавляем город: MSK");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).shouldBe(visible);
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).shouldBe(visible);
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).click();
        System.out.println(" - Готово");

        //Добавляем группу из 35 человек
        System.out.println("[-] Добавляем новую группу - 35 человек:");
        $(By.xpath(NewQuotationPage.GroupsTable.groupsAddButtonREG)).scrollTo().click();
        Alert alert = (new WebDriverWait(driver, 4))
                    .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().sendKeys("35");
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();

        //Проверяем что новая группа добавилась на 4-ю позицию
        if ($(By.xpath(NewQuotationPage.groupsTableREG+"//tbody//tr[4]/td[@class=\"people\"]")).getText().equals("35")){
            System.out.println(CommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+CommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(NewQuotationPage.groupsTableREG+"//tbody//tr[4]/td[@class=\"people\"]")).getText())
                    .as("Check that group is right place, Groups table")
                    .isEqualTo(String.valueOf("35"));
        } else {
            System.out.println(CommonCode.ANSI_RED+"      Группа не на своём месте"+ CommonCode.ANSI_RESET);
        }

        //Проверяем что в секции Program группа также на своём месте
        System.out.println("[-] Проверяем позицию группы \"35\" в секции Program:");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();
        if ($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                +"//td[@class=\"featureds\"]//table//tbody//tr[4]/td[@class=\"people\"]")).getText().equals("35")){
            System.out.println(CommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+CommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                    NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                    NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                    +"//td[@class=\"featureds\"]//table//tbody//tr[4]/td[@class=\"people\"]")).getText())
                    .as("Check that group is right place, Program section")
                    .isEqualTo(String.valueOf("35"));
        } else {
            System.out.println(CommonCode.ANSI_RED+"      Группа не на своём месте"+ CommonCode.ANSI_RESET);
        }
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();

        //Проверяем что в результатах группа на всоём месте
        System.out.println("[-] Запускаем Расчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что группа 35 человек в 4-ом столбце в Results:");
        //Проверяем что новая группа добавилась в 4-й столбец
        if ($(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//thead//th[5]")).scrollTo().getText().equals("35")){
            System.out.println(CommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+CommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//thead//th[5]")).getText())
                    .as("Check that group is right place, Results section")
                    .isEqualTo(String.valueOf("35"));
        } else {
            System.out.println(CommonCode.ANSI_RED+"      Группа не на своём месте"+ CommonCode.ANSI_RESET);
        }


        //Добавляем группу 18 человек
        System.out.println("[-] Добавляем новую группу - 18 человек:");
        $(By.xpath(NewQuotationPage.GroupsTable.groupsAddButtonREG)).scrollTo().click();
        alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().sendKeys("18");
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();

        //Проверяем что новая группа добавилась на 2-ю позицию
        if ($(By.xpath(NewQuotationPage.groupsTableREG+"//tbody//tr[2]/td[@class=\"people\"]")).scrollTo().getText().equals("18")){
            System.out.println(CommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+CommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(NewQuotationPage.groupsTableREG+"//tbody//tr[2]/td[@class=\"people\"]")).getText())
                    .as("Check that group is right place, Groups table")
                    .isEqualTo(String.valueOf("18"));
        } else {
            System.out.println(CommonCode.ANSI_RED+"      Группа не на своём месте"+ CommonCode.ANSI_RESET);
        }

        //Проверяем что в секции Program группа также на своём месте
        System.out.println("[-] Проверяем позицию группы \"18\" в секции Program:");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();
        if ($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                +"//td[@class=\"featureds\"]//table//tbody//tr[2]/td[@class=\"people\"]")).scrollTo().getText().equals("18")){
            System.out.println(CommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+CommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                    NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                    NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                    +"//td[@class=\"featureds\"]//table//tbody//tr[2]/td[@class=\"people\"]")).getText())
                    .as("Check that group is right place, Program section")
                    .isEqualTo(String.valueOf("18"));
        } else {
            System.out.println(CommonCode.ANSI_RED+"      Группа не на своём месте"+ CommonCode.ANSI_RESET);
        }
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();

        //Проверяем что в результатах группа 18 на всоём месте
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что группа 18 человек в 2-ом столбце в Results:");
        //Проверяем что новая группа добавилась в 4-й столбец
        if ($(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//thead//th[3]")).scrollTo().getText().equals("18")){
            System.out.println(CommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+CommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//thead//th[3]")).getText())
                    .as("Check that group is right place, Results section")
                    .isEqualTo(String.valueOf("18"));
        } else {
            System.out.println(CommonCode.ANSI_RED+"      Группа не на своём месте"+ CommonCode.ANSI_RESET);
        }


        //Проверяем что можно удалить группу
        System.out.println("[-] Пробуем удалить группу - 18 человек:");
        $(By.xpath(NewQuotationPage.GroupsTable.GetGroupByNumberDeleteButtonREG(2))).scrollTo().click();
        alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        CommonCode.WaitForProgruzkaSilent();
        //Проверяем что теперь на 2-ой позиции группа 20
        if ($(By.xpath(NewQuotationPage.groupsTableREG+"//tbody//tr[2]/td[@class=\"people\"]")).scrollTo().getText().equals("20")){
            System.out.println(CommonCode.ANSI_GREEN+"      Группа удалена успешно "+CommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(NewQuotationPage.groupsTableREG+"//tbody//tr[2]/td[@class=\"people\"]")).getText())
                    .as("Check that group was deleted, Groups table")
                    .isEqualTo(String.valueOf("20"));
        } else {
            System.out.println(CommonCode.ANSI_RED+"      Группа не удалилась корректно"+ CommonCode.ANSI_RESET);
        }

        //Проверяем что гркппа удалилась из Program
        System.out.println("[-] Проверяем, что группа удалена из секции Program:");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();
        if ($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                +"//td[@class=\"featureds\"]//table//tbody//tr[2]/td[@class=\"people\"]")).scrollTo().getText().equals("20")){
            System.out.println(CommonCode.ANSI_GREEN+"      Группа удалена корректно "+CommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                    NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                    NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                    +"//td[@class=\"featureds\"]//table//tbody//tr[2]/td[@class=\"people\"]")).getText())
                    .as("Check that group is right place, Program section")
                    .isEqualTo(String.valueOf("20"));
        } else {
            System.out.println(CommonCode.ANSI_RED+"      Группа не удалена"+ CommonCode.ANSI_RESET);
        }
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)+
                NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();

        //Проверяем что группа удалилась из Results
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что группа удалена из Results:");
        //Проверяем что новая группа 20 теперь вместо 18
        if ($(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//thead//th[3]")).scrollTo().getText().equals("20")){
            System.out.println(CommonCode.ANSI_GREEN+"      Группа удалена корректно "+CommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath("//div[@id=\"results\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//thead//th[3]")).getText())
                    .as("Check that group is right place, Results section")
                    .isEqualTo(String.valueOf("20"));
        } else {
            System.out.println(CommonCode.ANSI_RED+"      Группа не удалена"+ CommonCode.ANSI_RESET);
        }

    }


    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }


}
