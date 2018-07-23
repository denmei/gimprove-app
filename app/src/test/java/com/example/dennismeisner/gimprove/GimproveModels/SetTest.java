package com.example.dennismeisner.gimprove.GimproveModels;

// Static imports for assertion methods
import android.os.Build;

import com.example.dennismeisner.gimprove.GimproveModels.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import java.util.Date;

@RunWith(RobolectricTestRunner.class)
public class SetTest {
    private Set set;

    @Test
    public void test_creation_from_json() throws JSONException {
        JSONObject jsonmsg = new JSONObject();
        jsonmsg.put("id", "123456");

    }
}