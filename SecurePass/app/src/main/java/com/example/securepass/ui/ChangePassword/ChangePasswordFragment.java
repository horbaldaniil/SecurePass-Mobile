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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;

public class ChangePasswordFragment extends Fragment {

    private int id;

    private int userId;
    private DBHelper dbHelper;
    EditText titleEditView;

    EditText emailEditView;

    EditText passwordEditView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);
        Bundle bundle = getArguments();


        if (bundle != null) {
            id = bundle.getInt("passwordId");
            dbHelper = new DBHelper(requireContext());

            Cursor password = dbHelper.getPasswordById(id);
            password.moveToFirst();

            if (password != null && password.moveToFirst()) {

                titleEditView = root.findViewById(R.id.textTitle);
                emailEditView = root.findViewById(R.id.textEmail);
                passwordEditView = root.findViewById(R.id.textPassword);

                titleEditView.setText(password.getString(password.getColumnIndexOrThrow("title")));
                emailEditView.setText(password.getString(password.getColumnIndexOrThrow("email_username")));
                passwordEditView.setText(password.getString(password.getColumnIndexOrThrow("password")));

                password.close();
            } else {
                Log.e("SinglePasswordFragment", "Password not found or empty cursor");
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

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            userId = mainActivity.getUserId();
        }

        if (!title.isEmpty() && !password.isEmpty() && folderId != -1 && userId != -1) {
            dbHelper.updatePasswordById(id, title, email, password, folderId);
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private int getSelectedFolderId() {
        return 0;
    }
}