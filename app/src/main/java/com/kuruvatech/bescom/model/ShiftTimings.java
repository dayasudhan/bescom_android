package com.kuruvatech.bescom.model;

/**
 * Created by dayas on 16-03-2019.
 */

public class ShiftTimings {
    String nightshift_endtime;
    String nightshift_starttime;
    boolean nightshift_on;
    boolean dayshift_on;
    String dayshift_endtime;
    String dayshift_starttime;
    
    public String getNightshift_endtime() {
        return nightshift_endtime;
    }

    public void setNightshift_endtime(String nightshift_endtime) {
        this.nightshift_endtime = nightshift_endtime;
    }

    public String getNightshift_starttime() {
        return nightshift_starttime;
    }

    public void setNightshift_starttime(String nightshift_starttime) {
        this.nightshift_starttime = nightshift_starttime;
    }

    public boolean isNightshift_on() {
        return nightshift_on;
    }

    public void setNightshift_on(boolean nightshift_on) {
        this.nightshift_on = nightshift_on;
    }

    public boolean isDayshift_on() {
        return dayshift_on;
    }

    public void setDayshift_on(boolean dayshift_on) {
        this.dayshift_on = dayshift_on;
    }

    public String getDayshift_endtime() {
        return dayshift_endtime;
    }

    public void setDayshift_endtime(String dayshift_endtime) {
        this.dayshift_endtime = dayshift_endtime;
    }

    public String getDayshift_starttime() {
        return dayshift_starttime;
    }

    public void setDayshift_starttime(String dayshift_starttime) {
        this.dayshift_starttime = dayshift_starttime;
    }
    public String timings()
    {
        String ret = new String("Day Shift : ");
        ret = ret  + getDayshift_starttime() + "to" + getDayshift_endtime() +"\n";
        ret = ret  + "Night Shift: " + getNightshift_starttime() + "to" + getNightshift_endtime();
        return ret;
    }


}
