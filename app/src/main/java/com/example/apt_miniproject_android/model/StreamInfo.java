package com.example.apt_miniproject_android.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by eric on 10/20/17.
 */

public class StreamInfo {

    private long id;
    private String owner;
    private String name;
    private String coverImageURL;
    private int numViews;
    private int numItems;
    private String dateAdded;
    private String newestDate;

    public long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getCoverImageURL() {
        return coverImageURL;
    }

    public int getNumViews() {
        return numViews;
    }

    public int getNumItems() {
        return numItems;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getNewestDate() {
        return newestDate;
    }

}
