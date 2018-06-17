package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.ListContent.ListItem;

import java.util.Date;
import java.util.LinkedList;

public class ExerciseUnit extends ListItem {

    private LinkedList<Set> sets;
    private Date date;

    public ExerciseUnit() {
        sets = new LinkedList<Set>();
    }

    @Override
    public String toString() {
        return date.toString();
    }

    @Override
    public String getContent() {
        return sets.toString();
    }
}
