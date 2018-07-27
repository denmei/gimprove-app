package com.example.dennismeisner.gimprove.HistoryFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dennismeisner.gimprove.ListContent.ListItemRecyclerViewAdapter;
import com.example.dennismeisner.gimprove.Activities.LoggedInActivity;
import com.example.dennismeisner.gimprove.R;

public class TrainUnitOverviewFragment extends HistoryFragment {

    public static TrainUnitOverviewFragment newInstance() {
        TrainUnitOverviewFragment fragment = new TrainUnitOverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof LoggedInActivity) {
            ((LoggedInActivity) getActivity()).setActionBarTitle(getResources()
                    .getString(R.string.actionbar_history_trainunits));
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

}
