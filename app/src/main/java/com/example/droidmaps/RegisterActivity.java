package com.example.droidmaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText name, password, email, apikey, mCnfPassword;
    private TextView lgnBtn;
    private Button registerbtn;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    private String userId;
    private GoogleSignInClient mGoogleSignInClient;
    private Button mGoogleSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_pass);
        //apikey=findViewById(R.id.text_apikey);
        registerbtn = findViewById(R.id.reg_button);
        mCnfPassword = findViewById(R.id.user_cnf_pass);
        lgnBtn = findViewById(R.id.txtLogIn);
        mGoogleSignin = findViewById(R.id.google_signIn);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        //if the user is currently loggedin then we donot need to display this activity

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailid = email.getText().toString().trim();
                String passwordstring = password.getText().toString().trim();
                final String username = name.getText().toString();
                String cnfPassword = mCnfPassword.getText().toString().trim();
                //final String apikeystored = apikey.getText().toString();
                //checking if the email field is not empty
                if (TextUtils.isEmpty(emailid)) {
                    email.setError("Email is a required field");
                    return;
                }
                //checking if the password field is not empty
                if (TextUtils.isEmpty(passwordstring)) {
                    password.setError("Password is required");
                    return;
                }
                //checking the length of the password is a minimum of 6 chars
                if (passwordstring.length() < 6) {
                    password.setError("Password should be minimum of 6 characters");
                    return;
                }
                //checking if both passwords match
                if (!passwordstring.equals(cnfPassword)) {
                    mCnfPassword.setError("Please make sure both passwords match");
                    return;
                }
                //registering the user
                fAuth.createUserWithEmailAndPassword(emailid, passwordstring).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if user is successfully created then redirected to main activity
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                            //retrieving the id from the firebase auth
//                            userId = fAuth.getCurrentUser().getUid();
                            //storing user details in the firestore
                            //to refer to the documents in the firestore
//                            DocumentReference documentReference = fstore.collection("users").document(userId);
//                            using hashmap to store data
//                            Map<String, Object> user = new HashMap<>();
//                            user.put("Name", username);
//                            user.put("Email", emailid);
                            //user.put("APIkey", apikeystored);
                            //inserting in the cloud database
//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(RegisterActivity.this, "User profile is created and added to database " + userId, Toast.LENGTH_SHORT).show();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(RegisterActivity.this, "An error occured.Please try again in a while", Toast.LENGTH_SHORT).show();
//                                }
//                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        //if error then display the error
                        else {
                            Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        lgnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 16);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 16) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(RegisterActivity.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            fireBaseAuthentication(acc);
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        } catch (ApiException e) {
            Toast.makeText(RegisterActivity.this, "Sign In Error", Toast.LENGTH_SHORT).show();
            Log.e("Sign In Error", e.getMessage());
            fireBaseAuthentication(null);
        }
    }

    private void fireBaseAuthentication(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //remove this toast afterwards
                    Toast.makeText(RegisterActivity.this, "Firebase SignIn Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Firebase SignIn Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}