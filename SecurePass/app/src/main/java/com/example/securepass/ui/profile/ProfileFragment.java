package com.example.securepass.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepass.R;
import com.example.securepass.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner languageSpinner = root.findViewById(R.id.language_spinner);
        Spinner themeSpinner = root.findViewById(R.id.theme_spinner);

        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(
                root.getContext(),
                R.array.language,
                android.R.layout.simple_spinner_item
        );

        ArrayAdapter<CharSequence> themeAdapter = ArrayAdapter.createFromResource(
                root.getContext(),
                R.array.themes,
                android.R.layout.simple_spinner_item
        );

        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languageSpinner.setAdapter(languageAdapter);
        themeSpinner.setAdapter(themeAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}