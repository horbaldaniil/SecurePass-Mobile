package com.example.securepass.ui.AddPassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.securepass.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPasswordFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_password, container, false);
    }
}