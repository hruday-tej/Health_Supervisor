package com.example.healthsupervisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.http.Url;

public class DownloadUrl {

    public String readUrl(String myUrl) throws IOException{
        String data="";
        InputStream inputStream = null;
        HttpsURLConnection urlConnection = null;
        try {
            URL url = new URL(myUrl);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String Line = "";
            while ((Line = br.readLine())!=null){
                sb.append(Line);
            }

            data = sb.toString();
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            urlConnection.disconnect();
        }
    return data;
    }

}
