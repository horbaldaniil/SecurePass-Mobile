package com.example.securepass.ui.singlePassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.securepass.R;

public class SinglePasswordFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single_password, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("title");
            String email = bundle.getString("email");

            TextView titleTextView = root.findViewById(R.id.single_password_title);
            TextView emailTextView = root.findViewById(R.id.single_password_email_text);

            titleTextView.setText(title);
            emailTextView.setText(email);
        }

        return root;
    }
}