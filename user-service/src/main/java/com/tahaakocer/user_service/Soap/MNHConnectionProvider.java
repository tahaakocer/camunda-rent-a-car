
package com.tahaakocer.user_service.Soap;
//------------------------------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 9.3.3.2
//
// Created by Quasar Development 
//
// This class has been generated for test purposes only and cannot be used in any commercial project.
// To use it in commercial project, you need to generate this class again with Premium account.
// Check https://EasyWsdl.com/Payment/PremiumAccountDetails to see all benefits of Premium account.
//
// Licence: 9AEA2E6C3CE2CC4D4DC893FAA660F91A29CEB1D1BBCE867CF9F4C3C308CC90A11113BB642E205617A7D6315D7B22AADF619B71DDC1F1D7B32CF94D98C7315168
//------------------------------------------------------------------------
import java.net.URL;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public interface MNHConnectionProvider
{
    MNHResponseData sendRequest(String url,String requestBody,HashMap< String,String> httpHeaders, MNHRequestResultHandler handler,String contentType) throws java.lang.Exception;
}

class MNHHttpConnectionProvider implements MNHConnectionProvider{

    public void prepareRequest(HttpURLConnection url,String requestBody, MNHRequestResultHandler handler,String contentType ) throws java.lang.Exception {
        OutputStreamWriter wr = new OutputStreamWriter(url.getOutputStream());
        wr.write(requestBody);
        wr.flush();
    }

    @Override
    public MNHResponseData sendRequest(String url,String requestBody, HashMap< String, String> httpHeaders,MNHRequestResultHandler handler,String contentType ) throws java.lang.Exception
    {
        URL urlObject=new URL(url);
        HttpURLConnection connection=(HttpURLConnection)urlObject.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        for(Map.Entry<String, String> entry : httpHeaders.entrySet())
        {
            connection.setRequestProperty(entry.getKey(),entry.getValue());
        }
        connection.setRequestProperty("user-agent","https://easyWSDL.com Generator 9.3.3.2");
        prepareRequest(connection,requestBody,handler, contentType );


        MNHResponseData response=new MNHResponseData();
        for(Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet())
        {
            if(entry.getKey()!=null)
            {
                String values = "";
                for (String value : entry.getValue()) {
                    values = values + value;
                }
                response.getHeaders().put(entry.getKey(),values);
            }
        }
        
        try(InputStream dataStream = getResponseStream(connection,response,handler))
        {
            response.setBody(MNHHelper.streamToString(dataStream));
        }
        
        return response;
    }

    protected InputStream getResponseStream(
        HttpURLConnection url,
        MNHResponseData response,
        MNHRequestResultHandler handler
    ) throws java.lang.Exception
    {
        try{
            return url.getInputStream();
        }
        catch (IOException ex)
        {
            return url.getErrorStream();
        }
    }
}