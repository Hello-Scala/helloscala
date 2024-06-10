package com.helloscala.web.im;


import java.util.HashMap;
import java.util.Map;

public class RequestParamUtil {


    public static Map<String, String> urlSplit(String URL){
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = truncateUrlPage(URL);
        if(strUrlParam == null){
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for(String strSplit : arrSplit){
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            if(arrSplitEqual.length > 1){
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            }else{
                if(arrSplitEqual[0] != ""){
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }


    private static String truncateUrlPage(String strURL){
        String strAllParam=null;
        String[] arrSplit=null;
//        strURL=strURL.trim().toLowerCase();
        strURL=strURL.trim();
        arrSplit=strURL.split("[?]");
        if(strURL.length()>1){
            if(arrSplit.length>1){
                for (int i=1;i<arrSplit.length;i++){
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }
}


