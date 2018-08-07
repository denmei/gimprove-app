package com.dennismeisner.gimprove.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;

import com.dennismeisner.gimprove.Fragments.HistoryFragments.ExerciseUnitDetailFragment;
import com.dennismeisner.gimprove.Fragments.HistoryFragments.HistoryFragment;
import com.dennismeisner.gimprove.Fragments.HistoryFragments.SetDetailFragment;
import com.dennismeisner.gimprove.Fragments.HistoryFragments.TrainUnitDetailFragment;
import com.dennismeisner.gimprove.Fragments.HistoryFragments.TrainUnitOverviewFragment;
import com.dennismeisner.gimprove.Models.ListContent.ListItem;
import com.dennismeisner.gimprove.Fragments.LoggedInFragments.LiveFeedbackFragment;
import com.dennismeisner.gimprove.Fragments.LoggedInFragments.ReviewFragment;
import com.dennismeisner.gimprove.Utilities.RequestManager;
import com.dennismeisner.gimprove.Utilities.TokenManager;
import com.dennismeisner.gimprove.Utilities.UserRepository;
import com.dennismeisner.gimprove.Models.GimproveModels.ExerciseUnit;
import com.dennismeisner.gimprove.Models.GimproveModels.Set;
import com.dennismeisner.gimprove.Models.GimproveModels.TrainUnit;
import com.dennismeisner.gimprove.Models.GimproveModels.User;
import com.example.dennismeisner.gimprove.R;

import java.text.SimpleDateFormat;

public class LoggedInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HistoryFragment.OnListFragmentInteractionListener, LiveFeedbackFragment.OnSetFinishedListener,
        ReviewFragment.OnReviewDoneListener, SetDetailFragment.OnSetDetailListener,
        TrainUnitOverviewFragment.OnTrainUnitDaySelectedListener {

    private LiveFeedbackFragment liveFeedbackFragment;
    private TrainUnitOverviewFragment trainUnitOverviewFragment;
    private TrainUnitDetailFragment exerciseUnitHistoryFragment;
    private ExerciseUnitDetailFragment setHistoryFragment;
    private SetDetailFragment setDetailFragment;
    private ReviewFragment reviewFragment;
    private User user;
    private SharedPreferences sharedPreferences;
    private TokenManager tokenManager;
    private RequestManager requestManager;
    private UserRepository userRepository;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("LOGGEDIN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences(
                "com.example.dennismeisner.gimprove.app", Context.MODE_PRIVATE);
        tokenManager = new TokenManager(sharedPreferences);
        requestManager = new RequestManager(this, tokenManager);

        user = User.getInstance();

        userRepository = new UserRepository(tokenManager.getToken(), this, sharedPreferences,
                getResources().getString(R.string.baseUrl));
        try {
            userRepository.updateUser(
                    sharedPreferences.getString("UserName", "UserName"),
                    sharedPreferences.getInt("userid", 0)
            );
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
        trainUnitOverviewFragment = new TrainUnitOverviewFragment();
        exerciseUnitHistoryFragment = new TrainUnitDetailFragment();
        setHistoryFragment = new ExerciseUnitDetailFragment();
        setDetailFragment = new SetDetailFragment();
        reviewFragment = new ReviewFragment();

        // load start fragment
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, liveFeedbackFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        } else {
            super.onBackPressed();
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

        /* noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.tracking) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_placeholder, liveFeedbackFragment, "LiveFeedbackFragment");
            ft.commit();
        } else if (id == R.id.history) {
            // TODO: Remove this update. Update only if user requests.
            try {
                userRepository.updateTrainUnits();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_placeholder, trainUnitOverviewFragment, "TrainUnitFragment");
            ft.addToBackStack("LiveFeedbackFragment");
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

    public void setActionBarTitle(String title) {
        this.toolbar.setTitle(title);
    }

    public void onSetFinished(String exerciseName, Set newSet) {
        Bundle bundle = new Bundle();
        bundle.putString("exeriseName", exerciseName);
        bundle.putSerializable("newSet", newSet);
        reviewFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, reviewFragment);
        ft.addToBackStack("LiveFeedbackFragment");
        ft.commit();
    }

    public void onReviewDone() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, liveFeedbackFragment);
        ft.commit();
    }

    @Override
    public UserRepository getUserRepo() {
        return userRepository;
    }

    @Override
    public void onListFragmentInteraction(ListItem listItem) {
        Bundle bundle = new Bundle();
        if (listItem instanceof ExerciseUnit) {
            System.out.println("Exerciseunit: " + listItem.toString());
            try {
                userRepository.updateSets();
            } catch (Exception e) {
                e.printStackTrace();
            }
            bundle.putString("EXERCISE_UNIT", listItem.getId());
            bundle.putString("EXERCISE_UNIT_NAME", listItem.toString());
            setHistoryFragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_placeholder, setHistoryFragment);
            ft.addToBackStack("ExerciseUnitFragment");
            ft.commit();
        } else if (listItem instanceof Set) {
            bundle.putString("SETID", listItem.getId());
            setDetailFragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_placeholder, setDetailFragment);
            ft.addToBackStack("SetDetailFragment");
            ft.commit();
        }
    }

    @Override
    public void onSetDetail(Uri uri) {

    }

    @Override
    public void onTrainUnitDaySelected(String trainUnitId) {
        Bundle bundle = new Bundle();
        try {
            userRepository.updateExerciseUnits();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TrainUnit detailUnit = User.getInstance().getTrainUnitById(trainUnitId);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        if(detailUnit != null) {
            bundle.putString("TRAIN_UNIT_DATE", dateFormatter.format(detailUnit.getDate()));
        } else {
            bundle.putString("TRAIN_UNIT_DATE", "");
        }
        bundle.putString("TRAIN_UNIT", trainUnitId);
        exerciseUnitHistoryFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, exerciseUnitHistoryFragment);
        ft.addToBackStack("TrainUnitFragment");
        ft.commit();
    }
}
