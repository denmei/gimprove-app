package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.ListContent.ListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents a single set.
 */
public class Set extends ListItem implements Serializable {

    private String id;
    private String exercise_unit;
    private Date dateTime;
    private int repetitions;
    private double weight;
    private double[] durations;
    private Boolean autoTracking;
    private Boolean active;
    private Date lastUpdate;
    private String exerciseName;
    private String equipmentId;

    public Set(String id, Date date_time, int repetitions, double weight,
               double[] durations, Boolean auto_tracking, Boolean active, String exercise_name,
               String equipmentId) {
        this.id = id;
        this.dateTime = date_time;
        this.repetitions = repetitions;
        this.weight = weight;
        this.durations = durations;
        this.autoTracking = auto_tracking;
        this.active = active;
        this.exerciseName = exercise_name;
        this.equipmentId = equipmentId;
    }

    /**
     * Create new Set with Json-Msg from websocket.
     * @param jsonMsg with these keys: set_id, date_time, exercise_unit, repetitions, weight,
     *                durations, auto_tracking, last_update
     */
    public Set(JSONObject jsonMsg) throws JSONException, ParseException {
        this.id = jsonMsg.getString("set_id");
        this.dateTime = parseTimestamp(jsonMsg.getString("date_time"));
        this.exercise_unit = "replace";
        this.exerciseName = jsonMsg.getString("exercise_name");
        this.active = jsonMsg.getBoolean("active");
        this.repetitions = jsonMsg.getInt("repetitions");
        this.weight = jsonMsg.getDouble("weight");
        this.durations = parseStringToDoubleArray(jsonMsg.getString("durations"));
        this.autoTracking = true;
        this.equipmentId = jsonMsg.getString("equipment_id");
        this.lastUpdate = new Date();
    }

    /**
     * Converts a String of double values to an array of doubles.
     * @param arrayAsString String with doubles in this format: "[0.0, 0.1]"
     * @return array of doubles that contains the values of the input string
     */
    private double[] parseStringToDoubleArray(String arrayAsString) {
        System.out.println(arrayAsString);
        String[] splitArray = arrayAsString
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll("\\s", "")
                .split(",");
        double[] durations = new double[splitArray.length];
        for(int i = 0; i < splitArray.length; i++) {
            durations[i] = Double.parseDouble(splitArray[i]);
        }
        return durations;
    }

    /**
     * Returns the number of repetitions of the set.
     * @return number of repetitions
     */
    public int getRepetitions() {
        return repetitions;
    }

    /**
     * Returns the repetitions' durations
     * @return Array of durations
     */
    public double[] getDurations() {
        return this.durations;
    }

    /**
     * Sets the value of the repetitions. Since the durations of the single repetitions are also
     * affected, all durations are set to 0.0. Auto_tracking is set to false.
     * @param repetitions
     */
    public void setRepetitions(int repetitions) {
        if (repetitions >= 0) {
            this.repetitions = repetitions;
            double[] newDurations = new double[this.repetitions];
            for(int i=0; i < newDurations.length; i++) {
                newDurations[i] = 0.0;
            }
            this.durations = newDurations;
            this.autoTracking = false;
        }
    }

    /**
     * Returns the weight of the set.
     * @return Weight of the set.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the set. Auto_tracking will be set to false.
     * @param weight new weight, must be >= 0.
     */
    public void setWeight(double weight) {
        if (weight >= 0) {
            this.weight = weight;
            this.autoTracking = false;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append("Repetitions: " + repetitions + " - ");
        builder.append("Weight: " + weight);
        builder.append("Durations: " + Arrays.toString(durations));
        builder.append("Date:" + dateTime.toString());
        builder.append("Exercise Unit: " + exercise_unit);
        return builder.toString();
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getExerciseUnit() {
        return this.exercise_unit;
    }

    public HashMap<String, Object> getHashMap() {

        HashMap<String, Object> retHash = new HashMap<>();
        retHash.put("repetitions", repetitions);
        retHash.put("weight", weight);
        retHash.put("rfid", User.getInstance().getRfid());
        retHash.put("equipment_id", this.equipmentId);
        if(active) {
            retHash.put("active", "True");
        } else {
            retHash.put("active", "False");
        }
        retHash.put("exercise_name", this.exerciseName);
        retHash.put("durations", Arrays.toString(durations));
        return retHash;
    }

}
