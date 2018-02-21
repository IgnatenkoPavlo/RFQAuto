package com.rfqDemoOltatravel.RFQApp;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class NewQuotationPage extends QuotationListPage{

    public final static String quotationId = "#content span[class=\"grey-title\"] span";
    public final static String quotationIdXP = "//div[@id=\"content\"]//span[@class=\"grey-title\"]//span";
    //public final static String clientName = "#content div[class=\"container-olta title-row\"] div[class=\"left-col\"] #clientName div[class=\"hover-block\"]";
    public final static String clientName = "#clientName";
    public final static String chooseClientNamePopup = "#clientName div[class=\"clients-list check-wrapper\"]";
    public final static String chooseClientNamePopupXP = "//div[@id=\"clientName\"]//div[@class=\"clients-list check-wrapper\"]";

    public static class ChooseClientNamePopup{

        public final static String searchField = chooseClientNamePopup + " div[class=\"check-list-top-row\"] form input[class=\"super-check-list-filter\"]";

    }

    public final static String optionsArea = "#options div[class=\"options-row\"]";
    public final static String optionsAreaXP = "//div[@id=\"options\"]//div[@class=\"options-row\"]";
    public static class Options{

        public final static String currencyButton = optionsArea+ " div[class=\"options-col have-list\"]";
        public final static String currencySelectors = optionsArea+ " div[class=\"check-list\"]";
        public final static String currencySelectorsXP = optionsAreaXP+ "//div[@class=\"check-list\"]";
        public final static String currencyRUB = currencySelectors+ " div[data-value=\"RUB\"]";
        public final static String currencyRUBXP = currencySelectorsXP+ "//div[@data-value=\"RUB\"]";

        public final static String nightsButton = optionsArea+ ":nth-child(3) div[class=\"options-col have-string value-null\"]";
        public final static String nightsButtonXP = optionsAreaXP+ "[2]//div[@class=\"options-col have-string value-null\"]";
        public final static String nightsInput = optionsArea+ ":nth-child(3) div input[data-optionkey=\"number_of_nights\"]";
        public final static String nightsInputXP = optionsAreaXP+ "[2]//div//input[@data-optionkey=\"number_of_nights\"]";

        public final static String presentMealServicesButton = optionsArea+ ":nth-child(4) div";
        public final static String presentMealServicesSelectors = presentMealServicesButton+ " div[class=\"check-list\"]";
        public final static String presentMealServiceFullBoard = presentMealServicesSelectors+ " div[data-value=\"FB\"]";
        public final static String presentMealServiceNO = presentMealServicesSelectors+ " div[data-value=\"NO\"]";

        public final static String presentMenuButton = optionsArea+ ":nth-child(4) div:nth-child(2)";
        public final static String presentMenuSelectors = presentMenuButton+ " div[class=\"check-list country-list\"]";
        public final static String presentMenuIndian = presentMenuSelectors+ " div[data-value=\"Indian\"]";
        public final static String presentMenuRussian = presentMenuIndian+ " div[data-value=\"Russian\"]";

        public final static String additionalServicesButton = optionsArea+ ":nth-child(5) div";
        public final static String additionalServicesSelectors = additionalServicesButton+" div[class=\"radio-list\"]";
        public final static String additionalServicesHeadphones = additionalServicesSelectors+" div[class=\"radio-row\"] div[title=\"Headphones\"]";

        public final static String guidesLanguageButton = optionsArea+" div:nth-child(2)";
        public final static String guidesLanguageArea = guidesLanguageButton+" div[class=\"check-list country-list\"]";
        public final static String guidesLanguageEnglish = guidesLanguageArea+" div[data-value=\"English\"]";

        public final static String freeTourLeadersButton = optionsArea+ ":nth-child(3) div:nth-child(2)";
        public final static String freeTourLeadersInput = optionsArea+ ":nth-child(3) div:nth-child(2) input[data-optionkey=\"ftl_number\"]";
    }

    public final static String datesArea = "#datesBlock";
    public final static String datesAreaXP = "//div[@id=\"datesBlock\"]";
    public final static String periodsArea = "#periodsBlock";
    public final static String periodsAreaXP = "//div[@id=\"periodsBlock\"]";
    public static class DatesPeriods{

        public final static String periodsSwitchButton = datesAreaXP + "//div[@class=\"top-row\"]//a[contains(text(), 'Periods')]";

        public final static String firstIntervalFromInput = datesArea+ " div[style=\"cursor: pointer;\"] div[class=\"info-col\"] input[type=\"text\"]";
        public final static String firstIntervalPeriodsFromInput = periodsArea+ " div[style=\"cursor: pointer;\"] div[class=\"info-col\"] input[type=\"text\"]";

        public final static String firstIntervalToInput = datesArea+ " div[style=\"cursor: pointer;\"] div[class=\"info-col\"]:nth-child(2) input[type=\"text\"]";
        public final static String firstIntervalPeriodsToInput = periodsArea+ " div[style=\"cursor: pointer;\"] div[class=\"info-col\"]:nth-child(2) input[type=\"text\"]";


    }

    public static class Groups{


    }

    public final static String accommodationsArea = "#accommodationsBlock";
    public final static String accommodationsAreaXP = "//div[@id=\"accommodationsBlock\"]";
    public static class Accommodations{

        public final static String cityAddButton = accommodationsArea+ " div[class=\"top-row\"] div[class=\"add-btn\"]";
        public final static String cityList = cityAddButton + " div[class=\"check-wrapper city-selector\"]";
        public final static String moscowButton = cityList + " div[class=\"check-list jspScrollable\"] div[class=\"jspContainer\"]" +
                " div[class=\"jspPane\"] div[class=\"check-wrap\"] span";
        public final static String addCityToLastPossiblePositionControlXP = accommodationsAreaXP+"//div[@class=\"info-row empty-accommodation\"]//div[@class=\"info-col\"]/h5";
        public final static String cityNameXP = "//div[@data-key=\"cityName\"]//span[@class=\"data-info\"]";
        public final static String cityNightXP = "//div[@data-key=\"nights\"]//span[@class=\"data-info\"]";
        public final static String cityHotelTypeXP = "//div[@data-key=\"hotelTypeId\"]//span[@class=\"data-info\"]";


        public static String CityByNumberXP(int cityNumber){
            return String.valueOf(accommodationsAreaXP+"//div[@class=\"info-row\"]["+cityNumber+"]");
        }

    }

    public static class Itinerary{

        public static String ProgramSectionXP(){

            String result=$(By.cssSelector(NewQuotationPage.quotationId)).getText();
            result = result.substring(1, result.length());
            result = "//div[@id=\"quotationdays"+result+"\"]";

            return result;
        }

        public static String DayByNumberXP(int dayCounter){

            return ProgramSectionXP()+"//div[@class=\"day-info\"]["+String.valueOf(dayCounter)+"]";
        }

        public static String GetDataIdOfDayByNumber(int dayCounter){

            return $(By.xpath(DayByNumberXP(dayCounter))).getAttribute("data-id");
        }

        public static String DayCityByNumberXP(int dayCounter, int cityCounter){

            return "//div[@id=\"quotationdaycities"+GetDataIdOfDayByNumber(dayCounter)+"\"]/div["+String.valueOf(cityCounter)+"]";
        }

        public static final String ServiceAddButtonXP = "//div[@class=\"insert-after-btn\"]";

        public static String ServiceByNumberXP(int serviceCounter){

            return "/div[2]//div[@class=\"dayCityServices ui-sortable\"]/div["+String.valueOf(serviceCounter)+"]";
        }
    }

    public final static String resultsAreaXP = "//div[@id=\"resultsBlock\"]";
    public static class Results{

        public static final String calculateButton = resultsAreaXP + "//button[@id=\"execute\"]";
        public static final String totalsWE = resultsAreaXP + "//div[@id=\"result\"]//table[@id=\"table-result-totals\"]/tbody";
        public static final String totalsWD = resultsAreaXP + "//div[@id=\"result\"]//table[@id=\"table-result-totals-wd\"]/tbody";

        public static String PeriodByNumber(int number){
            return String.valueOf("//tr["+ (number+1) +"]");
        }

        public static String DatesOfPeriodByNumber(int number){
            return String.valueOf("//tr["+ (number+1) +"]/td");
        }

        public static String GroupByNumber(int number){
            return String.valueOf("//td["+ (number+1) +"]");
        }


    }


}
