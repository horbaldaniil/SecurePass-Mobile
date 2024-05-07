package com.example.securepass.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.securepass.DBHelper;
import com.example.securepass.MainActivity;
import com.example.securepass.R;
import com.example.securepass.ui.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);

        EditText editTextEmail = findViewById(R.id.email_input);
        EditText editTextPassword = findViewById(R.id.password_input);

        Button buttonConfirm = findViewById(R.id.login_button);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int userId = dbHelper.getUserId(email, password);
                if (userId != -1) {
                    // Credentials are valid, start MainActivity with user ID
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", userId); // Pass user ID to MainActivity
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login. Please check your credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView textViewSignUp = findViewById(R.id.signup_link);

        textViewSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}