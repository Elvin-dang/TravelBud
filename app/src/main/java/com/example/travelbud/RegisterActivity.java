package com.example.travelbud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText regEmail, regUsername;
    TextInputEditText regPassword, repeatPassword;
    TextView loginHere;
    Button btnRegister;

    FirebaseAuth mAuth;

    //Login automatically if not logout
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regEmail = findViewById(R.id.etRegEmail);
        regPassword = findViewById(R.id.etRegPass);
        repeatPassword = findViewById(R.id.etRepeatPass);
        regUsername = findViewById(R.id.etUserName);
        loginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        loginHere.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void createUser(){
        String email = regEmail.getText().toString();
        String password = regPassword.getText().toString();
        String username = regUsername.getText().toString();
        String repPass = repeatPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            regUsername.setError("Username cannot be empty");
            regUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)){
            regEmail.setError("Email cannot be empty");
            regEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            regEmail.setError("Please provide valid email");
            regEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            regPassword.setError("Password cannot be empty");
            regPassword.requestFocus();
            return;
        }

        if (password.length()<6) {
            regPassword.setError("Minimum password length should be 6 characters");
            regPassword.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(repPass)) {
            repeatPassword.setError("Please repeat the password");
            repeatPassword.requestFocus();
            return;
        }

        if (!repPass.matches(password)){
            repeatPassword.setError("It doesn't match with the password");
            repeatPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    TravelBudUser user = new TravelBudUser(username, email, new ArrayList<Trip>(), new ArrayList<TravelBudUser>());
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}