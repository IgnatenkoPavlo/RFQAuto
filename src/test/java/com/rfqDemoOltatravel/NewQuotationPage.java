package com.rfqDemoOltatravel;


import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

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
        CommonCode.WaitForProgruzkaSilent();
        System.out.print(" - Готово - номер квотации - ");
        System.out.println($(By.cssSelector("input#input-search-query")).getValue());
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

        public static void SetNumberOfNights(int nightsCounter) {

            CommonCode commonCode = new CommonCode();
            $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).scrollTo().click();
            commonCode.WaitForProgruzkaSilent();
            $(By.cssSelector(NewQuotationPage.OptionsTable.numberOfNights)).setValue(String.valueOf(nightsCounter)).pressEnter();
            commonCode.WaitForProgruzkaSilent();
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
    public static final String accomodationsTableREG = "//table[@id=\"table-accommodations\"]";

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
        public static final String nightsCounterForCityREG = "//td[2]";

        public static String CityByNumberREG (int cityCounter) {

            String result = accomodationsTableREG + "//tbody//tr["+String.valueOf(cityCounter)+"]";

            return result;
        }
    }


    //Попап добавления городов
    public static final String cityAddPopup = "div[id=\"modal-cityselector\"] div[class=\"modal-dialog\"]";
    public static final String cityAddPopupREG = "//div[@id=\"modal-cityselector\"]//div[@class=\"modal-dialog\"]";
    public static String GetCityNameButtonREG(String cityName) {

        String result = cityAddPopupREG + "//div[@class=\"modal-body\"]//button[contains(text(), '" + cityName + "')]";

        return result;
    }

    public static void AddCityToAccomodationByName(String cityName) {

        CommonCode commonCode = new CommonCode();
        $(By.cssSelector(NewQuotationPage.AccomodationsTable.addButton)).click();
        //Ждём появления меню
        $(By.xpath(cityAddPopupREG)).shouldBe(Condition.visible);
        //Кликаем по кнопке с cityName
        $(By.xpath(GetCityNameButtonREG(cityName))).shouldBe(Condition.visible);
        $(By.xpath(GetCityNameButtonREG(cityName))).click();
        commonCode.WaitForProgruzkaSilent();
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

            String result = "//table[@class=\"services\"]//tbody[@class=\"auto\"]//tr[" + String.valueOf(serviceCounter) + "]";

            return result;
        }

        public static String GetDailyServiceByNumberREG(int serviceCounter) {

            String result = "//div[@class=\"serviceByDayInfo\"]//table[@class=\"serviceByDayTable services\"]//tbody//tr[" + String.valueOf(serviceCounter) + "]";

            return result;
        }

        public static String GetDailySumForPeopleREG(int peopleCounter) {

            String result = "";
            result = "//td[@class=\"featureds\"]//table[@class=\"table-featureds\"]//tbody//tr[" + String.valueOf(peopleCounter) + "]//td[@class=\"number sum\"]//span[@class=\"sum\"]";

            return result;
        }

        public static String GetSumForPeopleREG(int peopleCounter) {

            String result = "";
            result = "//td[@class=\"featureds\"]//table[@class=\"table-featureds\"]//tbody//tr[" + String.valueOf(peopleCounter) + "]//td[4]//span[@class=\"sum\"]";

            return result;
        }

        public static String GetSumForUnitREG(int peopleCounter) {

            String result = "";
            result = "//td[@class=\"featureds\"]//tbody//tr[" + String.valueOf(peopleCounter) + "]//td[3]//span[@class=\"editable editable-featured-service-price price\"]";

            return result;
        }
    }
}
