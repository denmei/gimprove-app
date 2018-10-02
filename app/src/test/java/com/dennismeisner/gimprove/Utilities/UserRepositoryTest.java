package com.dennismeisner.gimprove.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.dennismeisner.gimprove.Models.GimproveModels.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.concurrent.CountDownLatch;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class UserRepositoryTest {

    private UserRepository userRepository;
    private MockWebServer mockWebServer;
    private HttpUrl baseurl;
    private final CountDownLatch latch = new CountDownLatch(1);

    @Before
    public void setUpMockserver() throws IOException {
        mockWebServer = new MockWebServer();

        mockWebServer.start();
        baseurl = mockWebServer.url("/url/");

        userRepository = new UserRepository("TOKEN",
                RuntimeEnvironment.application.getApplicationContext(), baseurl.toString());
    }

    @Test
    public void testSetUpdate() throws InterruptedException, JSONException, ParseException {
        JSONObject msg = new JSONObject();
        msg.put("id", "2838cbf4-ff49-4a9a-a3c1-e230829856d6");
        msg.put("date_time", "2018-05-28T19:19:57.115408+02:00");
        msg.put("durations", "[17, 1, 19, 9, 11]");
        msg.put("exercise_unit","aec47ebb-6fd2-45d2-a8b2-d0767a455d88");
        msg.put("repetitions", 5);
        msg.put("weight", 5.0);
        msg.put("rfid", "0006921147");
        msg.put("equipment", "test_equipment");
        msg.put("exercise", "Lat Pulldown");
        Set sendSet = new Set(msg);

        userRepository.sendUpdateSet(sendSet);
        RecordedRequest request = mockWebServer.takeRequest();
        String body = request.getBody().readUtf8();
        JSONObject requestJsonBody = new JSONObject(body);
        System.out.println(requestJsonBody.toString());

        Assert.assertEquals(5.0, requestJsonBody.get("weight"));
        Assert.assertEquals(5, requestJsonBody.get("repetitions"));
        Assert.assertEquals("Lat Pulldown", requestJsonBody.get("exercise"));
        Assert.assertEquals("test_equipment", requestJsonBody.get("equipment"));
        Assert.assertEquals("[17.0, 1.0, 19.0, 9.0, 11.0]", requestJsonBody.get("durations"));

        Assert.assertEquals("Token TOKEN", request.getHeader("Authorization"));

        Assert.assertEquals("http://localhost:59763/url/set_detail_rest/2838cbf4-ff49-4a9a-a3c1-e230829856d6",
                request.getRequestUrl().toString());
    }

    @Test
    public void testUserDataUpdate() throws InterruptedException {
        SharedPreferences sharedPreferencesTest = RuntimeEnvironment.application.getSharedPreferences("you_custom_pref_name", Context.MODE_PRIVATE);
        mockWebServer.enqueue(new MockResponse().setBody("{rfid_tag: 'test_rfid'}")
                .setResponseCode(HttpURLConnection.HTTP_OK));

        userRepository.updateUserData("TestUser", 0);

        // correct url used
        RecordedRequest request = mockWebServer.takeRequest();
        Assert.assertTrue(request.getRequestUrl().toString().contains("/url/userprofile_detail_rest/"));

        // TODO
        // update rfid in shared preferences
        // Assert.assertEquals("test_rfid", sharedPreferencesTest.getString("rfid_tag", "def"));

        // updated user data
    }
}
