package com.mikhaildev.test.taskmanager.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class presents model of task's data. This one implements Serializable interface.
 */
public class Task implements Serializable {

    @SerializedName("ID")
    private int id;
    private String title;
    private long date;
    private String text;
    private String longText;
    private String durationLimitText;
    private String price;
    private String locationText;
    private Location location;
    private int zoomLevel;
    private Price[] prices;
    private boolean translation;
    private String reflink;
    private String bingMapImage;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getLongText() {
        return longText;
    }

    public String getDurationLimitText() {
        return durationLimitText;
    }

    public String getPrice() {
        return price;
    }

    public String getLocationText() {
        return locationText;
    }

    public Location getLocation() {
        return location;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public Price[] getPrices() {
        return prices;
    }

    public boolean isTranslation() {
        return translation;
    }

    public String getReflink() {
        return reflink;
    }

    public String getBingMapImage() {
        return bingMapImage;
    }


    public static class Location implements Serializable{
        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "lon=" + lon +
                    ", lat=" + lat +
                    '}';
        }
    }


    public static class Price implements Serializable{
        private long price;
        private String description;

        public long getPrice() {
            return price;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "Price{" +
                    "price=" + price +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", longText='" + longText + '\'' +
                ", durationLimitText='" + durationLimitText + '\'' +
                ", price='" + price + '\'' +
                ", locationText='" + locationText + '\'' +
                ", location=" + location +
                ", zoomLevel=" + zoomLevel +
                ", prices=" + Arrays.toString(prices) +
                ", translation=" + translation +
                ", reflink='" + reflink + '\'' +
                ", bingMapImage='" + bingMapImage + '\'' +
                '}';
    }
}
