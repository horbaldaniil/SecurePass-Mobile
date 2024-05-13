package com.example.securepass.ui.AddPassword;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;

import java.util.ArrayList;
import java.util.List;

public class AddPasswordFragment extends Fragment {

    private EditText textTitle;
    private EditText textEmail;
    private EditText textPassword;
    private Spinner folderSpinner;
    private DBHelper dbHelper;
    private List<String> folderTitles = new ArrayList<>();
    private int userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_password, container, false);

        textTitle = root.findViewById(R.id.textTitle);
        textEmail = root.findViewById(R.id.textEmail);
        textPassword = root.findViewById(R.id.textPassword);
        folderSpinner = root.findViewById(R.id.folderSpinner);

        dbHelper = new DBHelper(getContext());

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            userId = mainActivity.getUserId();
        }

        populateFolderSpinner();

        TextView saveButton = root.findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> savePassword());

        ImageButton backButton = root.findViewById(R.id.icon_back);
        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
            navController.navigate(R.id.navigation_passwords);
        });

        return root;
    }

    private void savePassword() {
        String title = textTitle.getText().toString().trim();
        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        int folderId = getSelectedFolderId();

        if (!title.isEmpty() && !password.isEmpty() && folderId != -1 && userId != -1) {
            // Add the password to the database
            dbHelper.addPassword(title, email, password, folderId, userId);

            // Clear input fields after saving
            textTitle.setText("");
            textEmail.setText("");
            textPassword.setText("");

            // Optionally, navigate back to the previous fragment or perform other actions
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack();
            navController.navigate(R.id.navigation_passwords);
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateFolderSpinner() {
        // Get user ID

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            userId = mainActivity.getUserId();
        }

        // Get folders for the user
        Cursor cursor = dbHelper.getFoldersForUser(userId);

        // Clear existing folder titles
        folderTitles.clear();

        // Iterate through the cursor to extract folder titles
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String folderTitle = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_FOLDER_TITLE));
                folderTitles.add(folderTitle); // Add folder title to the list
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Create an adapter for the folder spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, folderTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        folderSpinner.setAdapter(adapter);
    }

    // Implement this method to get the selected folder ID from the spinner
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
