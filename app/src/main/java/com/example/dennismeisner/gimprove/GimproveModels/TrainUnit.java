package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.ListContent.ListItem;

import java.util.Date;
import java.util.LinkedList;

public class TrainUnit extends ListItem {

    private LinkedList<ExerciseUnit> exerciseUnits;
    private Date date;

    public TrainUnit(Date date) {
        // TODO: extend
        this.date = date;
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
