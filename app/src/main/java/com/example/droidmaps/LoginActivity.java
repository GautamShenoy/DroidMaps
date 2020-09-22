package com.example.droidmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button btn_login;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.text_loginemail);
        password=findViewById(R.id.text_loginpassword);
        fAuth=FirebaseAuth.getInstance();
        btn_login=findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailid=email.getText().toString();
                String passwordstring=password.getText().toString();
                //checking if the email field is not empty
                if(TextUtils.isEmpty(emailid)){
                    email.setError("Email is a required field");
                    return;
                }
                //checking if the password field is not empty
                if(TextUtils.isEmpty(passwordstring)){
                    password.setError("Password is required");
                    return;
                }
                //checking the length of the password is a minimum of 6 chars
                if(passwordstring.length()<6){
                    password.setError("Password should be minimum of 6 characters");
                    return;
                }
                fAuth.signInWithEmailAndPassword(emailid,passwordstring).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void goToRegister(View view) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}