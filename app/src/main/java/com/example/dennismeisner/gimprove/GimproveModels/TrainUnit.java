package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.ListContent.ListItem;

import java.util.Date;
import java.util.LinkedList;

public class TrainUnit extends ListItem {

    private LinkedList<ExerciseUnit> exerciseUnits;
    private String id;
    private String start_time_date;
    private String end_time_date;
    private int user;

    public TrainUnit(String id, String start_time_date, String end_time_date, Date date, int user) {
        // TODO: extend
    }

    @Override
    public String toString() {
        return date.toString();
    }

    @Override
    public String getContent() {
        return exerciseUnits.toString();
    }
}
