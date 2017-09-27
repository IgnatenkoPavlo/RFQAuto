package com.rfqDemoOltatravel;


public class NewQuotationPage {


    public static final String quotationDuplicateButton = "";

    //Таблица Dates
    public static class DatesPeriodsTable {

        public static final String datesPeriodsTable = "table[id=\"table-dates\"] ";

        public static final String addButton = datesPeriodsTable + "a[class=\"qbtn qbtn-add\"]";
        public static final String newDateInputField = datesPeriodsTable + "input[class=\"input-date hasDatepicker\"]";
        public static final String saveButton = datesPeriodsTable + "a[class=\"qbtn qbtn-save\"]";
    }


    //Taблица Accommodations
    public static class AccomodationsTable{

        public static final String accomodationsTable = "table[id=\"table-accommodations\"] ";

        public static final String addButton = accomodationsTable + "a[class=\"qbtn qbtn-add\"]";
        public static final String showAllPricesButton = accomodationsTable + "a[class=\"qbtn qbtn-showallprices\"]";

    }


    //Попап добавления городов
    public static final String cityNameButton = "div[id=\"modal-cityselector\"] button[class=\"btn btn-default\"]";
}
