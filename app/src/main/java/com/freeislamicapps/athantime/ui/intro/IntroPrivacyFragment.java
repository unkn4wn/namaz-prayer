package com.freeislamicapps.athantime.ui.intro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.freeislamicapps.athantime.R;

/**
 * Intro Fragment
 * The class IntroPrivacyFragment will be shown during first start of the app
 * shows the user information about the privacy policy
 */
public class IntroPrivacyFragment extends Fragment {
    public IntroPrivacyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.intro_fragment_privacy, container, false);
        Button button = view.findViewById(R.id.getStartedButton);

        button.setOnClickListener(view1 -> {
            Fragment newFragment = new IntroLocationFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new IntroLocationFragment());
            transaction.commit();
        });

        // Inflate the layout for this fragment
        return view;
    }
}