package com.example.securepass;

import android.os.Bundle;

import android.view.MenuItem;
import android.widget.ListView;
import com.example.securepass.ui.folders.FoldersFragment;
import com.example.securepass.ui.passwords.PasswordsFragment;
import com.example.securepass.ui.profile.ProfileFragment;
import com.example.securepass.ui.scanner.ScannerFragment;
import com.example.securepass.ui.trash.TrashFragment;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.securepass.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.securepass.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve user ID from intent extras
        userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId != -1) {
            // User ID received successfully, you can use it as needed
            // For example, you can pass it to fragments or perform actions specific to the logged-in user
            Log.d("MainActivity", "User ID: " + userId);
        } else {
            // User ID not found in intent extras, handle this scenario accordingly
            Log.e("MainActivity", "User ID not found");
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_passwords, R.id.navigation_folders, R.id.navigation_trash, R.id.navigation_scanner, R.id.navigation_profile, R.id.navigation_single_password)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public int getUserId() {
        // Return the user_id here
        return userId;
    }
}