package com.rfqDemoOltatravel;

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

public class TestOfFreeTourLeaders {

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
    public void testOfFreeTourLeadersOption(){

        WebDriverRunner.setWebDriver(driver);
        System.out.print("[-] Открываем URL: http://rfq-demo.oltatravel.com/");
        open("http://rfq-demo.oltatravel.com/");
        commonCode.WaitForPageToLoad(driver);
        System.out.println(" - Готово");


        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("pavel.sales");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(" - Готово");

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        commonCode.WaitForProgruzka();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем Quotation приложение");
        open("http://rfq-demo.oltatravel.com/application/olta.quotation");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);
        System.out.println(" - Готово");

        //Ждём доступности "Create New Quotation"
        System.out.print("[-] Ждём доступности кнопки Create New Quotation");
        $(By.id("qbtn-create")).isDisplayed();
        System.out.println(" - Готово");

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
        System.out.println("[-] Выставляем курс евро 70");
        $(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).setValue("70").pressEnter();
        commonCode.WaitForProgruzkaSilent();
        Double rubEur = 0.0;
        rubEur = Double.valueOf($(By.cssSelector(NewQuotationPage.OptionsTable.rubEurRate)).getText());

        //Выставляем колество ночей - 2
        int nightInOptionsCounter = 2;
        System.out.println("[-] Меняем количество ночей на " + nightInOptionsCounter);
        NewQuotationPage.OptionsTable.SetNumberOfNights(nightInOptionsCounter);


        //Выставляем Free Tour Leaders в 2
        System.out.print("[-] Выставляем Free Tour Leaders в 2");
        $(By.cssSelector(NewQuotationPage.OptionsTable.freeTourLeaders)).click();
        commonCode.WaitForProgruzkaSilent();
        $(By.cssSelector(NewQuotationPage.OptionsTable.freeTourLeaders)).sendKeys("2");
        $(By.cssSelector(NewQuotationPage.OptionsTable.freeTourLeaders)).pressEnter();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        System.out.print("[-] Сохраняем маржу");
        Double generalMarge = 0.0;
        generalMarge = Double.valueOf(($(By.cssSelector(NewQuotationPage.OptionsTable.generalMarge)).getText()).replace(',', '.'));
        //System.out.println(generalMarge);
        System.out.println(" - Готово");

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
        System.out.println(" - Готово");

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
        System.out.println(" - Готово");


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
        Double priceSS = priceSGLD - priceDBLD/2;

        //Проверяем что в Program кол-во в группе отбражается как 15 + 2
        System.out.println("[-] Проверяем, что в Program кол-во в группе отбражается как 15+2:");
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + NewQuotationPage.ProgrammSection.showAllPricesForADayREG)).scrollTo().click();
        String numberOfPeopleInProgram = String.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(1)
                + "//td//table[@class=\"table-featureds\"]//tbody//tr//td[@class=\"people\"]")).getText());
        if (numberOfPeopleInProgram.equals("15+2")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество людей отображается корректно + "
                    +CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfPeopleInProgram)
                    .as("Check that number of people in Program section is correct")
                    .isEqualTo(String.valueOf("15+2"));
            System.out.println(CommonCode.ANSI_RED +"      Значение количества людей отображается не корректое: "
                    + CommonCode.ANSI_RESET+ numberOfPeopleInProgram);
        }
        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(1)
                + NewQuotationPage.ProgrammSection.GetACityByNumberREG(1)
                + NewQuotationPage.ProgrammSection.hideAllPricesForADayREG)).scrollTo().click();

        Double programServicesFor15 = 150.0;
        Double programServicesFor20 = 20.0;
        Double programServicesFor25 = 25.0;

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



                    /*$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(2))).click();

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(2))).setValue(programServicesFor20.toString()).pressEnter();

                    programServicesFor20 = programServicesFor20 + 6.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);


                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(3))).click();

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(3))).setValue(programServicesFor25.toString()).pressEnter();


                    programServicesFor25 = programServicesFor25 + 7.0;
                    $(By.xpath("//span[contains(text(),'Loading')]")).shouldNot(exist);*/
                }

                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//tr//td//a[@class=\"qbtn qbtn-hideallprices\"]")).shouldBe(visible).click();

            }
            System.out.println(" - готово");

        }

        programServicesFor15 = 0.0;
        programServicesFor20 = 0.0;
        programServicesFor25 = 0.0;

        Double programDailyServicesFor15 = 0.0;
        Double programDailyServicesFor20 = 0.0;
        Double programDailyServicesFor25 = 0.0;

        System.out.println("[-] Считаем суммы для 3-х групп: 15, 20, 25");
        dayCounterMax = nightInOptionsCounter + 1;
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            System.out.print("      - считаем для дня номер "+ dayCounter);

            //Считаем для Daily Services
            $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)+ NewQuotationPage.ProgrammSection.showAllDailyPricesREG)).scrollTo().click();
            if(($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                    + NewQuotationPage.ProgrammSection.guideFromMoscowREG)).isSelected())
                    || ($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                    + NewQuotationPage.ProgrammSection.goldenRingGuide)).isSelected())
                    || ($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                    + NewQuotationPage.ProgrammSection.transportFromMoscowREG)).isSelected())){

                int dalyServicesMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                        +"//div[@class=\"serviceByDayInfo\"]//table[@class=\"serviceByDayTable services\"]//tbody//tr[@class=\"service_by_day\"]")).size();
                //System.out.println(dalyServicesMax+"");
                for(int dailyServiceCounter=1;dailyServiceCounter<=dalyServicesMax;dailyServiceCounter++){

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                            + NewQuotationPage.ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                            + NewQuotationPage.ProgrammSection.GetDailySumForPeopleREG(1))).scrollTo();

                    programDailyServicesFor15 = programDailyServicesFor15 +
                            Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                                    + NewQuotationPage.ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                                    + NewQuotationPage.ProgrammSection.GetDailySumForPeopleREG(1))).getText());


                    programDailyServicesFor20 = programDailyServicesFor20 +
                            Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                                    + NewQuotationPage.ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                                    + NewQuotationPage.ProgrammSection.GetDailySumForPeopleREG(2))).getText());

                    programDailyServicesFor25 = programDailyServicesFor25 +
                            Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                                    + NewQuotationPage.ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                                    + NewQuotationPage.ProgrammSection.GetDailySumForPeopleREG(3))).getText());
                }
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)+ NewQuotationPage.ProgrammSection.hideAllDailyPricesREG)).click();
            }

            //Считаем для Services
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)+"//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++){
                System.out.print("         - считаем для города номер "+ cityCounter);
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

                int serviceCounterMax= $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) +
                        NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= serviceCounterMax; serviceCounter++) {

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(1))).scrollTo();

                    programServicesFor15 = programServicesFor15 +
                            Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                    + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(1))).getText());
                    //System.out.println(programServicesFor15);

                    programServicesFor20 = programServicesFor20 +
                            Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                    + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(2))).getText());

                    programServicesFor25 = programServicesFor25 +
                            Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                    + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(3))).getText());
                }

                int autoServiceCounterMax= $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) +
                        NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"auto\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= autoServiceCounterMax; serviceCounter++) {

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter) + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(1))).scrollTo();

                    programServicesFor15 = programServicesFor15 +
                            Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                                    + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(1))).getText());

                    programServicesFor20 = programServicesFor20 +
                            Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                                    + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(2))).getText());

                    programServicesFor25 = programServicesFor25 +
                            Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                    + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                                    + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(3))).getText());
                }

                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//table[@class=\"services\"]//tfoot//a[@class=\"qbtn qbtn-hideallprices\"]")).scrollTo().click();

            }
            System.out.println(" - готово");


        }

        programServicesFor15 = programServicesFor15/15.0;

        //Проверяем, что в Results кол-во в группе отбражается как 15 + 2
        System.out.print("[-] Запускаем перерасчёт");
        $(By.id("qbtn-execute")).scrollTo().click();
        commonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово");

        System.out.println("[-] Проверяем результаты расчёта:");
        System.out.println("[-] Проверяем, что в Results кол-во в группе отбражается как 15 + 2:");
        String numberOfPeopleInResult = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-wo-margin-we\"]//thead//tr[1]//th[2]")).scrollTo().getText();
        if (numberOfPeopleInResult.equals("15+2")){
            System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, количество людей корректное + "+CommonCode.ANSI_RESET);
        } else {
            softAssertions.assertThat(numberOfPeopleInResult)
                    .as("Check that dates in Results are set correctly")
                    .isEqualTo(String.valueOf("15+2"));
            System.out.println(CommonCode.ANSI_RED +"      Количество людей некорректные: " + CommonCode.ANSI_RESET
                    + numberOfPeopleInResult + " -");
        }

        System.out.println("    Проверяем таблицу Hotels (WE) w/o margin:");
        String hotelsWE15womS = $(By.cssSelector("table[id=\"table-result-hotels-wo-margin-we\"] tbody tr td")).getText();
        String hotelsWEwomSSS = $(By.xpath("//table[@id=\"table-result-hotels-wo-margin-we\"]//tbody//tr//td[4]")).getText();
        hotelsWE15womS = hotelsWE15womS.substring(0, hotelsWE15womS.indexOf(' '));
        hotelsWEwomSSS = hotelsWEwomSSS.substring(0, hotelsWEwomSSS.indexOf(' '));
        //System.out.println("hotelsWE 15 w/o marge: " + hotelsWE15womS);
        String priceDBLDS = String.valueOf((int) new BigDecimal(priceDBLD/2+(priceDBLD/15)).setScale(0, RoundingMode.HALF_UP).floatValue());
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
        Double hotelsWE = priceDBLD/2+(priceDBLD/15);
        hotelsWE = hotelsWE / rubEur;
        hotelsWE = hotelsWE / generalMarge;
        String hotelsWES = String.valueOf((int) new BigDecimal(hotelsWE).setScale(0, RoundingMode.HALF_UP).floatValue());
        //System.out.println("Hotels WE 15: " + hotelsWES);
        $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]")).scrollTo();
        String hotelsWER = $(By.xpath("//div[@id=\"result\"]//table[@id=\"table-result-hotels-we\"]//tbody//tr//th/following-sibling::td[1]")).getText();
        hotelsWER = hotelsWER.substring(0, hotelsWER.indexOf('€'));//€
        if(hotelsWES.equals(hotelsWER)) {
            System.out.println(commonCode.ANSI_GREEN+"      - Значения для группы 15 верное +"+commonCode.ANSI_RESET);
        }
        else {System.out.println(commonCode.ANSI_RED+"      -  Значения для группы 15 неверное: "
                + hotelsWES + " не равен " + hotelsWER + "-"+commonCode.ANSI_RESET);
            softAssertions.assertThat(hotelsWES)
                    .as("Check that value in Hotels (WE) for 15 is correct")
                    .isEqualTo(hotelsWER);
        }

        Double hotelsWESS=0.0;
        hotelsWESS = priceSS;
        hotelsWESS = hotelsWESS / rubEur /generalMarge;
        String hotelsWESSS = String.valueOf((int) new BigDecimal(hotelsWESS).setScale(0, RoundingMode.DOWN).floatValue());
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
        services15 = programServicesFor15;
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

        System.out.println("    Проверяем таблицу Totals (WE):");
        Double totalWE15 = 0.0;
        totalWE15 = priceDBLD/2 +(priceDBLD/15)+ programServicesFor15 + programDailyServicesFor15;
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



    }


    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();

    }
}
