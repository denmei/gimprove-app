package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.ListContent.ListItem;
import com.example.dennismeisner.gimprove.Utilities.DateFormater;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Date start_time_date;
    private Date end_time_date;
    private int user;

    public TrainUnit (String id, Date start_time_date, Date end_time_date) {
        this.id = id;
        this.start_time_date = start_time_date;
        this.end_time_date = end_time_date;
    }

    public TrainUnit(JSONObject jsonMsg) throws JSONException, ParseException {
        this.id = jsonMsg.getString("id");
        this.start_time_date = DateFormater.parseTimestamp(jsonMsg.getString("start_time_date"));
        this.end_time_date = DateFormater.parseTimestamp(jsonMsg.getString("end_time_date"));
        this.user = jsonMsg.getInt("user");
    }

    public Date getDate() {
        return this.end_time_date;
    }

    @Override
    public String toString() {
        return DateFormater.getDateString(start_time_date);
    }

    public String toExtString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.id + " ");
        builder.append(this.start_time_date + " ");
        builder.append(this.end_time_date);
        return builder.toString();
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
