package com.example.dennismeisner.gimprove.Utilities;

import com.example.dennismeisner.gimprove.GimproveModels.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.Date;

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

    @Before
    public void setUpMockserver() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody("your json body")
                .setResponseCode(HttpURLConnection.HTTP_OK));

        mockWebServer.start();
        baseurl = mockWebServer.url("/url/");
    }

    @Test
    public void testSetUpdate() throws InterruptedException, JSONException, ParseException {
        UserRepository userRepository = new UserRepository("TOKEN",
                RuntimeEnvironment.application.getApplicationContext(), baseurl.toString());
        double[] durs = new double[1];
        durs[0] = 0.1;
        JSONObject msg = new JSONObject();
        msg.put("id", "2838cbf4-ff49-4a9a-a3c1-e230829856d6");
        msg.put("date_time", "2018-05-28T19:19:57.115408+02:00");
        msg.put("durations", "[17, 1, 19, 9, 11]");
        msg.put("exercise_unit","aec47ebb-6fd2-45d2-a8b2-d0767a455d88");
        msg.put("repetitions", 5);
        msg.put("weight", 5);
        msg.put("rfid", "0006921147");
        msg.put("equipment_id", null);
        msg.put("exercise_name", "Lat Pulldown");

        Set sendSet = new Set(msg);
        userRepository.sendUpdateSet(sendSet);
        RecordedRequest request = mockWebServer.takeRequest();
        System.out.println(request.getBody().toString());


        System.out.println(request.getHeaders());
        String header = request.getHeader("Authorization");
        System.out.println(header);
    }

    @Test
    public void testUserDataUpdate() {

    }
}
