package com.nasser.poulet.conquest.server;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Thomas on 5/5/14.
 */
public class HTTP {
    private static final String USER_AGENT = "Conquest/Api";

    public static String sendPost(String apiname, String parameters) throws Exception {

        String url = "http://conquest.nagyzzer.com/api/api_"+apiname+".php";
        URL obj = new URL(url);
        HttpURLConnection  con = (HttpURLConnection ) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(parameters);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }

    public static String[] getPage(String pageUrl) throws Exception {

        System.out.println("Call get");

        URL obj = new URL(pageUrl);
        HttpsURLConnection con = (HttpsURLConnection ) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.flush();
        wr.close();


        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        //StringBuffer response = new StringBuffer();

        ArrayList<String> response = new ArrayList<String>();

        while ((inputLine = in.readLine()) != null) {
            response.add(inputLine);
        }
        in.close();

        String[] returnValue = new String[response.size()];
        returnValue = response.toArray(returnValue);

        //print result
        return returnValue;

    }
}
