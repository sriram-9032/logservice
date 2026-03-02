package com.dtt.logs.service.impl;

import com.dtt.logs.Model.AggCount;
import com.dtt.logs.Model.AggCountSP;
import com.dtt.logs.Model.CountLog;
import com.dtt.logs.Model.CountSPLog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CentralLogAggCountImpl {


    private final MongoTemplate mongoTemplate;

    public CentralLogAggCountImpl(@Qualifier("centralLogMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Aggregation groupAggr(MatchOperation matchOperation)
    {
        GroupOperation g1= Aggregation.group().sum("$countOfServiceProviders").as("countOfServiceProviders").
                sum("$countOfCertificates").as("countOfCertificates").sum("$countOfSubscribers").as("countOfSubscribers")
                .sum("$countOfSignaturesWithXadesSuccess").as("countOfSignaturesWithXadesSuccess").sum("$countOfSignaturesWithPadesSuccess").as("countOfSignaturesWithPadesSuccess")
                .sum("$countOfSignaturesWithCadesSuccess").as("countOfSignaturesWithCadesSuccess").sum("$countOfSignaturesWithESealSuccess").as("countOfSignaturesWithESealSuccess")
                .sum("$countOfSignaturesWithDataSuccess").as("countOfSignaturesWithDataSuccess").sum("$countOfSignaturesWithXadesFailed").as("countOfSignaturesWithXadesFailed")
                .sum("$countOfSignaturesWithPadesFailed").as("countOfSignaturesWithPadesFailed").sum("$countOfSignaturesWithCadesFailed").as("countOfSignaturesWithCadesFailed")
                .sum("$countOfSignaturesWithESealFailed").as("countOfSignaturesWithESealFailed").sum("$countOfSignaturesWithDataFailed").as("countOfSignaturesWithDataFailed")
                .sum("$countOfAuthenticationsSuccess").as("countOfAuthenticationsSuccess").sum("$countOfAuthenticationsFailed").as("countOfAuthenticationsFailed");
        Aggregation aggregation = Aggregation.newAggregation(matchOperation,g1);
        return aggregation;
    }
    public Aggregation groupSPAggr(MatchOperation matchOperation)
    {
        GroupOperation g1= Aggregation.group()
                .sum("$countOfSignaturesWithXadesSuccess").as("countOfSignaturesWithXadesSuccess").sum("$countOfSignaturesWithPadesSuccess").as("countOfSignaturesWithPadesSuccess")
                .sum("$countOfSignaturesWithCadesSuccess").as("countOfSignaturesWithCadesSuccess").sum("$countOfSignaturesWithESealSuccess").as("countOfSignaturesWithESealSuccess")
                .sum("$countOfSignaturesWithDataSuccess").as("countOfSignaturesWithDataSuccess").sum("$countOfSignaturesWithXadesFailed").as("countOfSignaturesWithXadesFailed")
                .sum("$countOfSignaturesWithPadesFailed").as("countOfSignaturesWithPadesFailed").sum("$countOfSignaturesWithCadesFailed").as("countOfSignaturesWithCadesFailed")
                .sum("$countOfSignaturesWithESealFailed").as("countOfSignaturesWithESealFailed").sum("$countOfSignaturesWithDataFailed").as("countOfSignaturesWithDataFailed")
                .sum("$countOfAuthenticationsSuccess").as("countOfAuthenticationsSuccess").sum("$countOfAuthenticationsFailed").as("countOfAuthenticationsFailed");
        Aggregation aggregation = Aggregation.newAggregation(matchOperation,g1);
        return aggregation;
    }




    public JSONArray  getDayCountAgg(int year,String month,String day) throws JSONException {
        String identifier = year + "-" + month + "-" + day;
        MatchOperation matchOperation = Aggregation.match(new Criteria("identifier").is(identifier));
        Aggregation aggregation = groupAggr(matchOperation);
        List<AggCount> aggCountList = mongoTemplate.aggregate(aggregation,mongoTemplate.getCollectionName(CountLog.class), AggCount.class).getMappedResults();
        JSONArray array=new JSONArray();
        if(aggCountList.isEmpty())
        {
            array.put(new JSONObject(new AggCount(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L).toString()));
        }
        else{
            array.put(new JSONObject(aggCountList.get(0).toString()));

        }
        return array;
    }


    public JSONArray getMonthCountAgg(String month,int year) throws JSONException {
        String regex= "^"+year+"-"+month+"-(0[1-9]|[12][0-9]|3[01])$";
        MatchOperation matchOperation1 = Aggregation.match(new Criteria("identifier").regex(regex));
        Aggregation aggregation1= groupAggr(matchOperation1);
        List<AggCount> aggCountList = mongoTemplate.aggregate(aggregation1,mongoTemplate.getCollectionName(CountLog.class), AggCount.class).getMappedResults();
        JSONArray array=new JSONArray();
        if(aggCountList.isEmpty())
        {
            array.put(new JSONObject(new AggCount(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L).toString()));
        }
        else{
            array.put(new JSONObject(aggCountList.get(0).toString()));

        }
        return array;
    }
    public JSONArray getYearCountAgg(int year) throws JSONException {
        String regex= "^"+year+"-"+"(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
        MatchOperation matchOperation2 = Aggregation.match(new Criteria("identifier").regex(regex));
        Aggregation aggregation2= groupAggr(matchOperation2);
        List<AggCount> aggCountList = mongoTemplate.aggregate(aggregation2,mongoTemplate.getCollectionName(CountLog.class), AggCount.class).getMappedResults();
        JSONArray array=new JSONArray();
        if(aggCountList.isEmpty())
        {
            array.put(new JSONObject(new AggCount(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L).toString()));
        }
        else{
            array.put(new JSONObject(aggCountList.get(0).toString()));

        }
        return array;

    }
    public JSONArray getCumulativeAgg() throws JSONException {
        GroupOperation g1= Aggregation.group().sum("$countOfServiceProviders").as("countOfServiceProviders").
                sum("$countOfCertificates").as("countOfCertificates").sum("$countOfSubscribers").as("countOfSubscribers")
                .sum("$countOfSignaturesWithXadesSuccess").as("countOfSignaturesWithXadesSuccess").sum("$countOfSignaturesWithPadesSuccess").as("countOfSignaturesWithPadesSuccess")
                .sum("$countOfSignaturesWithCadesSuccess").as("countOfSignaturesWithCadesSuccess").sum("$countOfSignaturesWithESealSuccess").as("countOfSignaturesWithESealSuccess")
                .sum("$countOfSignaturesWithDataSuccess").as("countOfSignaturesWithDataSuccess").sum("$countOfSignaturesWithXadesFailed").as("countOfSignaturesWithXadesFailed")
                .sum("$countOfSignaturesWithPadesFailed").as("countOfSignaturesWithPadesFailed").sum("$countOfSignaturesWithCadesFailed").as("countOfSignaturesWithCadesFailed")
                .sum("$countOfSignaturesWitheSealFailed").as("countOfSignaturesWitheSealFailed").sum("$countOfSignaturesWithDataFailed").as("countOfSignaturesWithDataFailed")
                .sum("$countOfAuthenticationsSuccess").as("countOfAuthenticationsSuccess").sum("$countOfAuthenticationsFailed").as("countOfAuthenticationsFailed");
        Aggregation aggregation = Aggregation.newAggregation(g1);
        List<AggCount> aggCountList = mongoTemplate.aggregate(aggregation,mongoTemplate.getCollectionName(CountLog.class), AggCount.class).getMappedResults();
        JSONArray array=new JSONArray();
        if(aggCountList.isEmpty())
        {
            array.put(new JSONObject(new AggCount(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L).toString()));
        }
        else{
            array.put(new JSONObject(aggCountList.get(0).toString()));

        }
        return array;


    }

    public JSONArray getCumulativeCountSP(String serviceProviderName) throws JSONException {
        String regex= "^"+serviceProviderName+"_"+"(181[2-9]|18[2-9]\\d|19\\d\\d|2\\d{3}|30[0-3]\\d|304[0-8])"+"-"+"(0[1-9]|1[0-2])"+"-(0[1-9]|[12][0-9]|3[01])$";
        MatchOperation matchOperation= Aggregation.match(new Criteria("identifier").regex(regex));
        Aggregation aggregation1=groupSPAggr(matchOperation);
        List<AggCountSP> aggCountList = mongoTemplate.aggregate(aggregation1,mongoTemplate.getCollectionName(CountSPLog.class), AggCountSP.class).getMappedResults();
        JSONArray array=new JSONArray();
        if(aggCountList.isEmpty())
        {
            array.put(new JSONObject(new AggCount(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L).toString()));
        }
        else{
            array.put(new JSONObject(aggCountList.get(0).toString()));

        }
        return array;
    }


    public JSONArray getCumulativeCountSPWithList(List<String> serviceProviderName) throws JSONException {
        AggCountSP result = new AggCountSP(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L);
       for(String clientId : serviceProviderName){
           String regex= "^"+clientId+"_"+"(181[2-9]|18[2-9]\\d|19\\d\\d|2\\d{3}|30[0-3]\\d|304[0-8])"+"-"+"(0[1-9]|1[0-2])"+"-(0[1-9]|[12][0-9]|3[01])$";
           MatchOperation matchOperation= Aggregation.match(new Criteria("identifier").regex(regex));
           Aggregation aggregation1=groupSPAggr(matchOperation);
           List<AggCountSP> aggCountList = mongoTemplate.aggregate(aggregation1,mongoTemplate.getCollectionName(CountSPLog.class), AggCountSP.class).getMappedResults();
           if(!aggCountList.isEmpty()){
               AggCountSP result1 = aggCountList.get(0);
               result.setCountOfSignaturesWithXadesSuccess(result.getCountOfSignaturesWithXadesSuccess()+result1.getCountOfSignaturesWithXadesSuccess());
               result.setCountOfSignaturesWithXadesFailed(result.getCountOfSignaturesWithXadesFailed()+result1.getCountOfSignaturesWithXadesFailed());
               result.setCountOfSignaturesWithPadesSuccess(result.getCountOfSignaturesWithPadesSuccess()+result1.getCountOfSignaturesWithPadesSuccess());
               result.setCountOfSignaturesWithPadesFailed(result.getCountOfSignaturesWithPadesFailed()+result1.getCountOfSignaturesWithPadesFailed());
               result.setCountOfSignaturesWithCadesSuccess(result.getCountOfSignaturesWithCadesSuccess()+ result1.getCountOfSignaturesWithCadesSuccess());
               result.setCountOfSignaturesWithCadesFailed(result.getCountOfSignaturesWithCadesFailed()+ result1.getCountOfSignaturesWithCadesFailed());
               result.setCountOfSignaturesWithESealSuccess(result.getCountOfSignaturesWithESealSuccess()+result1.getCountOfSignaturesWithESealSuccess());
               result.setCountOfSignaturesWithESealFailed(result.getCountOfSignaturesWithESealFailed()+result1.getCountOfSignaturesWithESealFailed());
               result.setCountOfSignaturesWithDataSuccess(result.getCountOfSignaturesWithDataSuccess()+result1.getCountOfSignaturesWithDataSuccess());
               result.setCountOfSignaturesWithDataFailed(result.getCountOfSignaturesWithDataFailed()+result1.getCountOfSignaturesWithDataFailed());
               result.setCountOfAuthenticationsSuccess(result.getCountOfAuthenticationsSuccess()+result1.getCountOfAuthenticationsSuccess());
               result.setCountOfAuthenticationsFailed(result.getCountOfAuthenticationsFailed()+result1.getCountOfAuthenticationsFailed());
           }
       }
//
        JSONArray array=new JSONArray();
        array.put(new JSONObject(result.toString()));
        return array;
    }


    public AggCountSP getDayCountSPAgg(int year, String month, int day, String serviceProviderName) {
        String d= day<10?"0"+day:""+day;
        String identifier=serviceProviderName+"_"+year+"-"+month+"-"+d;
        System.out.println(identifier);
        MatchOperation matchOperation= Aggregation.match(new Criteria("identifier").is(identifier));
        Aggregation aggregation= groupSPAggr(matchOperation);
        List<AggCountSP> countSPLogList= mongoTemplate.aggregate(aggregation,mongoTemplate.getCollectionName( CountSPLog.class), AggCountSP.class).getMappedResults();
        if(countSPLogList.isEmpty()) {
            return new AggCountSP(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L);
        }
        return (countSPLogList.get(0));
    }

    public AggCountSP getDayCountSPAggForOid(int year, String month, int day, String serviceProviderName) {
        String d= day<10?"0"+day:""+day;
        String identifier=serviceProviderName+"_"+year+"-"+month+"-"+d;
        System.out.println(identifier);
        MatchOperation matchOperation= Aggregation.match(new Criteria("identifier").is(identifier));
        Aggregation aggregation= groupSPAggr(matchOperation);
        List<AggCountSP> countSPLogList= mongoTemplate.aggregate(aggregation,mongoTemplate.getCollectionName( CountSPLog.class), AggCountSP.class).getMappedResults();
        if(countSPLogList.isEmpty()) {
            return new AggCountSP(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L);
        }
        return (countSPLogList.get(0));
    }


    public AggCountSP getMonthCountSPAgg(String month,int year,String serviceProviderName)
    {
        System.out.println("Month :: "+month+"Year :: "+year+"serviceProviderName :: "+serviceProviderName);
        String regex= "^"+serviceProviderName+"_"+year+"-"+month+"-(0[1-9]|[12][0-9]|3[01])$";
        MatchOperation matchOperation1 = Aggregation.match(new Criteria("identifier").regex(regex));
        Aggregation aggregation1= groupSPAggr(matchOperation1);
        List<AggCountSP> aggCountList = mongoTemplate.aggregate(aggregation1,mongoTemplate.getCollectionName(CountSPLog.class), AggCountSP.class).getMappedResults();
        if(aggCountList.isEmpty())
        {
            System.out.println("Returning empty");
            return new AggCountSP(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L);
        }
        System.out.println("Not Returning Empty");
        return (aggCountList.get(0));
    }
    public AggCountSP getYearCountSPAgg(int year, String serviceProviderName){
        String regex= "^"+serviceProviderName+"_"+year+"-"+"(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
        MatchOperation matchOperation2 = Aggregation.match(new Criteria("identifier").regex(regex));
        Aggregation aggregation2= groupSPAggr(matchOperation2);
        List<AggCountSP> aggCountList = mongoTemplate.aggregate(aggregation2,mongoTemplate.getCollectionName(CountSPLog.class), AggCountSP.class).getMappedResults();
        if(aggCountList.isEmpty())
        {
            return new AggCountSP(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L);
        }
        return aggCountList.get(0);
    }

    public JSONArray getWeekCountSP(LocalDateTime date, String spName) throws JSONException {
        int wd= date.getDayOfWeek().getValue();
        int d= date.getDayOfMonth();
        int year= date.getYear();
        int month= date.getMonthValue();
        String m= month<10?"0"+month:""+month;
        JSONArray list=new JSONArray();
        int _wd=0; //nearest Sunday (starts with 0)
        for(int i=d-wd;i<=d;i++) // Day of Month - Day of Week = Sunday
        {
            JSONObject jsonObject =new JSONObject();
            AggCountSP weekDayCount=getDayCountSPAgg(year,m,i,spName);

            jsonObject.put("weekday",findWeekDay(_wd)); //start weekday from sunday and increment until current day
            jsonObject.put("count",new JSONObject(weekDayCount.toString()));
            list.put(jsonObject);
            _wd+=1;
        }
        return list;
    }

    public JSONArray getWeekCountSPForOid(LocalDateTime date, List<String> spName) throws JSONException {
        int wd= date.getDayOfWeek().getValue();
        int d= date.getDayOfMonth();
        int year= date.getYear();
        int month= date.getMonthValue();
        String m= month<10?"0"+month:""+month;
        JSONArray list=new JSONArray();
        int _wd=0; //nearest Sunday (starts with 0)

        for(int i=d-wd;i<=d;i++) // Day of Month - Day of Week = Sunday
        {
            AggCountSP result = new AggCountSP(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L);
            JSONObject jsonObject =new JSONObject();
            for(String clientId : spName){
                AggCountSP weekDayCount=getDayCountSPAggForOid(year,m,i,clientId);
                result.setCountOfSignaturesWithXadesSuccess(result.getCountOfSignaturesWithXadesSuccess()+weekDayCount.getCountOfSignaturesWithXadesSuccess());
                result.setCountOfSignaturesWithXadesFailed(result.getCountOfSignaturesWithXadesFailed()+weekDayCount.getCountOfSignaturesWithXadesFailed());
                result.setCountOfSignaturesWithPadesSuccess(result.getCountOfSignaturesWithPadesSuccess()+weekDayCount.getCountOfSignaturesWithPadesSuccess());
                result.setCountOfSignaturesWithPadesFailed(result.getCountOfSignaturesWithPadesFailed()+weekDayCount.getCountOfSignaturesWithPadesFailed());
                result.setCountOfSignaturesWithCadesSuccess(result.getCountOfSignaturesWithCadesSuccess()+ weekDayCount.getCountOfSignaturesWithCadesSuccess());
                result.setCountOfSignaturesWithCadesFailed(result.getCountOfSignaturesWithCadesFailed()+ weekDayCount.getCountOfSignaturesWithCadesFailed());
                result.setCountOfSignaturesWithESealSuccess(result.getCountOfSignaturesWithESealSuccess()+weekDayCount.getCountOfSignaturesWithESealSuccess());
                result.setCountOfSignaturesWithESealFailed(result.getCountOfSignaturesWithESealFailed()+weekDayCount.getCountOfSignaturesWithESealFailed());
                result.setCountOfSignaturesWithDataSuccess(result.getCountOfSignaturesWithDataSuccess()+weekDayCount.getCountOfSignaturesWithDataSuccess());
                result.setCountOfSignaturesWithDataFailed(result.getCountOfSignaturesWithDataFailed()+weekDayCount.getCountOfSignaturesWithDataFailed());
                result.setCountOfAuthenticationsSuccess(result.getCountOfAuthenticationsSuccess()+weekDayCount.getCountOfAuthenticationsSuccess());
                result.setCountOfAuthenticationsFailed(result.getCountOfAuthenticationsFailed()+weekDayCount.getCountOfAuthenticationsFailed());
            }

            jsonObject.put("weekday",findWeekDay(_wd)); //start weekday from sunday and increment until current day
            jsonObject.put("count",new JSONObject(result.toString()));
            list.put(jsonObject);
            _wd+=1;
        }
        return list;
    }



    public JSONArray getMonthCountsSP(LocalDateTime l,String spName) throws JSONException {
        int year= l.getYear();
        int prevYear=year-1;
        int month=l.getMonthValue();

        //get last 5 months count
        JSONArray array= new JSONArray();
        for(int i=month-4;i<=month;i++)
        {
            JSONObject jsonObject=new JSONObject();
            String m;
            AggCountSP monthList;
            String monthString;

            int n= (i<1)?12+i:i;

            if(i<1){
                m= ""+n; // i is negative; hence addition
                monthList=getMonthCountSPAgg(m,prevYear,spName);
                monthString = new DateFormatSymbols().getMonths()[n-1].substring(0,3).toUpperCase()+" "+prevYear;

            }
            else{
                m= n<10?"0"+n:""+n;
                 monthList=getMonthCountSPAgg(m,year,spName);
                 monthString = new DateFormatSymbols().getMonths()[n-1].substring(0,3).toUpperCase()+" "+year;

            }
            jsonObject.put("month",monthString);
            jsonObject.put("count",new JSONObject(monthList.toString()));//creates a JSON object to get required format
            array.put(jsonObject);
        }

        return array;
    }


    public JSONArray getMonthCountsSPWithList(LocalDateTime l,List<String> spName) throws JSONException {
        int year= l.getYear();
        int prevYear=year-1;
        int month=l.getMonthValue();



        //get last 5 months count
        JSONArray array= new JSONArray();
        for(int i=month-4;i<=month;i++)
        {
            JSONObject jsonObject=new JSONObject();
            AggCountSP result = new AggCountSP(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L);
            String m;
            AggCountSP monthList;
            String monthString;

            int n= (i<1)?12+i:i;
//

            if(i<1){
                m= ""+n; // i is negative; hence addition
                for(String clientId : spName){
                    monthList=getMonthCountSPAgg(m,prevYear,clientId);
                    result.setCountOfSignaturesWithXadesSuccess(result.getCountOfSignaturesWithXadesSuccess()+monthList.getCountOfSignaturesWithXadesSuccess());
                    result.setCountOfSignaturesWithXadesFailed(result.getCountOfSignaturesWithXadesFailed()+monthList.getCountOfSignaturesWithXadesFailed());
                    result.setCountOfSignaturesWithPadesSuccess(result.getCountOfSignaturesWithPadesSuccess()+monthList.getCountOfSignaturesWithPadesSuccess());
                    result.setCountOfSignaturesWithPadesFailed(result.getCountOfSignaturesWithPadesFailed()+monthList.getCountOfSignaturesWithPadesFailed());
                    result.setCountOfSignaturesWithCadesSuccess(result.getCountOfSignaturesWithCadesSuccess()+ monthList.getCountOfSignaturesWithCadesSuccess());
                    result.setCountOfSignaturesWithCadesFailed(result.getCountOfSignaturesWithCadesFailed()+ monthList.getCountOfSignaturesWithCadesFailed());
                    result.setCountOfSignaturesWithESealSuccess(result.getCountOfSignaturesWithESealSuccess()+monthList.getCountOfSignaturesWithESealSuccess());
                    result.setCountOfSignaturesWithESealFailed(result.getCountOfSignaturesWithESealFailed()+monthList.getCountOfSignaturesWithESealFailed());
                    result.setCountOfSignaturesWithDataSuccess(result.getCountOfSignaturesWithDataSuccess()+monthList.getCountOfSignaturesWithDataSuccess());
                    result.setCountOfSignaturesWithDataFailed(result.getCountOfSignaturesWithDataFailed()+monthList.getCountOfSignaturesWithDataFailed());
                    result.setCountOfAuthenticationsSuccess(result.getCountOfAuthenticationsSuccess()+monthList.getCountOfAuthenticationsSuccess());
                    result.setCountOfAuthenticationsFailed(result.getCountOfAuthenticationsFailed()+monthList.getCountOfAuthenticationsFailed());
                }

                monthString = new DateFormatSymbols().getMonths()[n-1].substring(0,3).toUpperCase()+" "+prevYear;

            }
            else{
                m= n<10?"0"+n:""+n;
                for(String clientId : spName){
                    monthList=getMonthCountSPAgg(m,year,clientId);
                    result.setCountOfSignaturesWithXadesSuccess(result.getCountOfSignaturesWithXadesSuccess()+monthList.getCountOfSignaturesWithXadesSuccess());
                    result.setCountOfSignaturesWithXadesFailed(result.getCountOfSignaturesWithXadesFailed()+monthList.getCountOfSignaturesWithXadesFailed());
                    result.setCountOfSignaturesWithPadesSuccess(result.getCountOfSignaturesWithPadesSuccess()+monthList.getCountOfSignaturesWithPadesSuccess());
                    result.setCountOfSignaturesWithPadesFailed(result.getCountOfSignaturesWithPadesFailed()+monthList.getCountOfSignaturesWithPadesFailed());
                    result.setCountOfSignaturesWithCadesSuccess(result.getCountOfSignaturesWithCadesSuccess()+ monthList.getCountOfSignaturesWithCadesSuccess());
                    result.setCountOfSignaturesWithCadesFailed(result.getCountOfSignaturesWithCadesFailed()+ monthList.getCountOfSignaturesWithCadesFailed());
                    result.setCountOfSignaturesWithESealSuccess(result.getCountOfSignaturesWithESealSuccess()+monthList.getCountOfSignaturesWithESealSuccess());
                    result.setCountOfSignaturesWithESealFailed(result.getCountOfSignaturesWithESealFailed()+monthList.getCountOfSignaturesWithESealFailed());
                    result.setCountOfSignaturesWithDataSuccess(result.getCountOfSignaturesWithDataSuccess()+monthList.getCountOfSignaturesWithDataSuccess());
                    result.setCountOfSignaturesWithDataFailed(result.getCountOfSignaturesWithDataFailed()+monthList.getCountOfSignaturesWithDataFailed());
                    result.setCountOfAuthenticationsSuccess(result.getCountOfAuthenticationsSuccess()+monthList.getCountOfAuthenticationsSuccess());
                    result.setCountOfAuthenticationsFailed(result.getCountOfAuthenticationsFailed()+monthList.getCountOfAuthenticationsFailed());
                }
                monthString = new DateFormatSymbols().getMonths()[n-1].substring(0,3).toUpperCase()+" "+year;

            }
            jsonObject.put("month",monthString);
            jsonObject.put("count",new JSONObject(result.toString()));//creates a JSON object to get required format
            array.put(jsonObject);
        }

        return array;
    }

    public JSONArray getYearCountSP(LocalDateTime l, String spName) throws JSONException {
        int year= l.getYear();
        JSONArray array= new JSONArray();
        for(int i=year-4;i<=year;i++)
        {
            AggCountSP yearCount= getYearCountSPAgg(i,spName);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("year",i);
            jsonObject.put("count",new JSONObject(yearCount.toString()));
            array.put(jsonObject);
        }
        return array;
    }

    public JSONArray getYearCountSPWithList(LocalDateTime l, List<String> spName) throws JSONException {
        int year= l.getYear();
        JSONArray array= new JSONArray();
        AggCountSP result = new AggCountSP(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L);
        for(int i=year-4;i<=year;i++)
        {
            for(String clientId : spName){
                AggCountSP yearCount= getYearCountSPAgg(i,clientId);
                result.setCountOfSignaturesWithXadesSuccess(result.getCountOfSignaturesWithXadesSuccess()+yearCount.getCountOfSignaturesWithXadesSuccess());
                result.setCountOfSignaturesWithXadesFailed(result.getCountOfSignaturesWithXadesFailed()+yearCount.getCountOfSignaturesWithXadesFailed());
                result.setCountOfSignaturesWithPadesSuccess(result.getCountOfSignaturesWithPadesSuccess()+yearCount.getCountOfSignaturesWithPadesSuccess());
                result.setCountOfSignaturesWithPadesFailed(result.getCountOfSignaturesWithPadesFailed()+yearCount.getCountOfSignaturesWithPadesFailed());
                result.setCountOfSignaturesWithCadesSuccess(result.getCountOfSignaturesWithCadesSuccess()+ yearCount.getCountOfSignaturesWithCadesSuccess());
                result.setCountOfSignaturesWithCadesFailed(result.getCountOfSignaturesWithCadesFailed()+ yearCount.getCountOfSignaturesWithCadesFailed());
                result.setCountOfSignaturesWithESealSuccess(result.getCountOfSignaturesWithESealSuccess()+yearCount.getCountOfSignaturesWithESealSuccess());
                result.setCountOfSignaturesWithESealFailed(result.getCountOfSignaturesWithESealFailed()+yearCount.getCountOfSignaturesWithESealFailed());
                result.setCountOfSignaturesWithDataSuccess(result.getCountOfSignaturesWithDataSuccess()+yearCount.getCountOfSignaturesWithDataSuccess());
                result.setCountOfSignaturesWithDataFailed(result.getCountOfSignaturesWithDataFailed()+yearCount.getCountOfSignaturesWithDataFailed());
                result.setCountOfAuthenticationsSuccess(result.getCountOfAuthenticationsSuccess()+yearCount.getCountOfAuthenticationsSuccess());
                result.setCountOfAuthenticationsFailed(result.getCountOfAuthenticationsFailed()+yearCount.getCountOfAuthenticationsFailed());
            }

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("year",i);
            jsonObject.put("count",new JSONObject(result.toString()));
            array.put(jsonObject);
        }
        return array;
    }

    public String findWeekDay(int wd)
    {
       if(wd!=0)
       {
           return DayOfWeek.of(wd).name().substring(0,3).toUpperCase();
       }
       return DayOfWeek.of(7).name().substring(0,3).toUpperCase();
    }

}
