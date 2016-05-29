package com.applications.h.prajapati.myapplication;

/**
 * Created by Harsh on 2016-05-15.
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class SitesXmlPullParser {

    static final String KEY_SITE = "viewentry";
    static final String KEY_NAME = "entrydata";
    static final String KEY_LINK = "link";
    static final String KEY_ABOUT = "EventName";
    static final String KEY_IMAGE_URL = "image";
    private static int i = 0;
    private static List<StackSite> stackSites;

    public static List<StackSite> getStackSitesFromFile(Context ctx) {

        // List of StackSites that we will return
        stackSites = new ArrayList<StackSite>();

        // temp holder for current StackSite while parsing
        StackSite curStackSite = null;
        // temp holder for current text value while parsing
        String curText = "";

        try {
            // Get our factory and PullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            // Open up InputStream and Reader of our file.
            FileInputStream fis = ctx.openFileInput("StackSites.xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            // point the parser to our file.
            xpp.setInput(reader);

            // get initial eventType
            int eventType = xpp.getEventType();

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the current tag
                String tagname = xpp.getName();

                // React to different event types appropriately
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase(KEY_SITE)) {
                            // If we are starting a new <site> block we need
                            //a new StackSite object to represent it
                            curStackSite = new StackSite();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        //grab the current text so we can use it in END_TAG event
                        curText = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase(KEY_SITE)) {
                            // if </site> then we are done with current Site
                            // add it to the list.
                            stackSites.add(curStackSite);
                            i = 0;
                        }
                        else if (tagname.equalsIgnoreCase(KEY_NAME)) {
                            // if </site> then we are done with current Site
                            // add it to the list.
                            if(i == 0) {
                                curStackSite.setName(curText);
                            }else if(i == 5){
                                curStackSite.setStartDate(curText);
                            }else if(i == 6){
                                curStackSite.setStartTime(curText);
                            }else if(i == 7){
                                curStackSite.setEndDate(curText);
                            }else if(i == 8){
                                curStackSite.setEndTime(curText);
                            }else if(i == 10){
                                curStackSite.setDescription(curText);
                            }
                            i++;
                        }
                        break;

                    default:
                        break;
                }
                //move on to next iteration
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the populated list.
        return stackSites;
    }
    public static int getStackSize()
    {
        return stackSites.size();
    }
}