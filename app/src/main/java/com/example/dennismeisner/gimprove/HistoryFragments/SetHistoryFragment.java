package com.example.dennismeisner.gimprove.HistoryFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dennismeisner.gimprove.GimproveModels.ExerciseUnit;
import com.example.dennismeisner.gimprove.GimproveModels.Set;
import com.example.dennismeisner.gimprove.GimproveModels.User;
import com.example.dennismeisner.gimprove.ListContent.ListItemRecyclerViewAdapter;
import com.example.dennismeisner.gimprove.R;

import java.util.List;

public class SetHistoryFragment extends HistoryFragment {

    private final static String EXERCISE_UNIT="EXERCISE_UNIT";

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SetHistoryFragment newInstance(int columnCount, String trainUnit) {
        SetHistoryFragment fragment = new SetHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            String exerciseUnit = getArguments().getString(EXERCISE_UNIT, "");
            List<Set> exercises = User.getInstance().getSetsByExerciseUnits(exerciseUnit);
            recyclerView.setAdapter(new ListItemRecyclerViewAdapter(exercises, mListener));
        }
        return view;
    }

}
