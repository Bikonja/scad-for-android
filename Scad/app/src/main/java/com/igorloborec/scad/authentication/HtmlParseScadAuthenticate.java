/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.authentication;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bikonja on 3.2.2015..
 */
public class HtmlParseScadAuthenticate implements ScadAuthenticate {
    private static final String LOG_TAG = ScadAuthenticate.class.getSimpleName();

    @Override
    public String userSignIn(String user, String pass, String portalUrl, String authType) throws Exception {
        /*
        Code based on:
        https://github.com/Udinic/AccountAuthenticator/blob/master/src/com/udinic/accounts_authenticator_example/authentication/ParseComServerAuthenticate.java
        */

        Log.d(LOG_TAG, "userSignIn");

        String authtoken = null;



        /*
        URL url = new URL(portalUrl + "/?q=inlo&destination=inlo");
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");

        String data = "name=" + URLEncoder.encode(user, "UTF-8") +
                "&pass=" + URLEncoder.encode(pass, "UTF-8") +
                "&op=Log+in&form_id=user_login_block";

        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
        writer.write(data);
        writer.flush();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        writer.close();
        reader.close();

        urlConnection.disconnect();
        authtoken = urlConnection.getHeaderField("Set-Cookie");

*/


        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = portalUrl + "/?q=inlo&destination=inlo";

        HttpGet httpGet = new HttpGet(url);
        HttpResponse getResponse = httpClient.execute(httpGet);

        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("name", user));
        params.add(new BasicNameValuePair("pass", pass));
        params.add(new BasicNameValuePair("op", "Log+in"));
        params.add(new BasicNameValuePair("form_id", "user_login_block"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
        httpPost.setEntity(formEntity);

        //httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        //httpPost.addHeader("Content-Length", "" + formEntity.getContentLength());

        try {
            HttpResponse response = httpClient.execute(httpPost);

            String responseString = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 302) {
                Log.d(LOG_TAG, "userSignIn Error: " + responseString);
                throw new Exception("Error signing-in: " + responseString);
            }

            List<Cookie> cookies = httpClient.getCookieStore().getCookies();
            if (cookies != null && !cookies.isEmpty()) {
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();
                    if (name.startsWith("SESS")) {
                        authtoken = String.format("%s=%s; path=%s; domain=%s", name, cookie.getValue(), cookie.getPath(), cookie.getDomain());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authtoken;
    }
}
