package com.example.securepass.ui.signup;

import android.content.Intent;
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
import com.example.securepass.ui.login.LoginActivity;

public class SignUpActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        dbHelper = new DBHelper(this);

        EditText editTextEmail = findViewById(R.id.email_input);
        EditText editTextPassword = findViewById(R.id.password_input);
        EditText editTextConfirmPassword = findViewById(R.id.confirm_password_input);
        Button buttonSignUp = findViewById(R.id.signup_button);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Please enter email, password, and confirm password", Toast.LENGTH_SHORT).show();
                }

                if (!password.equals(confirmPassword)) { // Check if passwords match
                    Toast.makeText(SignUpActivity.this, "Passwords do not match. Please re-enter", Toast.LENGTH_SHORT).show();
                }

                long userId = dbHelper.addUser(email, password);

                if (userId != -1) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();
                } else {
                    // User addition failed, show error message
                    Toast.makeText(SignUpActivity.this, "Failed to sign up. User already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView textViewLogin = findViewById(R.id.login_link);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
