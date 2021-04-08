package org.me.gcu.equakestartercode.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
//Implements parcelable to be allowed to save in InstanceState
public class Item implements Serializable, Comparable , Parcelable {

    private String title;
    private  String description;
    private String location;
    private String pubDate;
    private String depth;
    private String magnitude;
    private String category;
    private String geoLat;
    private String  geoLong;
    private String link;

    public Item(String title , String description , String link , String category , String pubDate , String geoLat  , String geoLong){
        this.title = title;
        this.description = description;
        this.link = link;
        this.category = category;
        this.pubDate = pubDate;
        this.geoLat = geoLat;
        this.geoLong = geoLong;
    }

    public Item(){}

    protected Item(Parcel in) {
        title = in.readString();
        description = in.readString();
        link = in.readString();
        pubDate = in.readString();
        category = in.readString();
        geoLat = in.readString();
        geoLong = in.readString();
        location = in.readString();
        depth = in.readString();
        magnitude = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    //Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getCategory() {
        return category;
    }

    public String getGeoLat() {
        return geoLat;
    }

    public String getGeoLong() {
        return geoLong;
    }

    public String getLocation() {
        return location;
    }

    public String getDepth() {
        return depth;
    }

    public String getMagnitude() {
        return magnitude;
    }



    //Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
        //Splits description into Array of strings
        //";" to split all values from each other
        //":" to split value tag from value itself
        //For example - "Location : JERSY"
        // ["Location" , "JERSY"] <- [1] the bit you want.
        String[] tokens = description.split(";|: ");
        //Takes unique items in description and sets to corresponding variables
        //Have to do loop for some reason
        // 3  = Location information , 7 = Depth inforamtion and 9 Magnitude information
        for(int i = 0 ; i < tokens.length ; i++) {
           if (i == 3)
           {
               this.setLocation(tokens[i].trim());
           }
           else if (i == 7)
           {
               this.setDepth(tokens[i].trim());
           }
           else if (i == 9)
           {
               this.setMagnitude(tokens[i].trim());
           }
        }
       }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setGeoLat(String geoLat) {
        this.geoLat = geoLat;
    }

    public void setGeoLong(String geoLong) {
        this.geoLong = geoLong;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeString(pubDate);
        dest.writeString(category);
        dest.writeString(geoLat);
        dest.writeString(geoLong);
        dest.writeString(location);
        dest.writeString(depth);
        dest.writeString(magnitude);
    }

    //This will fail if not comparing item
    //Input has to be Object because parcelable says so.
    @Override
    public int compareTo(Object o) {
        Item i = (Item) o;
        Float mag1 = Float.parseFloat(this.getMagnitude());
        Float mag2 = Float.parseFloat(i.getMagnitude());
        return mag1.compareTo(mag2);
    }
}
