package com.example.dennismeisner.gimprove.Utilities;

import android.content.Context;

import com.example.dennismeisner.gimprove.GimproveModels.Set;
import com.example.dennismeisner.gimprove.SystemCheck;

import org.json.JSONException;
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

@RunWith(RobolectricTestRunner.class)
public class UserRepositoryTest {

    private UserRepository userRepository;

    @Test
    public void test_set_update() throws IOException, JSONException, InterruptedException {
        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody("your json body")
                .setResponseCode(HttpURLConnection.HTTP_OK));
        final MockResponse response = new MockResponse().setResponseCode(401);

        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request)
                    throws InterruptedException {
                return response; // this could have been more sophisticated
            }
        });

        mockWebServer.start();
        HttpUrl baseurl = mockWebServer.url("/url/");

        UserRepository userRepository = new UserRepository("TOKEN",
                RuntimeEnvironment.application.getApplicationContext(), baseurl.toString());
        Set sendSet = new Set(1, 1, new Date());
        userRepository.sendUpdateSet(sendSet);
        System.out.println("X");
        RecordedRequest request = mockWebServer.takeRequest();
        System.out.println("Y");
        System.out.println(request.getBody());
        System.out.println(request.getHeaders());
    }
}
