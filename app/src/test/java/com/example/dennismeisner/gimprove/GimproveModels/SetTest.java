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
    public void test_hashmap_transformation() {
        double [] durations = new double[2];
        durations[0] = 0.5;
        durations[1] = 0.9;
        HashMap<String, Object> setMap = this.set.getHashMap();
        Assert.assertEquals(setMap.get("repetitions"), 2);
        Assert.assertEquals(setMap.get("weight"), 20.0);
        Assert.assertEquals(setMap.get("durations"), durations);
        Assert.assertEquals(setMap.get("active"), "False");
        Assert.assertEquals(setMap.get("exercise_name"), "test_exercise");
        Assert.assertEquals(setMap.get("equipment_id"), "test_equipment");
    }
}