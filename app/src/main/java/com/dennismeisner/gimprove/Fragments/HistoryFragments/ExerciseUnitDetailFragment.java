package com.dennismeisner.gimprove.Fragments.HistoryFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dennismeisner.gimprove.Models.GimproveModels.Set;
import com.dennismeisner.gimprove.Models.GimproveModels.User;
import com.dennismeisner.gimprove.Models.ListContent.ListItemRecyclerViewAdapter;
import com.dennismeisner.gimprove.Activities.LoggedInActivity;
import com.example.dennismeisner.gimprove.R;

import java.util.List;

public class ExerciseUnitDetailFragment extends HistoryFragment {

    private final static String EXERCISE_UNIT="EXERCISE_UNIT";
    private final static String EXERCISE_UNIT_NAME="EXERCISE_UNIT_NAME";

    public static ExerciseUnitDetailFragment newInstance(String exerciseUnitId, String exerciseUnitName) {
        ExerciseUnitDetailFragment fragment = new ExerciseUnitDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXERCISE_UNIT, exerciseUnitId);
        args.putString(EXERCISE_UNIT_NAME, exerciseUnitName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof LoggedInActivity) {
            String exerciseUnitName = getArguments().getString(EXERCISE_UNIT_NAME, "");
            ((LoggedInActivity) getActivity()).setActionBarTitle(exerciseUnitName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainunitdetail, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            String exerciseUnit = getArguments().getString(EXERCISE_UNIT, "");
            List<Set> exercises = User.getInstance().getSetsByExerciseUnits(exerciseUnit);
            recyclerView.setAdapter(new ListItemRecyclerViewAdapter(exercises, mListener));
        }
        return view;
    }

}
