package com.example.dennismeisner.gimprove;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewFragment.OnReviewDoneListener} interface
 * to handle interaction events.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {

    private String exerciseName;
    private Integer repetitions;
    private Double weight;
    private Button continueButton;

    private Button incrRepsButton;
    private Button decrRepsButton;

    private TextView weightText;
    private TextView repsText;

    private OnReviewDoneListener mListener;

    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ReviewFragment.
     */
    public static ReviewFragment newInstance(String exerciseName, Integer repetitions, Double weight) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString("exerciseName", exerciseName);
        args.putInt("repetitions", repetitions);
        args.putDouble("weight", weight);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseName = getArguments().getString("exerciseName");
            repetitions = getArguments().getInt("repetitions");
            weight = getArguments().getDouble("weight");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof LoggedInActivity) {
            exerciseName = getArguments().getString("exerciseName", "");
            repetitions = getArguments().getInt("repetitions");
            weight = getArguments().getDouble("weight");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        repsText = (TextView) view.findViewById(R.id.textReps);
        repsText.setText(String.valueOf(repetitions));

        // Initialize continue-Button
        continueButton = (Button) view.findViewById(R.id.continueButton);
        this.continueButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onReviewDone();
                    }
                }
        );

        incrRepsButton = (Button) view.findViewById(R.id.buttonIncRep);
        this.incrRepsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer reps = Integer.parseInt(repsText.getText().toString());
                        repsText.setText(String.valueOf(reps + 1));
                    }
                }
        );

        decrRepsButton = (Button) view.findViewById(R.id.buttonDecRep);
        this.decrRepsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer reps = Integer.parseInt(repsText.getText().toString());
                        if (reps > 0) {
                            repsText.setText(String.valueOf(reps - 1));
                        }
                    }
                }
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReviewDoneListener) {
            mListener = (OnReviewDoneListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnReviewDoneListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnReviewDoneListener {
        public void onReviewDone();
    }
}
