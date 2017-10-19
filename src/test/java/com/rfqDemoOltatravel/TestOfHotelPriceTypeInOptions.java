package com.rfqDemoOltatravel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class TestOfHotelPriceTypeInOptions {

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
    public void testOfHotelPriceTypeInOptions() {

        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;
        System.out.print("[-] Открываем URL: http://rfq-demo.oltatravel.com/");
        open("http://rfq-demo.oltatravel.com/");
        commonCode.WaitForPageToLoad(driver);
        System.out.println(" - готово");


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("pavel.sales");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(" - готово");

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        commonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open("http://rfq-demo.oltatravel.com/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(" - готово");

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).isDisplayed();
        System.out.println(" - готово");

        //Создаём новый Quotation
        System.out.println("[-] Создаём новый Quotation:");
        $(By.id("qbtn-create")).click();
        $(By.xpath(QuotationListPage.newQuotationPopapREG)).isDisplayed();
        $(By.xpath(QuotationListPage.newQuotationNameREG)).setValue("PTestQuotation1");
        System.out.println("      Имя - PTestQuotation1");
        $(By.xpath(QuotationListPage.newQuotationClientNameREG)).selectOptionContainingText("Тест компания");
        System.out.println("      Клиент - Тест компания");
        $(By.xpath(QuotationListPage.newQuotationPopapOkButtonREG)).click();

        NewQuotationPage newQuotationPage = new NewQuotationPage();

        //Ждём пока страница прогрузится
        commonCode.WaitForProgruzka();

        //Выставляем курс Евро
        System.out.print("[-] Выставляем курс евро 70");
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");
        Double rubEur = 0.0;
        rubEur = Double.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText());

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 1;
        System.out.print("[-] Меняем количество ночей на " + nightInOptionsCounter+ ": ");
        NewQuotationPage.OptionsTable.SetNumberOfNights(nightInOptionsCounter);
        System.out.println(" - готово");

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(NewQuotationPage.OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(" - готово");

        //Выставляем дату
        Instant nowDate = Instant.now();
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        System.out.print("[-] Добавляем новую дату: " + formatForDate.format(nowDate));
        //Кликаем на кнопку Add
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.addButton)).click();
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).click();
        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));

        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.newDateInputField)).setValue(formatForDate.format(nowDate));
        //Кликаем кнопку сохранить
        $(By.cssSelector(NewQuotationPage.DatesPeriodsTable.saveButton)).click();
        System.out.println(" - готово");

        //Добавляем город
        System.out.print("[-] Добавляем город: MSK");
        //Кликаем Add
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(newQuotationPage.cityAddPopupREG)).isDisplayed();
        //Кликаем по кнопке с MSK
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).isDisplayed();
        $(By.xpath(newQuotationPage.GetCityNameButtonREG("MSK"))).click();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Проганяем стили css для контрола Hotel Price Type
        $(By.cssSelector(NewQuotationPage.OptionsTable.hotelPriceType)).scrollTo().selectOptionContainingText("Individual");
        commonCode.WaitForProgruzkaSilent();
        $(By.cssSelector(NewQuotationPage.OptionsTable.hotelPriceType)).scrollTo().selectOptionContainingText("Group");
        commonCode.WaitForProgruzkaSilent();

        //Проверяем, что Hotel Price Type стоит DBL
        System.out.println("[-] Проверяем, что Hotel Price Type стоит DBL:");
        String result = $(By.cssSelector(NewQuotationPage.OptionsTable.hotelPriceType)).scrollTo().getSelectedText();
        if (result.equals("Group")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Hotel Price Type in Options set as Group")
                    .isEqualTo(String.valueOf("Group"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что заголовок таблицы Accommodations корректный
        System.out.println("[-] Проверяем, что заголовок таблицы Accommodations корректный:");
        result = $(By.xpath(NewQuotationPage.accomodationsTableREG + "//thead//th[@class=\"visibility-group\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: table-cell;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo("display: table-cell;");
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что заголовок таблицы Accommodations для Individual скрыт
        System.out.println("[-] Проверяем, что заголовок таблицы Accommodations для Individual скрыт:");
        result = $(By.xpath(NewQuotationPage.accomodationsTableREG + "//thead//th[@class=\"visibility-individual\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: none;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo(String.valueOf("display: none;"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что дропдаун Hotel Type активен для Group
        System.out.println("[-] Проверяем, что дропдаун Hotel Type для Group:");
        result = $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotelType visibility-group\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: table-cell;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo("display: table-cell;");
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что дропдаун Hotel Type активен для Individual
        System.out.println("[-] Проверяем, что дропдаун Hotel Type для Individual скрыт:");
        result = $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotel visibility-individual\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: none;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo(String.valueOf("display: none;"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проставляем Hotel Price Type в Individual
        System.out.print("[-] Проставляем Hotel Price Type в Individual");
        $(By.cssSelector(NewQuotationPage.OptionsTable.hotelPriceType)).scrollTo().selectOptionContainingText("Individual");
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Проверяем, что заголовок таблицы Accommodations корректный
        System.out.println("[-] Проверяем, что заголовок таблицы Accommodations корректный:");
        result = $(By.xpath(NewQuotationPage.accomodationsTableREG + "//thead//th[@class=\"visibility-group\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: none;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo("display: none;");
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что заголовок таблицы Accommodations для Individual скрыт
        System.out.println("[-] Проверяем, что заголовок таблицы Accommodations для Individual скрыт:");
        result = $(By.xpath(NewQuotationPage.accomodationsTableREG + "//thead//th[@class=\"visibility-individual\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: table-cell;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo(String.valueOf("display: table-cell;"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что дропдаун Hotel Type активен для Group
        System.out.println("[-] Проверяем, что дропдаун Hotel Type для Group:");
        result = $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotelType visibility-group\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: none;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo("display: none;");
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что дропдаун Hotel Type активен для Individual
        System.out.println("[-] Проверяем, что дропдаун Hotel Type для Individual скрыт:");
        result = $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1) + "//td[@class=\"hotel visibility-individual\"]")).scrollTo().getAttribute("style");
        if (result.equals("display: table-cell;")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(result)
                    .as("Check that Heads in Accommodations are correct")
                    .isEqualTo(String.valueOf("display: table-cell;"));
            System.out.println(CommonCode.ANSI_RED +"      Значение не некорректные: " + CommonCode.ANSI_RESET
                    + result + " -");
        }

        //Проверяем, что можно выставить отель
        System.out.print("[-] Проверяем, что можно выставить отел");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                + "//td[@class=\"hotel visibility-individual\"]"
                + "//select[@class=\"hotel\"]")).selectOptionContainingText("PTestHotel1");
        commonCode.WaitForProgruzkaSilent();
        System.out.println(commonCode.ANSI_GREEN+" - готово"+commonCode.ANSI_RESET);

        //Проверяем, что можно выставить номер
        System.out.print("[-] Проверяем, что можно выставить тип номера");
        $(By.xpath(NewQuotationPage.AccomodationsTable.CityByNumberREG(1)
                + "//td[@class=\"hotel visibility-individual\"]"
                + "//select[@class=\"roomCategory\"]")).selectOptionContainingText("Standard Single");
        commonCode.WaitForProgruzkaSilent();
        System.out.println(commonCode.ANSI_GREEN+" - готово"+commonCode.ANSI_RESET);

        String priceDBL = "";
        String priceSGL = "";
        Double priceSGLD = 0.0;
        Double priceDBLD = 0.0;
        $(By.xpath("//table[@id=\"table-accommodations\"]//tbody//tr[1]//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-toggleprices\"]")).click();

        $(By.xpath("//table[@id=\"table-accommodations\"]//tbody//tr[1]//table[@class=\"prices\"]//tbody//tr//a[@class=\"qbtn qbtn-prices\"]")).click();

        //Ждём появления модального окна с ценами отеля
        $(By.cssSelector("div[id=\"modal-dialog\"]")).isDisplayed();
        //Сохраняем сумму дабл в переменную
        priceSGL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[2]")).getText();
        priceDBL = $(By.xpath("//div[@id=\"modal-accommodation-days-prices\"]//div[@class=\"modal-body\"]/table/tfoot/tr[1]/th[3]")).getText();
        //System.out.println(priceDBL);
        priceSGLD = priceSGLD + Double.valueOf(priceSGL);
        priceDBLD = priceDBLD + Double.valueOf(priceDBL);
        //System.out.println("priceDBLD = "+priceDBLD);
        //Закрываем модальное окно
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).click();
        $(By.cssSelector("div[id=\"modal-accommodation-days-prices\"] button[class=\"btn btn-primary\"]")).shouldNotBe(visible);
        $(By.xpath("//table[@id=\"table-accommodations\"]//tbody//tr[1]//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-toggleprices\"]")).click();

        Double priceSS = priceSGLD - priceDBLD/2;

        Double programServicesFor15 = 150.0;
        Double programServicesFor20 = 140.0;
        Double programServicesFor25 = 130.0;

        //Выставляем суммы для 3-х групп: 15, 20, 25
        System.out.println("[-] Выставляем суммы для 3-х групп: 15, 20, 25");
        int dayCounterMax = nightInOptionsCounter + 1;
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
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(1))).setValue(programServicesFor15.toString()).pressEnter();

                    //programServicesFor15 = programServicesFor15 + 5.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);



                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(2))).click();

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(2))).setValue(programServicesFor20.toString()).pressEnter();

                    //programServicesFor20 = programServicesFor20 + 6.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);


                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(3))).click();

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(3))).setValue(programServicesFor25.toString()).pressEnter();


                    //programServicesFor25 = programServicesFor25 + 7.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
                }

                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//tr//td//a[@class=\"qbtn qbtn-hideallprices\"]")).shouldBe(visible).click();

            }
            System.out.println(" - готово");
        }

        double programServices[] = {0.0, 0.0, 0.0};
        double programDailyServices[] = {0.0, 0.0, 0.0};

        Double programDailyServicesFor15 = 0.0;
        Double programDailyServicesFor20 = 0.0;
        Double programDailyServicesFor25 = 0.0;

        System.out.println("[-] Считаем суммы для 3-х групп: 15, 20, 25");
        System.out.println("[-] Считаем для Services:");
        commonCode.GetSumsForServices(programServices);
        System.out.println(" - Готово");
        System.out.println("[-] Считаем для Daily Services:");
        commonCode.GetSumsForDailyServices(programDailyServices);
        System.out.println(" - Готово");

        programServices[0] = programServices[0]/15.0;
        programServices[1] = programServices[1]/20.0;
        programServices[2] = programServices[2]/25.0;

        System.out.print("[-] Запускаем перерасчёт: ");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - готово");

        System.out.println("[-] Проверяем результаты расчёта.");

        System.out.println("    Проверяем таблицу Hotels (WE) w/o margin:");
        String hotelsWE15womS = $(By.cssSelector("table[id=\"table-result-hotels-wo-margin-we\"] tbody tr td")).getText();
        String hotelsWE20womS = $(By.xpath("//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr//td[2]")).getText();
        String hotelsWE25womS = $(By.xpath("//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr//td[3]")).getText();
        String hotelsWEwomSSS = $(By.xpath("//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr//td[4]")).getText();
        hotelsWE15womS = hotelsWE15womS.substring(0, hotelsWE15womS.indexOf(' '));
        hotelsWE20womS = hotelsWE20womS.substring(0, hotelsWE20womS.indexOf(' '));
        hotelsWE25womS = hotelsWE25womS.substring(0, hotelsWE25womS.indexOf(' '));
        hotelsWEwomSSS = hotelsWEwomSSS.substring(0, hotelsWEwomSSS.indexOf(' '));
        //System.out.println("hotelsWE 15 w/o marge: " + hotelsWE15womS);
        String priceDBLDS = String.valueOf((int) new BigDecimal(priceDBLD/2).setScale(0, RoundingMode.HALF_UP).floatValue());
        String priceDBLDS20 = String.valueOf((int) new BigDecimal(priceDBLD/2).setScale(0, RoundingMode.HALF_UP).floatValue());
        String priceDBLDS25 = String.valueOf((int) new BigDecimal(priceDBLD/2).setScale(0, RoundingMode.HALF_UP).floatValue());
        String priceSGLDS = String.valueOf((int) new BigDecimal(priceSS).setScale(0, RoundingMode.HALF_UP).floatValue());
        //Assert.assertEquals(priceDBLDS, hotelsWE15womS);
        if(priceDBLDS.equals(hotelsWE15womS)) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значения для группы 15 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения для группы 15 неверное: "
                + priceDBLDS + " не равен " + hotelsWE15womS + " -"+commonCode.ANSI_RESET);
            softAssertions.assertThat(priceDBLDS)
                    .as("Check that value in Hotels (WE) w/o margin for 15 is correct")
                    .isEqualTo(hotelsWE15womS);
        }
        if(priceDBLDS20.equals(hotelsWE20womS)) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значения для группы 20 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения для группы 20 неверное: "
                + priceDBLDS20 + " не равен " + hotelsWE20womS + " -"+commonCode.ANSI_RESET);
            softAssertions.assertThat(priceDBLDS20)
                    .as("Check that value in Hotels (WE) w/o margin for 20 is correct")
                    .isEqualTo(hotelsWE20womS);
        }
        if(priceDBLDS25.equals(hotelsWE25womS)) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значения для группы 25 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения для группы 25 неверное: "
                + priceDBLDS25 + " не равен " + hotelsWE25womS + " -"+commonCode.ANSI_RESET);
            softAssertions.assertThat(priceDBLDS25)
                    .as("Check that value in Hotels (WE) w/o margin for 25 is correct")
                    .isEqualTo(hotelsWE25womS);
        }
        if(priceSGLDS.equals(hotelsWEwomSSS)) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значения для SS верное + "+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения SS неверное: "
                + priceSGLDS + " не равен " + hotelsWEwomSSS + " -");
            softAssertions.assertThat(priceSGLDS)
                    .as("Check that value in Hotels (WE) w/o margin for SS is correct"+commonCode.ANSI_RESET)
                    .isEqualTo(hotelsWEwomSSS);
        }

        System.out.println("    Проверяем таблицу Hotels (WE):");
        Double hotelsWE15 = priceDBLD/2;
        hotelsWE15 = hotelsWE15 / rubEur;
        hotelsWE15 = hotelsWE15 / generalMarge;
        String hotelsWE15S = String.valueOf((int) new BigDecimal(hotelsWE15).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]")).scrollTo();
        String hotelsWER = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        hotelsWER = hotelsWER.substring(0, hotelsWER.indexOf('€'));//€
        if(hotelsWE15S.equals(hotelsWER)) {
            System.out.println(commonCode.ANSI_GREEN+"      - Значения для группы 15 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения для группы 15 неверное: "
                + hotelsWE15S + " не равен " + hotelsWER + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWE15S)
                    .as("Check that value in Hotels (WE) for 15 is correct")
                    .isEqualTo(hotelsWER);
        }

        Double hotelsWE20 = priceDBLD/2;
        hotelsWE20 = hotelsWE20 / rubEur;
        hotelsWE20 = hotelsWE20 / generalMarge;
        String hotelsWE20S = String.valueOf((int) new BigDecimal(hotelsWE20).setScale(0, RoundingMode.HALF_UP).floatValue());
        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]")).scrollTo();
        hotelsWER = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]//tbody//tr//th/following-sibling::td[2]")).getText();
        hotelsWER = hotelsWER.substring(0, hotelsWER.indexOf('€'));//€
        if(hotelsWE20S.equals(hotelsWER)) {
            System.out.println(commonCode.ANSI_GREEN+"      - Значения для группы 20 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения для группы 20 неверное: "
                + hotelsWE20S + " не равен " + hotelsWER + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWE20S)
                    .as("Check that value in Hotels (WE) for 20 is correct")
                    .isEqualTo(hotelsWER);
        }

        Double hotelsWE25 = priceDBLD/2;
        hotelsWE25 = hotelsWE25 / rubEur;
        hotelsWE25 = hotelsWE25 / generalMarge;
        String hotelsWE25S = String.valueOf((int) new BigDecimal(hotelsWE25).setScale(0, RoundingMode.HALF_UP).floatValue());
        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]")).scrollTo();
        hotelsWER = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]//tbody//tr//th/following-sibling::td[3]")).getText();
        hotelsWER = hotelsWER.substring(0, hotelsWER.indexOf('€'));//€
        if(hotelsWE25S.equals(hotelsWER)) {
            System.out.println(commonCode.ANSI_GREEN+"      - Значения для группы 25 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения для группы 25 неверное: "
                + hotelsWE25S + " не равен " + hotelsWER + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWE25S)
                    .as("Check that value in Hotels (WE) for 25 is correct")
                    .isEqualTo(hotelsWER);
        }

        Double hotelsWESS=0.0;
        hotelsWESS = priceSS;
        hotelsWESS = hotelsWESS / rubEur /generalMarge;
        String hotelsWESSS = String.valueOf((int) new BigDecimal(hotelsWESS).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]")).scrollTo();
        hotelsWER = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]//tbody//tr//th/following-sibling::td[4]")).getText();
        hotelsWER = hotelsWER.substring(0, hotelsWER.indexOf('€'));
        if(hotelsWESSS.equals(hotelsWER)) {
            System.out.println(commonCode.ANSI_GREEN+"      - Значения для SS верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      - Значения для SS неверное: "
                + hotelsWESSS + " не равен " + hotelsWER + " -"+commonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWESSS)
                    .as("Check that value in Hotels (WE) for SS is correct")
                    .isEqualTo(hotelsWER);
        }

        System.out.println("    Проверяем таблицу Services:");
        Double services15 = 0.0;
        services15 = programServices[0];
        services15 = services15 / rubEur;
        services15 = services15 / generalMarge;

        //System.out.println("Services WE w/om 15: " + (new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]")).scrollTo();
        String services15S = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        services15S = services15S.substring(0, services15S.indexOf('€'));
        if(services15S.equals(String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 15 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + services15S + " не равен " + String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(services15S)
                    .as("Check that value in Services for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double services20 = 0.0;
        services20 = programServices[1];
        services20 = services20 / rubEur;
        services20 = services20 / generalMarge;

        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]")).scrollTo();
        String services20S = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]//tbody//tr//th/following-sibling::td[2]")).getText();
        services20S = services20S.substring(0, services20S.indexOf('€'));
        if(services20S.equals(String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 20 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 20 неверное: "
                + services20S + " не равен " + String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(services20S)
                    .as("Check that value in Services for 20 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services20).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double services25 = 0.0;
        services25 = programServices[2];
        services25 = services25 / rubEur;
        services25 = services25 / generalMarge;

        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]")).scrollTo();
        String services25S = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-services\"]//tbody//tr//th/following-sibling::td[3]")).getText();
        services25S = services25S.substring(0, services25S.indexOf('€'));
        if(services25S.equals(String.valueOf((int) new BigDecimal(services25).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 25 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 25 неверное: "
                + services25S + " не равен " + String.valueOf((int) new BigDecimal(services25).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(services25S)
                    .as("Check that value in Services for 25 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(services25).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        System.out.println("    Проверяем таблицу Totals (WE):");
        Double totalWE15 = 0.0;
        totalWE15 = priceDBLD/2 + programServices[0] + programDailyServices[0];
        totalWE15 = totalWE15 / rubEur;
        totalWE15 = totalWE15 / generalMarge;
        //System.out.println("Total WE 15: " + (new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-totals\"]")).scrollTo();
        String totalWE15S = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-totals\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        totalWE15S = totalWE15S.substring(0, totalWE15S.indexOf('€'));
        if(totalWE15S.equals(String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 15 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 15 неверное: "
                + totalWE15S + " не равен " + String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE15S)
                    .as("Check that value in Totals (WE) for 15 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE15).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double totalWE20 = 0.0;
        totalWE20 = priceDBLD/2 + programServices[1] + programDailyServices[1];
        totalWE20 = totalWE20 / rubEur;
        totalWE20 = totalWE20 / generalMarge;

        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-totals\"]")).scrollTo();
        String totalWE20S = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-totals\"]//tbody//tr//th/following-sibling::td[2]")).getText();
        totalWE20S = totalWE20S.substring(0, totalWE20S.indexOf('€'));
        if(totalWE20S.equals(String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 20 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 20 неверное: "
                + totalWE20S + " не равен " + String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE20S)
                    .as("Check that value in Totals (WE) for 20 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE20).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

        Double totalWE25 = 0.0;
        totalWE25 = priceDBLD/2 + programServices[2] + programDailyServices[2];
        totalWE25 = totalWE25 / rubEur;
        totalWE25 = totalWE25 / generalMarge;

        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-totals\"]")).scrollTo();
        String totalWE25S = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-totals\"]//tbody//tr//th/following-sibling::td[3]")).getText();
        totalWE25S = totalWE25S.substring(0, totalWE25S.indexOf('€'));
        if(totalWE25S.equals(String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()))) {
            System.out.println(commonCode.ANSI_GREEN+"      -  Значение для группы 25 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значение для группы 25 неверное: "
                + totalWE25S + " не равен " + String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()) + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(totalWE25S)
                    .as("Check that value in Totals (WE) for 25 is correct")
                    .isEqualTo(String.valueOf((int) new BigDecimal(totalWE25).setScale(0, RoundingMode.HALF_UP).floatValue()));
        }

    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();

    }
}
