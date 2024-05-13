package com.example.securepass;

import android.content.Context;
import android.os.Bundle;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

        EdgeToEdge.enable(this);
        com.example.securepass.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId != -1) {
            Log.d("MainActivity", "User ID: " + userId);
        } else {
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
        return getIntent().getIntExtra("USER_ID", -1);
    }
}