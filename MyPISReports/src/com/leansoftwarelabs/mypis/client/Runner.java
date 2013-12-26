package com.leansoftwarelabs.mypis.client;

import com.leansoftwarelabs.mypis.domain.TrackingCategory;
import com.leansoftwarelabs.mypis.domain.TrackingValue;
import com.leansoftwarelabs.mypis.service.ServiceFacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public class Runner {
    public static void main(String[] args) {
        Runner runner = new Runner();
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH,0);
        Date startDate = cal.getTime();
        List<Object[]> trackingInfoList = new ArrayList<Object[]>();
        ServiceFacade serviceFacade = new ServiceFacade();
        List<TrackingCategory> trackingCategoryList = serviceFacade.findAll(TrackingCategory.class);
        for(TrackingCategory category: trackingCategoryList){
            trackingInfoList.add(new Object[] { category, "All" });
        }
        
        List<Object[]> result = serviceFacade.getAccountSummaryData(81, startDate, endDate, trackingInfoList);
        for(Object[] obj: result){
            System.out.println();
            for(int i = 0; i< obj.length; i++){
                System.out.print(obj[i]);
                System.out.print("\t");
            }
        }
//        
//        System.out.println("------>>>>");
//        System.out.println(runner.getAccountSummaryData(75, startDate, endDate, trackingInfoList));
    }

 
}
