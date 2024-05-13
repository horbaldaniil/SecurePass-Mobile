package com.example.securepass.ui.singlePassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.database.Cursor;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;

public class SinglePasswordFragment extends Fragment {

    private DBHelper dbHelper;

    private int id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single_password, container, false);
        Button delete = root.findViewById(R.id.delete_button);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("passwordId");
            dbHelper = new DBHelper(requireContext());

            Cursor password = dbHelper.getPasswordById(id);
            password.moveToFirst();
            if ((password.getInt(password.getColumnIndexOrThrow("deleted")) == 1))
            {
                delete.setText("Delete password");
            }
            else {
                delete.setText("Move to trash");
            }

            if (password != null && password.moveToFirst()) {
                TextView titleTextView = root.findViewById(R.id.add_password_title);
                TextView emailTextView = root.findViewById(R.id.single_password_email_text);

                titleTextView.setText(password.getString(password.getColumnIndexOrThrow("title")));
                emailTextView.setText(password.getString(password.getColumnIndexOrThrow("email_username")));

                password.close();
            } else {
                Log.e("SinglePasswordFragment", "Password not found or empty cursor");
            }
        }

        ImageButton backButton = root.findViewById(R.id.icon_back);
        backButton.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);

            navController.popBackStack();
            navController.navigate(R.id.navigation_passwords);
        });

        TextView edit = root.findViewById(R.id.edit);
        edit.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);

            navController.popBackStack();
            navController.navigate(R.id.navigation_change_password);
        });



        delete.setOnClickListener(v -> {
            dbHelper = new DBHelper(requireContext());

            Cursor password = dbHelper.getPasswordById(id);
            password.moveToFirst();
            if ((password.getInt(password.getColumnIndexOrThrow("deleted")) == 1))
            {
                dbHelper.deletePasswordById(id);
                NavController navController = Navigation.findNavController(v);
                navController.popBackStack();
                navController.navigate(R.id.navigation_trash);
            }
            else {
                dbHelper.softDeletePasswordById(id);
                NavController navController = Navigation.findNavController(v);
                navController.popBackStack();
                navController.navigate(R.id.navigation_passwords);
            }
        });
        return root;
    }
}