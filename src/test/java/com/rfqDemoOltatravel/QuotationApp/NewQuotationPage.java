package com.rfqDemoOltatravel.QuotationApp;


import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.WebDriverRunner.url;

public class NewQuotationPage {


    public static void CreateQuotation(String quotationName, String companyName) {
        System.out.println("[-] Создаём новый Quotation:");
        $(By.id("qbtn-create")).click();
        $(By.xpath(QuotationListPage.newQuotationPopapREG)).shouldBe(visible);
        $(By.xpath(QuotationListPage.newQuotationNameREG)).setValue(quotationName);
        System.out.println("      Имя - "+quotationName);
        $(By.xpath(QuotationListPage.newQuotationClientNameREG)).selectOptionContainingText(companyName);
        System.out.println("      Клиент - "+companyName);
        $(By.xpath(QuotationListPage.newQuotationPopapOkButtonREG)).click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        System.out.println(" - Готово - ссылка квотации - "+url());
    }

    public static final String quotationDuplicateButton = "";

    //Таблица Options
    public static class OptionsTable {

        public static final String optionsTable = "table[id=\"table-options\"]";

        public static final String rubUsdRate = optionsTable + " tr[data-key=\"rub_usd_rate\"] td[class=\"value editable editable-quotatoin-option-value\"]";
        public static final String rubEurRate = optionsTable + " tr[data-key=\"rub_eur_rate\"] td[class=\"value editable editable-quotatoin-option-value\"]";
        public static final String generalMarge = optionsTable + " tr[data-key=\"general_marge\"] td[class=\"value editable editable-quotatoin-option-value\"]";
        public static final String guidesLanguage = optionsTable + " tr[data-key=\"guide_language\"] td select[class=\"option\"]";
        public static final String numberOfNights = optionsTable + " tr[data-key=\"number_of_nights\"] td[class=\"value editable editable-quotatoin-option-value\"]";
        public static final String hotelPriceType = optionsTable + " tr[data-key=\"hotel_price_type\"] td select[class=\"option\"]";
        public static final String freeTourLeadersAccoommType = optionsTable + " tr[data-key=\"ftl_acc_type\"] td select[class=\"option\"]";
        public static final String freeTourLeaders = optionsTable + " tr[data-key=\"ftl_number\"] td[class=\"value editable editable-quotatoin-option-value\"]";
        public static final String currency = optionsTable + " tr[data-key=\"currency\"] select[class=\"option\"]";
        public static final String registrationFeeForSPB = optionsTable + " tr[data-key=\"spb_hotel_registration_fee\"] td[class=\"value editable editable-quotatoin-option-value\"]";
        public static final String presentMealServices = optionsTable + " tr[data-key=\"preset_meal_services\"] td[class=\"value\"] select[class=\"option\"]";

        public static void SetNumberOfNightsInOptions(int nightsCounter) {

            System.out.print("[-] Меняем количество ночей на "+String.valueOf(nightsCounter));
            $(By.cssSelector(OptionsTable.numberOfNights)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            $(By.cssSelector(OptionsTable.numberOfNights)).setValue(String.valueOf(nightsCounter)).pressEnter();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            String result = $(By.cssSelector(OptionsTable.numberOfNights)).getText();
            if(String.valueOf(nightsCounter).equals(result)) {System.out.println(QuotationAppCommonCode.OK); }
            else {
                System.out.println(QuotationAppCommonCode.ANSI_RED+" - Колличество ночей в Options не установлено"+ QuotationAppCommonCode.ANSI_RESET);
                throw new IllegalArgumentException("Can`t set number of nights in Options table as "+String.valueOf(nightsCounter)); }
        }

        public static void SetCurrencyInOptions(String currency) {

            System.out.print("[-] Выставляем валюту в "+currency);
            $(By.cssSelector(OptionsTable.currency)).scrollTo().selectOptionContainingText(currency);
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            String result = $(By.cssSelector(OptionsTable.currency)).getSelectedText();
            if(currency.equals(result)) {System.out.println(QuotationAppCommonCode.OK); }
            else {
                System.out.println(QuotationAppCommonCode.ANSI_RED+" - Валюта в Options не установлена"+ QuotationAppCommonCode.ANSI_RESET);
                throw new IllegalArgumentException("Can`t set currency in Options table as "+currency); }
        }

        public static void SetCurrencyRateForUSD(double usdRate) {

            System.out.print("[-] Выставляем курс доллара - "+usdRate);
            $(By.cssSelector(OptionsTable.rubUsdRate)).scrollTo().setValue(String.valueOf(usdRate)).pressEnter();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            String result = $(By.cssSelector(OptionsTable.rubUsdRate)).getText();
            if(String.valueOf(usdRate).equals(result)) {System.out.println(QuotationAppCommonCode.OK); }
            else {
                System.out.println(QuotationAppCommonCode.ANSI_RED+" - Курс доллара в Options не установлен"+ QuotationAppCommonCode.ANSI_RESET);
                throw new IllegalArgumentException("Can`t set USD rate in Options table as "+usdRate); }
        }

        public static void SetCurrencyRateForEUR(double eurRate) {

            System.out.print("[-] Выставляем курс евро - "+eurRate);
            $(By.cssSelector(OptionsTable.rubEurRate)).scrollTo().setValue(String.valueOf(eurRate)).pressEnter();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            String result = $(By.cssSelector(OptionsTable.rubEurRate)).getText();
            if(String.valueOf(eurRate).equals(result)) {System.out.println(QuotationAppCommonCode.OK); }
            else {
                System.out.println(QuotationAppCommonCode.ANSI_RED+" - Курс евро в Options не установлен"+ QuotationAppCommonCode.ANSI_RESET);
                throw new IllegalArgumentException("Can`t set EUR rate in Options table as "+eurRate); }
        }

        public static void SetFreeTourLeadersInOptions(int freeTourLeaders) {

            System.out.print("[-] Выставляем Free Tour Leaders в "+freeTourLeaders);
            $(By.cssSelector(OptionsTable.freeTourLeaders)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            $(By.cssSelector(OptionsTable.freeTourLeaders)).sendKeys(String.valueOf(freeTourLeaders));
            $(By.cssSelector(OptionsTable.freeTourLeaders)).pressEnter();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            String result = $(By.cssSelector(OptionsTable.freeTourLeaders)).getText();
            if(String.valueOf(freeTourLeaders).equals(result)) {System.out.println(QuotationAppCommonCode.OK); }
            else {
                System.out.println(QuotationAppCommonCode.ANSI_RED+" - Free Tour Leaders в Options не установлено"+ QuotationAppCommonCode.ANSI_RESET);
                throw new IllegalArgumentException("Can`t set Free Tour Leaders in Options table as "+freeTourLeaders); }
        }
    }

    //Таблица Groups
    public static final String groupsTableREG = "//table[@id=\"table-groups\"]";
    public static class GroupsTable {

        public static final String groupsAddButtonREG = groupsTableREG+"//tfoot//a[@class=\"qbtn qbtn-add\"]";

        public static String GetGroupByNumberDeleteButtonREG (int groupNumber) {

            String result = groupsTableREG + "//tbody//tr["+String.valueOf(groupNumber)+"]//a[@class=\"qbtn qbtn-delete-group\"]";
            return result;
        }

        public static String GroupByNumberREG(int groupCounter) {

            String result = groupsTableREG + "//tbody//tr["+String.valueOf(groupCounter)+"]";

            return result;
        }

        public static void AddNewGroup(String group) {


        }

    }


    //Таблица Dates
    public static class DatesPeriodsTable {

        public static final String datesTable = "table[id=\"table-dates\"]";
        public static final String datesTableREG = "//table[@id=\"table-dates\"]";

        public static final String periodsTable = "table[id=\"table-periods\"]";
        public static final String periodsTableREG = "//table[@id=\"table-periods\"]";

        public static final String addDateButton = datesTable + " a[class=\"qbtn qbtn-add\"]";
        public static final String newDateInputField = datesTable + " input[class=\"input-date hasDatepicker\"]";
        public static final String fromDateField = datesTable + " td[class=\"dateFrom\"]";
        public static final String tillDateInput = datesTable + " td[class=\"dateTo\"]";
        public static final String saveDateButton = datesTable + " a[class=\"qbtn qbtn-save\"]";

        public static final String addPeriodButton = periodsTable + " a[class=\"qbtn qbtn-add\"]";
        public static final String fromPeriodInputField = periodsTable + " td:nth-of-type(1) input[class=\"input-date hasDatepicker\"]";
        public static final String toPeriodInputField = periodsTable + " td:nth-of-type(2) input[class=\"input-date hasDatepicker\"]";
        public static final String savePeriodButton = periodsTable + " a[class=\"qbtn qbtn-save\"]";

        public static String GetFromDateByPeriodCounter(int periodCounter) {

            String result = $(By.xpath(datesTableREG + "//tbody//tr["+String.valueOf(periodCounter)
                    +"]//td[@class=\"dateFrom\"]")).scrollTo().getText();

            return result;
        }

        public static String GetTillDateByPeriodCounter(int periodCounter) {

            String result = $(By.xpath(datesTableREG + "//tbody//tr["+String.valueOf(periodCounter)
                    +"]//td[@class=\"dateTo\"]")).scrollTo().getText();

            return result;
        }

        /*public static void DeleteDatesPeriodByNumber(int periodCounter) {

            $(By.xpath(datesTableREG + "//tbody//tr["+String.valueOf(periodCounter)
                    +"]//a[@class=\"qbtn qbtn-delete\"]")).scrollTo().click();
        }*/
    }


    //Taблица Accommodations
    public static final String accomodationsTable = "table[id=\"table-accommodations\"]";
    public static final String accomodationsTableREG = "//div[@id=\"accommodations\"]/table[@id=\"table-accommodations\"]";

    public static class AccomodationsTable{

        public static String AccommodationDateByNumberREG(int dateCounter) {
            String result = "//div[@id=\"accommodations\"]//div[@id=\"accommodation-select-date\"]//label["+String.valueOf(dateCounter)+"]";

            return result;
        }

        public static final String addButton = accomodationsTable + " a[class=\"qbtn qbtn-add\"]";
        public static final String showAllPricesButton = accomodationsTable + " a[class=\"qbtn qbtn-showallprices\"]";
        public static final String nightsAvailableUsedIndicator = accomodationsTable
                + " tfoot tr[class=\"totals\"] td[class=\"nightsTotal\"]";

        public static final String moveUpOfCityREG = "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-move qbtn-moveup\"]";
        public static final String insertBeforeOfCityREG = "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-insert qbtn-insertbefore\"]";
        public static final String deleteOfCityREG = "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]";
        public static final String togglePricesOfCityREG = "//td[@class=\"actions\"]//a[@class=\"qbtn qbtn-toggleprices\"]";
        public static final String nightsCounterForCityREG = "//td[@class=\"editable editable-accommodation-nights nights\"]";
        public static final String hotelTypeForCityREG = "/td[@class=\"hotelType visibility-group\"]/select[@class=\"hotelType\"]";
        public static final String hotelSelectionDropDownREG = "//td[@class=\"prices-group visibility-group\"]//table//tbody//tr//select[@class=\"hotel\"]";


        public static String CityByNumberREG(int cityCounter) {

            String result = accomodationsTableREG + "//tbody//tr["+String.valueOf(cityCounter)+"]";

            return result;
        }

        public static String GroupPeriodByNumberREG(int periodCounter) {

            String result = "//td[@class=\"prices-group visibility-group\"]//table//tbody//tr["+String.valueOf(periodCounter)+"]";

            return result;
        }

        public static double GroupGetPriceSGLForCityForPeriod(int cityCounter, int periodCounter) {

            String temp = $(By.xpath(CityByNumberREG(cityCounter)+ GroupPeriodByNumberREG(periodCounter)
                    + "//td[@class=\"editable editable-accommodation-date-price-sgl-group priceSglGroup\"]")).getText();
            if(temp.isEmpty()) temp = $(By.xpath(CityByNumberREG(cityCounter)+ GroupPeriodByNumberREG(periodCounter)
                    + "//td[@class=\"editable editable-accommodation-date-price-sgl-group priceSglGroup\"]")).getValue();

            double result=Double.valueOf(temp);
            return result;
        }

        public static double GroupGetPriceDBLForCityForPeriod(int cityCounter, int periodCounter) {

            String temp = $(By.xpath(CityByNumberREG(cityCounter)+ GroupPeriodByNumberREG(periodCounter)
                    + "//td[@class=\"editable editable-accommodation-date-price-dbl-group priceDblGroup\"]")).getText();
            if(temp.isEmpty()) temp = $(By.xpath(CityByNumberREG(cityCounter)+ GroupPeriodByNumberREG(periodCounter)
                    + "//td[@class=\"editable editable-accommodation-date-price-dbl-group priceDblGroup\"]")).getValue();

            double result=Double.valueOf(temp);
            return result;
        }

        public static String IndividualPeriodByNumberREG(int periodCounter) {

            String result = "//td[@class=\"prices-individual visibility-individual\"]//table//tbody//tr["+String.valueOf(periodCounter)+"]";

            return result;
        }

        public static double IndividualGetPriceSGLForCityForPeriod(int cityCounter, int periodCounter) {

            String temp = $(By.xpath(CityByNumberREG(cityCounter)+ IndividualPeriodByNumberREG(periodCounter)
                    + "//td[@class=\"editable editable-accommodation-date-price-sgl-individual priceSglIndividual\"]")).getText();
            if(temp.isEmpty()) temp = $(By.xpath(CityByNumberREG(cityCounter)+ IndividualPeriodByNumberREG(periodCounter)
                    + "//td[@class=\"editable editable-accommodation-date-price-sgl-individual priceSglIndividual\"]")).getValue();

            double result=Double.valueOf(temp);
            return result;
        }

        public static double IndividualGetPriceDBLForCityForPeriod(int cityCounter, int periodCounter) {

            String temp = $(By.xpath(CityByNumberREG(cityCounter)+ IndividualPeriodByNumberREG(periodCounter)
                    + "//td[@class=\"editable editable-accommodation-date-price-dbl-individual priceDblIndividual\"]")).getText();
            if(temp.isEmpty()) temp = $(By.xpath(CityByNumberREG(cityCounter)+ IndividualPeriodByNumberREG(periodCounter)
                    + "//td[@class=\"editable editable-accommodation-date-price-dbl-individual priceDblIndividual\"]")).getValue();

            double result=Double.valueOf(temp);
            return result;
        }

        public static void SetNightForCityByNumber(String cityName, int cityNumber, String nightsNumber) {

            System.out.print("[-] Изменяем количество дней в "+cityName+" на "+cityNumber);
            $(By.cssSelector(accomodationsTable)).scrollTo();
            $(By.xpath(CityByNumberREG(cityNumber)+nightsCounterForCityREG)).click();
            $(By.xpath(CityByNumberREG(cityNumber)+nightsCounterForCityREG)).setValue(nightsNumber).pressEnter();
            confirm();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
            String result = $(By.xpath(CityByNumberREG(cityNumber)+nightsCounterForCityREG)).getText();
            if(nightsNumber.equals(result)) {System.out.println(QuotationAppCommonCode.OK); }
            else {
                System.out.println(QuotationAppCommonCode.ANSI_RED+" - Количество ночей не изменено"+ QuotationAppCommonCode.ANSI_RESET);
                throw new IllegalArgumentException("Can`t set nights value for a city"+ cityNumber+ " in Accommodations as "+nightsNumber); }

        }
    }


    //Попап добавления городов
    public static final String cityAddPopup = "div[id=\"modal-cityselector\"] div[class=\"modal-dialog\"]";
    public static final String cityAddPopupREG = "//div[@id=\"modal-cityselector\"]//div[@class=\"modal-dialog\"]";
    public static String GetCityNameButtonREG(String cityName) {

        String result = cityAddPopupREG + "//div[@class=\"modal-body\"]//button[contains(text(), '" + cityName + "')]";

        return result;
    }

    public static void AddCityToAccomodationByName(String cityName, int position) {

        System.out.print("[-] Добавляем город: "+cityName+" на позицию "+position);
        $(By.cssSelector(AccomodationsTable.addButton)).scrollTo().click();
        //Ждём появления меню
        $(By.xpath(cityAddPopupREG)).shouldBe(Condition.visible);
        //Кликаем по кнопке с cityName
        $(By.xpath(GetCityNameButtonREG(cityName))).shouldBe(Condition.visible);
        $(By.xpath(GetCityNameButtonREG(cityName))).click();
        QuotationAppCommonCode.WaitForProgruzkaSilent();
        String result = $(By.xpath(AccomodationsTable.CityByNumberREG(position)+"//td[@class=\"city\"]")).getText();
        if(String.valueOf(cityName).equals(result)) {System.out.println(QuotationAppCommonCode.OK); }
        else {
            System.out.println(QuotationAppCommonCode.ANSI_RED+" - Город не добавлен"+ QuotationAppCommonCode.ANSI_RESET);
            throw new IllegalArgumentException("Can`t add new city to Accommodations "+cityName); }
    }
    //Конец Попапа


    //Таблица Programm
    public static class ProgrammSection {

        public static final String addServiceButtonREG = "//table//tfoot//tr//td[2]//a[@class=\"qbtn qbtn-add\"]";
        public static final String showAllPricesForADayREG = "//table//tfoot//tr//td[7]//a[@class=\"qbtn qbtn-showallprices\"]";
        public static final String hideAllPricesForADayREG = "//table//tfoot//tr//td[7]//a[@class=\"qbtn qbtn-hideallprices\"]";
        public static final String guideFromMoscowREG = "//span[@class=\"checkDayLine\"]//input[@name=\"guideDay\"]";
        public static final String goldenRingGuide = "//span[@class=\"checkDayLine\"]//input[@name=\"goldenRingGuideDay\"]";
        public static final String transportFromMoscowREG = "//span[@class=\"checkDayLine\"]//input[@name=\"transportDay\"]";
        public static final String showAllDailyPricesREG = "//span[@class=\"checkDayLine\"]//a[@class=\"qbtn qbtn-showalldailyservices\"]";
        public static final String hideAllDailyPricesREG = "//span[@class=\"checkDayLine\"]//a[@class=\"qbtn qbtn-hidealldailyservices\"]";
        public static final String serviceNameDropDownREG = "/td[3]/select";
        public static final String serviceCriteriaNameREG = "/td[@class=\"criteria\"]/select[@class=\"serviceName\"]";
        public static final String serviceCriteriaDurationREG = "/td[@class=\"criteria\"]/input[@name=\"hours2\"]";
        public static final String serviceCriteriaDaysREG = "/td[@class=\"criteria\"]//input[@name=\"days\"]";
        public static final String serviceCriteriaRestaurantTypeREG = "/td[@class=\"criteria\"]/select[@name=\"restaurantType\"]";
        public static final String serviceCriteriaRateREG = "/td[@class=\"criteria\"]/select[@class=\"rate2\"]";
        public static final String serviceCriteriaClassREG = "/td[@class=\"criteria\"]//select[@name=\"class\"]";
        public static final String serviceOptionsAddInfoREG = "/td[@class=\"options\"]//span/input[@name=\"customData\"]";
        public static final String serviceOptionsDurationREG = "/td[@class=\"options\"]/span//input[@name=\"duration\"]";




        public static String GetADayByNumberREG(int dayCounter) {

            String result = "//div[@id=\"program\"]//div[@class=\"day\"][" + String.valueOf(dayCounter) + "]";

            return result;
        }

        public static String GetACityByNumberREG(int cityCounter) {

            String result = "//div[@class=\"cities\"]//div[@class=\"city\"]["+ String.valueOf(cityCounter) +"]";

            return result;
        }

        public static String GetMainServiceByNumberREG(int serviceCounter) {

            String result = "//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[" + String.valueOf(serviceCounter) + "]";

            return result;
        }

        public static String GetAutoServiceByNumberREG(int serviceCounter) {

            String result = "//table[@class=\"services\"]/tbody[@class=\"auto\"]/tr[" + String.valueOf(serviceCounter) + "]";

            return result;
        }

        public static String GetDailyServiceByNumberREG(int serviceCounter) {

            String result = "//div[@class=\"serviceByDayInfo\"]//table[@class=\"serviceByDayTable services\"]//tbody//tr[" + String.valueOf(serviceCounter) + "]";

            return result;
        }

        public static String GetDailySumForPeopleREG(int peopleCounter) {

            String result = "//td[@class=\"featureds\"]//table[@class=\"table-featureds\"]//tbody//tr[" + String.valueOf(peopleCounter) + "]//td[@class=\"number sum\"]//span[@class=\"sum\"]";

            return result;
        }

        public static String GetSumForPeopleREG(int peopleCounter) {

            String result = "//td[@class=\"featureds\"]//table[@class=\"table-featureds\"]//tbody//tr[" + String.valueOf(peopleCounter) + "]//td[4]//span[@class=\"sum\"]";

            return result;
        }

        public static String GetSumForUnitREG(int peopleCounter) {

            String result = "//td[@class=\"featureds\"]//tbody//tr[" + String.valueOf(peopleCounter) + "]//td[3]//span[@class=\"editable editable-featured-service-price price\"]";

            return result;
        }

        public static void AddServiceByName(int day, int cityCounter, String serviceName) {

            int servicesSize = $$(By.xpath(GetADayByNumberREG(day)
                    +GetACityByNumberREG(cityCounter)
                    +"//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size();

            $(By.xpath(GetADayByNumberREG(day)+GetACityByNumberREG(cityCounter)+addServiceButtonREG)).scrollTo().click();
            QuotationAppCommonCode.WaitForProgruzkaSilent();

            $(By.xpath(GetADayByNumberREG(day)
                    +GetACityByNumberREG(cityCounter)
                    +GetMainServiceByNumberREG(servicesSize+1)
                    +serviceNameDropDownREG)).scrollTo().selectOptionContainingText(serviceName);
            QuotationAppCommonCode.WaitForProgruzkaSilent();

            String result = $(By.xpath(GetADayByNumberREG(day)
                    +GetACityByNumberREG(cityCounter)
                    +GetMainServiceByNumberREG(servicesSize+1)
                    +serviceNameDropDownREG)).getSelectedText();
            if(serviceName.equals(result)) {System.out.println(QuotationAppCommonCode.OK); }
            else {
                System.out.println(QuotationAppCommonCode.ANSI_RED+" - Service не добавлен"+ QuotationAppCommonCode.ANSI_RESET);
                throw new IllegalArgumentException("Can`t add new city to Accommodations "+serviceName+" get"+result); }
        }

        public static void DeleteLastMainService(int day, int cityCounter) {

            int servicesSize = $$(By.xpath(GetADayByNumberREG(day)
                    +GetACityByNumberREG(cityCounter)
                    +"//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size();

            /*$(By.xpath(GetADayByNumberREG(day)
                            +GetACityByNumberREG(cityCounter)
                            +GetMainServiceByNumberREG(servicesSize)
                            +"/td[@class=\"actions\"]")).scrollTo().click();*/
            $(By.xpath(GetADayByNumberREG(day)
                    +GetACityByNumberREG(cityCounter)
                    +GetMainServiceByNumberREG(servicesSize)
                    +"/td[@class=\"actions\"]//a[@class=\"qbtn qbtn-delete\"]")).scrollTo().click();

            confirm();
            QuotationAppCommonCode.WaitForProgruzkaSilent();
        }
    }

    //Results
    public static final String resultsSectionREG = "//div[@id=\"results\"]";
    public static class ResultsSection{

        public static final String hotelsWOMTableREG = resultsSectionREG+"//table[@id=\"table-result-hotels-wo-margin-hybrid\"]";
        public static final String hotelsTableREG = resultsSectionREG+"//table[@id=\"table-result-hotels-hybrid\"]";
        public static final String servicesTableREG = resultsSectionREG+"//table[@id=\"table-result-services\"]";
        public static final String dayRelatedServicesTableREG = resultsSectionREG+"//table[@id=\"table-result-services-by-days\"]";
        public static final String totalsTableREG = resultsSectionREG+"//table[@id=\"table-result-totals\"]";

    }
}
