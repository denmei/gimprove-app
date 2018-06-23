package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.Utilities.RequestManager;

import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class User {

    private String name;
    private int id;
    private List<TrainUnit> trainUnits;
    private List<ExerciseUnit> exerciseUnits;
    private List<Set> sets;
    private static User instance;

    private User () {
        // TODO: delete this:
    }

    public static User getInstance() {
        if(User.instance == null) {
            User.instance = new User();
        }
        return User.instance;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return this.id;
    }

    public List<TrainUnit> getTrainUnits() {
        return this.trainUnits;
    }

    public List<ExerciseUnit> getExerciseUnits() {
        return this.exerciseUnits;
    }

    public void setUserAttributes(String name, int ID) {
        this.name = name;
        this.id = ID;
    }

    public void setTrainUnits(List<TrainUnit> trainUnits) {
        this.trainUnits = trainUnits;
    }

    public void setExerciseUnits(List<ExerciseUnit> exerciseUnits) {
        this.exerciseUnits = exerciseUnits;
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
    }

}
