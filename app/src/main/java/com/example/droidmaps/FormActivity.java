package com.example.droidmaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

public class FormActivity extends AppCompatActivity {
    public static final int REQUEST_CODE=1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
    }

    public void goToMapActivityForLocation(View view) {
        Intent intent=new Intent(this, MapActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
                if(resultCode== Activity.RESULT_OK){
                    String co_ordinates=data.getStringExtra("co-ordinates");
                    String arr[]=co_ordinates.split(" ");
                    String latitude=arr[0];
                    String longitude=arr[1];
                }
                else{
                    Log.i("Error","Activity canceled");
                }
        }
    }

    public void addDataToDatabase(View view) {
        //code of adding to the database to be added here

    }

}