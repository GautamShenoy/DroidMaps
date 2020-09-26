package com.example.droidmaps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class LocationAdders extends AppCompatActivity {

    private List<LatLng> locationPoints;
    private Button addPoint;
    private Button submitServer;
    private TextView locationPointsDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_adders);
        locationPoints = new ArrayList<>();
        addPoint = findViewById(R.id.add_point);
        submitServer = findViewById(R.id.submit_server);
        locationPointsDisplay = findViewById(R.id.txtInp);

        addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LocationAdders.this, MapActivity.class), 1);

            }
        });

        submitServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationPointsDisplay.getText().toString().equals("")) {
                    Toast.makeText(LocationAdders.this, "You need to add valid Locations to list first", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LocationAdders.this);
                    builder.setTitle("Consent Check")
                            .setMessage("Are you sure you want to proceed ? ")
                            .setNegativeButton("NO", null)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(LocationAdders.this, "Server Updated with the selected locations", Toast.LENGTH_SHORT).show();
                                    //write code for adding elements to sever
                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == 1 ) {
            if (resultCode == RESULT_OK) {

                double lat = data.getDoubleExtra("Latitude",0.0);
                double lon = data.getDoubleExtra("Longitude",0.0);

                locationPoints.add(new LatLng(lat, lon));

                String locations = "";

                for (int i = 0 ; i < locationPoints.size() ; i++) {
                    locations += "Point " + (i + 1) + " : Latitude : " + lat + " , Longitude : " + lon + " \n\n";
                }

                locationPointsDisplay.setText(locations);

            }
        }
    }
}