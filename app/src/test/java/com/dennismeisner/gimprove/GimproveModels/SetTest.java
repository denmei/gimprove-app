package com.dennismeisner.gimprove.GimproveModels;

// Static imports for assertion methods

import com.dennismeisner.gimprove.GimproveModels.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.text.ParseException;
import java.util.HashMap;

@RunWith(RobolectricTestRunner.class)
public class SetTest {
    private Set set;

    @Before
    public void create_sample_set() {
        double [] durations = new double[2];
        durations[0] = 0.5;
        durations[1] = 0.9;
        // this.set = new Set("test_id", new Date(), 2, 20.0, durations, false, false, "test_exercise", "test_equipment");
    }

    @Test
    public void test_creation_from_json() throws JSONException, ParseException {
        JSONObject msg = new JSONObject();
        msg.put("id", "2838cbf4-ff49-4a9a-a3c1-e230829856d6");
        msg.put("date_time", "2018-05-28T19:19:57.115408+02:00");
        msg.put("durations", "[17, 1, 19, 9, 11]");
        msg.put("exercise_unit","aec47ebb-6fd2-45d2-a8b2-d0767a455d88");
        msg.put("repetitions", 5);
        msg.put("weight", 5);
        msg.put("rfid", "0006921147");
        msg.put("equipment_id", "null");
        msg.put("exercise_name", "Lat Pulldown");

        Set testSet = new Set(msg);
    }

    @Test
    /**
     * The hashmap of the set must contain all relevant values for the rest-api with the correct names.
     */
    public void test_hashmap_transformation() {
        HashMap<String, Object> setMap = this.set.getHashMap();
        Assert.assertEquals(2, setMap.get("repetitions"));
        Assert.assertEquals(20.0, setMap.get("weight"));
        Assert.assertEquals("False", setMap.get("active"));
        Assert.assertEquals("test_exercise", setMap.get("exercise_name"));
        Assert.assertEquals("test_equipment", setMap.get("equipment_id"));
        System.out.println(setMap.toString());
        Assert.assertEquals(7, setMap.size());
    }
}