package com.example.dennismeisner.gimprove.GimproveModels;

import java.util.Date;

public class Set {

    private int repetitions;
    private float weight;
    private Date date;
    private float[] durations;

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
}
