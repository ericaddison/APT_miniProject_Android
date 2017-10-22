package com.example.apt_miniproject_android.model;

public class StreamItemInfo {
    private long streamId;
    private String streamName;
    private String imageUrl;
    private String dateAdded;
    private String lat;
    private String lng;

    public long getId(){
        return streamId;
    }

    public String getStreamName(){
        return streamName;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getDateAdded(){
        return dateAdded;
    }

    public String getLat(){
        return lat;
    }

    public String getLng(){
        return lng;
    }
}
