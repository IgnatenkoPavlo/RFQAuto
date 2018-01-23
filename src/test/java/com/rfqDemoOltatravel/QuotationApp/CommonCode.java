package com.rfqDemoOltatravel.QuotationApp;

import org.openqa.selenium.*;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CommonCode extends com.rfqDemoOltatravel.CommonCode{

    public static final String QUOTATIONAPPLOGIN = "alexkudrya91@gmail.com";
    public static final String QUOTATIONAPPPASSWORD = "password";

    public void GetSumsForServices(double... programServicesGroups) {

        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        int dayCounterMax = $$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size();
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            System.out.println("      - считаем для дня номер "+ dayCounter);

            //Считаем для Services
            int cityCounterMax = $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)+"//div[@class=\"cities\"]//div[@class=\"city\"]")).size();
            for (int cityCounter = 1; cityCounter <= cityCounterMax; cityCounter++){
                System.out.println("         - считаем для города номер "+ cityCounter);
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//a[@class=\"qbtn qbtn-showallprices\"]")).scrollTo().click();

                int serviceCounterMax= $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) +
                        NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"main\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= serviceCounterMax; serviceCounter++) {

                    for (int serviceSumCounter = 0; serviceSumCounter < programServicesGroups.length; serviceSumCounter++) {
                        $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(serviceSumCounter+1))).scrollTo();

                        programServicesGroups[serviceSumCounter] = programServicesGroups[serviceSumCounter] +
                                Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                        + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                                        + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(serviceSumCounter+1))).getText());
                        //System.out.println(programServicesGroups[serviceSumCounter]);

                    }
                }

                int autoServiceCounterMax= $$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) +
                        NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter) + "//table[@class=\"services\"]//tbody[@class=\"auto\"]//tr[@class=\"service\"]")).size();
                for (int serviceCounter = 1; serviceCounter <= autoServiceCounterMax; serviceCounter++) {

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(serviceCounter+1))).scrollTo();

                    for (int serviceSumCounter = 0; serviceSumCounter < programServicesGroups.length; serviceSumCounter++) {
                        programServicesGroups[serviceSumCounter] = programServicesGroups[serviceSumCounter] +
                                Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                                        + NewQuotationPage.ProgrammSection.GetAutoServiceByNumberREG(serviceCounter)
                                        + NewQuotationPage.ProgrammSection.GetSumForPeopleREG(serviceSumCounter+1))).getText());

                    }
                }

                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//table[@class=\"services\"]//tfoot//a[@class=\"qbtn qbtn-hideallprices\"]")).scrollTo().click();

            }

        }

    }

    public void GetSumsForDailyServices(double... programDailyServicesGroups) {

        $(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"][1]")).scrollTo();
        int dayCounterMax = $$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size();
        for (int dayCounter = 1; dayCounter <= dayCounterMax; dayCounter++) {
            System.out.println("      - считаем для дня номер "+ dayCounter);

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
                    for(int serviceSumCounter = 0; serviceSumCounter < programDailyServicesGroups.length; serviceSumCounter++) {
                        programDailyServicesGroups[serviceSumCounter] = programDailyServicesGroups[serviceSumCounter] +
                                Double.valueOf($(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)
                                        + NewQuotationPage.ProgrammSection.GetDailyServiceByNumberREG(dailyServiceCounter)
                                        + NewQuotationPage.ProgrammSection.GetDailySumForPeopleREG(serviceSumCounter+1))).getText());
                    }

                }
                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter)+ NewQuotationPage.ProgrammSection.hideAllDailyPricesREG)).click();
            }


        }

    }

    public void SetValuesForServicesInProgram(int ... values) {

        System.out.println("[-] Выставляем суммы для 3-х групп: 15, 20, 25");
        int dayCounterMax = $$(By.xpath("//div[@id=\"program\"]//div[@class=\"day\"]")).size();
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
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(1))).setValue(String.valueOf(values[0])).pressEnter();

                    CommonCode.WaitForProgruzkaSilent();


                    if(values.length>=2)
                    {$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(2))).click();

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(2))).setValue(String.valueOf(values[1])).pressEnter();

                    CommonCode.WaitForProgruzkaSilent();}


                    if(values.length>=3)
                    {$(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(3))).click();

                    $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                            + NewQuotationPage.ProgrammSection.GetMainServiceByNumberREG(serviceCounter)
                            + NewQuotationPage.ProgrammSection.GetSumForUnitREG(3))).setValue(String.valueOf(values[2])).pressEnter();

                    CommonCode.WaitForProgruzkaSilent();}
                }

                $(By.xpath(NewQuotationPage.ProgrammSection.GetADayByNumberREG(dayCounter) + NewQuotationPage.ProgrammSection.GetACityByNumberREG(cityCounter)
                        + "//tfoot//tr//td//a[@class=\"qbtn qbtn-hideallprices\"]")).shouldBe(visible).click();

            }
            System.out.println(CommonCode.OK);

        }

    }

}
