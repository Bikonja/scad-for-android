/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.data.WebScraperProvider;


import android.os.AsyncTask;
import android.util.Log;

import com.igorloborec.scad.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Bikonja on 5.3.2015..
 */
public class Helper {
    public static String GetHtmlForUrl(String url, String authToken) throws MalformedURLException {
        String html = null;

        final DefaultHttpClient httpClient = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(url);

        BasicClientCookie authCookie;
        List<HttpCookie> cookies = HttpCookie.parse(authToken);

        if (cookies != null && !cookies.isEmpty()) {
            HttpCookie cookie = cookies.get(0);
            authCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
            authCookie.setDomain(cookie.getDomain());
            authCookie.setPath(cookie.getPath());
            httpClient.getCookieStore().addCookie(authCookie);

            /*try {
                html = new AsyncTask<Void, Void, String>(){
                    @Override
                    protected String doInBackground(Void... params) {
                        String html = null;*/

                        try {
                            HttpResponse response = httpClient.execute(httpGet);
                            if (response.getStatusLine().getStatusCode() != 403) {
                                html = EntityUtils.toString(response.getEntity());
                            } else {
                                Log.d("SCAD", "Access denied");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return html;
                    /*}
                }.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }*/
        }

        return html;
    }

    public static String GetNodeTextWithNewLines(Node node) {
        String text = "";

        if (node != null) {
            for (Node childNode : node.childNodes()) {
                if (childNode.nodeName().toLowerCase().equals("br")) {
                    text += "\n";
                } else {
                    if (childNode instanceof TextNode) {
                        text += childNode;
                    } else {
                        text += GetNodeTextWithNewLines(childNode);
                    }
                }
            }
        }

        return text;
    }
}
