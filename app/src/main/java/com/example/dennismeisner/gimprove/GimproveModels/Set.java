package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.ListContent.ListItem;

import java.util.Date;

public class Set extends ListItem{

    private String id;
    private String date_time;
    private String exercise_unit;
    private int repetitions;
    private float weight;
    private String durations;
    private Boolean auto_tracking;
    private Date last_update;

    public Set(int repetitions, float weight, Date date) {
        this.repetitions = repetitions;
        this.weight = weight;
        this.date = date;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions, float[] durations) {
        if(durations.length == repetitions) {
            this.repetitions = repetitions;
        }
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append(date_time + " ");
        builder.append(repetitions+ " ");
        builder.append(weight);
        return builder.toString();
    }

    @Override
    public String getContent() {
        return null;
    }
}
