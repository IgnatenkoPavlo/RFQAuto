package com.rfqDemoOltatravel.PricesApp;

import org.openqa.selenium.By;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PricesAppCommonCode extends com.rfqDemoOltatravel.CommonCode {

    public class PeriodsCollection{
        public LocalDate dateFrom;
        public LocalDate dateTo;
        public int priceSGL;
        public int priceDBL;
        public int priceSGLWE;
        public int priceDBLWE;

        PeriodsCollection(String ... data) {
            if(data.length>=1)
                dateFrom = LocalDate.of(Integer.valueOf(data[0].substring(6,data[0].length())), Integer.valueOf(data[0].substring(3,5)), Integer.valueOf(data[0].substring(0,2)));
            if(data.length>=2)
                dateTo = LocalDate.of(Integer.valueOf(data[1].substring(6,data[1].length())), Integer.valueOf(data[1].substring(3,5)), Integer.valueOf(data[1].substring(0,2)));
            if(data.length>=3)
                priceSGL = Integer.valueOf(data[2]);
            if(data.length>=4)
                priceDBL = Integer.valueOf(data[3]);
            if(data.length>=5)
                priceSGLWE = Integer.valueOf(data[4]);
            if(data.length>=6)
                priceDBLWE = Integer.valueOf(data[5]);
        }
    }

    public List<PeriodsCollection> SavePeriodsForACityAndHotelType(String cityName, String hotelType) {

        //Выбираем город
        System.out.print("[-] Выбираем город - "+cityName);
        //$(By.cssSelector("div[id=\"title-bar\"] div[id=\"switch-city\"] button[data-switch-value=\""+cityName+"\"]")).scrollTo().click();
        $(By.xpath("//div[@id=\"switch-city\"]//button[@data-switch-value=\""+cityName+"\"]")).click();
        WaitForProgruzkaSilent();
        System.out.println(" - готово");

        //Открываем текущий год
        System.out.print("[-] Открываем текущий год");
        $(By.xpath("//div[@id=\"switch-year\"]//button[contains(text(),'2018')]")).click();
        com.rfqDemoOltatravel.CommonCode.WaitForProgruzkaSilent();
        System.out.println(com.rfqDemoOltatravel.CommonCode.OK);

        //Выбираем тип отеля
        System.out.print("[-] Выбираем "+hotelType);
        $(By.xpath("//div[@id=\"filters-bar\"]//div[@id=\"switch-hotel-type\"]//button[contains(text(),'"+hotelType+"')]")).scrollTo().click();
        WaitForProgruzkaSilent();
        System.out.println(" - готово");

        DateTimeFormatter formatForPrices = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        $(By.xpath("//div[@id=\"content\"]//center[contains(text(),'2018')]")).scrollTo();
        //Откраваем 01-01-2018
        $(By.xpath("//div[@id=\"content\"]//div[@id=\"hotel-calendar\"]//div[@data-year=\"2018\"]" +
                "//div//table//tbody//tr" +
                "//td[@data-date=\"2018-01-01\"]")).click();
        WaitForProgruzkaSilent();


        List<PeriodsCollection> result = new ArrayList<>();

        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);
        //$(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);
        //Сохраняем значения из попапа
        String dateFrom = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
        //String dateFrom = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
        String dateTo = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
        //String dateTo = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
        String priceSGL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
        //String priceSGL = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
        String priceDBL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
        //String priceDBL = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
        String priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
        //String priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
        String priceDBLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();
        //tring priceDBLWE = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();

        //Сохраняем значения в новый элемент списка
        result.add(new PeriodsCollection(dateFrom, dateTo, priceSGL, priceDBL, priceSGLWE, priceDBLWE));

        //System.out.println(dateFrom+" "+dateTo+" "+priceSGL+" "+priceDBL+" "+priceSGLWE+" "+priceDBLWE);
        //Закрываем попап
        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);
        //$(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
        //$(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);

        //Получаем дату начала следующего периода
        LocalDate dateToNext = LocalDate.of(Integer.valueOf(dateTo.substring(6,dateTo.length())), Integer.valueOf(dateTo.substring(3,5)), Integer.valueOf(dateTo.substring(0,2))).plusDays(1);

        //Проходим по всем периодам и сохраняем значения в список
        System.out.print("[-] Проходим по всем периодам и сохраняем значения в список");
        while(!dateTo.equals("31-12-2018")){
            $(By.xpath("//div[@id=\"content\"]//div[@id=\"hotel-calendar\"]//div[@data-year=\"2018\"]" +
                    "//div//table//tbody//tr" +
                    "//td[@data-date=\""+dateToNext.format(formatForPrices)+"\"]")).scrollTo().click();

            $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);
            //$(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);

            dateFrom = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
            dateTo = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
            priceSGL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
            priceDBL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
            priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
            priceDBLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();
            //System.out.println(dateFrom+" "+dateTo+" "+priceSGL+" "+priceDBL+" "+priceSGLWE+" "+priceDBLWE);

            /*dateFrom = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
            dateTo = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
            priceSGL = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
            priceDBL = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
            priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
            priceDBLWE = $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-individual-average-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();*/

            result.add(new PeriodsCollection(dateFrom, dateTo, priceSGL, priceDBL, priceSGLWE, priceDBLWE));

            $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
            $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);
            /*$(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
            $(By.xpath("//div[@id=\"modal-edit-individual-average-hotel-price\"]//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);*/

            dateToNext = LocalDate.of(Integer.valueOf(dateTo.substring(6,dateTo.length())), Integer.valueOf(dateTo.substring(3,5)), Integer.valueOf(dateTo.substring(0,2))).plusDays(1);

        }
        System.out.println(" - готово");
        return result;
    }

}
