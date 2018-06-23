package com.example.dennismeisner.gimprove;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.dennismeisner.gimprove.GimproveModels.User;
import com.example.dennismeisner.gimprove.ListContent.ListItem;
import com.example.dennismeisner.gimprove.Utilities.RequestManager;
import com.example.dennismeisner.gimprove.Utilities.TokenManager;
import com.example.dennismeisner.gimprove.Utilities.UserRepository;

import org.json.JSONException;

import java.io.IOException;

public class LoggedInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HistoryFragment.OnListFragmentInteractionListener {

    private LiveFeedbackFragment liveFeedbackFragment;
    private HistoryFragment historyFragment;
    private User user;
    private SharedPreferences sharedPreferences;
    private TokenManager tokenManager;
    private RequestManager requestManager;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences(
                "com.example.dennismeisner.gimprove.app", Context.MODE_PRIVATE);
        tokenManager = new TokenManager(sharedPreferences);
        requestManager = new RequestManager(this, tokenManager);

        user = User.getInstance();
        user.setUserAttributes(sharedPreferences.getString("UserName", "UserName"),
                sharedPreferences.getInt("userid", 0));

        userRepository = new UserRepository(requestManager, tokenManager.getToken(), this);
        try {
            userRepository.updateUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView userNameField = (TextView) header.findViewById(R.id.nav_username);
        userNameField.setText("Hi " + sharedPreferences.getString("UserName", "").toString() + "!");

        liveFeedbackFragment = new LiveFeedbackFragment();
        historyFragment = new HistoryFragment();

        // load start fragment
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, liveFeedbackFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logged_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.tracking) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_placeholder, liveFeedbackFragment);
            ft.commit();
        } else if (id == R.id.history) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_placeholder, historyFragment);
            ft.commit();
        } else if (id == R.id.logout) {
            sharedPreferences.edit().putString("Token", "").apply();
            sharedPreferences.edit().putString("UserName", "").apply();
            Intent systemcheckIntent = new Intent(this, SystemCheck.class);
            startActivity(systemcheckIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(ListItem listItem) {
        System.out.println("CLICK: " + listItem.toString());
    }
}
