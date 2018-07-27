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
import com.example.dennismeisner.gimprove.GimproveModels.User;
import com.example.dennismeisner.gimprove.ListContent.ListItemRecyclerViewAdapter;
import com.example.dennismeisner.gimprove.Activities.LoggedInActivity;
import com.example.dennismeisner.gimprove.R;

import java.util.List;

public class TrainUnitDetailFragment extends HistoryFragment {

    private final static String TRAIN_UNIT="TRAIN_UNIT";

    public static TrainUnitDetailFragment newInstance(String trainUnitId) {
        TrainUnitDetailFragment fragment = new TrainUnitDetailFragment();
        Bundle args = new Bundle();
        args.putString(TRAIN_UNIT, trainUnitId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof LoggedInActivity) {
            ((LoggedInActivity) getActivity()).setActionBarTitle(getResources()
                    .getString(R.string.actionbar_history_exerciseunits));
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
            String trainUnit = getArguments().getString(TRAIN_UNIT, "");
            List<ExerciseUnit> exercises = User.getInstance().getExerciseUnitsByTrainUnit(trainUnit);
            recyclerView.setAdapter(new ListItemRecyclerViewAdapter(exercises, mListener));
        }
        return view;
    }

}
