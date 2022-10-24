package com.example.travelbud;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    TextInputEditText resetEmail;
    TextView backToLogin;
    Button btnReset;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        resetEmail = (TextInputEditText) findViewById(R.id.resetEmail);
        backToLogin = findViewById(R.id.backToLogin);
        btnReset = findViewById(R.id.btnReset);

        mAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(view -> {
            resetPassword();
        });

        backToLogin.setOnClickListener(view -> {
            startActivity(new Intent(ResetPassActivity.this, LoginActivity.class));
        });
    }

    private void resetPassword() {
        String email = resetEmail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            resetEmail.setError("Email cannot be empty");
            resetEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            resetEmail.setError("Please provide valid email");
            resetEmail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPassActivity.this, "Please check your email to reset your" +
                            " password", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPassActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(ResetPassActivity.this, "Something wrong happened. Please try " +
                            "again." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}