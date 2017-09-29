package com.rfqDemoOltatravel;


public class NewQuotationPage {


    public static final String quotationDuplicateButton = "";

    //Таблица Options
    public static class OptionsTable {

        public static final String optionsTable = "table[id=\"table-options\"]";

        public static final String rubUsdRate = optionsTable + " tr[data-key=\"rub_usd_rate\"] td[class=\"value editable editable-quotatoin-option-value\"]";
        public static final String generalMarge = optionsTable + " tr[data-key=\"general_marge\"] td[class=\"value editable editable-quotatoin-option-value\"]";
        public static final String numberOfNights = optionsTable + " tr[data-key=\"number_of_nights\"] td[class=\"value editable editable-quotatoin-option-value\"]";
        public static final String registrationFeeForSPB = optionsTable + " tr[data-key=\"spb_hotel_registration_fee\"] td[class=\"value editable editable-quotatoin-option-value\"]";
    }

    //Таблица Dates
    public static class DatesPeriodsTable {

        public static final String datesPeriodsTable = "table[id=\"table-dates\"]";

        public static final String addButton = datesPeriodsTable + " a[class=\"qbtn qbtn-add\"]";
        public static final String newDateInputField = datesPeriodsTable + " input[class=\"input-date hasDatepicker\"]";
        public static final String saveButton = datesPeriodsTable + " a[class=\"qbtn qbtn-save\"]";
    }


    //Taблица Accommodations
    public static class AccomodationsTable{

        public static final String accomodationsTable = "table[id=\"table-accommodations\"]";

        public static final String addButton = accomodationsTable + " a[class=\"qbtn qbtn-add\"]";
        public static final String showAllPricesButton = accomodationsTable + " a[class=\"qbtn qbtn-showallprices\"]";

    }


    //Попап добавления городов
    public static final String cityAddPopup = "div[id=\"modal-cityselector\"] div[class=\"modal-dialog\"]";
    public static final String cityAddPopupREG = "//div[@id=\"modal-cityselector\"]//div[@class=\"modal-dialog\"]";
    public static String GetCityNameButtonREG(String cityName) {

        String result = cityAddPopupREG + "//div[@class=\"modal-body\"]//button[contains(text(), '" + cityName + "')]";

        return result;
    }


    //Таблица Programm
    public static class ProgrammSection {

        public static String GetADayREG(int dayCounter) {

            String result = "";
            result = "//div[@id=\"program\"]//div[@class=\"day\"][" + String.valueOf(dayCounter) + "]";

            return result;
        }

        public static String GetSumForPeopleREG(int peopleCounter) {

            String result = "";
            result = "//td[@class=\"featureds\"]//tbody//tr[" + String.valueOf(peopleCounter) + "]//span[@class=\"sum\"]";

            return result;
        }

        public static String GetSumForUnitREG(int peopleCounter) {

            String result = "";
            result = "//td[@class=\"featureds\"]//tbody//tr[" + String.valueOf(peopleCounter) + "]//td[3]//span[@class=\"editable editable-featured-service-ppu pricePerUnit\"]";

            return result;
        }
    }
}
