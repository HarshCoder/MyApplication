package com.applications.h.prajapati.myapplication;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Harsh on 2016-05-15.
 */
/*
 * Data object that holds all of our information about a StackExchange Site.
 */
public class StackSite {

    private String name;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String description;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String link) {
        this.startDate = link;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }
    public String getEndDate(){
        return endDate;
    }
    public void setEndTime(String endTime){
        this.endTime = endTime;
    }
    public String getEndTime(){
        return endTime;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }



    @Override
    public String toString() {
        return "StackSite [name=" + name + ", startDate=" + startDate + ", startTime="
                + startTime + ", endDate=" + endDate + "]";
    }
}