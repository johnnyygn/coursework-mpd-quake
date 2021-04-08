package org.me.gcu.equakestartercode.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import org.me.gcu.equakestartercode.utility.GetXMLString;
import org.me.gcu.equakestartercode.interfaces.IGetXMLResponse;
import org.me.gcu.equakestartercode.models.Item;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class ItemViewModel extends ViewModel implements IGetXMLResponse {
    //Mutable live data updates observers
    private MutableLiveData<ArrayList<Item>> items;
    private static String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    //If items == null, load data set items and return
    //Else return items
    public MutableLiveData<ArrayList<Item>> getItems() {
        if(items == null)
        {
            items = new MutableLiveData<ArrayList<Item>>();
            loadItems();
        }
        return items;
    }

    //Load new data and set items and therefore update observers
    public void loadNewItems() {
            items = new MutableLiveData<ArrayList<Item>>();
            loadItems();
    }

    private void loadItems() {
        GetXMLString getQuakeData = new GetXMLString(this);
        getQuakeData.execute(urlSource);
    }

    //get big xml string parse it into Arraylist and set it to items
    @Override
    public void returnXML(String xml) {
        //setValue cause main thread
        items.setValue(parseXML(xml));
    }

    //Converts big xml string into Arraylist of Items adn returns
    private ArrayList<Item> parseXML(@NotNull String XML) {
        Item item = new Item();
        ArrayList<Item> list = new ArrayList<Item>();

        try {
            String unull = XML.replace("null", "");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(unull));
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Found a start tag
                if (eventType == XmlPullParser.START_TAG) {
                    // Check which Tag we have
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        item = new Item();
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        // Now just get the associated text
                        String temp = xpp.nextText();
                        // Do something with text
                        Log.e("MyTag", "Title " + temp);
                        item.setTitle(temp);
                    } else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("description")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.e("MyTag", "Nut is desc " + temp);
                            item.setDescription(temp);
                        } else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("link")) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Washer is link" + temp);
                                item.setLink(temp);
                            } else
                                // Check which Tag we have
                                if (xpp.getName().equalsIgnoreCase("pubDate")) {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag", "Washer is pub " + temp);
                                    item.setPubDate(temp);
                                } else
                                    // Check which Tag we have
                                    if (xpp.getName().equalsIgnoreCase("category")) {
                                        // Now just get the associated text
                                        String temp = xpp.nextText();
                                        // Do something with text
                                        Log.e("MyTag", "Washer is  cat" + temp);
                                        item.setCategory(temp);
                                    } else
                                        // Check which Tag we have
                                        if (xpp.getName().equalsIgnoreCase("lat")) {
                                            // Now just get the associated text
                                            String temp = xpp.nextText();
                                            // Do something with text
                                            Log.e("MyTag", "Washer is lat " + temp);
                                            item.setGeoLat(temp);
                                        } else
                                            // Check which Tag we have
                                            if (xpp.getName().equalsIgnoreCase("long")) {
                                                // Now just get the associated text
                                                String temp = xpp.nextText();
                                                // Do something with text
                                                Log.e("MyTag", "Washer is " + temp);
                                                item.setGeoLong(temp);
                                            }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        list.add(item);
                    }
                }


                // Get the next event
                eventType = xpp.next();

            } // End of while

            return list;

            //return alist;
        } catch (XmlPullParserException ae1) {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing");
        }
        return list;
    }
}
