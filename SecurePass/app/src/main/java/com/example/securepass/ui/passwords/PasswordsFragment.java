package com.example.securepass.ui.passwords;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;
import com.example.securepass.databinding.FragmentPasswordsBinding;
import com.example.securepass.ui.singlePassword.SinglePasswordFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordsFragment extends Fragment {
    private FragmentPasswordsBinding binding;
    private ListView listView;
    private DBHelper dbHelper;
    private int userId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.passwords_list);
        dbHelper = new DBHelper(requireContext());

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            userId = mainActivity.getUserId();
        }

        if (userId != -1) {
            Cursor cursor = dbHelper.getPasswordsForUser(userId);
            if (cursor != null) {
                List<String[]> passwordsList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TITLE));
                    @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EMAIL_USERNAME));
                    passwordsList.add(new String[]{title, email});
                }
                cursor.close();

                PasswordAdapter adapter = new PasswordAdapter(requireContext(), R.layout.password_item, passwordsList);
                listView.setAdapter(adapter);
            } else {
                Log.e("PasswordsFragment", "Cursor is null");
            }
        } else {
            Log.e("PasswordsFragment", "Invalid user ID");
        }

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get password ID from cursor
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            @SuppressLint("Range") int passwordId = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID));

            // Fetch password data from the database
            Cursor passwordCursor = dbHelper.getPasswordById(passwordId);
            if (passwordCursor != null && passwordCursor.moveToFirst()) {
                // Retrieve password details
                @SuppressLint("Range") String title = passwordCursor.getString(passwordCursor.getColumnIndex(DBHelper.KEY_TITLE));
                @SuppressLint("Range") String email = passwordCursor.getString(passwordCursor.getColumnIndex(DBHelper.KEY_EMAIL_USERNAME));

                // Navigate to SinglePasswordFragment with password details
                NavController navController = Navigation.findNavController(view);

                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("email", email);
                // Add password ID to bundle
                bundle.putInt("passwordId", passwordId);

                navController.popBackStack(R.id.navigation_passwords, true);
                navController.navigate(R.id.navigation_single_password, bundle);
            } else {
                Log.e("PasswordsFragment", "Password cursor is null or empty");
            }
        });

        ImageButton addButton = root.findViewById(R.id.add_password_button);
        addButton.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);

            navController.popBackStack();
            navController.navigate(R.id.navigation_add_password);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}