package com.matthewschwegler.rssfeeddownloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Matt Schwegler on 2/15/2016.
 */
public class ParseApplications {
    private String xmlData;
    private ArrayList<Application> applications;

    public ParseApplications(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<Application>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public boolean process() {
        boolean status = true;
        Application currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factor = XmlPullParserFactory.newInstance();
            factor.setNamespaceAware(true);
            XmlPullParser xpp = factor.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int evenType = xpp.getEventType();

            while (evenType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (evenType) {
                    case XmlPullParser.START_TAG:
//                        Log.d("ParseApplications", "Starting tag for " + tagName);
                        if (tagName.equalsIgnoreCase("entry")) {
                            inEntry = true;
                            currentRecord = new Application();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
//                        Log.d("ParseApplications", "Ending tag for " + tagName);
                        if(inEntry) {
                            // Once finished with entry add it to array
                            if(tagName.equalsIgnoreCase("entry")) {
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if(tagName.equalsIgnoreCase("name")) {
                                currentRecord.setName(textValue);
                            } else if(tagName.equalsIgnoreCase("artist")) {
                                currentRecord.setArtist(textValue);
                            } else if(tagName.equalsIgnoreCase("releaseDate")) {
                                currentRecord.setReleaseDate(textValue);
                            }
                        }
                        break;
                    default:
                        //Nothing else to do
                }
                evenType = xpp.next();
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        for(Application app : applications) {
            Log.d("ParseApplications", "**************");
            Log.d("ParseApplications", "Name: " + app.getName());
            Log.d("ParseApplications", "Artist: " + app.getArtist());
            Log.d("ParseApplications", "Release Date: " + app.getReleaseDate());
        }
        return true;
    }
}
