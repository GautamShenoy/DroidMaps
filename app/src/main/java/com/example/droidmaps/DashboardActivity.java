package com.example.droidmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class DashboardActivity extends AppCompatActivity {
    TextView name,email,apikey;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        name=findViewById(R.id.text_dashboardname);
        email=findViewById(R.id.text_dashboardemail);
        apikey=findViewById(R.id.text_dashboardapikey);
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        userId=fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference=fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            apikey.setText(documentSnapshot.getString("APIkey"));
            name.setText(documentSnapshot.getString("Name"));
            email.setText(documentSnapshot.getString("Email"));
            }
        });
    }

    public void addDataToFirestore(View view) {
        startActivity(new Intent(DashboardActivity.this,FormActivity.class));
    }

    public void retriveDataFromFirestore(View view) {
        startActivity(new Intent(DashboardActivity.this,DisplayRetrivedDataActivity.class));
    }

    public void logout(View view) {
    }
}