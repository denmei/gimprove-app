package com.example.dennismeisner.gimprove.GimproveModels;

// Static imports for assertion methods
import android.os.Build;

import com.example.dennismeisner.gimprove.GimproveModels.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import java.util.Date;
import java.util.HashMap;

@RunWith(RobolectricTestRunner.class)
public class SetTest {
    private Set set;

    @Before
    public void create_sample_set() {
        double [] durations = new double[2];
        durations[0] = 0.5;
        durations[1] = 0.9;
        this.set = new Set("test_id", new Date(), 2, 20.0, durations, false, false, "test_exercise", "test_equipment");
    }

    @Test
    public void test_creation_from_json() {

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