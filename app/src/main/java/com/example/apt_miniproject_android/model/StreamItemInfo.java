package com.example.apt_miniproject_android.model;

public class StreamItemInfo {
    private String dateadded;
    private String imageurl;
    private String streamname;
    private long streamid;
    private String lat;
    private String lng;

    public long getId(){
        return streamid;
    }

    public String getStreamName(){
        if(streamname == null || streamname.trim() == "") return "0";
        else return streamname.trim();
    }

    public String getImageUrl(){
        if(imageurl == null || imageurl.trim() == "") return "0";
        else return imageurl.trim();
    }

    public String getDateAdded(){
        if(dateadded == null || dateadded.trim() == "") return "1900-01-01 00:00:00";
        else return dateadded.trim();
    }

    public String getLat(){
        if(lat == null || lat.trim() == "") return "0";
        else return lat.trim();
    }

    public String getLng(){
        if(lng == null || lng.trim() == "") return "0";
        else return lng.trim();
    }
}
