package com.example.securepass.ui.scanner;

import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;
import com.example.securepass.databinding.FragmentScannerBinding;

import java.io.Serializable;

public class ScannerFragment extends Fragment {

    private FragmentScannerBinding binding;
    DBHelper dbHelper;

    private int userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScannerViewModel scannerViewModel =
                new ViewModelProvider(this).get(ScannerViewModel.class);

        binding = FragmentScannerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dbHelper = new DBHelper(requireContext());



        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            userId = mainActivity.getUserId();
        }

        LinearLayout weakPasswords = root.findViewById(R.id.weak_password);
        LinearLayout reusedPasswords = root.findViewById(R.id.reused_password);
        LinearLayout oldPasswords = root.findViewById(R.id.old_password);

        String accountsString = getResources().getString(R.string.accounts);

        TextView weakPasswordsCount = root.findViewById(R.id.weak_password_count);
        weakPasswordsCount.setText(String.valueOf(dbHelper.getWeakPasswordsCursorForUser(userId).getCount()) + ' ' + accountsString);

        if (dbHelper.getWeakPasswordsCursorForUser(userId).getCount() > 0) {
            weakPasswords.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
        }
        else {
            weakPasswords.setBackground(getResources().getDrawable(R.drawable.round_corner_green));
        }

        if (dbHelper.getOldPasswordsForUser(userId).getCount() > 0) {
            oldPasswords.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
        }
        else {
            oldPasswords.setBackground(getResources().getDrawable(R.drawable.round_corner_green));
        }

        if (dbHelper.getReusedPasswordsForUser(userId).getCount() > 0) {
            reusedPasswords.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
        }
        else {
            reusedPasswords.setBackground(getResources().getDrawable(R.drawable.round_corner_green));
        }

        TextView reusedPasswordsCount = root.findViewById(R.id.reused_password_count);
        reusedPasswordsCount.setText(String.valueOf(dbHelper.getReusedPasswordsForUser(userId).getCount()) + ' ' + accountsString);

        TextView oldPasswordsCount = root.findViewById(R.id.old_password_count);
        oldPasswordsCount.setText(String.valueOf(dbHelper.getOldPasswordsForUser(userId).getCount()) + ' ' + accountsString);

        oldPasswords.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", "old");

            NavController navController = Navigation.findNavController(v);
            navController.popBackStack(R.id.navigation_scanner, true);
            navController.navigate(R.id.navigation_old_passwords, bundle);
        });

        weakPasswords.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", "weak");

            NavController navController = Navigation.findNavController(v);
            navController.popBackStack(R.id.navigation_scanner, true);
            navController.navigate(R.id.navigation_weak_passwords, bundle);
        });

        reusedPasswords.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", "reused");

            NavController navController = Navigation.findNavController(v);
            navController.popBackStack(R.id.navigation_scanner, true);
            navController.navigate(R.id.navigation_reused_passwords, bundle);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}