package com.example.dennismeisner.gimprove.LoggedInFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dennismeisner.gimprove.Activities.LoggedInActivity;
import com.example.dennismeisner.gimprove.GimproveModels.Set;
import com.example.dennismeisner.gimprove.R;
import com.example.dennismeisner.gimprove.Utilities.UserRepository;


/**
 * Fragmennt to review the results of one set.
 * Provides possibility to change the key figures: repetitions and weight. Changes will be
 * communicated to the server.
 */
public class ReviewFragment extends Fragment {

    private Set newSet;   // Reference to the new set
    private String exerciseName;    // Name of the exercise the set belongs to
    private Integer newRepetitions;    // Repetitions the user will set eventually
    private Double newWeight;    // Weight the user will set eventually
    private Integer originalRepetitions;    // Repetitions sent from the server
    private Double originalWeight;    // Weight sent from the server

    private Button continueButton;    // Button to close the fragment
    private Button incrRepsButton;
    private Button decrRepsButton;

    private TextView weightText;
    private TextView repsText;
    private TextView exerciseNameText;

    private OnReviewDoneListener mListener;
    private UserRepository userRepository;

    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ReviewFragment.
     */
    public static ReviewFragment newInstance(Integer checkRepetitions, double checkWeight,
                                             String exerciseName) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putInt("checkReps", checkRepetitions);
        args.putDouble("checkWeight", checkWeight);
        args.putString("exerciseName", exerciseName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = mListener.getUserRepo();
        if (getArguments() != null) {
            exerciseName = getArguments().getString("exerciseName");
            newSet = (Set) getArguments().getSerializable("newSet");
            originalWeight = newSet.getWeight();
            originalRepetitions = newSet.getRepetitions();
            newRepetitions = newSet.getRepetitions();
            newWeight = newSet.getWeight();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userRepository = mListener.getUserRepo();
        if(getActivity() instanceof LoggedInActivity && getArguments() != null) {
            exerciseName = getArguments().getString("exerciseName");
            newSet = (Set) getArguments().getSerializable("newSet");
            originalWeight = newSet.getWeight();
            originalRepetitions = newSet.getRepetitions();
            newRepetitions = newSet.getRepetitions();
            newWeight = newSet.getWeight();
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

        // Initialize textfield for name of exercise
        exerciseNameText = (TextView) view.findViewById(R.id.exerciseName);
        exerciseNameText.setText(exerciseName);

        // Initialize textfield for repetition counter
        repsText = (TextView) view.findViewById(R.id.textReps);
        repsText.setText(String.valueOf(originalRepetitions));

        // Initialize textfield for weight
        weightText = (TextView) view.findViewById(R.id.textWeight);
        weightText.setText(String.valueOf(originalWeight));

        // Initialize continue-Button
        continueButton = (Button) view.findViewById(R.id.continueButton);
        this.continueButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    // If the user corrected one of the key figures, send an update to the server.
                    public void onClick(View v) {
                        sendChanges();
                        mListener.onReviewDone();
                    }
                }
        );

        // Initialize increment button for reps
        incrRepsButton = (Button) view.findViewById(R.id.buttonIncRep);
        this.incrRepsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newRepetitions < 98) {
                            newRepetitions = newRepetitions + 1;
                            repsText.setText(String.valueOf(newRepetitions));
                        }
                        adapatUIToChanges();
                    }
                }
        );

        // Initialize decrement button for reps
        decrRepsButton = (Button) view.findViewById(R.id.buttonDecRep);
        this.decrRepsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newRepetitions > 0) {
                            newRepetitions = newRepetitions - 1;
                            repsText.setText(String.valueOf(newRepetitions));
                        }
                        adapatUIToChanges();
                    }
                }
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Check whether activity implements the required interface
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

    /**
     * Changes text of continuebutton and textcolors when the weights or repetitions are changed.
     */
    private void adapatUIToChanges() {

        // set color for repetitions
        if (repetitionsChanged()) {
            repsText.setTextColor(getResources().getColor(R.color.gimproveWhite));
        } else {
            repsText.setTextColor(getResources().getColor(R.color.grimproveOrange));
        }

        // set color for weight
        if (weightChanged()) {
            weightText.setTextColor(getResources().getColor(R.color.gimproveWhite));
        } else {
            weightText.setTextColor(getResources().getColor(R.color.grimproveOrange));
        }

        // adapt continue button
        if(weightChanged() || repetitionsChanged()) {
            continueButton.setText(getResources().getString(R.string.continueButton_with_changes));
        } else {
            continueButton.setText(getResources().getString(R.string.continueButton_no_changes));
        }
    }

    /**
     * Checks whether the number of repetitions was changed.
     * @return
     */
    private boolean repetitionsChanged() {
        return !originalRepetitions.equals(newRepetitions);
    }

    /**
     * Check whether the weight has been changed.
     * @return
     */
    private boolean weightChanged() {
        return !originalWeight.equals(newWeight);
    }

    /**
     * Sends set-update to client.
     */
    private void sendChanges() {
        if(weightChanged() || repetitionsChanged()) {
            double[] newDurations = new double[newRepetitions];
            for(int i=0; i < newDurations.length; i++) {
                newDurations[i] = 0.0;
            }
            newSet.setRepetitions(newRepetitions);
            newSet.setWeight(newWeight);
            System.out.println("New set: " + newSet.toString());
            userRepository.sendUpdateSet(newSet);
        }
    }

    /**
     * Must be implemented by activity.
     */
    public interface OnReviewDoneListener {
        public void onReviewDone();
        public UserRepository getUserRepo();
    }
}
