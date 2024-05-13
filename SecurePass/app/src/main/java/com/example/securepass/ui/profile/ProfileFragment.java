package com.example.securepass.ui.profile;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;
import com.example.securepass.databinding.FragmentPasswordsBinding;
import com.example.securepass.databinding.FragmentProfileBinding;
import com.example.securepass.ui.login.LoginActivity;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    DBHelper dbHelper;

    private int userId;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}