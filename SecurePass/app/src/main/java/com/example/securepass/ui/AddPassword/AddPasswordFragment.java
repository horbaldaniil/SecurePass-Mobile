package com.example.securepass.ui.AddPassword;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;

public class AddPasswordFragment extends Fragment {

    private EditText textTitle;
    private EditText textEmail;
    private EditText textPassword;
    private Spinner folderSpinner;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_password, container, false);

        textTitle = root.findViewById(R.id.textTitle);
        textEmail = root.findViewById(R.id.textEmail);
        textPassword = root.findViewById(R.id.textPassword);
        folderSpinner = root.findViewById(R.id.folderSpinner);

        dbHelper = new DBHelper(getContext());

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
        int folderId = getSelectedFolderId(); // Implement this method to get the selected folder ID from the spinner
        int userId = -1;

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            userId = mainActivity.getUserId();
        }

        if (!title.isEmpty() && !password.isEmpty() && folderId != -1 && userId != -1) {
            // Add the password to the database
            dbHelper.addPassword(title, email, password, folderId, userId);

            // Clear input fields after saving
            textTitle.setText("");
            textEmail.setText("");
            textPassword.setText("");

            // Optionally, navigate back to the previous fragment or perform other actions
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    // Implement this method to get the selected folder ID from the spinner
    private int getSelectedFolderId() {
        return 0;
    }
}
