package com.dennismeisner.gimprove.GimproveModels;

import com.dennismeisner.gimprove.ListContent.ListItem;

import java.util.LinkedList;

public class ExerciseUnit extends ListItem {

    private LinkedList<Set> sets;
    private String id;
    private String time_date;
    private String train_unit;
    private String exercise;

    public ExerciseUnit() {

    }

    public String getTrainUnit() {
        return this.train_unit;
    }

    @Override
    public String toString() {
        return exercise;
    }

    @Override
    public String getContent() {
        return sets.toString();
    }

    @Override
    public String getId() {
        return id;
    }
}
