package com.example.dennismeisner.gimprove.GimproveModels;

import com.example.dennismeisner.gimprove.ListContent.ListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Set extends ListItem implements Serializable {

    private String id;
    private String exercise_unit;
    private Date date_time;
    private int repetitions;
    private double weight;
    private double[] durations;
    private Boolean auto_tracking;
    private Date last_update;

    public Set(int repetitions, double weight, Date date) {
        this.id = "NONE";
        this.repetitions = repetitions;
        this.weight = weight;
        this.date_time = date;
    }

    public Set(String id, Date date_time, int repetitions, double weight,
               double[] durations, Boolean auto_tracking, Date last_update) {
        this.id = id;
        this.date_time = date_time;
        this.repetitions = repetitions;
        this.weight = weight;
        this.durations = durations;
        this.auto_tracking = auto_tracking;
        this.last_update = new Date();
    }

    /**
     * Create new Set from Json-Msg by websocket.
     * @param jsonMsg with these keys: set_id, date_time, exercise_unit, repetitions, weight,
     *                durations, auto_tracking, last_update
     */
    public Set(JSONObject jsonMsg) throws JSONException, ParseException {
        this.id = jsonMsg.getString("set_id");
        this.date_time = parseTimestamp(jsonMsg.getString("date_time"));
        this.exercise_unit = "replace";
        this.repetitions = jsonMsg.getInt("repetitions");
        this.weight = jsonMsg.getDouble("weight");
        this.durations = parseStringToDoubleArray(jsonMsg.getString("durations"));
        this.auto_tracking = true;
        this.last_update = new Date();
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
        this.repetitions = repetitions;
        double[] newDurations = new double[this.repetitions];
        for(int i=0; i < newDurations.length; i++) {
            newDurations[i] = 0.0;
        }
        this.durations = newDurations;
        this.auto_tracking = false;
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
     * @param weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
        this.auto_tracking = false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append("Repetitions: " + repetitions + " - ");
        builder.append("Weight: " + weight);
        builder.append("Durations: " + Arrays.toString(durations));
        builder.append("Date:" + date_time.toString());
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
        retHash.put("set_id", id);
        retHash.put("weight", weight);
        retHash.put("rfid", User.getInstance().getRfid());
        // retHash.put("date_time", ); // TODO Format: %Y-%m-%dT%H:%M:%SZ
        // retHash.put("equipment_id", ); // TODO
        retHash.put("active", ""); // TODO
        retHash.put("exercise_name", ""); // TODO
        retHash.put("durations", Arrays.toString(durations));
        return retHash;
    }

}
