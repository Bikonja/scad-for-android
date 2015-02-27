package com.igorloborec.vsite.HtmlParse;

import android.content.Context;
import android.util.Log;

import com.igorloborec.vsite.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Bikonja on 27.2.2015..
 */
public class Helper {
    private static final String LOG_TAG = Helper.class.getSimpleName();

    // Method copied from: http://snipplr.com/view/51608/
    static public String readStringFromResource(Context ctx, int resourceID) {
        StringBuilder contents = new StringBuilder();
        String sep = System.getProperty("line.separator");

        try {
            InputStream is = ctx.getResources().openRawResource(resourceID);

            BufferedReader input =  new BufferedReader(new InputStreamReader(is), 1024*8);
            try {
                String line = null;
                while (( line = input.readLine()) != null){
                    contents.append(line);
                    contents.append(sep);
                }
            }
            finally {
                input.close();
            }
        }
        catch (FileNotFoundException ex) {
            Log.e(LOG_TAG, "Couldn't find the file " + resourceID  + " " + ex);
            return "";
        }
        catch (IOException ex){
            Log.e(LOG_TAG, "Error reading file " + resourceID + " " + ex);
            return "";
        }

        return contents.toString();
    }
}
