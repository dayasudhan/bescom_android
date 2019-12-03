package com.kuruvatech.bescom.model;

import java.util.ArrayList;

/**
 * Created by gagan on 10/24/2017.
 */
public class FeedItem2 {
    String feedname;
    String feedno;
    ShiftTimings shiftTimings;
    public String getFeedname() {
        return feedname;
    }
    public void setFeedname(String feedname) {
        this.feedname = feedname;
    }
    public String getFeedno() {
        return feedno;
    }
    public void setFeedno(String feedno) {
        this.feedno = feedno;
    }
    public ShiftTimings getShiftTimings() {
        return shiftTimings;
    }
    public void setShiftTimings(ShiftTimings shiftTimings) {
        this.shiftTimings = shiftTimings;
    }
    public FeedItem2()
    {
        feedname = new String();
        feedno = new String();
        shiftTimings = new ShiftTimings();
    }
 }
