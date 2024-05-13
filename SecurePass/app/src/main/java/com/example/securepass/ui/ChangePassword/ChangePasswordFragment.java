package com.example.securepass.ui.ChangePassword;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;

import java.util.ArrayList;
import java.util.List;

public class ChangePasswordFragment extends Fragment {
    private int id;
    private int userId;
    private DBHelper dbHelper;
    EditText titleEditView;
    EditText emailEditView;
    EditText passwordEditView;
    Spinner folderSpinner;

    private List<String> folderTitles = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);

        // Initialize views
        titleEditView = root.findViewById(R.id.textTitle);
        emailEditView = root.findViewById(R.id.textEmail);
        passwordEditView = root.findViewById(R.id.textPassword);
        folderSpinner = root.findViewById(R.id.folderSpinner);

        // Get user ID
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            userId = mainActivity.getUserId();
        }

        // Fetch the password details from the database
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("passwordId");
            dbHelper = new DBHelper(requireContext());

            Cursor passwordCursor = dbHelper.getPasswordById(id);
            if (passwordCursor != null && passwordCursor.moveToFirst()) {
                // Extract password details
                String title = passwordCursor.getString(passwordCursor.getColumnIndexOrThrow("title"));
                String email = passwordCursor.getString(passwordCursor.getColumnIndexOrThrow("email_username"));
                String password = passwordCursor.getString(passwordCursor.getColumnIndexOrThrow("password"));
                int folderId = passwordCursor.getInt(passwordCursor.getColumnIndexOrThrow("folder_id"));

                // Set the values to the respective views
                titleEditView.setText(title);
                emailEditView.setText(email);
                passwordEditView.setText(password);

                // Fetch and populate folder spinner
                populateFolderSpinner(folderId);

                // Close the cursor
                passwordCursor.close();
            } else {
                Log.e("ChangePasswordFragment", "Password not found or empty cursor");
            }
        }

        TextView saveButton = root.findViewById(R.id.save);

        saveButton.setOnClickListener(v -> {
            savePassword();
            NavController navController = Navigation.findNavController(v);

            navController.popBackStack();
            navController.navigate(R.id.navigation_passwords);

        });
        return root;
    }

    private void savePassword() {
        String title = titleEditView.getText().toString().trim();
        String email = emailEditView.getText().toString().trim();
        String password = passwordEditView.getText().toString().trim();
        int folderId = getSelectedFolderId();

        if (!title.isEmpty() && !password.isEmpty() && folderId != -1 && userId != -1) {
            dbHelper.updatePasswordById(id, title, email, password, folderId);
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateFolderSpinner(int selectedFolderId) {
        // Get folders for the user
        Cursor cursor = dbHelper.getFoldersForUser(userId);

        // Clear existing folder titles
        folderTitles.clear();

        // Iterate through the cursor to extract folder titles and IDs
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String folderTitle = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_FOLDER_TITLE));
                int folderId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_ID));

                folderTitles.add(folderTitle); // Add folder title to the list

                // If the current folder ID matches the selected folder ID, set it as the selected item in the spinner
                if (folderId == selectedFolderId) {
                    folderSpinner.setSelection(cursor.getPosition());
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Create an adapter for the folder spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, folderTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        folderSpinner.setAdapter(adapter);
    }

    private int getSelectedFolderId() {
        int selectedPosition = folderSpinner.getSelectedItemPosition();

        if (selectedPosition != Spinner.INVALID_POSITION) {
            Cursor cursor = dbHelper.getFoldersForUser(userId);

            if (cursor != null && cursor.moveToPosition(selectedPosition)) {
                int folderIdIndex = cursor.getColumnIndexOrThrow(DBHelper.KEY_ID);
                return cursor.getInt(folderIdIndex);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}