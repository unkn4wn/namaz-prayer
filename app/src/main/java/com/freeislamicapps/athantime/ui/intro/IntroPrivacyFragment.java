package com.freeislamicapps.athantime.ui.intro;

import android.os.Bundle;

import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

        //Autosize below Api 26
        TextView titleLocation = view.findViewById(R.id.title_privacy);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(titleLocation,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        Button button = view.findViewById(R.id.getStartedButton);

        button.setOnClickListener(view1 -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new IntroLocationFragment());
            transaction.commit();
        });

        // Inflate the layout for this fragment
        return view;
    }
}