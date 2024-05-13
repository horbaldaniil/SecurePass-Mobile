package com.example.securepass.ui.passwords;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;
import com.example.securepass.databinding.FragmentPasswordsBinding;

public class PasswordsFragment extends Fragment {

    private FragmentPasswordsBinding binding;
    private ListView listView;
    private DBHelper dbHelper;
    private int userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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
                PasswordAdapter adapter = new PasswordAdapter(requireContext(), cursor, R.layout.password_item);
                listView.setAdapter(adapter);
            } else {
                Log.e("PasswordsFragment", "Cursor is null");
            }
        } else {
            Log.e("PasswordsFragment", "Invalid user ID");
        }

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            int passwordId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_ID));

            NavController navController = Navigation.findNavController(view);

            Bundle bundle = new Bundle();
            bundle.putInt("passwordId", passwordId);

            navController.popBackStack(R.id.navigation_passwords, true);
            navController.navigate(R.id.navigation_single_password, bundle);
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