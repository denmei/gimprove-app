package com.example.dennismeisner.gimprove.HistoryFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dennismeisner.gimprove.Activities.LoggedInActivity;
import com.example.dennismeisner.gimprove.GimproveModels.Set;
import com.example.dennismeisner.gimprove.GimproveModels.User;
import com.example.dennismeisner.gimprove.R;


public class SetDetailFragment extends Fragment {

    private static final String SETID = "SETID";
    private Set set;
    private String setId;

    private TextView repsText;
    private TextView repsVal;

    private OnSetDetailListener mListener;

    public SetDetailFragment() {
        // Required empty public constructor
    }

    public static SetDetailFragment newInstance(String setId) {
        SetDetailFragment fragment = new SetDetailFragment();
        Bundle args = new Bundle();
        args.putString(SETID, setId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setId = getArguments().getString(SETID);
            set = User.getInstance().getSetById(setId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof LoggedInActivity) {
            ((LoggedInActivity) getActivity()).setActionBarTitle(getResources()
                    .getString(R.string.actionbar_history_setdetails));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        repsText = view.findViewById(R.id.repsText);
        repsText.setText("Repetitions");
        repsVal = view.findViewById(R.id.repsVal);
        repsVal.setText(Integer.toString(set.getRepetitions()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetDetailListener) {
            mListener = (OnSetDetailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnSetDetailListener {
        // TODO: Update argument type and name
        void onSetDetail(Uri uri);
    }
}
