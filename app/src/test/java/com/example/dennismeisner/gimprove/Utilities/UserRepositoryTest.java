package com.example.dennismeisner.gimprove.Utilities;

import android.content.Context;

import com.example.dennismeisner.gimprove.GimproveModels.Set;
import com.example.dennismeisner.gimprove.SystemCheck;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
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
    public void testSetUpdate() throws InterruptedException, JSONException {
        UserRepository userRepository = new UserRepository("TOKEN",
                RuntimeEnvironment.application.getApplicationContext(), baseurl.toString());
        Set sendSet = new Set(1, 1, new Date());
        userRepository.sendUpdateSet(sendSet);
        RecordedRequest request = mockWebServer.takeRequest();
        // System.out.println(request.getBody().toString());
        JSONObject jsonRequestBody = new JSONObject(request.getBody().readUtf8());
        System.out.println(jsonRequestBody.toString());
        assertEquals(jsonRequestBody.get("weight"), 1.0);
        assertEquals(jsonRequestBody.get("repetitions"), 1);

        System.out.println(request.getHeaders());
        String header = request.getHeader("Authorization");
        System.out.println(header);
    }

    @Test
    public void testUserDataUpdate() {

    }
}
