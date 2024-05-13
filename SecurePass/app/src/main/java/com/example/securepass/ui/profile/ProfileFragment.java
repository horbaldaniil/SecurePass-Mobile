package com.example.securepass.ui.profile;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;
import com.example.securepass.databinding.FragmentProfileBinding;
import com.example.securepass.ui.login.LoginActivity;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    DBHelper dbHelper;

    private int userId;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner languageSpinner = view.findViewById(R.id.language_spinner);

        languageSpinner.post(() -> {
            languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedLanguage = parent.getItemAtPosition(position).toString();
                    changeLanguage(selectedLanguage);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        });
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            userId = mainActivity.getUserId();
        }

        TextView emailTextView = root.findViewById(R.id.emailText);
        Button logoutButton = root.findViewById(R.id.logoutButton);
        dbHelper = new DBHelper(requireContext());
        emailTextView.setText(dbHelper.getUserEmailById(userId));


        Spinner languageSpinner = root.findViewById(R.id.language_spinner);

        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(
                root.getContext(),
                R.array.language,
                android.R.layout.simple_spinner_item
        );


        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languageSpinner.setAdapter(languageAdapter);

        logoutButton.setOnClickListener(v -> {
            if (getActivity() != null && getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.getIntent().removeExtra("USER_ID");
                Intent intent = new Intent(root.getContext(), LoginActivity.class);
                startActivity(intent);
                mainActivity.finish();
            }
        });

        return root;
    }

    private void changeLanguage(String language) {
        Locale locale;
        if (language.equals("Українська")) {
            locale = new Locale("uk");
        } else {
            locale = Locale.ENGLISH;
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.recreate();
        }

    }

}