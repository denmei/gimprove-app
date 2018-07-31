package com.example.dennismeisner.gimprove.HistoryFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.dennismeisner.gimprove.GimproveModels.TrainUnit;
import com.example.dennismeisner.gimprove.GimproveModels.User;
import com.example.dennismeisner.gimprove.ListContent.ListItemRecyclerViewAdapter;
import com.example.dennismeisner.gimprove.Activities.LoggedInActivity;
import com.example.dennismeisner.gimprove.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TrainUnitOverviewFragment extends HistoryFragment {

    private CalendarView calendarView;
    private List<EventDay> trainUnitDays;
    static private SimpleDateFormat dateFormat;
    private Button selectedDateButton;
    private OnTrainUnitDaySelectedListener trainUnitListener;

    public static TrainUnitOverviewFragment newInstance() {
        TrainUnitOverviewFragment fragment = new TrainUnitOverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof LoggedInActivity) {
            ((LoggedInActivity) getActivity()).setActionBarTitle(getResources()
                    .getString(R.string.actionbar_history_trainunits));
        }
        List<TrainUnit> trainUnits = User.getInstance().getTrainUnits();
        trainUnitDays = new ArrayList<>();
        for(TrainUnit trainUnit: trainUnits) {
            Calendar c = Calendar.getInstance();
            c.setTime(trainUnit.getDate());
            System.out.println(trainUnit.getDate());
            EventDay e = new EventDay(c, R.drawable.g5707_small);
            System.out.println(e.toString());
            trainUnitDays.add(new EventDay(c, R.drawable.g5707_small));
        }
        calendarView.setEvents(trainUnitDays);
        Calendar cal = Calendar.getInstance();
        try {
            calendarView.setDate(cal);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ListItemRecyclerViewAdapter(this.user.getTrainUnits(), mListener));
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        calendarView = view.findViewById(R.id.trainUnitCalendar);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");

            @Override
            public void onDayClick(EventDay eventDay) {
                // Check whether picked date has a train unit
                TrainUnit selectedUnit = null;
                String selectedDate = dateF.format(eventDay.getCalendar().getTime());
                for(TrainUnit trainUnit:User.getInstance().getTrainUnits()) {
                    if (dateF.format(trainUnit.getDate()).equals(selectedDate)) {
                        selectedUnit = trainUnit;
                        break;
                    }
                }
                // if trainunit available, open detailview
                if (selectedUnit != null) {
                    System.out.println("valid selection");
                    System.out.println(selectedUnit.getId());
                    trainUnitListener.onTrainUnitDaySelected(selectedUnit.getId());
                } else {
                    System.out.println("not valid selection");
                }
            }
        });

        System.out.println("CreateD");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTrainUnitDaySelectedListener) {
            trainUnitListener = (OnTrainUnitDaySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public interface OnTrainUnitDaySelectedListener {
        void onTrainUnitDaySelected(String trainUnitId);
    }

}
