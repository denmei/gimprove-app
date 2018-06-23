package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.ListContent.ListItem;

import java.util.Date;
import java.util.LinkedList;

public class ExerciseUnit extends ListItem {

    private LinkedList<Set> sets;
    private String id;
    private String time_date;
    private String train_unit;
    private String exercise;

    public ExerciseUnit() {

    }

    @Override
    public String toString() {
        return exercise;
    }

    @Override
    public String getContent() {
        return sets.toString();
    }
}
