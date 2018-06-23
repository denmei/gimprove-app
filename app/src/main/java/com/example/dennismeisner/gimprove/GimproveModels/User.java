package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.Utilities.RequestManager;

import java.net.URL;
import java.util.Date;
import java.util.LinkedList;

public class User {

    private String name;
    private LinkedList<TrainUnit> trainUnits;
    private static User instance;

    private User () {
        // TODO: delete this:
        trainUnits = new LinkedList<TrainUnit>();
        TrainUnit testUnit = new TrainUnit(new Date());
        trainUnits.add(testUnit);
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

    public void setUserAttributes(String name) {
        this.name = name;
    }

    public LinkedList<TrainUnit> getTrainUnits() {
        return this.trainUnits;
    }

    public void setTrainUnits(LinkedList<TrainUnit> trainUnits) {
        this.trainUnits = trainUnits;
    }

}
