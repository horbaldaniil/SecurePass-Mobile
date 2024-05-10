package com.example.securepass;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.securepass.ui.folders.FoldersFragment;
import com.example.securepass.ui.passwords.PasswordsFragment;
import com.example.securepass.ui.profile.ProfileFragment;
import com.example.securepass.ui.scanner.ScannerFragment;
import com.example.securepass.ui.trash.TrashFragment;
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

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_passwords, R.id.navigation_folders, R.id.navigation_trash, R.id.navigation_scanner, R.id.navigation_profile, R.id.navigation_single_password)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}