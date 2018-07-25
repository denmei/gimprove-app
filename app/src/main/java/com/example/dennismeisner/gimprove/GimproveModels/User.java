package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.Utilities.RequestManager;

import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Model class for the user.
 * Holds relevant training figures like TrainUnits, ExerciseUnits and Sets of the current user.
 * Implemented as Singleton.
 */
public class User {

    private String name;
    private int id;
    private String rfid;
    private List<TrainUnit> trainUnits;
    private List<ExerciseUnit> exerciseUnits;
    private List<Set> sets;
    private static User instance;
    private boolean initialized;

    private User () {
        this.sets = new LinkedList<Set>();
        this.exerciseUnits = new LinkedList<ExerciseUnit>();
        this.trainUnits = new LinkedList<TrainUnit>();
    }

    public static User getInstance() {
        if(User.instance == null) {
            User.instance = new User();
            User.instance.initialized = false;
        }
        return User.instance;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return this.id;
    }

    public String getRfid() {
        return this.rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public List<TrainUnit> getTrainUnits() {
        return this.trainUnits;
    }

    public List<ExerciseUnit> getExerciseUnits() {
        return this.exerciseUnits;
    }

    /**
     * Returns a list of the ExerciseUnits that belong to the defined Trainunit.
     * @param trainUnitId Trainunit to be filtered for.
     * @return List of ExerciseUnits that belong to the specified Trainunit.
     */
    public List<ExerciseUnit> getExerciseUnitsByTrainUnit(String trainUnitId) {
        List<ExerciseUnit> relevantUnits = new LinkedList<ExerciseUnit>();
        for(ExerciseUnit unit:this.exerciseUnits) {
            if(unit.getTrainUnit().equals(trainUnitId)) {
                relevantUnits.add(unit);
            }
        }
        return relevantUnits;
    }

    /**
     * Returns a list of the Sets that belong to the defined ExerciseUnit.
     * @param exerciseUnitId ExerciseUnit to be filtered for.
     * @return List of Sets that belong to the specified ExerciseUnit.
     */
    public List<Set> getSetsByExerciseUnits(String exerciseUnitId) {
        List<Set> relevantUnits = new LinkedList<>();
        for(Set set:this.sets) {
            if(set.getExerciseUnit().equals(exerciseUnitId)) {
                relevantUnits.add(set);
            }
        }
        return relevantUnits;
    }

    /**
     * Allows to set basic attributes of the user. Can only be used once after the initialisation.
     * @param name Name of the user.
     * @param ID User-Id.
     * @throws UserInitializationException: Is thrown if the method is tried to be executed more
     * than once.
     */
    public void setUserAttributes(String name, int ID, String rfid) {
        this.name = name;
        this.id = ID;
        this.rfid = rfid;
        this.initialized = true;
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

    public class UserInitializationException extends RuntimeException {

        public UserInitializationException(String message) {
            super(message);
        }
    }

}
