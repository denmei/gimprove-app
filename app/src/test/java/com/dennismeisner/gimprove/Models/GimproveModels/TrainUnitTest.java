package com.dennismeisner.gimprove.Models.GimproveModels;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.text.ParseException;

@RunWith(RobolectricTestRunner.class)
public class TrainUnitTest {

    @Test
    public void testTrainUnitCreationFromJson() throws JSONException, ParseException {
        JSONObject trainUnitJson = new JSONObject("{id:80f8447c-241a-4c1f-8d90-99a6eace3a82,start_time_date:'2018-08-01T08:01:18.397983+02:00',end_time_date:'2018-08-01T08:01:18.397983+02:00',date:'2018-08-01',user:1}");
        TrainUnit testUnit = new TrainUnit(trainUnitJson);
        Assert.assertEquals("1533103278397", Long.toString(testUnit.getDate().getTime()));
        Assert.assertEquals("80f8447c-241a-4c1f-8d90-99a6eace3a82", testUnit.getId());
    }
}
