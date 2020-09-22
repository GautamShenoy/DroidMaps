package com.example.droidmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText name,password,email,apikey;
    TextView text;
    Button registerbtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.text_registerfullname);
        email=findViewById(R.id.text_registeremailid);
        password=findViewById(R.id.text_registerpassword);
        apikey=findViewById(R.id.text_apikey);
        registerbtn=findViewById(R.id.btn_register);
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        //if the user is currently loggedin then we donot need to display this activity
        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
        }
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailid=email.getText().toString().trim();
                String passwordstring=password.getText().toString().trim();
                final String username=name.getText().toString();
                final String apikeystored=apikey.getText().toString();
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
                //registering the user
                fAuth.createUserWithEmailAndPassword(emailid,passwordstring).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if user is successfully created then redirected to main activity
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                           //retrieving the id from the firebase auth
                            userId=fAuth.getCurrentUser().getUid();
                            //storing user details in the firestore
                            //to refer to the documents in the firestore
                            DocumentReference documentReference=fstore.collection("users").document(userId);
                            //using hashmap to store data
                            Map<String,Object> user=new HashMap<>();
                            user.put("Name",username);
                            user.put("Email",emailid);
                            user.put("APIkey",apikeystored);
                            //inserting in the cloud database
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "User profile is created and added to database "+userId, Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "An error occured.Please try again in a while", Toast.LENGTH_SHORT).show();
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        //if error then display the error
                        else{
                            Toast.makeText(RegisterActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void goToLoginActivity(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }
}