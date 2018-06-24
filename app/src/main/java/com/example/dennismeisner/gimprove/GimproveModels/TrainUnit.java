package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.ListContent.ListItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class TrainUnit extends ListItem {

    private List<ExerciseUnit> exerciseUnits;
    private String id;
    private String start_time_date;
    private Date end_time_date;
    private int user;

    public TrainUnit() {

    }

    @Override
    public String toString() {
        try {
            Date date = this.parseTimestamp(start_time_date);
        } catch (ParseException e) {
            e.printStackTrace();
            return start_time_date;
        }
        return dateFormat.format(date);
    }

    @Override
    public String getContent() {
        return exerciseUnits.toString();
    }

    @Override
    public String getId() {
        return id;
    }
}
