package com.rfqDemoOltatravel.QuotationApp;

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

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.open;
import static com.rfqDemoOltatravel.QuotationApp.NewQuotationPage.*;

public class TestOfGroups {

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
    public void testOfGoups() {

        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;

        isWindows=false;
        if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){isWindows=true;}

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
        QuotationAppCommonCode.WaitForProgruzka();

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

        //Выставляем курс Евро - 70.0
        Double rubEur = 70.0;
        OptionsTable.SetCurrencyRateForEUR(rubEur);

        //Сохраняем значение комиссии за бронь в SPB
        System.out.print("[-] Сохраняем значение комиссии за бронь в SPB");
        Double registrationFeeForSPB = Double.valueOf($(By.cssSelector(OptionsTable.registrationFeeForSPB)).getText());
        System.out.println(QuotationAppCommonCode.OK);

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
        $(By.cssSelector(DatesPeriodsTable.addDateButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).click();
        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));

        //Вводим дату в поле
        $(By.cssSelector(DatesPeriodsTable.newDateInputField)).setValue(formatForDate.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(DatesPeriodsTable.saveDateButton)).click();
        System.out.println(QuotationAppCommonCode.OK);

        //Добавляем город
        AddCityToAccomodationByName("MSK", 1);

        //Добавляем группу из 35 человек
        System.out.println("[-] Добавляем новую группу - 35 человек:");
        $(By.xpath(GroupsTable.groupsAddButtonREG)).scrollTo().click();
        Alert alert = (new WebDriverWait(driver, 4))
                    .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().sendKeys("35");
        confirm();
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        //Проверяем что новая группа добавилась на 4-ю позицию
        if ($(By.xpath(groupsTableREG+"//tbody//tr[4]/td[@class=\"people\"]")).getText().equals("35")){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(groupsTableREG+"//tbody//tr[4]/td[@class=\"people\"]")).getText())
                    .as("Check that group is right place, Groups table")
                    .isEqualTo(String.valueOf("35"));
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Группа не на своём месте"+ QuotationAppCommonCode.ANSI_RESET);
        }

        //Проверяем что в секции Program группа также на своём месте
        System.out.println("[-] Проверяем позицию группы \"35\" в секции Program:");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                ProgrammSection.GetACityByNumberREG(1)+
                ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();
        if ($(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                ProgrammSection.GetACityByNumberREG(1)+
                ProgrammSection.GetMainServiceByNumberREG(1)
                +"//td[@class=\"featureds\"]//table//tbody//tr[4]/td[@class=\"people\"]")).getText().equals("35")){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                    ProgrammSection.GetACityByNumberREG(1)+
                    ProgrammSection.GetMainServiceByNumberREG(1)
                    +"//td[@class=\"featureds\"]//table//tbody//tr[4]/td[@class=\"people\"]")).getText())
                    .as("Check that group is right place, Program section")
                    .isEqualTo(String.valueOf("35"));
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Группа не на своём месте"+ QuotationAppCommonCode.ANSI_RESET);
        }
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                ProgrammSection.GetACityByNumberREG(1)+
                ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();

        //Проверяем что в результатах группа на всоём месте
        System.out.println("[-] Запускаем Расчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что группа 35 человек в 4-ом столбце в Results:");
        //Проверяем что новая группа добавилась в 4-й столбец
        if ($(By.xpath(ResultsSection.hotelsWOMTableREG+"//thead//th[5]")).scrollTo().getText().equals("35")){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(ResultsSection.hotelsWOMTableREG+"//thead//th[5]")).getText())
                    .as("Check that group is right place, Results section")
                    .isEqualTo(String.valueOf("35"));
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Группа не на своём месте"+ QuotationAppCommonCode.ANSI_RESET);
        }


        //Добавляем группу 18 человек
        System.out.println("[-] Добавляем новую группу - 18 человек:");
        $(By.xpath(GroupsTable.groupsAddButtonREG)).scrollTo().click();
        alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().sendKeys("18");
        confirm();
        QuotationAppCommonCode.WaitForProgruzkaSilent();

        //Проверяем что новая группа добавилась на 2-ю позицию
        if ($(By.xpath(groupsTableREG+"//tbody//tr[2]/td[@class=\"people\"]")).scrollTo().getText().equals("18")){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(groupsTableREG+"//tbody//tr[2]/td[@class=\"people\"]")).getText())
                    .as("Check that group is right place, Groups table")
                    .isEqualTo(String.valueOf("18"));
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Группа не на своём месте"+ QuotationAppCommonCode.ANSI_RESET);
        }

        //Проверяем что в секции Program группа также на своём месте
        System.out.println("[-] Проверяем позицию группы \"18\" в секции Program:");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                ProgrammSection.GetACityByNumberREG(1)+
                ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();
        if ($(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                ProgrammSection.GetACityByNumberREG(1)+
                ProgrammSection.GetMainServiceByNumberREG(1)
                +"//td[@class=\"featureds\"]//table//tbody//tr[2]/td[@class=\"people\"]")).scrollTo().getText().equals("18")){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                    ProgrammSection.GetACityByNumberREG(1)+
                    ProgrammSection.GetMainServiceByNumberREG(1)
                    +"//td[@class=\"featureds\"]//table//tbody//tr[2]/td[@class=\"people\"]")).getText())
                    .as("Check that group is right place, Program section")
                    .isEqualTo(String.valueOf("18"));
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Группа не на своём месте"+ QuotationAppCommonCode.ANSI_RESET);
        }
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                ProgrammSection.GetACityByNumberREG(1)+
                ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();

        //Проверяем что в результатах группа 18 на всоём месте
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что группа 18 человек в 2-ом столбце в Results:");
        //Проверяем что новая группа добавилась в 4-й столбец
        if ($(By.xpath(ResultsSection.hotelsWOMTableREG+"//thead//th[3]")).scrollTo().getText().equals("18")){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Группа добавлена на своё место "+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(ResultsSection.hotelsWOMTableREG+"//thead//th[3]")).getText())
                    .as("Check that group is right place, Results section")
                    .isEqualTo(String.valueOf("18"));
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Группа не на своём месте"+ QuotationAppCommonCode.ANSI_RESET);
        }


        //Проверяем что можно удалить группу
        System.out.println("[-] Пробуем удалить группу - 18 человек:");
        $(By.xpath(GroupsTable.GetGroupByNumberDeleteButtonREG(2))).scrollTo().click();
        alert = (new WebDriverWait(driver, 4))
                .until(ExpectedConditions.alertIsPresent());
        confirm();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        //Проверяем что теперь на 2-ой позиции группа 20
        if ($(By.xpath(groupsTableREG+"//tbody//tr[2]/td[@class=\"people\"]")).scrollTo().getText().equals("20")){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Группа удалена успешно "+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(groupsTableREG+"//tbody//tr[2]/td[@class=\"people\"]")).getText())
                    .as("Check that group was deleted, Groups table")
                    .isEqualTo(String.valueOf("20"));
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Группа не удалилась корректно"+ QuotationAppCommonCode.ANSI_RESET);
        }

        //Проверяем что гркппа удалилась из Program
        System.out.println("[-] Проверяем, что группа удалена из секции Program:");
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                ProgrammSection.GetACityByNumberREG(1)+
                ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();
        if ($(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                ProgrammSection.GetACityByNumberREG(1)+
                ProgrammSection.GetMainServiceByNumberREG(1)
                +"//td[@class=\"featureds\"]//table//tbody//tr[2]/td[@class=\"people\"]")).scrollTo().getText().equals("20")){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Группа удалена корректно "+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                    ProgrammSection.GetACityByNumberREG(1)+
                    ProgrammSection.GetMainServiceByNumberREG(1)
                    +"//td[@class=\"featureds\"]//table//tbody//tr[2]/td[@class=\"people\"]")).getText())
                    .as("Check that group is right place, Program section")
                    .isEqualTo(String.valueOf("20"));
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Группа не удалена"+ QuotationAppCommonCode.ANSI_RESET);
        }
        $(By.xpath(ProgrammSection.GetADayByNumberREG(1)+
                ProgrammSection.GetACityByNumberREG(1)+
                ProgrammSection.GetMainServiceByNumberREG(1)
                +"//a[@class=\"qbtn qbtn-prices\"]")).scrollTo().click();

        //Проверяем что группа удалилась из Results
        System.out.println("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        QuotationAppCommonCode.WaitForProgruzka();
        System.out.println("[-] Проверяем что группа удалена из Results:");
        //Проверяем что новая группа 20 теперь вместо 18
        if ($(By.xpath(ResultsSection.hotelsWOMTableREG+"//thead//th[3]")).scrollTo().getText().equals("20")){
            System.out.println(QuotationAppCommonCode.ANSI_GREEN+"      Группа удалена корректно "+ QuotationAppCommonCode.ANSI_RESET);
            softAssertions.assertThat($(By.xpath(ResultsSection.hotelsWOMTableREG+"//thead//th[3]")).getText())
                    .as("Check that group is right place, Results section")
                    .isEqualTo(String.valueOf("20"));
        } else {
            System.out.println(QuotationAppCommonCode.ANSI_RED+"      Группа не удалена"+ QuotationAppCommonCode.ANSI_RESET);
        }

    }


    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }


}
