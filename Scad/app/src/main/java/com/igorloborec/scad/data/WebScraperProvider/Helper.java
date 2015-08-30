/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.data.WebScraperProvider;


import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.igorloborec.scad.AccessDeniedException;
import com.igorloborec.scad.R;

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

/**
 * Created by Bikonja on 5.3.2015..
 */
public class Helper {
    public static String GetHtmlForUrl(String url, String authToken) throws MalformedURLException, AccessDeniedException {
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

            try {
                HttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() != 403) {
                    html = EntityUtils.toString(response.getEntity());
                } else {
                    Log.d("SCAD", "Access denied");
                    throw new AccessDeniedException("Access denied for page " + url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public static class Preferences {
        public static String GetDateFormatString(Activity activity) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            return preferences.getString(activity.getResources().getString(R.string.PREFERENCE_CALENDAR_DATE_FORMAT), activity.getResources().getStringArray(R.array.Calendar_date_format_strings)[0]);
        }
    }
}

